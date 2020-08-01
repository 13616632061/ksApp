package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseActivity;

/**
 * Created by Administrator on 2020/3/7.
 */

public class MarketingActivity extends BaseActivity implements View.OnClickListener {


    ImageView iv_managmentfragment_back;
    TextView tv_new_marketing;
    ImageView image_member,image_sweep_code,image_delivery;
//    PagingListView lv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing);
        initToolbar(this);
        initView();

//        LoadAdats();
    }

    //初始化
    private void initView() {
        iv_managmentfragment_back = (ImageView) findViewById(R.id.iv_managmentfragment_back);
        iv_managmentfragment_back.setOnClickListener(this);
        tv_new_marketing = (TextView) findViewById(R.id.tv_new_marketing);
        tv_new_marketing.setOnClickListener(this);

        image_member= (ImageView) findViewById(R.id.image_member);
        image_member.setOnClickListener(this);
        image_sweep_code= (ImageView) findViewById(R.id.image_sweep_code);
        image_sweep_code.setOnClickListener(this);
        image_delivery= (ImageView) findViewById(R.id.image_delivery);
        image_delivery.setOnClickListener(this);

//        lv_content= (PagingListView) findViewById(R.id.lv_content);
//
//        View view= LayoutInflater.from(this).inflate(R.layout.itme_marketing,null);
//        lv_content.addHeaderView(view);

    }

    //设置点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_managmentfragment_back:
                finish();
                break;
            case R.id.image_member:
                Intent intent2=new Intent(MarketingActivity.this,MemberSpecificationsActivity.class);
                startActivity(intent2);
                break;
            case R.id.image_sweep_code:
                Intent intent=new Intent(MarketingActivity.this,CodescanningActivity.class);
                startActivity(intent);
                break;
            case R.id.image_delivery:
                Intent intent1=new Intent(MarketingActivity.this,AddressActivity.class);
                startActivity(intent1);
                break;
        }
    }


//    public void LoadAdats(){
//        Map<String,String> map=new HashMap<>();
//        map.put("type","1");
//        CustomRequest r=new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("recharge_list"), map, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.d("print","打印出来的会员充值的数据"+response.toString());
//                    JSONObject ret=SysUtils.didResponse(response);
//                    String status = ret.getString("status");
//                    String message = ret.getString("message");
//                    JSONObject dataObject = null;
//                    if (!status.equals("200")) {
//                        SysUtils.showError(message);
//                    } else {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        executeRequest(r);
//    }

}
