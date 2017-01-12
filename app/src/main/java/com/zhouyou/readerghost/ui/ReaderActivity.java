package com.zhouyou.readerghost.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.folioreader.activity.ContentHighlightActivity;
import com.folioreader.model.Highlight;
import com.folioreader.model.WebViewPosition;
import com.folioreader.smil.AudioElement;
import com.folioreader.smil.SmilFile;
import com.folioreader.smil.TextElement;
import com.folioreader.sqlite.DbAdapter;
import com.folioreader.util.AppUtil;
import com.folioreader.util.EpubManipulator;
import com.folioreader.util.FileUtil;
import com.folioreader.util.ProgressDialog;
import com.folioreader.view.AudioViewBottomSheetDailogFragment;
import com.folioreader.view.ConfigBottomSheetDialogFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.zhouyou.readerghost.R;
import com.zhouyou.readerghost.ui.adapter.EpubPagerAdapter;
import com.zhouyou.readerghost.ui.listener.OnFragmentCallback;
import com.zhouyou.readerghost.utils.FileUtils;
import com.zhouyou.readerghost.views.DirectionalViewPager;

import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;

import static com.folioreader.Constants.BOOK;
import static com.folioreader.Constants.CHAPTER_SELECTED;
import static com.folioreader.Constants.HIGHLIGHT_SELECTED;
import static com.folioreader.Constants.SELECTED_CHAPTER_POSITION;
import static com.folioreader.Constants.TYPE;

/**
 * Created by zhouyou on 17/1/7.
 */

public class ReaderActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentCallback {

    public static final String INTENT_EPUB_SOURCE_PATH = "com.folioreader.epub_asset_path";
    public static final String INTENT_EPUB_SOURCE_TYPE = "epub_source_type";
    public static final int ACTION_CONTENT_HIGHLIGHT = 77;
    private static final String HIGHLIGHT_ITEM = "highlight_item";
    public static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                saveBookState();
                finish();
                break;
            case R.id.iv_menu:
                Intent intent = new Intent(ReaderActivity.this, ContentHighlightActivity.class);
                mBook.setResources(null);
                mBook.setNcxResource(null);
                intent.putExtra(BOOK, mBook);
                int TOCposition = AppUtil.getTOCpos(mTocReferences, mSpineReferences.get(mChapterPosition));
                intent.putExtra(SELECTED_CHAPTER_POSITION, TOCposition);
                startActivityForResult(intent, ACTION_CONTENT_HIGHLIGHT);
                overridePendingTransition(com.folioreader.R.anim.slide_in_up, com.folioreader.R.anim.slide_out_up);
                break;
            case R.id.iv_config:
                break;
            default:
                break;
        }
    }

    public enum EpubSourceType {
        RAW,
        ASSESTS,
        SD_CARD
    }

    private DirectionalViewPager vp;
    private Toolbar toolbar;
    private TextView tvTitle;

    private EpubSourceType mEpubSourceType;
    private String mEpubFilePath;
    private String mEpubFileName;
    private int mEpubRawId;
    private Book mBook;
    private ArrayList<TOCReference> mTocReferences;
    private List<SpineReference> mSpineReferences;
    private List<AudioElement> mAudioElementArrayList;
    private List<TextElement> mTextElementList = new ArrayList<>();

    public boolean mIsActionBarVisible;
    public boolean mIsSmilParsed = false;
    private int mChapterPosition;
    private boolean mIsSmilAvailable;
    private EpubPagerAdapter epubPagerAdapter;
    private int mWebViewScrollPosition;
    private ConfigBottomSheetDialogFragment mConfigBottomSheetDialogFragment;
    private AudioViewBottomSheetDailogFragment mAudioBottomSheetDialogFragment;
    private boolean mIsbookOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        mEpubSourceType = (EpubSourceType) getIntent().getExtras().getSerializable(INTENT_EPUB_SOURCE_TYPE);
        if (mEpubSourceType.equals(EpubSourceType.RAW)) {
            mEpubRawId = getIntent().getExtras().getInt(INTENT_EPUB_SOURCE_PATH);
        } else {
            mEpubFilePath = getIntent().getExtras().getString(INTENT_EPUB_SOURCE_PATH);
        }

        mEpubFileName = FileUtils.getEpubFilename(this, mEpubSourceType, mEpubFilePath, mEpubRawId);
        vp = (DirectionalViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_config).setOnClickListener(this);
        initBook();
        BUS.register(this);
    }

    private void initBook() {
        final Dialog d = ProgressDialog.show(this, "正在加载中，请稍候");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBook = FileUtils.saveEpubFile(ReaderActivity.this, mEpubSourceType, mEpubFilePath,
                        mEpubRawId, mEpubFileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBook();
                        if (d.isShowing()) d.dismiss();
                    }
                });
            }
        }).start();
        new DbAdapter(this);
    }

    private void loadBook() {
        configRecyclerViews();
        configFolio();
        parseSmil();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_config).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        saveBookState();
        super.onBackPressed();
    }


