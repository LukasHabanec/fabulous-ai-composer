package com.habanec.composer2.models;

import com.habanec.composer2.assets.HarmonyFiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccompanimentFiguration {

    public final int lengthInBeat;
    public final char figurationChar;
    public final String rhythmLetters;
    public final Integer figurationNum;
    public final HarmonyFiguration harmonyFiguration;
    private Integer[] rhythmPattern;

    public AccompanimentFiguration(int lengthInBeat,
                                   char figurationChar,
                                   String rhythmLetters,
                                   Integer figurationNum,
                                   HarmonyFiguration harmonyFiguration,
                                   Integer[] rhythmPattern) {
        this.lengthInBeat = lengthInBeat;
        this.figurationChar = figurationChar;
        this.rhythmLetters = rhythmLetters;
        this.figurationNum = figurationNum;
        this.harmonyFiguration = harmonyFiguration;
        this.rhythmPattern = makeUpRhythmPattern(rhythmPattern);

    }

    private Integer[] makeUpRhythmPattern(Integer[] pattern) {
        if (pattern == null) { return null; }
        switch(lengthInBeat) {
            case 1 : {
                if (pattern[0] == 2) { return new Integer[] {2, 2}; }
                if (pattern[0] == -2) { return new Integer[] {-2, 2}; }
                return new Integer[] {4};
            }
            case 3 : {
                if (pattern[0] == 8 && pattern[1] >= 4) { return new Integer[] {12}; } //8+8, 8+6, 8+4%

                List<Integer> list = Arrays.stream(pattern).collect(Collectors.toList());
                while (sophisticatedSum(list) > 12) {
                    if (sophisticatedSum(list.subList(0,list.size() - 1)) >= 12) {
                        list.remove(list.size() -1);
                    } else {
                        if (list.get(list.size() - 1) > 0) {
                            list.set(list.size() - 1, list.get(list.size() - 1) - 2);
                        } else {
                            list.set(list.size() - 1, list.get(list.size() - 1) + 2);
                        }
                    }
                }
                return list.stream().toArray(Integer[]::new);
            }
            case 2 :
            case 4 : {
                //if (pattern[0] == 8 && pattern[1] == 8) { return new Integer[] {16}; }
            }
        }
        return pattern;
    }

    private int sophisticatedSum(List<Integer> list) {
        return list.stream()
                .mapToInt(Integer::intValue)
                .map(Math::abs)
                .sum();
    }

    public Integer[] getRhythmPattern() { return rhythmPattern; }
    public HarmonyFiguration getHarmonyFiguration() { return harmonyFiguration; }
}
