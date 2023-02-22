package com.habanec.composer2.models;

public class MidiTone {
    private int high;
    private int volume;
    private int tick;
    private int length;

    public MidiTone(int high, int volume, int tick, int length) {
        this.high = high;
        this.volume = volume;
        this.tick = tick;
        this.length = length;
    }
    public int getHigh() { return high; }
    public int getVolume() { return volume; }
    public int getTick() { return tick; }
    public int getLength() { return length; }
    public void setHigh(int high) { this.high = high; }
}
