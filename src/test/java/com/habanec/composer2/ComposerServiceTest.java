package com.habanec.composer2;

import com.habanec.composer2.assets.HarmonyFiguration;
import com.habanec.composer2.assets.QuintCircle;
import com.habanec.composer2.models.HarmonyField;
import com.habanec.composer2.models.Key;
import com.habanec.composer2.assets.Mode;
import com.habanec.composer2.services.ComposerService;
import com.habanec.composer2.services.DokumentationService;
import com.habanec.composer2.services.ExternFileService;
import com.habanec.composer2.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ComposerServiceTest {
    @Autowired
    ComposerService cs;

//    @Test
//    void working_num_of_patterns_needed() {
//        assertEquals(5, cs.getNumOfPatternsNeeded("ABCDEABECE"));
//    }

    @Test
    void working_feed_midi_values() {
        Key key = new Key(QuintCircle.C, Mode.MAJOR);
        System.out.println(key.toString());
    }
//    @Test
//    void bla() {
//        HarmonyField hf = new HarmonyField(0,0, 0, 2,
//                new int[] {60, 60, 60, 60, 62, 62, 62, 62, 64, 64, 64, 64, 65, 65, 65, 65},
//                new Key(QuintCircle.C, Mode.MAJOR));
//        System.out.println(hf.getHarmonyGradesWithAccordance());
//    }

    @Test
    void listingDirectory() {
        DokumentationService ds = new ExternFileService();
        System.out.println(ds.listFilesInDirectory("./skladby"));
    }

    @Test
    void cleanupString() {
        System.out.println(Utils.cleanUpString("asdf_asdf)asdf(asdf*asdf"));
    }

    @Test
    void figuration_enums_counts() {
        System.out.println(Arrays.toString(HarmonyFiguration.values()));
        System.out.println(HarmonyFiguration.values().length);
    }

    @Test
    void array_equals_array() {
        Integer[] a1 = {1, 2, 3, 4};
        Integer[] a2 = {1, 2, 3, 4};
        Integer[] a3 = {1, 2, 3};
//        System.out.println(Utils.arrayEqualsArray(a1, a2));
//        System.out.println(Utils.arrayEqualsArray(a1, a3));
        System.out.println(Arrays.equals(a1,a2));
        System.out.println(Arrays.equals(a1,a3));
    }
}