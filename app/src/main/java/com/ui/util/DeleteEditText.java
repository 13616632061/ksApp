package com.ui.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DeleteEditText {
    private EditText eText;
    private TextView tView;

    public DeleteEditText(EditText editText, TextView textView) {
        this.eText = editText;
        this.tView = textView;

        this.eText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    tView.setVisibility(View.VISIBLE);
                } else {
                    tView.setVisibility(View.GONE);
                }
            }
        });

        this.tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eText.setText("");
            }
        });
    }
}
