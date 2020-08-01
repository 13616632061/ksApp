package com.ui.fragment;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer; // 媒体播放器对象
    private String path;
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        path = intent.getStringExtra("url");
        Log.v("ks", "music: " + path);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        play();

        return START_STICKY;
    }

//    private void play() {
//        try {
//            mediaPlayer.reset();// 把各项参数恢复到初始状态
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare(); // 进行缓冲
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}
