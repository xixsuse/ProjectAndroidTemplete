package com.facebook.ads.internal.p010h.p011a;

import android.os.Build.VERSION;
import android.util.Base64;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;

/* renamed from: com.facebook.ads.internal.h.a.o */
public class C0568o {
    private static String m995a(byte[] bArr, String str) {
        MessageDigest instance = MessageDigest.getInstance(str);
        instance.reset();
        return Base64.encodeToString(instance.digest(bArr), 0);
    }

    public static void m996a(HttpsURLConnection httpsURLConnection, Set<String> set, Set<String> set2) {
        if (VERSION.SDK_INT != 15 || !"4.0.3".equals(VERSION.RELEASE)) {
            try {
                Certificate[] serverCertificates = httpsURLConnection.getServerCertificates();
                int length = serverCertificates.length;
                int i = 0;
                while (i < length) {
                    X509Certificate x509Certificate = (X509Certificate) serverCertificates[i];
                    String a = C0568o.m995a(x509Certificate.getEncoded(), "SHA-1");
                    if (set == null || !set.contains(a)) {
                        String a2 = C0568o.m995a(x509Certificate.getPublicKey().getEncoded(), "SHA-1");
                        if (set2 == null || !set2.contains(a2)) {
                            i++;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                throw new CertificateException("Unable to find valid certificate or public key.");
            } catch (Exception e) {
                throw e;
            }
        }
    }
}