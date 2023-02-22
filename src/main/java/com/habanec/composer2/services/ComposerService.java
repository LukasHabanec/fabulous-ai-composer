package com.habanec.composer2.services;

import com.habanec.composer2.assets.*;
import com.habanec.composer2.models.*;
import com.habanec.composer2.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ComposerService {

    private DokumentationService dokumentationService;
    private MidiService midiService;

    private Composition myComposition;

    @Autowired
    public ComposerService(DokumentationService dokumentationService, MidiService midiService) {
        this.dokumentationService = dokumentationService;
        this.midiService = midiService;
    }

    public Composition composeANewOpus(QuintCircle mainKey, Mode mode, Form form, Grade startingGrade) {
        myComposition = new Composition(
                Utils.setHash(4),
                form,
                mainKey.ordinal(),
                mode.ordinal(),
                startingGrade,
                getNewShuffledSet(Rhythm.patterns.length),
                getNewShuffledSet(Tune.patterns.length),
                Utils.getZeroArray(form.rhythmScheme.length()), // napr.rhythmScheme, ale je to jen kvuli poctu taktu
                getNewShuffledSet(HarmonyFiguration.values().length),
                120
        );
        return myComposition;
    }

    public Integer[] getNewShuffledSet(int length) {
//        List<Integer> list = new ArrayList<>();
        List<Integer> list = IntStream.range(0, length).boxed().collect(Collectors.toList());
//        for (int i = 0; i < length; i++) { // krmim cisly 0-moc podle existujicich patternu
//            list.add(i);
//        }
        System.out.println(list);
        Collections.shuffle(list); // zamicham
        return list.toArray(new Integer[0]);
    }


//

    public Composition loadCompositionFromTxt(String filename) {
        List<String> lines = dokumentationService.readFile(filename);
        Form form = Form.valueOf(lines.get(0));
        int modeIndex = Integer.parseInt(lines.get(1).substring(0, 1));
        int quintCircleMainKeyIndex = Integer.parseInt(lines.get(1).substring(2));
        Grade startingGrade = Grade.getGrade(Integer.parseInt(lines.get(2)));
        Integer[] rhythmSet = Utils.getIntArrayOutOfString(lines.get(3));
        Integer[] tuneSet = Utils.getIntArrayOutOfString(lines.get(4));
        Integer[] userShifters = Utils.getIntArrayOutOfString(lines.get(5));
        Integer[] figurations = Utils.getIntArrayOutOfString(lines.get(6));
        int tempo = Integer.parseInt(lines.get(7));
        String hash = lines.get(8);
        myComposition = new Composition(
                hash,
                form,
                quintCircleMainKeyIndex,
                modeIndex,
                startingGrade,
                rhythmSet,
                tuneSet,
                userShifters,
                figurations,
                tempo);
        return myComposition;
    }



    public Composition getMyComposition() {
        return myComposition;
    }

    public void playMyComposition(Integer measureIndex) {
        if (measureIndex == null) { measureIndex = 0; }
        midiService.midiInit();
        midiService.feedMidiSequence(myComposition, measureIndex);
        midiService.play();
    }

    public void stopPlayingMyComposition() {
        midiService.stop();
    }

    public String saveDocumentation() {
        return dokumentationService.save(myComposition, "skladby/" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmm")),
                myComposition.getHash()
        );
    }

    public boolean exportMidi() {
        return midiService.record(myComposition,
                "skladby/" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmm")),
                        myComposition.getHash()
        );
    }
    //TODO: momentalne nefunguje a nechci to takhle. nebude new, jen se nektere fieldy prepisi
    public void makeUpMyComposition(Integer tempo,
                                    Integer quintCircleIndex,
                                    Integer modeIndex,
                                    Integer startingGradeIndex,
                                    Boolean melodyOn,
                                    Boolean harmonyOn) {
        if (tempo != myComposition.getTempo()) {
            myComposition.setTempo(tempo);
        }
        if (quintCircleIndex != myComposition.getQuintCircleMainKeyIndex() ||
                modeIndex != myComposition.getModeIndex()) {
            myComposition = new Composition(
                    myComposition.getHash(),
                    myComposition.getForm(),
                    quintCircleIndex,
                    modeIndex,
                    Grade.getGrade(startingGradeIndex),
                    myComposition.getMelody().getShuffledRhythmPatterns(),
                    myComposition.getMelody().getShuffledTunePatterns(),
                    myComposition.getMelody().getUserSpecialShifters(),
                    myComposition.getAccompaniment().getShuffledFigurationEnums(),
                    tempo
            );
        } else if (Grade.getGrade(startingGradeIndex) != myComposition.getMelody().getStartingGrade()) {
            myComposition.getMelody().setStartingGrade(startingGradeIndex);
        }
        myComposition.setMelodyAndAccompanimentOn(melodyOn, harmonyOn);
    }

    public void shiftMeasureFirstTone(Integer measureIndex, int shifter) {
        myComposition.getMelody().shiftUserSpecialTone(measureIndex, shifter);
        myComposition.getAccompaniment().resetHarmonyFields(measureIndex);
    }

    public List<String> getContentOfDirectory(String dir) {
        return dokumentationService.listFilesInDirectory(dir);
    }

    public String print2DArray(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < array.length; y++) {
            sb.append("\n" + Utils.ALPHABET.charAt(y) + " ");
            for (int x = 0; x < array[y].length; x++) {
                sb.append(array[y][x] + " ");
            }
        }
        return sb.toString();
    }

    public long getNumOfPatternsNeeded(String formString) {
        return formString.chars()
                .distinct()
                .count();
    }

}
