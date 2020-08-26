package com.ui.ks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ui.entity.GoodSort;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.GetImagePath;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.ui.util.UploadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ui.util.GetImagePath.getDataColumn;
import static com.ui.util.GetImagePath.isDownloadsDocument;
import static com.ui.util.GetImagePath.isExternalStorageDocument;
import static com.ui.util.GetImagePath.isMediaDocument;

public class AddGoodsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_member_price01)
    TextView tvMemberPrice01;
    @BindView(R.id.ed_member_price01)
    EditText edMemberPrice01;
    @BindView(R.id.tv_member_price02)
    TextView tvMemberPrice02;
    @BindView(R.id.ed_member_price02)
    EditText edMemberPrice02;
    @BindView(R.id.tv_member_price03)
    TextView tvMemberPrice03;
    @BindView(R.id.ed_member_price03)
    EditText edMemberPrice03;
    @BindView(R.id.tv_member_price04)
    TextView tvMemberPrice04;
    @BindView(R.id.ed_member_price04)
    EditText edMemberPrice04;
    @BindView(R.id.tv_member_price05)
    TextView tvMemberPrice05;
    @BindView(R.id.ed_member_price05)
    EditText edMemberPrice05;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.layout_other_member)
    LinearLayout layoutOtherMember;
    @BindView(R.id.iv_more_member)
    ImageView ivMoreMember;
    private Switch btn_switch, good_switch_up, good_group_buying, take_out_food, table_number;
    private EditText et_goodname, et_goodprice, et_goodcode, et_good_costprice, et_good_grossprice,
            et_good_stocknum, et_good_stock, et_good_remark, ed_market_value;
    private ImageView iv_scangoodcode, iv_goodpicture, iv_back;
    private Button btn_scangoodcode, btn_save, btn_edit_cell, btn_edit_save, btn_cell, btn_sure, btn_add_store;
    private TextView tv_good_type, tv_title_name, tv_message, tv_py_code;
    private LinearLayout good_stocknum_layout, good_edit_bottombtn_layout;
    private RelativeLayout good_stock_layout, layout_goodscode, layout_good_type;
    private int btn_switch_type = 1;//1无码商品添加，2有码商品添加
    private int good_switch_type = 1;//0商品下架，1商品上架
    private SelectPicPopupWindow mSelectPicPopupWindow;
    private int GALLERY_REQUSET_CODE_KITKAT = 204;
    private int INTENT_BTN_PICK_PHOTO = 200;
    private int INTENT_BTN_TAKE_PHOTO = 201;
    private int PHOTO_CROP_CODE = 203;//图片裁剪
    private int INTENT_GOODS_SORT = 202;
    private String mFilePath;
    private Uri uri;
    private Uri photoUri;
    //    private final String requrl=SysUtils.getUploadImageServiceUrl("fileup");
    private final String requrl = SysUtils.getnewsellerUrl("Helper/uploadImg");
    private File myCaptureFile = null;
    private String good_trpe_result;
    private String good_trpe_result_id;
    private String image_id = "";
    private int type;
    private Uri pick_photo;
    private Bitmap bit = null;
    private int isTakePhoto = 1;//1表示从相册获取图片，2表示拍照获取
    private String goods_id = "";//商品id
    private String product_id = "";//产品id，编辑删除后台需要
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.book)
            .showImageForEmptyUri(R.drawable.book)
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new RoundedBitmapDisplayer(20))
            .build();
    private TextView tv_quantity_increase;
    private TextView et_good_describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        SysUtils.setupUI(this, findViewById(R.id.activity_add_goods));
        ButterKnife.bind(this);
        initToolbar(this);
