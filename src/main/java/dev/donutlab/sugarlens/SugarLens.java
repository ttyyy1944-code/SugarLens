package dev.donutlab.sugarlens;

import dev.donutlab.sugarlens.session.ScanSession;
import dev.donutlab.sugarlens.detect.BrandDetector;
import dev.donutlab.sugarlens.detect.ModeDetector;
import dev.donutlab.sugarlens.storage.ReportStore;
import java.nio.file.Path;

/** Public entry point for SugarLens Core. */
public final class SugarLens {
    private SugarLens() {}
    public static ScanSession begin(String serverAddress, Path gameDirectory) {
        return new ScanSession(serverAddress, new BrandDetector(), new ModeDetector(),
                new ReportStore(gameDirectory.resolve("donutlab_scans")));
    }
}
