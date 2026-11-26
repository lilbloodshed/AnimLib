package org.holy.animlib.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.holy.animlib.api.builder.AnimationBuilder;
import org.holy.animlib.api.effects.AnimEffect;
import org.holy.animlib.api.text.TextEffectContext;
import org.holy.animlib.api.text.TextRenderState;
import org.holy.animlib.core.Time;
import org.holy.animlib.core.registry.AnimatorRegistry;
import org.holy.animlib.runtime.animator.ScreenAnimator;

/**
 * Small facade for screen-bound animations
 *
 * Keep calls to this class in the screen that owns the animation. That makes
 * cleanup predictable when Minecraft closes or rebuilds the screen
 */
public final class AnimLib {
    private static final long TEXT_EFFECT_START_NANOS = System.nanoTime();

    private AnimLib() {
    }

    public static AnimationBuilder animate(Screen screen) {
        return new AnimationBuilder(AnimatorRegistry.get(screen));
    }

    public static ScreenAnimator animator(Screen screen) {
        return AnimatorRegistry.get(screen);
    }

    /**
     * Tick animations for this screen. Call once from render before drawing
     * animated text or textures
     */
    public static void update(Screen screen) {
        Time.update();
        AnimatorRegistry.get(screen).tick(Time.getDelta());
    }

    /**
     * Drop screen-bound animation state. Usually called from Screen#onClose()
     */
    public static void clear(Screen screen) {
        AnimatorRegistry.remove(screen);
    }

    public static void drawText(GuiGraphics graphics, Font font, Component text, int x, int y, int color, boolean shadow, AnimEffect... effects) {
        drawText(graphics, font, text.getString(), x, y, color, shadow, effects);
    }

    public static void drawText(GuiGraphics graphics, Font font, String text, int x, int y, int color, boolean shadow, AnimEffect... effects) {
        if (effects == null || effects.length == 0) {
            graphics.drawString(font, text, x, y, color, shadow);
            return;
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0f);

        int[] glyphs = text.codePoints().toArray();
        float cursorX = 0f;
        float time = effectTime();

        for (int i = 0; i < glyphs.length; i++) {
            String glyph = new String(Character.toChars(glyphs[i]));
            TextRenderState state = new TextRenderState(cursorX, 0f, color);
            TextEffectContext context = new TextEffectContext(text, glyphs[i], i, glyphs.length, time);

            for (AnimEffect effect : effects) {
                if (effect != null) {
                    effect.apply(state, context);
                }
            }

            drawGlyph(graphics, font, glyph, state, shadow);
            cursorX += font.width(glyph);
        }

        pose.popPose();
    }

    public static void blit(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, AnimEffect... effects) {
        blit(graphics, texture, x, y, 0, 0, width, height, width, height, effects);
    }

    public static void blit(GuiGraphics graphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, AnimEffect... effects) {
        if (effects == null || effects.length == 0) {
            graphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
            return;
        }

        TextRenderState state = new TextRenderState(0f, 0f, 0xFFFFFF);
        TextEffectContext context = new TextEffectContext("texture", 0, 1, effectTime());

        for (AnimEffect effect : effects) {
            if (effect != null) {
                effect.apply(state, context);
            }
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + state.getX() + width / 2f, y + state.getY() + height / 2f, 0f);
        pose.mulPose(Axis.ZP.rotationDegrees(state.getRotationDegrees()));
        pose.scale(state.getScale(), state.getScale(), 1f);
        pose.translate(-width / 2f, -height / 2f, 0f);

        graphics.setColor(1f, 1f, 1f, state.getAlpha());
        graphics.blit(texture, 0, 0, u, v, width, height, textureWidth, textureHeight);
        graphics.setColor(1f, 1f, 1f, 1f);

        pose.popPose();
    }

    private static void drawGlyph(GuiGraphics graphics, Font font, String glyph, TextRenderState state, boolean shadow) {
        PoseStack pose = graphics.pose();
        float glyphWidth = font.width(glyph);

        pose.pushPose();
        pose.translate(state.getX() + glyphWidth / 2f, state.getY() + font.lineHeight / 2f, 0f);
        pose.mulPose(Axis.ZP.rotationDegrees(state.getRotationDegrees()));
        pose.scale(state.getScale(), state.getScale(), 1f);
        pose.translate(-glyphWidth / 2f, -font.lineHeight / 2f, 0f);
        graphics.drawString(font, glyph, 0, 0, colorWithAlpha(state), shadow);
        pose.popPose();
    }

    private static int colorWithAlpha(TextRenderState state) {
        int alpha = Math.round(Math.max(0f, Math.min(1f, state.getAlpha())) * 255f);
        return (alpha << 24) | (state.getColor() & 0x00FFFFFF);
    }

    private static float effectTime() {
        return (System.nanoTime() - TEXT_EFFECT_START_NANOS) / 1_000_000_000f;
    }
}
