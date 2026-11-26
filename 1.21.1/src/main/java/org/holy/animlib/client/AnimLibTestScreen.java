package org.holy.animlib.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.holy.animlib.api.AnimLib;
import org.holy.animlib.api.effects.AnimEffect;
import org.holy.animlib.api.effects.AnimEffects;

public class AnimLibTestScreen extends Screen {
    private static final int TEXT_COLOR = 0xD9CAD5;
    private static final ResourceLocation TEST_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/diamond.png");

    public AnimLibTestScreen() {
        super(Component.literal("AnimLib Test"));
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("Close"), button -> onClose())
                .pos(width / 2 - 30, height - 32)
                .size(60, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int left = width / 2 - 150;
        int top = 28;

        renderTextRows(graphics, left, top + 16);
        renderTextureRows(graphics, left + 190, top + 16);
    }

    private void renderTextRows(GuiGraphics graphics, int x, int y) {
        drawTextEffect(graphics, "wave", x, y, AnimEffects.wave(2f, 1f, 0.7f));
        drawTextEffect(graphics, "bounce", x, y + 18, AnimEffects.bounce(2.5f, 1.0f, 0.6f));
        drawTextEffect(graphics, "shake", x, y + 36, AnimEffects.shake(1f, 1f));
        drawTextEffect(graphics, "wiggle", x, y + 54, AnimEffects.wiggle(1.2f, 0.8f, 0.6f));
        drawTextEffect(graphics, "swing", x, y + 72, AnimEffects.swing(18f, 1f, 0.18f));
        drawTextEffect(graphics, "pendulum", x, y + 90, AnimEffects.pendulum(10f, 1.2f, 1f));
        drawTextEffect(graphics, "turbulence", x, y + 108, AnimEffects.turbulence(1f, 1f));
        drawTextEffect(graphics, "glitch", x, y + 126, AnimEffects.glitch(1f));
        drawTextEffect(graphics, "fade", x, y + 144, AnimEffects.fade(0.35f, 1.2f, 0.3f));
        drawTextEffect(graphics, "rainbow", x, y + 162, AnimEffects.rainbow(0.18f, 0.08f));
        drawTextEffect(graphics, "gradient", x, y + 180, AnimEffects.gradient(0x80FFE1, 0xA98DFF, 0.18f));
        drawTextEffect(graphics, "scalePulse", x, y + 198, AnimEffects.scalePulse(0.85f, 1.15f, 1f, 0.35f));
    }

    private void renderTextureRows(GuiGraphics graphics, int x, int y) {
        drawTextureEffect(graphics, "wave", x, y, AnimEffects.wave(2f, 1f, 0f));
        drawTextureEffect(graphics, "bounce", x, y + 24, AnimEffects.bounce(3f, 1.3f, 0f));
        drawTextureEffect(graphics, "shake", x, y + 48, AnimEffects.shake(1f, 1f));
        drawTextureEffect(graphics, "wiggle", x, y + 72, AnimEffects.wiggle(1.4f, 1f, 0f));
        drawTextureEffect(graphics, "swing", x, y + 96, AnimEffects.swing(18f, 1f, 0f));
        drawTextureEffect(graphics, "pendulum", x, y + 120, AnimEffects.pendulum(10f, 1.2f, 1f));
        drawTextureEffect(graphics, "turbulence", x, y + 144, AnimEffects.turbulence(1f, 1f));
        drawTextureEffect(graphics, "glitch", x, y + 168, AnimEffects.glitch(1f));
        drawTextureEffect(graphics, "fade", x, y + 192, AnimEffects.fade(0.35f, 1.2f, 0f));
        drawTextureEffect(graphics, "scalePulse", x, y + 216, AnimEffects.scalePulse(0.85f, 1.15f, 1f, 0f));
    }

    private void drawTextEffect(GuiGraphics graphics, String label, int x, int y, AnimEffect effect) {
        graphics.drawString(font, label, x, y, 0x999999, true);
        AnimLib.drawText(graphics, font, "Sample text", x + 72, y, TEXT_COLOR, true, effect);
    }

    private void drawTextureEffect(GuiGraphics graphics, String label, int x, int y, AnimEffect effect) {
        graphics.drawString(font, label, x, y + 5, 0x999999, true);
        AnimLib.blit(graphics, TEST_TEXTURE, x + 72, y, 0, 0, 16, 16, 16, 16, effect);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
