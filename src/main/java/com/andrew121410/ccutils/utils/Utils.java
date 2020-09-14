package com.andrew121410.ccutils.utils;

import java.util.List;

public class Utils {

    public static boolean isByte(String input) {
        try {
            Byte.parseByte(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Byte asByteOrElse(String input, Byte fallback) {
        try {
            return Byte.parseByte(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isShort(String input) {
        try {
            Short.parseShort(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Short asShortOrElse(String input, Short fallback) {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer asIntegerOrElse(String input, Integer fallback) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Long asLongOrElse(String input, Long fallback) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Float asFloatOrElse(String input, Float fallback) {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Double asDoubleOrElse(String input, Double fallback) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static boolean isBoolean(String input) {
        try {
            Boolean.parseBoolean(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Boolean asBooleanOrElse(String input, Boolean fallback) {
        try {
            return Boolean.parseBoolean(input);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static String getIndexFromStringList(List<String> stringList, int index) {
        if (index >= 0 && index < stringList.size()) {
            return stringList.get(index);
        }
        return null;
    }

    public String getLastStrings(String myString, int index) {
        if (myString.length() > index)
            return myString.substring(myString.length() - index);
        else
            return myString;
    }

    public boolean isClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
