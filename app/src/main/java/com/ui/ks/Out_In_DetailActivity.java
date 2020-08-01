package com.ui.ks;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.google.gson.Gson;
import com.ui.entity.Out_in_entity;
import com.library.utils.BigDecimalArith;
import com.ui.util.CustomRequest;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/6/12.
 */

public class Out_In_DetailActivity extends BaseActivity {

    public ListView lv_content;
    public String order_id="";
    public String total="";
    public Out_in_entity out_in_entity;
    public Out_in_Adapter adpater;
    public RelativeLayout shipView;
    public TextView tv_total;

//    store_info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_in_detailactivity);

        initView();

        initToolbar(this);


        shipView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.footer_total, lv_content, false);
        tv_total= (TextView) shipView.findViewById(R.id.tv_total);
        lv_content.addFooterView(shipView);



        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("order_id")) {
                order_id = bundle.getString("order_id");
                total=bundle.getString("total");
                tv_total.setText(total);
            }
        }

        if(StringUtils.isEmpty(order_id)) {
            finish();
        }


        getdate();

    }

    private void initView() {
        lv_content= (ListView) findViewById(R.id.lv_content);
        adpater=new Out_in_Adapter();
    }


    public void getdate() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", order_id);
        CustomRequest r=new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("store_info"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret = SysUtils.didResponse(response);
                System.out.println("print商品出入库详情"+ret);
                Gson gson=new Gson();
                out_in_entity=gson.fromJson(ret.toString(),Out_in_entity.class);
                lv_content.setAdapter(adpater);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        executeRequest(r);
    }

    public class Out_in_Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return out_in_entity.getData().size();
        }

        @Override
        public Object getItem(int i) {
            return out_in_entity.getData().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder=null;
            if (view!=null){
                viewHolder= (ViewHolder) view.getTag();
            }else {
                viewHolder =new ViewHolder();
                view= LayoutInflater.from(Out_In_DetailActivity.this).inflate(R.layout.item_order_goods,null);
                viewHolder.textView1= (TextView) view.findViewById(R.id.textView1);
                viewHolder.textView2= (TextView) view.findViewById(R.id.textView2);
                viewHolder.textView3= (TextView) view.findViewById(R.id.textView3);
                viewHolder.textView4= (TextView) view.findViewById(R.id.textView4);
                view.setTag(viewHolder);
            }

            viewHolder.textView4.setVisibility(View.VISIBLE);
            viewHolder.textView1.setMaxEms(10);
            viewHolder.textView1.setLines(1);
            viewHolder.textView1.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.textView1.setText(out_in_entity.getData().get(i).getName());
            viewHolder.textView2.setText(out_in_entity.getData().get(i).getNums());
            viewHolder.textView3.setText(BigDecimalArith.mul(Double.parseDouble(out_in_entity.getData().get(i).getNums()),Double.parseDouble(out_in_entity.getData().get(i).getCost()))+"");
            viewHolder.textView4.setText(Double.parseDouble(out_in_entity.getData().get(i).getCost())+"");
            return view;
        }
    }

    static class ViewHolder {
        public TextView textView1, textView2, textView3, textView4, textView11;
    }

}
