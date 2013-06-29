package com.zjuhjz.yapm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.zjuhjz.yapm.adapter.ReceiverListAdapter;

import java.util.HashMap;

public class ReceiverList extends ListActivity {
    HashMap<String,Boolean> mComponentList = null;
    HashMap<String,Boolean> mComponentListClone = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_list);
        Intent intent = getIntent();
        mComponentList= (HashMap<String,Boolean>)intent.getSerializableExtra("ComponentList");
        mComponentListClone = (HashMap<String,Boolean>)mComponentList.clone();
        ReceiverListAdapter receiverListAdapter = new ReceiverListAdapter(this,mComponentList);
        setListAdapter(receiverListAdapter);
    }

    public void setState(){
        if(mComponentList==null){
            return;
        }
        ListView listView = getListView();

    }


}