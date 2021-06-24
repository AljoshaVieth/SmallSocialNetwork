package de.aljoshavieth.smallsocialandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;

public class ViewPostActivity extends AppCompatActivity implements SmallSocialNetworkApiService.ApiCallback {

    private String postId;
    private Post post;
    private SmallSocialNetworkApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        TextView contentTextView = findViewById(R.id.postContentTextView);
        TextView authorTextView = findViewById(R.id.postAuthorTextView);
        TextView timeTextView = findViewById(R.id.postTimeTextView);
        ImageButton deletePostButton = findViewById(R.id.deletePostButton);
        post = MainActivity.posts.get(postId);
        assert post != null;
        if (userIsAuthor()) {
            deletePostButton.setVisibility(View.VISIBLE);
        }
        contentTextView.setText(post.getContent());
        contentTextView.getRootView().setBackgroundColor(Color.parseColor(post.getColor()));
        authorTextView.setText("@" + post.getAuthor().getName());
        timeTextView.setText(Utils.unixToFormattedTime(post.getTime()));
        apiService = new SmallSocialNetworkApiService(this);

    }

    private boolean userIsAuthor() {
        return post.getAuthor().getId().equals(MainActivity.user.getId());
    }

    public void deletePost(View view) {
        if (userIsAuthor()) {
            apiService.deletePost(getString(R.string.apiBaseUrl) + "/post", postId, this);
        }
    }

    @Override
    public void onErrorResponseReceived() {

    }

    @Override
    public void onResponseReceived() {
        //Post deleted
        Toast toast = Toast.makeText(this, getString(R.string.post_deleted), Toast.LENGTH_LONG);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("update", true);
        startActivity(intent);
    }

    @Override
    public void onPostResponseReceived(TreeMap<String, Post> posts) {

    }
}