//        setToolbarTitle("无码商品添加");
        initView();
        tv_title_name.setText(getString(R.string.btn_addgood));
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", 0);
            goods_id = intent.getStringExtra("goods_id");
            if (type == 2 || type == 3) {
                tv_title_name.setText(getString(R.string.str107));
                good_edit_bottombtn_layout.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_add_store.setVisibility(View.VISIBLE);
                getGoodsDeatil();
            } else {
                good_edit_bottombtn_layout.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                btn_add_store.setVisibility(View.GONE);
                getSortlist();
            }
        }
    }

    private void initView() {
        tvMemberPrice01.setText(getString(R.string.str413) + "1");
        tvMemberPrice02.setText(getString(R.string.str413) + "2");
        tvMemberPrice03.setText(getString(R.string.str413) + "3");
        tvMemberPrice04.setText(getString(R.string.str413) + "4");
        tvMemberPrice05.setText(getString(R.string.str413) + "5");
        btn_switch = (Switch) findViewById(R.id.btn_switch);
        good_switch_up = (Switch) findViewById(R.id.good_switch_up);
        good_group_buying = (Switch) findViewById(R.id.good_group_buying);
        take_out_food = (Switch) findViewById(R.id.take_out_food);
        table_number = (Switch) findViewById(R.id.table_number);
        et_goodname = (EditText) findViewById(R.id.et_goodname);
        et_goodprice = (EditText) findViewById(R.id.et_goodprice);
        et_goodcode = (EditText) findViewById(R.id.et_goodcode);
        et_good_costprice = (EditText) findViewById(R.id.et_good_costprice);
        et_good_grossprice = (EditText) findViewById(R.id.et_good_grossprice);
        et_good_stocknum = (EditText) findViewById(R.id.et_good_stocknum);
        et_good_stock = (EditText) findViewById(R.id.et_good_stock);
        et_good_remark = (EditText) findViewById(R.id.et_good_remark);
        ed_market_value = (EditText) findViewById(R.id.ed_market_value);
        iv_scangoodcode = (ImageView) findViewById(R.id.iv_scangoodcode);
        iv_goodpicture = (ImageView) findViewById(R.id.iv_goodpicture);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_scangoodcode = (Button) findViewById(R.id.btn_scangoodcode);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_edit_cell = (Button) findViewById(R.id.btn_edit_cell);
        btn_edit_save = (Button) findViewById(R.id.btn_edit_save);
        btn_add_store = (Button) findViewById(R.id.btn_add_store);
        tv_good_type = (TextView) findViewById(R.id.tv_good_type);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        layout_goodscode = (RelativeLayout) findViewById(R.id.layout_goodscode);
        good_stock_layout = (RelativeLayout) findViewById(R.id.good_stock_layout);
        good_stocknum_layout = (LinearLayout) findViewById(R.id.good_stocknum_layout);
        layout_good_type = (RelativeLayout) findViewById(R.id.layout_good_type);
        good_edit_bottombtn_layout = (LinearLayout) findViewById(R.id.good_edit_bottombtn_layout);

        et_good_describe = (TextView) findViewById(R.id.et_good_describe);
        tv_py_code = (TextView) findViewById(R.id.tv_py_code);

//        et_goodcode.setInputType(InputType.TYPE_NULL);//设置输入框不能点击
        SetEditTextInput.judgeNumber(et_good_grossprice);
        SetEditTextInput.judgeNumber(et_good_costprice);
        SetEditTextInput.judgeNumber(et_goodprice);
        SetEditTextInput.judgeNumber(ed_market_value);
        btn_switch.setOnCheckedChangeListener(this);
        good_switch_up.setOnCheckedChangeListener(this);
        good_group_buying.setOnCheckedChangeListener(this);
        take_out_food.setOnCheckedChangeListener(this);
        table_number.setOnCheckedChangeListener(this);
        iv_goodpicture.setOnClickListener(this);
        layout_good_type.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_edit_save.setOnClickListener(this);
        btn_edit_cell.setOnClickListener(this);
        iv_scangoodcode.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_scangoodcode.setOnClickListener(this);
        btn_add_store.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivMoreMember.setOnClickListener(this);

        et_goodname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String pinyinCode = PinyinHelper.getShortPinyin(s.toString());
                    tv_py_code.setText(pinyinCode.toUpperCase());
                } catch (PinyinException e) {
                    LogUtils.e(e);
                }

