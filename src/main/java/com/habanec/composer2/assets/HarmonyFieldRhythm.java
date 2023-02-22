package com.habanec.composer2.assets;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HarmonyFieldRhythm {
    //public static final int[][] quarterpatterns;
    public static final int[][] halfpatterns;

    static {
        halfpatterns = new int[7][];
        halfpatterns[0] = new int[] {8};
        halfpatterns[1] = new int[] {4,4};
        halfpatterns[2] = new int[] {2,2,2,2};
        halfpatterns[3] = new int[] {4,-4};
        halfpatterns[4] = new int[] {-4,4};
        halfpatterns[5] = new int[] {-2,2,2,2};
        halfpatterns[6] = new int[] {6,2};


    }
    public static int getIndex(Integer[] array) {
        for (int i = 0; i < HarmonyFieldRhythm.halfpatterns.length; i++) {
            if (Arrays.equals(Stream.of(array).mapToInt(Integer::intValue).toArray(),
                    HarmonyFieldRhythm.halfpatterns[i])) {
                return i;
            }
        }
        return -1;
    }
}
