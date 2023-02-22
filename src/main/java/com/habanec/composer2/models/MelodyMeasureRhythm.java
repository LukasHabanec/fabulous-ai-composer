package com.habanec.composer2.models;

import com.habanec.composer2.assets.Tune;

public class MelodyMeasureRhythm {
    public final char letter;
    public final int[] pattern;

    public MelodyMeasureRhythm(char letter, int[] pattern) {
        this.letter = letter;
        this.pattern = pattern;
    }

}
