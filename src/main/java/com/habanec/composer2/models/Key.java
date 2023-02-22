package com.habanec.composer2.models;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.Mode;
import com.habanec.composer2.assets.QuintCircle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Key {

    private QuintCircle keyLabel;
    public final GradeInContext[] myGradeScale;
    public final int[] scaleMidiValues;
    public final Mode mode;
    public final int initialMidiValue;
    private int lowestToneIndex;
    private int melodyGroundToneIndex;

    public Key(QuintCircle keyLabel, Mode mode) {
        this.keyLabel = keyLabel;
        this.mode = mode;
        initialMidiValue = keyLabel.getIniMidiTone();
         // todo bacha, na chromatiku a pentatoniku to nebude fungovat!!!
        scaleMidiValues = feedMidiValues();
        myGradeScale = feedContextGradeScale(); //puvodne bylo responsivni a slo udelat i 12 grades, ale chroma i penta kaslu

    }

    private int[] feedMidiValues() {
        //int toneCount = mode.intervals.length * 10 + 1;
        int groundTone = 0;
        List<Integer> helperList = new ArrayList<>();
        int index = 0;
        int increment = 0;

        for (int i = 0; i + initialMidiValue < 127; i += increment) {
            helperList.add(initialMidiValue + i);
            increment = mode.intervals[index % mode.intervals.length];
            if (index == 5 * mode.intervals.length) {
                groundTone = initialMidiValue + i;
                // o 4 oktavy vys nez initialMidiValue je groundTone
            }
            index++;
        }
        index = 2;
        for (int i = -1; i + initialMidiValue >= 0 ; i -= increment) {
            // DOPLNI NEJHLUBSI TONY
            if (i + initialMidiValue < 0) { break; }
            helperList.add(initialMidiValue + i);
            increment = mode.intervals[mode.intervals.length - index];
            index++;
        }
        helperList.sort(Integer::compareTo);
        melodyGroundToneIndex = helperList.indexOf(groundTone);
        lowestToneIndex = helperList.indexOf(initialMidiValue);
//        System.out.println("Ground Tone is " + groundTone);
//        System.out.println("Ground Tone Index is " + groundToneIndex);
//        System.out.println(helperList);
        return helperList.stream().mapToInt(Integer::intValue).toArray();
    }

    private GradeInContext[] feedContextGradeScale() {
        List<GradeInContext> helperList = new ArrayList<>();
        for (int g = 0; g < 7; g++) {
            helperList.add(new GradeInContext(Grade.getGrade(g),
                    this, lowestToneIndex + g));
//        System.out.println("KEY " + keyLabel + " si vyrabi myGradeScale: " + Arrays.toString(helperList.get(g).getHarmoniumMidiValues()));
        }
        return helperList.toArray(new GradeInContext[0]);
    }

    public int getHarmonicToneValue(Grade grade) {
        return myGradeScale[grade.index].getOctaveMidiValues()[4];
    }
    public int[] getScaleMidiValues() { return scaleMidiValues; }
    public int getInitialMidiValue() { return initialMidiValue; }
    public int getMelodyGroundToneIndex() { return melodyGroundToneIndex; }
    public int getLowestToneIndex() { return lowestToneIndex; }
    public String getKeyLabel() { return keyLabel.getLabel(); }
    public GradeInContext[] getGradeScale() { return myGradeScale; }
    public int getIndexOfScaleMidiValue(int midiValue) {
        return Arrays.stream(scaleMidiValues).boxed().collect(Collectors.toList()).indexOf(midiValue);
    }
    @Override
    public String toString() {
        return keyLabel.getLabel() + "-" + mode.getLabel();
    }
}
