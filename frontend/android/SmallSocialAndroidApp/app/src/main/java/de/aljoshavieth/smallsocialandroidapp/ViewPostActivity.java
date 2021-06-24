package de.aljoshavieth.smallsocialandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        String postID = intent.getStringExtra("postId");
        TextView contentTextView = findViewById(R.id.postContentTextView);
        TextView authorTextView = findViewById(R.id.postAuthorTextView);
        TextView timeTextView = findViewById(R.id.postTimeTextView);
        Post post = MainActivity.posts.get(postID);
        assert post != null;
        contentTextView.setText(post.getContent());
        contentTextView.getRootView().setBackgroundColor(Color.parseColor(post.getColor()));
        authorTextView.setText("@" + post.getAuthor().getName());
        timeTextView.setText(Utils.unixToFormattedTime(post.getTime()));

    }

}