package com.best.nature.wallpaper.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	private String mPhotoID;
	private String mFarmID;
	private String mServerID;
	private String mSecret;
	private String mThumbnailImageURL;
	private String mImageURL;

	public Photo(String mFarmID, String mPhotoID, String mServerID,
			String mSecret) {
		super();
		this.mFarmID = mFarmID;
		this.mPhotoID = mPhotoID;
		this.mServerID = mServerID;
		this.mSecret = mSecret;
	}


	public String getmFarmID() {
		return mFarmID;
	}

	public void setmFarmID(String mFarmID) {
		this.mFarmID = mFarmID;
	}

	public String getmPhotoID() {
		return mPhotoID;
	}

	public void setmPhotoID(String mPhotoID) {
		this.mPhotoID = mPhotoID;
	}

	public String getmServerID() {
		return mServerID;
	}

	public void setmServerID(String mServerID) {
		this.mServerID = mServerID;
	}

	public String getmSecret() {
		return mSecret;
	}

	public void setmSecret(String mSecret) {
		this.mSecret = mSecret;
	}

	public String getmThumbnailImageURL() {
		if (mThumbnailImageURL == null)
			mThumbnailImageURL = PhotoUtils.createURL(
					PhotoUtils.THUMB_URL_KEY, this);
		return mThumbnailImageURL;
	}

	public void setmThumbnailImageURL(String mThumbnailImageURL) {
		this.mThumbnailImageURL = mThumbnailImageURL;
	}

	public String getmImageURL() {
		if (mImageURL == null)
			mImageURL = PhotoUtils.createURL(PhotoUtils.IMAGE_URL_KEY, this);
		return mImageURL;
	}

	public void setmImageURL(String mImageURL) {
		this.mImageURL = mImageURL;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mPhotoID);
		dest.writeString(mFarmID);
		dest.writeString(mServerID);
		dest.writeString(mSecret);
		dest.writeString(getmThumbnailImageURL());
		dest.writeString(getmImageURL());

	}

	public Photo(Parcel in) {
		mPhotoID = in.readString();
		mFarmID = in.readString();
		mServerID = in.readString();
		mSecret = in.readString();
		mThumbnailImageURL = in.readString();
		mImageURL = in.readString();
	}
	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};

}
