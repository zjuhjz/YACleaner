package com.zjuhjz.yapm.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yapm.AutoStartInfo;
import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.db.AppItemObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoStartAppListAdapter extends BaseAdapter {
	private List<AppItemObject>  data;
    LayoutInflater inflater;
	// HashMap<String,String> processInfo;

	@SuppressWarnings("unchecked")
	public AutoStartAppListAdapter(Context context,List<AppItemObject> appItemObjects
			) {
		super();
        inflater = LayoutInflater.from(context);
        data = appItemObjects;
	}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        if (mView==null){
            mView = inflater.inflate(R.layout.autostart_app_list_item,null);
        }
		ImageView imageView = (ImageView)mView.findViewById(R.id.autostart_app_icon);
        TextView bootTextView = (TextView)mView.findViewById(R.id.boottag);
        TextView autoTextView = (TextView)mView.findViewById(R.id.autotag);
        TextView appNameTextView = (TextView)mView.findViewById(R.id.autostart_app_name);
		imageView.setImageDrawable(data.get(position).appIcon);
        appNameTextView.setText(data.get(position).appName);
        int bootIntent =  data.get(position).bootIntent;
        int autoIntent =  data.get(position).autoIntent;
        SpannableString bootText = new SpannableString("BOOT");
        SpannableString autoText = new SpannableString("AUTO");

        switch (bootIntent){
            case AutoStartInfo.ALL_DISABLED:
                bootText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                bootTextView.setBackgroundColor(android.R.attr.colorBackground);
                bootTextView.setTextColor(Color.parseColor("#000000"));
                break;
            case AutoStartInfo.PARTIALLY_DISABLED:
                //bootText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //bootTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                //bootTextView.setTextColor(Color.parseColor("#FFFFFF"));
                //break;
            case AutoStartInfo.ALl_ENABLED:
                bootTextView.setBackgroundColor(android.R.attr.colorBackground);
                bootTextView.setTextColor(Color.parseColor("#000000"));
                break;
            case AutoStartInfo.NON_EXSISTS:
                bootTextView.setBackgroundColor(android.R.attr.colorBackground);
                bootTextView.setTextColor(android.R.attr.colorBackground);
                break;
        }
        bootTextView.setText(bootText);

        switch (autoIntent){
            case AutoStartInfo.ALL_DISABLED:
                autoText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                autoTextView.setBackgroundColor(android.R.attr.colorBackground);
                autoTextView.setTextColor(Color.parseColor("#000000"));

                break;
            case AutoStartInfo.PARTIALLY_DISABLED:
                //autoText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //autoTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                //autoTextView.setTextColor(Color.parseColor("#FFFFFF"));
                //break;
            case AutoStartInfo.ALl_ENABLED:
                autoTextView.setBackgroundColor(android.R.attr.colorBackground);
                autoTextView.setTextColor(Color.parseColor("#000000"));
                break;
            case AutoStartInfo.NON_EXSISTS:
                autoTextView.setBackgroundColor(android.R.attr.colorBackground);
                autoTextView.setTextColor(android.R.attr.colorBackground);
                break;
        }
        autoTextView.setText(autoText);
		return mView;
	}

}
