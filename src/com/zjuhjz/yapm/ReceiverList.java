package com.zjuhjz.yapm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zjuhjz.yapm.adapter.ReceiverListAdapter;
import com.zjuhjz.yapm.db.AppItemObject;
import com.zjuhjz.yapm.db.IntentInfoObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiverList extends ListActivity {
    //HashMap<String,Boolean> mComponentList = null;
    HashMap<String,Boolean> mComponentListClone = null;
    ArrayList<IntentInfoObject> intentInfoObjects;
    String packageName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_list);
        Intent intent = getIntent();
        intentInfoObjects = intent.getParcelableArrayListExtra("IntentInfoObjects");
        packageName = intent.getStringExtra("PackageName");
        ReceiverListAdapter receiverListAdapter = new ReceiverListAdapter(this,intentInfoObjects);
        setListAdapter(receiverListAdapter);
    }

    public void confirm(View view){
        if(intentInfoObjects==null){
            return;
        }
        Intent data = getIntent();
        data.putParcelableArrayListExtra("IntentInfoObjects",intentInfoObjects);
        data.putExtra("PackageName",packageName);
        setResult(1, data);
        finish();
    }

    public void cancel(View view){
        setResult(0);
        finish();
    }

}