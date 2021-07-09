package com.borf.bookman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.borf.bookman.R;
import com.borf.bookman.base.BaseActivity;

public class BookInfoActivity extends BaseActivity {

    private String isbn;

    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");
        if (TextUtils.isEmpty(isbn)) {
            ToastUtils.showLong("无法获取 isbn ！！！");
            finish();
        }

    }
}