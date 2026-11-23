package dev.donutlab.sugarlens.fabric;

import dev.donutlab.sugarlens.SugarLens;
import dev.donutlab.sugarlens.session.ScanSession;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Initial Fabric lifecycle adapter.
 *
 * Packet-level brand and KeepAlive hooks are intentionally kept for the next
 * integration step; this adapter currently owns session lifecycle only.
 */
public final class SugarLensFabric implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("SugarLens");
    private static volatile ScanSession activeSession;

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
