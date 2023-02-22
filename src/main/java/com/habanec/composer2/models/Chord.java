package com.habanec.composer2.models;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.HarmonyGrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chord {

    private Grade groundTone;
    private Grade thirdTone;
    private Grade fifthTone;
    private Grade seventhTone;

    //private List<Grade> gradeList;
    private GradeInContext harmonyFunction;

    public Chord(Grade groundTone, Grade thirdTone, Grade fifthTone, Grade seventhTone) {
        this.groundTone = groundTone;
        this.thirdTone = thirdTone;
        this.fifthTone = fifthTone;
        this.seventhTone = seventhTone;
//        gradeList = new ArrayList<>();
//        gradeList.addAll(Arrays.asList(groundTone, thirdTone, fifthTone, seventhTone));
    }

    public Chord(Grade[] grades, GradeInContext harmonyFunction) {
        groundTone = grades[0];
        thirdTone = grades[1];
        fifthTone = grades[2];
        seventhTone = (grades.length > 3) ? grades[3] : null;
        this.harmonyFunction = harmonyFunction;
    }

    public Grade getGroundTone() { return groundTone; }
    public Grade getThirdTone() { return thirdTone; }
    public Grade getFifthTone() { return fifthTone; }
    public Grade getSeventhTone() { return seventhTone; }
    //public List<Grade> getGradeList() { return gradeList; }
    public GradeInContext getHarmonyFunction() { return harmonyFunction; }

    public void setHarmonyFunction(int gradeIndex, Key currentKey) {
        this.harmonyFunction = currentKey.getGradeScale()[gradeIndex];
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
