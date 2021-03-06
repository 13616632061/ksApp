package com.ui.SafeEdit;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ui.fragment.ChooseScanCodeFragment;
import com.ui.ks.R;
import com.ui.scancodetools.CameraManager;

import java.util.Vector;

/**
 * Created by Administrator on 2017/3/31.
 */

public final class OpenorderScanCodeHander extends Handler {
    private static final String TAG = OpenorderScanCodeHander.class.getSimpleName();

    private final ChooseScanCodeFragment activity;
    private final ScanDecodeThread decodeThread;
    private OpenorderScanCodeHander.State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public OpenorderScanCodeHander(ChooseScanCodeFragment activity, Vector<BarcodeFormat> decodeFormats,
                                  String characterSet) {
        this.activity = activity;
        decodeThread = new ScanDecodeThread (activity, decodeFormats, characterSet,
                new ViewfinderResultPointCallback(activity.getViewfinderView()));
        decodeThread.start();
        state = OpenorderScanCodeHander.State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.auto_focus:
                //Log.d(TAG, "Got auto-focus message");
                // When one auto focus pass finishes, start another. This is the closest thing to
                // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
                if (state == OpenorderScanCodeHander.State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
                break;
            case R.id.restart_preview:
                Log.d(TAG, "Got restart preview message");
                restartPreviewAndDecode();
                break;
            case R.id.decode_succeeded:
                Log.d(TAG, "Got decode succeeded message");
                state = OpenorderScanCodeHander.State.SUCCESS;
                Bundle bundle = message.getData();

                /***********************************************************************/
                Bitmap barcode = bundle == null ? null :
                        (Bitmap) bundle.getParcelable(ScanDecodeThread .BARCODE_BITMAP);//锟斤拷锟矫憋拷锟斤拷锟竭筹拷

                activity.handleDecode((Result) message.obj, barcode);//锟斤拷锟截斤拷锟?        /***********************************************************************/
                break;
            case R.id.decode_failed:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = OpenorderScanCodeHander.State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
            case R.id.return_scan_result:
                Log.d(TAG, "Got return scan result message");
                activity.getActivity().setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.getActivity().finish();
                break;
            case R.id.launch_product_query:
                Log.d(TAG, "Got product query message");
                String url = (String) message.obj;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                activity.startActivity(intent);
                break;
        }
    }

    public void quitSynchronously() {
        state = OpenorderScanCodeHander.State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == OpenorderScanCodeHander.State.SUCCESS) {
            state = OpenorderScanCodeHander.State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            activity.drawViewfinder();
        }
    }

}
