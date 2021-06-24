package de.aljoshavieth.smallsocialandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements SmallSocialNetworkApiService.ApiCallback {
    String apiBaseUrl;
    ListView postListView;
    static User user;
    public static TreeMap<String, Post> posts = new TreeMap<>();
    private PostListAdapter postAdapter;
    private SmallSocialNetworkApiService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = new SmallSocialNetworkApiService(this);
        postListView = findViewById(R.id.postListView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.progressBar);
        updateData();
        user = setUser();
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("SmallSocialAndroidApp", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateData();
                    }
                }
        );
    }

    void populatePostListView() {

        ArrayList<Post> postList = new ArrayList<>(posts.values());
        postList.sort(Comparator.comparing(Post::getTime));
        Collections.reverse(postList);
        postAdapter = new PostListAdapter(this, postList);
        postListView.setAdapter(postAdapter);
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postId = postList.get(position).getId();
                openViewPostActivity(postId);
            }
        });
    }

    private void updateData() {
        Log.i("SmallSocialAndroidApp", "--------------Updating...");
        apiService.getPosts(getString(R.string.apiBaseUrl) + "/post", this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean update = intent.getBooleanExtra("update", false);
        if (update) {
            apiService.getPosts(getString(R.string.apiBaseUrl) + "/post", this);
        }
    }

    public void openViewPostActivity(String postId) {
        Intent intent = new Intent(this, ViewPostActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    public void openCreatePostActivity(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        String postId = "postId";
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    private User setUser() {
        SharedPreferences sharedPref = getSharedPreferences(
                "smallsocialandroidapppreferences", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "Anonymous");
        String uuid;
        if (sharedPref.contains("uuid")) {
            uuid = sharedPref.getString("uuid", "");
        } else {
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("uuid", uuid);
            editor.apply();
        }
        return new User(uuid, username);
    }


    @Override
    public void onErrorResponseReceived() {
    }

    @Override
    public void onResponseReceived() {

    }

    @Override
    public void onPostResponseReceived(TreeMap<String, Post> posts) {
        MainActivity.posts = posts;
        populatePostListView();
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }


}