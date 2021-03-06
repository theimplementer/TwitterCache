package com.github.theimplementer.twittercache.view;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.theimplementer.twittercache.R;
import com.github.theimplementer.twittercache.preferences.TwitterSharedPreferences;

import java.util.List;

import twitter4j.Status;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.github.theimplementer.twittercache.TwitterInstance.getInstance;

public class TweetListFragment extends ListFragment implements Updatable<Status> {

    private TweetsAdapter tweetsAdapter;
    private TweetsFetcher tweetsFetcher;
    private TwitterSharedPreferences twitterPreferences;
    private TweetItemClickListener tweetItemClickListener;

    public TweetListFragment() {
        this.tweetsFetcher = new RemoteTweetsFetcher(this);
    }

    public static Fragment newInstance() {
        return new TweetListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TweetItemClickListener) {
            tweetItemClickListener = (TweetItemClickListener) activity;
        } else {
            throw new RuntimeException("The hosting activity must implement the TweetItemClickListener interface.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        twitterPreferences = new TwitterSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tweetsAdapter = new TweetsAdapter(getActivity());
        setListAdapter(tweetsAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        tweetsFetcher.fetch(twitterPreferences.getTweetsCacheSize());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                twitterPreferences.setUserLoggedOut();
                getInstance().resetInstance();
                final Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                return true;
            case R.id.settings:
                final Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Status tweet = (Status) getListAdapter().getItem(position);
        tweetItemClickListener.displayDetailsFor(tweet);
    }

    public void add(List<Status> tweets) {
        tweetsAdapter.addTweets(tweets);
        tweetsAdapter.notifyDataSetChanged();
    }
}
