package com.habanec.composer2.assets;

public enum Mode {
    MAJOR(new int[] {2,2,1,2,2,2,1}, "Major"),
    MINOR_AEOLIAN(new int[] {2,1,2,2,1,2,2}, "Minor (aeolian)"),
    MINOR_DORIAN(new int[] {2,1,2,2,2,1,2}, "Minor (dorian)"),
    MINOR_HARMONIC(new int[] {2,1,2,2,1,3,1}, "Minor (harmonic)");
    //CHROMATIC(new int[] {1,1,1,1, 1,1,1,1, 1,1,1,1}, "Chromatic"),
    //PENTATONIC(new int[] {2,2,3,2,3}, "Pentatonic"); //todo nefungujou v GradeInContext

    public final int[] intervals;
    private final String label;

    Mode(int[] intervals, String label) {
        this.intervals = intervals;
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

}
