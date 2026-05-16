package fitox.filter.chatFilter.detection;

import java.util.HashSet;
import java.util.Set;


public class WhitelistManager {
    
    private final Set<String> whitelistedPhrases = new HashSet<>();
    
    public WhitelistManager() {
        whitelistedPhrases.add("killed the");
        whitelistedPhrases.add("kill the");
        whitelistedPhrases.add("nice kill");
        whitelistedPhrases.add("good kill");
        whitelistedPhrases.add("kill count");
        whitelistedPhrases.add("killstreak");
        whitelistedPhrases.add("overkill");
        whitelistedPhrases.add("roadkill");
        whitelistedPhrases.add("killing it");
        whitelistedPhrases.add("killer game");
        whitelistedPhrases.add("killer move");
        whitelistedPhrases.add("kill boss");
        whitelistedPhrases.add("kill dragon");
        whitelistedPhrases.add("kill mob");
        whitelistedPhrases.add("kill wither");
        whitelistedPhrases.add("kill ender");
        whitelistedPhrases.add("in pvp");
        whitelistedPhrases.add("in game");
        whitelistedPhrases.add("in arena");

        whitelistedPhrases.add("i died");
        whitelistedPhrases.add("you died");
        whitelistedPhrases.add("he died");
        whitelistedPhrases.add("she died");
        whitelistedPhrases.add("they died");
        whitelistedPhrases.add("died to");
        whitelistedPhrases.add("died from");
        whitelistedPhrases.add("died in");
        whitelistedPhrases.add("dont die");
        whitelistedPhrases.add("try not to die");
        whitelistedPhrases.add("almost died");
        whitelistedPhrases.add("nearly died");

        addSafeWord("lmao");
        addSafeWord("lmfao");
        addSafeWord("rofl");
        addSafeWord("lol");
        addSafeWord("haha");
        addSafeWord("hehe");

        addSafeWord("say");
        addSafeWord("says");
        addSafeWord("said");
        addSafeWord("saying");
        addSafeWord("cuss");
        addSafeWord("cussing");
        addSafeWord("discuss");
        addSafeWord("discussion");
        addSafeWord("nigh");
        addSafeWord("night");
        addSafeWord("nights");
        addSafeWord("knight");
        addSafeWord("knights");
        addSafeWord("nightmare");
        addSafeWord("nighttime");
        addSafeWord("midnight");
        addSafeWord("tonight");
        addSafeWord("goodnight");
        addSafeWord("good night");

        addSafeWord("assassin");
        addSafeWord("assassins");
        addSafeWord("assassination");
        addSafeWord("class");
        addSafeWord("classes");
        addSafeWord("classic");
        addSafeWord("grass");
        addSafeWord("pass");
        addSafeWord("passed");
        addSafeWord("passing");
        addSafeWord("compass");
        addSafeWord("glass");
        addSafeWord("mass");
        addSafeWord("bass");

        addSafeWord("but");
        addSafeWord("butt");
        addSafeWord("button");
        addSafeWord("buttons");
        addSafeWord("butter");
        addSafeWord("butterfly");
        addSafeWord("buttercup");
        addSafeWord("rebuttal");

        addSafeWord("sextant");
        addSafeWord("sextet");
        addSafeWord("unisex");
        addSafeWord("sussex");
        addSafeWord("essex");
        addSafeWord("middlesex");

        addSafeWord("analyze");
        addSafeWord("analysis");
        addSafeWord("analytical");
        addSafeWord("analog");
        addSafeWord("analogy");
        addSafeWord("canal");
        addSafeWord("banal");

        addSafeWord("grape");
        addSafeWord("grapes");
        addSafeWord("grapefruit");
        addSafeWord("drape");
        addSafeWord("drapes");
        addSafeWord("scrape");
        addSafeWord("scraper");
        addSafeWord("scraping");
        addSafeWord("therapist");
        addSafeWord("therapy");

        addSafeWord("predict");
        addSafeWord("prediction");
        addSafeWord("predictable");
        addSafeWord("verdict");
        addSafeWord("dedicate");
        addSafeWord("dedicated");
        addSafeWord("dedication");
        addSafeWord("indicate");
        addSafeWord("indication");
        addSafeWord("indicator");
        addSafeWord("syndicate");
        addSafeWord("edicule");
        addSafeWord("dickens");

        addSafeWord("peacock");
        addSafeWord("peacocks");
        addSafeWord("woodcock");
        addSafeWord("gamecock");
        addSafeWord("cockpit");
        addSafeWord("cockroach");
        addSafeWord("cocktail");
        addSafeWord("shuttlecock");
        addSafeWord("cockatoo");
        addSafeWord("cockatiel");

        addSafeWord("cucumber");
        addSafeWord("document");
        addSafeWord("documents");
        addSafeWord("circumstance");
        addSafeWord("circumstances");
        addSafeWord("accumulate");
        addSafeWord("circumvent");
        addSafeWord("succumb");

        addSafeWord("title");
        addSafeWord("titles");
        addSafeWord("entity");
        addSafeWord("entities");
        addSafeWord("identity");
        addSafeWord("quantity");
        addSafeWord("petition");
        addSafeWord("competition");
        addSafeWord("repetition");
        addSafeWord("stitches");
        addSafeWord("stitching");
        addSafeWord("stitched");
        addSafeWord("stitcher");
        addSafeWord("stitchery");
        addSafeWord("stitchwork");
        addSafeWord("stitchcraft");

        addSafeWord("pass");
        addSafeWord("passed");
        addSafeWord("passing");
        addSafeWord("passenger");
        addSafeWord("passage");
        addSafeWord("compass");
        addSafeWord("glass");
        addSafeWord("glasses");
        addSafeWord("mass");
        addSafeWord("massive");
        addSafeWord("bass");
        addSafeWord("brass");
        addSafeWord("harass");
        addSafeWord("harassment");
        addSafeWord("embarrass");
        addSafeWord("embarrassed");
        addSafeWord("embarrassing");
        addSafeWord("ambassador");
        addSafeWord("assemble");
        addSafeWord("assembly");
        addSafeWord("assess");
        addSafeWord("assessment");
        addSafeWord("asset");
        addSafeWord("assets");
        addSafeWord("assign");
        addSafeWord("assignment");
        addSafeWord("assist");
        addSafeWord("assistance");
        addSafeWord("assistant");
        addSafeWord("associate");
        addSafeWord("association");
        addSafeWord("assume");
        addSafeWord("assumption");
        addSafeWord("assure");
        addSafeWord("assurance");

        addSafeWord("bedrock");
        addSafeWord("grassblock");
        addSafeWord("grass block");
        addSafeWord("hopper");
        addSafeWord("dropper");
        addSafeWord("dispenser");
        addSafeWord("observer");
        addSafeWord("piston");
        addSafeWord("sticky piston");
        addSafeWord("redstone");
        addSafeWord("comparator");
        addSafeWord("repeater");

        addSafeWord("what the heck");
        addSafeWord("what the hell");
        addSafeWord("oh my god");
        addSafeWord("oh my gosh");
        addSafeWord("omg");
        addSafeWord("brb");
        addSafeWord("afk");
        addSafeWord("gtg");
        addSafeWord("good game");
        addSafeWord("well played");
        addSafeWord("easy");
        addSafeWord("ez");
        addSafeWord("good games");

        addSafeWord("i cant say");
        addSafeWord("i can't say");
        addSafeWord("cant say");
        addSafeWord("can't say");
        addSafeWord("i cant");
        addSafeWord("i can't");
        addSafeWord("so like");
        addSafeWord("like so");
        addSafeWord("you know");
        addSafeWord("i mean");
        addSafeWord("i guess");
        addSafeWord("i think");
        addSafeWord("i dont know");
        addSafeWord("i don't know");
        addSafeWord("no cap");
        addSafeWord("on god");

        addSafeWord("button");
        addSafeWord("buttons");
        addSafeWord("butter");
        addSafeWord("butterfly");
        addSafeWord("buttercup");
        addSafeWord("rebuttal");
        addSafeWord("bummed");
        addSafeWord("birthday cake");
        addSafeWord("hard mode");
        addSafeWord("hard game");
        addSafeWord("hard work");
        addSafeWord("wet grass");
        addSafeWord("its wet");
        addSafeWord("my head");
        addSafeWord("your head");
        addSafeWord("head to");
        addSafeWord("head over");
        addSafeWord("overhead");
        addSafeWord("heading");
        addSafeWord("stroke of luck");
        addSafeWord("swimming stroke");
        addSafeWord("heat stroke");
        addSafeWord("roof rack");
        addSafeWord("bike rack");
        addSafeWord("server rack");
        addSafeWord("rack of ribs");
        addSafeWord("powerpoint");
        addSafeWord("profile pic");
        addSafeWord("submarine");
        addSafeWord("substitute");
        addSafeWord("subway");
        addSafeWord("subreddit");
        addSafeWord("saltine crackers");
        addSafeWord("graham crackers");
        addSafeWord("swat the fly");
        addSafeWord("swatting flies");
        addSafeWord("leotard");
        addSafeWord("mustard");
        addSafeWord("spazzing");
        addSafeWord("fairy tale");
        addSafeWord("tooth fairy");
        addSafeWord("homo sapiens");
        addSafeWord("ding dong");
        addSafeWord("woodpecker");
        addSafeWord("soccer balls");
        addSafeWord("basketballs");
        addSafeWord("tj maxx");
        addSafeWord("police escort");
        addSafeWord("ford escort");
        addSafeWord("pedometer");
        addSafeWord("pedestrian");
        addSafeWord("boot from server");

        addSafeWord("come inside");
        addSafeWord("come in");
        addSafeWord("welcome");
        addSafeWord("welcome in");
        addSafeWord("welcome inside");
        addSafeWord("outcome");
        addSafeWord("income");
        addSafeWord("become");
        addSafeWord("overcome");
        
        addSafeWord("eat me alive");
        addSafeWord("eat my food");
        addSafeWord("eat my lunch");
        addSafeWord("eat my dinner");
        addSafeWord("eat that apple");
        addSafeWord("eat that food");
        addSafeWord("eat it up");
        
        addSafeWord("hit that");
        addSafeWord("hit it");
        addSafeWord("gonna hit");
        addSafeWord("imma hit");
        
        addSafeWord("smash that");
        addSafeWord("smash it");
        addSafeWord("gonna smash");
        
        addSafeWord("fill you up with food");
        addSafeWord("fill you up with water");
        addSafeWord("fill it up with water");
        addSafeWord("fill it up with gas");
        addSafeWord("fill your inventory");
        addSafeWord("fill your tank");
        addSafeWord("fill your bottle");
        
        addSafeWord("load in");
        addSafeWord("load on");
        
        addSafeWord("bust in");
        addSafeWord("bust on");
        
        addSafeWord("go die");
        addSafeWord("go die in");
        addSafeWord("go die to");
        
        addSafeWord("go commit");
        addSafeWord("commit to");
        
        addSafeWord("spread your wings");
        addSafeWord("spread your arms");
        addSafeWord("spread out team");
        addSafeWord("spread out guys");
        addSafeWord("spread the word");
        addSafeWord("spread the news");
        
        addSafeWord("touch me not");
        addSafeWord("dont touch me");
        addSafeWord("don't touch me");
        addSafeWord("touch my stuff");
        addSafeWord("touch my things");
        addSafeWord("touch my items");
        addSafeWord("touch that block");
        addSafeWord("touch that button");
        
        addSafeWord("grab my hand");
        addSafeWord("grab my items");
        addSafeWord("grab my stuff");
        addSafeWord("grab me a sword");
        addSafeWord("grab me some");
        addSafeWord("grab that item");
        addSafeWord("grab that block");
        
        addSafeWord("feel me");
        addSafeWord("you feel me");
        addSafeWord("u feel me");
        
        addSafeWord("stroke my ego");
        addSafeWord("stroke of luck");
        addSafeWord("stroke of genius");
        
        addSafeWord("taste my cooking");
        addSafeWord("taste my food");
        addSafeWord("taste that food");
        addSafeWord("taste that dish");
        addSafeWord("taste it first");
        
        addSafeWord("lick my wounds");
        addSafeWord("lick that ice cream");
        addSafeWord("lick that lollipop");
        
        addSafeWord("suck me in");
        addSafeWord("suck my thumb");
        addSafeWord("suck that lollipop");
        addSafeWord("suck that candy");
        
        addSafeWord("blow my mind");
        addSafeWord("blow a kiss");
        addSafeWord("blow a fuse");
        addSafeWord("blow the whistle");
        addSafeWord("blow the horn");
        
        addSafeWord("nut inside");
        addSafeWord("nut in");
        addSafeWord("peanut");
        addSafeWord("peanuts");
        addSafeWord("walnut");
        addSafeWord("walnuts");
        addSafeWord("chestnut");
        addSafeWord("chestnuts");
        addSafeWord("coconut");
        addSafeWord("coconuts");
        addSafeWord("donut");
        addSafeWord("donuts");
        addSafeWord("doughnut");
        addSafeWord("doughnuts");
        addSafeWord("hazelnut");
        addSafeWord("hazelnuts");

        addSafeWord("daddy");
        addSafeWord("mommy");
        addSafeWord("my daddy");
        addSafeWord("my mommy");
        addSafeWord("your daddy");
        addSafeWord("your mommy");
        
        addSafeWord("send pics");
        addSafeWord("send pictures");
        addSafeWord("send images");
        addSafeWord("show me your build");
        addSafeWord("show me your base");
        addSafeWord("show me your house");
        addSafeWord("show me your skin");
        addSafeWord("show me your stats");
        
        addSafeWord("strip for");
        addSafeWord("strip the");
        addSafeWord("strip mine");
        addSafeWord("strip mining");
        
        addSafeWord("crack that");
        addSafeWord("crack the");
        addSafeWord("crack open");
        
        addSafeWord("pound that");
        addSafeWord("pound the");
        
        addSafeWord("rail that");
        addSafeWord("on the rail");
        addSafeWord("rail system");
        
        addSafeWord("breed that");
        addSafeWord("breed the");
        addSafeWord("breeding");
        
        addSafeWord("stuff your backpack");
        addSafeWord("stuff your inventory");
        addSafeWord("stuff that in there");
        addSafeWord("stuff that in the chest");
        addSafeWord("stuff the chest");
        addSafeWord("stuff the bag");
        
        addSafeWord("grind on");
        addSafeWord("grinding");
        addSafeWord("grind for");
        
        addSafeWord("hump that");
        addSafeWord("hump the");
        
        addSafeWord("tap that");
        addSafeWord("tap the");
        addSafeWord("tap it");
        
        addSafeWord("squeeze that");
        addSafeWord("squeeze the");
        addSafeWord("squeeze it");
        
        addSafeWord("bounce that");
        addSafeWord("bounce the");
        addSafeWord("bounce it");
        
        addSafeWord("shake that tree");
        addSafeWord("shake that bottle");
        addSafeWord("shake the bottle");
        addSafeWord("shake the tree");
        addSafeWord("shake it up");
        addSafeWord("shake it off");
        addSafeWord("shake your head");
        addSafeWord("shake your hand");
        
        addSafeWord("squeeze your hand");
        addSafeWord("squeeze your shoulder");
        addSafeWord("squeeze the trigger");
        addSafeWord("squeeze the lemon");
        addSafeWord("squeeze that lemon");
        
        addSafeWord("bounce that ball");
        addSafeWord("bounce the ball");
        addSafeWord("bounce it off the wall");
        addSafeWord("bounce them off");
        
        addSafeWord("jiggle that handle");
        addSafeWord("jiggle the key");
        addSafeWord("jiggle it loose");
        addSafeWord("jiggle them loose");
        
        addSafeWord("sniff that flower");
        addSafeWord("sniff your food");
        addSafeWord("sniff the air");
        addSafeWord("can i sniff it");
        addSafeWord("wanna sniff it");
        
        addSafeWord("gonna fill you up");
        addSafeWord("imma fill you up");
        addSafeWord("gonna stuff you");
        addSafeWord("imma stuff you");
        
        addSafeWord("motorboat");
        addSafeWord("motor boat");
        addSafeWord("on a motorboat");
        addSafeWord("riding a motorboat");
        
        addSafeWord("vibrate");
        addSafeWord("vibrating");
        addSafeWord("vibrating controller");
        addSafeWord("phone vibrate");
        
        addSafeWord("jiggle");
        addSafeWord("jiggling");
        addSafeWord("jiggle the handle");
        
        addSafeWord("bounce");
        addSafeWord("bouncing");
        addSafeWord("bounce the ball");
        addSafeWord("bouncing ball");
        
        addSafeWord("lay pipe");
        addSafeWord("laying pipe");
        addSafeWord("pipe laying");

        addSafeWord("grapevine");
        addSafeWord("grape juice");
        addSafeWord("grape soda");
        addSafeWord("eating grapes");
        addSafeWord("purple grape");
        addSafeWord("green grape");
        addSafeWord("grape flavor");
        
        addSafeWord("unalive");
        addSafeWord("feeling unalive");
        
        addSafeWord("diddle");
        addSafeWord("diddling with");
        addSafeWord("diddling around");
        addSafeWord("diddle around");
        
        addSafeWord("groom");
        addSafeWord("grooming");
        addSafeWord("dog grooming");
        addSafeWord("cat grooming");
        addSafeWord("pet grooming");
        addSafeWord("horse grooming");
        addSafeWord("grooming salon");
        addSafeWord("well groomed");
        addSafeWord("groom the dog");
        addSafeWord("groom the cat");
        addSafeWord("groom the horse");
        
        addSafeWord("choke");
        addSafeWord("choking");
        addSafeWord("choked");
        addSafeWord("choke on");
        addSafeWord("choking on");
        addSafeWord("choked on");
        addSafeWord("choke point");
        addSafeWord("chokepoint");
        addSafeWord("choke on food");
        addSafeWord("choking on food");
        
        addSafeWord("gas");
        addSafeWord("gasoline");
        addSafeWord("gas station");
        addSafeWord("gas tank");
        addSafeWord("gas pump");
        addSafeWord("out of gas");
        addSafeWord("need gas");
        addSafeWord("getting gas");
        addSafeWord("natural gas");
        addSafeWord("gas car");
        addSafeWord("gas vehicle");
        
        addSafeWord("neck");
        addSafeWord("my neck");
        addSafeWord("your neck");
        addSafeWord("sore neck");
        addSafeWord("stiff neck");
        addSafeWord("neck pain");
        addSafeWord("necklace");
        addSafeWord("turtleneck");
        addSafeWord("crewneck");
        addSafeWord("v neck");
        addSafeWord("necktie");
        addSafeWord("neck hurts");
        
        addSafeWord("oof");
        addSafeWord("big oof");
        addSafeWord("oof moment");
        addSafeWord("thats an oof");
        addSafeWord("that's an oof");
        addSafeWord("oof size");

        addSafeWord("they pick");
        addSafeWord("they picked");
        addSafeWord("they picking");
        addSafeWord("the pick");
        addSafeWord("the picked");
        addSafeWord("them pick");
        addSafeWord("their pick");
        addSafeWord("pick it");
        addSafeWord("pick up");
        addSafeWord("pick that");
        addSafeWord("pick the");
        addSafeWord("wont pick");
        addSafeWord("won't pick");
        addSafeWord("cant pick");
        addSafeWord("can't pick");
        addSafeWord("will pick");
        addSafeWord("should pick");

        addSafeWord("gonna make you lose");
        addSafeWord("gonna make you rage");
        addSafeWord("gonna make you quit");
        addSafeWord("gonna make you laugh");
        addSafeWord("gonna make you cry");  // "cry from laughter"
        addSafeWord("gonna make you smile");
        addSafeWord("gonna make you happy");
        addSafeWord("gonna make you proud");
        addSafeWord("gonna make you a sandwich");
        addSafeWord("gonna make you some food");
        addSafeWord("gonna make you something");
        addSafeWord("imma make you lose");
        addSafeWord("imma make you rage");
        addSafeWord("imma make you laugh");
        addSafeWord("ill make you laugh");
        addSafeWord("ill make you smile");
        addSafeWord("ill make you proud");
        addSafeWord("ill make you happy");
        addSafeWord("ill make you a deal");
        addSafeWord("ill make you an offer");
    }
    

    private void addSafeWord(String word) {
        whitelistedPhrases.add(word.toLowerCase());
    }

    public boolean isWhitelisted(String normalizedMessage) {
        String[] messageWords = normalizedMessage.trim().split("\\s+");
        
        for (String phrase : whitelistedPhrases) {
            if (phrase.contains(" ")) {
                if (normalizedMessage.contains(phrase)) {
                    return true;
                }
            } else {
                for (String word : messageWords) {
                    if (word.equals(phrase)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addWhitelistedPhrase(String phrase) {
        whitelistedPhrases.add(phrase.toLowerCase());
    }
    

    public void removeWhitelistedPhrase(String phrase) {
        whitelistedPhrases.remove(phrase.toLowerCase());
    }
}
