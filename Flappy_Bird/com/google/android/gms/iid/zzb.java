package com.google.android.gms.iid;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.drive.events.CompletionEvent;

public interface zzb extends IInterface {

    public static abstract class zza extends Binder implements zzb {

        private static class zza implements zzb {
            private IBinder zznF;

            zza(IBinder iBinder) {
                this.zznF = iBinder;
            }

            public IBinder asBinder() {
                return this.zznF;
            }

            public void send(Message msg) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.iid.IMessengerCompat");
                    if (msg != null) {
                        obtain.writeInt(1);
                        msg.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.iid.IMessengerCompat");
        }

        public static zzb zzbR(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.iid.IMessengerCompat");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzb)) ? new zza(iBinder) : (zzb) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    data.enforceInterface("com.google.android.gms.iid.IMessengerCompat");
                    send(data.readInt() != 0 ? (Message) Message.CREATOR.createFromParcel(data) : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.iid.IMessengerCompat");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void send(Message message) throws RemoteException;
}
