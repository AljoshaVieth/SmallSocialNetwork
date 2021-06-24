package de.aljoshavieth.smallsocialandroidapp;

import android.view.View;
import android.widget.TextView;

public class PostListHolder {
    TextView postListTextView;
    TextView postListAuthorTextView;
    TextView postListTimeTextView;

    public PostListHolder(View v) {
        postListTextView = v.findViewById(R.id.postListTextView);
        postListAuthorTextView = v.findViewById(R.id.postListAuthorTextView);
        postListTimeTextView = v.findViewById(R.id.postListTimeTextView);

    }
}
