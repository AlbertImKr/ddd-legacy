package stringcalculator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RegexCache {

    private static final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    private RegexCache() {
        throw new IllegalStateException("Utility class");
    }

    public static Pattern getPattern(String regex) {
        return patternCache.computeIfAbsent(regex, Pattern::compile);
    }

    public static boolean matches(String regex, CharSequence input) {
        return getPattern(regex).matcher(input).matches();
    }
}
