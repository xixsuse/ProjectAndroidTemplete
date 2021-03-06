package com.badlogic.gdx.backends.android;

import android.media.AudioManager;
import android.media.SoundPool;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.IntArray;
import com.google.android.gms.cast.TextTrackStyle;

final class AndroidSound implements Sound {
    final AudioManager manager;
    final int soundId;
    final SoundPool soundPool;
    final IntArray streamIds;

    AndroidSound(SoundPool pool, AudioManager manager, int soundId) {
        this.streamIds = new IntArray(8);
        this.soundPool = pool;
        this.manager = manager;
        this.soundId = soundId;
    }

    public void dispose() {
        this.soundPool.unload(this.soundId);
    }

    public long play() {
        return play(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public long play(float volume) {
        if (this.streamIds.size == 8) {
            this.streamIds.pop();
        }
        int streamId = this.soundPool.play(this.soundId, volume, volume, 1, 0, TextTrackStyle.DEFAULT_FONT_SCALE);
        if (streamId == 0) {
            return -1;
        }
        this.streamIds.add(streamId);
        return (long) streamId;
    }

    public void stop() {
        int n = this.streamIds.size;
        for (int i = 0; i < n; i++) {
            this.soundPool.stop(this.streamIds.get(i));
        }
    }

    public void stop(long soundId) {
        this.soundPool.stop((int) soundId);
    }

    public void pause() {
        this.soundPool.autoPause();
    }

    public void pause(long soundId) {
        this.soundPool.pause((int) soundId);
    }

    public void resume() {
        this.soundPool.autoResume();
    }

    public void resume(long soundId) {
        this.soundPool.resume((int) soundId);
    }

    public void setPitch(long soundId, float pitch) {
        this.soundPool.setRate((int) soundId, pitch);
    }

    public void setVolume(long soundId, float volume) {
        this.soundPool.setVolume((int) soundId, volume, volume);
    }

    public long loop() {
        return loop(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public long loop(float volume) {
        if (this.streamIds.size == 8) {
            this.streamIds.pop();
        }
        int streamId = this.soundPool.play(this.soundId, volume, volume, 1, -1, TextTrackStyle.DEFAULT_FONT_SCALE);
        if (streamId == 0) {
            return -1;
        }
        this.streamIds.add(streamId);
        return (long) streamId;
    }

    public void setLooping(long soundId, boolean looping) {
        this.soundPool.setLoop((int) soundId, looping ? -1 : 0);
    }

    public void setPan(long soundId, float pan, float volume) {
        float leftVolume = volume;
        float rightVolume = volume;
        if (pan < 0.0f) {
            rightVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        } else if (pan > 0.0f) {
            leftVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        }
        this.soundPool.setVolume((int) soundId, leftVolume, rightVolume);
    }

    public long play(float volume, float pitch, float pan) {
        if (this.streamIds.size == 8) {
            this.streamIds.pop();
        }
        float leftVolume = volume;
        float rightVolume = volume;
        if (pan < 0.0f) {
            rightVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        } else if (pan > 0.0f) {
            leftVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        }
        int streamId = this.soundPool.play(this.soundId, leftVolume, rightVolume, 1, 0, pitch);
        if (streamId == 0) {
            return -1;
        }
        this.streamIds.add(streamId);
        return (long) streamId;
    }

    public long loop(float volume, float pitch, float pan) {
        if (this.streamIds.size == 8) {
            this.streamIds.pop();
        }
        float leftVolume = volume;
        float rightVolume = volume;
        if (pan < 0.0f) {
            rightVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        } else if (pan > 0.0f) {
            leftVolume *= TextTrackStyle.DEFAULT_FONT_SCALE - Math.abs(pan);
        }
        int streamId = this.soundPool.play(this.soundId, leftVolume, rightVolume, 1, -1, pitch);
        if (streamId == 0) {
            return -1;
        }
        this.streamIds.add(streamId);
        return (long) streamId;
    }

    public void setPriority(long soundId, int priority) {
        this.soundPool.setPriority((int) soundId, priority);
    }
}
