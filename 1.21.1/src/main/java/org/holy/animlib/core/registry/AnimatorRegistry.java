package org.holy.animlib.core.registry;

import net.minecraft.client.gui.screens.Screen;
import org.holy.animlib.runtime.animator.ScreenAnimator;

import java.util.Map;
import java.util.WeakHashMap;

public final class AnimatorRegistry {
    private static final Map<Screen, ScreenAnimator> MAP = new WeakHashMap<>();

    private AnimatorRegistry() {
    }

    public static ScreenAnimator get(Screen screen) {
        return MAP.computeIfAbsent(screen, s -> new ScreenAnimator());
    }

    public static void remove(Screen screen) {
        MAP.remove(screen);
    }
}
