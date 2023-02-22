package com.habanec.composer2.assets;

import com.habanec.composer2.models.Chord;

public enum HarmonyGrade {
    I___T__(0, "T__", new Grade[] {Grade.I_, Grade.III_, Grade.V_}),
    II__Sp_(1, "Sp_", new Grade[] {Grade.II_, Grade.IV_, Grade.IV_}),
    III_Dp_(2, "Dp_", new Grade[] {Grade.III_, Grade.V_, Grade.VII_}),
    IV__S__(3, "S__", new Grade[] {Grade.IV_, Grade.VI_, Grade.I_}),
//    IV__S7_(3, "S7_", new Grade[] {Grade.IV_, Grade.VI_, Grade.I_, Grade.III_}),
    IV__S6_(3, "S6_", new Grade[] {Grade.IV_, Grade.VI_, Grade.I_, Grade.II_}),
    V___D__(4, "D__", new Grade[] {Grade.V_, Grade.VII_, Grade.II_}),
    V___D7_(4, "D7_", new Grade[] {Grade.V_, Grade.VII_, Grade.II_, Grade.IV_}),
//    VII_Dx7(6, "Dx7", new Grade[] {Grade.VII_, Grade.II_, Grade.IV_}),
    VI__Tp_(5, "Tp_", new Grade[] {Grade.VI_, Grade.I_, Grade.III_});

    private Integer index;
    private String label;
    private Grade[] chord;

    HarmonyGrade(Integer index, String label, Grade[] chord) {
        this.index = index;
        this.label = label;
        this.chord = chord;
    }

    public String getLabel() { return label; }
    public Grade[] getChord() { return chord; }
    public Integer getIndex() { return index; }

//    public boolean equals(HarmonyGrade otherGrade) {
//        if (otherGrade == null) {
//            return true;
//        }
//        if (this.index == otherGrade.index) {
//            return true;
//        } else return false;
//    }


}
