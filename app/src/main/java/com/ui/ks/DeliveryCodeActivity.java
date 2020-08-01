package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.DeleteEditText;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;


public class DeliveryCodeActivity extends BaseActivity {
    TextView textView1;
    EditText textView3;
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_code);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if(bundle.containsKey("code")) {
                code = bundle.getString("code");
            }
        }

        textView1 = (TextView) findViewById(R.id.textView1);
        textView3 = (EditText) findViewById(R.id.textView3);    //快递单号
        new DeleteEditText(textView3, textView1);
        textView3.setText(code);

        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textView3.getText().toString();

                if(StringUtils.isEmpty(username)) {
                    SysUtils.showError("快递单号不能为空");
                } else {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("corp_code", username);
                    returnIntent.putExtras(bundle);
                    setResult(RESULT_OK, returnIntent);

                    onBackPressed();
                }
            }
        });
    }
}
