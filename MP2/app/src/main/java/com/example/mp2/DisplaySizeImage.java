package com.example.mp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplaySizeImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ImageView imageView = new ImageView(getApplicationContext());
        //get the image from the intent extra
        imageView.setImageResource(intent.getIntExtra("image", 0));

        setContentView(imageView);
    }

}
