package net.squanchy.search.engines;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

final class StringNormalizer {

    private static final String EMPTY_STRING = "";
    private static final String ACCENTS_PATTERN_STRING = "\\p{M}";
    private static final Pattern ACCENTS_PATTERN = Pattern.compile(ACCENTS_PATTERN_STRING);

    static String normalize(String text) {
        String lowercasedText = lowercase(text);
        return removeDiacritics(lowercasedText);
    }

    private static String lowercase(String text) {
        return text.toLowerCase(Locale.getDefault());
    }

    private static String removeDiacritics(String text) {
        String normalised = Normalizer.normalize(text, Normalizer.Form.NFD);
        return ACCENTS_PATTERN.matcher(normalised).replaceAll(EMPTY_STRING);
    }
}
