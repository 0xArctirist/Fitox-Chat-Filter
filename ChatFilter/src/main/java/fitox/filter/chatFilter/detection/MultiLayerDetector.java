package fitox.filter.chatFilter.detection;

import fitox.filter.chatFilter.config.FilterConfig;
import fitox.filter.chatFilter.util.TextNormalizer;

import java.util.regex.Pattern;

public class MultiLayerDetector {
    
    private final FilterConfig config;
    private final WhitelistManager whitelist;

    private final java.util.Set<String> slursSet;
    private final java.util.Set<String> threatsSet;
    private final java.util.Set<String> sexualSet;
    private final java.util.Set<String> advertisingSet;

    private final Pattern singleLetterPattern;
    private final Pattern repeatedCharPattern;

    private final SlidingWindowDetector slidingWindow;
    private final ReputationManager reputationManager;

    private static final int FUZZY_MATCH_THRESHOLD = 2;
    
    public MultiLayerDetector(FilterConfig config) {
        this.config = config;
        this.whitelist = new WhitelistManager();

        this.slursSet = new java.util.HashSet<>(config.getSlurs());
        this.threatsSet = new java.util.HashSet<>(config.getThreats());
        this.sexualSet = new java.util.HashSet<>(config.getSexualContent());
        this.advertisingSet = new java.util.HashSet<>(config.getAdvertising());

        this.singleLetterPattern = Pattern.compile("(?:^|\\s)([a-z])(?:\\s+([a-z]))+(?:\\s|$)");
        this.repeatedCharPattern = Pattern.compile(".*(.)\\1{4,}.*");

        this.slidingWindow = new SlidingWindowDetector(config.getSlurs());
        this.reputationManager = new ReputationManager();
    }

    public ReputationManager getReputationManager() {
        return reputationManager;
    }

    public DetectionResult analyze(String message, String playerName, java.util.UUID playerId) {
        DetectionResult.Builder result = new DetectionResult.Builder()
                .originalMessage(message);

        if (message.length() < 3) {
            reputationManager.rewardCleanMessage(playerId);
            return result.blocked(false).totalScore(0).build();
        }

        String normalized = TextNormalizer.normalize(message);
        String aggressive = TextNormalizer.normalizeAggressive(message);
        String superAggressive = TextNormalizer.normalizeSuperAggressive(message);
        result.normalizedMessage(normalized);

        boolean wl1 = whitelist.isWhitelisted(message.toLowerCase());
        boolean wl2 = whitelist.isWhitelisted(normalized);
        boolean wl3 = whitelist.isWhitelisted(aggressive);
        
        if (wl1 || wl2 || wl3) {
            reputationManager.rewardCleanMessage(playerId);
            return result.blocked(false).totalScore(0).build();
        }
        
        int score = 0;

        score += checkExactMatch(normalized, result);

        if (score >= 50) {
            applyReputationAndFinalize(result, score, playerId);
            return result.build();
        }

        score += checkExactMatch(superAggressive, result);

        if (score >= 50) {
            applyReputationAndFinalize(result, score, playerId);
            return result.build();
        }

        score += checkContains(normalized, result);
        
        if (score >= 50) {
            applyReputationAndFinalize(result, score, playerId);
            return result.build();
        }

        if (message.contains(" ")) {
            score += checkSingleLetterSpacing(message, result);
            
            if (score >= 50) {
                applyReputationAndFinalize(result, score, playerId);
                return result.build();
            }
        }

        if (score > 0 || containsSuspiciousPatterns(normalized)) {
            score += checkRegex(normalized, result);
        }

        score += analyzeContext(normalized, result);

        if (score < 50) {
            score += checkSpam(message, result);
        }

        if (score >= 20 && score < 50) {
            score += checkFuzzyMatch(normalized, aggressive, result);
        }

        if (score >= 20 && score < 50) {
            score += checkSlidingWindow(aggressive, result);
        }

        applyReputationAndFinalize(result, score, playerId);
        return result.build();
    }

    private void applyReputationAndFinalize(DetectionResult.Builder result, int score, java.util.UUID playerId) {
        double reputationModifier = reputationManager.getScoreModifier(playerId);
        score = (int) (score * reputationModifier);
        
        result.totalScore(score);

        if (score >= 50) {
            result.blocked(true).reason("violation flagged (score: " + score + ")");

            boolean severe = score >= 80;
            reputationManager.penalizeViolation(playerId, severe);
        } else {
            reputationManager.rewardCleanMessage(playerId);
        }
    }

