package com.habanec.composer2.models;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.Mode;
import com.habanec.composer2.assets.Rhythm;
import com.habanec.composer2.assets.Tune;
import com.habanec.composer2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Melody {

    private Composition composition;
    private String rhythmScheme;
    private String tuneScheme;
    private Grade startingGrade;

    private List<MelodyMeasure> measureList;
    private Integer[] shuffledRhythmPatterns;
    private Integer[] shuffledTunePatterns;
    private Integer[] userSpecialShifters;

    public Melody(Composition composition, Grade startingGrade,
                  Integer[] rhythmPatterns, Integer[] tunePatterns, Integer[] userSpecialShifters) {
        this.composition = composition;
        this.startingGrade = startingGrade;
        this.shuffledRhythmPatterns = rhythmPatterns;
        this.shuffledTunePatterns = tunePatterns;
        this.userSpecialShifters = userSpecialShifters;
        rhythmScheme = composition.getForm().rhythmScheme;
        tuneScheme = composition.getForm().tuneScheme;
        populateMeasureList();
    }


    public void populateMeasureList() {
        measureList = new ArrayList<>();
        Key currentKey;
        int firstToneIndex = composition.getTonicKey().getMelodyGroundToneIndex() + startingGrade.index;
        //dejme tomu, ze Key.groundToneIndex je zdrava hodnota, ale ne vzdy. Potrebuji korekci.
        while (composition.getTonicKey().getScaleMidiValues()[firstToneIndex] < 55) {
            firstToneIndex += Mode.values()[composition.getModeIndex()].intervals.length; // vyse o oktavu
        } // takze nejnizsi ton neni nizsi nez 55 = g

        for (int i = 0; i < rhythmScheme.length(); i++) {
            if (i < 8) {  // docasne reseni harmonickeho planu
                currentKey = composition.getTonicKey();
            } else {
                currentKey = composition.getDominantKey();
            }
            measureList.add(new MelodyMeasure(
                            i,
                            rhythmScheme.charAt(i),
                            tuneScheme.charAt(i),
                            Rhythm.patterns[shuffledRhythmPatterns[Utils.ALPHABET.indexOf(
                                    rhythmScheme.charAt(i))]],
                            Tune.patterns[shuffledTunePatterns[Utils.ALPHABET.indexOf(
                                    tuneScheme.charAt(i))]],
                            currentKey,
                            firstToneIndex,
                            userSpecialShifters[i],
                            Utils.ALPHABET.indexOf(composition.getForm().harmonyFragmentationScheme.charAt(i))
                    ) //todo: posledni argument: neni to spravne, nejak tam musim dostat shuffledFigurationPatterns
            );
            firstToneIndex += measureList.get(i).getTune().getNextMeasureShifter();
        }
    }

    public void shiftUserSpecialTone(int measureIndex, int shifter) {
        for (int i = measureIndex; i < measureList.size(); i++) { // nenavysuje pouze jeden takt, ale vsechny nasledujici
            userSpecialShifters[i] += shifter;
            measureList.get(i).increaseUserSpecialShifter(shifter);

        }
        composition.getAccompaniment().getAccompanimentService().runSmartAccordSuiteManager(
                measureIndex,
                composition.getAccompaniment().getHarmonyFieldList(),
                composition.getAccompaniment().getMeasureList()
        );
    }

    public Integer[] getShuffledRhythmPatterns() { return shuffledRhythmPatterns; }
    public Integer[] getShuffledTunePatterns() { return shuffledTunePatterns; }
    public Integer[] getUserSpecialShifters() { return userSpecialShifters; }
    public String getRhythmScheme() { return rhythmScheme; }
    public String getTuneScheme() { return tuneScheme; }
    public List<MelodyMeasure> getMeasureList() { return measureList; }
    public Grade getStartingGrade() { return startingGrade; }

    public void setStartingGrade(Integer startingGradeIndex) {
        startingGrade = Grade.getGrade(startingGradeIndex);
        //todo sice zmeni grade, ale midi hodnoty se neprepisi
        //slo by to snadno: populateMeasureList() - tim ovsem vytvorim uplne novy list, zvazuji
    }
}
