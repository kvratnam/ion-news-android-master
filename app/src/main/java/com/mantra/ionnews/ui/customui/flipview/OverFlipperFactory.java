package com.mantra.ionnews.ui.customui.flipview;

/**
 * Created by TaNMay on 11/04/17.
 */

public class OverFlipperFactory {

    static OverFlipper create(FlipView v, OverFlipMode mode) {
        switch (mode) {
            case GLOW:
                return new GlowOverFlipper(v);
            case RUBBER_BAND:
                return new RubberBandOverFlipper();
        }
        return null;
    }

}