package org.holy.animlib.runtime.animation;

import java.util.function.Function;

public class AnimatedValue {
    protected float start, end, value;
    protected Animation animation;
    protected Function<Float, Float> easing;

    public AnimatedValue(float start, float end, float duration, AnimMode mode, Function<Float, Float> easing) {
        this.start = start;
        this.end = end;
        this.value = start;
        this.animation = new Animation(duration, mode);
        this.easing = easing;
    }

    public void update(float delta) {
        animation.update(delta);
        float progress = easing.apply(animation.getProgress());
        value = lerp(start, end, progress);
    }

    public boolean isFinished() {
        return animation.isFinished();
    }

    protected float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public float get() {
        return value;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void reset() {
        animation.reset();
        value = start;
    }
}
