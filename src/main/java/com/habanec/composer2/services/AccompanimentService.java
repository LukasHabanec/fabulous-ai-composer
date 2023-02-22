package com.habanec.composer2.services;

import com.habanec.composer2.assets.HarmonyFieldRhythm;
import com.habanec.composer2.assets.HarmonyFiguration;
import com.habanec.composer2.assets.HarmonyFragmentation;
import com.habanec.composer2.assets.HarmonyGrade;
import com.habanec.composer2.models.*;
import com.habanec.composer2.utils.Utils;

import java.util.*;

public class AccompanimentService {

    private Map<String, Integer[]> mapOfUsedLetters;

    public List<HarmonyField> populateHarmonyFieldList(List<AccompanimentMeasure> measureList,
                                                       String figurationScheme,
                                                       String rhythmScheme,
                                                       Integer[] shuffledFigurationEnums,
                                                       Integer[] melodySpecialShifters) {
        List<HarmonyField> harmonyFieldList = new ArrayList<>();
        int id = 0;
        mapOfUsedLetters = new HashMap<>();

        for (int i = 0; i < measureList.size(); i++) {

            int[] fragmentationPattern = measureList.get(i).fragmentationPattern;
            int startingBeat = 0;
            for (int j = 0; j < fragmentationPattern.length; j++) {
                //now: podle delky fieldu umistuju jedno nebo dve pismena
                char figurationChar = setAccompanimentLetterFromSchema(i,
                        fragmentationPattern[j], startingBeat, figurationScheme).charAt(0);
                String rhythmLetters = setAccompanimentLetterFromSchema(i,
                        fragmentationPattern[j], startingBeat, rhythmScheme);
                int lengthInBeats = fragmentationPattern[j];

                // 4kove a 3kove fields maji nutne jen jednu figuraci - vic nezvladnu
                Integer figurationNum = shuffledFigurationEnums[Utils.ALPHABET_LOWER.indexOf(figurationChar)];

                // bude jen jeden pattern, z funkce se mi vraci dostatecne dlouha
                //todo az zapojim vsechny figurationEnumy, musim podminkovat, jestli to nejsou oktavy, decimy, pomlka
                //jinak se vse rozmlati v nasledujici metode
                Integer[] rhythmPattern = getHarmonyFieldRhythmPattern(
                        lengthInBeats, rhythmLetters, figurationNum, figurationChar);

                AccompanimentFiguration figuration = new AccompanimentFiguration(
                        lengthInBeats,
                        figurationChar,
                        rhythmLetters,
                        figurationNum,
                        HarmonyFiguration.values()[figurationNum],
                        rhythmPattern
                );

                harmonyFieldList.add(new HarmonyField(
                        id,
                        i,
                        startingBeat,
                        fragmentationPattern[j],
                        measureList.get(i).getMelodyMatrix(),
                        measureList.get(i).getCurrentKey(),
                        melodySpecialShifters[i],
                        figuration
                ));
                id++;
                startingBeat += fragmentationPattern[j];
            }
        }
//        for (String s: mapOfUsedLetters.keySet()) {
//            System.out.println(s + " = " + Arrays.toString(mapOfUsedLetters.get(s)));
//        }
        return harmonyFieldList;
    }

