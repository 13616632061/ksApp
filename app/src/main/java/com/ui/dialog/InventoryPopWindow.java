package com.ui.dialog;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ui.entity.Goods_Inventory;
import com.ui.ks.R;
import com.ui.util.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lyf on 2020/8/11.
 */

public class InventoryPopWindow extends PopupWindow implements View.OnClickListener {

    TextView tvGoodsName;
    TextView tvGoodsCode;
    TextView tvStock;
    EditText edInventoryStock;
    Button btnAddInventory;

    private View view;
    private Context context;

    public InventoryPopWindow(Context context, Goods_Inventory goods_inventory) {
        super(context);
        this.context = context;
        view = View.inflate(context, R.layout.inventorygoods_dialog, null);
        initset();

        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsCode = view.findViewById(R.id.tv_goods_code);
        tvStock = view.findViewById(R.id.tv_stock);
        edInventoryStock = view.findViewById(R.id.ed_inventory_stock);
        btnAddInventory = view.findViewById(R.id.btn_add_inventory);

        tvGoodsName.setText(goods_inventory.getName());
        tvGoodsCode.setText(goods_inventory.getBncode());
        tvStock.setText(goods_inventory.getStore());
        edInventoryStock.setText(goods_inventory.getStore());
        edInventoryStock.setSelection(goods_inventory.getStore().length());
        edInventoryStock.setFocusable(true);
        edInventoryStock.setFocusableInTouchMode(true);
        edInventoryStock.requestFocus();
//        KeyboardUtils.showSoftInput((Activity) context);
        InputMethodManager  imm = (InputMethodManager) edInventoryStock.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        btnAddInventory.setOnClickListener(this);
    }

    private void initset() {
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置弹窗的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置弹窗的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置弹窗可点击
        this.setFocusable(true);
        //设置弹出的动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable mColorDrawable = new ColorDrawable(0x50000000);
        //设置弹框的背景
        this.setBackgroundDrawable(mColorDrawable);

        //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int yTop = view.findViewById(R.id.layout_root).getTop();
                int yBottom = view.findViewById(R.id.layout_root).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < yTop || y > yBottom) {
                        setDismiss();
                    }
                }
                return true;
            }
        });
    }


    /**
     * 设置盘点库存
     */
    public void setOnAddInventoryStock(OnAddInventoryStock onAddInventoryStock) {
        this.onAddInventoryStock = onAddInventoryStock;
    }

    public static OnAddInventoryStock onAddInventoryStock;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_inventory:
                if (onAddInventoryStock != null) {
                    onAddInventoryStock.addInventoryStock(edInventoryStock.getText().toString());
                }
                setDismiss();
                break;
        }
    }

    public interface OnAddInventoryStock {
        void addInventoryStock(String InventoryStock);
    }

    public void setDismiss(){
        if (isShowing()){
            KeyboardUtils.hideSoftInput(edInventoryStock);
//            InputMethodManager  imm = (InputMethodManager) edInventoryStock.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            dismiss();
        }

    }
}
