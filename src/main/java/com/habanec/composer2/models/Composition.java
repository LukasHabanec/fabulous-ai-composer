package com.habanec.composer2.models;

import com.habanec.composer2.assets.*;
import com.habanec.composer2.services.AccompanimentService;
import com.habanec.composer2.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Composition {

    private final String hash;
    private final Form form;
    private Integer tempo;
    private boolean melodyOn, accompanimentOn;

    private int quintCircleMainKeyIndex;
    private int modeIndex;
    private Key tonicKey;
    private Key dominantKey;

    private Melody melody;
    private Accompaniment accompaniment;

    public Composition(String hash,
                       Form form,
                       int quintCircleMainKeyIndex,
                       int modeIndex,
                       Grade startingGrade,
                       Integer[] rhythmPatterns,
                       Integer[] tunePatterns,
                       Integer[] userSpecialShifters,
                       Integer[] shuffledFigurationEnums,
                       int tempo) {
        this.hash = hash;
        this.form = form;
        this.tempo = tempo;
        this.quintCircleMainKeyIndex = quintCircleMainKeyIndex;
        this.modeIndex = modeIndex;
        Integer[] shuffledFragmentationPatterns = new Integer[] {0, 1, 2, 3, 4, 5, 6};

        tonicKey = new Key(QuintCircle.values()[quintCircleMainKeyIndex], Mode.values()[modeIndex]);
        dominantKey = new Key(QuintCircle.values()[quintCircleMainKeyIndex + 1], Mode.values()[modeIndex]);

        melody = new Melody(this, startingGrade,
                rhythmPatterns, tunePatterns, userSpecialShifters);

        accompaniment = new Accompaniment(this,
                new AccompanimentService(),
                shuffledFragmentationPatterns,
                shuffledFigurationEnums,
                form.harmonyFragmentationScheme,
                Utils.cleanUpString(form.figurationScheme),
                Utils.cleanUpString(form.harmonyRhythmScheme)
        );
        melodyOn = true;
        accompanimentOn = true;


    }

    public String getHash() {
        return hash;
    }
    public int getQuintCircleMainKeyIndex() {
        return quintCircleMainKeyIndex;
    }
    public Key getTonicKey() {
        return tonicKey;
    }
    public Key getDominantKey() {
        return dominantKey;
    }
    public int getModeIndex() {
        return modeIndex;
    }
    public int getTempo() {
        return tempo;
    }
    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }
    public Form getForm() {
        return form;
    }
    public Melody getMelody() { return melody; }
    public Accompaniment getAccompaniment() { return accompaniment; }
    public boolean isMelodyOn() { return melodyOn; }
    public boolean isAccompanimentOn() { return accompanimentOn; }
    public void setMelodyAndAccompanimentOn(Boolean melodyOn, Boolean accompanimentOn) {
        this.melodyOn = melodyOn;
        this.accompanimentOn = accompanimentOn;
    }
}
