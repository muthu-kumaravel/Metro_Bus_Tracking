package com.example.mtc_bus;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.example.hospitalmap.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomAdapter1 extends BaseAdapter
{

	private Context context;
	private  List<ViewItem> rowItems;
	private int resource;
	private View view;
	
     public CustomAdapter1(Context context, int resource, List<ViewItem> rowItems) 
     {
	  this.context = context;
	  this.resource = resource;
	  this.rowItems = rowItems;
	  }
	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) 
	{
		 return rowItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		 return rowItems.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stubViewHolder holder = null;
		
		  if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater) context
	                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.list_view, null);
	        }
				TextView task1= (TextView) convertView.findViewById(R.id.taskperform);
				
				
				ViewItem row_pos = rowItems.get(position);
//				
			task1.setText(row_pos.gettask());
			
		return convertView;
	}
	public  class ViewHolder {
		TextView task;
		
	}

}
