package fitox.filter.chatFilter.util;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class TextNormalizer {
    
    private static final Map<Character, Character> symbolMap = new HashMap<>();
    
    static {
        symbolMap.put('0', 'o');
        symbolMap.put('1', 'i');
        symbolMap.put('3', 'e');
        symbolMap.put('4', 'a');
        symbolMap.put('5', 's');
        symbolMap.put('6', 'g');
        symbolMap.put('7', 't');
        symbolMap.put('8', 'b');
        symbolMap.put('9', 'g');

        symbolMap.put('@', 'a');
        symbolMap.put('$', 's');
        symbolMap.put('!', 'i');
        symbolMap.put('|', 'i');
        symbolMap.put('(', 'c');
        symbolMap.put('<', 'c');
        symbolMap.put('+', 't');
        symbolMap.put('*', 'a');
        symbolMap.put('/', 'i');
        symbolMap.put('\\', 'i');
        symbolMap.put('[', 'c');
        symbolMap.put(']', 'c');
        symbolMap.put('{', 'c');
        symbolMap.put('}', 'c');
        symbolMap.put('&', 'a');
        symbolMap.put('%', 'x');
        symbolMap.put('#', 'h');
        symbolMap.put('~', 'n');
        symbolMap.put('^', 'a');
        symbolMap.put('=', 'e');
        symbolMap.put('_', 'i');
        symbolMap.put('-', 'i');
        symbolMap.put('`', 'i');
        symbolMap.put('\'', 'i');
        symbolMap.put('"', 'i');
        symbolMap.put('.', 'o');
        symbolMap.put(',', 'i');
        symbolMap.put(';', 'i');
        symbolMap.put('?', 'o');

        symbolMap.put('а', 'a'); // Cyrillic a
        symbolMap.put('е', 'e'); // Cyrillic e
        symbolMap.put('і', 'i'); // Cyrillic i
        symbolMap.put('о', 'o'); // Cyrillic o
        symbolMap.put('р', 'p'); // Cyrillic p
        symbolMap.put('с', 'c'); // Cyrillic c
        symbolMap.put('у', 'y'); // Cyrillic y
        symbolMap.put('х', 'x'); // Cyrillic x
        symbolMap.put('ѕ', 's'); // Cyrillic s
        symbolMap.put('һ', 'h'); // Cyrillic h
        symbolMap.put('ј', 'j'); // Cyrillic j
        symbolMap.put('ԁ', 'd'); // Cyrillic d
        symbolMap.put('ѵ', 'v'); // Cyrillic v
        symbolMap.put('г', 'r'); // Cyrillic r
        symbolMap.put('п', 'n'); // Cyrillic n
        symbolMap.put('в', 'b'); // Cyrillic b
        symbolMap.put('к', 'k'); // Cyrillic k
        symbolMap.put('м', 'm'); // Cyrillic m
        symbolMap.put('т', 't'); // Cyrillic t

        symbolMap.put('α', 'a'); // Greek alpha
        symbolMap.put('ο', 'o'); // Greek omicron
        symbolMap.put('υ', 'u'); // Greek upsilon
        symbolMap.put('κ', 'k'); // Greek kappa
        symbolMap.put('ν', 'v'); // Greek nu
        symbolMap.put('τ', 't'); // Greek tau
        symbolMap.put('ε', 'e'); // Greek epsilon
        symbolMap.put('ι', 'i'); // Greek iota
        symbolMap.put('ρ', 'p'); // Greek rho

        symbolMap.put('ո', 'n'); // Armenian n
        symbolMap.put('օ', 'o'); // Armenian o
        symbolMap.put('ս', 's'); // Armenian s
    }

    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        String normalized = text;

        normalized = removeZeroWidthChars(normalized);

        normalized = convertPipePatterns(normalized);

        normalized = normalized.toLowerCase();

        normalized = removeFancyUnicode(normalized);

        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // Remove diacritics

        normalized = normalized.replaceAll("88", "gg");

        normalized = convertSymbols(normalized);

        normalized = normalized.replaceAll("[^a-z0-9\\s]", "");

        normalized = removeRepeatedLetters(normalized);
        
        return normalized;
    }
    public static String normalizeSuperAggressive(String text) {
        String normalized = normalize(text);
        normalized = normalized.replaceAll("(.)\\1+", "$1");
        
        return normalized;
    }
    

    private static String convertPipePatterns(String text) {
        text = text.replaceAll("\\|\\s*6\\s*6\\s*@", "nigga");
        text = text.replaceAll("\\|\\s*6\\s*6\\s*4", "nigga");
        text = text.replaceAll("\\|\\s*1\\s*6\\s*6\\s*@", "nigga");
        text = text.replaceAll("\\|\\s*1\\s*6\\s*6\\s*4", "nigga");

        // ||\|| = N (double pipe with backslash pipe)
        text = text.replaceAll("\\|\\|\\\\\\|\\|", "n");
        text = text.replaceAll("\\|\\|/\\|\\|", "n");
        text = text.replaceAll("\\|\\|\\s*\\\\\\s*\\|\\|", "n"); // w spaces
        text = text.replaceAll("\\|\\|\\s*/\\s*\\|\\|", "n"); // w spaces
        
        // |\| or |\\| = N (single pipe patterns)
        text = text.replaceAll("\\|\\\\\\|", "n");
        text = text.replaceAll("\\|/\\|", "n");
        text = text.replaceAll("\\|\\s*\\\\\\s*\\|", "n"); // w spaces
        text = text.replaceAll("\\|\\s*/\\s*\\|", "n"); // w spaces
        
        // /\/ = N
        text = text.replaceAll("/\\\\/", "n");
        text = text.replaceAll("/\\s*\\\\\\s*/", "n"); // w spaces
        
        // |\/| = M
        text = text.replaceAll("\\|\\\\/\\|", "m");
        text = text.replaceAll("\\|\\s*\\\\\\s*/\\s*\\|", "m"); // w spaces
        
        // /\/\ = M
        text = text.replaceAll("/\\\\/\\\\", "m");
        text = text.replaceAll("/\\s*\\\\\\s*/\\s*\\\\", "m"); // w spaces
        
        // |_| = U
        text = text.replaceAll("\\|_\\|", "u");
        text = text.replaceAll("\\|\\s*_\\s*\\|", "u"); // w spaces
        
        // /_\ = A
        text = text.replaceAll("/_\\\\", "a");
        text = text.replaceAll("/\\s*_\\s*\\\\", "a"); // w spaces
        
        // |< = K
        text = text.replaceAll("\\|<", "k");
        text = text.replaceAll("\\|\\s*<", "k"); // w spaces
        
        // |) = D
        text = text.replaceAll("\\|\\)", "d");
        text = text.replaceAll("\\|\\s*\\)", "d"); // w spaces
        
        // |- = F
        text = text.replaceAll("\\|-", "f");
        text = text.replaceAll("\\|\\s*-", "f"); // w spaces
        
        // |= = E
        text = text.replaceAll("\\|=", "e");
        text = text.replaceAll("\\|\\s*=", "e"); // w spaces
        
        // () = O
        text = text.replaceAll("\\(\\)", "o");
        text = text.replaceAll("\\(\\s*\\)", "o"); // w spaces
        
        // [] = O or D
        text = text.replaceAll("\\[\\]", "o");
        text = text.replaceAll("\\[\\s*\\]", "o"); // w spaces
        
        // >< = X
        text = text.replaceAll("><", "x");
        text = text.replaceAll(">\\s*<", "x"); // w spaces
        
        // \\/ = V
        text = text.replaceAll("\\\\/", "v");
        text = text.replaceAll("\\\\\\s*/", "v"); // w spaces
        
        // /\\ = A
        text = text.replaceAll("/\\\\", "a");
        text = text.replaceAll("/\\s*\\\\", "a"); // w spaces
        
        return text;
    }

    private static String removeZeroWidthChars(String text) {
        return text.replaceAll("[\\u200B\\u200C\\u200D\\uFEFF\\u00AD\\u2060\\u180E]", "");
    }
    

    public static String normalizeAggressive(String text) {
        String normalized = normalize(text);

        normalized = normalized.replaceAll("\\s+", "");
        
        return normalized;
    }

    public static String removeSpacing(String text) {
        return text.replaceAll("\\s+", "");
    }

    private static String convertSymbols(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            result.append(symbolMap.getOrDefault(c, c));
        }
        return result.toString();
    }

    private static String removeRepeatedLetters(String text) {
        return text.replaceAll("(.)\\1{2,}", "$1$1");
    }

    private static String removeFancyUnicode(String text) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int codePoint = text.codePointAt(i);

            if (Character.isHighSurrogate(c)) {
                i++;
                if (i < text.length()) {
                    codePoint = Character.toCodePoint(c, text.charAt(i));
                }
            }

            char normalized = normalizeFancyChar(codePoint);
            result.append(normalized);
        }
        
        return result.toString();
    }

    private static char normalizeFancyChar(int codePoint) {
        // mathematical Bold (𝐀-𝐙, 𝐚-𝐳)
        if (codePoint >= 0x1D400 && codePoint <= 0x1D419) return (char) ('A' + (codePoint - 0x1D400));
        if (codePoint >= 0x1D41A && codePoint <= 0x1D433) return (char) ('a' + (codePoint - 0x1D41A));
        
        // mathematical Italic (𝐴-𝑍, 𝑎-𝑧)
        if (codePoint >= 0x1D434 && codePoint <= 0x1D44D) return (char) ('A' + (codePoint - 0x1D434));
        if (codePoint >= 0x1D44E && codePoint <= 0x1D467) return (char) ('a' + (codePoint - 0x1D44E));
        
        // mathematical Bold Italic (𝑨-𝒁, 𝒂-𝒛)
        if (codePoint >= 0x1D468 && codePoint <= 0x1D481) return (char) ('A' + (codePoint - 0x1D468));
        if (codePoint >= 0x1D482 && codePoint <= 0x1D49B) return (char) ('a' + (codePoint - 0x1D482));

        // mathematical Script (𝒜-𝒵, 𝒶-𝓏)
        if (codePoint >= 0x1D49C && codePoint <= 0x1D4B5) return (char) ('A' + (codePoint - 0x1D49C));
        if (codePoint >= 0x1D4B6 && codePoint <= 0x1D4CF) return (char) ('a' + (codePoint - 0x1D4B6));

        // mathematical Fraktur (𝔄-𝔜, 𝔞-𝔷)
        if (codePoint >= 0x1D504 && codePoint <= 0x1D51C) return (char) ('A' + (codePoint - 0x1D504));
        if (codePoint >= 0x1D51E && codePoint <= 0x1D537) return (char) ('a' + (codePoint - 0x1D51E));

        // circled letters (Ⓐ-Ⓩ, ⓐ-ⓩ)
        if (codePoint >= 0x24B6 && codePoint <= 0x24CF) return (char) ('A' + (codePoint - 0x24B6));
        if (codePoint >= 0x24D0 && codePoint <= 0x24E9) return (char) ('a' + (codePoint - 0x24D0));

        // fullwidth (Ａ-Ｚ, ａ-ｚ)
        if (codePoint >= 0xFF21 && codePoint <= 0xFF3A) return (char) ('A' + (codePoint - 0xFF21));
        if (codePoint >= 0xFF41 && codePoint <= 0xFF5A) return (char) ('a' + (codePoint - 0xFF41));
        
        return (char) codePoint;
    }
    

    public static boolean containsInvisibleChars(String text) {
        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c) && c != ' ') return true;
            if (c == '\u200B' || c == '\u200C' || c == '\u200D') return true;
            if (c == '\uFEFF') return true;
        }
        return false;
    }
}
