package com.google.android.exoplayer.extractor.wav;

import com.google.android.exoplayer.C0989C;

final class WavHeader {
    private final int averageBytesPerSecond;
    private final int bitsPerSample;
    private final int blockAlignment;
    private long dataSize;
    private long dataStartPosition;
    private final int encoding;
    private final int numChannels;
    private final int sampleRateHz;

    public WavHeader(int numChannels, int sampleRateHz, int averageBytesPerSecond, int blockAlignment, int bitsPerSample, int encoding) {
        this.numChannels = numChannels;
        this.sampleRateHz = sampleRateHz;
        this.averageBytesPerSecond = averageBytesPerSecond;
        this.blockAlignment = blockAlignment;
        this.bitsPerSample = bitsPerSample;
        this.encoding = encoding;
    }

    public long getDurationUs() {
        return (getNumFrames() * C0989C.MICROS_PER_SECOND) / ((long) this.sampleRateHz);
    }

    public long getNumSamples() {
        return this.dataSize / ((long) getBytesPerSample());
    }

    public long getNumFrames() {
        return getNumSamples() / ((long) getNumChannels());
    }

    public int getBytesPerSample() {
        return this.blockAlignment / this.numChannels;
    }

    public int getBytesPerFrame() {
        return this.blockAlignment;
    }

    public int getBitrate() {
        return (this.sampleRateHz * this.bitsPerSample) * this.numChannels;
    }

    public int getSampleRateHz() {
        return this.sampleRateHz;
    }

    public int getNumChannels() {
        return this.numChannels;
    }

    public long getPosition(long timeUs) {
        return ((((((long) this.averageBytesPerSecond) * timeUs) / C0989C.MICROS_PER_SECOND) / ((long) this.numChannels)) * ((long) this.numChannels)) + this.dataStartPosition;
    }

    public long getTimeUs(long position) {
        return (C0989C.MICROS_PER_SECOND * position) / ((long) this.averageBytesPerSecond);
    }

    public boolean hasDataBounds() {
        return (this.dataStartPosition == 0 || this.dataSize == 0) ? false : true;
    }

    public void setDataBounds(long dataStartPosition, long dataSize) {
        this.dataStartPosition = dataStartPosition;
        this.dataSize = dataSize;
    }

    public int getEncoding() {
        return this.encoding;
    }
}
