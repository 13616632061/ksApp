package com.ui.ks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ui.SafeEdit.SoftKeyBoard;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.OrderGoods;
import com.ui.ks.ScanHander.BaseScanHanderActivity;
import com.ui.scancodetools.CameraManager;
import com.ui.scancodetools.CaptureActivityHandler;
import com.ui.scancodetools.InactivityTimer;
import com.ui.scancodetools.ViewfinderView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.PreferencesService;
import com.ui.util.RequestManager;
import com.ui.util.ScanGunKeyEventHelper;
import com.ui.util.SpeechUtilOffline;
import com.ui.util.SysUtils;
import com.ui.util.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 扫描器页面
 */

public class MipcaActivityCapture extends BaseScanHanderActivity implements SurfaceHolder.Callback, View.OnClickListener,ScanGunKeyEventHelper.OnScanSuccessListener, TextWatcher {

    private ViewfinderView viewfinderView;
    private ImageButton mipca_capture_back;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private RelativeLayout cash_layout,paycode_layout;

    private  String total_fee;
    private  String total_fee_double;
    private  String pay_type;
    private TextView mipca_capture_description,mipca_capture_price,tv_explain,mipca_capture_title;
    // 语音合成客户端
    private SpeechUtilOffline tts;
    private  ScanGunKeyEventHelper mScanGunKeyEventHelper;
    private int type;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private double total_price;
    private ArrayList<OrderGoods> goodsList;
    private String goodsname;
    private String goodsprice;
    private String goodsnum;
    private PreferencesService service;
    EditText tv_keyboard;


//    SoftKeyBoard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mipca_capture);

        SysUtils.setupUI(this, findViewById(R.id.activity_mipca_capture));
        CameraManager.init(this);

