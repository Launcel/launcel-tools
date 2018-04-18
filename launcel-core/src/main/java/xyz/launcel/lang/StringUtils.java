package xyz.launcel.lang;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public interface StringUtils {
    
    
    static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }
    
    
    static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
        return seq != null && searchSeq != null && indexOf(seq, searchSeq, 0) >= 0;
    }
    
    static boolean isNotEmpty(final CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }
    
    static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
    
    static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    static String capitalize(final String str) {
        if (isBlank(str)) {
            return null;
        }
        
        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            return str;
        }
        
        return Character.toTitleCase(firstChar) + str.substring(1);
    }
    
    static boolean isTrue(String s) {
        return "true".equalsIgnoreCase(s);
    }
    
    
    static boolean isFalse(String s) {
        return "false".equalsIgnoreCase(s);
    }
    
    static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    @SuppressWarnings("rawtypes")
    static Stream spiltStream(String strings, String split) {
        return Arrays.stream(strings.split(split)).filter(StringUtils::isNotBlank);
    }
    
    static void main(String[] args) {
        System.out.println(capitalize("a"));
    }
    
}

