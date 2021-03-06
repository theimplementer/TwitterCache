package com.github.theimplementer.twittercache.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;

public class TwitterSharedPreferences implements TwitterPreferences {
    private static final String TCACHE_SHARED_KEY = "tcache_shared";
    private static final String USER_LOGGED_IN = "user_logged_in";
    private static final String TWITTER_ACCESS_TOKEN = "access_token";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "access_token_secret";
    private static final String PREF_TWEETS_CACHE_SIZE = "pref_tweets_cache_size";

    private final SharedPreferences sharedPreferences;

    public TwitterSharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(TCACHE_SHARED_KEY, MODE_PRIVATE);
    }

    @Override
    public void setUserLoggedIn(String accessToken, String accessTokenSecret) {
        final Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_LOGGED_IN, true);
        editor.putString(TWITTER_ACCESS_TOKEN, accessToken);
        editor.putString(TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret);
        editor.commit();
    }

    @Override
    public void setUserLoggedOut() {
        final Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_LOGGED_IN, false);
        editor.remove(TWITTER_ACCESS_TOKEN);
        editor.remove(TWITTER_ACCESS_TOKEN_SECRET);
        editor.commit();
    }

    @Override
    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(USER_LOGGED_IN, false);
    }

    @Override
    public void setAccessToken(String accessToken) {
        final Editor editor = sharedPreferences.edit();
        editor.putString(TWITTER_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(TWITTER_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessTokenSecret(String accessTokenSecret) {
        final Editor editor = sharedPreferences.edit();
        editor.putString(TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret);
        editor.commit();
    }

    @Override
    public String getAccessTokenSecret() {
        return sharedPreferences.getString(TWITTER_ACCESS_TOKEN_SECRET, null);
    }

    @Override
    public int getTweetsCacheSize() {
        return sharedPreferences.getInt(PREF_TWEETS_CACHE_SIZE, 10);
    }
}
