package com.zhouyou.readerghost.ui;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zhouyou.lib.flipper.CurlView;
import com.zhouyou.readerghost.R;
import com.zhouyou.readerghost.base.App;
import com.zhouyou.readerghost.utils.PageProvider;
import com.zhouyou.readerghost.views.SizeChangedObserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by zhouyou on 17/1/7.
 */

public class ReaderActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_marker).setOnClickListener(this);
        Book book = App.get().getBook();
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
