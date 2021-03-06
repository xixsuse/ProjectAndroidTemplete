package com.google.android.gms.drive;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.badlogic.gdx.graphics.GL20;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.example.games.basegameutils.GameHelper;

public class Permission implements SafeParcelable {
    public static final Creator<Permission> CREATOR;
    final int zzCY;
    private int zzadA;
    private String zzadB;
    private String zzadC;
    private int zzadD;
    private boolean zzadE;
    private String zzadz;

    static {
        CREATOR = new zzh();
    }

    Permission(int versionCode, String accountIdentifier, int accountType, String accountDisplayName, String photoLink, int role, boolean isLinkRequired) {
        this.zzCY = versionCode;
        this.zzadz = accountIdentifier;
        this.zzadA = accountType;
        this.zzadB = accountDisplayName;
        this.zzadC = photoLink;
        this.zzadD = role;
        this.zzadE = isLinkRequired;
    }

    public static boolean zzbZ(int i) {
        switch (i) {
            case GL20.GL_DEPTH_BUFFER_BIT /*256*/:
            case FileUploadPreferences.BATTERY_USAGE_CHARGING_ONLY /*257*/:
            case 258:
                return true;
            default:
                return false;
        }
    }

    public static boolean zzca(int i) {
        switch (i) {
            case GameHelper.CLIENT_NONE /*0*/:
            case CompletionEvent.STATUS_FAILURE /*1*/:
            case CompletionEvent.STATUS_CONFLICT /*2*/:
            case CompletionEvent.STATUS_CANCELED /*3*/:
                return true;
            default:
                return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Permission permission = (Permission) o;
        return zzt.equal(this.zzadz, permission.zzadz) && this.zzadA == permission.zzadA && this.zzadD == permission.zzadD && this.zzadE == permission.zzadE;
    }

    public int getRole() {
        return !zzca(this.zzadD) ? -1 : this.zzadD;
    }

    public int hashCode() {
        return zzt.hashCode(this.zzadz, Integer.valueOf(this.zzadA), Integer.valueOf(this.zzadD), Boolean.valueOf(this.zzadE));
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzh.zza(this, dest, flags);
    }

    public String zzpo() {
        return !zzbZ(this.zzadA) ? null : this.zzadz;
    }

    public int zzpp() {
        return !zzbZ(this.zzadA) ? -1 : this.zzadA;
    }

    public String zzpq() {
        return this.zzadB;
    }

    public String zzpr() {
        return this.zzadC;
    }

    public boolean zzps() {
        return this.zzadE;
    }
}
