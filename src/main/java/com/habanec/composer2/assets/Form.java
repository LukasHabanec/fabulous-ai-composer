package com.habanec.composer2.assets;

public enum Form {

    HOOK_D("ABCDAEFGCCCCFFFF",
            "ABCDABEFGHGHIGIJ",
            "BABABDEABABDDFCA",
            "aaaaabcc_aaaaddee_ccccccca_aaadaaaf",
            "aaabacab_aaaaaadb_ddddddde_bebaaaaf"
    ),
    HOOK_D_EAS("ABCDAEFGCCCCFFFF",
            "ABCDABEFGHGHIGIJ",
            "BBBBBBBBBBBBBBBB",
            "aaababab_aaabbbbb_bbbbbbba_abbaabba",
            "aaahaiad_aaaebbbd_bbbbbbbe_dedbbbaf");
            //ABCDAEFGCCFFCCFHBFBFBFFH//ABCDAEFI";

    public final String rhythmScheme;
    public final String tuneScheme;
    public final String harmonyFragmentationScheme;
    public final String figurationScheme;
    public final String harmonyRhythmScheme;

    Form(String rhythmScheme, String tuneScheme,
         String harmonyFragmentationScheme, String figurationScheme, String harmonyRhythmScheme) {
        this.rhythmScheme = rhythmScheme;
        this.tuneScheme = tuneScheme;
        this.harmonyFragmentationScheme = harmonyFragmentationScheme;
        this.figurationScheme = figurationScheme;
        this.harmonyRhythmScheme = harmonyRhythmScheme;
    }
}
