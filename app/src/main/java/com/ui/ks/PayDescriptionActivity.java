package com.ui.ks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class PayDescriptionActivity extends AppCompatActivity {

    private RelativeLayout konw_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_description);
        konw_layout= (RelativeLayout) findViewById(R.id.konw_layout);
        konw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
