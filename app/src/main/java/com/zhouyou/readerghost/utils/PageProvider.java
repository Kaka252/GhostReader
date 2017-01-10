package com.zhouyou.readerghost.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.zhouyou.lib.flipper.CurlPage;
import com.zhouyou.lib.flipper.CurlView;
import com.zhouyou.lib.utils.Scale;

/**
 * 作者：ZhouYou
 * 日期：2017/1/10.
 */
public class PageProvider implements CurlView.PageProvider {

    private Paint bgPaint;
    private TextPaint textPaint;

    private Context context;
    private String text = "sfdsafasfsafdsfasdfasfsfdasdfdsafasdfasdfsafasfasdfsafdsafdasfdsafsadfasfdafdsfasfasdfasfdsafdfasdfsfsadfasdf";

    public PageProvider(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.TRANSPARENT);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
    }

    @Override
    public int getPageCount() {
        return 5;
    }

    @Override
    public void updatePage(CurlPage page, int width, int height, int index) {
        switch (index) {
            // First case is image on front side, solid colored back.
            case 0: {
                Bitmap front = loadBitmap(width, height, 0);
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK);
                break;
            }
            // Second case is image on back side, solid colored front.
            case 1: {
                Bitmap back = loadBitmap(width, height, 2);
                page.setTexture(back, CurlPage.SIDE_BACK);
                page.setColor(Color.rgb(127, 140, 180), CurlPage.SIDE_FRONT);
                break;
            }
            // Third case is images on both sides.
            case 2: {
                Bitmap front = loadBitmap(width, height, 1);
                Bitmap back = loadBitmap(width, height, 3);
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setTexture(back, CurlPage.SIDE_BACK);
                break;
            }
            // Fourth case is images on both sides - plus they are blend against
            // separate colors.
            case 3: {
                Bitmap front = loadBitmap(width, height, 2);
                Bitmap back = loadBitmap(width, height, 1);
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setTexture(back, CurlPage.SIDE_BACK);
                page.setColor(Color.argb(127, 170, 130, 255),
                        CurlPage.SIDE_FRONT);
                page.setColor(Color.rgb(255, 190, 150), CurlPage.SIDE_BACK);
                break;
            }
            // Fifth case is same image is assigned to front and back. In this
            // scenario only one texture is used and shared for both sides.
            case 4:
                Bitmap front = loadBitmap(width, height, 0);
                page.setTexture(front, CurlPage.SIDE_BOTH);
                page.setColor(Color.argb(127, 255, 255, 255),
                        CurlPage.SIDE_BACK);
                break;
        }
    }

    private Bitmap loadBitmap(int width, int height, int index) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        b.eraseColor(0xFFFFFFFF);
        Canvas c = new Canvas(b);
//        Drawable d = new BitmapDrawable()
//        Drawable d = context.getResources().getDrawable(mBitmapIds[index]);

        int margin = 7;
        int border = 3;
        Rect r = new Rect(0, 0, Scale.getDisplayWidth(context), Scale.getDisplayHeight(context));
//        Rect r = new Rect(margin, margin, width - margin, height - margin);
//
//        int imageWidth = r.width() - (border * 2);
//        int imageHeight = imageWidth * d.getIntrinsicHeight()
//                / d.getIntrinsicWidth();
//        if (imageHeight > r.height() - (border * 2)) {
//            imageHeight = r.height() - (border * 2);
//            imageWidth = imageHeight * d.getIntrinsicWidth()
//                    / d.getIntrinsicHeight();
//        }
//
//        r.left += ((r.width() - imageWidth) / 2) - border;
//        r.right = r.left + imageWidth + border + border;
//        r.top += ((r.height() - imageHeight) / 2) - border;
//        r.bottom = r.top + imageHeight + border + border;
//
//        Paint p = new Paint();
//        p.setColor(Color.BLACK);
        c.drawRect(r, bgPaint);
        r.left += border;
        r.right -= border;
        r.top += border;
        r.bottom -= border;

        c.drawText(text, 0, 0, textPaint);
//        d.setBounds(r);
//        d.draw(c);

        return b;
    }
}
