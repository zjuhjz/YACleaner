package com.zjuhjz.yacleaner;

//import com.example.android.supportv4.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class ProcessList extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_process_list);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_process_list, container, false);
        //View tv = v.findViewById(R.id.text);
        //((TextView)tv).setText("Fragment #" + mNum);
        //tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
        return v;
    }

	//@Override
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_process_list, menu);
		return true;
	}*/

}
