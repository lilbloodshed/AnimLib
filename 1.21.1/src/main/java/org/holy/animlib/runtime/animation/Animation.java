package org.holy.animlib.runtime.animation;

public class Animation {
    private final float duration;
    private float time;
    private boolean forward = true;
    private final AnimMode mode;
    private boolean finished = false;
    private int repeatCount = 1;
    private int currentRepeat = 0;

    public Animation(float duration, AnimMode mode) {
        this.duration = Math.max(0f, duration);
        this.mode = mode;
    }

    public void update(float delta) {
        if (finished) return;

        if (forward) {
            time += delta;
        } else {
            time -= delta;
        }
        if (time >= duration || time <= 0) {
            switch (mode) {
                case ONCE -> {
                    currentRepeat++;

                    if (currentRepeat >= repeatCount) {
                        time = duration;
                        finished = true;
                    } else {
                        time = 0;
                    }
                }
                case LOOP -> {
                    time = 0;
                }
                case PING_PONG -> {
                    if (!forward) {
                        currentRepeat++;

                        if (currentRepeat >= repeatCount) {
                            finished = true;
                            time = 0;
                            return;
                        }
                    }

                    forward = !forward;
                    time = Math.max(0, Math.min(time, duration));
                }
            }
        }
    }

    public float getProgress() {
        if (duration == 0) {
            return 1f;
        }
        return Math.max(0f, Math.min(1f, time / duration));
    }

    public boolean isFinished() {
        return finished;
    }

    public void setRepeatCount(int count) {
        this.repeatCount = Math.max(1, count);
    }

    public float getDuration() {
        return duration;
    }

    public void reset() {
        time = 0f;
        finished = false;
        forward = true;
        currentRepeat = 0;
    }
}
