package dev.donutlab.sugarlens;
import dev.donutlab.sugarlens.model.ScanReport;
import dev.donutlab.sugarlens.storage.ReportStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
class ReportStoreTest {
 @TempDir Path temp;
 @Test void writesOnlyOnce() throws Exception {
  ReportStore store = new ReportStore(temp.resolve("donutlab_scans"));
  ScanReport report = ScanReport.create("play.example.net", "survival_default", "Paper", null);
  assertTrue(store.writeIfAbsent(report)); assertFalse(store.writeIfAbsent(report));
 }
}
