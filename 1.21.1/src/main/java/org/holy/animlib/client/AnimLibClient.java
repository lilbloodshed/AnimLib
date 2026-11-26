package org.holy.animlib.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.holy.animlib.AnimLibMod;
import org.lwjgl.glfw.GLFW;

public final class AnimLibClient {
    private static final KeyMapping OPEN_TEST_SCREEN = new KeyMapping(
            "key.animlib.open_test_screen",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.animlib"
    );

    private AnimLibClient() {
    }

    @EventBusSubscriber(modid = AnimLibMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static final class ModEvents {
        private ModEvents() {
        }

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            if (AnimLibMod.DEV_MODE) {
                event.register(OPEN_TEST_SCREEN);
            }
        }
    }

    @EventBusSubscriber(modid = AnimLibMod.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeEvents {
        private ForgeEvents() {
        }

        @SubscribeEvent
        public static void clientTick(ClientTickEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (AnimLibMod.DEV_MODE) {
                while (OPEN_TEST_SCREEN.consumeClick()) {
                    if (minecraft.player != null && minecraft.screen == null) {
                        minecraft.setScreen(new AnimLibTestScreen());
                    }
                }
            }
        }
    }
}
