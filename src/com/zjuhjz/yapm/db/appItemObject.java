package com.zjuhjz.yapm.db;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karl on 13-7-27.
 */
public class AppItemObject implements Parcelable{

    public String appName;
    public Drawable appIcon;
    public String packageName;
    public ArrayList<IntentInfoObject> intentInfoObjects;
    public int bootIntent;
    public int autoIntent;
    public AppItemObject(){
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(appIcon);
    }
}
