package com.google.android.exoplayer.extractor.ogg;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.ogg.OggUtil.PageHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

public class OggExtractor implements Extractor {
    private StreamReader streamReader;

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        try {
            ParsableByteArray scratch = new ParsableByteArray(new byte[27], 0);
            PageHeader header = new PageHeader();
            if (!OggUtil.populatePageHeader(input, header, scratch, true) || (header.type & 2) != 2 || header.bodySize < 7) {
                return false;
            }
            scratch.reset();
            input.peekFully(scratch.data, 0, 7);
            if (FlacReader.verifyBitstreamType(scratch)) {
                this.streamReader = new FlacReader();
            } else {
                scratch.reset();
                if (!VorbisReader.verifyBitstreamType(scratch)) {
                    return false;
                }
                this.streamReader = new VorbisReader();
            }
            return true;
        } catch (ParserException e) {
            return false;
        }
    }

    public void init(ExtractorOutput output) {
        TrackOutput trackOutput = output.track(0);
        output.endTracks();
        this.streamReader.init(output, trackOutput);
    }

    public void seek() {
        this.streamReader.seek();
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        return this.streamReader.read(input, seekPosition);
    }
}
