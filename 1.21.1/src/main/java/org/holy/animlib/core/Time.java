package org.holy.animlib.core;

public final class Time {
    private static long lastTime = System.nanoTime();
    private static float deltaTime;

    private Time() {
    }

    public static void update() {
        long now = System.nanoTime();
        deltaTime = (now - lastTime) / 1_000_000_000f;
        lastTime = now;

        if (deltaTime > 0.05f) {
            deltaTime = 0.05f;
        }
    }

    public static float getDelta() {
        return deltaTime;
    }
}
