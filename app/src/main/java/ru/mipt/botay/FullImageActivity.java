package ru.mipt.botay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends Activity {

    public Integer[] mThumbIds = {R.drawable.tm15_1, R.drawable.tm15_9, R.drawable.tm15_14,
            R.drawable.tm15_22, R.drawable.tm16_12, R.drawable.tm16_40,
            R.drawable.tm16_64, R.drawable.tm16_66,R.drawable.tm17_4, R.drawable.tm17_11_d,
            R.drawable.tm17_23,R.drawable.tm18_34, R.drawable.tm18_42, R.drawable.tm18_43_d
    };
    public Integer[] mColorIds = {0xffe53096, 0xffe53096, 0xffe53096,
            0xffe53096, 0xffe53096, 0xffe53096, 0xffe53096,
            0xffe53096, 0xffe53096, 0xffe53096, 0xffe53096,
            0xffe53096, 0xffe53096, 0xffe53096, 0xffe53096,
            0xffe53096, 0xffe53096, 0xffe53096
    };
    public String[] mThumbNames = {"15.1","15.9","15.14","15.22","16.12","16.40","16.64","16.66","17.4","17.11.d","17.23","18.34","18.42","18.43.d"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
     //   ImageTextAdapter imageTextAdapter = new ImageTextAdapter(this,mColorIds,mThumbNames);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageResource(mThumbIds[position]);
    }
}