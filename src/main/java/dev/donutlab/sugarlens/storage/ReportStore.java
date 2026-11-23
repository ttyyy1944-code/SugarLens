package dev.donutlab.sugarlens.storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.donutlab.sugarlens.detect.ServerAddress;
import dev.donutlab.sugarlens.model.ScanReport;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class ReportStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path root;
    public ReportStore(Path root) { this.root = root; }
    public boolean writeIfAbsent(ScanReport report) throws IOException {
        String serverId = ServerAddress.folderId(report.server_ip());
        String modeId = report.detected_mode().replaceAll("[^a-zA-Z0-9._-]", "_");
        Path folder = root.resolve(serverId).resolve(modeId).normalize();
        if (!folder.startsWith(root.normalize())) throw new IOException("Report path escapes scan directory");
        Path file = folder.resolve("report.json");
        if (Files.exists(file)) return false;
        Files.createDirectories(folder);
        try {
            Files.writeString(file, GSON.toJson(report), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            return true;
        } catch (FileAlreadyExistsException duplicate) { return false; }
    }
}
