package com.lixinyang.banner_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author:Created by WangZhiQiang on 2017/11/15.
 */

public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ImageView iv_list2 = (ImageView) findViewById(R.id.iv_list2);
        TextView tv_list2_1 = (TextView) findViewById(R.id.tv_list2_1);
        TextView tv_list2_2 = (TextView) findViewById(R.id.tv_list2_2);
        TextView tv_list2_3 = (TextView) findViewById(R.id.tv_list2_3);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String id = intent.getStringExtra("id");
        String image = intent.getStringExtra("image");
        String summary = intent.getStringExtra("summary");
        ImageLoader instance = ImageLoader.getInstance();
        tv_list2_1.setText(title);
        tv_list2_2.setText(id);
        tv_list2_3.setText(summary);
        instance.displayImage(image,iv_list2);
    }
}
