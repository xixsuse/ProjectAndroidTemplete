package com.google.android.gms.cast;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.internal.zzjz;
import com.google.android.gms.plus.PlusShare;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaMetadata {
    public static final String KEY_ALBUM_ARTIST = "com.google.android.gms.cast.metadata.ALBUM_ARTIST";
    public static final String KEY_ALBUM_TITLE = "com.google.android.gms.cast.metadata.ALBUM_TITLE";
    public static final String KEY_ARTIST = "com.google.android.gms.cast.metadata.ARTIST";
    public static final String KEY_BROADCAST_DATE = "com.google.android.gms.cast.metadata.BROADCAST_DATE";
    public static final String KEY_COMPOSER = "com.google.android.gms.cast.metadata.COMPOSER";
    public static final String KEY_CREATION_DATE = "com.google.android.gms.cast.metadata.CREATION_DATE";
    public static final String KEY_DISC_NUMBER = "com.google.android.gms.cast.metadata.DISC_NUMBER";
    public static final String KEY_EPISODE_NUMBER = "com.google.android.gms.cast.metadata.EPISODE_NUMBER";
    public static final String KEY_HEIGHT = "com.google.android.gms.cast.metadata.HEIGHT";
    public static final String KEY_LOCATION_LATITUDE = "com.google.android.gms.cast.metadata.LOCATION_LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE";
    public static final String KEY_LOCATION_NAME = "com.google.android.gms.cast.metadata.LOCATION_NAME";
    public static final String KEY_RELEASE_DATE = "com.google.android.gms.cast.metadata.RELEASE_DATE";
    public static final String KEY_SEASON_NUMBER = "com.google.android.gms.cast.metadata.SEASON_NUMBER";
    public static final String KEY_SERIES_TITLE = "com.google.android.gms.cast.metadata.SERIES_TITLE";
    public static final String KEY_STUDIO = "com.google.android.gms.cast.metadata.STUDIO";
    public static final String KEY_SUBTITLE = "com.google.android.gms.cast.metadata.SUBTITLE";
    public static final String KEY_TITLE = "com.google.android.gms.cast.metadata.TITLE";
    public static final String KEY_TRACK_NUMBER = "com.google.android.gms.cast.metadata.TRACK_NUMBER";
    public static final String KEY_WIDTH = "com.google.android.gms.cast.metadata.WIDTH";
    public static final int MEDIA_TYPE_GENERIC = 0;
    public static final int MEDIA_TYPE_MOVIE = 1;
    public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
    public static final int MEDIA_TYPE_PHOTO = 4;
    public static final int MEDIA_TYPE_TV_SHOW = 2;
    public static final int MEDIA_TYPE_USER = 100;
    private static final String[] zzRL;
    private static final zza zzRM;
    private final Bundle zzRN;
    private int zzRO;
    private final List<WebImage> zzvi;

    private static class zza {
        private final Map<String, String> zzRP;
        private final Map<String, String> zzRQ;
        private final Map<String, Integer> zzRR;

        public zza() {
            this.zzRP = new HashMap();
            this.zzRQ = new HashMap();
            this.zzRR = new HashMap();
        }

        public zza zzb(String str, String str2, int i) {
            this.zzRP.put(str, str2);
            this.zzRQ.put(str2, str);
            this.zzRR.put(str, Integer.valueOf(i));
            return this;
        }

        public String zzbw(String str) {
            return (String) this.zzRP.get(str);
        }

        public String zzbx(String str) {
            return (String) this.zzRQ.get(str);
        }

        public int zzby(String str) {
            Integer num = (Integer) this.zzRR.get(str);
            return num != null ? num.intValue() : MediaMetadata.MEDIA_TYPE_GENERIC;
        }
    }

    static {
        zzRL = new String[]{null, "String", "int", "double", "ISO-8601 date String"};
        zzRM = new zza().zzb(KEY_CREATION_DATE, "creationDateTime", MEDIA_TYPE_PHOTO).zzb(KEY_RELEASE_DATE, "releaseDate", MEDIA_TYPE_PHOTO).zzb(KEY_BROADCAST_DATE, "originalAirdate", MEDIA_TYPE_PHOTO).zzb(KEY_TITLE, PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, MEDIA_TYPE_MOVIE).zzb(KEY_SUBTITLE, "subtitle", MEDIA_TYPE_MOVIE).zzb(KEY_ARTIST, "artist", MEDIA_TYPE_MOVIE).zzb(KEY_ALBUM_ARTIST, "albumArtist", MEDIA_TYPE_MOVIE).zzb(KEY_ALBUM_TITLE, "albumName", MEDIA_TYPE_MOVIE).zzb(KEY_COMPOSER, "composer", MEDIA_TYPE_MOVIE).zzb(KEY_DISC_NUMBER, "discNumber", MEDIA_TYPE_TV_SHOW).zzb(KEY_TRACK_NUMBER, "trackNumber", MEDIA_TYPE_TV_SHOW).zzb(KEY_SEASON_NUMBER, "season", MEDIA_TYPE_TV_SHOW).zzb(KEY_EPISODE_NUMBER, "episode", MEDIA_TYPE_TV_SHOW).zzb(KEY_SERIES_TITLE, "seriesTitle", MEDIA_TYPE_MOVIE).zzb(KEY_STUDIO, "studio", MEDIA_TYPE_MOVIE).zzb(KEY_WIDTH, "width", MEDIA_TYPE_TV_SHOW).zzb(KEY_HEIGHT, "height", MEDIA_TYPE_TV_SHOW).zzb(KEY_LOCATION_NAME, "location", MEDIA_TYPE_MOVIE).zzb(KEY_LOCATION_LATITUDE, "latitude", MEDIA_TYPE_MUSIC_TRACK).zzb(KEY_LOCATION_LONGITUDE, "longitude", MEDIA_TYPE_MUSIC_TRACK);
    }

    public MediaMetadata() {
        this(MEDIA_TYPE_GENERIC);
    }

    public MediaMetadata(int mediaType) {
        this.zzvi = new ArrayList();
        this.zzRN = new Bundle();
        this.zzRO = mediaType;
    }

    private void zza(JSONObject jSONObject, String... strArr) {
        try {
            int length = strArr.length;
            for (int i = MEDIA_TYPE_GENERIC; i < length; i += MEDIA_TYPE_MOVIE) {
                String str = strArr[i];
                if (this.zzRN.containsKey(str)) {
                    switch (zzRM.zzby(str)) {
                        case MEDIA_TYPE_MOVIE /*1*/:
                        case MEDIA_TYPE_PHOTO /*4*/:
                            jSONObject.put(zzRM.zzbw(str), this.zzRN.getString(str));
                            break;
                        case MEDIA_TYPE_TV_SHOW /*2*/:
                            jSONObject.put(zzRM.zzbw(str), this.zzRN.getInt(str));
                            break;
                        case MEDIA_TYPE_MUSIC_TRACK /*3*/:
                            jSONObject.put(zzRM.zzbw(str), this.zzRN.getDouble(str));
                            break;
                        default:
                            break;
                    }
                }
            }
            for (String str2 : this.zzRN.keySet()) {
                if (!str2.startsWith("com.google.")) {
                    Object obj = this.zzRN.get(str2);
                    if (obj instanceof String) {
                        jSONObject.put(str2, obj);
                    } else if (obj instanceof Integer) {
                        jSONObject.put(str2, obj);
                    } else if (obj instanceof Double) {
                        jSONObject.put(str2, obj);
                    }
                }
            }
        } catch (JSONException e) {
        }
    }

    private void zzb(JSONObject jSONObject, String... strArr) {
        Set hashSet = new HashSet(Arrays.asList(strArr));
        try {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!"metadataType".equals(str)) {
                    String zzbx = zzRM.zzbx(str);
                    if (zzbx == null) {
                        Object obj = jSONObject.get(str);
                        if (obj instanceof String) {
                            this.zzRN.putString(str, (String) obj);
                        } else if (obj instanceof Integer) {
                            this.zzRN.putInt(str, ((Integer) obj).intValue());
                        } else if (obj instanceof Double) {
                            this.zzRN.putDouble(str, ((Double) obj).doubleValue());
                        }
                    } else if (hashSet.contains(zzbx)) {
                        try {
                            Object obj2 = jSONObject.get(str);
                            if (obj2 != null) {
                                switch (zzRM.zzby(zzbx)) {
                                    case MEDIA_TYPE_MOVIE /*1*/:
                                        if (!(obj2 instanceof String)) {
                                            break;
                                        }
                                        this.zzRN.putString(zzbx, (String) obj2);
                                        break;
                                    case MEDIA_TYPE_TV_SHOW /*2*/:
                                        if (!(obj2 instanceof Integer)) {
                                            break;
                                        }
                                        this.zzRN.putInt(zzbx, ((Integer) obj2).intValue());
                                        break;
                                    case MEDIA_TYPE_MUSIC_TRACK /*3*/:
                                        if (!(obj2 instanceof Double)) {
                                            break;
                                        }
                                        this.zzRN.putDouble(zzbx, ((Double) obj2).doubleValue());
                                        break;
                                    case MEDIA_TYPE_PHOTO /*4*/:
                                        if (!(obj2 instanceof String)) {
                                            break;
                                        }
                                        if (zzjz.zzbK((String) obj2) == null) {
                                            break;
                                        }
                                        this.zzRN.putString(zzbx, (String) obj2);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            }
        } catch (JSONException e2) {
        }
    }

    private boolean zzb(Bundle bundle, Bundle bundle2) {
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            Object obj2 = bundle2.get(str);
            if ((obj instanceof Bundle) && (obj2 instanceof Bundle) && !zzb((Bundle) obj, (Bundle) obj2)) {
                return false;
            }
            if (obj == null) {
                if (obj2 != null || !bundle2.containsKey(str)) {
                    return false;
                }
            } else if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    private void zzf(String str, int i) throws IllegalArgumentException {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("null and empty keys are not allowed");
        }
        int zzby = zzRM.zzby(str);
        if (zzby != i && zzby != 0) {
            throw new IllegalArgumentException("Value for " + str + " must be a " + zzRL[i]);
        }
    }

    public void addImage(WebImage image) {
        this.zzvi.add(image);
    }

    public void clear() {
        this.zzRN.clear();
        this.zzvi.clear();
    }

    public void clearImages() {
        this.zzvi.clear();
    }

    public boolean containsKey(String key) {
        return this.zzRN.containsKey(key);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MediaMetadata)) {
            return false;
        }
        MediaMetadata mediaMetadata = (MediaMetadata) other;
        return zzb(this.zzRN, mediaMetadata.zzRN) && this.zzvi.equals(mediaMetadata.zzvi);
    }

    public Calendar getDate(String key) {
        zzf(key, MEDIA_TYPE_PHOTO);
        String string = this.zzRN.getString(key);
        return string != null ? zzjz.zzbK(string) : null;
    }

    public String getDateAsString(String key) {
        zzf(key, MEDIA_TYPE_PHOTO);
        return this.zzRN.getString(key);
    }

    public double getDouble(String key) {
        zzf(key, MEDIA_TYPE_MUSIC_TRACK);
        return this.zzRN.getDouble(key);
    }

    public List<WebImage> getImages() {
        return this.zzvi;
    }

    public int getInt(String key) {
        zzf(key, MEDIA_TYPE_TV_SHOW);
        return this.zzRN.getInt(key);
    }

    public int getMediaType() {
        return this.zzRO;
    }

    public String getString(String key) {
        zzf(key, MEDIA_TYPE_MOVIE);
        return this.zzRN.getString(key);
    }

    public boolean hasImages() {
        return (this.zzvi == null || this.zzvi.isEmpty()) ? false : true;
    }

    public int hashCode() {
        int i = 17;
        for (String str : this.zzRN.keySet()) {
            i *= 31;
            i = this.zzRN.get(str).hashCode() + i;
        }
        return (i * 31) + this.zzvi.hashCode();
    }

    public Set<String> keySet() {
        return this.zzRN.keySet();
    }

    public void putDate(String key, Calendar value) {
        zzf(key, MEDIA_TYPE_PHOTO);
        this.zzRN.putString(key, zzjz.zza(value));
    }

    public void putDouble(String key, double value) {
        zzf(key, MEDIA_TYPE_MUSIC_TRACK);
        this.zzRN.putDouble(key, value);
    }

    public void putInt(String key, int value) {
        zzf(key, MEDIA_TYPE_TV_SHOW);
        this.zzRN.putInt(key, value);
    }

    public void putString(String key, String value) {
        zzf(key, MEDIA_TYPE_MOVIE);
        this.zzRN.putString(key, value);
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("metadataType", this.zzRO);
        } catch (JSONException e) {
        }
        zzjz.zza(jSONObject, this.zzvi);
        String[] strArr;
        switch (this.zzRO) {
            case MEDIA_TYPE_GENERIC /*0*/:
                strArr = new String[MEDIA_TYPE_PHOTO];
                strArr[MEDIA_TYPE_GENERIC] = KEY_TITLE;
                strArr[MEDIA_TYPE_MOVIE] = KEY_ARTIST;
                strArr[MEDIA_TYPE_TV_SHOW] = KEY_SUBTITLE;
                strArr[MEDIA_TYPE_MUSIC_TRACK] = KEY_RELEASE_DATE;
                zza(jSONObject, strArr);
                break;
            case MEDIA_TYPE_MOVIE /*1*/:
                strArr = new String[MEDIA_TYPE_PHOTO];
                strArr[MEDIA_TYPE_GENERIC] = KEY_TITLE;
                strArr[MEDIA_TYPE_MOVIE] = KEY_STUDIO;
                strArr[MEDIA_TYPE_TV_SHOW] = KEY_SUBTITLE;
                strArr[MEDIA_TYPE_MUSIC_TRACK] = KEY_RELEASE_DATE;
                zza(jSONObject, strArr);
                break;
            case MEDIA_TYPE_TV_SHOW /*2*/:
                zza(jSONObject, KEY_TITLE, KEY_SERIES_TITLE, KEY_SEASON_NUMBER, KEY_EPISODE_NUMBER, KEY_BROADCAST_DATE);
                break;
            case MEDIA_TYPE_MUSIC_TRACK /*3*/:
                zza(jSONObject, KEY_TITLE, KEY_ARTIST, KEY_ALBUM_TITLE, KEY_ALBUM_ARTIST, KEY_COMPOSER, KEY_TRACK_NUMBER, KEY_DISC_NUMBER, KEY_RELEASE_DATE);
                break;
            case MEDIA_TYPE_PHOTO /*4*/:
                zza(jSONObject, KEY_TITLE, KEY_ARTIST, KEY_LOCATION_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_WIDTH, KEY_HEIGHT, KEY_CREATION_DATE);
                break;
            default:
                zza(jSONObject, new String[MEDIA_TYPE_GENERIC]);
                break;
        }
        return jSONObject;
    }

    public void zzf(JSONObject jSONObject) {
        clear();
        this.zzRO = MEDIA_TYPE_GENERIC;
        try {
            this.zzRO = jSONObject.getInt("metadataType");
        } catch (JSONException e) {
        }
        zzjz.zza(this.zzvi, jSONObject);
        String[] strArr;
        switch (this.zzRO) {
            case MEDIA_TYPE_GENERIC /*0*/:
                strArr = new String[MEDIA_TYPE_PHOTO];
                strArr[MEDIA_TYPE_GENERIC] = KEY_TITLE;
                strArr[MEDIA_TYPE_MOVIE] = KEY_ARTIST;
                strArr[MEDIA_TYPE_TV_SHOW] = KEY_SUBTITLE;
                strArr[MEDIA_TYPE_MUSIC_TRACK] = KEY_RELEASE_DATE;
                zzb(jSONObject, strArr);
            case MEDIA_TYPE_MOVIE /*1*/:
                strArr = new String[MEDIA_TYPE_PHOTO];
                strArr[MEDIA_TYPE_GENERIC] = KEY_TITLE;
                strArr[MEDIA_TYPE_MOVIE] = KEY_STUDIO;
                strArr[MEDIA_TYPE_TV_SHOW] = KEY_SUBTITLE;
                strArr[MEDIA_TYPE_MUSIC_TRACK] = KEY_RELEASE_DATE;
                zzb(jSONObject, strArr);
            case MEDIA_TYPE_TV_SHOW /*2*/:
                zzb(jSONObject, KEY_TITLE, KEY_SERIES_TITLE, KEY_SEASON_NUMBER, KEY_EPISODE_NUMBER, KEY_BROADCAST_DATE);
            case MEDIA_TYPE_MUSIC_TRACK /*3*/:
                zzb(jSONObject, KEY_TITLE, KEY_ALBUM_TITLE, KEY_ARTIST, KEY_ALBUM_ARTIST, KEY_COMPOSER, KEY_TRACK_NUMBER, KEY_DISC_NUMBER, KEY_RELEASE_DATE);
            case MEDIA_TYPE_PHOTO /*4*/:
                zzb(jSONObject, KEY_TITLE, KEY_ARTIST, KEY_LOCATION_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_WIDTH, KEY_HEIGHT, KEY_CREATION_DATE);
            default:
                zzb(jSONObject, new String[MEDIA_TYPE_GENERIC]);
        }
    }
}
