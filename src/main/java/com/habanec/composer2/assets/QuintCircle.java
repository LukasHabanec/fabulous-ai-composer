package com.habanec.composer2.assets;

public enum QuintCircle {
    F_FLAT(0,4, "Fb"),
    C_FLAT(1, 11, "Cb"),
    G_FLAT(2, 6, "Gb"),
    D_FLAT(3, 1, "Db"),
    A_FLAT(4, 8, "Ab"),
    E_FLAT(5, 3, "Eb"),
    B_FLAT(6, 10, "Bb"),
    F(7, 5, "F"),
    C(8, 0, "C"),
    G(9, 7, "G"),
    D(10, 2, "D"),
    A(11, 9, "A"),
    E(12, 4, "E"),
    B(13, 11, "B/H"),
    F_SHARP(14, 6, "F#"),
    C_SHARP(15, 1, "C#"),
    G_SHARP(16, 8, "G#"),
    D_SHARP(17, 3, "D#"),
    A_SHARP(18, 10, "A#");

    private Integer index;
    private Integer iniMidiTone;
    private String label;

    QuintCircle(int index, int iniTone, String label) {
        this.index = index;
        this.iniMidiTone = iniTone;
        this.label = label;
    }

    public Integer getIniMidiTone() { return iniMidiTone; }
    public String getLabel() { return label; }
    public Integer getIndex() { return index; }

}

