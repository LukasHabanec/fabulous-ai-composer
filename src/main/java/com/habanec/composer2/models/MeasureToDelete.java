package com.habanec.composer2.models;

import com.habanec.composer2.assets.HarmonyFragmentation;
import com.habanec.composer2.assets.Rhythm;
import com.habanec.composer2.assets.Tune;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeasureToDelete {

    private final int index;
    private final int numOfBeats;
    private final char rhythmLetter;
    private final char tuneLetter;
    private Integer rhythmPatternIndex;
    private Integer tunePatternIndex;
    private Integer harmonyFragmentationIndex;
    private String rhythmPatternString;
    private String tunePatternString;
    private String melodyPatternString;
    private String harmonyFragmentationString;
    private String harmonyFigurationString;
    private int[] thisTunePattern;
    private int[] thisHarmonyFragmentationPattern;
    private int[] melodyMatrix;
    //private int[] concreteMidiTones;
    private Key currentKey;
    private final int firstToneIndex;
    private int nextMeasureShifter;
    private Integer userSpecialShifter;

    private List<HarmonyField> harmonyFields;

    private boolean locked;

    public MeasureToDelete(int index,
                           char formLetter,
                           char tuneLetter,
                           Integer rhythmPattern,
                           Integer tunePattern,
                           Integer harmonyFragmentationIndex,
                           Key currentKey,
                           int firstToneIndex,
                           Integer userSpecialShifter) {
        this.index = index;
        this.rhythmLetter = formLetter;
        this.tuneLetter = tuneLetter;
        this.rhythmPatternIndex = rhythmPattern;
        this.tunePatternIndex = tunePattern;
        this.harmonyFragmentationIndex = harmonyFragmentationIndex;
        this.currentKey = currentKey;
        this.firstToneIndex = firstToneIndex;
        this.userSpecialShifter = userSpecialShifter;
        thisHarmonyFragmentationPattern = HarmonyFragmentation.patterns[harmonyFragmentationIndex];
        locked = false;

        numOfBeats = 4;
        setTunePattern();
        setPatternStrings();
        setMelodyMatrix();
        setMelodyPatternString();


        //createConcreteMidiTones();
    }

    private void setMelodyPatternString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < thisTunePattern.length; i++) {
            sb.append(thisTunePattern[i] + firstToneIndex + userSpecialShifter);
            for (int r = 0; r < Rhythm.patterns[rhythmPatternIndex][i] - 1; r++) {
                sb.append("__");
            }
        }
        sb.append("]");
        melodyPatternString = sb.toString();
    }

//    public void reconstruct() {
//        setMelodyMatrix();
//        setHarmonyFields();
//    }

    public void setMelodyMatrix() {
        melodyMatrix = new int[4 * numOfBeats];
        int index = 0;
        for (int i = 0; i < Rhythm.patterns[rhythmPatternIndex].length; i++) {
            for (int j = 0; j < Rhythm.patterns[rhythmPatternIndex][i]; j++) {
                melodyMatrix[index] = currentKey.getScaleMidiValues()[
                        firstToneIndex + Tune.patterns[tunePatternIndex][i] + userSpecialShifter
                        ];
                index++;
            }
        }
        //System.out.println(Arrays.toString(melodyMatrix));
    }


    public void resetHarmonyFields() {
        for (int i = 0; i < thisHarmonyFragmentationPattern.length; i++) {
            if (!this.isLocked()) {
                harmonyFields.get(i).reset(melodyMatrix);
            }
        }
    }

    private void setPatternStrings() { //todo: zvaz, jestli to uz neni passe, na frontendu se to neukazuje
        int[] rhyPattern = Rhythm.patterns[rhythmPatternIndex];
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rhyPattern.length; i++) {
            sb.append(rhyPattern[i]);
            for (int r = 0; r < rhyPattern[i] - 1; r++) {
                sb.append("_");
            }
        }
        sb.append("]");
        rhythmPatternString = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        for (int i = 0; i < thisTunePattern.length; i++) {
            sb2.append(thisTunePattern[i] + " ");
        }
        sb2.append("*" + nextMeasureShifter + "]");
        tunePatternString = sb2.toString();

        int[] harmonyFragmentationPattern = HarmonyFragmentation.patterns[harmonyFragmentationIndex];
        StringBuilder sb3 = new StringBuilder(); // todo:rozhodne potrebuju tenhle - nastavuje selecty
        sb3.append("[");
        for (int i = 0; i < harmonyFragmentationPattern.length; i++) {
            sb3.append(harmonyFragmentationPattern[i]);
            for (int r = 0; r < harmonyFragmentationPattern[i] - 1; r++) {
                sb3.append("_");
            }
        }
        sb3.append("]");
        harmonyFragmentationString = sb3.toString();
    }

    private void setTunePattern() {
        thisTunePattern = new int[Rhythm.patterns[rhythmPatternIndex].length];
        for (int i = 0; i < thisTunePattern.length + 1; i++) {
            if (i == thisTunePattern.length) {
                nextMeasureShifter = Tune.patterns[tunePatternIndex][i];
            } else {
                thisTunePattern[i] = Tune.patterns[tunePatternIndex][i];
            }
        }
    }

    public void increaseUserSpecialShifter(int shifter) {
        this.userSpecialShifter += shifter;
        setMelodyMatrix();
        setMelodyPatternString();
        resetHarmonyFields();
    }

    public int getNextMeasureShifter() { return nextMeasureShifter; }
    public int[] getTunePattern() { return thisTunePattern; }
    public Integer getRhythmPatternIndex() { return rhythmPatternIndex; }
    public Integer getTunePatternIndex() { return tunePatternIndex; }
    public int[] getThisTunePattern() { return thisTunePattern; }
    public Key getCurrentKey() { return currentKey; }
    public int getFirstToneIndex() { return firstToneIndex; }
    public char getRhythmLetter() { return rhythmLetter; }
    public char getTuneLetter() { return tuneLetter; }
    public Integer getUserSpecialShifter() { return userSpecialShifter; }
    public int getIndex() { return index; }
    public String getRhythmPatternString() { return rhythmPatternString; }
    public String getTunePatternString() { return tunePatternString; }
    public String getMelodyPatternString() { return melodyPatternString; }
    public String getHarmonyFragmentationString() { return harmonyFragmentationString; }
    public List<HarmonyField> getHarmonyFields() { return harmonyFields; }
    public Integer getHarmonyFragmentationIndex() { return harmonyFragmentationIndex; }
    public boolean isLocked() { return locked; }
    public void switchLock() { locked = !locked; }
    public String getHarmonyFigurationString() { return harmonyFigurationString; }


}
