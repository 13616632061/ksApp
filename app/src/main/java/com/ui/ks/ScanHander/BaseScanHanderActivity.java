package com.ui.ks.ScanHander;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.LogUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.library.base.mvp.BaseActivity;
import com.thefinestartist.utils.content.Res;
import com.ui.ks.MipcaActivityCapture;
import com.ui.ks.R;
import com.ui.scancodetools.CameraManager;
import com.ui.scancodetools.CaptureActivityHandler;
import com.ui.scancodetools.InactivityTimer;
import com.ui.scancodetools.ViewfinderView;
import com.ui.util.ScanGunKeyEventHelper;
import com.ui.util.SpeechUtilOffline;

import java.io.IOException;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description:扫描处理
 * @Author:lyf
 * @Date: 2020/8/24
 */
public abstract class BaseScanHanderActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, ScanGunKeyEventHelper.OnScanSuccessListener {

    private static final String TAG = BaseScanHanderActivity.class.getSimpleName();
    @BindView(R.id.preview_view)
    SurfaceView previewView;
    @BindView(R.id.viewfinder_view)
    ViewfinderView viewfinderView;

    private SurfaceHolder surfaceHolder;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private SpeechUtilOffline tts;
    private ScanGunKeyEventHelper mScanGunKeyEventHelper;


    @Override
    public int getContentView() {
        return setContentView();
    }

    @Override
    protected void initView() {
        CameraManager.init(this);
        inactivityTimer = new InactivityTimer(this);
        tts = new SpeechUtilOffline(this);
        mScanGunKeyEventHelper = new ScanGunKeyEventHelper(this);

        initSurfaceHolder();
        initBeepSound();

    }

    /**
     * 返回一个用于页面显示界面的布局id
     *
     * @return
     */
    public abstract int setContentView();


    private void initSurfaceHolder() {
        surfaceHolder = previewView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(this);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * @Description:处理扫描结果
     * @Author:lyf
     * @Date: 2020/8/25
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
//        tts.play(getString(R.string.str348));
        LogUtils.i(TAG + " resultString:  " + resultString);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.seekTo(0);
    }

    /**
    *@Description:扫码枪成功
    *@Author:lyf
    *@Date: 2020/8/25
    */
    @Override
    public void onScanSuccess(String barcode) {
        Result result = new Result(barcode, null, null, null);
        handleDecode(result,null);
    }
}
