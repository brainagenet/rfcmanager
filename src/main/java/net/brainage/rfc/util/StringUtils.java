/*
 * (#) net.brainage.rfc.util.StringUtils.java
 * Created on 2011. 6. 7.
 */
package net.brainage.rfc.util;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public final class StringUtils
{

    public static final String EMPTY = "";

    public static final String[] EMPTY_ARRAY = new String[] {};

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasText(String text) {
        if (!hasLength(text)) {
            return false;
        }

        for (int i = 0, l = text.length(); i < l; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String toFirstUpperCase(String str) {
        if (isEmpty(str)) {
            throw new IllegalArgumentException("str must not be null or empty.");
        }

        if (str.length() == 1) {
            return str.toUpperCase();
        }

        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}
