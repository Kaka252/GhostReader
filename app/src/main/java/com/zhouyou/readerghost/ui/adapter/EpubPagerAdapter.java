package com.zhouyou.readerghost.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.folioreader.smil.TextElement;
import com.squareup.otto.Subscribe;
import com.zhouyou.readerghost.ui.ReaderActivity;
import com.zhouyou.readerghost.ui.fragment.EpubReadFragment;

import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;

/**
 * 作者：ZhouYou
 * 日期：2017/1/12.
 */
public class EpubPagerAdapter extends FragmentStatePagerAdapter {

    private List<SpineReference> mSpineReferences;
    private Book mBook;
    private String mEpubFileName;
    private EpubReadFragment f;
    private ArrayList<TextElement> mTextElementArrayList;
    private boolean mIsSmileAvailable;

    public EpubPagerAdapter(FragmentManager fm, List<SpineReference> spineReferences, Book book, String epubFilename) {
        super(fm);
        this.mSpineReferences = spineReferences;
        this.mBook = book;
        this.mEpubFileName = epubFilename;
        ReaderActivity.BUS.register(this);
    }

    @Override
    public Fragment getItem(int position) {
        f = EpubReadFragment.newInstance(position, mBook, mEpubFileName, mTextElementArrayList, mIsSmileAvailable);
        f.setFragmentPos(position);
        return f;
    }

    @Override
    public int getCount(){return mSpineReferences.size(); }

    @Subscribe
    public void setTextElementList(ArrayList<TextElement> textElementList) {
        if (textElementList != null && textElementList.size() > 0) {
            mIsSmileAvailable = true;
            mTextElementArrayList = textElementList;
        }
    }
}
