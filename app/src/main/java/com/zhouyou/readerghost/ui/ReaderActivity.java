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

    private CurlView curlView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_marker).setOnClickListener(this);

        int index = 0;
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }
        curlView = (CurlView) findViewById(R.id.curl_view);
        curlView.setPageProvider(new PageProvider(this));
        curlView.setSizeChangedObserver(new SizeChangedObserver(curlView));
        curlView.setCurrentIndex(index);
        curlView.setBackgroundColor(0xFF202830);
        Book book = App.get().getBook();

//        Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());
//        Log.i("epublib", "title: " + book.getTitle());
//        logTableOfContents(book.getTableOfContents().getTocReferences(), 0);

    }

//    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
//        if (tocReferences == null) return;
//        for (TOCReference tocReference : tocReferences) {
//            StringBuilder tocString = new StringBuilder();
//            for (int i = 0; i < depth; i++) {
//                tocString.append("\t");
//            }
//            tocString.append(tocReference.getTitle());
//            Log.i("epublib", tocString.toString());
//
//            logTableOfContents(tocReference.getChildren(), depth + 1);
//        }
//    }


    @Override
    public Object onRetainNonConfigurationInstance() {
        return curlView.getCurrentIndex();
    }

    @Override
    public void onPause() {
        super.onPause();
        curlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        curlView.onResume();
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
