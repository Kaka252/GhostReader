package com.zhouyou.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhouyou on 17/1/8.
 */

public class ContentBean implements Serializable, Parcelable {

    public String title;
    public String href;
    public int size;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.href);
        dest.writeInt(this.size);
    }

    public ContentBean() {
    }

    protected ContentBean(Parcel in) {
        this.title = in.readString();
        this.href = in.readString();
        this.size = in.readInt();
    }

    public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
        @Override
        public ContentBean createFromParcel(Parcel source) {
            return new ContentBean(source);
        }

        @Override
        public ContentBean[] newArray(int size) {
            return new ContentBean[size];
        }
    };
}
