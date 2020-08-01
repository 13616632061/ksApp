package com.ui.util;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

/**
 * Created by eddie on 2016/11/21.
 */
public class TextSpeaker {
    private Context context;
    private TextToSpeech tts;

    public TextSpeaker(final Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS)
                {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(context, "Language is not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
