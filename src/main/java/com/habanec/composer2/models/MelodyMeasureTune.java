package com.habanec.composer2.models;

public class MelodyMeasureTune {
    public final char letter;
    public final int[] pattern;
    public final Key currentKey;
    private int firstToneIndex;
    private int nextMeasureShifter;
    private int userSpecialShifter;

    public MelodyMeasureTune(char letter, int[] patternRaw, int rhythmValueCount, Key key,
                             int firstToneIndex, int userSpecialShifter) {
        this.letter = letter;
        this.pattern = setTunePattern(patternRaw, rhythmValueCount);
        this.currentKey = key;
        this.firstToneIndex = firstToneIndex;
        this.userSpecialShifter = userSpecialShifter;
    }

    private int[] setTunePattern(int[] raw, int count) {
        int[] tunePattern = new int[count];
        for (int i = 0; i < count + 1; i++) {
            if (i == tunePattern.length) {
                nextMeasureShifter = raw[i];
            } else {
                tunePattern[i] = raw[i];
            }
        }
        return tunePattern;
    }

    public int getFirstToneIndex() { return firstToneIndex; }
    public int getNextMeasureShifter() { return nextMeasureShifter; }
    public int getUserSpecialShifter() { return userSpecialShifter; }
    public void setUserSpecialShifter(int shifter) { this.userSpecialShifter += shifter; }

}
