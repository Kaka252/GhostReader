package com.zhouyou.readerghost.utils;

import android.content.res.AssetManager;

import com.zhouyou.readerghost.base.App;

import java.io.IOException;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by zhouyou on 17/1/8.
 */

public class EpubUtils {

    public static Book getEpubBook(String asset) {
        Book book = null;
        AssetManager assetManager = App.get().getAssets();
        try {
            InputStream epubInputStream = assetManager.open(asset);
            book = (new EpubReader()).readEpub(epubInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return book;
    }
}
