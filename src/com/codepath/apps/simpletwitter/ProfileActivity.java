package com.codepath.apps.simpletwitter;

import org.json.JSONObject;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.fragments.OtherUserTimeLineFragment;
import com.codepath.apps.simpletwitter.fragments.UserTimeLineFragment;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	private TwitterClient twitterClient;
	private User currentUser = null;
	private User otherUser = null;
	private String otherUserScreenName = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		twitterClient = TwitterApplication.getRestClient();
		
		currentUser = (User) getIntent().getParcelableExtra("currentUser");
		
		if(currentUser!=null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.flUserTimeLineContainer, new UserTimeLineFragment());
			ft.commit();
			
			getActionBar().setTitle("@" + currentUser.getScreenName());
			populateProfileHeader(currentUser);
		} 
		
		otherUserScreenName = (String) getIntent().getStringExtra("otherUserScreenName");
		
		if(otherUserScreenName!=null) {
			OtherUserTimeLineFragment.setOtherUserScreenName(otherUserScreenName);
			
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.flUserTimeLineContainer, new OtherUserTimeLineFragment());
			ft.commit();
			
			getActionBar().setTitle("@" + otherUserScreenName);
			loadOtherUserProfile();
		}
	}
	
	private void loadOtherUserProfile() {
		twitterClient.getOtherUserProfile(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject jsonObj) {			
				otherUser = new User(jsonObj);
				getActionBar().setTitle("@" + otherUser.getScreenName());
				
				populateProfileHeader(otherUser);
				Log.d("debug", otherUser.getScreenName());
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.d("debug", e.toString());
			}
		}, otherUserScreenName);
		
	}

	private void populateProfileHeader(User user) {
		TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
		TextView tvUserTagLine = (TextView) findViewById(R.id.tvUserTagLine);
		TextView tvUserFollowers = (TextView) findViewById(R.id.tvUserFollowers);
		TextView tvUserFollowing = (TextView) findViewById(R.id.tvUserFollowing);
		ImageView ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
		
		tvUserName.setText(user.getUserName());
		tvUserTagLine.setText(user.getTagLine());
		tvUserFollowers.setText(user.getFollowersCount() + " Followers");
		tvUserFollowing.setText(user.getFollowingCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUserImage); 
	}
}
