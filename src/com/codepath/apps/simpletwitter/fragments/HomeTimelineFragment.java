package com.codepath.apps.simpletwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codepath.apps.simpletwitter.EndlessScrollListener;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetListFragment {
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
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				customLoadMoreDataFromApi(page);  
				// or customLoadMoreDataFromApi(totalItemsCount); 
			}
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() { 
				clearAdapter();
				isFresh = true;
				maxId = 0;
				populateTimeLine();		
			}
		});
	}
	
	public void customLoadMoreDataFromApi(int offset) {
		populateTimeLine();
	}
	
	public void populateTimeLine() {

		getTwitterClient().getHomeTimeLine(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray json) {
				Log.d("debug ", " is Fresh " + isFresh + " maxId " + maxId);

				clearAdapter(); 
				addTweetsToAdapter(Tweet.parseJsonArray(json, isFresh));
				maxId = Tweet.getLowestId();
				isFresh = false;
				lvTweets.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable e, String s) {				
				Log.d("debug", e.toString());

			}
		}, maxId);
	}
}
