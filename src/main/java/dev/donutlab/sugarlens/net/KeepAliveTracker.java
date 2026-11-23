package dev.donutlab.sugarlens.net;
import dev.donutlab.sugarlens.model.LatencySummary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class KeepAliveTracker {
    private static final long WINDOW = 30_000_000_000L;
    private final long started;
    private final Map<Long, Long> sent = new ConcurrentHashMap<>();
    private int samples; private double min = Double.POSITIVE_INFINITY, max, total;
    public KeepAliveTracker(long startedAtNanos) { started = startedAtNanos; }
    public void onSent(long id, long now) { if (now - started <= WINDOW) sent.put(id, now); }
    public synchronized void onReceived(long id, long now) {
        Long then = sent.remove(id);
        if (then == null || now - started > WINDOW) return;
        double ms = Math.max(0, (now - then) / 1_000_000.0);
        min = Math.min(min, ms); max = Math.max(max, ms); total += ms; samples++;
    }
    public synchronized LatencySummary summary() {
        return samples == 0 ? null : new LatencySummary(min, max, total / samples, samples, 30);
    }
}
