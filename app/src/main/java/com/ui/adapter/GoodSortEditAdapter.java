package com.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.entity.GoodSort;
import com.ui.ks.R;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品分类编辑适配器
 * Created by Administrator on 2017/3/11.
 */

public class GoodSortEditAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GoodSort> goodsortList;
    private Handler handler;
    private  InputMethodManager imm;
    private AlertDialog mAlertDialog;
    private  EditText et_accountinfo_input;
    private Dialog progressDialog = null;
    private boolean istonch=false;

    public GoodSortEditAdapter(Context context,  ArrayList<GoodSort> goodsortList,Handler handler) {
        this.context = context;
        this.handler = handler;
        this.goodsortList = goodsortList;
    }

    @Override
    public int getCount() {
        return goodsortList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean istonch(boolean istonch) {
        this.istonch=istonch;
        notifyDataSetChanged();
        return istonch;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_goodsortedit_layout,null);
            holder=new Holder();
            holder.btn_goodsortnum= (Button) convertView.findViewById(R.id.btn_goodsortnum);
            holder.tv_goodsortname= (TextView) convertView.findViewById(R.id.tv_goodsortname);
            holder.tv_goodsort_remove= (TextView) convertView.findViewById(R.id.tv_goodsort_remove);
            holder.relativeLayout_goodsort_edit= (RelativeLayout) convertView.findViewById(R.id.relativeLayout_goodsort_edit);
            holder.relativeLayout_goodsort_remove= (RelativeLayout) convertView.findViewById(R.id.relativeLayout_goodsort_remove);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(goodsortList.size()>0) {
            holder.btn_goodsortnum.setText(position+ 1 + "");
            holder.tv_goodsortname.setText(goodsortList.get(position).getName());
            final Holder finalHolder = holder;
            holder.relativeLayout_goodsort_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.relativeLayout_goodsort_edit.setVisibility(View.GONE);
                    finalHolder.relativeLayout_goodsort_remove.setVisibility(View.GONE);
                    finalHolder.tv_goodsort_remove.setVisibility(View.VISIBLE);
                }
            });
            holder.tv_goodsort_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SysUtils.isFastDoubleClick();
                    removeSortlist(position);
                    finalHolder.relativeLayout_goodsort_edit.setVisibility(View.VISIBLE);
                    finalHolder.relativeLayout_goodsort_remove.setVisibility(View.VISIBLE);
                    finalHolder.tv_goodsort_remove.setVisibility(View.GONE);
                }
            });
            holder.relativeLayout_goodsort_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addsortDialog(position);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.relativeLayout_goodsort_edit.setVisibility(View.VISIBLE);
                    finalHolder.relativeLayout_goodsort_remove.setVisibility(View.VISIBLE);
                    finalHolder.tv_goodsort_remove.setVisibility(View.GONE);
                }
            });
            if(istonch){
                finalHolder.relativeLayout_goodsort_edit.setVisibility(View.VISIBLE);
                finalHolder.relativeLayout_goodsort_remove.setVisibility(View.VISIBLE);
                finalHolder.tv_goodsort_remove.setVisibility(View.GONE);
            }

        }
        return convertView;
    }
    private class  Holder{
        private Button btn_goodsortnum;
        private TextView tv_goodsortname;
        private TextView tv_goodsort_remove;
        private RelativeLayout relativeLayout_goodsort_edit;
        private RelativeLayout   relativeLayout_goodsort_remove;
    }
    /**
     * 删除分类信息
     */
    private  void removeSortlist(final int position){
        Map<String,String> map=new HashMap<String, String>();
        map.put("tag_id",goodsortList.get(position).getId());
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_remove"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("删除分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        goodsortList.remove(position);
                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                SysUtils.showNetworkError();

            }
        });
        RequestManager.addRequest(customRequest, this);
        progressDialog = DialogUtils.createLoadingDialog(context, context.getResources().getString(R.string.str92));
        progressDialog.show();
    }
    /**
     * 编辑分类弹窗
     */
    private void addsortDialog(final int position) {
        String content=goodsortList.get(position).getName().trim().toString();
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        View view =View.inflate(context, R.layout.editaccountinfo_dialog,null);
        TextView tv_accountinfo_name=(TextView)view.findViewById(R.id.tv_accountinfo_name);
        et_accountinfo_input=(EditText)view.findViewById(R.id.et_accountinfo_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
        et_accountinfo_input.setText(content);
        et_accountinfo_input.setSelection(content.length());//将光标移至文字末尾
        //软键盘显示
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        TextView tv_editaccount_cancel=(TextView)view.findViewById(R.id.tv_editaccount_cancel);
        TextView tv_editaccount_save=(TextView)view.findViewById(R.id.tv_editaccount_save);
        tv_accountinfo_name.setText("编辑分类");
        tv_editaccount_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                //再次调用软键盘消失
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        tv_editaccount_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_accountinfo_input.getText().toString().trim())){
                    editSort(position);
                    mAlertDialog.dismiss();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog= dialog.setView(view).show();
        mAlertDialog.show();
    }
    /**
     * 编辑分类
     */
    private void editSort(int position){
        String nav=et_accountinfo_input.getText().toString().trim();
        Map<String,String> map=new HashMap<>();
        map.put("nav",nav);
        map.put("tag_id",goodsortList.get(position).getId());
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_edit"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("添加分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    handler.sendEmptyMessage(200);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
            }
        });
        RequestManager.addRequest(customRequest, this);

    }


}
