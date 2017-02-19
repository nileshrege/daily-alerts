package com.saven.batch.domain.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nrege on 1/30/2017.
 */
public class RegexUtils {

    public static boolean matches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static String replaceVarWithVal(String text, String regex, Function<String, String> replacer, String defaultValue) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String replacement = replacer.apply(group);
            text = text.replace(group, replacement);
        }
        return text;
    }

}
