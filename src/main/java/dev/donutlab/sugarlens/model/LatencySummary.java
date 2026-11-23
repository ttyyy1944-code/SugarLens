package dev.donutlab.sugarlens.model;
public record LatencySummary(double min_ms, double max_ms, double average_ms, int samples, int window_seconds) {}
