package dev.donutlab.sugarlens.detect;
import java.util.Locale;

public final class BrandDetector {
    public String detect(String payload) {
        if (payload == null || payload.isBlank()) return "Unknown";
        String value = payload.toLowerCase(Locale.ROOT);
        if (value.contains("velocity")) return "Velocity";
        if (value.contains("bungeecord") || value.contains("bungee")) return "BungeeCord";
        if (value.contains("paper")) return "Paper";
        if (value.contains("spigot")) return "Spigot";
        if (value.contains("fabric")) return "Fabric";
        if (value.contains("vanilla")) return "Vanilla";
        return payload.trim();
    }
}
