package de.aljoshavieth.smallsocialandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.UUID;

import de.aljoshavieth.smallsocialandroidapp.api.SmallSocialNetworkApiService;
import de.aljoshavieth.smallsocialandroidapp.layouthelper.CommentsListAdapter;
import de.aljoshavieth.smallsocialandroidapp.models.Comment;
import de.aljoshavieth.smallsocialandroidapp.models.Post;

public class ViewPostActivity extends AppCompatActivity implements SmallSocialNetworkApiService.ApiCallback {

    private String postId;
    private Post post;
    private SmallSocialNetworkApiService apiService;
    private TextView contentTextView;
    private TextView authorTextView;
    private TextView timeTextView;
    private ImageButton deletePostButton;
    private EditText createCommentEditText;
    private ListView commentsListView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        contentTextView = findViewById(R.id.postContentTextView);
        authorTextView = findViewById(R.id.postAuthorTextView);
        timeTextView = findViewById(R.id.postTimeTextView);
        deletePostButton = findViewById(R.id.deletePostButton);
        createCommentEditText = findViewById(R.id.createCommentEditText);
        commentsListView = findViewById(R.id.commentsListView);
        post = MainActivity.posts.get(postId);
        assert post != null;
        if (userIsAuthor()) {
            deletePostButton.setVisibility(View.VISIBLE);
        }
        setContent();
        apiService = new SmallSocialNetworkApiService(this);
    }

    private void setContent() {
        contentTextView.setText(post.getContent());
        contentTextView.getRootView().setBackgroundColor(Color.parseColor(post.getColor()));
        authorTextView.setText("@" + post.getAuthor().getName());
        timeTextView.setText(Utils.unixToFormattedTime(post.getTime()));
        setUpCommentEditText();
        populateCommentListView();
    }

    private void populateCommentListView() {
        ArrayList<Comment> commentList = new ArrayList<>(post.getComments());
        commentList.sort(Comparator.comparing(Comment::getTime));
        CommentsListAdapter commentListAdapter = new CommentsListAdapter(this, commentList, post.getColor());
        commentsListView.setAdapter(commentListAdapter);
    }

    private void setUpCommentEditText() {
        createCommentEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        createCommentEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        createCommentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addComment(createCommentEditText.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
    }

    private boolean userIsAuthor() {
        return post.getAuthor().getId().equals(MainActivity.user.getId());
    }

    public void deletePost(View view) {
        if (userIsAuthor()) {
            apiService.deletePost(getString(R.string.apiBaseUrl) + "/post", postId, this);
        }
    }

    void addComment(String content) {
        ArrayList<Comment> comments = post.getComments();
        long unixTime = System.currentTimeMillis() / 1000L;
        comments.add(new Comment(UUID.randomUUID().toString(), content, MainActivity.user, unixTime));
        MainActivity.posts.put(postId, post);
        updatePost();
    }

    void updatePost() {
        String url = getString(R.string.apiBaseUrl) + "/post";
        Gson gson = new Gson();
        JSONObject postAsJson = null;
        try {
            postAsJson = new JSONObject(gson.toJson(post));
            apiService.submitData(url, postAsJson, this);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SmallSocialAndroidApp", "Could not update comments....");
        }
    }

    @Override
    public void onErrorResponseReceived() {

    }

    @Override
    public void onResponseReceived() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("update", true);
        startActivity(intent);
    }

    @Override
    public void onPostResponseReceived(TreeMap<String, Post> posts) {

    }
}