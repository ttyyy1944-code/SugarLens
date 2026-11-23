package dev.donutlab.sugarlens.fabric;

import dev.donutlab.sugarlens.SugarLens;
import dev.donutlab.sugarlens.session.ScanSession;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/** Fabric lifecycle and live client-signal adapter. */
public final class SugarLensFabric implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("SugarLens");
    private static volatile ScanSession activeSession;

    public static void recordBrandPayload(String payload) {
        ScanSession session = activeSession;
        if (session != null) session.recordBrandPayload(payload);
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerData server = client.getCurrentServer();
            if (server == null || server.ip == null || server.ip.isBlank()) {
                activeSession = null;
                return;
            }

            try {
                Path gameDirectory = client.gameDirectory.toPath();
                activeSession = SugarLens.begin(server.ip, gameDirectory);
                LOGGER.info("SugarLens scan started for {}", server.ip);
            } catch (IllegalArgumentException error) {
                activeSession = null;
                LOGGER.warn("Could not start SugarLens scan: {}", error.getMessage());
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ScanSession session = activeSession;
            if (session == null || client.level == null) return;
            try {
                var scoreboard = client.level.getScoreboard();
                var sidebar = scoreboard.getDisplayObjective(net.minecraft.world.scores.DisplaySlot.SIDEBAR);
                String title = sidebar == null ? "" : sidebar.getDisplayName().getString();
                session.recordModeSignals(title, "", "");
            } catch (RuntimeException error) {
                LOGGER.debug("Could not sample scoreboard signal.", error);
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ScanSession session = activeSession;
            activeSession = null;
            if (session == null) return;

            try {
                boolean created = session.finishAndWrite();
                LOGGER.info(created ? "SugarLens report saved." : "SugarLens report already exists; duplicate skipped.");
            } catch (IOException error) {
                LOGGER.error("Could not write SugarLens report.", error);
            }
        });

        LOGGER.info("SugarLens Fabric adapter initialized.");
    }
}
