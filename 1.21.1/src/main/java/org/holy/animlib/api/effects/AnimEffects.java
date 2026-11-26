package org.holy.animlib.api.effects;

import org.holy.animlib.api.effects.AnimEffect;
import org.holy.animlib.api.text.TextEffectContext;

public final class AnimEffects {
    private static final float TWO_PI = (float) (Math.PI * 2d);

    private AnimEffects() {
    }

    public static AnimEffect wave(float amplitude, float speed, float spacing) {
        return (state, context) -> state.moveY(sin(context, speed, spacing) * amplitude);
    }

    public static AnimEffect bounce(float amplitude, float speed, float spacing) {
        return (state, context) -> state.moveY(-Math.abs(sin(context, speed, spacing)) * amplitude);
    }

    public static AnimEffect shake(float amplitude, float speed) {
        return (state, context) -> {
            int step = Math.round(context.getTime() * Math.max(0.01f, speed) * 18f);
            float x = signedNoise(context, step, 17) * amplitude * 0.6f;
            float y = signedNoise(context, step, 41) * amplitude * 0.6f;
            state.moveX(x);
            state.moveY(y);
        };
    }

    public static AnimEffect wiggle(float amplitude, float speed, float spacing) {
        return (state, context) -> {
            float direction = stableAngle(context);
            float distance = sin(context, speed, spacing * 2f) * amplitude * 1.5f;
            state.moveX((float) Math.cos(direction) * distance);
            state.moveY((float) Math.sin(direction) * distance);
        };
    }

    public static AnimEffect swing(float angleDegrees, float speed, float spacing) {
        return (state, context) -> state.rotate(sin(context, speed * 0.3f, spacing) * angleDegrees * 0.5f);
    }

    public static AnimEffect pendulum(float angleDegrees, float radius, float speed) {
        return (state, context) -> {
            float phase = context.getTime() * speed * 2f - context.getGlyphIndex() * 0.1f;
            float wave = (float) Math.sin(phase);
            state.rotate(wave * angleDegrees);
            if (radius != 0f) {
                state.moveX((float) Math.cos(phase) * radius);
                state.moveY((float) Math.sin(phase) * radius);
            }
        };
    }

    public static AnimEffect turbulence(float amplitude, float speed) {
        return (state, context) -> {
            float t = context.getTime() * speed * 2f;
            float x = (float) Math.sin(t * 1.7f + context.getGlyphIndex() * 0.31f + context.getCodePoint() * 0.07f);
            float y = (float) Math.sin(t * 2.3f + context.getGlyphIndex() * 0.27f + context.getCodePoint() * 0.11f);
            state.moveX(x * amplitude * 1.5f);
            state.moveY(y * amplitude * 1.5f);
        };
    }

    public static AnimEffect glitch(float frequency, float jitterChance, float blinkChance) {
        return (state, context) -> {
            int frame = Math.round(context.getTime() * Math.max(0.01f, frequency) * 25f);
            float jitterRoll = noise(context, frame, 103);
            if (jitterRoll < jitterChance) {
                state.moveX(signedNoise(context, frame, 211) * 4f);
                state.moveY(signedNoise(context, frame, 307) * 2f);
            }

            float blinkRoll = noise(context, frame, 419);
            if (blinkRoll < blinkChance) {
                state.multiplyAlpha(blinkRoll < blinkChance * 0.3f ? 0f : 0.3f);
            }
        };
    }

    public static AnimEffect glitch(float frequency) {
        return glitch(frequency, 0.015f, 0.003f);
    }

    public static AnimEffect fade(float minAlpha, float speed, float spacing) {
        return pulse(minAlpha, speed * 0.32f, spacing);
    }

    public static AnimEffect pulse(float minAlpha, float speed, float spacing) {
        return (state, context) -> {
            float wave = (sin(context, speed, spacing) + 1f) * 0.5f;
            state.multiplyAlpha(clamp(minAlpha + (1f - minAlpha) * wave, 0f, 1f));
        };
    }

    public static AnimEffect rainbow(float speed, float spacing) {
        return (state, context) -> {
            float hue = positiveModulo(context.getTime() * speed + context.getGlyphIndex() * spacing, 1f);
            state.setColor(hsvToRgb(hue, 0.75f, 1f));
        };
    }

    public static AnimEffect gradient(int fromColor, int toColor, float speed) {
        return (state, context) -> {
            float base = context.getGlyphCount() <= 1 ? 0f : context.getGlyphIndex() / (float) (context.getGlyphCount() - 1);
            float t = speed == 0f ? base : pingPong(base + context.getTime() * speed);
            state.setColor(lerpColor(fromColor, toColor, t));
        };
    }

    public static AnimEffect scalePulse(float minScale, float maxScale, float speed, float spacing) {
        return (state, context) -> {
            float wave = (sin(context, speed, spacing) + 1f) * 0.5f;
            state.multiplyScale(minScale + (maxScale - minScale) * wave);
        };
    }

    private static float sin(TextEffectContext context, float speed, float spacing) {
        return (float) Math.sin(context.getTime() * speed * TWO_PI + context.getGlyphIndex() * spacing);
    }

    private static float stableAngle(TextEffectContext context) {
        return noise(context, 0, 53) * TWO_PI;
    }

    private static float signedNoise(TextEffectContext context, int frame, int salt) {
        return noise(context, frame, salt) * 2f - 1f;
    }

    private static float noise(TextEffectContext context, int frame, int salt) {
        int value = context.getCodePoint() * 734_287 + context.getGlyphIndex() * 91_397 + frame * 1_301 + salt;
        value ^= value >>> 15;
        value *= 0x2c1b3c6d;
        value ^= value >>> 12;
        value *= 0x297a2d39;
        value ^= value >>> 15;
        return (value & 0x00FFFFFF) / (float) 0x01000000;
    }

    private static int hsvToRgb(float hue, float saturation, float value) {
        float h = positiveModulo(hue, 1f) * 6f;
        int sector = (int) Math.floor(h);
        float f = h - sector;
        float p = value * (1f - saturation);
        float q = value * (1f - saturation * f);
        float t = value * (1f - saturation * (1f - f));

        return switch (sector % 6) {
            case 0 -> rgb(value, t, p);
            case 1 -> rgb(q, value, p);
            case 2 -> rgb(p, value, t);
            case 3 -> rgb(p, q, value);
            case 4 -> rgb(t, p, value);
            default -> rgb(value, p, q);
        };
    }

    private static int rgb(float red, float green, float blue) {
        int r = Math.round(clamp(red, 0f, 1f) * 255f);
        int g = Math.round(clamp(green, 0f, 1f) * 255f);
        int b = Math.round(clamp(blue, 0f, 1f) * 255f);
        return (r << 16) | (g << 8) | b;
    }

    private static int lerpColor(int fromColor, int toColor, float t) {
        int fromR = (fromColor >> 16) & 0xFF;
        int fromG = (fromColor >> 8) & 0xFF;
        int fromB = fromColor & 0xFF;
        int toR = (toColor >> 16) & 0xFF;
        int toG = (toColor >> 8) & 0xFF;
        int toB = toColor & 0xFF;

        int r = Math.round(fromR + (toR - fromR) * t);
        int g = Math.round(fromG + (toG - fromG) * t);
        int b = Math.round(fromB + (toB - fromB) * t);
        return (r << 16) | (g << 8) | b;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float positiveModulo(float value, float modulo) {
        float result = value % modulo;
        return result < 0f ? result + modulo : result;
    }

    private static float pingPong(float value) {
        float phase = positiveModulo(value, 2f);
        return phase <= 1f ? phase : 2f - phase;
    }
}
