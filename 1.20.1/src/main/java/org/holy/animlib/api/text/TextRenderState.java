package org.holy.animlib.api.text;

public class TextRenderState {
    private float x;
    private float y;
    private float alpha = 1f;
    private float scale = 1f;
    private float rotationDegrees;
    private int color;

    public TextRenderState(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void moveX(float delta) {
        this.x += delta;
    }

    public float getY() {
        return y;
    }

    public void moveY(float delta) {
        this.y += delta;
    }

    public float getAlpha() {
        return alpha;
    }

    public void multiplyAlpha(float alpha) {
        this.alpha *= Math.max(0f, Math.min(1f, alpha));
    }

    public float getScale() {
        return scale;
    }

    public void multiplyScale(float scale) {
        this.scale *= scale;
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public void rotate(float degrees) {
        this.rotationDegrees += degrees;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
