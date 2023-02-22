package com.habanec.composer2.services;

import com.habanec.composer2.models.Composition;
import com.habanec.composer2.models.HarmonyField;
import com.habanec.composer2.models.MidiTone;
import org.springframework.stereotype.Service;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.sound.midi.ShortMessage.*;

@Service
public class MidiService {

    private Sequencer sequencer;
    private Sequence sonatina;
    private Track track;
    private int resolution;
    private int tickPerMeasure;
    private boolean isPlaying;
    private FragmentScriptor midiScriptor;

    private static final int MELODY_CHANNEL = 0;
    private static final int ACCOMPANIMENT_CHANNEL = 1;


    public void midiInit() {
        if (isPlaying) {
            stop();
        }
        resolution = 4; //az budu menit resolution, musim prekodovat i rhythmpatterns
        tickPerMeasure = 4 * resolution;
        midiScriptor = new FragmentScriptor(resolution);
        sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();

            /*sequencer.addMetaEventListener(new MetaEventListener() {
                public void meta(MetaMessage m) { // A message of this type is automatically sent when we reach the end of the track
                    if (m.getType() == countOfMeasures) {
                        stop();
                        System.exit(0);
                    }
                }
            });*/

            sonatina = new Sequence(Sequence.PPQ, resolution);
            track = sonatina.createTrack();
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void feedMidiSequence(Composition composition, int initialMeasure) {
        sequencer.setTempoInBPM(composition.getTempo());
        if(composition.isMelodyOn()) {
            populateMelodyByMeasures(composition, initialMeasure);
        }
        if(composition.isAccompanimentOn()) {
            populateAccompanimentByFields(composition, initialMeasure);
        }
    }

    private void populateAccompanimentByFields(Composition composition, int initialMeasure) {
        setInstrument(ACCOMPANIMENT_CHANNEL, 0, 0);
        int countOfFields = composition.getAccompaniment().getHarmonyFieldList().size();
        int idOfTheFirstFieldBelongingToInitialMeasure =
                composition.getAccompaniment().getHarmonyFieldList().stream()
                        .filter(n -> n.getBelongsToMeasure() == initialMeasure)
                        .findFirst().get().getId();
        int tick = 0;
        for (int id = idOfTheFirstFieldBelongingToInitialMeasure; id < countOfFields; id++) {
            makeOneFieldWithFigurationSelect(composition, tick, id);
            tick += composition.getAccompaniment().getHarmonyFieldList().get(id)
                    .getLengthInBeats() * resolution;
        }
    }

    private void makeOneFieldWithFigurationSelect(Composition composition,
                                                  int startingTick,
                                                  int fieldId) {
        HarmonyField thisField = composition.getAccompaniment().getHarmonyFieldList().get(fieldId);
        List<MidiTone> toneList = midiScriptor.scriptFragment(composition.getMelody(), thisField, startingTick);
//        System.out.print("field id" + fieldId + ": ");
        if (toneList == null || toneList.isEmpty()) { return; }
        for (MidiTone midiTone : toneList) {
//            System.out.print(midiTone.getHigh() + ", ");
            addMidiEventOntoTrack(ACCOMPANIMENT_CHANNEL,
                    midiTone.getHigh(), midiTone.getVolume(), midiTone.getTick(), midiTone.getLength());
        }
//        System.out.println(thisField.getCurrentKey() + thisField.getCurrentHarmonyGrade().getChord().getHarmonyFunction().getGrade().toString());
    }

    private void populateMelodyByMeasures(Composition composition, int initialMeasure) {
        setInstrument(MELODY_CHANNEL, 0, 0);
        int countOfMeasures = composition.getMelody().getMeasureList().size();
        for (int i = initialMeasure; i < countOfMeasures; i++) {
            makeMelodyMeasure(
                    composition.getMelody().getMeasureList().get(i).getTune().currentKey.getScaleMidiValues(),
                    i - initialMeasure,
                    composition.getMelody().getMeasureList().get(i).getTune().getFirstToneIndex() +
                            composition.getMelody().getUserSpecialShifters()[i],
                    composition.getMelody().getMeasureList().get(i).getRhythm().pattern,
                    composition.getMelody().getMeasureList().get(i).getTune().pattern
            );
        }
    }


    private void makeMelodyMeasure(int[] keyValues, int measureNum, int initialToneIndex,
                                   int[] rhythmValues, int[] tuneValues) {
//        System.out.println("InitialTone: " + keyValues[initialToneIndex]);
        int tick = measureNum * tickPerMeasure;
        int high;
        int volume = 100;
        for (int i = 0; i < rhythmValues.length; i++) {
            high = initialToneIndex + tuneValues[i];
            if (i > 0 && i <= rhythmValues.length / 2) { volume -= 15; }
            else if (i > rhythmValues.length / 2) { volume += 15; }

            addMidiEventOntoTrack(MELODY_CHANNEL, keyValues[high], volume, tick, rhythmValues[i]);
            tick += rhythmValues[i];
        }
    }
//    public void feedTrackWithBeat() {
//        for (int i = 0; i < countOfMeasures * tickPerMeasure; i += 2) { // DRUMS PATTERN
//            if (i % 16 - 8 == 0) {
//                makeTone(9, 38, 100, i, 1);
//            }
//            if (i % countOfMeasures == 0) {
//                makeTone(9, 36, 100, i, 1);
//            }
//            makeTone(9, 44, 70, i, 1);
//        }
//    }


    public void play() {
        try {
            sequencer.setSequence(sonatina);
            isPlaying = true;
            sequencer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean record(Composition composition, String filename, String hash) {
        feedMidiSequence(composition, 0);
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sonatina);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
            return false;
        } else {
            try {
                MidiSystem.write(sonatina, allowedTypes[0], new File(filename + "-" + hash + ".mid"));
                System.out.println("Successfully saved.");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("No success saving midi file.");
                return false;
            }
            //System.exit(0);
        }
    }


    private void setInstrument(int chan, int instrument, int tick) {
        try {
            track.add(new MidiEvent(new ShortMessage(PROGRAM_CHANGE, chan, instrument, 0), tick));
        } catch (Exception e) {
            System.out.println("Event not created.");
        }
    }

    private void addMidiEventOntoTrack(int chan, int high, int vol, int tick, int len) {
        try {
            track.add(new MidiEvent(new ShortMessage(NOTE_ON, chan, high, vol), tick));
            track.add(new MidiEvent(new ShortMessage(NOTE_OFF, chan, high, vol), tick + len));
        } catch (Exception e) {
            System.out.println("Event not created.");
        }
    }

    public void stop() {
        isPlaying = false;
        sequencer.stop();
    }
}
