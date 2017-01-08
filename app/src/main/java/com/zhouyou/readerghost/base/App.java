package com.zhouyou.readerghost.base;

import android.app.Application;

import com.zhouyou.lib.utils.PoolUtils;
import com.zhouyou.readerghost.utils.EpubUtils;

import nl.siegmann.epublib.domain.Book;

/**
 * 作者：ZhouYou
 * 日期：2017/1/6.
 */
public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PoolUtils.POOL.submit(new Runnable() {
            @Override
            public void run() {
                book = EpubUtils.getEpubBook("ghost_lantern.epub");
            }
        });
    }

    private static Book book;

    public Book getBook() {
        return book;
    }
}
