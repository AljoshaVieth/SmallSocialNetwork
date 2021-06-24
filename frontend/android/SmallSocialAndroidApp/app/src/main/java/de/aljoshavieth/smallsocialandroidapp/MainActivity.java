package de.aljoshavieth.smallsocialandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferencesButton) {
            // do something here
            Log.i("SmallSocialAndroidApp", "Preferences button pressed!");
            showPreferencesDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.settings);
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.preferences_alert, null, false);
        // Set up the textSizeInput
        final EditText userNameInput;
        userNameInput = viewInflated.findViewById(R.id.userNameInput);
        userNameInput.setText(MainActivity.user.getName());
        userNameInput.setSelection(userNameInput.length());
        builder.setView(viewInflated);


        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (userNameInput.getText() != null && userNameInput.getText().length() > 0) {
                    String newName = String.valueOf(userNameInput.getText());
                    String userId = MainActivity.user.getId();
                    MainActivity.user = new User(userId, newName);
                    SharedPreferences sharedPref = getSharedPreferences(
                            "smallsocialandroidapppreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", newName);
                    editor.apply();
                    //TODO submit new name to REST API
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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