package com.zhouyou.readerghost.ui.listener;

/**
 * 作者：ZhouYou
 * 日期：2017/1/12.
 */
public interface OnFragmentCallback {
    String getChapterHtmlContent(int position);

    void hideOrshowToolBar();

    void hideToolBarIfVisible();

    void setPagerToPosition(String href);

    void setLastWebViewPosition(int position);
}
