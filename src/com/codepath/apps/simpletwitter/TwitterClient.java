package com.codepath.apps.simpletwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; 
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "mPg5clOhCL5Vsy3kkVBY0at4c";       // Change this
    public static final String REST_CONSUMER_SECRET = "vwVpz8qJJ6OGDf8sAsYT2vNyRFdGlM87QwcfQ3X7fKedjY0DMa"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    } 
    
    public void getHomeTimeLine(AsyncHttpResponseHandler asyncHandler, long maxId) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	
    	Log.d("debug ", "query " + apiUrl);
    	
    	RequestParams reqParams = new RequestParams();
    	if(maxId == 0) {
    		reqParams.put("since_id", "1");
    	} else {
    		reqParams.put("max_id", maxId+"");
    	}
    	client.get(apiUrl, reqParams, asyncHandler);
    }
    
    public void getMentionsTimeLine(AsyncHttpResponseHandler asyncHandler, long maxId) {
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	
    	RequestParams reqParams = new RequestParams();
    	if(maxId == 0) {
    		reqParams.put("since_id", "1");
    	} else {
    		reqParams.put("max_id", maxId+"");
    	}
    	client.get(apiUrl, reqParams, asyncHandler);
    }
    
    public void postNewTweet(AsyncHttpResponseHandler asyncHandler, String data) {
    	String apiUrl = getApiUrl("statuses/update.json");
    	
    	RequestParams reqParams = new RequestParams();
    	reqParams.put("status", data);
    	client.post(apiUrl, reqParams, asyncHandler);
    }
    
    public void getUserProfile(AsyncHttpResponseHandler asyncHandler) {
    	String apiUrl = getApiUrl("account/verify_credentials.json");
    	
    	Log.d("debug ", "query profile " + apiUrl);
    	
    	RequestParams reqParams = new RequestParams();
    	client.get(apiUrl, null, asyncHandler);
    }
    
    public void getOtherUserProfile(AsyncHttpResponseHandler asyncHandler, String userScreenName) {
    	String apiUrl = getApiUrl("users/show.json?screen_name=" + userScreenName);
    	
    	RequestParams reqParams = new RequestParams();
    	client.get(apiUrl, null, asyncHandler);
    }
    
    public void getUserTimeLine(AsyncHttpResponseHandler asyncHandler, long maxId) {
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
    	
    	RequestParams reqParams = new RequestParams();
    	if(maxId == 0) {
    		reqParams.put("since_id", "1");
    	} else {
    		reqParams.put("max_id", maxId+"");
    	}
    	client.get(apiUrl, reqParams, asyncHandler);
    }
    
    public void getOtherUserTimeLine(AsyncHttpResponseHandler asyncHandler, String screenName, long maxId) {
    	String apiUrl = getApiUrl("statuses/user_timeline.json?screen_name=" + screenName);
    	
    	RequestParams reqParams = new RequestParams();
    	if(maxId == 0) {
    		reqParams.put("since_id", "1");
    	} else {
    		reqParams.put("max_id", maxId+"");
    	}
    	client.get(apiUrl, reqParams, asyncHandler);
    }
    
    /*// CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }*/
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}