package dev.donutlab.sugarlens.session;
import dev.donutlab.sugarlens.detect.*;
import dev.donutlab.sugarlens.model.ScanReport;
import dev.donutlab.sugarlens.net.KeepAliveTracker;
import dev.donutlab.sugarlens.storage.ReportStore;
import java.io.IOException;

public final class ScanSession {
    private final String serverIp; private final BrandDetector brands; private final ModeDetector modes;
    private final ReportStore store; private final KeepAliveTracker latency;
    private volatile String brand = "Unknown", scoreboard = "", bossBar = "", tab = "";
    public ScanSession(String address, BrandDetector brands, ModeDetector modes, ReportStore store) {
        serverIp = ServerAddress.normalize(address); this.brands = brands; this.modes = modes; this.store = store;
        latency = new KeepAliveTracker(System.nanoTime());
    }
    public void recordBrandPayload(String payload) { brand = brands.detect(payload); }
    public void recordModeSignals(String scoreboard, String bossBar, String tab) {
        this.scoreboard = scoreboard == null ? "" : scoreboard;
        this.bossBar = bossBar == null ? "" : bossBar;
        this.tab = tab == null ? "" : tab;
    }
    public void onKeepAliveSent(long id, long nanoTime) { latency.onSent(id, nanoTime); }
    public void onKeepAliveReceived(long id, long nanoTime) { latency.onReceived(id, nanoTime); }
    public boolean finishAndWrite() throws IOException {
        return store.writeIfAbsent(ScanReport.create(serverIp, modes.detect(scoreboard, bossBar, tab), brand, latency.summary()));
    }
}
