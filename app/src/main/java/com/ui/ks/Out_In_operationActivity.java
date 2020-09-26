package com.ui.ks;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication.KsApplication;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
import com.constant.RouterPath;
import com.google.gson.Gson;
import com.library.utils.BigDecimalArith;
import com.ui.adapter.Out_In_Adapter;
import com.ui.adapter.Remark_Adapter;
import com.ui.entity.Goods_Common_Notes;
import com.ui.entity.Out_in_Goods;
import com.ui.util.CustomRequest;
import com.ui.util.ScanGunKeyEventHelper;
import com.ui.util.SpeechUtilOffline;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.ui.view.MyOut_INlistView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 出入库
 * Created by admin on 2018/7/10.
 */
@Route(path = RouterPath.ACTIVITY_OUT_IN_OPERATION)
public class Out_In_operationActivity extends BaseActivity implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener, Out_In_Adapter.Setonclick, Out_In_Adapter.SetLongOnclick {

    private static final String TAG = Out_In_operationActivity.class.getSimpleName();
    @BindView(R.id.iv_scan)
    ImageView ivScan;

    public TextView tv_report;
    public ImageView iv_cell, iv_back;
    public EditText et_inputgoodname;
    public List<Out_in_Goods> out_in_goodsList;
    public MyOut_INlistView My_ListView;
    public Out_In_Adapter adapter;
    public List<Out_in_Goods> adats;
    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    public ListView lc_outin;
    public Button btn_out, btn_in;
    public List<Map<String, String>> listmap;
    public List<Goods_Common_Notes> goods_common_notesList;

