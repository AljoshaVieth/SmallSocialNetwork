package de.aljoshavieth.smallsocialandroidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity implements SmallSocialNetworkApiService.ApiCallback {

    private ArrayList<Integer> backgroundColors;
    private int currentBackgroundColor;
    private EditText createPostEditText;
    private SmallSocialNetworkApiService apiService;
    private Post post;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        initializeBackgroundColors();
        createPostEditText = (EditText) findViewById(R.id.createPostEditText);
        createPostEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        currentBackgroundColor = getRandomBackgroundColor();
        createPostEditText.getRootView().setBackgroundColor(currentBackgroundColor);
        apiService = new SmallSocialNetworkApiService(this);
    }

    public void submitPost(View view) {
        String content = createPostEditText.getText().toString();
        if (content.length() < 1) {
            Toast toast = Toast.makeText(this, getString(R.string.empty_content_error), Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        long unixTime = System.currentTimeMillis() / 1000L;
        post = new Post(UUID.randomUUID().toString(), content, getColorAsString(currentBackgroundColor), new ArrayList<>(), MainActivity.user, unixTime);
        String url = getString(R.string.apiBaseUrl) + "/post";
        Gson gson = new Gson();
        try {
            JSONObject postAsJson = new JSONObject(gson.toJson(post));
            apiService.submitData(url, postAsJson, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeBackgroundColors() {
        backgroundColors = new ArrayList<>();
        backgroundColors.add(getColor(R.color.dark_orange));
        backgroundColors.add(getColor(R.color.canary_yellow));
        backgroundColors.add(getColor(R.color.blue));
        backgroundColors.add(getColor(R.color.electric_red));
        backgroundColors.add(getColor(R.color.la_salle_green));
        backgroundColors.add(getColor(R.color.patriarch));
    }

    private int getRandomBackgroundColor() {
        Random r = new Random();
        return backgroundColors.get(r.nextInt(backgroundColors.size()));
    }

    private String getColorAsString(int color) {
        return String.format("#%06x", color & 0xffffff);
    }

    @Override
    public void onResponseReceived() {
        Toast toast = Toast.makeText(this, getString(R.string.post_submitted), Toast.LENGTH_LONG);
        toast.show();
        Log.i("SmallSocialAndroidApp", "Received response!");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("update", true);
        startActivity(intent);
    }

    @Override
    public void onPostResponseReceived(TreeMap<String, Post> posts) {

    }

    @Override
    public void onErrorResponseReceived() {

    }
}