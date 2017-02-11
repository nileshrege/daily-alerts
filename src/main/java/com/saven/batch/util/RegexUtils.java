package com.saven.batch.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nrege on 1/30/2017.
 */
public class RegexUtils {

    public static String replaceVarWithVal(String text, String regex, Function<String, String> replacer, String defaultValue) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacer.apply(matcher.group(1).toUpperCase());
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null) {
                if (defaultValue != null) {
                    builder.append(defaultValue);
                }
                else {
                    builder.append(matcher.group(0));
                }
            }
            else {
                builder.append(replacement);
            }
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }

}
