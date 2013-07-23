package com.zjuhjz.yapm.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yapm.AutoStartInfo;
import com.zjuhjz.yapm.ProcessList;
import com.zjuhjz.yapm.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AutoStartAppListAdapter extends SimpleAdapter {
	private List<HashMap<String, Object>>  data;
	// HashMap<String,String> processInfo;

	@SuppressWarnings("unchecked")
	public AutoStartAppListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = (List<HashMap<String, Object>>)data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		ImageView imageView = (ImageView)view.findViewById(R.id.autostart_app_icon);
        TextView bootTextView = (TextView)view.findViewById(R.id.boottag);
        TextView autoTextView = (TextView)view.findViewById(R.id.autotag);
		imageView.setImageDrawable((Drawable)data.get(position).get("appIcon"));
        int bootIntent = (Integer) data.get(position).get("bootIntent");
        int autoIntent = (Integer) data.get(position).get("autoIntent");
        SpannableString bootText = new SpannableString("BOOT");
        SpannableString autoText = new SpannableString("AUTO");

        switch (bootIntent){
            case AutoStartInfo.ALL_DISABLED:
                bootText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                bootTextView.setBackgroundColor(android.R.attr.colorBackground);
                bootTextView.setTextColor(Color.parseColor("#000000"));
                break;
            case AutoStartInfo.PARTIALLY_DISABLED:
                bootText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                bootTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                bootTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case AutoStartInfo.ALl_ENABLED:
                bootTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                bootTextView.setTextColor(Color.parseColor("#FFFFFF"));
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
                autoText.setSpan(new StrikethroughSpan(),0,bootText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                autoTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                autoTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case AutoStartInfo.ALl_ENABLED:
                autoTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                autoTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case AutoStartInfo.NON_EXSISTS:
                autoTextView.setBackgroundColor(android.R.attr.colorBackground);
                autoTextView.setTextColor(android.R.attr.colorBackground);
                break;
        }
        autoTextView.setText(autoText);
		return view;
	}

}
