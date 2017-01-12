package com.zhouyou.readerghost.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.folioreader.activity.FolioActivity;
import com.zhouyou.readerghost.R;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.iv_read_entry).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_read_entry:
//                Intent intent = new Intent(this, FolioActivity.class);
//                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.ASSESTS);
//                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH, "epub/The Silver Chair.epub");
//                startActivity(intent);
                Intent intent = new Intent(this, ReaderActivity.class);
                intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_TYPE, ReaderActivity.EpubSourceType.ASSESTS);
                intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_PATH, "ghost_lantern.epub");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
