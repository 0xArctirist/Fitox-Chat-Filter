package fitox.filter.chatFilter.detection;

public class LevenshteinMatcher {

    public static int distance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return Integer.MAX_VALUE;
        }
        
        if (s1.equals(s2)) {
            return 0;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) > 2) {
            return Math.abs(len1 - len2);
        }

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                
                dp[i][j] = Math.min(
                    Math.min(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1
                    ),
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        
        return dp[len1][len2];
    }

    public static boolean isSimilar(String word, Iterable<String> wordList, int threshold) {
        if (word == null || word.length() < 4) {
            return false;
        }
        
        for (String blockedWord : wordList) {
            if (Math.abs(word.length() - blockedWord.length()) <= threshold) {
                int dist = distance(word, blockedWord);
                if (dist <= threshold) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static String findClosestMatch(String word, Iterable<String> wordList, int threshold) {
        if (word == null || word.length() < 4) {
            return null;
        }
        
        String closestMatch = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (String blockedWord : wordList) {
            if (Math.abs(word.length() - blockedWord.length()) <= threshold) {
                int dist = distance(word, blockedWord);
                if (dist < minDistance && dist <= threshold) {
                    minDistance = dist;
                    closestMatch = blockedWord;
                }
            }
        }
        
        return closestMatch;
    }
}
