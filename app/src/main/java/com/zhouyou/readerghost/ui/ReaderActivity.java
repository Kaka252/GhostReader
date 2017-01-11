package com.zhouyou.readerghost.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zhouyou.readerghost.R;
import com.zhouyou.readerghost.base.App;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;

/**
 * Created by zhouyou on 17/1/7.
 */

public class ReaderActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ReaderActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_marker).setOnClickListener(this);
        Book book = App.get().getBook();


        Resource resource = book.getContents().get(0);
        String href = resource.getHref();
        Log.d(TAG, href);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_menu:
                break;
            case R.id.iv_marker:
                break;
            default:
                break;
        }
    }
}
