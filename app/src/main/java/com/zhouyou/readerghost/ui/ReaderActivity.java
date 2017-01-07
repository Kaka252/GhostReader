package com.zhouyou.readerghost.ui;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zhouyou.readerghost.R;

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

//        AssetManager assetManager = getAssets();
//        try {
//            InputStream epubInputStream = assetManager.open("ghost_lantern.epub");
//            Book book = (new EpubReader()).readEpub(epubInputStream);
//            Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());
//            Log.i("epublib", "title: " + book.getTitle());
//            Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());
//            logTableOfContents(book.getTableOfContents().getTocReferences(), 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) return;
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            Log.i("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
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
