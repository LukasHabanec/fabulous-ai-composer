package com.habanec.composer2.assets;

public enum Grade {

    I_(0, "T_"), //todo tady to asi nechci
    II_(1, "Sp"),
    III_(2, "Dp"),
    IV_(3, "S_"),
    V_(4, "D_"),
    VI_(5, "Tp"),
    VII_(6, "D7");

    public final int index;
    //public final String harmonyLabel;

    Grade(int index, String harmonyLabel) {
        this.index = index;
        //this.harmonyLabel = harmonyLabel;
    }
    public static Grade getGrade(int index) {
        for (Grade grade : Grade.values()) {
            if (grade.index == index) {
                return grade;
            }
        }
        return null;
    }
//    public String getHarmonyLabel() {
//        return harmonyLabel;
//    }


}
