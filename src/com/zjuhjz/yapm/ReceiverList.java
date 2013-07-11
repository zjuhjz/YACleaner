package com.zjuhjz.yapm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zjuhjz.yapm.adapter.ReceiverListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiverList extends ListActivity {
    HashMap<String,Boolean> mComponentList = null;
    HashMap<String,Boolean> mComponentListClone = null;
    String packageName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_list);
        Intent intent = getIntent();
        mComponentList= (HashMap<String,Boolean>)intent.getSerializableExtra("ComponentList");
        packageName = intent.getStringExtra("PackageName");
        mComponentListClone =(HashMap<String,Boolean>)mComponentList.clone();
        ReceiverListAdapter receiverListAdapter = new ReceiverListAdapter(this,mComponentListClone);
        setListAdapter(receiverListAdapter);
    }

    public void confirm(View view){
        if(mComponentList==null){
            return;
        }
        Intent data = getIntent();
        data.putExtra("ComponentList",mComponentListClone);
        data.putExtra("PackageName",packageName);
        setResult(1, data);
        finish();
    }

    public void cancel(View view){
        finish();
    }

}