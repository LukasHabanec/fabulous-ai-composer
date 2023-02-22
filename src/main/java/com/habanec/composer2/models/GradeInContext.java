package com.habanec.composer2.models;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.HarmonyGrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradeInContext {

    private Key currentKey;
    private Grade grade;
    private final int lowestMidiValueIndex;
    public final int[] octaveMidiValues;
    public final int[] harmoniumMidiValues;
    private final int[] harmoniumOctaveMidiValues; // vytvoreno, ale kdo ho potrebuje? Mozna nakonec nikdo!
    private final int[] harmonyPentachordValues;
    //private List<HarmonyGrade> possibleChords;

    GradeInContext(Grade grade, Key currentKey, int lowestMidiValueIndex) { //todo bacha, prichazi midiValue, refactor!
        this.grade = grade;
        this.currentKey = currentKey;
        this.lowestMidiValueIndex = lowestMidiValueIndex;
        octaveMidiValues = setOctaveMidiValues();
        harmoniumMidiValues = setHarmoniumValues(53, 64);
        harmoniumOctaveMidiValues = setHarmoniumOctaveMidiValues();
        harmonyPentachordValues = setPentachordValues(48);

        //TODO: tonicka a dominantni tonina maji jaksi stejne harmoniumMidiValues. Jak k tomu doslo!!!
        //todo a dokonce se rozhasila melodie!!! ze by lowestMidiValueIndex_refactor?
        //possibleChords = setPossibleChords();
        //System.out.println(Arrays.toString(harmoniumValues));
    }

    private int[] setPentachordValues(int minimalValue) {
        int firstToneIndex = lowestMidiValueIndex;
        while (currentKey.scaleMidiValues[firstToneIndex] < minimalValue) {
            firstToneIndex += 7;
        }
        int[] pentachord = new int[5];
        for (int i = 0; i < pentachord.length; i++) {
            pentachord[i] = currentKey.scaleMidiValues[firstToneIndex + i];
        }
//        System.out.println(currentKey + " PENTACHORD for grade " + grade + ": " + Arrays.toString(pentachord));
        return pentachord;
    }

    private int[] setHarmoniumOctaveMidiValues() { //nevim, jestli pouziju :(
        List<Integer> list = new ArrayList<>();
        int octaviser = 0;
        while (octaviser < 127) {
            list.addAll(Arrays.asList(
                    currentKey.scaleMidiValues[lowestMidiValueIndex] + octaviser,
                    currentKey.scaleMidiValues[lowestMidiValueIndex + 2] + octaviser,
                    currentKey.scaleMidiValues[lowestMidiValueIndex + 4] + octaviser));
            octaviser += 12;
        }
        return list.stream().mapToInt(n -> n).toArray();
    }

//    private List<HarmonyGrade> setPossibleChords() {
//        List<HarmonyGrade> list = new ArrayList<>();
//        for (HarmonyGrade harmonyGrade : HarmonyGrade.values()) {
//            if (harmonyGrade.getChord().getGroundTone() == grade) {
//                list.add(harmonyGrade);
//            }
//        }
//        return list;
//    }

    private int[] setHarmoniumValues(int minMidiValue, int maxMidiValue) {
        int[] chord = new int[3];
        chord[0] = currentKey.scaleMidiValues[lowestMidiValueIndex];
        chord[1] = currentKey.scaleMidiValues[lowestMidiValueIndex + 2];
        chord[2] = currentKey.scaleMidiValues[lowestMidiValueIndex + 4];
        //tady chybi 4.ton pro septakord, jindy... anebo nema existovat :) kazdy Key ma 7 Grades in context
        for (int i = 0; i < chord.length; i++) {
            while (chord[i] < minMidiValue) {
                chord[i] += 12;
            }
        }
        return chord;
    }

    private int[] setOctaveMidiValues() {
        int lowestIndex = (lowestMidiValueIndex >= 7) ? lowestMidiValueIndex - 7 : lowestMidiValueIndex;
        List<Integer> helperList = new ArrayList<>();
        for (int i = 0; currentKey.scaleMidiValues[lowestIndex] + i * 12 < 127; i++) {
            helperList.add(currentKey.scaleMidiValues[lowestIndex] + i * 12);
        }
        return helperList.stream().mapToInt(Integer::intValue).toArray();
    }


    public int[] getOctaveMidiValues() { return octaveMidiValues; }
    public Grade getGrade() { return grade; }
    //public List<HarmonyGrade> getPossibleChords() { return possibleChords; }
    public int[] getHarmoniumMidiValues() { return harmoniumMidiValues; }
    public int[] getHarmoniumOctaveMidiValues() { return harmoniumOctaveMidiValues; }
    public int[] getHarmonyPentachordValues() { return harmonyPentachordValues; }

    @Override
    public String toString() {
        return grade.name();
    }
    @Override
    public int hashCode() {
        return grade.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return this.grade.equals(((GradeInContext) obj).grade);
    }
//    private int getIndexOf(int value, int[] array) {
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] == value) {
//                return i;
//            }
//        }
//        return 0;
//    }
}
