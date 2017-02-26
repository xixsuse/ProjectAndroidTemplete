package com.google.android.gms.games.appcontent;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzkx;
import java.util.ArrayList;

public final class AppContentUtils {

    private interface AppContentRunner {
        void zzb(ArrayList<DataHolder> arrayList, int i);
    }

    /* renamed from: com.google.android.gms.games.appcontent.AppContentUtils.1 */
    static class C04271 implements AppContentRunner {
        final /* synthetic */ ArrayList zzaoH;

        C04271(ArrayList arrayList) {
            this.zzaoH = arrayList;
        }

        public void zzb(ArrayList<DataHolder> arrayList, int i) {
            this.zzaoH.add(new AppContentActionRef(arrayList, i));
        }
    }

    /* renamed from: com.google.android.gms.games.appcontent.AppContentUtils.2 */
    static class C04282 implements AppContentRunner {
        final /* synthetic */ ArrayList zzaoH;

        C04282(ArrayList arrayList) {
            this.zzaoH = arrayList;
        }

        public void zzb(ArrayList<DataHolder> arrayList, int i) {
            this.zzaoH.add(new AppContentAnnotationRef(arrayList, i));
        }
    }

    /* renamed from: com.google.android.gms.games.appcontent.AppContentUtils.3 */
    static class C04293 implements AppContentRunner {
        final /* synthetic */ ArrayList zzaoH;

        C04293(ArrayList arrayList) {
            this.zzaoH = arrayList;
        }

        public void zzb(ArrayList<DataHolder> arrayList, int i) {
            this.zzaoH.add(new AppContentConditionRef(arrayList, i));
        }
    }

    /* renamed from: com.google.android.gms.games.appcontent.AppContentUtils.4 */
    static class C04304 implements AppContentRunner {
        final /* synthetic */ DataHolder zzaoI;
        final /* synthetic */ Bundle zzaoJ;

        C04304(DataHolder dataHolder, Bundle bundle) {
            this.zzaoI = dataHolder;
            this.zzaoJ = bundle;
        }

        public void zzb(ArrayList<DataHolder> arrayList, int i) {
            AppContentTuple appContentTupleRef = new AppContentTupleRef(this.zzaoI, i);
            this.zzaoJ.putString(appContentTupleRef.getName(), appContentTupleRef.getValue());
        }
    }

    public static ArrayList<AppContentAction> zza(DataHolder dataHolder, ArrayList<DataHolder> arrayList, String str, int i) {
        ArrayList<AppContentAction> arrayList2 = new ArrayList();
        DataHolder dataHolder2 = dataHolder;
        String str2 = str;
        zza(dataHolder2, 1, str2, "action_id", i, new C04271(arrayList2), arrayList);
        return arrayList2;
    }

    private static void zza(DataHolder dataHolder, int i, String str, String str2, int i2, AppContentRunner appContentRunner, ArrayList<DataHolder> arrayList) {
        DataHolder dataHolder2 = (DataHolder) arrayList.get(i);
        Object zzd = dataHolder.zzd(str, i2, dataHolder.zzbh(i2));
        if (!TextUtils.isEmpty(zzd)) {
            int count = dataHolder2.getCount();
            String[] split = zzd.split(",");
            for (int i3 = 0; i3 < count; i3++) {
                CharSequence zzd2 = dataHolder2.zzd(str2, i3, dataHolder2.zzbh(i3));
                if (!TextUtils.isEmpty(zzd2) && zzkx.zzb(split, zzd2)) {
                    appContentRunner.zzb(arrayList, i3);
                }
            }
        }
    }

    public static ArrayList<AppContentAnnotation> zzb(DataHolder dataHolder, ArrayList<DataHolder> arrayList, String str, int i) {
        ArrayList<AppContentAnnotation> arrayList2 = new ArrayList();
        DataHolder dataHolder2 = dataHolder;
        String str2 = str;
        zza(dataHolder2, 2, str2, "annotation_id", i, new C04282(arrayList2), arrayList);
        return arrayList2;
    }

    public static ArrayList<AppContentCondition> zzc(DataHolder dataHolder, ArrayList<DataHolder> arrayList, String str, int i) {
        ArrayList<AppContentCondition> arrayList2 = new ArrayList();
        DataHolder dataHolder2 = dataHolder;
        String str2 = str;
        zza(dataHolder2, 4, str2, "condition_id", i, new C04293(arrayList2), arrayList);
        return arrayList2;
    }

    public static Bundle zzd(DataHolder dataHolder, ArrayList<DataHolder> arrayList, String str, int i) {
        Bundle bundle = new Bundle();
        DataHolder dataHolder2 = dataHolder;
        String str2 = str;
        zza(dataHolder2, 3, str2, "tuple_id", i, new C04304((DataHolder) arrayList.get(3), bundle), arrayList);
        return bundle;
    }
}
