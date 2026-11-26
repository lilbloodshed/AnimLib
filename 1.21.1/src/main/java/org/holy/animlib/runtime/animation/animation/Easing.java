package org.holy.animlib.runtime.animation.animation;

public final class Easing {
    private Easing() {
    }

    public static float linear(float t) {
        return clamp(t);
    }

    public static float easeInOut(float t) {
        t = clamp(t);
        return t * t * (3 - 2 * t);
    }

    public static float easeOut(float t) {
        t = clamp(t);
        return 1 - (float)Math.pow(1 - t, 3);
    }

    public static float easeIn(float t) {
        t = clamp(t);
        return t * t * t;
    }

    public static float sineIn(float t) {
        t = clamp(t);
        return 1f - (float) Math.cos((t * Math.PI) / 2f);
    }

    public static float sineOut(float t) {
        t = clamp(t);
        return (float) Math.sin((t * Math.PI) / 2f);
    }

    public static float sineInOut(float t) {
        t = clamp(t);
        return -((float) Math.cos(Math.PI * t) - 1f) / 2f;
    }

    public static float quadIn(float t) {
        t = clamp(t);
        return t * t;
    }

    public static float quadOut(float t) {
        t = clamp(t);
        return 1f - (1f - t) * (1f - t);
    }

    public static float quadInOut(float t) {
        t = clamp(t);
        return t < 0.5f ? 2f * t * t : 1f - (float) Math.pow(-2f * t + 2f, 2f) / 2f;
    }

    public static float cubicIn(float t) {
        return easeIn(t);
    }

    public static float cubicOut(float t) {
        return easeOut(t);
    }

    public static float cubicInOut(float t) {
        t = clamp(t);
        return t < 0.5f ? 4f * t * t * t : 1f - (float) Math.pow(-2f * t + 2f, 3f) / 2f;
    }

    public static float backOut(float t) {
        t = clamp(t);
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1f + c3 * (float) Math.pow(t - 1f, 3f) + c1 * (float) Math.pow(t - 1f, 2f);
    }

    public static float backIn(float t) {
        t = clamp(t);
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return c3 * t * t * t - c1 * t * t;
    }

    public static float elasticOut(float t) {
        t = clamp(t);
        if (t == 0f || t == 1f) {
            return t;
        }
        float c4 = (2f * (float) Math.PI) / 3f;
        return (float) Math.pow(2f, -10f * t) * (float) Math.sin((t * 10f - 0.75f) * c4) + 1f;
    }

    public static float bounceOut(float t) {
        t = clamp(t);
        float n1 = 7.5625f;
        float d1 = 2.75f;

        if (t < 1f / d1) {
            return n1 * t * t;
        }
        if (t < 2f / d1) {
            t -= 1.5f / d1;
            return n1 * t * t + 0.75f;
        }
        if (t < 2.5f / d1) {
            t -= 2.25f / d1;
            return n1 * t * t + 0.9375f;
        }

        t -= 2.625f / d1;
        return n1 * t * t + 0.984375f;
    }

    public static float bounceIn(float t) {
        return 1f - bounceOut(1f - clamp(t));
    }

    private static float clamp(float t) {
        return Math.max(0f, Math.min(1f, t));
    }
}
