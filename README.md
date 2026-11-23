# SugarLens v1.0 — Diagnóstico & Escáner

SugarLens Core is an independent observation and recording module for DonutLab / Sugar Series. It captures a Minecraft server endpoint, detected server software, active game mode and early-session latency, then writes one JSON report per unique server + mode pair.

## Features
- Normalize host names to lowercase and remove the default :25565 port.
- Detect Paper, Spigot, Fabric, BungeeCord, Velocity or Vanilla from the minecraft:brand payload.
- Resolve mode names from scoreboard title, boss bar, then tab header; fallback: survival_default.
- Track min/max/average KeepAlive RTT during the first 30 seconds.
- Write donutlab_scans/<server-id>/<mode-id>/report.json.
- Skip existing reports to avoid duplicates.

## Build
Requires JDK 21 and Gradle 8.7+.

```bash
gradle test
gradle build
```

## Minecraft integration
The core has no Minecraft runtime dependency. A loader-specific adapter should:
1. Pass ServerData.serverIP to SugarLens.begin() on connection.
2. Pass the decoded minecraft:brand custom payload to recordBrandPayload().
3. Supply scoreboard title, boss-bar title and tab header to recordModeSignals().
4. Pair KeepAlive IDs using onKeepAliveSent(id, System.nanoTime()) and onKeepAliveReceived(id, System.nanoTime()).
5. Call finishAndWrite() on disconnect or scan completion.

The adapter must use supported client APIs/mixins for the target loader/version and must not block render or network threads during file writes.

## Report
The required fields are timestamp (ISO-8601 UTC), server_ip, detected_mode and brand_info. The optional latency object contains min_ms, max_ms, average_ms, samples and window_seconds.

## Privacy
Reports are local only. SugarLens does not upload server addresses or scan data. Do not commit generated scan reports.