        initView();
    }

    protected void initView() {
        viewfinderView= (ViewfinderView) findViewById(R.id.viewfinder_view);
        mipca_capture_back= (ImageButton) findViewById(R.id.mipca_capture_back);
        mipca_capture_price= (TextView) findViewById(R.id.mipca_capture_price);
        mipca_capture_title= (TextView) findViewById(R.id.mipca_capture_title);
        tv_explain= (TextView) findViewById(R.id.tv_explain);
        mipca_capture_description= (TextView) findViewById(R.id.mipca_capture_description);
        cash_layout= (RelativeLayout) findViewById(R.id.cash_layout);
        paycode_layout= (RelativeLayout) findViewById(R.id.paycode_layout);

        tv_keyboard= (EditText) findViewById(R.id.tv_keyboard);

        tts = new SpeechUtilOffline(MipcaActivityCapture.this);

        ViewUtils.setNokeyboard(tv_keyboard);

        tv_keyboard.setOnClickListener(this);
        tv_keyboard.addTextChangedListener(this);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        mipca_capture_back.setOnClickListener(this);
        mipca_capture_description.setOnClickListener(this);
        cash_layout.setOnClickListener(this);
        paycode_layout.setOnClickListener(this);

        Intent intent=getIntent();
        total_fee=intent.getStringExtra("total_fee");
        total_fee_double=intent.getStringExtra("total_fee_double");
        pay_type=intent.getStringExtra("pay_type");
        mipca_capture_price.setText("￥"+total_fee_double);
        mScanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        if(intent!=null){
            type=intent.getIntExtra("type",1);
            if(type==2){
                mipca_capture_title.setText(getString(R.string.str19));
                tv_explain.setText(getString(R.string.str20));
                mipca_capture_description.setVisibility(View.GONE);
                mipca_capture_price.setVisibility(View.GONE);
                cash_layout.setVisibility(View.GONE);
                paycode_layout.setVisibility(View.GONE);
            }else if(type==3){
                getOpenOrders_choose=intent.getParcelableArrayListExtra("getOpenOrders_choose");
                total_price=Double.parseDouble(intent.getStringExtra("total_price"));
                mipca_capture_price.setText("￥"+total_price);
                mipca_capture_title.setText(getString(R.string.str12));
                tv_explain.setText(getString(R.string.str14));
                mipca_capture_description.setVisibility(View.VISIBLE);
                mipca_capture_price.setVisibility(View.VISIBLE);
                cash_layout.setVisibility(View.VISIBLE);
                paycode_layout.setVisibility(View.VISIBLE);

              goodsList=new ArrayList<>();
                for(int i=0;i<getOpenOrders_choose.size();i++){
                    boolean ishas=true;
                    for(int j=0;j<getOpenOrders_choose.get(i).getGetOpenOrder_infos().size();j++){
                       goodsname= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getName();
                        goodsnum= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getNum();
                        goodsprice= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getPrice();
                        for(int z=0;z<goodsList.size();z++){
                            if(goodsList.get(z).getName().equals(goodsname)){
                                goodsList.get(z).setQuantity(goodsList.get(z).getQuantity()+1);
                                ishas=false;
                            }
                    }
                        if(ishas){
                            goodsList.add(new OrderGoods(Integer.parseInt(goodsnum),goodsname,Double.parseDouble(goodsprice)));
                        }
                    }
                }
            } else if (type==4){
                mipca_capture_title.setText(getString(R.string.str12));
                tv_explain.setText(getString(R.string.str14));
                mipca_capture_description.setVisibility(View.VISIBLE);
                mipca_capture_price.setVisibility(View.VISIBLE);
                cash_layout.setVisibility(View.GONE);
                paycode_layout.setVisibility(View.GONE);
            }else {
                mipca_capture_title.setText(getString(R.string.str12));
                tv_explain.setText(getString(R.string.str14));
                mipca_capture_description.setVisibility(View.VISIBLE);
                mipca_capture_price.setVisibility(View.VISIBLE);
                cash_layout.setVisibility(View.GONE);
                paycode_layout.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int setContentView() {
        return R.layout.activity_mipca_capture;
    }

    /**
     * Activity截获按键事件.发给ScanGunKeyEventHelper
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//            if(event.getKeyCode()!=KeyEvent.KEYCODE_BACK){
                mScanGunKeyEventHelper.analysisKeyEvent(event);
//            }
        return true;
    }

    /**
     * 扫码枪显示扫描内容
     * @param barcode 条形码
     */
    @Override
    public void onScanSuccess(String barcode) {
        //TODO 显示扫描内容
        if(type==2){
            Intent intent=new Intent();
            intent.putExtra("barcode",barcode);
            MipcaActivityCapture.this.setResult(206,intent);
            finish();
        }else if (type==4){
            surplus_pay(barcode,total_fee);
        }else {
            mobilepay(barcode);
        }
    }

    public void mobilepay(String barcode){
        Map<String,String> map= new HashMap<String,String>();
        if(type==3){
            ArrayList<Map<String,String>>  mapArrayList=new ArrayList<>();
            for(int i=0;i<getOpenOrders_choose.size();i++){
                Map<String,String> map1=new HashMap<>();
                map1.put("order_id",getOpenOrders_choose.get(i).getOrder_id());
                map1.put("price",getOpenOrders_choose.get(i).getPrice()+"");
                mapArrayList.add(map1);
            }
            map.put("map",mapArrayList.toString());
            JSONObject jsonObject=new JSONObject(map);
            try {
                String map_str=jsonObject.getString("map");
                jsonArray=new JSONArray(map_str);
                map.put("map",jsonArray+"");//转化为json数组的字符串
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("total_fee",(new Double(total_price*100)).intValue()+"");
        }else {
            map.put("total_fee",total_fee);
        }
        map.put("pay_type","micro");
        map.put("auth_code",barcode);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("common_pay"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=null;
                    if (!status.equals("200")) {
                        if(status.equals("E.404")){
                            DialogUtils.showbuilder(MipcaActivityCapture.this,message);
                        }else {
                            SysUtils.showError(message);
                        }
                    } else {
                        SysUtils.showSuccess(getString(R.string.str21));
//                            startTTS();
                        if (Build.VERSION.SDK_INT >= 23) {
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MipcaActivityCapture.this, Manifest.permission.RECORD_AUDIO);
                            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MipcaActivityCapture.this,new String[]{android.Manifest.permission.RECORD_AUDIO},222);
                                return;
                            }else{
                                if(type==3){
                                    tts.play(getString(R.string.str22)+total_price+getString(R.string.str23));
                                }else {
                                    tts.play(getString(R.string.str22)+total_fee_double+getString(R.string.str23));
                                }
                            }
                        } else {
                            if(type==3){
                                tts.play(getString(R.string.str22)+total_price+getString(R.string.str23));
                            }else {
                                tts.play(getString(R.string.str22)+total_fee_double+getString(R.string.str23));
                            }
                        }
//                        SysUtils.startAct(MipcaActivityCapture.this,new PaySuccessActivity());
                        data=ret.getJSONObject("data");
                        JSONObject dataonfo=data.getJSONObject("data");
                        String order_id=dataonfo.getString("order_id");
                        String sellername=dataonfo.getString("sellername");
                        String payed_time=dataonfo.getString("payed_time");
                        String pay_status=dataonfo.getString("pay_status");
                        if(type==3){
                            Intent intent = new Intent(MipcaActivityCapture.this, PayOpenOrderSuccessActivity.class);
                            intent.putExtra("type", 3);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("payed_time", payed_time);
                            intent.putExtra("pay_status", pay_status);
                            intent.putParcelableArrayListExtra("goodsList",goodsList);
                            intent.putExtra("total_price", total_price+"");
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(MipcaActivityCapture.this, MyScanCodeActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("total_fee_double", total_fee_double);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("sellername", sellername);
                            intent.putExtra("payed_time", payed_time);
                            intent.putExtra("pay_status", pay_status);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showNetworkError();
            }
        });
        RequestManager.addRequest(r, this);
        showLoading();
    }


    public void surplus_pay(String pay_code,String total_fee){
        HashMap<String,String> map=new HashMap<>();
        map.put("pay_code",pay_code);
        map.put("total_fee",total_fee);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("surplus_pay_app"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(response);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=null;
                    if (!status.equals("200")) {
                        if(status.equals("E.404")){
                            DialogUtils.showbuilder(MipcaActivityCapture.this,message);
                        }else {
                            SysUtils.showError(message);
                        }
                    } else {
                        SysUtils.showSuccess(getString(R.string.str21));
//                            startTTS();
                        if (Build.VERSION.SDK_INT >= 23) {
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MipcaActivityCapture.this, Manifest.permission.RECORD_AUDIO);
                            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MipcaActivityCapture.this,new String[]{android.Manifest.permission.RECORD_AUDIO},222);
                                return;
                            }else{
                                if(type==3){
                                    tts.play(getString(R.string.str22)+total_price+getString(R.string.str23));
                                }else {
                                    tts.play(getString(R.string.str22)+total_fee_double+getString(R.string.str23));
                                }
                            }
                        } else {
                            if(type==3){
                                tts.play(getString(R.string.str22)+total_price+getString(R.string.str23));
                            }else {
                                tts.play(getString(R.string.str22)+total_fee_double+getString(R.string.str23));
                            }
                        }
//                        SysUtils.startAct(MipcaActivityCapture.this,new PaySuccessActivity());
                        data=ret.getJSONObject("data");
                        JSONObject dataonfo=data.getJSONObject("data");
                        String order_id=dataonfo.getString("order_id");
                        String sellername=dataonfo.getString("sellername");
                        String payed_time=dataonfo.getString("payed_time");
                        String pay_status=dataonfo.getString("pay_status");
                        if(type==3){
                            Intent intent = new Intent(MipcaActivityCapture.this, PayOpenOrderSuccessActivity.class);
                            intent.putExtra("type", 3);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("payed_time", payed_time);
                            intent.putExtra("pay_status", pay_status);
                            intent.putParcelableArrayListExtra("goodsList",goodsList);
                            intent.putExtra("total_price", total_price+"");
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(MipcaActivityCapture.this, MyScanCodeActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("total_fee_double", total_fee_double);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("sellername", sellername);
                            intent.putExtra("payed_time", payed_time);
                            intent.putExtra("pay_status", pay_status);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();
            }
        });
        RequestManager.addRequest(r, this);
        showLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
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
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

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
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     * @param result
     * @param barcode
     */
    private JSONArray jsonArray;
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("resultString="+resultString)) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        }else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            bundle.putParcelable("bitmap", barcode);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            if(type==2){
                Intent intent=new Intent();
                intent.putExtra("barcode",resultString);
                MipcaActivityCapture.this.setResult(206,intent);
                finish();
            }else if (type==4){
                surplus_pay(resultString,total_fee);
            }else {
                mobilepay(resultString);
//                Map<String,String> map= new HashMap<String,String>();
//                if(type==3){
//                    ArrayList<Map<String,String>>  mapArrayList=new ArrayList<>();
//                    for(int i=0;i<getOpenOrders_choose.size();i++){
//                        Map<String,String> map1=new HashMap<>();
//                        map1.put("order_id",getOpenOrders_choose.get(i).getOrder_id());
//                        map1.put("price",getOpenOrders_choose.get(i).getPrice()+"");
//                        mapArrayList.add(map1);
//                    }
//                    map.put("map",mapArrayList.toString());
//                    JSONObject jsonObject=new JSONObject(map);
//                    try {
//                        String map_str=jsonObject.getString("map");
//                        jsonArray=new JSONArray(map_str);
//                        map.put("map",jsonArray+"");//转化为json数组的字符串
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    map.put("total_fee",(new Double(total_price*100)).intValue()+"");
//                }else {
//                    map.put("total_fee",total_fee);
//                }
//            map.put("pay_type","micro");
//            map.put("auth_code",resultString);
//                System.out.println("map=="+map);
//            CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("common_pay"), map, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    hideLoading();
//
//                    try {
//                        JSONObject ret = SysUtils.didResponse(jsonObject);
//                        System.out.println("ret="+ret);
//                        String status = ret.getString("status");
//                        String message = ret.getString("message");
//                        JSONObject data=null;
//
//                        if (!status.equals("200")) {
//                            if(status.equals("E.404")){
//                                DialogUtils.showbuilder(MipcaActivityCapture.this,message);
//                            }else {
//                                SysUtils.showError(message);
//                            }
//
//                        } else {
//                            SysUtils.showSuccess("扫描收款成功！");
////                            startTTS();
//                            if (Build.VERSION.SDK_INT >= 23) {
//                                int checkCallPhonePermission = ContextCompat.checkSelfPermission(MipcaActivityCapture.this, Manifest.permission.RECORD_AUDIO);
//                                if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                                    ActivityCompat.requestPermissions(MipcaActivityCapture.this,new String[]{android.Manifest.permission.RECORD_AUDIO},222);
//                                    return;
//                                }else{
//                                    if(type==3){
//                                        tts.play("收款成功"+total_price+"元");
//                                    }else {
//                                        tts.play("收款成功"+total_fee_double+"元");
//                                    }
//                                }
//                            } else {
//                                if(type==3){
//                                    tts.play("收款成功"+total_price+"元");
//                                }else {
//                                    tts.play("收款成功"+total_fee_double+"元");
//                                }
//                            }
////                            SysUtils.startAct(MipcaActivityCapture.this,new PaySuccessActivity());
////                            finish();
//                            data=ret.getJSONObject("data");
//                            JSONObject dataonfo=data.getJSONObject("data");
//                            String order_id=dataonfo.getString("order_id");
//                            String sellername=dataonfo.getString("sellername");
//                            String payed_time=dataonfo.getString("payed_time");
//                            String pay_status=dataonfo.getString("pay_status");
//                            if(type==3){
//                                Intent intent = new Intent(MipcaActivityCapture.this, PayOpenOrderSuccessActivity.class);
//                                intent.putExtra("type", 3);
//                                intent.putExtra("order_id", order_id);
//                                intent.putExtra("payed_time", payed_time);
//                                intent.putExtra("pay_status", pay_status);
//                                intent.putParcelableArrayListExtra("goodsList",goodsList);
//                                intent.putExtra("total_price", total_price+"");
//                                startActivity(intent);
//                                finish();
//                            }else {
//                                Intent intent = new Intent(MipcaActivityCapture.this, MyScanCodeActivity.class);
//                                intent.putExtra("type", 2);
//                                intent.putExtra("total_fee_double", total_fee_double);
//                                intent.putExtra("order_id", order_id);
//                                intent.putExtra("sellername", sellername);
//                                intent.putExtra("payed_time", payed_time);
//                                intent.putExtra("pay_status", pay_status);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    hideLoading();
//                    SysUtils.showNetworkError();
//                }
//            });
//
//            executeRequest(r);
//            showLoading(MipcaActivityCapture.this);
        }
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
            handler = new CaptureActivityHandler(this, decodeFormats,
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

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mipca_capture_back:
                finish();
                break;
            case R.id.mipca_capture_description:
                SysUtils.startAct(MipcaActivityCapture.this,new PayDescriptionActivity());
                break;
            case R.id.cash_layout:
                Intent intent=new Intent(MipcaActivityCapture.this,CashChargeActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                intent.putExtra("total_price",total_price+"");
                startActivity(intent);
                finish();
                break;
            case R.id.paycode_layout:
                paycode();
                break;
            case R.id.tv_keyboard:
                SoftKeyBoard softKeyBoard=new SoftKeyBoard(this);
                softKeyBoard.setEdit(tv_keyboard);
                softKeyBoard.show();
                break;
        }
    }
    /**
     * 选择二维码支付
     */
    private JSONArray paycode_jsonArray;
    private void paycode(){
        Map<String,String> map= new HashMap<String,String>();
        ArrayList<Map<String,String>>  mapArrayList=new ArrayList<>();
        for(int i=0;i<getOpenOrders_choose.size();i++){
            Map<String,String> map1=new HashMap<>();
            map1.put("order_id",getOpenOrders_choose.get(i).getOrder_id());
            map1.put("price",getOpenOrders_choose.get(i).getPrice()+"");
            mapArrayList.add(map1);
            map.put("map",mapArrayList.toString());
            JSONObject jsonObject=new JSONObject(map);
            try {
                String map_str=jsonObject.getString("map");
                paycode_jsonArray=new JSONArray(map_str);
                map.put("map",paycode_jsonArray+"");//转化为json数组的字符串
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.put("total_fee",(new Double(total_price*100)).intValue()+"");
        map.put("pay_type","wxpayjsapi");
        map.put("auth_code","code");
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("common_pay"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=null;
                    if (!status.equals("200")) {
                        if(status.equals("E.404")){
                            DialogUtils.showbuilder(MipcaActivityCapture.this,message);
                        }else {
                            SysUtils.showError(message);
                        }
                    } else {
                        data=ret.getJSONObject("data");
                        String order_id=data.getString("order_id");
                        String code_url=data.getString("url");

                        service=new PreferencesService(MipcaActivityCapture.this);
                        service.save_order_id(order_id);

                        Intent intent=new Intent(MipcaActivityCapture.this,OpenOrderPayCodeActivity.class);
                        intent.putExtra("code_url",code_url);
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                        intent.putExtra("total_price",total_price+"");
                        startActivity(intent);
                        finish();

                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showNetworkError();
            }
        });

        RequestManager.addRequest(r, this);
        showLoading();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //获取支付的数据
    @Override
    public void afterTextChanged(Editable editable) {
        if (type==4){
            if (editable.toString().length()==11||editable.toString().length()==15){
                surplus_pay(editable.toString(),total_fee);
            }
        }else {

        }
    }
}