    private Integer[] getHarmonyFieldRhythmPattern(int lengthInBeats,
                                                   String rhythmLetters,
                                                   Integer figurationNum,
                                                   char figurationChar) {
        if (HarmonyFiguration.values()[figurationNum].possibleRhythmPatterns == null) {
            return null;
        }
        char char1 = rhythmLetters.charAt(0);
        char char2 = (rhythmLetters.length() > 1) ? rhythmLetters.charAt(1) : '?';

        Integer[] halfpattern = mapOfUsedLetters.get(figurationChar + "_" + char1);
        if (halfpattern != null && lengthInBeats < 3) {
            return halfpattern;
        } // vrat, pokud jednoduchy halfpattern existuje

        int[] possibleRhythmPatterns = new int[]{}; // potrebuju pro pripady 1 a 2

        switch(lengthInBeats) {
            case 3 :
            case 4 : { // halfpattern nestaci, musim concat
                /**
                 * ### musis zaridit, aby odlisna figurace nemela za nasledek uplne jiny rytmus
                 * ## nez to provedes, zvaz nekolikrat, jestli to stoji v tuto chvili za refactor
                 * # znamena to sbirat do mapy pouze chars (co pismeno, to rytmus) (7=7 :D)
                 * ale taky to znamena zajistit dohled na to, zda aktualni figurace podporuje onen rhthm
                 * */

                boolean symetricChars = (char1 == char2);
                Integer[] halfpattern2 = mapOfUsedLetters.get(figurationChar + "_" + char2);
                if (halfpattern != null && halfpattern2 != null) {
                    return Utils.concatArrays(halfpattern, halfpattern2);
                } // vrat, pokud oba halfpatterny existuji (ale prisne!! vcetne figurationChar)
                //todo pozdeji - co kdyz sice existuji, ale nesmi se spojovat?? kvuli figuraci...

                // odtud bud oba nove, nebo jeden novy
                String[] rawPossibleRhythmPatternS = HarmonyFiguration.values()[figurationNum]
                        .possibleRhythmPatterns;
                List<String> possibleRhythmPatternList = new ArrayList<>();
                String[] possibleRhythmPatternS = {};

                if (rawPossibleRhythmPatternS[0].equals("999")) {
                    possibleRhythmPatternS = rawPossibleRhythmPatternS;
                } else {
                    if (halfpattern != null) {
                        int patternIndex = HarmonyFieldRhythm.getIndex(halfpattern);
                        for (String s : rawPossibleRhythmPatternS) {
                            if(patternIndex == Character.getNumericValue(s.charAt(0)) &&
                                    (s.charAt(0) != s.charAt(1) && !symetricChars)) {
                                possibleRhythmPatternList.add(s);
                                //todo tady nekde se mi stalo, ze se nepridalo nic
                            }
                        }
//                        System.out.println(possibleRhythmPatternList);
                    } else if (halfpattern2 != null) {
                        int patternIndex = HarmonyFieldRhythm.getIndex(halfpattern2);
                        for (String s : rawPossibleRhythmPatternS) {
                            if(patternIndex == Character.getNumericValue(s.charAt(1)) &&
                                    (s.charAt(0) != s.charAt(1) && !symetricChars)) {
                                possibleRhythmPatternList.add(s);
                            }
                        }
//                        System.out.println(possibleRhythmPatternList);
                    } else { // pripad, ze resim oba nove:
                        for (String s : rawPossibleRhythmPatternS) {
                            boolean symetricString = s.charAt(0) == s.charAt(1);
                            if (symetricString && symetricChars) {
                                possibleRhythmPatternList.add(s);
                            }
                            if (!symetricString && !symetricChars) {
                                possibleRhythmPatternList.add(s);
                            }
                        }
                    }
                    // seznam moznych dvojic je omezen podle toho, zda je symetricky
                    possibleRhythmPatternS = possibleRhythmPatternList.stream()
                            .toArray(String[]::new);
                } // mam z ceho vybirat a je to protridene!!!

                int firstIndex = 0, secondIndex = 0;

                int tryCount = 10;
                boolean firstOk = halfpattern != null;
                boolean secondOk = halfpattern2 != null;
                boolean firstNewForSaving = !firstOk;
                boolean secondNewForSaving = !secondOk;
                while (tryCount > 0 && (!firstOk || !secondOk)) {
                    int rnd = (int) (Math.random() * possibleRhythmPatternS.length);
                    String theString = possibleRhythmPatternS[rnd];
                    //prave jsem vylosoval string jako "24" nebo "35"
                    if (theString.equals("999")) { //zolik
                        firstIndex = (int) (Math.random() * HarmonyFieldRhythm.halfpatterns.length);
                        if (symetricChars) {
                            secondIndex = firstIndex;
                        } else {
                            do {
                                secondIndex = (int) (Math.random() * HarmonyFieldRhythm.halfpatterns.length);
                            } while (firstIndex == secondIndex);
                        }
                    } else {
                        firstIndex = Integer.parseInt(theString.substring(0, 1));
                        secondIndex = Integer.parseInt(theString.substring(1, 2));
                    }
//                    System.out.println(theString + "... prave jsem vylosoval " + firstIndex + " & " + secondIndex);
                    // ted je jeden null a druhy existuje, protoze je na seznamu, nebo jsou null oba

                    // tady check, kdyz oba projdou, neni co resit, kdyz jeden neprojde ani napodesate, smula
                    if (halfpattern == null) { // vyrabime novou? ok. ale checkuju, jestli se nepodoba
                        if (!diversityApproved(Arrays.stream(HarmonyFieldRhythm.halfpatterns[firstIndex])
                                .boxed().toArray(Integer[]::new), char1)) {
                            tryCount--;
//                        System.out.println("jeste zbyva " + tryCount + " pokusu.");
                        } else {
                            firstOk = true;
                            // vyrobils novou konstelaci
                        }
                    }
                    if (halfpattern2 == null) {
                        if (!diversityApproved(Arrays.stream(HarmonyFieldRhythm.halfpatterns[secondIndex])
                                .boxed().toArray(Integer[]::new), char2)) {
                            tryCount--;
//                            System.out.println("jeste zbyva " + tryCount + " pokusu.");
                        } else {
                            secondOk = true;
                        }
                    }
                }
                //whileloop konci, takze vim, ze lepsi firstIndex a secondIndex nenajdu a ukladam je

                if (firstNewForSaving) {
                    halfpattern = Arrays.stream(HarmonyFieldRhythm.halfpatterns[firstIndex])
                            .boxed().toArray(Integer[]::new);
                    mapOfUsedLetters.put(figurationChar + "_" + char1, halfpattern);
                }
                if (secondNewForSaving) {
                    halfpattern2 = Arrays.stream(HarmonyFieldRhythm.halfpatterns[secondIndex])
                            .boxed().toArray(Integer[]::new);
                    mapOfUsedLetters.put(figurationChar + "_" + char2, halfpattern2);
                }
                return Utils.concatArrays(halfpattern, halfpattern2);
            }

            case 1 : {
                possibleRhythmPatterns = HarmonyFiguration.values()[figurationNum]
                        .possibleRhythmQuarterpatterns;
                break;
            }
            case 2 : {
                possibleRhythmPatterns = HarmonyFiguration.values()[figurationNum]
                        .possibleRhythmHalfpatterns;
                break;
            }
        }
        Integer[] thePattern = {};
        int tryCount = 4;
        while (tryCount > 0) {

            int rnd = (int) (Math.random() * possibleRhythmPatterns.length);
            int theIndex = possibleRhythmPatterns[rnd];
            thePattern = Arrays.stream(HarmonyFieldRhythm.halfpatterns[theIndex])
                    .boxed().toArray(Integer[]::new);
            if (!diversityApproved(thePattern, char1)) {
                tryCount--;
//                System.out.println("jeste zbyva " + tryCount + " pokusu.");
            } else { break; }
        }

        mapOfUsedLetters.put(figurationChar + "_" + char1, thePattern);

        return thePattern;
    }

