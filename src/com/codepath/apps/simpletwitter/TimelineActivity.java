package com.codepath.apps.simpletwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.codepath.apps.simpletwitter.Listeners.FragmentTabListener;
import com.codepath.apps.simpletwitter.fragments.ComposeTweetFragment;
import com.codepath.apps.simpletwitter.fragments.ComposeTweetFragment.ComposeTweetFragmentListener;
import com.codepath.apps.simpletwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletwitter.fragments.MentionsTimeLineFragment;
import com.codepath.apps.simpletwitter.fragments.TweetListFragment;
import com.codepath.apps.simpletwitter.models.Tweet;

public class TimelineActivity extends FragmentActivity implements ComposeTweetFragmentListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		setContentView(R.layout.activity_timeline);
	
		setupTabs();
	}
	
	 // Should be called manually when an async task has started
    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true); 
    }
    
    // Should be called when an async task has finished
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false); 
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_menu, menu);

		return super.onCreateOptionsMenu(menu); 
	}


	public void onComposeIconClick(MenuItem mi) {
		launchComposeDialog();
	}
	
	public void onProfileIconClick(MenuItem mi) {
	
		FragmentManager fm = getSupportFragmentManager();
		TweetListFragment tweetListFragment = (TweetListFragment) fm.findFragmentById(R.id.flContainer);
		
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("currentUser", tweetListFragment.getCurrentUser()); 
		startActivity(i);
	}

	private void launchComposeDialog() {
		
		FragmentManager fm = getSupportFragmentManager();
		TweetListFragment tweetListFragment = (TweetListFragment) fm.findFragmentById(R.id.flContainer);
		
		ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("Compose Tweet", tweetListFragment.getCurrentUser());
		composeTweetFragment.show(fm, "fragment_compose_tweet");
	}

	@Override
	public void onFinishComposeDialog(Tweet composedTweet) {
		
		FragmentManager fm = getSupportFragmentManager();
		TweetListFragment tweetListFragment = (TweetListFragment) fm.findFragmentById(R.id.flContainer);
		
		TweetListFragment.clearAdapter(); 
		//tweetListFragment.addTweetsToAdapterAtIndex(composedTweet, 0);
	}
	
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
 
		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_action_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "Home",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_action_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimeLineFragment>(R.id.flContainer, this, "Mentions",
								MentionsTimeLineFragment.class));

		actionBar.addTab(tab2);
	}
}
