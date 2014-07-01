package com.codepath.apps.simpletwitter.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.text.format.DateUtils;
import android.util.Log;

public class Tweet {

	private String tweetText;
	private long uid;
	private String createTime;
	private User user;
	
	private static ArrayList<Tweet> tweetsList = new ArrayList<Tweet>();
	private static ArrayList<String> tweetsListText = new ArrayList<String>();
	private static long lowestId = 0;
	
	Tweet(JSONObject jsonObj) {
		try {
			this.tweetText = jsonObj.getString("text");
			if(lowestId == 0) {
				lowestId = jsonObj.getLong("id");
			} else {
				if(lowestId > jsonObj.getLong("id"))
					lowestId = jsonObj.getLong("id"); 
			}
			this.uid = jsonObj.getLong("id");
			this.createTime = jsonObj.getString("created_at");
			this.user = new User(jsonObj.getJSONObject("user"));
			tweetsListText.add(this.tweetText);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Tweet> parseJsonArray(JSONArray jsonArray, boolean isFresh) {
		try {
			if(isFresh) {
				tweetsList.clear();
				tweetsListText.clear();
			}
			
			for(int i=0; i<jsonArray.length(); i++) {			
				JSONObject jsonObj =  jsonArray.getJSONObject(i);
				tweetsList.add(new Tweet(jsonObj));
			}
		} catch (JSONException e) {
				e.printStackTrace();
		}
		tweetsListText.get(0);
		return tweetsList;
	}

	public String getTweetText() {
		return tweetText;
	}

	public long getUid() {
		return uid;
	}

	public String getCreateTime() {
		return getRelativeTimeAgo(createTime);
	}

	public User getUser() {
		return user;
	}

	public static ArrayList<Tweet> getTweetsList() {
		return tweetsList;
	}
	
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	 
		String time[] = relativeDate.split(" ");	
		Log.d("debug time", relativeDate);
		if(time.length > 1) {
			return time[0] + time[1].substring(0,1);	
		} else {
			return time[0];
		}
	}
	
	public static long getLowestId() {
		return lowestId;
	}
}