    private SpeechUtilOffline tts;
    private boolean isseek = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_int_operationactivity);
        ButterKnife.bind(this);

        initToolbar(this);

        initView();

    }

    /**
     * @Description:请求权限
     * @Author:lyf
     * @Date: 2020/8/26
     */
    public void requestPermission() {
        PermissionGen.with(this)
                .addRequestCode(200)
                .permissions(
                        android.Manifest.permission.CAMERA
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 200)
    public void permissionSuccess() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_SCAN_HANDER).navigation(this, 200);

    }

    @PermissionFail(requestCode = 200)
    public void permissionFailure() {
        Toast.makeText(this, getString(R.string.str145), Toast.LENGTH_LONG).show();
    }

    private void initView() {

        //初始化语言信息
        tts = new SpeechUtilOffline(this);

        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);

        out_in_goodsList = new ArrayList<>();
        adats = new ArrayList<>();
        listmap = new ArrayList<>();
        goods_common_notesList = new ArrayList<>();


        lc_outin = (ListView) findViewById(R.id.lc_outin);

        btn_out = (Button) findViewById(R.id.btn_out);
        btn_out.setOnClickListener(this);
        btn_in = (Button) findViewById(R.id.btn_in);
        btn_in.setOnClickListener(this);
        tv_report = (TextView) findViewById(R.id.tv_report);
        tv_report.setOnClickListener(this);
        iv_cell = (ImageView) findViewById(R.id.iv_cell);
        iv_cell.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        et_inputgoodname = (EditText) findViewById(R.id.et_inputgoodname);
        et_inputgoodname.clearFocus();
        et_inputgoodname.setSelected(false);
        et_inputgoodname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    iv_cell.setVisibility(View.VISIBLE);
                } else {
                    iv_cell.setVisibility(View.GONE);
                }
            }
        });

        et_inputgoodname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    getseek(view.toString());
                }
                return false;
            }
        });

        watchSearch();

    }

    @OnClick({R.id.iv_scan})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                requestPermission();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_report://出入库报表
                Intent intent = new Intent(Out_In_operationActivity.this, OutofstorageActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_cell:
                et_inputgoodname.setText("");
                iv_cell.setVisibility(View.GONE);
                break;
            case R.id.iv_back:
                if (adats.size() == 0) {
                    this.finish();
                } else {
                    //还有商品没处理
                    Toast.makeText(Out_In_operationActivity.this, getString(R.string.str341), Toast.LENGTH_SHORT).show();
                }
                break;
            //出库按钮
            case R.id.btn_out:
                if (adats.size() > 0) {
                    getCommonNotesInfo("1");
                } else {
                    //没有出库商品
                    Toast.makeText(Out_In_operationActivity.this, getString(R.string.str342), Toast.LENGTH_SHORT).show();
                }
                break;
            //入库按钮
            case R.id.btn_in:
                if (adats.size() > 0) {
                    getCommonNotesInfo("0");
                } else {
                    //没有入库商品
                    Toast.makeText(Out_In_operationActivity.this, getString(R.string.str343), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //出库
    public void Updatas(final String remark, final String type) {
        double money = 0;
        double nums = 0;
        listmap.clear();
        for (int i = 0; i < adats.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", adats.get(i).getName());
            map.put("nums", "" + adats.get(i).getNums());
            nums = BigDecimalArith.add(nums, Double.parseDouble(adats.get(i).getNums()));
            map.put("cost", adats.get(i).getCost() + "");
            map.put("id", adats.get(i).getGoods_id());
            map.put("money", BigDecimalArith.mul(Double.parseDouble(adats.get(i).getNums() + ""), Double.parseDouble(adats.get(i).getCost() + "")) + "");
            money = BigDecimalArith.add(money, BigDecimalArith.mul(Double.parseDouble(adats.get(i).getNums() + ""), Double.parseDouble(adats.get(i).getCost() + "")));
            map.put("type", type);
            listmap.add(map);
        }

        Gson gson = new Gson();
        String json = gson.toJson(listmap);
        Map<String, String> map = new HashMap<>();
        map.put("goods_map", json);
        map.put("type", type);
        map.put("money", money + "");
        map.put("nums", nums + "");
        map.put("remark", remark);
        map.put("oparator", KsApplication.getString("login_username", ""));

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("store_detail"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    Log.e("测试数据", "onResponse: " + ret);
                    String status = ret.getString("status");
//                    ShowDialog();
                    if (status.equals("200")) {
                        adats.clear();
                        adapter.notifyDataSetChanged();
                        if (type.equals("0")) {
                            //入库成功
                            Toast.makeText(Out_In_operationActivity.this, getString(R.string.str344), Toast.LENGTH_SHORT).show();
                        } else {
                            //出库成功
                            Toast.makeText(Out_In_operationActivity.this, getString(R.string.str345), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (type.equals("0")) {
                            //入库失败
                            Toast.makeText(Out_In_operationActivity.this, getString(R.string.str346), Toast.LENGTH_SHORT).show();
                        } else {
                            //出库失败
                            Toast.makeText(Out_In_operationActivity.this, getString(R.string.str347), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        executeRequest(r);
    }


    /**
     * @方法说明:监控软键盘的的搜索按钮
     * @方法名称:watchSearch
     * @返回值:void
     */
    public void watchSearch() {
        et_inputgoodname.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_inputgoodname.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
//                    seachList(viewIndex);//这里是我要做的操作！

                    getSeek(v.getText().toString());

                    et_inputgoodname.setText("");

                    return true;
                }
                return false;
            }
        });
    }

    public void getSeek(String str) {
        String name = "search";
        if (StringUtils.isNumber1(str)) {
            name = "bncode";
        } else {
            name = "search";
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put(name, str);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodspinbanServiceUrl("goods_search"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    Log.e("测试数据", "onResponse: " + ret);
                    String status = ret.getString("status");
                    if (status.equals("200")) {
                        out_in_goodsList.clear();
                        JSONObject data = ret.getJSONObject("data");
                        JSONArray goods_info = data.getJSONArray("goods_info");
                        for (int i = 0; i < goods_info.length(); i++) {
                            JSONObject jso = goods_info.getJSONObject(i);
                            Out_in_Goods out_in_goods = new Out_in_Goods();
                            out_in_goods.setCost(jso.getString("cost"));
                            out_in_goods.setGoods_id(jso.getString("goods_id"));
                            out_in_goods.setMember_price(jso.getString("member_price"));
                            out_in_goods.setName(jso.getString("name"));
                            out_in_goods.setPrice(jso.getString("price"));
                            out_in_goods.setNums("1");
                            out_in_goodsList.add(out_in_goods);
                        }
                    }
                    ShowDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        executeRequest(r);
    }


    public void getseek(String str) {
        Map<String, String> map = new HashMap<String, String>();
        String name = "search";
        if (StringUtils.isNumber1(str)) {
            name = "bncode";
        } else {
            name = "search";
        }
        map.put(name, str);
        showLoading(this);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodspinbanServiceUrl("goods_search"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    LogUtils.i(TAG + " onResponse: " + ret);
                    String status = ret.getString("status");
                    if (status.equals("200")) {
                        isseek = false;
                        out_in_goodsList.clear();
                        JSONObject data = ret.getJSONObject("data");
                        JSONArray goods_info = data.getJSONArray("goods_info");
                        for (int i = 0; i < goods_info.length(); i++) {
                            JSONObject jso = goods_info.getJSONObject(i);
                            Out_in_Goods out_in_goods = new Out_in_Goods();
                            out_in_goods.setCost(jso.getString("cost"));
                            out_in_goods.setGoods_id(jso.getString("goods_id"));
                            out_in_goods.setMember_price(jso.getString("member_price"));
                            out_in_goods.setName(jso.getString("name"));
                            out_in_goods.setPrice(jso.getString("price"));
                            out_in_goods.setNums("1");
                            int in = 0;
                            aa:
                            for (int j = 0; j < adats.size(); j++) {
                                if (adats.get(j).getGoods_id().equals(out_in_goods.getGoods_id())) {
                                    in = in + (j + 1);
                                    break aa;
                                }
                            }
                            if (in == 0) {
                                adats.add(0, out_in_goods);
                                tts.play(getString(R.string.str348));//扫码成功
                            } else {
                                float j = Float.parseFloat(adats.get(in - 1).getNums());
                                j++;
                                adats.get(in - 1).setNums(j + "");
//                                tts.play(getString(R.string.str118)+j);
                                tts.play(j + "");
                            }
                        }
                        adapter = new Out_In_Adapter(Out_In_operationActivity.this, adats);
                        adapter.Setitmeonclcik(Out_In_operationActivity.this);
                        adapter.SetitmeLongOnclick(Out_In_operationActivity.this);
                        lc_outin.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    LogUtils.i(TAG + " e: " + e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG + " e: " + error.toString());
                isseek = false;
                hideLoading();
            }
        });
        executeRequest(r);
    }

    Dialog dialog = null;

    //搜索商品的弹窗
    public void ShowDialog() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(Out_In_operationActivity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Out_In_operationActivity.this, R.layout.remarks, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.addContentView(rootView, params);
//        final Dialog dialog = new Dialog(Out_In_operationActivity.this);
//        dialog.setTitle("出库备注");
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setContentView(R.layout.remarks);
        My_ListView = (MyOut_INlistView) rootView.findViewById(R.id.My_ListView);
        Out_In_Adapter out_in_adapter = new Out_In_Adapter(Out_In_operationActivity.this, out_in_goodsList);
        My_ListView.setAdapter(out_in_adapter);

        out_in_adapter.Setitmeonclcik(new Out_In_Adapter.Setonclick() {
            @Override
            public void Onitmeclick(int i) {
                int in = 0;
                aa:
                for (int j = 0; j < adats.size(); j++) {
                    if (adats.get(j).getGoods_id().equals(out_in_goodsList.get(i).getGoods_id())) {
                        in = in + (j + 1);
                        break aa;
                    }
                }
                if (in == 0) {
                    adats.add(0, out_in_goodsList.get(i));
                    tts.play(getString(R.string.str348));//扫码成功
                } else {
                    float j = Float.parseFloat(adats.get(in - 1).getNums());
                    j++;
                    adats.get(in - 1).setNums(j + "");
                    tts.play(getString(R.string.str118) + j);
                }
                adapter = new Out_In_Adapter(Out_In_operationActivity.this, adats);
                adapter.Setitmeonclcik(Out_In_operationActivity.this);
                adapter.SetitmeLongOnclick(Out_In_operationActivity.this);
                lc_outin.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
    }

    //备注信息的弹窗
    public void ShowremarkDialog(final String type) {
        final Dialog dialog = new Dialog(Out_In_operationActivity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Out_In_operationActivity.this, R.layout.remark_dialog, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        window.addContentView(rootView, params);

        Button btn_ubmission = (Button) rootView.findViewById(R.id.btn_ubmission);
        final EditText et_inputgoodname1 = (EditText) rootView.findViewById(R.id.et_inputgoodname1);
        final EditText et_inputgoodname2 = (EditText) rootView.findViewById(R.id.et_inputgoodname2);
        MyOut_INlistView MyListView = (MyOut_INlistView) rootView.findViewById(R.id.My_ListView);
        Remark_Adapter out_in_adapter = new Remark_Adapter(Out_In_operationActivity.this, goods_common_notesList);
        MyListView.setAdapter(out_in_adapter);

        btn_ubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!et_inputgoodname1.getText().toString().equals("") && !et_inputgoodname2.getText().toString().equals("")) {
                Updatas(et_inputgoodname1.getText().toString() + "," + et_inputgoodname2.getText().toString(), type);
                dialog.dismiss();
//                }
            }
        });

        out_in_adapter.Setitmeonclcik(new Remark_Adapter.Setonclick() {
            @Override
            public void Onitmeclick(int i) {

                String spStr[] = goods_common_notesList.get(i).getNotes().split(",");
                if (spStr.length > 1) {
                    et_inputgoodname1.setText(spStr[0]);
                    et_inputgoodname2.setText(spStr[1].replace(",", ""));
                }
            }
        });
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
        scanGunKeyEventHelper.analysisKeyEvent(event);
//            }
        return true;
    }

    @Override
    public void onScanSuccess(String barcode) {
        LogUtils.i(TAG + " barcode: " + barcode);
        if (!isseek) {
            isseek = true;
            getseek(barcode);
        }

    }


    //获取常用备注信息
    private void getCommonNotesInfo(final String type) {

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("remarks_list"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    Log.e("测试数据111", "onResponse: " + ret);
                    String status = ret.getString("status");
                    goods_common_notesList.clear();
                    if (status.equals("200")) {
                        JSONArray ja = ret.getJSONArray("data");
                        for (int i = 0; i < ja.length(); i++) {
                            Goods_Common_Notes goods_common_notes = new Goods_Common_Notes();
                            JSONObject json = ja.getJSONObject(i);
                            String id = json.getString("id");
                            String notes = json.getString("notes");
                            goods_common_notes.setId(id);
                            goods_common_notes.setNotes(notes);
                            goods_common_notesList.add(goods_common_notes);
                        }
                    }

                    ShowremarkDialog(type);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        executeRequest(r);

    }

    //
    @Override
    public void Onitmeclick(int i) {
        ShowDialogedit(i);
    }


    //编辑商品数量的弹窗
    public void ShowDialogedit(final int i) {
        final Dialog dialog = new Dialog(Out_In_operationActivity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Out_In_operationActivity.this, R.layout.itme_delite, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        window.addContentView(rootView, params);
        TextView tv_remark = (TextView) rootView.findViewById(R.id.tv_remark);
        TextView tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_name.setText(adats.get(i).getName());
        tv_remark.setVisibility(View.GONE);
        final EditText et_nums = (EditText) rootView.findViewById(R.id.et_nums);
        et_nums.setVisibility(View.VISIBLE);
        Button btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btn_ubmission = (Button) rootView.findViewById(R.id.btn_ubmission);
        btn_ubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (StringUtils.isNumber1(et_nums.getText().toString())) {
                    adats.get(i).setNums(et_nums.getText().toString());
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void LongitmeClick(int i) {
        ShowDialogdelete(i);
    }

    //删除商品的弹窗
    public void ShowDialogdelete(final int i) {
        final Dialog dialog = new Dialog(Out_In_operationActivity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Out_In_operationActivity.this, R.layout.itme_delite, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        window.addContentView(rootView, params);
        Button btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        TextView tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_name.setText(adats.get(i).getName());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btn_ubmission = (Button) rootView.findViewById(R.id.btn_ubmission);
        btn_ubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adats.remove(i);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果处理
        if (requestCode == 200 && resultCode == 200) {
            String resul = data.getExtras().getString("result");
            LogUtils.i(TAG + " resul:  " + resul);
            getseek(resul);
        }
    }
}
