package com.google.android.exoplayer.text.ttml;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class TtmlSubtitle implements Subtitle {
    private final long[] eventTimesUs;
    private final Map<String, TtmlStyle> globalStyles;
    private final Map<String, TtmlRegion> regionMap;
    private final TtmlNode root;

    public TtmlSubtitle(TtmlNode root, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> regionMap) {
        this.root = root;
        this.regionMap = regionMap;
        this.globalStyles = globalStyles != null ? Collections.unmodifiableMap(globalStyles) : Collections.emptyMap();
        this.eventTimesUs = root.getEventTimesUs();
    }

    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.eventTimesUs, timeUs, false, false);
        return index < this.eventTimesUs.length ? index : -1;
    }

    public int getEventTimeCount() {
        return this.eventTimesUs.length;
    }

    public long getEventTime(int index) {
        return this.eventTimesUs[index];
    }

    public long getLastEventTime() {
        return this.eventTimesUs.length == 0 ? -1 : this.eventTimesUs[this.eventTimesUs.length - 1];
    }

    TtmlNode getRoot() {
        return this.root;
    }

    public List<Cue> getCues(long timeUs) {
        return this.root.getCues(timeUs, this.globalStyles, this.regionMap);
    }

    Map<String, TtmlStyle> getGlobalStyles() {
        return this.globalStyles;
    }
}
