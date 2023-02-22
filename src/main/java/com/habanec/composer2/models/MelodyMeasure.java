package com.habanec.composer2.models;

import com.habanec.composer2.assets.HarmonyFragmentation;
import com.habanec.composer2.assets.Rhythm;
import com.habanec.composer2.assets.Tune;

public class MelodyMeasure {

    public final int index;
    public final int numOfBeats;
    private MelodyMeasureRhythm rhythm;
    private MelodyMeasureTune tune;
    private int[] melodyMatrix;
    private String patternString;
    private String harmonyFragmentationString;

    public MelodyMeasure(int index,
                   char rhythmLetter,
                   char tuneLetter,
                   int[] rhythmPattern,
                   int[] tunePatternRaw,
                   Key currentKey,
                   int firstToneIndex,
                   int userSpecialShifter,
                   int harmonyFragmentationIndex) {
        this.index = index;
        numOfBeats = 4;

        rhythm = new MelodyMeasureRhythm(rhythmLetter, rhythmPattern);

        tune = new MelodyMeasureTune(tuneLetter, tunePatternRaw, rhythmPattern.length,
                currentKey, firstToneIndex, userSpecialShifter);

        harmonyFragmentationString = setHarmonyFragmentationString(harmonyFragmentationIndex);
        setMelodyMatrix();
        setMelodyPatternString();
    }

    private String setHarmonyFragmentationString(int harmonyFragmentationIndex) {
        int[] harmonyFragmentationPattern = HarmonyFragmentation.patterns[harmonyFragmentationIndex];
        StringBuilder sb3 = new StringBuilder("[");
        for (int i = 0; i < harmonyFragmentationPattern.length; i++) {
            sb3.append(harmonyFragmentationPattern[i]);
            for (int r = 0; r < harmonyFragmentationPattern[i] - 1; r++) {
                sb3.append("_");
            }
        }
        return sb3.append("]").toString();
    }

    private void setMelodyPatternString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tune.pattern.length; i++) {
            sb.append(tune.pattern[i] + tune.getFirstToneIndex() + tune.getUserSpecialShifter());
            for (int r = 0; r < rhythm.pattern[i] - 1; r++) {
                sb.append("__");
            }
        }
        sb.append("]");
        patternString = sb.toString();
    }

    public void setMelodyMatrix() {
        melodyMatrix = new int[4 * numOfBeats];
        int index = 0;
        for (int i = 0; i < rhythm.pattern.length; i++) {
            for (int j = 0; j < rhythm.pattern[i]; j++) {
                melodyMatrix[index] = tune.currentKey.getScaleMidiValues()[
                        tune.getFirstToneIndex() + tune.pattern[i] + tune.getUserSpecialShifter()
                        ];
                index++;
            }
        }
        //System.out.println(Arrays.toString(melodyMatrix));
    }

    public void increaseUserSpecialShifter(int shifter) {
        tune.setUserSpecialShifter(shifter);
        setMelodyMatrix();
        setMelodyPatternString();

//        resetHarmonyFields(); //todo
    }



    public MelodyMeasureRhythm getRhythm() { return rhythm; }
    public MelodyMeasureTune getTune() { return tune; }
    public int[] getMelodyMatrix() { return melodyMatrix; }
    public String getPatternString() { return patternString; }
    public String getHarmonyFragmentationString() { return harmonyFragmentationString; }
}
