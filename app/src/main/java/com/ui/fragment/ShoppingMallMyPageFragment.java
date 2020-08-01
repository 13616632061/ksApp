package com.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.ks.R;
import com.ui.ks.SelectAdressActivity;
import com.ui.ks.WholeSaleOrdersActivity;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShoppingMallMyPageFragment extends BaseFragmentMainBranch implements View.OnClickListener {
    private RelativeLayout layout_allorder,layout_waitpay,layout_waitsend,layout_waitget,
            layout_outmoney,layout_adress,layout_collect;
    private TextView tv_phone;
    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.shoppingmallmypagefragment,null);
        initview(view);
        return view;
    }

    private void initview(View view) {
        layout_allorder= (RelativeLayout) view.findViewById(R.id.layout_allorder);
        layout_waitpay= (RelativeLayout) view.findViewById(R.id.layout_waitpay);
        layout_waitsend= (RelativeLayout) view.findViewById(R.id.layout_waitsend);
        layout_waitget= (RelativeLayout) view.findViewById(R.id.layout_waitget);
        layout_outmoney= (RelativeLayout) view.findViewById(R.id.layout_outmoney);
        layout_adress= (RelativeLayout) view.findViewById(R.id.layout_adress);
        layout_collect= (RelativeLayout) view.findViewById(R.id.layout_collect);
        tv_phone= (TextView) view.findViewById(R.id.tv_phone);

        layout_allorder.setOnClickListener(this);
        layout_waitpay.setOnClickListener(this);
        layout_waitsend.setOnClickListener(this);
        layout_waitget.setOnClickListener(this);
        layout_outmoney.setOnClickListener(this);
        layout_adress.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        tv_phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_allorder:
                Intent intent_allorder=new Intent(getActivity(),WholeSaleOrdersActivity.class);
                intent_allorder.putExtra("type",4);
                intent_allorder.putExtra("position",0);
                startActivity(intent_allorder);
                getActivity().finish();
                break;
            case R.id.layout_waitpay:
                Intent intent_waitpay=new Intent(getActivity(),WholeSaleOrdersActivity.class);
                intent_waitpay.putExtra("type",4);
                intent_waitpay.putExtra("position",1);
                startActivity(intent_waitpay);
                getActivity().finish();
                break;
            case R.id.layout_waitsend:
                Intent intent_waitsend=new Intent(getActivity(),WholeSaleOrdersActivity.class);
                intent_waitsend.putExtra("type",4);
                intent_waitsend.putExtra("position",2);
                startActivity(intent_waitsend);
                getActivity().finish();
                break;
            case R.id.layout_waitget:
                Intent intent_waitget=new Intent(getActivity(),WholeSaleOrdersActivity.class);
                intent_waitget.putExtra("type",4);
                intent_waitget.putExtra("position",3);
                startActivity(intent_waitget);
                getActivity().finish();
                break;
            case R.id.layout_outmoney:
                break;
            case R.id.layout_adress:
                Intent intent_adress=new Intent(getActivity(),SelectAdressActivity.class);
                startActivity(intent_adress);
                break;
            case R.id.layout_collect:
                break;
            case R.id.tv_phone:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CALL_PHONE},222);
                        return;
                    }else{
                        Intent intent_phone=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tv_phone.getText().toString().trim()));
                        startActivity(intent_phone);
                    }
                } else {
                    Intent intent_phone=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tv_phone.getText().toString().trim()));
                    startActivity(intent_phone);
                }
                break;

        }
    }
}
