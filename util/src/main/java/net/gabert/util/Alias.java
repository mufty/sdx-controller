package net.gabert.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Alias {
    private final static String ALIAS_PATTERN_STRING = "#\\{(.*?)\\}";
    private final static Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_PATTERN_STRING);

    public static List<String> getAliases(String lookupString) {
        final Matcher m = ALIAS_PATTERN.matcher(lookupString);

        return new ArrayList<String>() {{
            while(m.find()) {
                add(m.group(1));
            }
        }};
    }

    public static String normalize(String aliasedString, Map<String, String> aliases) {
        final Matcher m = ALIAS_PATTERN.matcher(aliasedString);

        String normalizedString = aliasedString;

        while(m.find()) {
            String aliasName = m.group(1);
            String aliasValue = aliases.get(aliasName);

            if (aliasValue == null) {
                throw new NullPointerException("Value for alias [" + aliasName + "] not found.");
            }

            normalizedString = normalizedString.replace(adaptAlias(aliasName),
                                                        aliasValue);
        }

        return normalizedString;
    }

    private static String adaptAlias(String aliasName) {
        return "#{" + aliasName + "}";
    }
}
