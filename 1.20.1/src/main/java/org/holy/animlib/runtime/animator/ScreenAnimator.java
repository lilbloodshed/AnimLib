package org.holy.animlib.runtime.animator;

import org.holy.animlib.core.Time;
import org.holy.animlib.runtime.animation.AnimMode;
import org.holy.animlib.runtime.animation.AnimatedValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ScreenAnimator {
    private final List<AnimatedValue> values = new ArrayList<>();

    public AnimatedValue create(float start, float end, float duration, AnimMode mode, Function<Float, Float> easing) {
        AnimatedValue v = new AnimatedValue(start, end, duration, mode, easing);
        values.add(v);

        return v;
    }

    public void add(AnimatedValue value) {
        values.add(value);
    }

    public List<AnimatedValue> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void clear() {
        values.clear();
    }

    public void tick() {
        tick(Time.getDelta());
    }

    public void tick(float delta) {
        Iterator<AnimatedValue> it = values.iterator();

        while (it.hasNext()) {
            AnimatedValue v = it.next();
            v.update(delta);

            if (v.isFinished()) {
                it.remove();
            }
        }
    }
}
