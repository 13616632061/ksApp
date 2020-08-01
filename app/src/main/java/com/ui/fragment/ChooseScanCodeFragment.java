package com.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ui.SafeEdit.OpenorderScanCodeHander;
import com.ui.SafeEdit.SafeEdit;
import com.ui.SafeEdit.ViewfinderView;
import com.ui.db.DBHelper;
import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.scancodetools.CameraManager;
import com.ui.scancodetools.InactivityTimer;
import com.ui.util.CustomRequest;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 开单选择扫码商品界面
 * Created by Administrator on 2017/3/31.
 */

public class ChooseScanCodeFragment extends BaseFragmentMainBranch implements SurfaceHolder.Callback {
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private OpenorderScanCodeHander handler;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private ViewfinderView viewfinderView;
    private  SurfaceView surfaceView;
    private SafeEdit et_safeedit;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==200){
                handler.restartPreviewAndDecode();//重新初始化，实现联续扫码
            }

        }
    };
    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.choosescancodefragment_layout,null);
        viewfinderView= (ViewfinderView) view.findViewById(R.id.viewfinder_view);
        surfaceView=(SurfaceView) view.findViewById(R.id.preview_view);
        et_safeedit= (SafeEdit) view.findViewById(R.id. et_safeedit);
        et_safeedit.getBackground().setAlpha(115);
//        et_safeedit.setBackgroundColor(Color.argb(100, 0, 0, 0)); //背景透明度
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
//        et_safeedit.setText(resultString);
        Scan_bncode(resultString);



    }
    /**
     * 扫描条码，获取商品信息
     */
    Double priceDouble;
    private void Scan_bncode(String bncode){
        Map<String,String> map=new HashMap<>();
        map.put("bncode",bncode);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("Scan_bncode"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("开单扫码ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONObject data=ret.getJSONObject("data");
                        String goods_id=data.getString("goods_id");
                        String name=data.getString("name");
                        String price=data.getString("price");
                        if(price!=null){
                            priceDouble=Double.parseDouble(price);
                        }
                        String tag_id=data.getString("tag_id");
                        String tag_name=data.getString("tag_name");
                        Goods_info goodsInfo=new Goods_info(name,"",goods_id,0,0,priceDouble,"",0,1,tag_id,true,1,"","");
                        addopenorder(goodsInfo);
                        getActivity().sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",1));
                        getActivity().sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",9));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    handler1.sendEmptyMessageDelayed(200,3000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });
        RequestManager.addRequest(r, mContext);

    }

    /**
     * 添加到购物车数据库
     * @param goods_info
     */
    private void addopenorder(Goods_info goods_info){
//        clearShoppingCarDB();
        DBHelper dbHelper = DBHelper.getInstance(getActivity());
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder WHERE id = ?";
        Cursor cursor = sqlite.rawQuery(sql, new String[] { goods_info.getGoods_id()});
        if(cursor.moveToFirst()){
            int num_db=cursor.getInt(cursor.getColumnIndex("num"));
            num_db++;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num_db});
        }else {
            sqlite.execSQL("insert into openorder (id,tag_id,name,price,num,obj_id,item_id) values(?,?,?,?,?,?,?)",
                    new Object[]{goods_info.getGoods_id(),goods_info.getTag_id(),
                            goods_info.getName(),goods_info.getPrice(),goods_info.getSelect_num(),goods_info.getObj_id(),goods_info.getItem_id()});

        }
        cursor.close();
        sqlite.close();
    }
    @Override
    public  void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        mContext.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_ChooseScanCodeFrament_ACTION));

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String bncode=et_safeedit.getText().toString().trim();
            if(!TextUtils.isEmpty(bncode)){
                Scan_bncode(bncode);
            }
        }
    };
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

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
    @Override
    public  void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(broadcastReceiver);
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private static final long VIBRATE_DURATION = 200L;
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
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
            handler = new OpenorderScanCodeHander(ChooseScanCodeFragment.this, decodeFormats,
                    characterSet);
        }
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


}
