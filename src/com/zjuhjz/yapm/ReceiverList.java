package com.zjuhjz.yapm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.zjuhjz.yapm.adapter.ReceiverListAdapter;

import java.util.HashMap;

public class ReceiverList extends ListActivity {
    HashMap<String,Boolean> mComponentList = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_list);
        Intent intent = getIntent();
        mComponentList= (HashMap<String,Boolean>)intent.getSerializableExtra("ComponentList");
        ReceiverListAdapter receiverListAdapter = new ReceiverListAdapter(this,mComponentList);
        setListAdapter(receiverListAdapter);
    }
}