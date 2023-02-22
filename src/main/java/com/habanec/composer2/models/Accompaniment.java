package com.habanec.composer2.models;

import com.habanec.composer2.services.AccompanimentService;

import java.util.*;
import java.util.stream.Collectors;

public class Accompaniment {

    private Composition composition;
    private AccompanimentService accompanimentService; // chces ho mit nestaticky, umisitis tam vsechny metody a mapOfUsed

    private String fragmentationScheme;
    private String figurationScheme;
    private String rhythmScheme;
    private Integer[] shuffledFragmentationPatterns; //zatim jen "jakoze" zamichane
    private Integer[] shuffledFigurationEnums;

    private List<AccompanimentMeasure> measureList;
    private List<HarmonyField> harmonyFieldList;

    public Accompaniment(Composition composition,
                         AccompanimentService accompanimentService,
                         Integer[] shuffledFragmentationPatterns,
                         Integer[] shuffledFigurationEnums,
                         String harmonyFragmentationScheme,
                         String harmonyFigurationScheme,
                         String harmonyRhythmScheme) {
        this.composition = composition;
        this.accompanimentService = accompanimentService;
        this.shuffledFigurationEnums = shuffledFigurationEnums;
        this.shuffledFragmentationPatterns = shuffledFragmentationPatterns;
        this.fragmentationScheme = harmonyFragmentationScheme;
        this.figurationScheme = harmonyFigurationScheme;
        this.rhythmScheme = harmonyRhythmScheme;

        measureList = accompanimentService.populateMeasureList(composition, fragmentationScheme, shuffledFragmentationPatterns);
        // AccompanimentMeasure obsahuje fragmentationPattern
        harmonyFieldList = accompanimentService.populateHarmonyFieldList(
                measureList,
                figurationScheme,
                rhythmScheme,
                shuffledFigurationEnums,
                composition.getMelody().getUserSpecialShifters()
        );
        // FieldList obsahuje vse ostatni, jsem schopen to nasazet uz pri vzniku
        harmonyFieldList = accompanimentService.runSmartAccordSuiteManager(
                0, harmonyFieldList, measureList);
        // nakonec urcuji harmonicky sled - lze az kdyz existuji fieldy
    }

    public void resetHarmonyFields(int startingMeasureIndex) {
        int startingFieldIndex = harmonyFieldList.indexOf(harmonyFieldList.stream()
                .filter(field -> field.getBelongsToMeasure() == startingMeasureIndex)
                .findFirst().get()
        );
        for (int i = startingFieldIndex; i < harmonyFieldList.size(); i++) {
            int measureIndex = harmonyFieldList.get(i).getBelongsToMeasure();
            if (!measureList.get(measureIndex).isLocked()) {
                harmonyFieldList.get(i).reset(composition.getMelody().getMeasureList().get(measureIndex).getMelodyMatrix());
            }
        }
        accompanimentService.runSmartAccordSuiteManager(startingMeasureIndex, harmonyFieldList, measureList);
    }


    public Integer[] getShuffledFigurationEnums() { return shuffledFigurationEnums; }
    public List<AccompanimentMeasure> getMeasureList() { return measureList; }
    public List<HarmonyField> getHarmonyFieldList() { return harmonyFieldList; }
    public AccompanimentService getAccompanimentService() { return accompanimentService; }

    public List<HarmonyField> getHarmonyFieldsParMeasureIndex(int index) {
        return harmonyFieldList.stream()
                .filter(n -> n.getBelongsToMeasure() == index)
                .collect(Collectors.toList());
    }

}
