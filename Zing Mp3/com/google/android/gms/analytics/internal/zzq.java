package com.google.android.gms.analytics.internal;

import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import com.facebook.share.internal.ShareConstants;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

abstract class zzq<T extends zzp> extends zzc {
    zza<T> ea;

    public interface zza<U extends zzp> {
        U zzaee();

        void zzd(String str, int i);

        void zze(String str, boolean z);

        void zzo(String str, String str2);

        void zzp(String str, String str2);
    }

    public zzq(zzf com_google_android_gms_analytics_internal_zzf, zza<T> com_google_android_gms_analytics_internal_zzq_zza_T) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.ea = com_google_android_gms_analytics_internal_zzq_zza_T;
    }

    private T zza(XmlResourceParser xmlResourceParser) {
        try {
            xmlResourceParser.next();
            int eventType = xmlResourceParser.getEventType();
            while (eventType != 1) {
                if (xmlResourceParser.getEventType() == 2) {
                    String toLowerCase = xmlResourceParser.getName().toLowerCase();
                    Object attributeValue;
                    Object trim;
                    if (toLowerCase.equals("screenname")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, ShareConstants.WEB_DIALOG_PARAM_NAME);
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(trim))) {
                            this.ea.zzo(attributeValue, trim);
                        }
                    } else if (toLowerCase.equals("string")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, ShareConstants.WEB_DIALOG_PARAM_NAME);
                        String trim2 = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || trim2 == null)) {
                            this.ea.zzp(attributeValue, trim2);
                        }
                    } else if (toLowerCase.equals("bool")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, ShareConstants.WEB_DIALOG_PARAM_NAME);
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(trim))) {
                            try {
                                this.ea.zze(attributeValue, Boolean.parseBoolean(trim));
                            } catch (NumberFormatException e) {
                                zzc("Error parsing bool configuration value", trim, e);
                            }
                        }
                    } else if (toLowerCase.equals("integer")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, ShareConstants.WEB_DIALOG_PARAM_NAME);
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(trim))) {
                            try {
                                this.ea.zzd(attributeValue, Integer.parseInt(trim));
                            } catch (NumberFormatException e2) {
                                zzc("Error parsing int configuration value", trim, e2);
                            }
                        }
                    } else {
                        continue;
                    }
                }
                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e3) {
            zze("Error parsing tracker configuration file", e3);
        } catch (IOException e4) {
            zze("Error parsing tracker configuration file", e4);
        }
        return this.ea.zzaee();
    }

    public T zzcg(int i) {
        try {
            return zza(zzabx().zzacl().getResources().getXml(i));
        } catch (NotFoundException e) {
            zzd("inflate() called with unknown resourceId", e);
            return null;
        }
    }
}
