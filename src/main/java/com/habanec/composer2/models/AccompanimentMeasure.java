package com.habanec.composer2.models;

public class AccompanimentMeasure {

    public final int index;
    public final int[] fragmentationPattern;
    public final String fragmentationString; //nakonec je totozny i v melodyMeasure - kvuli th:
    private int[] melodyMatrix;
    private Key currentKey;
    private boolean locked;

    public AccompanimentMeasure(int index, int[] fragmentationPattern, int[] melodyMatrix, Key key) {
        this.index = index;
        this.fragmentationPattern = fragmentationPattern;
        this.fragmentationString = setFragmentationString();
        this.melodyMatrix = melodyMatrix;
        this.currentKey = key;
        locked = false;
    }
    private String setFragmentationString() {
        StringBuilder sb3 = new StringBuilder("[");
        for (int i = 0; i < fragmentationPattern.length; i++) {
            sb3.append(fragmentationPattern[i]);
            for (int r = 0; r < fragmentationPattern[i] - 1; r++) {
                sb3.append("_");
            }
        }
        return sb3.append("]").toString();
    }
    public boolean isLocked() { return locked; }
    public void switchLock() { locked = !locked; }
    public int[] getMelodyMatrix() { return melodyMatrix; }
    public Key getCurrentKey() { return currentKey; }
    public int[] getFragmentationPattern() { return fragmentationPattern; }

}
