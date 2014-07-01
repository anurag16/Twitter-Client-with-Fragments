package com.codepath.apps.simpletwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ComposeTweetFragment extends DialogFragment {

	private View layoutComposeTweet;
	private TwitterClient twitterClient;
	private EditText etTweetData;
	private TextView tvNoOfChars;
	private Button btnTweetData;
	private Button btnOnCancel;
	private static String mUserName;
	private static String mProfileImageUrl; 
	private Tweet composedTweet;
	
	private ComposeTweetFragmentListener listener;
	
	public interface ComposeTweetFragmentListener {
		void onFinishComposeDialog(Tweet composedTweet);
	}


	public ComposeTweetFragment() {
		// Empty constructor required for DialogFragment
	}

	public static ComposeTweetFragment newInstance(String title, User user) {
		ComposeTweetFragment frag = new ComposeTweetFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
	
		mUserName = user.getUserName();
		mProfileImageUrl = user.getProfileImageUrl();
		
		return frag;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof ComposeTweetFragmentListener) {
			listener = (ComposeTweetFragmentListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + "must implement ComposeTweetFragmentListener");
		}
	}
	
	@Override
	public void onDetach() {	
		super.onDetach();
		listener = null;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layoutComposeTweet = inflater.inflate(R.layout.fragement_compose_tweet, container);

		etTweetData = (EditText) layoutComposeTweet.findViewById(R.id.etTweetData);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(140);
		etTweetData.setFilters(FilterArray);

		btnTweetData = (Button) layoutComposeTweet.findViewById(R.id.btnTweet);
		btnOnCancel = (Button) layoutComposeTweet.findViewById(R.id.btnCancel);
		
		btnTweetData.setEnabled(false);
		
		tvNoOfChars = (TextView) layoutComposeTweet.findViewById(R.id.tvNoofChars);

		twitterClient = TwitterApplication.getRestClient();

		String title = getArguments().getString("title", "Compose Tweet");
		getDialog().setTitle(title);
		// Show soft keyboard automatically
		etTweetData.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		ImageView ivUserImage = (ImageView) layoutComposeTweet.findViewById(R.id.ivUserImage);
		ivUserImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// Populate the data into the template view using the data object
		imageLoader.displayImage(mProfileImageUrl, ivUserImage);
		
		TextView tvUserInfo = (TextView) layoutComposeTweet.findViewById(R.id.tvUserInfo);
		tvUserInfo.setText(mUserName);

		etTweetData.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				int currentCharCount = etTweetData.getText().toString().length();
				if(currentCharCount > 0) {
					btnTweetData.setEnabled(true);
				} else {
					btnTweetData.setEnabled(false);
				}
				tvNoOfChars.setText("" + (140 - currentCharCount));			
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		btnTweetData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				String tweetData = etTweetData.getText().toString();

				twitterClient.postNewTweet((new JsonHttpResponseHandler(){

					@Override
					public void onSuccess(JSONArray tweetObj) {
						composedTweet = Tweet.parseJsonArray(tweetObj, true).get(0);
					}

					@Override
					public void onFailure(Throwable e, String s) {				
						Log.d("debug compose tweet", e.toString());
					}
				}), tweetData);

				etTweetData.setText("");
				
				//ComposeTweetFragmentListener listener = (ComposeTweetFragmentListener) getActivity();
				listener.onFinishComposeDialog(composedTweet);
				
				dismiss();
			}
		});

		btnOnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				dismiss();
			}
		});

		return layoutComposeTweet;
	}
}
