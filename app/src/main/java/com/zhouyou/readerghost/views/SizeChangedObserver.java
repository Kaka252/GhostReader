package com.zhouyou.readerghost.views;

import com.zhouyou.lib.flipper.CurlView;

/**
 * 作者：ZhouYou
 * 日期：2017/1/10.
 */
public class SizeChangedObserver implements CurlView.SizeChangedObserver {

    private CurlView curlView;

    public SizeChangedObserver(CurlView curlView) {
        this.curlView = curlView;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        if (w > h) {
            curlView.setViewMode(CurlView.SHOW_TWO_PAGES);
            curlView.setMargins(.1f, .05f, .1f, .05f);
        } else {
            curlView.setViewMode(CurlView.SHOW_ONE_PAGE);
            curlView.setMargins(.1f, .1f, .1f, .1f);
        }
    }
}
