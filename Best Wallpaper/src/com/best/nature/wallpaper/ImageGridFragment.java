package com.best.nature.wallpaper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.best.nature.wallpaer.R;
import com.best.nature.wallpaper.utils.Photo;
import com.best.nature.wallpaper.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageGridFragment extends Fragment implements OnScrollListener, OnItemClickListener {

	public static final String URL_KEY = "url";
	private static final String TAG = ImageGridFragment.class.getSimpleName();
	public static final int THUMB_HEIGHT = 150;
	public static final int THUMB_WIDTH = 150;
	public static final int SPACE = 1;
	private static final int THRESHOLD = 8;
	protected String mUrl = null;
	protected GridView mGridView = null;
	private ImageGridViewAdapter mGridViewAdapter = null;
	private Context mContext = null;
	protected ArrayList<Photo> mPhotos = null;
	protected boolean mLoding = false;
	protected int mCurrentPageNumber = 1;
	private LayoutInflater mInflater = null;
	private ImageLoader imageLoader = null;
	DisplayImageOptions options;
	protected AsyncTask<String, Integer, List<Photo>> mCurrentAsyncTask;

@Override
public void onSaveInstanceState(Bundle outState) {
	outState.putString(URL_KEY, mUrl);
	super.onSaveInstanceState(outState);
}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getArguments().getString(URL_KEY);
		mPhotos = new ArrayList<Photo>();
		if(mUrl != null)
			loadPhotos();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mGridView = (GridView) inflater.inflate(R.layout.image_grid, null);
		//initializeComponents();
		return mGridView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mGridViewAdapter = new ImageGridViewAdapter();
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);
		imageLoader =((BaseActivity) getActivity()).imageLoader;
		if(!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		mInflater = getLayoutInflater(savedInstanceState);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	  private void initializeComponents() {
	        float spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                SPACE, getResources().getDisplayMetrics());
	        mGridView.setNumColumns(getResources().getDisplayMetrics().widthPixels/ THUMB_WIDTH);
	        /*
	        mGridView.setPadding((int) spacing, (int) spacing, (int) spacing, (int) spacing);
	        mGridView.setVerticalSpacing((int) spacing);
	        mGridView.setHorizontalSpacing((int) spacing);
	        */
	        
	    }
	private class ImageGridViewAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return mPhotos.size();
		}

		@Override
		public Object getItem(int position) {
			return mPhotos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = mInflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(mPhotos.get(position).getmThumbnailImageURL(), holder.imageView, options, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {
											 holder.progressBar.setProgress(0);
											 holder.progressBar.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,
												 FailReason failReason) {
											 holder.progressBar.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											 holder.progressBar.setVisibility(View.GONE);
										 }
									 }, new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.progressBar.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);

			return view;
		}
		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}
	
	private class PhotoDownloadAsyncTask extends
			AsyncTask<String, Integer, List<Photo>> {

		@Override
		protected List<Photo> doInBackground(String... params) {
			Log.d(TAG, params[0]);
			InputStream io = null;
			try {
				URL url = new URL(params[0]);
				URLConnection urlConnection = url.openConnection();
				io = urlConnection.getInputStream();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(io == null)
				return null;
			List<Photo> photos = new ArrayList<Photo>();
			String reply = getString(io);
			
			//Log.d(TAG, reply);
			try {
				JSONObject json = new JSONObject(reply);
				if (!json.getString("stat").equals("ok")){
					
					return photos;
					
					
					
				}
				json = json.getJSONObject("photos");
				JSONArray jsonPhotos = json.getJSONArray("photo");

				JSONObject curJP = null;
				for (int idx = 0; idx < jsonPhotos.length(); idx++) {
					curJP = jsonPhotos.getJSONObject(idx);
					Photo p = new Photo(curJP.getString(PhotoUtils.FARM_KEY),
							curJP.getString(PhotoUtils.ID_KEY),
							curJP.getString(PhotoUtils.SERVER_KEY),
							curJP.getString(PhotoUtils.SECRET_KEY));
					photos.add(p);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return photos;
		}

		public String getString(InputStream io) {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(io));
			StringBuilder builder = new StringBuilder();
			String s = null;
			try {
				while ((s = reader.readLine()) != null)
					builder.append(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return builder.toString();

		}

		@Override
		protected void onPostExecute(List<Photo> result) {
			super.onPostExecute(result);
			mLoding = false;
			if(result == null){
				Toast.makeText(mContext, "Cann not connect to server\nPlease try later\n", Toast.LENGTH_LONG).show();
				return;
			}
			mPhotos.addAll(result);
			mGridViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE && view.getLastVisiblePosition() >= view.getCount() - THRESHOLD
				&& !mLoding ){
			mLoding = true;
			loadPhotos();
		}
		
	}

	protected void loadPhotos() {
		
		mCurrentAsyncTask = new PhotoDownloadAsyncTask().execute(mUrl + "&page="+ mCurrentPageNumber++);
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent pagerIntent = new Intent(mContext, PagerActivity.class);
		pagerIntent.putParcelableArrayListExtra(PagerActivity.PHOTOS, mPhotos);
		pagerIntent.putExtra(PagerActivity.INIT_POSITION, position);
		startActivity(pagerIntent);
	}

}
