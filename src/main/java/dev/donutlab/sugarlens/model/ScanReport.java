package dev.donutlab.sugarlens.model;
import java.time.Instant;
public record ScanReport(String timestamp, String server_ip, String detected_mode, String brand_info, LatencySummary latency) {
    public static ScanReport create(String ip, String mode, String brand, LatencySummary latency) {
        return new ScanReport(Instant.now().toString(), ip, mode, brand, latency);
    }
}
