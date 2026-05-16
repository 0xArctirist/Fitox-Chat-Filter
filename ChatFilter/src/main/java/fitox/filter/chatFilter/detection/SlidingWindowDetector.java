package fitox.filter.chatFilter.detection;

import java.util.ArrayList;
import java.util.List;


public class SlidingWindowDetector {
    
    private final List<String> blockedWords;
    private final int maxWordLength;
    
    public SlidingWindowDetector(List<String> blockedWords) {
        this.blockedWords = blockedWords;

        int max = 0;
        for (String word : blockedWords) {
            if (word.length() > max) {
                max = word.length();
            }
        }
        this.maxWordLength = max;
    }

    public List<String> findBadWords(String input) {
        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> foundWords = new ArrayList<>();

        for (int start = 0; start < input.length(); start++) {
            int maxOffset = Math.min(input.length() - start, maxWordLength);
            
            for (int offset = 1; offset <= maxOffset; offset++) {
                String substring = input.substring(start, start + offset);

                if (blockedWords.contains(substring)) {
                    if (!foundWords.contains(substring)) {
                        foundWords.add(substring);
                    }
                }
            }
        }
        
        return foundWords;
    }

    public boolean containsBadWord(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        for (int start = 0; start < input.length(); start++) {
            int maxOffset = Math.min(input.length() - start, maxWordLength);
            
            for (int offset = 1; offset <= maxOffset; offset++) {
                String substring = input.substring(start, start + offset);

                if (blockedWords.contains(substring)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }
}
