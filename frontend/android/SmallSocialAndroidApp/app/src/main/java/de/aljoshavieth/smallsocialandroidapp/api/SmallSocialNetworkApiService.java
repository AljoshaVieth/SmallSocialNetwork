package de.aljoshavieth.smallsocialandroidapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

import de.aljoshavieth.smallsocialandroidapp.models.Comment;
import de.aljoshavieth.smallsocialandroidapp.models.Post;
import de.aljoshavieth.smallsocialandroidapp.models.User;

public class SmallSocialNetworkApiService {
    private final ApiCallback apiCallback;

    public SmallSocialNetworkApiService(ApiCallback apiCallback) {
        this.apiCallback = apiCallback;
    }

    public void submitData(String url, JSONObject body, Context context) {
        Log.i("SmallSocialAndroidApp", "Sending data to " + url);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SmallSocialAndroidApp", "Received response: " + response);
                        try {
                            if (response.getString("status").equals("success")) {
                                apiCallback.onResponseReceived();
                            } else {
                                apiCallback.onErrorResponseReceived();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            apiCallback.onErrorResponseReceived();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallback.onErrorResponseReceived();
                        Log.e("SmallSocialAndroidApp", "An error occured: " + error.getMessage());
                    }
                });
        ApiHandler.getInstance(context).addToRequestQueue(request);
        Log.e("SmallSocialAndroidApp", "The body content type: " + request.getBodyContentType());
    }

    public void deletePost(String url, String postId, Context context) {
        Log.i("SmallSocialAndroidApp", "Sending delete request to " + url);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.DELETE, url + "/" + postId, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SmallSocialAndroidApp", "Received response: " + response);
                        try {
                            if (response.getString("status").equals("success")) {
                                apiCallback.onResponseReceived();
                            } else {
                                apiCallback.onErrorResponseReceived();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            apiCallback.onErrorResponseReceived();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallback.onErrorResponseReceived();
                        Log.e("SmallSocialAndroidApp", "An error occured: " + error.getMessage());
                    }
                });
        ApiHandler.getInstance(context).addToRequestQueue(request);
        Log.e("SmallSocialAndroidApp", "The body content type: " + request.getBodyContentType());
    }

    public void getPosts(String url, Context context) {
        JsonArrayRequest districtDataRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            TreeMap<String, Post> posts = new TreeMap<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postJSON = (JSONObject) response.get(i);
                                JSONObject postAuthorJSON = (JSONObject) postJSON.get("author");
                                User author = new User(postAuthorJSON.getString("id"), postAuthorJSON.getString("name"));
                                ArrayList<Comment> comments = new ArrayList<>();
                                JSONArray allCommentsJSON = (JSONArray) postJSON.get("comments");
                                allCommentsJSON.length();
                                for(int j = 0; j < allCommentsJSON.length(); j++){
                                    JSONObject commentJSON = (JSONObject) allCommentsJSON.get(j);
                                    JSONObject commentAuthorJSON = (JSONObject) commentJSON.get("author");
                                    User commentAuthor = new User(commentAuthorJSON.getString("id"), commentAuthorJSON.getString("name"));
                                    comments.add(new Comment(commentJSON.getString("id"), commentJSON.getString("content"), commentAuthor, commentJSON.getLong("time")));
                                }
                                Post post = new Post(postJSON.getString("id"), postJSON.getString("content"), postJSON.getString("color"), comments, author, postJSON.getLong("time"));
                                posts.put(post.getId(), post);
                            }
                            apiCallback.onPostResponseReceived(posts);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("CoronaDataApp", "Response received");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CoronaDataApp", "An error occured: " + error.getMessage());
                    }
                });
        Log.i("CoronaDataApp", "Adding to queue");
        ApiHandler.getInstance(context).addToRequestQueue(districtDataRequest);
    }

    public interface ApiCallback {
        void onErrorResponseReceived();

        void onResponseReceived();

        void onPostResponseReceived(TreeMap<String, Post> posts);
    }
}