//    @Override
//    public void onOrentationChange(int orentation) {
//        if (orentation == 0) {
//            vp.setDirection(DirectionalViewPager.Direction.VERTICAL);
//            if (mBook != null && mSpineReferences != null) {
//                mFolioPageFragmentAdapter =
//                        new FolioPageFragmentAdapter(getSupportFragmentManager(),
//                                mSpineReferences, mBook, mEpubFileName);
//                vp.setAdapter(mFolioPageFragmentAdapter);
//                vp.setOffscreenPageLimit(1);
//                vp.setCurrentItem(mChapterPosition);
//            }
//        } else {
//            vp.setDirection(DirectionalViewPager.Direction.HORIZONTAL);
//            if (mBook != null && mSpineReferences != null) {
//                mFolioPageFragmentAdapter =
//                        new FolioPageFragmentAdapter(getSupportFragmentManager(),
//                                mSpineReferences, mBook, mEpubFileName);
//                vp.setAdapter(mFolioPageFragmentAdapter);
//                vp.setCurrentItem(mChapterPosition);
//            }
//        }
//    }

    private Fragment getFragment(int pos) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + (pos));
    }


    public void configRecyclerViews() {
        mTocReferences = (ArrayList<TOCReference>) mBook.getTableOfContents().getTocReferences();
        mSpineReferences = mBook.getSpine().getSpineReferences();
        setSpineReferenceTitle();
    }

    public boolean setPagerToPosition(int audioPosition) {
        String src = mTextElementList.get(audioPosition).getSrc();
        String[] temp = src.split("#");
        String href = "text//" + temp[0];
        String currentHref = mSpineReferences.get(vp.getCurrentItem()).getResource().getHref();
        if (href.equalsIgnoreCase(currentHref)) {
            return false;
        } else {
            setPagerToPosition("text//" + temp[0]);
            return true;
        }
    }


    public void setPagerToPosition(String href) {
        for (int i = 0; i < mSpineReferences.size(); i++) {
            if (AppUtil.compareUrl(href, mSpineReferences.get(i).getResource().getHref())) {
                vp.setCurrentItem(i, true);
                toolbarAnimateHide();
                break;
            }
        }
    }

    private void configFolio() {
        vp.setOnPageChangeListener(new DirectionalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mChapterPosition = position;
                tvTitle.setText(mSpineReferences.get(position).getResource().getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mBook != null && mSpineReferences != null) {
            epubPagerAdapter = new EpubPagerAdapter(getSupportFragmentManager(), mSpineReferences, mBook, mEpubFileName);
            vp.setAdapter(epubPagerAdapter);
            if (AppUtil.checkPreviousBookStateExist(ReaderActivity.this, mBook)) {
                vp.setCurrentItem(AppUtil.getPreviousBookStatePosition(ReaderActivity.this, mBook));
            }
        }
    }

    private void setSpineReferenceTitle() {
        for (int j = 0; j < mSpineReferences.size(); j++) {
            String href = mSpineReferences.get(j).getResource().getHref();
            for (int i = 0; i < mTocReferences.size(); i++) {
                if (mTocReferences.get(i).getResource().getHref().equalsIgnoreCase(href)) {
                    mSpineReferences.get(j).getResource().setTitle(mTocReferences.get(i).getTitle());
                    break;
                } else {
                    mSpineReferences.get(j).getResource().setTitle("");
                }
            }
        }
        tvTitle.setText(mSpineReferences.get(0).getResource().getTitle());
    }

//    private void configDrawerLayoutButtons() {

//        findViewById(R.id.iv_config).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mConfigBottomSheetDialogFragment = new ConfigBottomSheetDialogFragment();
//                mConfigBottomSheetDialogFragment.show(getSupportFragmentManager(), mConfigBottomSheetDialogFragment.getTag());
//            }
//        });
//    }

    private void saveBookState() {
        AppUtil.saveBookState(ReaderActivity.this, mBook, vp.getCurrentItem(), mWebViewScrollPosition);
    }

    @Override
    public String getChapterHtmlContent(int position) {
        return readHTmlString(position);
    }

    @Override
    public void hideOrshowToolBar() {
        if (mIsActionBarVisible) {
            toolbarAnimateHide();
        } else {
            toolbarAnimateShow(1);
        }
    }

    @Override
    public void hideToolBarIfVisible() {
        if (mIsActionBarVisible) {
            toolbarAnimateHide();
        }
    }

    private String readHTmlString(int position) {
        String pageHref = mSpineReferences.get(position).getResource().getHref();
        String opfpath = AppUtil.getPathOPF(FileUtil.getFolioEpubFolderPath(mEpubFileName), ReaderActivity.this);
        if (AppUtil.checkOPFInRootDirectory(FileUtil.getFolioEpubFolderPath(mEpubFileName), ReaderActivity.this)) {
            pageHref = FileUtil.getFolioEpubFolderPath(mEpubFileName) + "/" + pageHref;
        } else {
            pageHref = FileUtil.getFolioEpubFolderPath(mEpubFileName) + "/" + opfpath + "/" + pageHref;
        }
        return EpubManipulator.readPage(pageHref);
    }

    private void toolbarAnimateShow(final int verticalOffset) {
        toolbar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarSetElevation(verticalOffset == 0 ? 0 : 1);
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsActionBarVisible) {
                            toolbarAnimateHide();
                        }
                    }
                });
            }
        }, 10000);

        mIsActionBarVisible = true;
    }

    private void toolbarAnimateHide() {
        toolbar.animate()
                .translationY(-toolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarSetElevation(0);
                    }
                });
        mIsActionBarVisible = false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(elevation);
        }
    }

    public Highlight setCurrentPagerPostion(Highlight highlight) {
        highlight.setCurrentPagerPostion(vp.getCurrentItem());
        return highlight;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_CONTENT_HIGHLIGHT && resultCode == RESULT_OK && data.hasExtra(TYPE)) {
            String type = data.getStringExtra(TYPE);
            if (type.equals(CHAPTER_SELECTED)) {
                mChapterPosition = data.getIntExtra(SELECTED_CHAPTER_POSITION, 0);
                int spineRefrencesPos = AppUtil.getSpineRefrecePos(mSpineReferences, mTocReferences.get(mChapterPosition));
                vp.setCurrentItem(spineRefrencesPos);
            } else if (type.equals(HIGHLIGHT_SELECTED)) {
                Highlight highlight = data.getParcelableExtra(HIGHLIGHT_ITEM);
                int position = highlight.getCurrentPagerPostion();
                vp.setCurrentItem(position);
                WebViewPosition webViewPosition = new WebViewPosition();
                webViewPosition.setWebviewPos(highlight.getCurrentWebviewScrollPos());
                BUS.post(webViewPosition);
            }
        }
    }

    private void parseSmil() {
        mIsSmilParsed = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmilFile smilFile = AppUtil.createSmilJson(ReaderActivity.this, mEpubFileName);
                if (smilFile != null) {
                    mAudioElementArrayList = smilFile.getAudioSegments();
                    mTextElementList = smilFile.getTextSegments();
                    mIsSmilAvailable = true;
                    BUS.post(mTextElementList);
                } else {
                    mIsSmilAvailable = false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsSmilParsed = true;
                    }
                });
            }


        }).start();

    }

    public AudioElement getElement(int position) {
        if (mAudioElementArrayList != null) {
            return mAudioElementArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioBottomSheetDialogFragment != null) {
            mAudioBottomSheetDialogFragment.unRegisterBus();
            mAudioBottomSheetDialogFragment.stopAudioIfPlaying();
            mAudioBottomSheetDialogFragment = null;
        }
    }

    @Override
    public void setLastWebViewPosition(int position) {
        mWebViewScrollPosition = position;
    }

    public String getEpubFileName() {
        return mEpubFileName;
    }

    public boolean isSmilAvailable() {
        return mIsSmilAvailable;
    }

    public int getmChapterPosition() {
        return mChapterPosition;
    }

    public boolean isbookOpened() {
        return mIsbookOpened;
    }

    public void setIsbookOpened(boolean mIsbookOpened) {
        this.mIsbookOpened = mIsbookOpened;
    }
}
