package org.holy.animlib.api.text;

public class TextEffectContext {
    private final String text;
    private final int codePoint;
    private final int glyphIndex;
    private final int glyphCount;
    private final float time;

    public TextEffectContext(String text, int glyphIndex, int glyphCount, float time) {
        this(text, 0, glyphIndex, glyphCount, time);
    }

    public TextEffectContext(String text, int codePoint, int glyphIndex, int glyphCount, float time) {
        this.text = text;
        this.codePoint = codePoint;
        this.glyphIndex = glyphIndex;
        this.glyphCount = glyphCount;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public int getGlyphIndex() {
        return glyphIndex;
    }

    public int getGlyphCount() {
        return glyphCount;
    }

    public float getTime() {
        return time;
    }
}
