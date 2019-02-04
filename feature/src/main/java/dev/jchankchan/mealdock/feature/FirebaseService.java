package dev.jchankchan.mealdock.feature;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class FirebaseService extends Service {

    private FirebaseApiBinder firebaseApiBinder = new FirebaseApiBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return firebaseApiBinder;
    }

    public class FirebaseApiBinder extends Binder {
        public FirebaseService getService() {
            return FirebaseService.this;
        }
    }


}
