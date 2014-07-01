package com.codepath.apps.simpletwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;


public class MentionsTimeLineFragment extends TweetListFragment {
	private long maxId;
	private boolean isFresh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		maxId = 0;
		isFresh = true;
		populateMentionsTimeLine();
	}
	
	public void populateMentionsTimeLine() {
 
		getTwitterClient().getMentionsTimeLine(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray json) {
				Log.d("debug mentions ", " isFresh = " + isFresh + " maxId = " + maxId );

				clearAdapter(); 
				addTweetsToAdapter(Tweet.parseJsonArray(json, isFresh));
				maxId = Tweet.getLowestId();
				isFresh = false;
				lvTweets.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable e, String s) {				
				Log.d("debug mentions", e.toString());

			}
		}, maxId);
	}
}