//                tv_py_code
            }
        });
        /**
         * 毛利根据输入的成本价 自动变化
         */
        et_good_costprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String goodprice_src = et_goodprice.getText().toString().trim();
                String good_costprice_src = et_good_costprice.getText().toString().trim();
                Double goodprice_double = 0.00;
                Double good_costprice_double = 0.00;
                if (goodprice_src.length() > 0) {
                    goodprice_double = Double.parseDouble(goodprice_src);
                }
                if (good_costprice_src.length() > 0) {
                    good_costprice_double = Double.parseDouble(good_costprice_src);
                }
                Double good_grossprice_double = goodprice_double - good_costprice_double;
                et_good_grossprice.setText(good_grossprice_double + "");
            }
        });
        tv_quantity_increase = (TextView) findViewById(R.id.tv_quantity_increase);
        SetEditTextvoid();
    }


    private void SetEditTextvoid() {
        SetEditTextInput.setEditTextInhibitInputSpace(et_goodname);
        SetEditTextInput.setEditTextInhibitInputSpace(et_goodcode);
    }


    /**
     * 光标在最后
     *
     * @param editText
     */
    private void edittext(EditText editText) {
        String editText_str = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(editText_str)) {
            editText.setSelection(editText_str.length());
        }


    }

    /**
     * 商品上传，及编辑上传
     */
    private void addSave() {
        String goodname = et_goodname.getText().toString().trim();
        String goodprice = et_goodprice.getText().toString().trim();
        String market_value = ed_market_value.getText().toString().trim();
        String goodcode = et_goodcode.getText().toString().trim();
        String good_costprice = et_good_costprice.getText().toString().trim();
        String good_grossprice = et_good_grossprice.getText().toString().trim();
        String good_stocknum = et_good_stocknum.getText().toString().trim();
        String good_stock = et_good_stock.getText().toString().trim();
        String good_remark = et_good_remark.getText().toString().trim();
        String quantity_increase = tv_quantity_increase.getText().toString();

        String menu_intro = et_good_describe.getText().toString();
        String strMemberPrice01 = edMemberPrice01.getText().toString().trim();
        String strMemberPrice02 = edMemberPrice02.getText().toString().trim();
        String strMemberPrice03 = edMemberPrice03.getText().toString().trim();
        String strMemberPrice04 = edMemberPrice04.getText().toString().trim();
        String strMemberPrice05 = edMemberPrice05.getText().toString().trim();
        if (TextUtils.isEmpty(strMemberPrice01)) {
            strMemberPrice01 = goodprice;
        }
        if (TextUtils.isEmpty(strMemberPrice02)) {
            strMemberPrice02 = goodprice;
        }
        if (TextUtils.isEmpty(strMemberPrice03)) {
            strMemberPrice03 = goodprice;
        }
        if (TextUtils.isEmpty(strMemberPrice04)) {
            strMemberPrice04 = goodprice;
        }
        if (TextUtils.isEmpty(strMemberPrice05)) {
            strMemberPrice05 = goodprice;
        }
        StringBuffer custom_member_price = new StringBuffer();
        custom_member_price.append(strMemberPrice01).append(",")
                .append(strMemberPrice02).append(",")
                .append(strMemberPrice03).append(",")
                .append(strMemberPrice04).append(",")
                .append(strMemberPrice05);
        if (TextUtils.isEmpty(goodname)) {
            SysUtils.showError(getString(R.string.str369));//商品名称不能为空
            return;
        }
        if (TextUtils.isEmpty(goodprice)) {
            SysUtils.showError(getString(R.string.str370));//商品售价不能为空
            return;
        }
        if (TextUtils.isEmpty(good_trpe_result_id)) {
            SysUtils.showError(getString(R.string.str371));//商品类别不能为空！
            return;
        }
//       if(btn_switch_type==2){
        if (TextUtils.isEmpty(goodcode)) {
            SysUtils.showError(getString(R.string.str372));//商品条码不能为空
            return;
        }
        if (TextUtils.isEmpty(good_stocknum)) {
            good_stocknum = "0";
        }
        if (TextUtils.isEmpty(good_stock)) {
            good_stock = "0";
        }
        if (TextUtils.isEmpty(quantity_increase)) {
            quantity_increase = "1";
        } else if (Float.parseFloat(quantity_increase) < 0.01) {
            SysUtils.showError(getString(R.string.str373));//增幅不能小于0.01！(最小起订量)
            return;
        }
//       }
        Log.d("print", "image_id=" + image_id);
        Map<String, String> map = new HashMap<>();
        map.put("name", goodname);//名称
        map.put("py", tv_py_code.getText().toString().trim());//拼音码
        map.put("custom_member_price", custom_member_price.toString());//会员价
        map.put("price", goodprice);//售价
        map.put("cost", good_costprice);//成本价
        map.put("bncode", goodcode);//条码
        map.put("little_profit", good_grossprice);//毛利
        map.put("store", good_stocknum);//库存量
        map.put("isup", good_switch_type + "");//是否上下架
        map.put("image_id", image_id);//图片url
        map.put("good_stock", good_stock);//库存预警量
        map.put("good_remark", good_remark);//备注
        map.put("tag_id", good_trpe_result_id);//类别
        map.put("btn_switch_type", btn_switch_type + "");
        map.put("mktprice", market_value);
        map.put("increase", quantity_increase);
        map.put("menu_intro", menu_intro);
        if (type == 2 || type == 3) {
            map.put("edit", "edit");
            map.put("goods_id", goods_id);
            map.put("product_id", product_id);
        }
        System.out.println("map=" + map);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("goodsToAdd"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret111=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        data = ret.getJSONObject("data");
                        String msg = data.getString("msg");
                        SysUtils.showError(msg);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (type == 2) {
                        sendBroadcast(new Intent(Global.BROADCAST_GoodsManagementactivity_ACTION));
                    } else if (type == 1) {
                        sendBroadcast(new Intent(Global.BROADCAST_GoodsManagementactivity_ACTION).putExtra("tag_id", good_trpe_result_id));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);//图片url
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("goods_page"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret22222=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        data = ret.getJSONObject("data");
                        JSONObject goods_info = data.getJSONObject("goods_info");
                        if (goods_info != null) {
                            String name = goods_info.getString("name");
                            Double price = goods_info.getDouble("price");
                            String bncode = goods_info.getString("bncode");
                            String is_group_by = goods_info.getString("is_group_by");
                            String mktprice = goods_info.getString("mktprice");
                            String is_sell_buy = goods_info.getString("is_sell_buy");
                            String is_menu = goods_info.getString("is_menu");
                            Double cost = goods_info.getDouble("cost");
                            Double little_profit = goods_info.getDouble("little_profit");
                            String image_default_id = goods_info.getString("image_default_id");
                            String good_remark = goods_info.getString("good_remark");
                            String menu_intro = goods_info.getString("menu_intro");
                            String status_up = goods_info.getString("status");
                            String img_src = data.getString("img_src");
                            float store = Float.parseFloat(goods_info.getString("store"));
                            int good_stock = goods_info.getInt("good_stock");
                            String increase = goods_info.getString("increase");
                            btn_switch_type = goods_info.getInt("btn_switch_type");
                            good_trpe_result = data.getString("tag_name");
                            good_trpe_result_id = data.getString("tag_id");
                            product_id = data.getString("product_id");
                            //会员价
                            String custom_member_price = goods_info.getString("custom_member_price");
                            if (!TextUtils.isEmpty(custom_member_price)) {
                                String[] memberPrice = custom_member_price.split(",");
                                if (memberPrice != null) {
                                    if (memberPrice.length > 0 && !TextUtils.isEmpty(memberPrice[0])) {
                                        edMemberPrice01.setText(memberPrice[0]);
                                    }
                                    if (memberPrice.length > 1 && !TextUtils.isEmpty(memberPrice[1])) {
                                        edMemberPrice02.setText(memberPrice[1]);
                                    }
                                    if (memberPrice.length > 2 && !TextUtils.isEmpty(memberPrice[2])) {
                                        edMemberPrice03.setText(memberPrice[2]);
                                    }
                                    if (memberPrice.length > 3 && !TextUtils.isEmpty(memberPrice[3])) {
                                        edMemberPrice04.setText(memberPrice[3]);
                                    }
                                    if (memberPrice.length > 4 && !TextUtils.isEmpty(memberPrice[4])) {
                                        edMemberPrice05.setText(memberPrice[4]);
                                    }

                                }
                            }
                            if (btn_switch_type == 2) {
                                btn_switch.setChecked(true);
                                et_goodcode.setText(bncode);
                            }
                            if (status_up.equals("true")) {
                                good_switch_up.setChecked(true);
                            } else if (status_up.equals("false")) {
                                good_switch_up.setChecked(false);
                            }
                            if (is_group_by.equals("no")) {
                                good_group_buying.setChecked(false);
                            } else {
                                good_group_buying.setChecked(true);
                            }
                            ed_market_value.setText(mktprice);
                            if (is_sell_buy.equals("yes")) {
                                take_out_food.setChecked(true);
                            } else {
                                take_out_food.setChecked(false);
                            }
                            if (is_menu.equals("0")) {
                                table_number.setChecked(false);
                            } else {
                                table_number.setChecked(true);
                            }

//                            //设置桌号点餐
//                            table_number.setChecked(false);

                            tv_quantity_increase.setText(increase);

                            image_id = image_default_id;
                            et_goodname.setText(name);
                            et_goodprice.setText(price + "");
                            et_good_costprice.setText(cost + "");
                            et_good_grossprice.setText(little_profit + "");

                            tv_good_type.setText(good_trpe_result);
                            Log.i("print", "打印商品的库存" + store);
                            et_good_stocknum.setText(store + "");
                            et_good_stock.setText(good_stock + "");
                            et_good_remark.setText(good_remark);
                            et_good_describe.setText(menu_intro);

                            //加载图片
                            ImageLoader.getInstance().displayImage(img_src, iv_goodpicture, options);
                        }

                    }

                } catch (JSONException e) {
                    LogUtils.e("error=" + e.toString());
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }

    /**
     * 获取商品条码
     */
    private void getGoodsCode() {
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("get_code"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        data = ret.getJSONObject("data");
                        String code = data.getString("code");
                        et_goodcode.setText(code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }

    /**
     * 删除商品
     */
    private void deteleGoods() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        map.put("product_id", product_id);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("remove_goods"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret3333=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    sendBroadcast(new Intent(Global.BROADCAST_GoodsManagementactivity_ACTION));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }

    /**
     * 各控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goodpicture:
                getSelectPicPopupWindow();
                mSelectPicPopupWindow.showAtLocation(findViewById(R.id.iv_goodpicture), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_good_type://获取商品类别
                Intent good_typeintent = new Intent(AddGoodsActivity.this, SortManagementActivity.class);
                good_typeintent.putExtra("type", 2);
                startActivityForResult(good_typeintent, INTENT_GOODS_SORT);
                break;
            case R.id.btn_edit_cell://商品删除
                isDelete();
                break;
            case R.id.btn_edit_save://商品编辑保存
            case R.id.tv_save:
                addSave();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save://商品添加
                addSave();
                break;
            case R.id.btn_scangoodcode://生成条码
                getGoodsCode();
                break;
            case R.id.iv_scangoodcode://获取扫描条码
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddGoodsActivity.this, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddGoodsActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                        return;
                    } else {
                        Intent intent = new Intent(AddGoodsActivity.this, MipcaActivityCapture.class);
                        intent.putExtra("type", 2);
                        startActivityForResult(intent, 205);
                    }
                } else {
                    Intent intent = new Intent(AddGoodsActivity.this, MipcaActivityCapture.class);
                    intent.putExtra("type", 2);
                    startActivityForResult(intent, 205);
                }
                break;
            case R.id.btn_add_store://补充库存
                addstore();
                break;
            case R.id.iv_more_member:
                if (layoutOtherMember.getVisibility() == View.VISIBLE) {
                    layoutOtherMember.setVisibility(View.GONE);
                } else {
                    layoutOtherMember.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    /**
     * 补充库存弹窗
     */
    AlertDialog maddstoredoalog;
    EditText et_accountinfo_input;
    InputMethodManager imm;

    private void addstore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoodsActivity.this);
        View view = View.inflate(AddGoodsActivity.this, R.layout.add_store_dialog, null);
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView tv_nomark = (TextView) view.findViewById(R.id.tv_nomark);
        et_accountinfo_input = (EditText) view.findViewById(R.id.et_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
        //软键盘显示
        imm = (InputMethodManager) AddGoodsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        tv_nomark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maddstoredoalog.dismiss();
                //再次调用软键盘消失
                if (isSoftShowing()) {
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_input_str = et_accountinfo_input.getText().toString().trim();
                if (TextUtils.isEmpty(et_input_str)) {
                    Toast.makeText(AddGoodsActivity.this, "请输入补充库存量！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String et_good_stocknum_str = et_good_stocknum.getText().toString().trim();
                if (!TextUtils.isEmpty(et_good_stocknum_str)) {
                    float et_good_stocknum_int = Float.parseFloat(et_good_stocknum_str) + Float.parseFloat(et_input_str);
                    et_good_stocknum.setText(et_good_stocknum_int + "");
                }
                maddstoredoalog.dismiss();
                //再次调用软键盘消失
                if (isSoftShowing()) {
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        maddstoredoalog = builder.setView(view).show();
        maddstoredoalog.setCancelable(false);

        Window dialogWindow = maddstoredoalog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP);
        lp.y = 200; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        maddstoredoalog.show();
    }

    //判断软键盘是否存在
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    /**
     * 商品删除
     */
    private void isDelete() {
        View view = View.inflate(AddGoodsActivity.this, R.layout.dialog_sure_delete_layout, null);
        final Dialog dialog = new Dialog(AddGoodsActivity.this);
        dialog.setContentView(view);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_cell = (Button) view.findViewById(R.id.btn_cell);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_message.setText(getString(R.string.str125));
        btn_sure.setText(getString(R.string.sure));
        btn_cell.setText(getString(R.string.cancel));
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deteleGoods();
                dialog.dismiss();
                finish();
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 选择按钮的监听
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.btn_switch:
//                if(isChecked){
//                    if(type==1){
//                        tv_title_name.setText("有码商品添加");
//                    }else {
//                        tv_title_name.setText("商品编辑");
//                    }
//                    btn_switch_type=2;
//                    layout_goodscode.setVisibility(View.VISIBLE);
//                    good_stock_layout.setVisibility(View.VISIBLE);
//                    good_stocknum_layout.setVisibility(View.VISIBLE);
//                }else {
//                    if(type==1){
//                    tv_title_name.setText("无码商品添加");
//                    }else {
//                        tv_title_name.setText("商品编辑");
//                    }
//                    btn_switch_type=1;
//                    layout_goodscode.setVisibility(View.GONE);
//                    good_stock_layout.setVisibility(View.GONE);
//                    good_stocknum_layout.setVisibility(View.GONE);
//                }
                break;
            case R.id.good_switch_up:
                if (isChecked) {
                    good_switch_type = 1;
                } else {
                    good_switch_type = 0;
                }
                break;
            //设置团购的开关
            case R.id.good_group_buying:
                if (buttonView.isPressed()) {
                    getSetgroup_buying(isChecked);
                }
                break;
            case R.id.take_out_food:
                if (buttonView.isPressed()) {
                    setSellGoodsNums(isChecked);
                }
                break;
            case R.id.table_number:
                if (buttonView.isPressed()) {
                    setTableNumber(isChecked);
                }
                break;
        }
    }

    //设置桌号点餐
    public void setTableNumber(boolean isgroup_buying) {
        showLoading(AddGoodsActivity.this);
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        if (isgroup_buying) {
            map.put("is_menu", "1");
        } else {
            map.put("is_menu", "0");
        }
        String url = SysUtils.getnewsellerUrl("Menu/goods");
        CustomRequest r = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                System.out.println("添加外卖商品ret=" + response.toString());
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError("添加成功");
                    } else {
                        String data = ret.getString("data");
                        SysUtils.showError(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
    }

    //设置外卖的商品
    public void setSellGoodsNums(boolean isgroup_buying) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        if (isgroup_buying) {
            map.put("type", "");
        } else {
            map.put("type", "delete");
        }
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("setSellGoodsNums"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                JSONObject ret = SysUtils.didResponse(response);
                System.out.println("添加外卖商品ret=" + ret);
                try {
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        String data = ret.getString("data");
                        SysUtils.showError(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }


    //设置团购的商品的
    public void getSetgroup_buying(boolean isgroup_buying) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        if (isgroup_buying) {
            map.put("type", "");
        } else {
            map.put("type", "delete");
        }
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("setGroupButGoodsNums"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                JSONObject ret = SysUtils.didResponse(response);
                System.out.println("添加团购商品ret=" + ret);
                try {
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        String data = ret.getString("data");
                        SysUtils.showError(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(customRequest);
        showLoading(AddGoodsActivity.this);
    }

    /**
     * 弹出拍照，从相册获取添加图片的窗口
     */
    private void getSelectPicPopupWindow() {
        mSelectPicPopupWindow = new SelectPicPopupWindow(AddGoodsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_pick_photo:
                        //从相册获取图片
//                        Intent intent_btn_pick_photo=new Intent();
//                        intent_btn_pick_photo.setType("image/*");
//                        intent_btn_pick_photo.setAction(Intent.ACTION_GET_CONTENT);
//                        intent_btn_pick_photo.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                            startActivityForResult(intent_btn_pick_photo, GALLERY_REQUSET_CODE_KITKAT);
////                        } else {
////                            startActivityForResult(intent_btn_pick_photo, INTENT_BTN_PICK_PHOTO);
////                        }
//                        startActivityForResult(intent_btn_pick_photo,INTENT_BTN_PICK_PHOTO);


                        //也可以这样写
                        Intent intentAlbum = new Intent(Intent.ACTION_PICK, null);
                        //其中External为sdcard下的多媒体文件,Internal为system下的多媒体文件。
                        //使用INTERNAL_CONTENT_URI只能显示存储在内部的照片
                        intentAlbum.setDataAndType(
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                        //返回结果和标识
                        startActivityForResult(intentAlbum, INTENT_BTN_PICK_PHOTO);

                        break;
                    case R.id.btn_take_photo:
                        if (Build.VERSION.SDK_INT >= 23) {
                            //动态添加sdk写入权限，主要适配与android6.0以上的系统
                            int checkwritefile = ContextCompat.checkSelfPermission(AddGoodsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (checkwritefile != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddGoodsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 223);
                                return;
                            } else {
                                creatfile();
                            }
                            //动态添加拍照权限
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddGoodsActivity.this, Manifest.permission.CAMERA);
                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddGoodsActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                                return;
                            } else {
//                                onOpenCamera();
                                takePhoto();
                            }
                        } else {
                            creatfile();
//                            onOpenCamera();
                            takePhoto();
                        }
                        break;

                }
            }
        });
    }

    /**
     * @param
     * @description 裁剪图片
     * @author ldm
     * @time 2016/11/30 15:19
     */
    // 定义拍照后存放图片的文件位置和名称，使用完毕后可以方便删除
    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

    private void startPhotoZoom(Uri uri, int type) {
        Bitmap bit = null;
        try {
            bit = UploadUtil.getBitmapFormUri(AddGoodsActivity.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String path = GetImagePath.getFilePathByUri(AddGoodsActivity.this, uri);


        myCaptureFile = UploadUtil.saveFile(bit, path + ".jpg");
        photoUri = Uri.fromFile(myCaptureFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
//        if(type==INTENT_BTN_PICK_PHOTO)
//        {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }else {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        //设置为不返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CROP_CODE);
        Log.d("print", "启动裁剪程序");
    }


    private void takePhoto() {
        // 步骤一：创建存储照片的文件
        String path = Environment.getExternalStorageDirectory().getPath() + "/image";
        file = new File(path, "temp.jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            photoUri = FileProvider.getUriForFile(AddGoodsActivity.this, "com.ui.ks.camera_photos.provider", file);
//            intent.setDataAndType(photoUri, "application/vnd.android.package-archive");

        } else {
            //步骤三：获取文件Uri
            photoUri = Uri.fromFile(file);
        }
        //步骤四：调取系统拍照

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, INTENT_BTN_TAKE_PHOTO);
    }


    /**
     * 图片裁剪的方法
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Log.e("uri=====", "" + uri);
        //com.android.camera.action.CROP，这个action是调用系统自带的图片裁切功能
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //uriClipUri为Uri类变量，实例化uriClipUri
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (isTakePhoto == 1) {//如果是7.0的拍照
//                //开启临时访问的读和写权限
//                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                //针对7.0以上的操作
//                intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
//                photoUri = uri;
//            } else {//如果是7.0的相册
//                //设置裁剪的图片地址Uri
//                photoUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg");
////                photoUri = Uri.parse("file://" + "/" + getFilesDir() + File.separator + "images" + File.separator + "/" + "test.jpg");
//            }
//
//        } else {
        photoUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg");
//            photoUri = Uri.parse("file://" + "/" + getFilesDir() + File.separator + "images" + File.separator + "/" + "test.jpg");
//        }
        Log.e("uriClipUri=====", "" + photoUri);
        //Android 对Intent中所包含数据的大小是有限制的，一般不能超过 1M，否则会使用缩略图 ,所以我们要指定输出裁剪的图片路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("return-data", false);//是否将数据保留在Bitmap中返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出格式，一般设为Bitmap格式及图片类型
        intent.putExtra("noFaceDetection", true);//人脸识别功能
        startActivityForResult(intent, PHOTO_CROP_CODE);//裁剪完成的标识
    }

    /**
     * 创建存储拍照图片的文件夹
     */
    private void creatfile() {
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String str_path = getSDPath() + "/image";
//        File file=new File(str_path.trim());
//        //判断文件夹是否存在,如果不存在则创建文件夹
        // 获取SD卡路径
        mFilePath = str_path + name + ".jpg";
        File file = new File(mFilePath);
//        if (!file.exists()) {
//            try {
//                file.mkdirs();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        pick_photo = Uri.fromFile(new File(mFilePath));
        ;
    }

    /**
     * 调用相机拍照
     */
    private void onOpenCamera() {
        // 加载路径
        // 指定存储路径，这样就可以保存原图了
//        Intent intent_btn_take_photo=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent_btn_take_photo.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent_btn_take_photo,INTENT_BTN_TAKE_PHOTO);
//
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
////                new File(Environment.getExternalStorageDirectory(), imgName)));
////        File file = new File(Environment.getExternalStorageDirectory(), imgName);
//        if (file != null) {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                uri = Uri.fromFile(file);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            } else {
//                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            }
////            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            startActivityForResult(cameraIntent, INTENT_BTN_TAKE_PHOTO);
//        }


        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mImageCaptureUri;
        // 判断7.0android系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //临时添加一个拍照权限
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider获取uri
            //临时添加一个拍照权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(AddGoodsActivity.this,
                    "com.ms.ks.provider", new File(mFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            mImageCaptureUri = Uri.fromFile(new File(mFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, INTENT_BTN_TAKE_PHOTO);
    }

    //    android获取sd卡路径方法：
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.getPath();
    }

    /**
     * 页面结果返回
     *
     * @param requestCode
     * @param resultCode
     * @param data        dsd
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_GOODS_SORT && resultCode == 204) {
            Bundle bundle = data.getExtras();
            good_trpe_result = bundle.getString("goodtype");
            good_trpe_result_id = bundle.getString("goodtype_id");
            tv_good_type.setText(good_trpe_result);
        }
        if (requestCode == 205 && resultCode == 206) {
            Bundle bundle = data.getExtras();
            et_goodcode.setText(bundle.getString("barcode"));
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 222) {
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
//                    onOpenCamera();
                    takePhoto();
                } else {
                    Toast.makeText(AddGoodsActivity.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            if (requestCode == 223) {
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    creatfile();
                } else {
                    Toast.makeText(AddGoodsActivity.this, "很遗憾你把读写权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            //从相册获取图片并显示
            if (requestCode == INTENT_BTN_PICK_PHOTO) {
                mSelectPicPopupWindow.dismiss();

                pick_photo = data.getData();
//                pick_photo=Uri.parse("file:///" + getPath(this, data.getData()));

                isTakePhoto = 1;
//                Bitmap bit=null;
//                try {
//                    bit = UploadUtil.getBitmapFormUri(AddGoodsActivity.this, pick_photo);
//                    String path = GetImagePath.getPath(AddGoodsActivity.this, pick_photo);
//                    myCaptureFile = UploadUtil.saveFile(bit,path+".jpg");
//                    photoUri = Uri.fromFile(myCaptureFile);
//                    startPhotoZoom(photoUri,INTENT_BTN_PICK_PHOTO);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("ee="+e.toString());
//                }

                startPhotoZoom(pick_photo);

//                upImageAsyTack upImageAsyTack=new upImageAsyTack();
//                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
//            if (requestCode==GALLERY_REQUSET_CODE_KITKAT){
//                mSelectPicPopupWindow.dismiss();
//                pick_photo = data.getData();
////                File faceFile;
////                try {
////                    ParcelFileDescriptor parcelFileDescriptor =
////                            getContentResolver().openFileDescriptor(contentUri, "r");
////                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
////                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
////                    faceFile = saveBitmap(image);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    return;
////                }
////                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                    // Android 7.0 "file://" uri权限适配
////                    pick_photo = FileProvider.getUriForFile(this,
////                            "gavinli.translator", faceFile);
////                } else {
////                    pick_photo = Uri.fromFile(faceFile);
////                }
//                isTakePhoto=1;
//                Bitmap bit=null;
//                try {
//                    bit = UploadUtil.getBitmapFormUri(AddGoodsActivity.this, pick_photo);
//                    String path = GetImagePath.getPath(AddGoodsActivity.this, pick_photo);
//                    myCaptureFile = UploadUtil.saveFile(bit,path+".jpg");
//                    photoUri = Uri.fromFile(myCaptureFile);
//                    startPhotoZoom(photoUri,INTENT_BTN_PICK_PHOTO);
////                    upImageAsyTack upImageAsyTack=new upImageAsyTack();
////                    upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("ee="+e.toString());
//                }
//            }


            //拍照获取图片并显示

            if (requestCode == INTENT_BTN_TAKE_PHOTO) {
                mSelectPicPopupWindow.dismiss();
                isTakePhoto = 2;


//                Uri clipUri;
//                //判断如果是7.0
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//                    pick_photo=  uri;
//                }else{
//                    pick_photo=Uri.fromFile(new File(mFilePath)) ;
//                }
                startPhotoZoom(photoUri);
//                Bitmap bit=null;
//                try {
//                    //裁剪后的图像转成BitMap
//                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
////                    bit = UploadUtil.getBitmapFormUri(AddGoodsActivity.this, photoUri);
//                    String path = GetImagePath.getPath(AddGoodsActivity.this, photoUri);
//                    myCaptureFile = UploadUtil.saveFile(bit,path+".jpg");
//                    upImageAsyTack upImageAsyTack=new upImageAsyTack();
//                    upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("ee="+e.toString());
//                }
//                try {
//                    bit=  UploadUtil.getBitmapFormUri(AddGoodsActivity.this,uri);
//                    myCaptureFile= UploadUtil.saveFile(bit,mFilePath+".jpg");
//                     photoUri = Uri.fromFile(myCaptureFile);
//                    startPhotoZoom(photoUri,INTENT_BTN_TAKE_PHOTO);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                upImageAsyTack upImageAsyTack=new upImageAsyTack();
//                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        if (requestCode == PHOTO_CROP_CODE) {
            Bitmap bit = null;
            try {
//                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                bit = UploadUtil.getBitmapFormUri(AddGoodsActivity.this, photoUri);
                String path = GetImagePath.getPath(AddGoodsActivity.this, photoUri);
                myCaptureFile = UploadUtil.saveFile(bit, path + ".jpg");
                upImageAsyTack upImageAsyTack = new upImageAsyTack();
                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ee=" + e.toString());
            }
        }
    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
// ExternalStorageProvider

            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);

                final String[] split = docId.split(":");

                final String type = split[0];


                if ("primary".equalsIgnoreCase(type)) {

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }


            }

            // DownloadsProvider

            else if (isDownloadsDocument(uri)) {


                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));


                return getDataColumn(context, contentUri, null, null);

            }

            // MediaProvider

            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);

                final String[] split = docId.split(":");

                final String type = split[0];


                Uri contentUri = null;

                if ("image".equals(type)) {

                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {

                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {

                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }


                final String selection = "_id=?";

                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

// MediaStore (and general)

        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * uri格式的图片显示
     *
     * @param uri
     */
    private void showUriImage(Uri uri) {
        iv_goodpicture.setImageURI(uri);
    }

    /**
     * 上传图片
     */
    class upImageAsyTack extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(AddGoodsActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("print", "doInBackground: 打印上传图片的url" + requrl);
//            String str = UploadUtil.uploadFile( myCaptureFile, requrl, "photo");
            String str = UploadUtil.uploadFile(myCaptureFile, requrl, "file");
            if (str != null) {
                try {
                    System.out.println("添加商品图片到七牛云" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("添加商品图片ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = ret.getJSONObject("data");
                    JSONObject object = data.getJSONObject("list");
                    image_id = object.getString("image_id");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return image_id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
            if (s != null) {
                if (isTakePhoto == 2) {
                    showUriImage(photoUri);
                }
                if (isTakePhoto == 1) {
                    if (pick_photo != null) {
                        showUriImage(photoUri);
                    }
                }
            } else {
                SysUtils.showError("图片上传失败");
            }
            if (myCaptureFile.exists()) {
                myCaptureFile.delete();  //删除原图片
            }
        }
    }

    /**
     * 获取分类信息
     */
    private void getSortlist() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_getlist"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("分类ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        data = ret.getJSONObject("data");
                        JSONArray arry = data.getJSONArray("nav_info");
                        if (arry != null && arry.length() > 0) {
                            JSONObject jsonObject1 = arry.getJSONObject(0);
                            String typeName = jsonObject1.getString("tag_name");
                            tv_good_type.setText(typeName);
                        }

                    }

                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
    }
}
