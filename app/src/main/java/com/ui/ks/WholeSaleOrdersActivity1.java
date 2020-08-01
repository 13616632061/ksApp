package com.ui.ks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.db.DBHelper;
import com.ui.util.SysUtils;

import java.util.ArrayList;
import java.util.Collections;

public class WholeSaleOrdersActivity1 extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_shopping_store,tv_myperson,tv_searchshopping;
    private RelativeLayout layout_search;
    private EditText et_input;
    private LinearLayout layout_historyrecord;
    private ArrayList<String> historyrecord_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_sale_orders1);
        SysUtils.setupUI(WholeSaleOrdersActivity1.this,findViewById(R.id.activity_whole_sale_orders1));
        initToolbar(this);
        setTintColor(5);
        initView();
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        tv_shopping_store= (TextView) findViewById(R.id.tv_shopping_store);
        tv_myperson= (TextView) findViewById(R.id.tv_myperson);
        tv_searchshopping= (TextView) findViewById(R.id.tv_searchshopping);
        layout_search= (RelativeLayout) findViewById(R.id.layout_search);
        et_input= (EditText) findViewById(R.id.et_input);
        layout_historyrecord= (LinearLayout) findViewById(R.id.layout_historyrecord);

        et_input.setInputType(InputType.TYPE_NULL);
        iv_back.setOnClickListener(this);
        tv_myperson.setOnClickListener(this);
        tv_shopping_store.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        tv_searchshopping.setOnClickListener(this);
        et_input.setOnClickListener(this);

        getSearchHistoryRecord();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_myperson:
                Intent intent=new Intent(WholeSaleOrdersActivity1.this,WholeSaleOrdersActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.tv_shopping_store:
                Intent intent1=new Intent(WholeSaleOrdersActivity1.this,WholeSaleOrdersActivity.class);
                intent1.putExtra("type",1);
                startActivity(intent1);
                break;
            case R.id.et_input:
                Intent intent2=new Intent(WholeSaleOrdersActivity1.this,ShoppingHomePageSearchActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_searchshopping:
                Intent intent3=new Intent(WholeSaleOrdersActivity1.this,ShoppingHomePageSearchActivity.class);
                startActivity(intent3);
                break;
        }
    }

    /**
     * 搜索记录横向滚动view
     * @param i
     */
    private void showHistoryRecord(final int i){
        View view=View.inflate(WholeSaleOrdersActivity1.this,R.layout.item_shopper_searchhistryrecord,null);
        TextView tv_searchhistoryrecord= (TextView) view.findViewById(R.id.tv_searchhistoryrecord);
        tv_searchhistoryrecord.setText(historyrecord_list.get(i));
        tv_searchhistoryrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WholeSaleOrdersActivity1.this,ShoppingHomePageSearchActivity.class);
                intent.putExtra("searchhistoryrecord",historyrecord_list.get(i));
                startActivity(intent);
            }
        });

        layout_historyrecord.addView(view);

    }
    /**
     * 读取搜索历史记录
     */
    private void getSearchHistoryRecord(){
        if(historyrecord_list!=null){
            historyrecord_list.clear();
        }
        historyrecord_list=new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance(WholeSaleOrdersActivity1.this);
        SQLiteDatabase sqlite= dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM searchhistoryrecords";
        Cursor cursor = sqlite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                historyrecord_list.add(name);
            } while (cursor.moveToNext());
        }
        Collections.reverse(historyrecord_list);
        for (int i=0;i<historyrecord_list.size();i++){
            showHistoryRecord(i);
        }
        cursor.close();
        sqlite.close();
    }
}
