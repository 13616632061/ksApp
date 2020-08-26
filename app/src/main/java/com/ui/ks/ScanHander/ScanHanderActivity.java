package com.ui.ks.ScanHander;

import android.content.Intent;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.constant.RouterPath;
import com.google.zxing.Result;
import com.ui.ks.R;

/**
 * Created by lyf on 2020/8/24.
 */
@Route(path = RouterPath.ACTIVITY_SCAN_HANDER)
public class ScanHanderActivity extends BaseScanHanderActivity {

    @Override
    public int setContentView() {
        return R.layout.activity_scan_hander;
    }

    @Override
    protected void initView() {
        super.initView();
        initTabTitle(getString(R.string.str427), "");
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        Intent intent = new Intent();
        intent.putExtra("result", result.getText());
        setResult(200, intent);
        finish();
    }
}
