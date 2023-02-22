package com.habanec.composer2.assets;

public enum HarmonyFiguration {
    TON("1ton", new int[] {1, 2}, new int[] {0, 1, 2, 3, 4, 5, 6}, new String[] {"999"}),
    OKTAVA_DOLU("OktV", new int[] {2}, new int[] {1, 2, 5, 6}, new String[] {"999"}),
    OKTAVA_NAVRCH("OktA", new int[] {2}, new int[] {1, 2, 5, 6}, new String[] {"999"}),
    HARM_INTERVAL("hInt", new int[] {1, 2, 5}, new int[] {0, 1, 2, 3, 4, 5, 6}, new String[] {"999"}),
    MEL_INTERVAL_NAVRCH("mInA", new int[] {2}, new int[] {1, 2, 5, 6}, new String[] {"999"}),
    MEL_INTERVAL_DOLU("mInV", new int[] {2}, new int[] {1, 2, 5, 6}, new String[] {"999"}),
    HARM_TERCIE_CZERNY("Terc", new int[] {1}, new int[] {2, 5, 6}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "41", "42", "45", "46",
            "51", "52", "55", "56", "60", "61", "62", "63", "65", "66"
    }),
    ALBERTI_1("Alb1", new int[] {1, 2}, new int[] {2, 5}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
            "50", "51", "52", "53", "54", "55", "56", "60", "61", "62", "63", "64", "65", "66"
    }),
    ALBERTI_3("Alb3", new int[] {1, 2}, new int[] {2, 5}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
            "50", "51", "52", "53", "54", "55", "56", "60", "61", "62", "63", "64", "65", "66"
    }),
    ALBERTI_5("Alb5", new int[] {1, 2}, new int[] {2, 5}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
            "50", "51", "52", "53", "54", "55", "56", "60", "61", "62", "63", "64", "65", "66"
    }),
    MEL_1_2A_2A("__-T", new int[] {1, 2}, new int[] {2, 5}, new String[] {
            "02", "05", "11", "12", "16",
            "21", "22", "25", "26", "32", "35", "42", "45",
            "50", "51", "52", "53", "54", "55", "56", "61", "62", "65", "66"
    }),
    MEL_2A_2A("_-T ", new int[] {1, 2}, new int[] {2, 5, 6}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
            "51", "52", "60", "61", "62", "63", "65", "66"
    }),
    MEL_3A_2V_2A_2A_2A("_T-T", new int[] {2}, new int[] {2, 5}, new String[] {
            "01", "02", "05", "06", "11", "12", "15", "16",
            "21", "22", "32", "35", "36", "42", "45", "46",
            "50", "51", "52", "53", "55", "56", "61", "62", "65", "66"
    }),
    MEL_4V_5V("8518", new int[] {1, 2}, new int[] {2, 5}, new String[] {
            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
            "50", "51", "52", "53", "54", "55", "56", "60", "61", "62", "63", "64", "65", "66"
    }),
    MEL_2V_2V_2V("T-_ ", new int[] {1}, new int[] {2, 5, 6}, new String[] { "999" }),
    MEL_2V_2A_3V("T-T_", new int[] {1, 2}, new int[] {2, 5}, new String[] { "999" }),
    MEL_4V_3V_3V("8531", new int[] {1}, new int[] {2, 5}, new String[] { "999"
//            "01", "02", "05", "06", "10", "11", "12", "13", "15", "16",
//            "20", "21", "22", "23", "25", "26", "31", "32", "35", "36", "41", "42", "45", "46",
//            "50", "51", "52", "53", "54", "55", "56", "60", "61", "62", "63", "64", "65", "66"
    }),
    COPY_MELODY_DECIMA("mDc*", null, null, null),
    COPY_MELODY_DECIMA_1("mDc1", null, null, null),
    COPY_MELODY_OKTAVA("mOk*", null, null, null),
    COPY_MELODY_OKTAVA_1("mDc1", null,null, null),
    POMLKA("REST", null, null, null);

    public final String label;
    public final int[] possibleRhythmQuarterpatterns;
    public final int[] possibleRhythmHalfpatterns;
    public final String[] possibleRhythmPatterns;

    HarmonyFiguration(String label, int[] possbleRhythmQuarterpatterns, int[] possibleRhythmHalfpatterns, String[] possibleRhythmPatterns) {
        this.label = label;
        this.possibleRhythmQuarterpatterns = possbleRhythmQuarterpatterns;
        this.possibleRhythmHalfpatterns = possibleRhythmHalfpatterns;
        this.possibleRhythmPatterns = possibleRhythmPatterns;
    }

}
