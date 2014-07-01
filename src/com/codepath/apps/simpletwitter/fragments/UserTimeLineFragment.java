package com.codepath.apps.simpletwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitter.EndlessScrollListener;
import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class UserTimeLineFragment extends TweetListFragment {
	
	private long maxId;
	private boolean isFresh;
	private TwitterClient twitterClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterClient = TwitterApplication.getRestClient();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		isFresh = true;
		maxId = 0;
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				customLoadMoreDataFromApi(page);  				
			}
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() { 
				clearAdapter();
				isFresh = true;
				maxId = 0;
				populateUserTimeLine();		
			}
		});
	}
	
	public void customLoadMoreDataFromApi(int offset) {
		populateUserTimeLine();
	}
	
	public void populateUserTimeLine() {
		twitterClient.getUserTimeLine(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				clearAdapter(); 
				addTweetsToAdapter(Tweet.parseJsonArray(jsonArray, isFresh));
				maxId = Tweet.getLowestId();
				isFresh = false;
				lvTweets.onRefreshComplete();
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.d("debug profile", e.toString());
			}
		}, maxId);
		
	}
}
