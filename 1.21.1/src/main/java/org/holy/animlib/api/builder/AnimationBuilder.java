package org.holy.animlib.api.builder;

import org.holy.animlib.runtime.animation.AnimMode;
import org.holy.animlib.runtime.animation.AnimatedValue;
import org.holy.animlib.runtime.animation.animation.Easing;
import org.holy.animlib.runtime.animator.ScreenAnimator;

import java.util.function.Consumer;
import java.util.function.Function;

public class AnimationBuilder {
    private final ScreenAnimator animator;
    private float from;
    private float to;
    private float duration = 1f;
    private int repeatCount = 1;
    private AnimMode mode = AnimMode.ONCE;
    private Function<Float, Float> easing = Easing::linear;
    private Consumer<Float> setter;

    public AnimationBuilder(ScreenAnimator animator) {
        this.animator = animator;
    }

    public AnimationBuilder from(float v) {
        this.from = v;
        return this;
    }

    public AnimationBuilder to(float v) {
        this.to = v;
        return this;
    }

    public AnimationBuilder duration(float d) {
        this.duration = Math.max(0f, d);
        return this;
    }

    public AnimationBuilder loop() {
        this.mode = AnimMode.LOOP;
        return this;
    }

    public AnimationBuilder pingPong() {
        this.mode = AnimMode.PING_PONG;
        return this;
    }

    public AnimationBuilder easeInOut() {
        this.easing = Easing::easeInOut;
        return this;
    }

    public AnimationBuilder easeIn() {
        this.easing = Easing::easeIn;
        return this;
    }

    public AnimationBuilder easeOut() {
        this.easing = Easing::easeOut;
        return this;
    }

    public AnimationBuilder sineIn() {
        this.easing = Easing::sineIn;
        return this;
    }

    public AnimationBuilder sineOut() {
        this.easing = Easing::sineOut;
        return this;
    }

    public AnimationBuilder sineInOut() {
        this.easing = Easing::sineInOut;
        return this;
    }

    public AnimationBuilder quadIn() {
        this.easing = Easing::quadIn;
        return this;
    }

    public AnimationBuilder quadOut() {
        this.easing = Easing::quadOut;
        return this;
    }

    public AnimationBuilder quadInOut() {
        this.easing = Easing::quadInOut;
        return this;
    }

    public AnimationBuilder cubicIn() {
        this.easing = Easing::cubicIn;
        return this;
    }

    public AnimationBuilder cubicOut() {
        this.easing = Easing::cubicOut;
        return this;
    }

    public AnimationBuilder cubicInOut() {
        this.easing = Easing::cubicInOut;
        return this;
    }

    public AnimationBuilder backIn() {
        this.easing = Easing::backIn;
        return this;
    }

    public AnimationBuilder backOut() {
        this.easing = Easing::backOut;
        return this;
    }

    public AnimationBuilder elasticOut() {
        this.easing = Easing::elasticOut;
        return this;
    }

    public AnimationBuilder bounceIn() {
        this.easing = Easing::bounceIn;
        return this;
    }

    public AnimationBuilder bounceOut() {
        this.easing = Easing::bounceOut;
        return this;
    }

    public AnimationBuilder easing(Function<Float, Float> easing) {
        this.easing = easing == null ? Easing::linear : easing;
        return this;
    }

    public AnimationBuilder bind(Consumer<Float> setter) {
        this.setter = setter;
        return this;
    }

    public AnimationBuilder repeat(int count) {
        this.repeatCount = count;
        return this;
    }

    public void start() {
        if (setter == null) {
            throw new IllegalStateException("AnimationBuilder.bind(...) must be called before start().");
        }

        addValue(from, to, duration, setter);
    }

    public AnimationBuilder value(float from, float to, float duration, Consumer<Float> setter) {
        addValue(from, to, duration, setter);
        return this;
    }

    private void addValue(float from, float to, float duration, Consumer<Float> setter) {
        if (setter == null) {
            throw new IllegalArgumentException("Animation setter cannot be null.");
        }

        AnimatedValue val = new AnimatedValue(from, to, duration, mode, easing) {
            @Override
            public void update(float delta) {
                super.update(delta);
                setter.accept(get());
            }
        };

        val.getAnimation().setRepeatCount(repeatCount);
        animator.add(val);
    }
}
