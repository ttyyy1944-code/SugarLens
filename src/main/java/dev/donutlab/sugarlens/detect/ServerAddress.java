package dev.donutlab.sugarlens.detect;
import java.util.Locale;

public final class ServerAddress {
    private ServerAddress() {}
    public static String normalize(String input) {
        if (input == null || input.isBlank()) throw new IllegalArgumentException("Server address cannot be blank");
        String value = input.trim().toLowerCase(Locale.ROOT);
        return value.endsWith(":25565") ? value.substring(0, value.length() - 6) : value;
    }
    public static String folderId(String input) {
        return normalize(input).replaceAll("[^a-z0-9._-]", "_");
    }
}
