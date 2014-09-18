package com.best.nature.wallpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.View;

import com.best.nature.wallpaer.R;
import com.best.nature.wallpaper.utils.PhotoUtils;

public class MainActivity extends BaseActivity implements ActionBar.TabListener, CategoryListFragment.OnCategoryListItemClickListern{

	public ActionBar mActionBar;
	public static final int NUM_TABS = 4;
	public static final int RECENT_POSITION = 0;
	public static final int POPULAR_POSITION = 1;
	public static final int CATEGORY_POSITION = 2;
	public static final int SEARCH_POSITION = 3;
	
	public static final String RECENT = "Recent";
	public static final String POPULAR = "Popular";
	public static final String CATEGORY = "Categories";
	public static final String SEARCH = "Search";
	public static final String TAGNAME_IMAGE_GRID = "Image Grid";
	private Fragment[] mFragments = new Fragment[NUM_TABS];
	private Fragment mCategoryGridFragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set up Fragment Holder
		setContentView(R.layout.activity_main);
		mActionBar = getSupportActionBar();
		//Set Actionbar as Tab navigation
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Add Tabs
		mActionBar.addTab(mActionBar.newTab().setText(RECENT).setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText(POPULAR).setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText(CATEGORY).setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText(SEARCH).setTabListener(this));
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		int position = tab.getPosition();
		Fragment af = null;
		switch(position){
		case RECENT_POSITION:
			if(mFragments[position] == null){
				mFragments[position] = new ImageGridFragment();
				Bundle args = new Bundle();
				args.putString(ImageGridFragment.URL_KEY, PhotoUtils.RECENT_URL);
				mFragments[position].setArguments(args);
				ft.add(R.id.fragment_container, mFragments[position], RECENT);
			}
			af = mFragments[position];
			break;
		case POPULAR_POSITION:
			if(mFragments[position] == null){
				mFragments[position] = new ImageGridFragment();
				Bundle args = new Bundle();
				args.putString(ImageGridFragment.URL_KEY, PhotoUtils.getPopularUrl());
				mFragments[position].setArguments(args);
				ft.add(R.id.fragment_container, mFragments[position], POPULAR);
			}
			af = mFragments[position];
			break;
		case CATEGORY_POSITION:
			if(mFragments[position] == null){
				mFragments[position] = new CategoryListFragment();
				ft.add(R.id.fragment_container, mFragments[position], CATEGORY);
				af = mFragments[position];
			}
			else if(mCategoryGridFragment != null)
				af = mCategoryGridFragment;
			else af = mFragments[position];
			break;
		case SEARCH_POSITION:
			if(mFragments[position] == null){
				Bundle args = new Bundle();
				mFragments[position] = new SearchImageGridFragment();
				mFragments[position].setArguments(args);
				ft.add(R.id.fragment_container, mFragments[position], SEARCH);
			}
			af = mFragments[position];
			break;
			default:
				return;
		
		}
		ft.attach(af);
		
	}


	@Override
	public void onBackPressed() {
		// if current tab is category tab and it is showing image on ImageGridFragment then back to CategoryListFragment
		Tab tab = getSupportActionBar().getSelectedTab();
		if(tab.getPosition() == CATEGORY_POSITION && mCategoryGridFragment != null){
			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().detach(mCategoryGridFragment)
			.attach(mFragments[CATEGORY_POSITION])
			.commit();
			mCategoryGridFragment = null;
			return;
		}
		super.onBackPressed();
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//Remove Fragment from fragment holder
		if(tab.getPosition() == CATEGORY_POSITION && mCategoryGridFragment != null)
			ft.detach(mCategoryGridFragment);
		
		ft.detach(mFragments[tab.getPosition()]);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Hide or show search view on Search tabl
		if(tab.getPosition() == SEARCH_POSITION && mFragments[SEARCH_POSITION] != null)
			((SearchImageGridFragment)mFragments[SEARCH_POSITION]).handleClose();
		
	}

	@Override
	
	public void onCategoryListItemClick(View v) {
		String txt = (String) v.getTag();
		
		
	        
	   //New Item has been clicked so show image thumbs on with ImageGridFragment
		Bundle args = new Bundle();
		if(txt.equals("Nature"))
			args.putString(ImageGridFragment.URL_KEY, PhotoUtils.getGroupUrl("24303550@N00"));
		else
			args.putString(ImageGridFragment.URL_KEY, PhotoUtils.getURLByText(txt));
		ImageGridFragment fragment = new ImageGridFragment();
		fragment.setArguments(args);
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().detach(mFragments[CATEGORY_POSITION]).commit();
		fm.beginTransaction().add(R.id.fragment_container, fragment, txt)
		.commit();
		
		mCategoryGridFragment = fragment;
	}
	public interface HandleSearchCloseListener{
		public abstract void handleClose();
	}
	
}
