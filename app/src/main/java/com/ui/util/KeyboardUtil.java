//package com.ms.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.inputmethodservice.Keyboard;
//import android.inputmethodservice.KeyboardView;
//import android.text.Editable;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.material.widget.Switch;
//import com.ms.ks.R;
//
///**自定义键盘
// * Created by Administrator on 2016/12/22.
// */
//
//public class KeyboardUtil {
//
//    private Context ctx;
//    private Activity act;
//    private KeyboardView keyboardView;
//    private Keyboard k2;// 数字键盘
//
//    private EditText ed;
//    private EditText tv;
//
//    public KeyboardUtil(Activity act, Context ctx, EditText edit,EditText tv) {
//        this.act = act;
//        this.ctx = ctx;
//        this.ed = edit;
//        this.tv=tv;
//        k2 = new Keyboard(ctx, R.xml.symbols);
//        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
//        keyboardView.setKeyboard(k2);
//        keyboardView.setEnabled(true);
//        keyboardView.setPreviewEnabled(true);
//        keyboardView.setOnKeyboardActionListener(listener);
//    }
//
//    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
//        @Override
//        public void swipeUp() {
//        }
//
//        @Override
//        public void swipeRight() {
//        }
//
//        @Override
//        public void swipeLeft() {
//        }
//
//        @Override
//        public void swipeDown() {
//        }
//
//        @Override
//        public void onText(CharSequence text) {
//        }
//
//        @Override
//        public void onRelease(int primaryCode) {
//        }
//
//        @Override
//        public void onPress(int primaryCode) {
//        }
//
//        @Override
//        public void onKey(int primaryCode, int[] keyCodes) {
//            Editable editable = ed.getText();
//            Editable tvable= tv.getText();
//            int start = ed.getSelectionStart();
//            int startv = tv.getSelectionStart();
//            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
//                if (tvable != null && tvable.length() > 0) {
//                    if (start > 0) {
//                        tvable.delete(startv - 1, startv);
//                    }
//                }
//            } else {
//                tvable.insert(startv, Character.toString((char) primaryCode));
//            }
//        }
//    };
//
//
//
//    public void showKeyboard() {
//        int visibility = keyboardView.getVisibility();
//        if (visibility == View.GONE || visibility == View.INVISIBLE) {
//            keyboardView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void hideKeyboard() {
//        int visibility = keyboardView.getVisibility();
//        if (visibility == View.VISIBLE) {
//            keyboardView.setVisibility(View.INVISIBLE);
//        }
//    }
//
//}
