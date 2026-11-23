# SugarLens v1.1 — Fabric adapter

SugarLens is a client-side Minecraft server diagnostics mod and reusable Java core for DonutLab / Sugar Series.

## Target
- Minecraft Java 1.21.11
- Java 21
- Fabric Loader 0.18.1
- Fabric API 0.141.1+1.21.11
- Fabric Loom 1.14

## Current implementation
- Server address normalization.
- Server software brand classification from a supplied brand payload.
- Game mode signal normalization from scoreboard, boss bar and tab header.
- KeepAlive RTT aggregation for the first 30 seconds when packet timestamps are supplied.
- JSON report output under `<game-directory>/donutlab_scans/<server>/<mode>/report.json`.
- Duplicate report prevention.
- Fabric client join/disconnect lifecycle adapter.
- GitHub Actions build and test workflow.

## Important integration status
The Fabric entrypoint currently starts a scan session on joining a multiplayer server and writes it on disconnect. The packet-level hooks that feed the brand payload, scoreboard/boss-bar/tab signals and KeepAlive timestamps are **not wired yet**. Until those hooks are added, reports use `brand_info: "Unknown"`, `detected_mode: "survival_default"` and omit latency unless data is supplied by another adapter. Do not treat these fields as detected values when the corresponding signal was not captured.

## Build
Requires JDK 21 and Gradle 9.2.1 or newer.

```bash
gradle clean test build
```

GitHub Actions runs the same build on pushes and pull requests.

## Privacy
Reports remain on the local game device. SugarLens does not transmit server addresses or report contents.
