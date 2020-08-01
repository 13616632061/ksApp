package com.ui.ks;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.DeleteEditText;
import com.ui.util.SetEditTextInput;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends BaseActivity {
    //发货人
//    TextView textView1;
    EditText textView2;

    //手机号
    TextView textView3;
    EditText textView4;

    //固定电话
    TextView textView5;
    EditText textView6;

    //省
    RelativeLayout relativeLayout1;
    TextView textView8;

    //市
//    RelativeLayout relativeLayout2;
//    TextView textView12;

    //县
//    RelativeLayout relativeLayout3;
//    TextView textView14;

    //详细地址
    TextView textView9;
    EditText textView10;
    TextView toolbar_title;

    private int provinceId = 0, cityId = 0, areaId=  0;
//    private AreaPicker areaPicker;

    private ImageView imageView1;
    private String area = "";
    EditText textView_freight;
    EditText textView_start_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.distribution_set));


//        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (EditText) findViewById(R.id.textView2);    //收件人
//        new DeleteEditText(textView2, textView1);

        textView_freight= (EditText) findViewById(R.id.textView_freight);
        textView_start_price= (EditText) findViewById(R.id.textView_start_price);

        SetEditTextInput.judgeNumber(textView_freight);
        SetEditTextInput.judgeNumber(textView_start_price);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (EditText) findViewById(R.id.textView4);    //手机号
        new DeleteEditText(textView4, textView3);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (EditText) findViewById(R.id.textView6);    //邮编
        new DeleteEditText(textView6, textView5);

//        areaPicker = new AreaPicker(this, new MaterialDialog.ButtonCallback() {
//            @Override
//            public void onPositive(MaterialDialog dialog) {
//                super.onPositive(dialog);
//
//                textView8.setText(areaPicker.getAreaStr());
//                provinceId = areaPicker.getProvinceId();
//                cityId = areaPicker.getCityId();
//                areaId = areaPicker.getAreaId();
////                SysUtils.showSuccess(areaStr);
//            }
//        });

        //省
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        textView8 = (TextView) findViewById(R.id.textView8);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putInt("pid", 0);

                SysUtils.startAct(AddressActivity.this, new AddressLocationActivity(), bundle, true);
            }
        });

        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (EditText) findViewById(R.id.textView10);    //详细地址
        new DeleteEditText(textView10, textView9);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        ContactsContract.Contacts.CONTENT_URI);

                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                AddressActivity.this.startActivityForResult(intent, 1);
            }
        });

        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String freight="0";
                String start_price="0";
                if (!textView_freight.getText().toString().equals("")){
                    freight=textView_freight.getText().toString();
                }
                if (!textView_start_price.getText().toString().equals("")){
                    start_price=textView_start_price.getText().toString();
                }
                String name = textView2.getText().toString();
                if(StringUtils.isEmpty(name)) {
                    SysUtils.showError("请填写发货人姓名");
                } else {
                    String mobile = textView4.getText().toString();
                    String phone = textView6.getText().toString();
                    if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(phone)) {
                        SysUtils.showError("手机号码和固定电话必填其一");
                    } else {
                        if(provinceId <= 0) {
                            SysUtils.showError("请选择所在地区");
                        } else {
                            String address = textView10.getText().toString();
                            if(StringUtils.isEmpty(address)) {
                                SysUtils.showError("请填写详细地址");
                            } else {
                                Map<String,String> map = new HashMap<String,String>();
                                map.put("officer", name);
                                map.put("mobile", mobile);
                                map.put("tel", phone);
                                map.put("area", getPostArea());
                                map.put("addr", address);
                                map.put("reward",freight);
                                map.put("freight",start_price);

                                CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sellerSave"), map, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        hideLoading();

                                        try {
                                            JSONObject ret = SysUtils.didResponse(jsonObject);
                                            String status = ret.getString("status");
                                            String message = ret.getString("message");

                                            if (!status.equals("200")) {
                                                SysUtils.showError(message);
                                            } else {
                                                SysUtils.showSuccess("发货地址已保存");
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

                                showLoading(AddressActivity.this);
                            }
                        }


                    }
                }
            }
        });

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            if(b != null && b.containsKey("provinceId") && b.containsKey("cityId") && b.containsKey("townId") && b.containsKey("areaStr")) {
                provinceId = b.getInt("provinceId");
                cityId = b.getInt("cityId");
                areaId = b.getInt("townId");
                area = b.getString("areaStr");
                textView8.setText(area);
            } else {
//                Uri contactData = data.getData();
//                Cursor cursor = managedQuery(contactData, null, null, null,
//                        null);
//                cursor.moveToFirst();
//                String num = this.getContactPhone(cursor);
//
//                int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                String name = cursor.getString(nameIdx);
//
//                if(!StringUtils.isEmpty(name)) {
//                    textView2.setText(name);
//                }
//
//                if(!StringUtils.isEmpty(num)) {
//                    //删除掉空格
//                    num = num.replaceAll("\\s*", "");
//                    textView4.setText(num);
//                }

                String phoneNo = null ;
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();

                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNo = cursor.getString(phoneIndex);

                if(!StringUtils.isEmpty(phoneNo)) {
//                    SysUtils.showSuccess(phoneNo);
                    //删除掉空格
                    phoneNo = phoneNo.replaceAll("\\s*", "");
                    textView4.setText(phoneNo);
                }

                int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(nameIdx);

                if(!StringUtils.isEmpty(name)) {
                    textView2.setText(name);
                }
            }
        }
    }

    private String getContactPhone(Cursor cursor) {
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
//                  switch (phone_type) {//此处请看下方注释
//                  case 2:
//                      result = phoneNumber;
//                      break;
//
//                  default:
//                      break;
//                  }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }

    private void initView() {
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sellerAddr"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("打印发货的数据ret22222="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        textView2.setText(dataObject.getString("officer"));
                        textView4.setText(dataObject.getString("mobile"));
                        textView6.setText(dataObject.getString("tel"));
                        textView_freight.setText(dataObject.getString("reward"));
                        textView_start_price.setText(dataObject.getString("freight"));

                        JSONObject areaObject = dataObject.getJSONObject("area");

                        area = areaObject.getString("area");
                        textView8.setText(area);

                        getAreaId(areaObject.getString("area_id"));
                        textView10.setText(dataObject.getString("addr"));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showNetworkError();
                hideLoading();
            }
        });

        executeRequest(r);
        showLoading(this);
    }


    private String getPostArea() {
        String ret = "mainland";
        ret += ":" + area;
        if(areaId > 0) {
            ret += ":" + areaId;
        } else if(cityId > 0) {
            ret += ":" + cityId;
        } else if(provinceId > 0) {
            ret += ":" + provinceId;
        }

//        SysUtils.showSuccess(ret);

        return ret;
    }

    private void getAreaId(String area_id) {
        String[] aa = area_id.split(",");

        int aIndex = 0;
        for (int  i = 0; i < aa.length; i++) {
            if (!StringUtils.isEmpty(aa[i])) {
                int aid = Integer.parseInt(aa[i]);

                if (aid > 0) {
                    if (aIndex == 0) {
                        provinceId = aid;
                    } else if(aIndex == 1) {
                        cityId = aid;
                    } else if(aIndex == 2) {
                        areaId= aid;
                    }
                    aIndex++;
                }

            }
        }
    }
}
