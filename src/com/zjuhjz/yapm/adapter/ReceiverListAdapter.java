package com.zjuhjz.yapm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zjuhjz.yapm.ProcessList;
import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.db.IntentInfoObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 13-6-20.
 */
public class ReceiverListAdapter extends BaseAdapter{
    final String TAG = ProcessList.TAG;

    ArrayList<IntentInfoObject> intentInfoObjects;
    LayoutInflater inflater;
    Context context;
    public  ReceiverListAdapter(Context context,ArrayList<IntentInfoObject> intentInfoObjects){
        super();
        this.intentInfoObjects = intentInfoObjects;
        this.context = context;
        inflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return intentInfoObjects.size();
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

        receiverName.setText(intentInfoObjects.get(i).componentName);
        receiverStatus.setChecked(intentInfoObjects.get(i).isEnable==1?true:false);
        receiverStatus.setOnCheckedChangeListener(new MyOnCheckedChangeListener(i) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                intentInfoObjects.get(index).isEnable = b?1:0;
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
