package com.best.nature.wallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.best.nature.wallpaer.R;

public class CategoryListFragment extends ListFragment {
	private static final String TAG = CategoryListFragment.class.getSimpleName();
	private Context mContext = null;
	private String[] mCategories = null;
	private CategoryListAdapter mAdapter = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mContext = getActivity();
		mCategories = getActivity().getResources().getStringArray(R.array.category_list);
		mAdapter = new CategoryListAdapter();
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		((OnCategoryListItemClickListern)getActivity()).onCategoryListItemClick(v);
				
	}
	private class CategoryListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return mCategories.length;
		}

		@Override
		public Object getItem(int position) {
			return mCategories[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view == null) 
				view = View.inflate(mContext, R.layout.category_list_item, null);
			((TextView)(view.findViewById(R.id.category_name))).setText(mCategories[position]);
			((ImageView)(view.findViewById(R.id.category_image))).setImageResource(R.drawable.ic_launcher);
			view.setTag(mCategories[position]);
		return view;	
		}
		
	}
	public static interface  OnCategoryListItemClickListern{
		public  abstract void onCategoryListItemClick(View v);
			
		
		
	}
}
