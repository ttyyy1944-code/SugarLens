package dev.donutlab.sugarlens;
import dev.donutlab.sugarlens.detect.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CoreDetectionTest {
 @Test void normalizesHost() { assertEquals("play.example.net", ServerAddress.normalize("PLAY.Example.NET:25565")); }
 @Test void detectsBrand() { assertEquals("Paper", new BrandDetector().detect("Paper 1.21")); }
 @Test void fallsBackToDefaultMode() { assertEquals("survival_default", new ModeDetector().detect("", "", "")); }
 @Test void readsScoreboardFirst() { assertEquals("pvp_box", new ModeDetector().detect("PvP Box", "Anarchy", "Lobby")); }
}
