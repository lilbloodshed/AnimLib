package org.holy.animlib.api.effects;

import org.holy.animlib.api.text.TextEffectContext;
import org.holy.animlib.api.text.TextRenderState;

@FunctionalInterface
public interface AnimEffect {
    void apply(TextRenderState state, TextEffectContext context);
}
