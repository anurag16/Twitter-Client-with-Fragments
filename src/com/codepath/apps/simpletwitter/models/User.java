package com.codepath.apps.simpletwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	private String userName;
	private String screenName;
	private long userId;
	private String location;
	private String friends;
	private String followersCount;
	private String followingCount;
	private String profileImageUrl;
	private String profileBackgroundColor;
	private String tagLine;

	public User(JSONObject jsonObj) {
		try {
			this.userName = jsonObj.getString("name");
			this.screenName = jsonObj.getString("screen_name");
			this.userId = jsonObj.getLong("id");
			this.location = jsonObj.getString("location");
			this.friends = jsonObj.getString("friends_count");
			this.followersCount = jsonObj.getString("followers_count");
			this.profileImageUrl = jsonObj.getString("profile_image_url");
			this.profileBackgroundColor = jsonObj.getString("profile_background_color");
			this.followingCount = jsonObj.getString("following");
			this.tagLine = jsonObj.getString("description");

		} catch (JSONException e) {			
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public String getFollowingCount() {
		return friends;
	}

	public String getTagLine() {
		return tagLine;
	}

	public static Parcelable.Creator<User> getCreator() {
		return CREATOR;
	}

	public String getScreenName() {
		return screenName;
	}

	public long getUserId() {
		return userId;
	}

	public String getLocation() {
		return location;
	}

	public String getFriends() {
		return friends;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(userName);
		out.writeString(screenName);
		out.writeLong(userId);
		out.writeString(location);
		out.writeString(friends);
		out.writeString(followersCount);
		out.writeString(profileImageUrl);
		out.writeString(profileBackgroundColor);
		out.writeString(tagLine);
	}	

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	private User(Parcel in) {
		screenName = in.readString();
		userName = in.readString();
		userId = in.readLong();
		location = in.readString();
		friends = in.readString();
		followersCount = in.readString();
		profileImageUrl = in.readString();
		profileBackgroundColor = in.readString();
		tagLine = in.readString();
	}

	public User() {
		//normal actions performed by class, it's still a normal object!
	}
}
