package com.habanec.composer2.services;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.HarmonyFiguration;
import com.habanec.composer2.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FragmentScriptor {

    private int resolution;

    public FragmentScriptor(int resolution) {
        this.resolution = resolution;
    }

    public List<MidiTone> scriptFragment(Melody melody, HarmonyField field, int fieldTick) {
        HarmonyFiguration thisFiguration = field.getAccompanimentFiguration().harmonyFiguration;
        switch(thisFiguration) {
            case TON: return scriptOneTone(field, fieldTick);
            case OKTAVA_DOLU: return scriptOctaveDown(field, fieldTick);
            case OKTAVA_NAVRCH: return scriptOctaveUp(field, fieldTick);
            case HARM_INTERVAL: return scriptHarmInterval(field, fieldTick);
            case MEL_INTERVAL_DOLU: return scriptMelInterval(field, fieldTick, true);
            case MEL_INTERVAL_NAVRCH: return scriptMelInterval(field, fieldTick, false);
            case HARM_TERCIE_CZERNY: return scriptCzernyThirds(field, fieldTick);
            case ALBERTI_1: return scriptAlberti(field, fieldTick, new int[] {0, 2, 1, 2, 0, 2, 1, 2});
            case ALBERTI_3: return scriptAlberti(field, fieldTick, new int[] {1, 2, 0, 2, 1, 2, 0, 2});
            case ALBERTI_5: return scriptAlberti(field, fieldTick, new int[] {2, 0, 1, 0, 2, 0, 1, 0});
            case COPY_MELODY_OKTAVA: return scriptMelodyParalell(field, fieldTick, 7, false);
            case COPY_MELODY_OKTAVA_1: return scriptMelodyParalell(field, fieldTick, 7, true);
            case COPY_MELODY_DECIMA: return scriptMelodyParalell(field, fieldTick, 9, false);
            case COPY_MELODY_DECIMA_1: return scriptMelodyParalell(field, fieldTick, 9, true);
            case MEL_1_2A_2A: return scriptMelody5(field, fieldTick, new int[] {0,0,1,2,0,0,1,2});
            case MEL_2A_2A: return scriptMelody5(field, fieldTick, new int[] {0,1,2,0,1,2,0,1});
            case MEL_3A_2V_2A_2A_2A: return scriptMelody5(field, fieldTick, new int[] {0, 2, 1, 2, 3, 4, 2, 4});
            case MEL_2V_2A_3V: return scriptMelody5(field, fieldTick, new int[] { 3, 2, 3, 1, 3, 2, 3, 1 });
            case MEL_4V_5V: return scriptMelody8(field, fieldTick, new int[] { 7, 4, 0, 7, 4, 0, 7, 4 });
            case MEL_2V_2V_2V: return scriptMelody8(field, fieldTick, new int[] {7, 6, 5, 4, 3, 2, 1, 0});
            case MEL_4V_3V_3V: return scriptMelody8(field, fieldTick, new int[] {7, 4, 2, 0, 7, 4, 2, 0});
            case POMLKA: return null;
        }
        return null;
    }

    private List<MidiTone> scriptMelody8(HarmonyField field, int mainTick, int[] tunePattern) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        Grade grade = field.getChord().getGroundTone();
        int high = field.getCurrentKey().getHarmonicToneValue(grade);
        int indexOfScaleMidiValue = field.getCurrentKey().getIndexOfScaleMidiValue(high);
        int tunePatternIndex = 0;
        int tick = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            midiTones.add(new MidiTone(field.getCurrentKey().scaleMidiValues[indexOfScaleMidiValue + tunePattern[tunePatternIndex]],
                    65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tunePatternIndex++;
            tick += rhythmPattern[i];
        }
        return midiTones;

    }

    private List<MidiTone> scriptMelody5(HarmonyField field, int mainTick, int[] tunePattern) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        int[] pentachord = field.getChord().getHarmonyFunction().getHarmonyPentachordValues();
        int tick = 0;
        int tunePatternIndex = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            midiTones.add(new MidiTone(pentachord[tunePattern[tunePatternIndex]],
                    65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tunePatternIndex++;
            tick += rhythmPattern[i];
        }
        return midiTones;
    }

    private List<MidiTone> scriptMelodyParalell(HarmonyField field, int mainTick, int indexDecrement, boolean firstOnly) {
        int[] melodyMatrix = field.getMelodyFragmentMatrix();
        List<MidiTone> midiTones = extractMidiTonesFromMatrix(melodyMatrix, mainTick, firstOnly);
        for (MidiTone midiTone : midiTones) {
            int midiValueIndex = field.getCurrentKey().getIndexOfScaleMidiValue(midiTone.getHigh());
            midiTone.setHigh(field.getCurrentKey().getScaleMidiValues()[midiValueIndex - indexDecrement]);
        }
        return midiTones;
    }

    private List<MidiTone> scriptAlberti(HarmonyField field, int mainTick, int[] tunePattern) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        int[] harmoniumValues = Arrays.stream(field.getChord().getHarmonyFunction().getHarmoniumMidiValues())
                .sorted().toArray();
        int tick = 0;
        int tunePatternIndex = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            midiTones.add(new MidiTone(harmoniumValues[tunePattern[tunePatternIndex]],
                    65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tunePatternIndex++;
            tick += rhythmPattern[i];
        }
        return midiTones;
    }

    private List<MidiTone> scriptCzernyThirds(HarmonyField field, int mainTick) {
        int[] tunePattern = new int[] {0, 2, 1, 3, 2, 4, 1, 3, 0, 2, 1, 3, 2, 4, 1, 3};
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        int[] pentachord = field
                .getChord().getHarmonyFunction().getHarmonyPentachordValues();
        int tick = 0;
        int tunePatternIndex = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            midiTones.add(new MidiTone(
                    pentachord[tunePattern[tunePatternIndex]], 65 - i * 3, mainTick + tick, rhythmPattern[i]));
            midiTones.add(new MidiTone(
                    pentachord[tunePattern[tunePatternIndex + 1]], 65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tick += rhythmPattern[i];
            tunePatternIndex += 2;
        }
        return midiTones;
    }

    private List<MidiTone> scriptMelInterval(HarmonyField field, int mainTick, boolean upperNow) {
        int[] melodyMatrix = field.getMelodyFragmentMatrix();
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        int[] harmonyChord = field.getChord().getHarmonyFunction().getHarmoniumMidiValues();
        int forbiddenTone = getForbiddenTone(harmonyChord, melodyMatrix);
        List<Integer> twoTones = IntStream.of(harmonyChord)
                .filter(n -> n != forbiddenTone)
                .sorted()
                .boxed().collect(Collectors.toList());
        int tick = 0;
        int high;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            if(upperNow) { high = twoTones.get(1); }
            else { high = twoTones.get(0); }
            midiTones.add(new MidiTone(high, 65 - i * 3, mainTick + tick, rhythmPattern[i]));
            upperNow = !upperNow;
            tick += rhythmPattern[i];
        }
        return midiTones;
    }


    private List<MidiTone> scriptHarmInterval(HarmonyField field, int mainTick) {
        int[] melodyMatrix = field.getMelodyFragmentMatrix();
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        int[] harmonyChord = field.getChord().getHarmonyFunction().getHarmoniumMidiValues();
        int forbiddenTone = getForbiddenTone(harmonyChord, melodyMatrix);
        int tick = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            for (int j = 0; j < harmonyChord.length; j++) {
                if (harmonyChord[j] != forbiddenTone) {
                    midiTones.add(new MidiTone(
                            harmonyChord[j], 65 - i * 3, mainTick + tick, rhythmPattern[i]));
                }
            }
            tick += rhythmPattern[i];
        }
        return midiTones;
    }

    private List<MidiTone> scriptOctaveUp(HarmonyField field, int mainTick) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        Grade grade = field.getChord().getGroundTone();
        int high = field.getCurrentKey().getHarmonicToneValue(grade);
        int tick = 0;
        int octaviser = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            octaviser = (i % 2 == 0) ? -12 : 0;
            midiTones.add(new MidiTone(high + octaviser, 65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tick += rhythmPattern[i];
        }
        return midiTones;
    }
    private List<MidiTone> scriptOctaveDown(HarmonyField field, int mainTick) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        Grade grade = field.getChord().getGroundTone();
        int high = field.getCurrentKey().getHarmonicToneValue(grade);
        int tick = 0;
        int octaviser = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            octaviser = (i % 2 == 0) ? 0 : -12;
                    midiTones.add(new MidiTone(high + octaviser, 65 - i * 3, mainTick + tick, rhythmPattern[i]));
            tick += rhythmPattern[i];
        }
        return midiTones;
    }

    private List<MidiTone> scriptOneTone(HarmonyField field, int fieldTick) {
        Integer[] rhythmPattern = field.getAccompanimentFiguration().getRhythmPattern();
        List<MidiTone> midiTones = new ArrayList<>();
        Grade grade = field.getChord().getGroundTone();
        int high = field.getCurrentKey().getHarmonicToneValue(grade);
        int tick = 0;
        for (int i = 0; i < rhythmPattern.length; i++) {
            if (rhythmPattern[i] < 0) { //pomlka
                tick -= rhythmPattern[i];
                continue;
            }
            midiTones.add(new MidiTone(high, 65 - i * 3, fieldTick + tick, rhythmPattern[i]));
            tick += rhythmPattern[i];
        }
        return midiTones;
    }

    private int getForbiddenTone(int[] harmonyChord, int[] melodyMatrix) {
        for (int i = 0; i < melodyMatrix.length; i++) {
            for (int j = 0; j < harmonyChord.length; j++) {
                if (harmonyChord[j] == melodyMatrix[i] ||
                        harmonyChord[j] == melodyMatrix[i] - 12 ||
                        harmonyChord[j] == melodyMatrix[i] - 24 ||
                        harmonyChord[j] == melodyMatrix[i] - 36 ||
                        harmonyChord[j] == melodyMatrix[i] - 48) {
                    return harmonyChord[j];
                }
            }
        }
        return -1;
    }

    private List<MidiTone> extractMidiTonesFromMatrix(int[] melodyMatrix, int mainTick, boolean firstOnly) {

        List<MidiTone> midiTones = new ArrayList<>();
//        System.out.println("matrix: " + Arrays.toString(melodyMatrix));
        int tick = 0;
        for (int i = 0; i < melodyMatrix.length; i++) {
            int growingLength = 0;
            int index = i;
            while (index < melodyMatrix.length - 1 &&
                    melodyMatrix[index] == melodyMatrix[index + 1]) {
                growingLength++;
                index++;
            }
//            System.out.println("vyrabim ton " + melodyMatrix[i] + " o delce " + (growingLength + 1) + " na ticku " + tick);
            midiTones.add(new MidiTone(melodyMatrix[i], 90, tick + mainTick, growingLength + 1));
            if (firstOnly) { return midiTones; }
            i += growingLength;
            tick += growingLength + 1;
        }
        return midiTones;
    }

}
