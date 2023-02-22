package com.habanec.composer2.models;

import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.HarmonyGrade;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HarmonyField {

    private final Integer id;
    private final Integer belongsToMeasure;
    private final Integer startingBeat;
    private final Integer lengthInBeats;

    private final Key currentKey;
    private List<Grade> melodyGrades;
    private List<HarmonyGrade> possibleHarmonyGrades;
    private Map<HarmonyGrade, Integer> harmonyGradesWithAccordance;
    private List<HarmonyGrade> maxAccordanceHarmonyGrades;
    private HarmonyGrade currentHarmonyGrade;
    private Chord chord;
    private int[] melodyFragmentMatrix;

    private AccompanimentFiguration accompanimentFiguration;

    private String label;

//    private Accompaniment accompaniment;
    //rytmus je ruznodoby, vyres pomoci superclassy a polymorhpismu?? nebo ne? nebo se jenom rekne index a hotovo?

    public HarmonyField(Integer id,
                        Integer belongsToMeasure,
                        Integer startingBeat,
                        Integer lengthInBeats,
                        int[] melodyMatrix,
                        Key currentKey,
                        Integer melodyShifter,
                        AccompanimentFiguration af) {
        this.id = id;
        this.belongsToMeasure = belongsToMeasure;
        this.startingBeat = startingBeat;
        this.lengthInBeats = lengthInBeats; // 1/2/3/4
        this.currentKey = currentKey;
        this.accompanimentFiguration = af;
        melodyGrades = analyseMelody(melodyMatrix);
        setAllHarmonyGradesLists();
        label = accompanimentFiguration.harmonyFiguration.label + " " + Arrays.toString(af.getRhythmPattern());
//        System.out.println(
//                "ID" + id + " msr: " + (belongsToMeasure +1) + "." + (startingBeat +1) + ", length: " + lengthInBeats +
//                        ", figSchema: " + af.figurationChar + ", rhySchema: " + af.rhythmLetters + ", figNum" + af.figurationNum +
//                " = " + af.harmonyFiguration.label + ", rhythm: " + Arrays.toString(af.getRhythmPattern())
//        );
    }

    private void setAllHarmonyGradesLists() {
        List<HarmonyGrade> allHarmonyGrades = Stream.of(HarmonyGrade.values())
                .collect(Collectors.toList());
        harmonyGradesWithAccordance = new HashMap<>();
        possibleHarmonyGrades = new ArrayList<>();
        for (HarmonyGrade harmonyGrade : allHarmonyGrades) {
            int intAccordance = computeAccordance(harmonyGrade);
            if (intAccordance != 0) {
                harmonyGradesWithAccordance.put(harmonyGrade, intAccordance);
                possibleHarmonyGrades.add(harmonyGrade);
            }
        }
        maxAccordanceHarmonyGrades = new ArrayList<>();
        int maxAccordance = harmonyGradesWithAccordance.values().stream()
                .max(Comparator.naturalOrder())
                .get();
        for (HarmonyGrade grade : harmonyGradesWithAccordance.keySet()) {
            if (harmonyGradesWithAccordance.get(grade) == maxAccordance) {
                maxAccordanceHarmonyGrades.add(grade);
            }
        }
        //System.out.println("maxAccordanceGrades are with: " + maxAccordance + " : " + maxAccordanceHarmonyGrades);

    }

    private Integer computeAccordance(HarmonyGrade harmonyGrade) {
        int accordancePoints = 0;
        for (Grade grade : melodyGrades) {
            if (Arrays.asList(harmonyGrade.getChord()).contains(grade)) {
                accordancePoints++;
            }
        }
        return accordancePoints * 100 / melodyGrades.size();
    }

    private List<Grade> analyseMelody(int[] melodyMatrix) {
        //todo, aha, ledaze bych shifter nepotreboval, bo se to odvoyuje z melodymatrixu!
        List<Grade> grades = new ArrayList<>();
        melodyFragmentMatrix = new int[lengthInBeats * 4];
        /// vyrobi mapu s hustotou 4*4 na measure
        for (int i = startingBeat * 4; i < (startingBeat + lengthInBeats) * 4; i++) {
            int analysed = melodyMatrix[i];
            melodyFragmentMatrix[i - startingBeat * 4] = melodyMatrix[i];
            Grade grade = getGradeFromMatrixPoint(analysed);
            grades.add(grade);
        }
        return grades.stream().distinct().collect(Collectors.toList());
    }

    private Grade getGradeFromMatrixPoint(int analysed) {
        for (GradeInContext grade : currentKey.getGradeScale()) {
            for (int tone : grade.getOctaveMidiValues()) {
                if (tone == analysed) {
                    return grade.getGrade();
                }
            }
        }
        return null;
    }

    public void reset(int[] melodyMatrix) {
        melodyGrades = analyseMelody(melodyMatrix);
        setAllHarmonyGradesLists();
    }
    public void setCurrentHarmonyGrade(HarmonyGrade harmonyGrade) {
        currentHarmonyGrade = harmonyGrade;
        chord = new Chord(getCurrentHarmonyGrade().getChord(), currentKey.myGradeScale[harmonyGrade.getIndex()]);
//        System.out.println("jeden harmonyField cislo " + id + " dostal pridelenu funkci " +
//                Arrays.toString(currentHarmonyGrade.getChord().getHarmonyFunction().getHarmoniumMidiValues()));
    }

    public Integer getId() { return id; }
    public Integer getStartingBeat() { return startingBeat; }
    public Integer getLengthInBeats() { return lengthInBeats; }
    public Key getCurrentKey() { return currentKey; }
    public List<Grade> getMelodyGrades() { return melodyGrades; }
    public Map<HarmonyGrade, Integer> getHarmonyGradesWithAccordance() { return harmonyGradesWithAccordance; }
    public HarmonyGrade getCurrentHarmonyGrade() { return currentHarmonyGrade; }
    public List<HarmonyGrade> getPossibleHarmonyGrades() { return possibleHarmonyGrades; }
    public List<HarmonyGrade> getMaxAccordanceHarmonyGrades() { return maxAccordanceHarmonyGrades; }
    public int[] getMelodyFragmentMatrix() { return melodyFragmentMatrix; }
    public Integer getBelongsToMeasure() { return belongsToMeasure; }
    public AccompanimentFiguration getAccompanimentFiguration() { return accompanimentFiguration; }
    public String getLabel() { return label; }
    public Chord getChord() { return chord; }
}
