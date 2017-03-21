package com.example.ravimathpal.directorysearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        String url = getIntent().getStringExtra("ImagePath");
        Glide.with(this)
                .load(url)
                .crossFade()
                .into(imageView);

    }
}
