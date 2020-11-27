package com.ui.ks;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.global.Global;
import com.ui.util.Code;
import com.ui.util.CustomRequest;
import com.ui.util.DeleteEditText;
import com.ui.util.LoginUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.ui.view.avtivity.ShopActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    TextView textView1, textView4;
    EditText textView3, textView6;
    LinearLayout linearlayout01, linearlayout02;
    private boolean is_eye_open = false;
    ImageView textView7;
    EditText textView9;
    private int fkType = 0;
    CheckBox cb_left, cb_right,cb_mainstore;
    private Spinner login_spinner;
    private  String[] type_list=new String[4];
    private ArrayAdapter<String> adapter;
    private RelativeLayout main;
    long[] mHits = new long[3];//点击3下
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=100;
    private TextView tv_Agreement;


    private void okModifyEnv(int env) {
        KsApplication.putInt("isTest", env);

        //退出登录
        LoginUtils.logout(this, 0);

        if(env == 1) {
            SysUtils.showSuccess(getString(R.string.str130));
        } else {
            SysUtils.showSuccess(getString(R.string.str131));
        }
    }


    private void setAgreement(TextView tv){
        SpannableStringBuilder style = new SpannableStringBuilder();

        //设置文字
        style.append(getString(R.string.str132));

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.kancloud.cn/yzysy/yzysy/1565261");
                intent.setData(content_url);
                startActivity(intent);
            }
        };
        style.setSpan(clickableSpan, 15, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        style.setSpan(foregroundColorSpan, 15, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //配置给TextView
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type_list[0]=getString(R.string.str133);
        type_list[1]=getString(R.string.str134);
        type_list[2]=getString(R.string.str135);
        type_list[3]=getString(R.string.str136);
        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        requestPermission();

        tv_Agreement = (TextView) findViewById(R.id.tv_Agreement);

        setAgreement(tv_Agreement);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView4 = (TextView) findViewById(R.id.textView4);
        login_spinner = (Spinner) findViewById(R.id.login_spinner);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        login_spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        login_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(type_list[position].equals(getString(R.string.str133))) {
                   fkType=1;
               }else if(type_list[position].equals(getString(R.string.str134))){
                   fkType=3;

               } else if(type_list[position].equals(getString(R.string.str135))){
                   fkType=4;

               } else if(type_list[position].equals(getString(R.string.str136))){
                   fkType=2;

               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textView3 = (EditText) findViewById(R.id.textView3);    //账号
        new DeleteEditText(textView3, textView1);
        if(!StringUtils.isEmpty(KsApplication.getString("login_username", ""))) {
            textView3.setText(KsApplication.getString("login_username", ""));
        }

        //密码
        textView6 = (EditText) findViewById(R.id.textView6);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_eye_open) {
                    textView6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close, 0);

                    is_eye_open = false;
                } else {
                    textView6.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open, 0);

                    is_eye_open = true;
                }
                textView6.setSelection(textView6.getText().length());
            }
        });


        //验证码
        textView7  =(ImageView) findViewById(R.id.textView7);
        textView7.setImageBitmap(Code.getInstance().createBitmap());
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView7.setImageBitmap(Code.getInstance().createBitmap());
            }
        });
        textView9 = (EditText) findViewById(R.id.textView9);


        cb_left = (CheckBox) findViewById(R.id.cb_left);
        cb_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFkType(2);
            }
        });
        cb_right = (CheckBox) findViewById(R.id.cb_right);
        cb_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFkType(1);
            }
        });
        cb_mainstore = (CheckBox) findViewById(R.id.cb_mainstore);
        cb_mainstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFkType(3);
            }
        });

        setFkType(fkType);

        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = textView3.getText().toString();

                if(StringUtils.isEmpty(username)) {
                    SysUtils.showError(getString(R.string.str137));
                } else {
                    final String password = textView6.getText().toString();

                    if(StringUtils.isEmpty(password)) {
                        SysUtils.showError(getString(R.string.str138));
                    } else {
                        String captcha = textView9.getText().toString();

                        if(StringUtils.isEmpty(captcha)) {
                            SysUtils.showError(getString(R.string.str139));
                        } else {
                            String realCode = Code.getInstance().getCode();
                            if(!captcha.equalsIgnoreCase(realCode)) {
                                textView7.setImageBitmap(Code.getInstance().createBitmap());
                                SysUtils.showError(getString(R.string.str140));//验证码不正确
                            } else {
                                if (fkType != 1 && fkType != 2 && fkType != 3 && fkType != 4) {
                                    SysUtils.showError(getString(R.string.str141));//请选择登录类型
                                } else {
                                    Map<String,String> map = new HashMap<String,String>();
                                    map.put("key", Global.LOGIN_KEY);

                                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sign"), map, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {
                                            try {
                                                JSONObject ret = SysUtils.didResponse(jsonObject);
                                                String status = ret.getString("status");
                                                String message = ret.getString("message");
                                                String signs = ret.getString("data");

                                                if (!status.equals("200")) {
                                                    hideLoading();
                                                    SysUtils.showError(message);
                                                } else {
                                                    doLogin(username, password, signs);
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

                                    executeRequest(r);

                                    showLoading(LoginActivity.this, getString(R.string.str142));//正在登录...
                                }

                            }

                        }
                    }
                }
            }
        });

        //忘记密码
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(LoginActivity.this, new ForgetPasswordActivity());
                finish();
            }
        });

        TextView textView12= (TextView) findViewById(R.id.textView12);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("http://yizhongyun.mikecrm.com/knb2uik");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.main );
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("print","打印环境切换的点击事件");
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    int isTest = KsApplication.getInt("isTest", 0);
                    new MaterialDialog.Builder(LoginActivity.this)
                            .title(getString(R.string.str114))
                            .items(R.array.env_list)
                            .theme(SysUtils.getDialogTheme())
                            .itemsCallbackSingleChoice(isTest, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    okModifyEnv(which);
                                    return true;
                                }
                            })
                            .positiveText(getString(R.string.sure))
                            .negativeText(getString(R.string.cancel))
                            .show();
                }
            }
        });
    }

    private void doLogin(final String username, String password, String sign) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", username);
        map.put("pwd", password);
        map.put("signs", sign);
        map.put("type", String.valueOf(fkType));

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getServiceUrl("log"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    System.out.println("登录ret:"+ret);
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        SysUtils.showSuccess(getString(R.string.str143));//登录成功
                        textView6.setText("");
                        textView9.setText("");
                        KsApplication.putString("login_username", username);
                        JSONObject dataObject = ret.getJSONObject("data");
                        LoginUtils.afterLogin(LoginActivity.this, dataObject, false, fkType);
                        toAct();
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
        executeRequest(r);
    }

    private void setFkType(int type) {
        if(type == 1) {
            cb_left.setChecked(false);
            cb_mainstore.setChecked(false);
            cb_right.setChecked(true);
        } else if (type == 2){
            cb_left.setChecked(true);
            cb_right.setChecked(false);
            cb_mainstore.setChecked(false);
        }  else if (type == 3) {
            cb_left.setChecked(false);
            cb_right.setChecked(false);
            cb_mainstore.setChecked(true);
        }

        this.fkType = type;
    }

    //接受广播，更新ui
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //注册成功了，销毁这个界面
//            finish();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_REGISTER_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    private void toAct() {
        if (LoginUtils.isSeller()) {
            //店铺
            SysUtils.startAct(LoginActivity.this, new ShopActivity());
        } else if (LoginUtils.isMember()) {
            //业务员
            SysUtils.startAct(LoginActivity.this, new ReportActivity());
        }else if (LoginUtils.isMainStore()) {
            //总店
            SysUtils.startAct(LoginActivity.this, new MainStoreActivity());
        }else if (LoginUtils.isShopper()) {
            //营业员
            SysUtils.startAct(LoginActivity.this, new ShopActivity());
        }
    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.str144))//警告！
                .setMessage(getString(R.string.str145))//请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }

}
