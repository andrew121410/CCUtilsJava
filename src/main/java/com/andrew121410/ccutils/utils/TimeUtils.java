package com.andrew121410.ccutils.utils;

public class TimeUtils {

    /**
     * theStartTime and theEndTime need to be milliseconds
     */
    public static String makeIntoEnglishWords(long theStartTime, long theEndTime, boolean shortText, boolean includeSeconds) {
        StringBuilder stringBuilder = new StringBuilder();

        long timeElapsed = theEndTime - theStartTime;

        long days = timeElapsed / (1000 * 60 * 60 * 24);
        long hours = (timeElapsed % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeElapsed % (1000 * 60)) / 1000;

        if (days > 0) {
            if (days == 1) {
                stringBuilder.append(days).append(" day, ");
            } else {
                stringBuilder.append(days).append(" days, ");
            }
        }

        if (hours > 0) {
            if (hours == 1) {
                stringBuilder.append(hours).append(" hour, ");
            } else {
                stringBuilder.append(hours).append(" hours, ");
            }
        }

        if (minutes > 0) {
            if (minutes == 1) {
                stringBuilder.append(minutes);
                if (shortText) {
                    stringBuilder.append(" min, ");
                } else {
                    stringBuilder.append(" minute, ");
                }
            } else {
                stringBuilder.append(minutes);
                if (shortText) {
                    stringBuilder.append(" mins, ");
                } else {
                    stringBuilder.append(" minutes, ");
                }
            }
            if (seconds > 0 && includeSeconds) {
                stringBuilder.append("and ");
            }
        }

        if (seconds > 0 && includeSeconds) {
            if (seconds == 1) {
                stringBuilder.append(seconds);
                if (shortText) {
                    stringBuilder.append(" sec");
                } else {
                    stringBuilder.append(" second");
                }
            } else {
                stringBuilder.append(seconds);
                if (shortText) {
                    stringBuilder.append(" secs");
                } else {
                    stringBuilder.append(" seconds");
                }
            }
        }

        return stringBuilder.toString();
    }
}