    private String collapseRepeats(String word) {
        return word.replaceAll("(.)\\1+", "$1");
    }

    private boolean wordMatchesSet(String word, java.util.Set<String> set) {
        if (set.contains(word)) return true;

        String collapsed = collapseRepeats(word);
        if (set.contains(collapsed)) return true;
        for (String entry : set) {
            if (entry.length() >= 4) {
                if (word.contains(entry)) return true;
                if (collapsed.contains(entry)) return true;
            }
        }
        return false;
    }

    private int checkExactMatch(String text, DetectionResult.Builder result) {
        int score = 0;
        boolean slurFound = false;
        boolean threatFound = false;
        boolean sexualFound = false;
        boolean advertisingFound = false;

        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.length() < 3) continue;
            if (wordMatchesSet(word, slursSet)) {
                result.addViolation(ViolationType.SLUR);
                score += 50;
                slurFound = true;
                break;
            }
        }
        if (!slurFound) {
            for (String slur : slursSet) {
                if (slur.contains(" ")) {
                    if (text.contains(slur)) {
                        result.addViolation(ViolationType.SLUR);
                        score += 50;
                        slurFound = true;
                        break;
                    }
                }
            }
        }
        for (String threat : threatsSet) {
            if (text.contains(threat)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 80;
                threatFound = true;
                break;
            }
        }
        if (!threatFound) {
            for (String word : words) {
                if (word.length() < 3) continue;
                if (wordMatchesSet(word, threatsSet)) {
                    result.addViolation(ViolationType.DEATH_THREAT);
                    score += 80;
                    threatFound = true;
                    break;
                }
            }
        }
        for (String word : words) {
            if (word.length() < 3) continue;
            if (wordMatchesSet(word, sexualSet)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 50;
                sexualFound = true;
                break;
            }
        }
        if (!sexualFound) {
            for (String sexual : sexualSet) {
                if (sexual.contains(" ")) {
                    if (text.contains(sexual)) {
                        result.addViolation(ViolationType.SEXUAL_CONTENT);
                        score += 50;
                        sexualFound = true;
                        break;
                    }
                }
            }
        }
        if (!advertisingFound) {
            for (String ad : advertisingSet) {
                if (text.contains(ad)) {
                    result.addViolation(ViolationType.ADVERTISING);
                    score += 60;
                    advertisingFound = true;
                    break;
                }
            }
        }

        return score;
    }
    

    private int checkContains(String text, DetectionResult.Builder result) {
        int score = 0;

        String[] threatPhrases = {
            "kill yourself", "kys", "kill your self",
            "go die", "i will kill you", "im going to kill you",
            "youre dead", "you are dead", "ill kill you",
            "cut yourself", "you should cut", "u should cut"
        };
        
        for (String phrase : threatPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 80;
                break;
            }
        }

        String[] hatePhrases = {
            "i hope you die", "you should die", "go commit",
            "end yourself", "hang yourself",
            "i hope u die", "u should die", "hope you die", "hope u die"
        };
        
        for (String phrase : hatePhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.EXTREME_HATE);
                score += 100;
                break;
            }
        }

        String[] indirectThreats = {
            "commit not alive", "commit die", "commit oof",
            "uninstall life", "delete yourself", "remove yourself",
            "log off life", "alt f4 life", "ctrl alt delete yourself",
            "go commit", "commit toaster bath", "commit rope",
            "commit sudoku", "commit sewer slide", "commit unlive",
            "neck rope", "rope yourself", "toaster bath",
            "drink bleach", "eat bleach", "eat tide pods"
        };
        
        for (String threat : indirectThreats) {
            if (text.contains(threat)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 80;
                break;
            }
        }

        String[] grapeContextPhrases = {
            "grape you", "grape u", "grape her", "grape him", "grape them",
            "graping you", "graping u", "graping her", "graping him",
            "graped you", "graped u", "graped her", "graped him",
            "gonna grape", "going to grape", "imma grape", "ima grape",
            "can i grape", "wanna grape", "let me grape",
            "ill grape", "i will grape", "im gonna grape"
        };
        
        for (String phrase : grapeContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 90;
                break;
            }
        }

        String[] unaliveContextPhrases = {
            "unalive you", "unalive u", "unalive yourself", "unalive urself",
            "gonna unalive", "going to unalive", "imma unalive", "ima unalive",
            "ill unalive", "i will unalive", "im gonna unalive",
            "make you unalive", "make u unalive"
        };
        
        for (String phrase : unaliveContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 85;
                break;
            }
        }

        String[] diddleContextPhrases = {
            "diddle you", "diddle u", "diddle your", "diddle ur",
            "diddling you", "diddling u", "diddled you", "diddled u",
            "gonna diddle", "going to diddle", "imma diddle", "ima diddle",
            "can i diddle", "wanna diddle", "let me diddle",
            "ill diddle", "i will diddle", "im gonna diddle"
        };
        
        for (String phrase : diddleContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 85;
                break;
            }
        }

        String[] touchKidsContextPhrases = {
            "touch kids", "touch children", "touch little",
            "touching kids", "touching children", "touching little",
            "touched kids", "touched children", "touched little",
            "molest kids", "molest children", "molest little",
            "molesting kids", "molesting children",
            "diddle kids", "diddle children", "diddle little",
            "diddling kids", "diddling children"
        };
        
        for (String phrase : touchKidsContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 100;
                break;
            }
        }

        String[] groomContextPhrases = {
            "groom you", "groom u", "groom her", "groom him",
            "grooming you", "grooming u", "grooming her", "grooming him",
            "groomed you", "groomed u", "groomed her", "groomed him",
            "gonna groom", "going to groom", "imma groom",
            "groom kids", "groom children", "grooming kids", "grooming children"
        };
        
        for (String phrase : groomContextPhrases) {
            if (text.contains(phrase)) {
                if (!text.contains("dog") && !text.contains("cat") && !text.contains("pet") && 
                    !text.contains("horse") && !text.contains("animal")) {
                    result.addViolation(ViolationType.SEXUAL_CONTENT);
                    score += 95;
                    break;
                }
            }
        }

        String[] chokeContextPhrases = {
            "choke you out", "choke u out", "choke you to death", "choke u to death",
            "choke you till", "choke u till", "choke you until", "choke u until",
            "gonna choke you", "going to choke you", "imma choke you", "ima choke you",
            "ill choke you", "i will choke you", "im gonna choke you",
            "choke me daddy", "choke me harder"
        };
        
        for (String phrase : chokeContextPhrases) {
            if (text.contains(phrase)) {
                if (!text.contains("food") && !text.contains("eating") && !text.contains("drink")) {
                    result.addViolation(ViolationType.DEATH_THREAT);
                    score += 80;
                    break;
                }
            }
        }

        String[] gasContextPhrases = {
            "gas the", "gassing the", "gassed the",
            "gas them", "gassing them", "gassed them",
            "gas chamber", "gas chambers"
        };
        
        for (String phrase : gasContextPhrases) {
            if (text.contains(phrase)) {
                if (!text.contains("car") && !text.contains("vehicle") && !text.contains("fuel") && 
                    !text.contains("station") && !text.contains("tank") && !text.contains("pump")) {
                    result.addViolation(ViolationType.EXTREME_HATE);
                    score += 100;
                    break;
                }
            }
        }

        String[] neckContextPhrases = {
            "neck yourself", "neck urself", "neck you", "neck u",
            "gonna neck", "going to neck", "imma neck", "ima neck",
            "ill neck", "i will neck", "im gonna neck"
        };
        
        for (String phrase : neckContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 85;
                break;
            }
        }

        String[] oofContextPhrases = {
            "oof yourself", "oof urself", "go oof", "commit oof"
        };
        
        for (String phrase : oofContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.DEATH_THREAT);
                score += 80;
                break;
            }
        }

        String[] sexualContextPhrases = {
            "nut inside", "nut in you", "nut in your", "nut in u", "nut in ur",
            "nuh inside", "nuh in you", "nuh in your", "nuh in u", "nuh in ur",
            "nutt inside", "nutt in you", "nutt in your",
            "nut on you", "nut on your", "nut on u", "nut on ur",
            "nuh on you", "nuh on your", "nuh on u", "nuh on ur",
            "gonna nut", "going to nut", "about to nut", "boutta nut",
            "gonna nuh", "going to nuh", "about to nuh", "boutta nuh",
            "imma nut", "ima nut", "im gonna nut", "im going to nut",
            "imma nuh", "ima nuh", "im gonna nuh", "im going to nuh",
            "make me nut", "makes me nut", "making me nut",
            "make me nuh", "makes me nuh", "making me nuh",
            "nut for you", "nut for u", "nut 4 you", "nut 4 u",
            "nuh for you", "nuh for u", "nuh 4 you", "nuh 4 u",
            "wanna nut", "want to nut", "need to nut",
            "wanna nuh", "want to nuh", "need to nuh",
            "can i nut", "can i nuh", "can we nut", "can we nuh",
            "let me nut", "let me nuh", "lemme nut", "lemme nuh",

            "cum inside", "cum in you", "cum in your", "cum in u", "cum in ur",
            "coom inside", "coom in you", "coom in your", "coom in u",
            "come inside", "come in you", "come in your", "come in u",
            "kum inside", "kum in you", "kum in your", "kum in u",
            "cum on you", "cum on your", "cum on u", "cum on ur",
            "coom on you", "coom on your", "coom on u",
            "come on you", "come on your", "kum on you", "kum on your",
            "gonna cum", "going to cum", "about to cum", "boutta cum",
            "gonna coom", "going to coom", "about to coom", "boutta coom",
            "gonna come", "going to come", "gonna kum", "going to kum",
            "imma cum", "ima cum", "im gonna cum", "im going to cum",
            "imma coom", "ima coom", "im gonna coom", "im going to coom",
            "imma come", "ima come", "imma kum", "ima kum",
            "make me cum", "makes me cum", "making me cum",
            "make me coom", "makes me coom", "making me coom",
            "make me come", "makes me come", "make me kum", "makes me kum",
            "cum for you", "cum for u", "cum 4 you", "cum 4 u",
            "coom for you", "coom for u", "come for you", "kum for you",
            "can i cum", "can i coom", "can i come", "can i kum",
            "let me cum", "let me coom", "let me come", "let me kum",

            "bust inside", "bust in you", "bust in your", "bust in u",
            "buss inside", "buss in you", "buss in your", "buss in u",
            "bust on you", "bust on your", "bust on u",
            "buss on you", "buss on your", "buss on u",
            "gonna bust", "going to bust", "imma bust", "ima bust",
            "gonna buss", "going to buss", "imma buss", "ima buss",
            "can i bust", "can i buss", "let me bust", "let me buss",

            "cream inside", "cream in you", "cream in your",
            "creem inside", "creem in you", "creem in your",
            "kream inside", "kream in you", "kream in your",
            "cream on you", "cream on your",
            "creem on you", "creem on your", "kream on you", "kream on your",
            "gonna cream", "imma cream", "ima cream",
            "gonna creem", "imma creem", "ima creem",
            "gonna kream", "imma kream", "ima kream",
            "can i cream", "can i creem", "can i kream",

            "load inside", "load in you", "load in your",
            "lode inside", "lode in you", "lode in your",
            "load on you", "load on your", "lode on you", "lode on your",
            "blow my load", "blow a load", "drop my load",
            "blow my lode", "blow a lode", "drop my lode",
            "can i load", "can i lode", "let me load", "let me lode",

            "jizz inside", "jizz in you", "jizz on you",
            "jiz inside", "jiz in you", "jiz on you",
            "jiss inside", "jiss in you", "jiss on you",
            "gonna jizz", "imma jizz", "can i jizz",
            "gonna jiz", "imma jiz", "can i jiz",

            "spunk inside", "spunk in you", "spunk on you",
            "gonna spunk", "imma spunk", "can i spunk"
        };
        
        for (String phrase : sexualContextPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 80;
                break;
            }
        }

        String[] crackContextPhrases = {
            "crack you", "crack u", "crack your", "crack ur",
            "crak you", "crak u", "crak your", "crak ur",
            "krak you", "krak u", "krak your", "krak ur",
            "imma crack", "ima crack", "im gonna crack", "gonna crack you",
            "imma crak", "ima crak", "im gonna crak", "gonna crak you",
            "imma krak", "ima krak", "im gonna krak", "gonna krak you",
            "crack your head", "crack your skull", "crack ur head", "crack ur skull",
            "crak your head", "crak your skull", "krak your head", "krak your skull",
            "crack you open", "crack u open", "crak you open", "krak you open",

            "can i crack", "can we crack", "wanna crack", "want to crack",
            "can i crak", "can we crak", "wanna crak", "want to crak",
            "can i krak", "can we krak", "wanna krak", "want to krak",
            "let me crack", "lemme crack", "gonna crack that",
            "let me crak", "lemme crak", "gonna crak that",
            "let me krak", "lemme krak", "gonna krak that"
        };
        
        for (String phrase : crackContextPhrases) {
            if (text.contains(phrase)) {
                if (!text.contains("code") && !text.contains("puzzle") && 
                    !text.contains("game") && !text.contains("chest") &&
                    !text.contains("safe") && !text.contains("lock") &&
                    !text.contains("egg") && !text.contains("shell")) {
                    result.addViolation(ViolationType.DEATH_THREAT);
                    score += 70;
                    break;
                }
            }
        }
        
        // sexual things
        String[] harassmentPhrases = {
            "sit on my face", "sit on me daddy", "ride me hard", "ride my face",
            "sitt on my face", "sitt on me daddy", "ryde me hard", "ryde my face",
            "suck my dick", "suck my cock", "lick my pussy", "lick me down",
            "succ my dick", "succ my cock", "suk my dick", "suk my cock", "lik my pussy",
            "touch my dick", "touch my cock", "touch my pussy", "grab my dick", "grab my cock", "grab my pussy",
            "tuch my dick", "tuch my cock", "grabb my dick", "grabb my cock",
            "feel my dick", "feel my cock", "stroke my dick", "stroke my cock",
            "feal my dick", "feal my cock", "stroak my dick", "stroak my cock",
            "bend over for me", "get on your knees for me", "on your knees bitch",
            "bnd over for me", "get on ur knees for me", "on ur knees bitch",
            "spread your legs", "spread ur legs", "open your legs for me", "open ur legs for me",
            "spred your legs", "spred ur legs", "opn your legs for me", "opn ur legs for me",
            "take off your clothes", "take off ur clothes", "strip for me", "get naked for me",
            "tak off your clothes", "tak off ur clothes", "strip 4 me", "get nakey for me",
            "show me your tits", "show me your pussy", "show me ur tits", "show me ur pussy",
            "sho me your tits", "sho me ur pussy", "lemme see your tits", "lemme see ur pussy",
            "send nudes", "send nude pics", "send nude pictures",
            "snd nudes", "snd nude pics", "send naked pics",
            
            // threatening/sexual "gonna/going to" phrases
            "gonna touch you", "going to touch you", "imma touch you", "ima touch you",
            "ill touch you", "i will touch you", "im gonna touch you", "im going to touch you",
            "gonna grab you", "going to grab you", "imma grab you", "ima grab you",
            "ill grab you", "i will grab you", "im gonna grab you", "im going to grab you",
            "gonna feel you", "going to feel you", "imma feel you", "ima feel you",
            "ill feel you", "i will feel you", "im gonna feel you", "im going to feel you",
            "gonna grope you", "going to grope you", "imma grope you", "ima grope you",
            "ill grope you", "i will grope you", "im gonna grope you", "im going to grope you",
            "gonna fondle you", "going to fondle you", "imma fondle you", "ima fondle you",
            "gonna violate you", "going to violate you", "imma violate you", "ima violate you",
            "gonna assault you", "going to assault you", "imma assault you", "ima assault you",
            "gonna force you", "going to force you", "imma force you", "ima force you",
            "ill force you", "i will force you", "im gonna force you", "im going to force you",
            "gonna make you", "going to make you", "imma make you", "ima make you",
            "ill make you", "i will make you", "im gonna make you", "im going to make you"
        };
        
        for (String phrase : harassmentPhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 75;
                break;
            }
        }

        String[] tapPhrases = {
            "tap that", "tap dat", "tap her", "tap him",
            "tapp that", "tapp dat", "tapp her", "tapp him",
            "can i tap", "can we tap", "wanna tap", "want to tap",
            "gonna tap", "going to tap", "imma tap", "ima tap",
            "let me tap", "lemme tap", "bout to tap", "boutta tap"
        };
        
        for (String phrase : tapPhrases) {
            if (text.contains(phrase)) {
                // checks false positives
                if (!text.contains("button") && !text.contains("sign") &&
                        !text.contains("block") && !text.contains("screen") &&
                        !text.contains("door") && !text.contains("chest") &&
                        !text.contains("lever") && !text.contains("pressure") &&
                        !text.contains("plate") && !text.contains("mob") &&
                        !text.contains("entity") && !text.contains("npc")) {
                    result.addViolation(ViolationType.SEXUAL_CONTENT);
                    score += 70;
                    break;
                }
            }
        }
        // these are only flagged if they appear truly standalone, not as part of common word combinations
        String[] shortAbbreviations = {
            "smd", "emp", "eyp", "emd"
        };
        // to prevent false positives
        String[] commonWords = {
            "the", "they", "them", "their", "there", "these", "those", "then", "than",
            "pick", "picky", "picked", "picking", "picker",
            "temp", "temporary", "temperature", "temple", "temper", "attempt",
            "hemp", "redemption", "preempt", "exempt",
            "deem", "redeem", "deemed"
        };

        for (String abbr : shortAbbreviations) {
            String[] words = text.split("[\\s_]+");
            for (String word : words) {
                if (word.equals(abbr)) {
                    boolean isSafeContext = false;
                    for (String commonWord : commonWords) {
                        if (text.contains(commonWord + " " + abbr) || 
                            text.contains(abbr + " " + commonWord) ||
                            text.contains(commonWord + "_" + abbr) ||
                            text.contains(abbr + "_" + commonWord)) {
                            isSafeContext = true;
                            break;
                        }
                    }

                    int commonWordCount = 0;
                    for (String commonWord : commonWords) {
                        if (text.contains(commonWord)) {
                            commonWordCount++;
                        }
                    }
                    if (commonWordCount >= 3) {
                        isSafeContext = true;
                    }
                    
                    if (!isSafeContext) {
                        result.addViolation(ViolationType.SEXUAL_CONTENT);
                        score += 75;
                        break;
                    }
                }
            }
            
            if (score > 0) break;
        }
        
        String[] suggestivePhrases = {
            "s m d", "sm d", "s md",
            "e m p", "em p", "e mp",
            "e y p", "ey p", "e yp",
            "e m d", "em d", "e md",

            "suck me off", "suck me dry", "suck my dick", "suck my cock",
            "succ me off", "succ me dry", "succ my dick", "succ my cock",
            "suk me off", "suk me dry", "suk my dick", "suk my cock",
            "let me suck you", "let me suck your", "lemme suck you", "lemme suck your",
            "can i suck you", "can i suck your", "wanna suck you", "wanna suck your",
            "gonna suck you off", "gonna suck your", "imma suck you off", "imma suck your",
            "suck you off", "suck u off", "suck your dick", "suck your cock",
            "succ you off", "succ u off", "succ your dick", "succ your cock",
            "suk you off", "suk u off", "suk your dick", "suk your cock",

            "eat me out", "eat my pussy", "eat my ass",
            "let me eat you out", "let me eat your pussy", "lemme eat you out", "lemme eat your pussy",
            "can i eat you out", "can i eat your pussy", "wanna eat you out", "wanna eat your pussy",
            "gonna eat you out", "gonna eat your pussy", "imma eat you out", "imma eat your pussy",
            "eat you out", "eat u out", "eat your pussy", "eat your ass",
            "eat that pussy", "eat dat pussy", "eat that ass", "eat dat ass",

            "lick me down there", "lick my pussy", "lick my ass", "lick your lips",
            "let me lick you down there", "let me lick your pussy", "lemme lick you down there", "lemme lick your pussy",
            "can i lick you down there", "can i lick your pussy", "wanna lick you down there", "wanna lick your pussy",
            "gonna lick you down there", "gonna lick your pussy", "imma lick you down there", "imma lick your pussy",
            "lick you out", "lick u out", "lick that pussy", "lick dat pussy",
            "lick it up", "lik my pussy", "lik your pussy",

            "taste me down there", "taste my pussy", "taste your own",
            "let me taste you down there", "let me taste your pussy", "lemme taste you down there", "lemme taste your pussy",
            "can i taste you down there", "can i taste your pussy", "wanna taste you down there", "wanna taste your pussy",
            "taste you down there", "taste that pussy", "taste dat pussy",

            "can i sniff you", "can i sniff your", "wanna sniff you", "wanna sniff your",
            "let me sniff you", "let me sniff your", "lemme sniff you", "lemme sniff your",
            "gonna sniff you", "gonna sniff your", "imma sniff you", "imma sniff your",
            "sniff that pussy", "sniff your pussy", "sniff ur pussy", "sniff you down there",
            "snort you", "snort your pussy",

            "does it jiggle", "do they jiggle", "does that jiggle",
            "make it jiggle", "make them jiggle", "make that jiggle",
            "can i jiggle", "wanna jiggle", "let me jiggle",
            "jiggle for me", "jiggle your tits", "jiggle your ass",

            "does it vibrate", "does that vibrate", "do they vibrate",
            "make it vibrate", "make that vibrate", "make them vibrate",
            "can i vibrate", "wanna vibrate", "let me vibrate",
            "vibrate for me",

            "does it bounce", "do they bounce", "does that bounce",
            "make it bounce", "make them bounce", "make that bounce",
            "can i bounce on you", "wanna bounce on you", "let me bounce on you",
            "bounce for me", "bounce your tits", "bounce your ass",

            "shake it for me", "shake that for me", "shake them for me",
            "shake your tits", "shake your ass", "shake that ass",
            "shake those tits", "shake dat ass",

            "can i squeeze you", "wanna squeeze you", "let me squeeze you",
            "squeeze your tits", "squeeze ur tits", "squeeze your ass", "squeeze ur ass",
            "squeeze them tits", "squeeze that ass", "squeeze dat ass",

            "clap them cheeks", "clap those cheeks", "clap that ass",
            "make it clap", "make them clap", "make that clap",
            "clap your cheeks", "clap ur cheeks",

            "can i smash you", "wanna smash you", "let me smash you",
            "gonna smash you", "imma smash you", "ima smash you",
            "smash that pussy", "smash you hard", "smash her pussy", "smash him hard",

            "can i hit you", "wanna hit you", "let me hit you",
            "gonna hit you", "imma hit you", "ima hit you",
            "hit that pussy", "hit it raw", "hit from behind", "hit you from behind",

            "can i pound you", "wanna pound you", "let me pound you",
            "gonna pound you", "imma pound you", "pound that pussy",
            "pound you hard", "pound her pussy", "pound him hard",

            "motorboat", "motor boat", "can i motorboat",

            "grind on me baby", "grind on my dick", "can i grind on you",
            "wanna grind on you", "let me grind on you", "grinding on your",

            "can i hump you", "wanna hump you", "let me hump you",
            "gonna hump you", "imma hump you", "hump you like",

            "can i plow you", "wanna plow you", "gonna plow you",
            "imma plow you", "plow you hard", "plow that pussy",

            "can i rail you", "wanna rail you", "gonna rail you",
            "imma rail you", "rail you hard", "rail that pussy",

            "lay pipe in you", "laying pipe in you", "lay the pipe in you",
            "give you pipe hard", "give u pipe hard",

            "can i breed you", "wanna breed you", "gonna breed you",
            "imma breed you", "breed you like", "breed that pussy",

            "stuff you full", "stuff your pussy", "stuff ur pussy",
            "gonna stuff you full", "imma stuff you full",

            "fill you up with cum", "fill u up with cum", "fill your pussy",
            "fill ur pussy", "gonna fill you up", "imma fill you up"
        };
        
        for (String phrase : suggestivePhrases) {
            if (text.contains(phrase)) {
                result.addViolation(ViolationType.SEXUAL_CONTENT);
                score += 75;
                break;
            }
        }
        
        return score;
    }

    private int checkRegex(String text, DetectionResult.Builder result) {
        int score = 0;

        Pattern[] patterns = {
            Pattern.compile("\\bk+i+l+l+\\s*y+o+u+r+s+e+l+f+\\b"),
            Pattern.compile("\\bn+i+g+a*\\b"),
            Pattern.compile("\\bf+a+g+o*t*\\b"),
            Pattern.compile("\\br+e+t+a+r+d+\\b"),
        };
        
        for (Pattern pattern : patterns) {
            if (pattern.matcher(text).find()) {
                result.addViolation(ViolationType.SLUR);
                score += 60;
                break;
            }
        }
        
        return score;
    }

    private int analyzeContext(String text, DetectionResult.Builder result) {
        int score = 0;

        if (text.contains("kill") && (text.contains("you") || text.contains("yourself"))) {
            if (!text.contains("in game") && !text.contains("in pvp") && 
                !text.contains("the boss") && !text.contains("the dragon") &&
                !text.contains("the mob") && !text.contains("in arena")) {
                score += 30;
            }
        }

        if (containsInsult(text) && containsAction(text)) {
            result.addViolation(ViolationType.MILD_INSULT);
            score += 20;
        }
        
        return score;
    }

    private int checkSpam(String message, DetectionResult.Builder result) {
        int score = 0;

        long upperCount = message.chars().filter(Character::isUpperCase).count();
        long letterCount = message.chars().filter(Character::isLetter).count();
        if (letterCount > 5 && upperCount > letterCount * 0.7) {
            result.addViolation(ViolationType.SPAM);
            score += 10;
        }

        long punctCount = message.chars().filter(c -> "!?.,;:".indexOf(c) >= 0).count();
        if (punctCount > 5) {
            result.addViolation(ViolationType.SPAM);
            score += 10;
        }

        if (repeatedCharPattern.matcher(message).matches()) {
            result.addViolation(ViolationType.SPAM);
            score += 5;
        }
        
        return score;
    }

    private int checkSingleLetterSpacing(String message, DetectionResult.Builder result) {
        int score = 0;
        
        String normalized = TextNormalizer.normalize(message);

        java.util.regex.Matcher matcher = singleLetterPattern.matcher(normalized);
        
        while (matcher.find()) {
            String sequence = matcher.group().trim().replaceAll("\\s+", "");

            if (sequence.length() < 4) {
                continue;
            }

            String[] severeSlurs = {
                "nigger", "nigga", "faggot", "fag", "retard"
            };
            
            for (String slur : severeSlurs) {
                if (sequence.equals(slur)) {
                    result.addViolation(ViolationType.SLUR);
                    score += 70;
                    return score;
                }
            }
        }

        String[] words = normalized.split("\\s+");
        if (words.length >= 5 && words.length <= 10) {
            StringBuilder firstLetters = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    firstLetters.append(word.charAt(0));
                }
            }
            
            String firstLetterSequence = firstLetters.toString();

            String[] severeSlurs = {
                "nigger", "nigga", "faggot", "retard", "kys"
            };
            
            for (String slur : severeSlurs) {
                // Exact match only - not contains (reduces false positives)
                if (firstLetterSequence.equals(slur)) {
                    result.addViolation(ViolationType.SLUR);
                    score += 80;
                    return score;
                }
            }
        }
        
        return score;
    }

    private boolean containsSuspiciousPatterns(String text) {
        return text.matches(".*[k|c]+[i|y]+[l]+.*") ||
               text.matches(".*[n]+[i]+[g]+.*") ||
               text.matches(".*[f]+[a]+[g]+.*");
    }
    

    private boolean containsInsult(String text) {
        String[] insults = {"idiot", "stupid", "dumb", "moron", "loser"};
        for (String insult : insults) {
            if (text.contains(insult)) return true;
        }
        return false;
    }

    private boolean containsAction(String text) {
        String[] actions = {"kill", "die", "hurt", "harm"};
        for (String action : actions) {
            if (text.contains(action)) return true;
        }
        return false;
    }

    private int checkFuzzyMatch(String normalized, String aggressive, DetectionResult.Builder result) {
        int score = 0;

        String[] words = normalized.split("\\s+");
        
        for (String word : words) {
            if (word.length() >= 4) {
                if (LevenshteinMatcher.isSimilar(word, config.getSlurs(), FUZZY_MATCH_THRESHOLD)) {
                    result.addViolation(ViolationType.SLUR);
                    score += 60;
                    break;
                }
                if (LevenshteinMatcher.isSimilar(word, config.getThreats(), FUZZY_MATCH_THRESHOLD)) {
                    result.addViolation(ViolationType.DEATH_THREAT);
                    score += 70;
                    break;
                }
                if (LevenshteinMatcher.isSimilar(word, config.getSexualContent(), FUZZY_MATCH_THRESHOLD)) {
                    result.addViolation(ViolationType.SEXUAL_CONTENT);
                    score += 60;
                    break;
                }
            }
        }
        if (score == 0 && aggressive.length() >= 4) {
            if (LevenshteinMatcher.isSimilar(aggressive, config.getSlurs(), FUZZY_MATCH_THRESHOLD)) {
                result.addViolation(ViolationType.SLUR);
                score += 60;
            }
        }
        
        return score;
    }

    private int checkSlidingWindow(String aggressive, DetectionResult.Builder result) {
        int score = 0;

        java.util.List<String> foundWords = slidingWindow.findBadWords(aggressive);
        
        if (!foundWords.isEmpty()) {
            for (String word : foundWords) {
                if (word.length() >= 4) {
                    result.addViolation(ViolationType.SLUR);
                    score += 40;
                    break;
                }
            }
        }
        
        return score;
    }
}
