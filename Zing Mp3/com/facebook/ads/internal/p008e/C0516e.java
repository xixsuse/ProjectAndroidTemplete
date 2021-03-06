package com.facebook.ads.internal.p008e;

import com.facebook.ads.AdError;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.e.e */
public class C0516e {
    private static final String f654c;
    private static final AdPlacementType f655d;
    public int f656a;
    public int f657b;
    private AdPlacementType f658e;
    private int f659f;
    private int f660g;
    private int f661h;
    private int f662i;
    private int f663j;
    private int f664k;
    private int f665l;
    private boolean f666m;
    private List<C0512b> f667n;

    static {
        f654c = C0516e.class.getSimpleName();
        f655d = AdPlacementType.UNKNOWN;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private C0516e(java.util.Map<java.lang.String, java.lang.String> r11) {
        /*
        r10 = this;
        r4 = 1;
        r3 = -1;
        r2 = 0;
        r10.<init>();
        r10.f656a = r3;
        r10.f657b = r3;
        r0 = f655d;
        r10.f658e = r0;
        r10.f659f = r4;
        r10.f660g = r2;
        r10.f661h = r2;
        r0 = 20;
        r10.f662i = r0;
        r10.f663j = r2;
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r10.f664k = r0;
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r10.f665l = r0;
        r10.f666m = r2;
        r0 = 0;
        r10.f667n = r0;
        r0 = r11.entrySet();
        r5 = r0.iterator();
    L_0x002f:
        r0 = r5.hasNext();
        if (r0 == 0) goto L_0x01f5;
    L_0x0035:
        r0 = r5.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (java.lang.String) r1;
        r6 = r1.hashCode();
        switch(r6) {
            case -1899431321: goto L_0x00cc;
            case -1561601017: goto L_0x0082;
            case -856794442: goto L_0x00b5;
            case -726276175: goto L_0x00c0;
            case -553208868: goto L_0x008c;
            case 3575610: goto L_0x005a;
            case 700812481: goto L_0x0064;
            case 858630459: goto L_0x006e;
            case 1085444827: goto L_0x0078;
            case 1183549815: goto L_0x00aa;
            case 1503616961: goto L_0x00a0;
            case 2002133996: goto L_0x0096;
            default: goto L_0x0048;
        };
    L_0x0048:
        r1 = r3;
    L_0x0049:
        switch(r1) {
            case 0: goto L_0x004d;
            case 1: goto L_0x00d8;
            case 2: goto L_0x00e6;
            case 3: goto L_0x00f4;
            case 4: goto L_0x0102;
            case 5: goto L_0x0110;
            case 6: goto L_0x0122;
            case 7: goto L_0x0130;
            case 8: goto L_0x013e;
            case 9: goto L_0x014c;
            case 10: goto L_0x015a;
            case 11: goto L_0x0168;
            default: goto L_0x004c;
        };
    L_0x004c:
        goto L_0x002f;
    L_0x004d:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = com.facebook.ads.internal.server.AdPlacementType.fromString(r0);
        r10.f658e = r0;
        goto L_0x002f;
    L_0x005a:
        r6 = "type";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x0062:
        r1 = r2;
        goto L_0x0049;
    L_0x0064:
        r6 = "min_viewability_percentage";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x006c:
        r1 = r4;
        goto L_0x0049;
    L_0x006e:
        r6 = "viewability_check_ticker";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x0076:
        r1 = 2;
        goto L_0x0049;
    L_0x0078:
        r6 = "refresh";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x0080:
        r1 = 3;
        goto L_0x0049;
    L_0x0082:
        r6 = "refresh_threshold";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x008a:
        r1 = 4;
        goto L_0x0049;
    L_0x008c:
        r6 = "cacheable";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x0094:
        r1 = 5;
        goto L_0x0049;
    L_0x0096:
        r6 = "placement_width";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x009e:
        r1 = 6;
        goto L_0x0049;
    L_0x00a0:
        r6 = "placement_height";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x00a8:
        r1 = 7;
        goto L_0x0049;
    L_0x00aa:
        r6 = "viewability_check_initial_delay";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x00b2:
        r1 = 8;
        goto L_0x0049;
    L_0x00b5:
        r6 = "viewability_check_interval";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x00bd:
        r1 = 9;
        goto L_0x0049;
    L_0x00c0:
        r6 = "request_timeout";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x00c8:
        r1 = 10;
        goto L_0x0049;
    L_0x00cc:
        r6 = "conv_tracking_data";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0048;
    L_0x00d4:
        r1 = 11;
        goto L_0x0049;
    L_0x00d8:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f659f = r0;
        goto L_0x002f;
    L_0x00e6:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f660g = r0;
        goto L_0x002f;
    L_0x00f4:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f661h = r0;
        goto L_0x002f;
    L_0x0102:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f662i = r0;
        goto L_0x002f;
    L_0x0110:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Boolean.valueOf(r0);
        r0 = r0.booleanValue();
        r10.f666m = r0;
        goto L_0x002f;
    L_0x0122:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f656a = r0;
        goto L_0x002f;
    L_0x0130:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f657b = r0;
        goto L_0x002f;
    L_0x013e:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f663j = r0;
        goto L_0x002f;
    L_0x014c:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f664k = r0;
        goto L_0x002f;
    L_0x015a:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = java.lang.Integer.parseInt(r0);
        r10.f665l = r0;
        goto L_0x002f;
    L_0x0168:
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = com.facebook.ads.internal.p008e.C0512b.m800a(r0);
        r10.f667n = r0;
        r1 = android.webkit.CookieManager.getInstance();	 Catch:{ Exception -> 0x01d9 }
        r6 = r1.acceptCookie();	 Catch:{ Exception -> 0x01d9 }
        r0 = 1;
        r1.setAcceptCookie(r0);	 Catch:{ Exception -> 0x01d9 }
        r0 = r10.f667n;	 Catch:{ Exception -> 0x01d9 }
        r7 = r0.iterator();	 Catch:{ Exception -> 0x01d9 }
    L_0x0186:
        r0 = r7.hasNext();	 Catch:{ Exception -> 0x01d9 }
        if (r0 == 0) goto L_0x01e3;
    L_0x018c:
        r0 = r7.next();	 Catch:{ Exception -> 0x01d9 }
        r0 = (com.facebook.ads.internal.p008e.C0512b) r0;	 Catch:{ Exception -> 0x01d9 }
        r8 = r0.m802b();	 Catch:{ Exception -> 0x01d9 }
        if (r8 == 0) goto L_0x0186;
    L_0x0198:
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01d9 }
        r8.<init>();	 Catch:{ Exception -> 0x01d9 }
        r9 = r0.f639b;	 Catch:{ Exception -> 0x01d9 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = "=";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = r0.f640c;	 Catch:{ Exception -> 0x01d9 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = ";Domain=";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = r0.f638a;	 Catch:{ Exception -> 0x01d9 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = ";Expires=";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = r0.m801a();	 Catch:{ Exception -> 0x01d9 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r9 = ";path=/";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x01d9 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x01d9 }
        r0 = r0.f638a;	 Catch:{ Exception -> 0x01d9 }
        r1.setCookie(r0, r8);	 Catch:{ Exception -> 0x01d9 }
        goto L_0x0186;
    L_0x01d9:
        r0 = move-exception;
        r0 = f654c;
        r1 = "Failed to set cookie.";
        android.util.Log.w(r0, r1);
        goto L_0x002f;
    L_0x01e3:
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01d9 }
        r7 = 21;
        if (r0 >= r7) goto L_0x01f0;
    L_0x01e9:
        r0 = android.webkit.CookieSyncManager.getInstance();	 Catch:{ Exception -> 0x01d9 }
        r0.startSync();	 Catch:{ Exception -> 0x01d9 }
    L_0x01f0:
        r1.setAcceptCookie(r6);	 Catch:{ Exception -> 0x01d9 }
        goto L_0x002f;
    L_0x01f5:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.e.e.<init>(java.util.Map):void");
    }

    public static C0516e m809a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        Iterator keys = jSONObject.keys();
        Map hashMap = new HashMap();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            hashMap.put(str, String.valueOf(jSONObject.opt(str)));
        }
        return new C0516e(hashMap);
    }

    public AdPlacementType m810a() {
        return this.f658e;
    }

    public long m811b() {
        return (long) (this.f661h * AdError.NETWORK_ERROR_CODE);
    }

    public long m812c() {
        return (long) (this.f662i * AdError.NETWORK_ERROR_CODE);
    }

    public boolean m813d() {
        return this.f666m;
    }

    public int m814e() {
        return this.f659f;
    }

    public int m815f() {
        return this.f660g;
    }

    public int m816g() {
        return this.f663j;
    }

    public int m817h() {
        return this.f664k;
    }

    public int m818i() {
        return this.f665l;
    }
}
