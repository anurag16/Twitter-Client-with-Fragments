package com.codepath.apps.simpletwitter.fragments;

import java.util.ArrayList;
import java.util.Currency;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.simpletwitter.EndlessScrollListener;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TweetsArrayAdapter;
import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TweetListFragment extends Fragment {
	
	private static TweetsArrayAdapter tweetArrayAdapter;
	private ArrayList<Tweet> tweetsList;
	static PullToRefreshListView lvTweets;
	private static TwitterClient twitterClient;
	private long maxId = 0;
	private User currentUser = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Initialization not related to the view	
		tweetsList = new ArrayList<Tweet>();
		tweetArrayAdapter = new TweetsArrayAdapter(getActivity(), tweetsList);
		twitterClient = TwitterApplication.getRestClient();
		
		loadUserProfileInfo();
	} 
	
	private void loadUserProfileInfo() {
		twitterClient.getUserProfile(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) { 			
				currentUser = new User(jsonObj);
			}

			@Override
			public void onFailure(Throwable e) {
				Log.d("debug", e.toString());
			}
		});
	}

	public User getCurrentUser() {
		return currentUser;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

		maxId = 0;
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(tweetArrayAdapter);
		
		return view; 
	}
	
	/*public void customLoadMoreDataFromApi(int offset) {
		if(maxId == 0) {
			populateTimeLine(true, 0);
		} 
		maxId = Tweet.getLowestId();
		populateTimeLine(false, maxId);	
	}
	
	public void populateTimeLine(boolean isFresh, long maxId) {
		// TODO Auto-generated method stub
	}*/
	
	public static void addTweetsToAdapter(ArrayList<Tweet> tweetList) {
		tweetArrayAdapter.addAll(tweetList);
	}
	
	public void addTweetsToAdapterAtIndex(Tweet tweet, int index) {
		tweetArrayAdapter.insert(tweet, index);
	}
	
	public static void clearAdapter() {
		tweetArrayAdapter.clear();
	}
	
	public static TwitterClient getTwitterClient() {
		return twitterClient;
	}
}
