package com.example.y.study;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private final static String TAG="MusicService";
    private MusicBinder musicBinder=new MusicBinder();

    public MusicService() {
    }

    class MusicBinder extends Binder{
        public void startMusic(){
            Log.i(TAG, "startMusic: ");
        }
        public void pauseMusic(){

        }
        public void stopMusic(){

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }
}
