package dev.donutlab.sugarlens.detect;
import java.util.List;
import java.util.Locale;

public final class ModeDetector {
    public static final String FALLBACK = "survival_default";
    public String detect(String scoreboardTitle, String bossBarTitle, String tabHeader) {
        for (String candidate : List.of(safe(scoreboardTitle), safe(bossBarTitle), safe(tabHeader))) {
            String normalized = sanitize(candidate);
            if (!normalized.isBlank()) return normalized;
        }
        return FALLBACK;
    }
    private static String safe(String value) { return value == null ? "" : value; }
    private static String sanitize(String value) {
        String plain = value.replaceAll("§[0-9A-FK-ORa-fk-or]", "").replaceAll("<[^>]*>", "").trim().toLowerCase(Locale.ROOT);
        return plain.replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
    }
}
