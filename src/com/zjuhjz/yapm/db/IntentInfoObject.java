package com.zjuhjz.yapm.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Karl on 13-7-27.
 */
public class IntentInfoObject implements Parcelable {
    public String componentName;
    public String packageName;
    public String intentName;
    public int isSystem;
    public int enableState;
    public int defaultEnabled;
    public int isEnable;
    public  IntentInfoObject(){
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IntentInfoObject> CREATOR = new Creator<IntentInfoObject>() {
        @Override
        public IntentInfoObject createFromParcel(Parcel parcel) {
            IntentInfoObject intentInfoObject = new IntentInfoObject();
            intentInfoObject.componentName = parcel.readString();
            intentInfoObject.packageName = parcel.readString();
            intentInfoObject.intentName = parcel.readString();
            intentInfoObject.isSystem = parcel.readInt();
            intentInfoObject.enableState = parcel.readInt();
            intentInfoObject.defaultEnabled = parcel.readInt();
            intentInfoObject.isEnable = parcel.readInt();
            return intentInfoObject;
        }

        @Override
        public IntentInfoObject[] newArray(int i) {
            return new IntentInfoObject[i];
        }
    };


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(componentName);
        parcel.writeString(packageName);
        parcel.writeString(intentName);
        parcel.writeInt(isSystem);
        parcel.writeInt(enableState);
        parcel.writeInt(defaultEnabled);
        parcel.writeInt(isEnable);
    }
}
