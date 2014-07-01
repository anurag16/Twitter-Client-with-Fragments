package com.codepath.apps.simpletwitter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

	public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets) {
		super(context, R.layout.item_tweet, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
		}
		// Lookup view for data population
		ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		TextView tvTweetText = (TextView) convertView.findViewById(R.id.tvTweetText);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		
		ivUserImage.setImageResource(android.R.color.transparent);
		ivUserImage.setContentDescription(tweet.getUser().getScreenName());
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// Populate the data into the template view using the data object
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivUserImage);
		tvUserName.setText(tweet.getUser().getUserName());
		tvTweetText.setText(tweet.getTweetText());
		tvTime.setText(tweet.getCreateTime());
	
		
		ivUserImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//Toast.makeText(getContext(), "clicked on image", Toast.LENGTH_SHORT).show();
				
				ImageView ivUser = (ImageView) v;
				String otherUserScreenName = ivUser.getContentDescription().toString();
				Intent i = new Intent(getContext(), ProfileActivity.class);		
				i.putExtra("otherUserScreenName", otherUserScreenName);
				getContext().startActivity(i);
			}
		});
		
		
		// Return the completed view to render on screen
		return convertView;
	}
}
