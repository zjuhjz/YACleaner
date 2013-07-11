package com.zjuhjz.yapm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zjuhjz.yapm.ProcessList;
import com.zjuhjz.yapm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 13-6-20.
 */
public class ReceiverListAdapter extends BaseAdapter{
    final String TAG = ProcessList.TAG;

    HashMap<String,Boolean> componentList;
    List<String> componentName;
    List<Boolean> componentStatus;
    LayoutInflater inflater;
    Context context;
    public  ReceiverListAdapter(Context context,HashMap<String,Boolean> componentList){
        super();
        this.componentList = componentList;
        this.context = context;
        componentName = new ArrayList<String>();
        componentStatus = new ArrayList<Boolean>();
        if (componentList!=null && componentList.size()>0){
            Map.Entry pairs ;
            for ( Iterator it =componentList.entrySet().iterator();it.hasNext();){
                pairs = (Map.Entry)it.next();
                componentName.add((String)pairs.getKey());
                componentStatus.add((Boolean)pairs.getValue());
            }
        }
        inflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return componentList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;
        if (mView==null){
            mView = inflater.inflate(R.layout.receiver_list_item,null);
        }
        TextView receiverName = (TextView)mView.findViewById(R.id.receiver_name);
        CheckBox receiverStatus = (CheckBox)mView.findViewById(R.id.receiver_checkbox);

        receiverName.setText(componentName.get(i));
        receiverStatus.setChecked(componentStatus.get(i));
        receiverStatus.setOnCheckedChangeListener(new MyOnCheckedChangeListener(i) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                componentStatus.set(index,b);
                componentList.put(componentName.get(index),componentStatus.get(index));
            }
        });
        return mView;
    }

    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        int index;
        public  MyOnCheckedChangeListener(int index){
            this.index = index;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        }
    }
}
