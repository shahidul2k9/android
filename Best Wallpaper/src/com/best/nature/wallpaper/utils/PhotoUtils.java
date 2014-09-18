package com.best.nature.wallpaper.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PhotoUtils {
	public static final String  ID_KEY = "id";
	public static final String SERVER_KEY = "server";
	public static final String SECRET_KEY = "secret";
	public static final String FARM_KEY = "farm";
	public static final String THUMB_URL_KEY = "url_q";
	public static final String IMAGE_URL_KEY = "url_b";
	private static final String PER_PAGE = "per_page";
	private static final String PER_PAGE_VALUE = "15";
	private static final String API_KEY = "api_key";
	private static final String API_KEY_VALUE = "PUT YOUR FLICKR API KEY HERE";
	private static final String BASE_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&content_type=1&media=photos&nojsoncallback=1";
	public static final String RECENT_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&" + "api_key=" + API_KEY_VALUE+ "&format=json&nojsoncallback=1&"
			+PER_PAGE +"=" + PER_PAGE_VALUE ;
	public static final String GROUP_BASE_URL = "https://api.flickr.com/services/rest/?method=flickr.groups.pools.getPhotos&api_key=" + API_KEY_VALUE + "&format=json&nojsoncallback=1&" + PER_PAGE +"=" + PER_PAGE_VALUE;  
	public static final String FORMAT = "format";
	public static final String FORMAT_VALUE = "json";
	public static final String TEXT = "text";
	public static final String EXTRAS = "extras";
	
	public static final String URL_ENCODING = "UTF-8";
	private static final String USER_ID = "121818519@N03";
	
	public static String getURLByText(String text){
		StringBuilder builder = new StringBuilder(BASE_URL);
		try {
			builder.append("&"+ API_KEY + "=" + URLEncoder.encode(API_KEY_VALUE, URL_ENCODING));
			builder.append("&" + FORMAT + "=" + FORMAT_VALUE);
			builder.append("&" + PER_PAGE + "=" + PER_PAGE_VALUE);
			if(text.equals("3D"))
				builder.append("&group_id=89888984%40N00");
			else
			builder.append("&" + TEXT + "=" + URLEncoder.encode(text, URL_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	public static String getPopularUrl(){
		
		try {
			return "https://api.flickr.com/services/rest/?method=flickr.people.getPhotos&format=json&nojsoncallback=1&api_key="+API_KEY_VALUE+"&user_id=" + URLEncoder.encode(USER_ID, URL_ENCODING) 
					+ "&" + PER_PAGE +"=" + PER_PAGE_VALUE;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
public static String getGroupUrl(String group){
	try {
		return GROUP_BASE_URL + "&group_id=" + URLEncoder.encode(group, URL_ENCODING);
	} catch (UnsupportedEncodingException e) {
	}
	return "";
}
	public static String createURL(String type, Photo p) {
		return String.format("http://farm%s.staticflickr.com/%s/%s_%s_%s.jpg", p.getmFarmID(), p.getmServerID(), p.getmPhotoID(), p.getmSecret(), type.substring(type.length()-1));
	}
}
