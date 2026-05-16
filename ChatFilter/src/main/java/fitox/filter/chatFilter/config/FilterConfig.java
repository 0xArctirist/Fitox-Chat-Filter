package fitox.filter.chatFilter.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterConfig {
    
    private final Plugin plugin;
    private List<String> slurs = new ArrayList<>();
    private List<String> threats = new ArrayList<>();
    private List<String> sexualContent = new ArrayList<>();
    private List<String> advertising = new ArrayList<>();
    
    public FilterConfig(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        File configDir = new File(plugin.getDataFolder(), "rules");
        if (!configDir.exists()) {
            configDir.mkdirs();
            createDefaultConfigs(configDir);
        } else {
            File slursFile = new File(configDir, "slurs.yml");
            File threatsFile = new File(configDir, "threats.yml");
            File sexualFile = new File(configDir, "sexual.yml");
            File advertisingFile = new File(configDir, "advertising.yml");
            
            if (!slursFile.exists() || !threatsFile.exists() || 
                !sexualFile.exists() || !advertisingFile.exists()) {
                createDefaultConfigs(configDir);
            }
        }

        loadMainConfig();

        slurs = loadRuleFile(new File(configDir, "slurs.yml"), "slurs");
        threats = loadRuleFile(new File(configDir, "threats.yml"), "threats");
        sexualContent = loadRuleFile(new File(configDir, "sexual.yml"), "sexual");
        advertising = loadRuleFile(new File(configDir, "advertising.yml"), "advertising");
    }
    

    public void forceRecreateConfigs() {
        File configDir = new File(plugin.getDataFolder(), "rules");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File slursFile = new File(configDir, "slurs.yml");
        File threatsFile = new File(configDir, "threats.yml");
        File sexualFile = new File(configDir, "sexual.yml");
        File advertisingFile = new File(configDir, "advertising.yml");
        
        if (slursFile.exists()) {
            slursFile.delete();
        }
        if (threatsFile.exists()) {
            threatsFile.delete();
        }
        if (sexualFile.exists()) {
            sexualFile.delete();
        }
        if (advertisingFile.exists()) {
            advertisingFile.delete();
        }
        createDefaultConfigs(configDir);

        loadConfig();
    }
    

    private void loadMainConfig() {
        plugin.saveDefaultConfig();
    }
    

    private List<String> loadRuleFile(File file, String key) {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getStringList(key);
    }

    private void createDefaultConfigs(File configDir) {
        createSlursConfig(new File(configDir, "slurs.yml"));
        createThreatsConfig(new File(configDir, "threats.yml"));
        createSexualConfig(new File(configDir, "sexual.yml"));
        createAdvertisingConfig(new File(configDir, "advertising.yml"));
    }
    
    private void createSlursConfig(File file) {
        FileConfiguration config = new YamlConfiguration();
        List<String> slurs = List.of(
            // n word
            "nigger", "nigga", "nigg", "niga", "nigz", "nigar", "nigr",
            "niqqa", "niqqer", "nggr", "ngger", "neigger", "n3igger",
            "nga", "ngr", "ngga", "nggah",
            "n1gger", "n1gga", "n1g", "nig3r", "ni99er", "ni66er", "n199er", "n166er",
            "niigger", "niigga", "niigg", "niig", "n11gger", "n11gga",
            "niqqer", "niqqa", "niqqr", "n1qqa", "n1qqer",
            "n!gger", "n!gga", "n!g", "n!gg3r", "n!gg@",
            "niqger", "niqga", "n1qger", "n1qga",
            "niggor", "niggur", "n1ggor", "n1ggur",
            "nicker", "nikker", "nikka", "n1kker", "n1kka", "n1cker",
            "kneegrow", "kneegro", "kn33grow", "kn33gro", "knee grow", "knee gro",
            "nickgurr", "nickger", "n1ckgurr", "n1ckger", "nick gurr", "nick ger",
            "n3gro", "negro", "negr0", "n3gr0", "negroe",
            "nigglet", "nigglets", "n1gglet", "n1gglets",
            "nigguh", "niggah", "niggaz", "n1gguh", "n1ggah", "n1ggaz",
            "nword", "nw0rd",
            "n1gg3r", "n!gg3r", "n1gg@", "n!gg@",
            "n199a", "n166a", "n!99er", "n!66er",
            "n1663r", "n166a", "n1993r", "n199a",
            "niqquh", "n1qquh", "niqqah", "n1qqah",
            "n3gga", "n3gger", "n3gg3r", "n3gg@",
            "niggas", "n1ggas", "n!ggas", "n1gg@s",
            "niggers", "n1ggers", "n!ggers",
            
            //  other stuff
            "nig ger", "nig ga", "ni gger", "ni gga", "n igger", "n igga",
            "nig let", "nig lets", "nig uh", "nig ah", "nig az",
            "nig or", "nig ur", "ne gro", "ne groe",
            "knee ger", "knee ga", "nick er", "nick a",
            "nig nog", "n1gnog", "nignug", "n1gnug", "nignag", "n1gnag",

            // f word
            "faggot", "fagot", "fagg", "f4g", "f4ggot", "f4got",
            "fgt", "fggt", "f99t", "f66t", "f99ot", "f66ot",
            "phag", "fhag", "fhagot", "phagot", "ph4g", "ph4got",
            "faaggot", "faag", "f44ggot", "f44g",
            "faggit", "faggat", "f4ggit", "f4ggat",
            "f@ggot", "f@g", "f@gg0t", "f@gg",
            "fagget", "faggett", "f4gget", "f4ggett",
            "phagot", "phaget", "ph4got", "ph4get",
            "faguette", "fagette", "f4guette", "f4gette",
            "faqqot", "faqgot", "f4qqot", "f4qgot",
            "faggort", "f4ggort", "f@ggort",
            "fword", "fw0rd",
            "f@99ot", "f@66ot", "f!ggot", "f!g", "f!gg0t",
            "f4gg", "f@gg", "f!gg",
            "faggots", "f4ggots", "f@ggots", "f!ggots",
            "f4gs", "f@gs", "f!gs", "fags",
            "f4gg1t", "f@gg1t", "f!gg1t",
            "f4gg0t", "f@gg0t",
            "fag got", "fag ot", "fa ggot", "fa got", "f aggot",
            "fag it", "fag at", "fag et", "fag ette",

            // r word
            "retard", "retarded", "rtard", "ret4rd", "r3tard", "r3t4rd",
            "tard", "rtrd", "t4rd", "r3t@rd",
            "retart", "reetard", "reetarded", "re3tard", "re3tarded",
            "ret@rd", "r3t@rd", "ret@rded", "r3t@rded",
            "ritard", "reterd", "retarted", "r1tard", "r1tarded",
            "retart", "retrd", "ret4rded", "r3t4rded",
            "rword", "rw0rd",
            "ret@rt", "r3t@rt", "r1t@rd", "r!tard",
            "retards", "r3tards", "ret4rds", "r3t4rds",
            "r3t@rds", "ret@rds",
            "ret4rd3d", "r3t4rd3d",
            "r3tard3d", "ret@rd3d",

            "re tard", "re tarded", "ret ard", "ret arded",
            "r tard", "r tarded", "re art", "ret art",

            // other slurs
            "tranny", "trannie", "trany", "tr@nny", "tranni", "tr4nny", "tr@nni", "tr4nni", "tr@ny", "tr4ny",
            "trannies", "tr@nnies", "tr4nnies",
            "chink", "ch1nk", "chnk", "ch!nk", "chinc", "ch1nc", "ch!nc", "ch1nk3r",
            "chinks", "ch1nks", "ch!nks",
            "ching chong", "chingchong", "ch1ngchong", "ching ch0ng", "ch1ng ch0ng",
            "spic", "sp1c", "spik", "sp!c", "spick", "sp1ck", "sp!ck", "sp1k", "sp!k",
            "spics", "sp1cs", "sp!cs", "spicks", "sp1cks",
            "kike", "k1ke", "kyke", "k!ke", "kyk", "k1k3", "k!k3", "k1k",
            "kikes", "k1kes", "k!kes",
            "heeb", "h3eb", "he3b", "h33b", "heebs", "h3ebs",
            "beaner", "b3aner", "be@ner", "beanr", "be4ner", "b3@ner", "be4n3r",
            "beaners", "b3aners", "be@ners",
            "wetback", "w3tback", "wetbac", "wet back", "w3tb4ck", "w3t back", "wetb4ck",
            "wetbacks", "w3tbacks",
            "gook", "g00k", "go0k", "g0ok", "g00ks", "go0ks", "g0oks",
            "coon", "c00n", "co0n", "c0on", "c00ns", "co0ns", "c0ons",
            "paki", "p4ki", "p@ki", "pakki", "p4kki", "p@kki", "p4k1", "p@k1",
            "pakis", "p4kis", "p@kis",
            
            // additional racial slurs
            "towelhead", "t0welhead", "towelhed", "t0welhed", "t0w3lhead", "towel head", "t0wel head",
            "raghead", "r4ghead", "raghed", "r4ghed", "r@ghead", "rag head", "r4g head",
            "sandnigger", "sandnigga", "s4ndnigger", "s@ndnigger", "sand nigger", "s4nd nigger",
            "camel jockey", "cameljockey", "c4meljockey", "camel j0ckey", "c@meljockey", "c4mel jockey",
            "zipperhead", "z1pperhead", "z1pp3rhead", "zipper head", "z1pper head", "z!pperhead",
            "cr4cker", "cracka", "crakkka", "cr@cker", "cr4cka", "cr@cka", "cr4kka",
            "honkey", "honky", "h0nkey", "h0nky", "h0nk3y", "h0nk1", "honk3y", "h0nkie",
            "whitey", "wh1tey", "whiteboy", "wh1t3y", "wh!tey", "white boy", "wh1te boy",
            "redskin", "r3dskin", "red skin", "r3dsk1n", "r3d skin", "r3dsk!n",
            "injun", "1njun", "1nj0n", "!njun", "inj0n",
            "jigaboo", "jiggaboo", "j1gaboo", "j1g4boo", "j!gaboo", "jigg4boo", "j1ggaboo",
            "porch monkey", "porchmonkey", "p0rchmonkey", "porch m0nkey", "p0rch monkey", "p0rchm0nkey",
            "uncle tom", "uncletom", "unclet0m", "uncle t0m", "uncl3tom",
            "wigger", "w1gger", "whigger", "w1gg3r", "w!gger", "wh1gger", "w1gg3r",

            // homophobic
            "dyke", "dyk3", "d1ke", "dike", "dyyk", "d!ke", "dyk", "d1k3",
            "queer", "qu33r", "kw33r", "kweer", "quear", "qu3er", "qu3ar", "kwe3r",
            "h0mo", "hom0", "h0m0", "homosexual", "h0mosexual", "hom0sexual",
            "sodomite", "s0domite", "sodom1te", "s0dom1te", "s0d0mite",
            "f4iry", "faerie", "faery", "f@iry", "f4ery", "f@ery",
            "fruity", "fr00ty", "fruitcake", "fru1ty", "fru!ty", "fruit cake",
            "p4nsy", "pansie", "p@nsy", "p4nsie", "p@nsie",
            "sissy", "s1ssy", "sissie", "s!ssy", "s1ssie", "s!ssie",
            "pillow biter", "pillowbiter", "p1llowbiter", "pillow b1ter", "p1llow biter",
            "fudge packer", "fudgepacker", "fudge p4cker", "fudgep4cker", "fudge pack3r",
            "carpet muncher", "carpetmuncher", "carpet munch3r", "carpetmunch3r", "c4rpetmuncher",
            
            // ableist slurs
            "cripple", "cr1pple", "cr1p", "cr!pple", "cr!p", "cripp", "cr1pp",
            "gimp", "g1mp", "g!mp", "g1mp3r", "gimpy", "g1mpy",
            "spastic", "sp4zz", "sp@zz", "sp4z", "sp@z", "sp4st1c", "sp@stic",
            "midget", "m1dget", "m!dget", "m1dg3t", "m!dg3t",
            "mongoloid", "mong", "m0ng", "m0ngoloid", "m0ng0loid", "mong0loid",
            "retardation", "ret4rdation", "ret@rdation", "ret4rd4tion",
            
            // misogynistic
            "bitch", "b1tch", "b!tch", "biatch", "biotch", "b1atch", "b!atch", "b!tch3s", "b1tches", "b!atch3s",
            "cunt", "c0nt", "kunt", "c**t", "k0nt", "c0nt5", "kunts", "c0nts",
            
            // hate groups and symbols
            "kkk", "k k k", "ku klux klan", "kukluxklan", "klu klux klan", "klukluxklan",
            "nazi", "n4zi", "n@zi", "naz1", "n4z1", "n@z1", "naz!",
            "neonazi", "neo nazi", "neo-nazi", "ne0nazi", "ne0 nazi", "n30nazi",
            "white supremacist", "whitesupremacist", "wh1tesupremacist", "white supremacy", "whitesupremacy",
            "white power", "whitepower", "wh1tepower", "wh1te power", "wp14", "wpww",
            "aryan", "ary4n", "@ryan", "aryan brotherhood", "aryannation", "aryan nation",
            "skinhead", "sk1nhead", "skinheads", "sk1nheads", "sk!nhead",
            "stormfront", "st0rmfront", "st0rmfr0nt", "storm front",
            "proud boys", "proudboys", "pr0udboys", "pr0ud boys",
            "oath keepers", "oathkeepers", "0athkeepers", "0ath keepers",
            "boogaloo", "b00galoo", "b00g4loo", "boog", "b00g", "b0og",
            "qanon", "q4non", "qan0n", "q4n0n", "q anon",
            "alt right", "altright", "alt-right", "@ltright", "4ltright",
            "fourteen words", "14words", "1488", "14 88", "14/88",
            "blood and soil", "bloodandsoil", "bl00dandsoil", "bl00d and soil",
            "heil", "h3il", "he1l", "h31l", "sieg heil", "siegheil", "s1egheil", "s1eg heil",
            "swastika", "sw4stika", "sw@stika", "sw4st1ka", "hakenkreuz", "h4kenkreuz",
            "iron cross", "ironcross", "1roncross", "1ron cross",
            
            // Note: "88" removed from standalone list to prevent false positives (birth years, numbers)
            // It's still caught in context like "1488", "14/88", "8 8"
            
            // historical hate figures
            "hitler", "h1tler", "h!tler", "h1tl3r", "adolf hitler", "adolfhitler", "ad0lfhitler", "ad0lf hitler",
            "fuhrer", "fuehrer", "führer", "fuhr3r", "fu3hrer", "f00hrer",
            "mussolini", "muss0lini", "muss0l1ni", "benito mussolini", "benitomuss0lini",
            "stalin", "st4lin", "st@lin", "st4l1n", "joseph stalin", "josephstalin", "j0sephstalin",
            "mao", "m4o", "m@o", "mao zedong", "maozedong", "m4ozedong", "m@ozedong",
            "pol pot", "polpot", "p0lpot", "p0l pot", "p0lp0t",
            "bin laden", "binladen", "b1nladen", "b1n laden", "osama", "0sama", "0s4ma", "osama bin laden", "osamabin laden",
            "is1s", "!sis", "1s1s", "isil", "is1l", "!sil", "daesh", "d4esh", "da3sh",
            "taliban", "t4liban", "t@liban", "t4l1ban", "t@l1ban", "tal1ban",
            "al qaeda", "alqaeda", "al-qaeda", "4lqaeda", "@lqaeda", "al qa3da", "alqa3da",
            
            // genocide references
            "holocaust", "h0locaust", "hol0caust", "h0l0caust", "h0loc4ust", "holoc4ust",
            "genocide", "gen0cide", "g3nocide", "gen0c1de", "g3noc1de", "g3n0cide",
            "ethnic cleansing", "ethniccleansing", "3thniccleansing", "ethnic cl3ansing",
            "final solution", "finalsolution", "f1nalsolution", "final solut1on", "f1nal solution",
            "gas chamber", "gaschamber", "g4schamber", "gas ch4mber", "gas chambers", "gasch4mbers",
            "concentration camp", "concentrationcamp", "c0ncentrationcamp", "concentration c4mp", "concentr4tioncamp",
            "auschwitz", "auschw1tz", "auschw!tz", "@uschwitz", "4uschwitz",
            "dachau", "d4chau", "d@chau", "d4ch4u",
            "treblinka", "trebl1nka", "trebl!nka", "tr3blinka", "tr3bl1nka",
            
            // extremist phrases
            "white genocide", "whitegenocide", "wh1tegenocide", "white gen0cide", "wh1te genocide",
            "great replacement", "greatreplacement", "gre4treplacement", "great repl4cement", "gre4t replacement",
            "race war", "racewar", "r4cewar", "race w4r", "r4ce war", "rahowa", "r4howa", "rah0wa",
            "day of the rope", "dayoftherope", "day0ftherope", "day of the r0pe", "dayofther0pe",
            "turner diaries", "turnerdiaries", "turn3rdiaries", "turner di4ries", "turn3r diaries",
            "protocols of zion", "protocolsofzion", "pr0tocolsofzion", "protocols of z1on", "pr0tocols of zion",
            "jewish question", "jewishquestion", "j3wishquestion", "jewish quest1on", "j3wish question",
            "zog", "z0g", "z00g", "zionist", "z1onist", "zi0nist", "z10nist", "z!onist"
        );
        config.set("slurs", slurs);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createThreatsConfig(File file) {
        FileConfiguration config = new YamlConfiguration();
        List<String> threats = List.of(
            // direct suicide commands
            "killyourself", "kys", "killyoself",
            "k1llyourself", "k1llyoself", "kyourself",
            "killurself", "kilurself", "kllurself",
            "killyourselfnow", "kysnow",
            
            // go die variations
            "godie", "gokys", "godye", "g0die",
            
            // direct threats
            "iwillkillyou", "imgoingtokillyou", "illkillyou",
            "imgonnakillyou", "iwillkillu", "illkillu",
            "youredead", "youaredead", "urdead",
            "yourded", "urded",
            "imgoingtokillu", "imgoingtokill",
            "imgunnakillyou", "imgunnakill",
            
            // hope you die
            "ihopeyoudie", "youshoulddie", "gocommit",
            "hopeyoudie", "ushouldie", "ihopeudie",
            "hopeudie", "ihopeyoud1e",
            "youshouldkys", "ushouldkys",
            
            // self harm
            "youshouldcutyourself", "cutyourself", "cutyoself",
            "youshouldcut", "ushouldcut",
            "endyourself", "hangyourself", "shootyourself",
            "jumpoffabridge", "jumpoffbridge",
            "slityourwrists", "slityourthroat",
            "slashyourwrists", "slashyourthroat",
            "overdose", "0verdose", "takepills",
            
            // coded/indirect threats
            "commitnotlive", "commitnotliving", "commitnotbreathing",
            "commitdie", "commitoof", "committoasterb4th",
            "committoasterbath", "commitrope", "commitnoalive",
            "uninstallife", "uninstalllife", "deleteyourself",
            "removeyourself", "logofflife", "altf4life",
            "ctrlaltdeleteyourself", "gocommitdie",
            "gocommitoof", "gocommitnotlive",
            "gocommittoasterbath", "gocommitrope",
            
            // suicide euphemisms
            "sudoku", "sewer slide", "sewerslide",
            "unlive", "unalive", "unliving",
            "stopbreathing", "stopexisting",
            "neckrope", "ropeyourself",
            "toasterbath", "toaster bath",
            "commit scooter ankle", "commitscooterankle",
            "commit lego step", "commitlegostep",
            
            // drink/eat dangerous things
            "drinkbleach", "eatbleach", "eattidepods",
            "drinktidepods", "eatcyanide", "drinkcyanide",
            "eatrat poison", "drinkrat poison",
            "eatpoisonberries", "drinkpoisonberries",
            
            // abbreviations
            "kms", "kmys", "gkys", "gky",
            "kmsn", "kmsnow",

            // school/mass violence threats
            "schoolshooter", "schoolshooting",
            "massshooter", "massshooting",
            "bombschool", "bomb school",
            "shootupschool", "shoot up school",
            
            // doxxing threats
            "doxx", "d0xx", "dox", "d0x",
            "doxxing", "d0xxing", "doxing", "d0xing",
            "leak your address", "leakyouraddress",
            "leak your ip", "leakyourip",
            "post your address", "postyouraddress",
            "post your ip", "postyourip",
            "find your address", "findyouraddress",
            "know where you live", "knowwhereyoulive",
            "i know your address", "iknowyouraddress",
            "i have your address", "ihaveyouraddress",
            "i have your ip", "ihaveyourip",
            "i know your ip", "iknowyourip",
            "pull your ip", "pullyourip",
            "grab your ip", "grabyourip",
            "ip grab", "ipgrab", "ip logger", "iplogger",
            "leak your info", "leakyourinfo",
            "leak your dox", "leakyourdox",
            
            // swatting threats
            "sw4t", "swatting", "sw4tting",
            "call the cops", "callthecops",
            "call the police", "callthepolice",
            "send cops", "sendcops",
            "send police", "sendpolice",
            "send swat", "sendswat",
            "swat team", "swatteam",
            "fake 911", "fake911",
            "prank call 911", "prankcall911",
            
            // ddos/booting threats
            "boot you offline", "bootyouoffline",
            "bootyou",
            "boot your wifi", "bootyourwifi",
            "boot your internet", "bootyourinternet",
            "ddos you", "ddosyou", "dd0s you",
            "dosyou", "d0s you",
            "lag you out", "lagyouout",
            "crash your wifi", "crashyourwifi",
            "crash your internet", "crashyourinternet",
            "take you offline", "takeyouoffline",
            "knock you offline", "knockyouoffline",
            "flood your ip", "floodyourip",
            "stress your ip", "stressyourip",
            "hit you offline", "hityouoffline",
            "boot your router", "bootyourrouter",
            "fry your router", "fryyourrouter",
            "im going to boot", "imgointoboot",
            "ill boot you", "illbootyou",
            "i will boot you", "iwillbootyou",
            "getting booted", "gettingbooted",
            "youre getting booted", "youregettingbooted",
            "stresser", "str3sser", "booter", "b00ter",
            "ip stresser", "ipstresser"
        );
        config.set("threats", threats);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createSexualConfig(File file) {
        FileConfiguration config = new YamlConfiguration();
        List<String> sexual = List.of(
            // explicit
            "porn", "pron", "pr0n", "p0rn", "p0rno", "porno", "pornography",
            "sexx", "secks", "s3xx", "s3cks",
            "dick", "d1ck", "dik", "dck", "d!ck", "d1k",
            "peepee", "pee pee", "weewee", "wee wee",
            "schlong", "schl0ng", "d0ng", "w4ng",
            "wiener", "w1ener", "p3cker",
            "cock", "c0ck", "cok", "cck", "c0k", "c**k",
            "chode", "ch0de",
            "pussy", "pus5y", "puss", "pu55y", "pu$y", "pu$$y",
            "puh", "psy", "pusy", "pussi", "pus1", "pu5i", "pu5sy",
            "bussy", "bu5sy", "bu$$y", "b0ssy",
            "coochie", "cooch", "c00chie", "c00ch",
            "cooter", "c00ter",
            "punani", "pun4ni",
            "vajayjay", "vaj", "v4j",
            "twat", "tw4t", "tw@t",
            "snatch", "sn4tch",
            "penis", "pen1s", "pnis", "p3nis", "pen15",
            "vagina", "vag1na", "vgina", "v@gina", "vag",
            "boobs", "b00bs", "boobies", "b00bies", "bewbs",
            "jugs", "j0gs", "melons", "mel0ns", "knockers", "kn0ckers",
            "tits", "t1ts", "titties", "t!ts", "t1tt1es",
            "arse", "a55", "azz", "@rse", "a$$",
            "booty", "b00ty", "bootay", "b00tay",
            "rump", "r0mp",
            "anal", "an4l", "anl", "@nal", "an@l",
            "rape", "r4pe", "rpe", "r@pe", "r@p3", "raped", "raping",
            "molest", "m0lest", "mol3st", "molested", "molesting",
            "coom", "c00m", "c0om", "coomer", "c00mer",
            "jizz", "j1zz", "j!zz", "jizzed", "j1zzed",
            "spunk", "sp0nk", "sp0nked",
            "orgasm", "0rgasm", "org@sm", "0rg@sm",
            "masturbate", "masterbate", "m@sturbate", "fap", "f@p", "fapping",
            "wank", "w4nk", "w@nk", "wanking", "w4nking",
            "jerkoff", "j3rkoff", "jerk off", "j3rk off",
            "jackoff", "j4ckoff", "jack off",
            "beating meat", "beat my meat", "beat his meat",
            "horny", "h0rny", "h0rni", "h**ny",
            "aroused", "ar0used",
            "turned on", "turnedon",
            "erect", "3rect",
            "moist", "m0ist",
            
            // spacing bypasses for sexual content
            "se x", "se xx", "s ex", "s3x", "s3xx",
            "por n", "p orn", "p0rn", "pr0n",
            "di ck", "d ick", "d1ck", "dik",
            "co ck", "c ock", "c0ck", "cok",
            "pu ssy", "pus sy", "p ussy", "puh sy",
            "bo obs", "bo ob", "b oobs", "b00bs",
            "ti ts", "t its", "t1ts", "titz",
            "an al", "a nal", "an4l", "anl",
            "ra pe", "r ape", "r4pe", "rpe",
            "mo lest", "mol est", "m0lest",
            "ho rny", "hor ny", "h0rny", "h0rni",
            "fa p", "f ap", "f@p",
            "ma sturbate", "mast urbate", "m@sturbate",
            "or gasm", "org asm", "0rgasm",
            
            // more explicit terms
            "blowjob", "bl0wjob", "blow job", "giving head",
            "blow j0b", "bl0w job", "beejay",
            "handjob", "h@ndjob", "hand job", "h4ndjob",
            "hand j0b", "h@nd job",
            "footjob", "f00tjob", "foot job",
            "foot j0b", "f00t job",
            "titjob", "t1tjob", "tit job",
            "tit j0b", "t1t job", "titfuck", "t1tfuck",
            "rimjob", "r1mjob", "rim job",
            "rim j0b", "r1m job",
            "jerkoff", "j3rkoff", "jackoff", "jerk off",
            "cumshot", "c0mshot", "cum shot",
            "creampie", "cr3ampie", "cream pie",
            "gangbang", "g@ngbang", "gang bang",
            "orgy", "0rgy", "0rgi",
            "incest", "1ncest", "inc3st",
            "p3dophile", "p3do", "paedo",
            "lolicon", "l0licon", "loli", "l0li",
            "hentai", "h3ntai", "h3nt@i",
            "nsfw", "n5fw",
            "milf", "m1lf", "dilf", "d1lf",
            
            // body parts (explicit)
            "ballsack", "b@llsack", "ball sack",
            "testicles", "t3sticles", "b@lls",
            "scrotum", "scr0tum", "scr0t0m",
            "clitoris", "cl1toris", "clit", "cl1t",
            "labia", "l@bia", "l@b1a",
            "nipples", "n1pples", "n1ps",
            "areola", "ar3ola",
            
            // sexual acts
            "intercourse", "1ntercourse", "s3xual intercourse",
            "penetration", "p3netration", "p3n3tration",
            "fellatio", "f3llatio", "f3ll@tio",
            "cunnilingus", "cunn1lingus", "cunn1l1ngus",
            "sixtynine", "sixty nine",
            "doggystyle", "d0ggystyle", "doggy style",
            "missionary", "m1ssionary", "m1ss1onary",
            "cowgirl", "c0wgirl", "reverse cowgirl",
            "threesome", "thr3esome", "3some",
            "foursome", "f0ursome", "4some",
            "bondage", "b0ndage", "b0nd@ge",
            "bdsm", "bd5m",
            "dominatrix", "d0minatrix", "domme",
            "submissive", "subm1ssive",
            "sadism", "s@dism", "masochism", "m@sochism",
            
            // sexual harassment
            "thot", "th0t", "th@t",
            "hoe", "h0e", "h03", "hoee", "h0ee",
            "slut", "sl0t", "sloot", "sl00t", "sl*t",
            "whore", "wh0re", "hore", "wh0r3", "wh**e",
            "prostitute", "pr0stitute", "pr0st1tute",
            "hooker", "h00ker", "h00k3r",
            "3scort", "3sc0rt",
            "stripper", "str1pper", "str1pp3r",
            "pornstar", "p0rnstar", "porn star",
            "camgirl", "c@mgirl", "cam girl",
            "camwhore", "c@mwhore", "cam whore",
            
            // grooming/predatory
            "send nudes", "sendnudes", "s3nd nudes",
            "show boobs", "showboobs", "show tits",
            "show pussy", "showpussy",
            "dick pic", "dickpic", "d1ck pic",
            "nude pic", "nudepic", "nudes",
            "sexting", "s3xting", "s3xt1ng"
        );
        config.set("sexual", sexual);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createAdvertisingConfig(File file) {
        FileConfiguration config = new YamlConfiguration();
        List<String> advertising = List.of(
            // server advertising
            "joinmyserver", "playat", "serverip",
            "joinserver", "playmy", "myserver",
            "joinmy", "playon", "serveraddress",
            "ipaddress", "serverlink", "joinlink",
            "joinourserver", "playourserver",
            "checkoutmyserver", "visitourserver",
            "comejoin", "comeplay",
            
            // unsafe/inappropriate websites
            "pornhub", "xvideos", "xhamster", "redtube",
            "youporn", "xnxx", "beeg", "spankbang",
            "chaturbate", "onlyfans", "patreon",
            "4chan", "8chan", "8kun",
            "bestgore", "liveleak", "goregrish",
            "thepiratebay", "kickass", "torrentz",
            "silk road", "darkweb", "deepweb",
            
            // gambling/betting sites
            "csgolounge", "csgobetting", "skinbetting",
            "stake", "roobet", "duelbits",
            
            // phishing/scam patterns
            "bit.ly", "tinyurl", "goo.gl",
            "grabify", "iplogger", "ipgrabber"
        );
        config.set("advertising", advertising);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSlurs() { return slurs; }
    public List<String> getThreats() { return threats; }
    public List<String> getSexualContent() { return sexualContent; }
    public List<String> getAdvertising() { return advertising; }
}