    private boolean diversityApproved(Integer[] pattern, char rhythmChar) {
        if (mapOfUsedLetters.isEmpty()) { return true; }
        for (String key : mapOfUsedLetters.keySet()) {
            if (Arrays.equals(pattern, mapOfUsedLetters.get(key))) {
                if (key.charAt(2) == rhythmChar) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    public List<AccompanimentMeasure> populateMeasureList(Composition composition,
                                                                 String fragmentationScheme,
                                                                 Integer[] shuffledFragmentationPatterns) {
        List<AccompanimentMeasure> measureList = new ArrayList<>();
        for (int i = 0; i < fragmentationScheme.length(); i++) {
            measureList.add(new AccompanimentMeasure(i,
                    HarmonyFragmentation.patterns[shuffledFragmentationPatterns[
                            Utils.ALPHABET.indexOf(fragmentationScheme.charAt(i))]],
                    composition.getMelody().getMeasureList().get(i).getMelodyMatrix(),
                    composition.getMelody().getMeasureList().get(i).getTune().currentKey)
            );
        }
        return measureList;
    }

    public List<HarmonyField> runSmartAccordSuiteManager(int startingMeasureIndex,
                                                                List<HarmonyField> harmonyFieldList,
                                                                List<AccompanimentMeasure> measureList) {
        System.out.println("SMART-ACCORD-SUITE-MANAGER__________________________");
        HarmonyGrade prohibitedHarmonyGradeBefore;
        HarmonyGrade prohibitedHarmonyGradeAfter;
        HarmonyGrade prohibitedHarmonyGradeSpecial = null;
        HarmonyGrade saviour = null;
        Integer[] impossibleRegister = Utils.getZeroArray(harmonyFieldList.size());
        int saviourIndex = 0;
        int returnCounter = 0;
        for (int i = 0; i < harmonyFieldList.size(); i++) {


            //="vylucuje zamcene takty a takty vyssi nez startingMeasure"
            if (harmonyFieldList.get(i).getBelongsToMeasure() >= startingMeasureIndex &&
                    !measureList.get(harmonyFieldList.get(i).getBelongsToMeasure()).isLocked()) {

                prohibitedHarmonyGradeBefore = null;
                prohibitedHarmonyGradeAfter = null;

                //="pokud ma nasledujici field jen jedinou moznost, tak tento field musi byt necim jinym"
                if (i < harmonyFieldList.size() - 1 &&
                        harmonyFieldList.get(i + 1).getMaxAccordanceHarmonyGrades().size() == 1) {
                    prohibitedHarmonyGradeAfter = harmonyFieldList.get(i + 1).getMaxAccordanceHarmonyGrades().get(0);
                }

                //="jedname-li o prvnim fieldu, bude mit tento urcite I__T__, pokud to lze"
                if (i == 0 &&
                        harmonyFieldList.get(i).getMaxAccordanceHarmonyGrades().contains(HarmonyGrade.I___T__) &&
                        prohibitedHarmonyGradeSpecial == null) {
                    harmonyFieldList.get(i).setCurrentHarmonyGrade(HarmonyGrade.I___T__);
//                    System.out.println("urcuji " + i + ", zvoleny grade : " + HarmonyGrade.I___T__);
                } else {
                    //="jedname-li o jakemkoliv fieldu mimo prvni, pro nejz by se hodil T__"

                    //="zajisti, at tento field neni stejny jako predchozi field"
                    if (i != 0 && impossibleRegister[i - 1] == 0) {
                        prohibitedHarmonyGradeBefore = harmonyFieldList.get(i - 1).getCurrentHarmonyGrade();
                    }

                    //="TADY PROBIHA SPRAVEDLIVA VOLBA"
                    int tryCount = harmonyFieldList.get(i).getMaxAccordanceHarmonyGrades().size();
                    int rndIndex = (int) (Math.random() * tryCount);
                    for (int j = 0; j < tryCount; j++) {
                        if (rndIndex >= tryCount) { rndIndex = 0; } //=obracecka

                        HarmonyGrade triedGrade = harmonyFieldList.get(i)
                                .getMaxAccordanceHarmonyGrades().get(rndIndex);

                        if ((prohibitedHarmonyGradeAfter == null ||
                                triedGrade.getIndex() != prohibitedHarmonyGradeAfter.getIndex()) &&
                                (prohibitedHarmonyGradeBefore == null ||
                                        triedGrade.getIndex() != prohibitedHarmonyGradeBefore.getIndex()) &&
                                (prohibitedHarmonyGradeSpecial == null ||
                                        triedGrade.getIndex() != prohibitedHarmonyGradeSpecial.getIndex())) {

                            harmonyFieldList.get(i).setCurrentHarmonyGrade(triedGrade);
                            prohibitedHarmonyGradeSpecial = null;
//                            System.out.println("urcuji " + i + ", zvoleny grade : " + triedGrade);
                            break;
                        } //="uspesne umisteno, forloop opoustim == uz nehledam"

                        //="aktivuje se, pokud neuspeju padesatkrat nize"
                        if (returnCounter > 5) {
                            harmonyFieldList.get(i).setCurrentHarmonyGrade(triedGrade);
                            returnCounter = 0;
                            System.out.println("urcuji " + i + ", v nouzi posledni navrh: " + triedGrade);
                            impossibleRegister[i] = 1;
                            System.out.println("imposibleRegister= " + Arrays.toString(impossibleRegister));
                            break;
                        } //="neuspesne umisteno, forloop opoustim == uz nehledam, padesatkrat stacilo"
                        rndIndex++;
                    }

                    if (harmonyFieldList.get(i).getCurrentHarmonyGrade() == null && impossibleRegister[i] == 0) {
                        //="klasicky nebylo lze umistit, vycerpal jsem maxAccordance pri dodrzeni prohibited"
                        prohibitedHarmonyGradeSpecial = harmonyFieldList.get(i - 1).getCurrentHarmonyGrade();
                        i -= 3;
                        returnCounter++;
                        System.out.println("vracime se pro zachranu");



//                        if (i == 0 && impossibleRegister[i] != 0) {
//                            System.out.println("Impossible to make it! Urcuju v nouzi T");
//                            harmonyFieldList.get(0).setCurrentHarmonyGrade(HarmonyGrade.I___T__);
//                        } else {
//                            //todo tady je neco shnile, musel jsem vypnout
//                            if (i > 0) {
//                                prohibitedHarmonyGradeSpecial = harmonyFieldList.get(i - 1).getCurrentHarmonyGrade();
//                                i -= 2;
//                                returnCounter++;
//                                System.out.println("vracime se pro zachranu");
//                            }
//
//                            if (i <= 0 || impossibleRegister[i] != 0) {
//                                if (saviourIndex == harmonyFieldList.get(0).getPossibleHarmonyGrades().size()) {
//                                    System.out.println("Impossible to make it! Urcuju v nouzi T");
//                                    harmonyFieldList.get(0).setCurrentHarmonyGrade(HarmonyGrade.I___T__);
//                                    break;
//                                } else {
//                                    saviour = harmonyFieldList.get(0).getPossibleHarmonyGrades().get(saviourIndex);
//                                    System.out.println("Pracuji se saviourem: " + saviour);
//                                    harmonyFieldList.get(0).setCurrentHarmonyGrade(saviour);
//                                    saviourIndex++;
//                                    i = 0;
//                                }
//                            }
//                        }
                    }// konci operace pro nepodarene umisteni
                }//
            }// vylucuje locked a pred startingMeasure
        }
        return harmonyFieldList;
    }


    private String setAccompanimentLetterFromSchema(int measureNum, int lengthInBeat,
                                                    int startingBeat, String scheme) {
        int schemeIndex = (startingBeat > 1) ? measureNum * 2 + 1: measureNum * 2;
        String letter = "" + scheme.charAt(schemeIndex);
        return (lengthInBeat > 2) ? letter + scheme.charAt(schemeIndex + 1) : letter;
    }
}
