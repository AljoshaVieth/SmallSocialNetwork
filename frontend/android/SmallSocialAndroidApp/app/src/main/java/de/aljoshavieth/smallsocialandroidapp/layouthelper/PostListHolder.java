package de.aljoshavieth.smallsocialandroidapp.layouthelper;

import android.view.View;
import android.widget.TextView;

import de.aljoshavieth.smallsocialandroidapp.R;

public class PostListHolder {
    TextView postListTextView;
    TextView postListAuthorTextView;
    TextView postListTimeTextView;

    public PostListHolder(View v) {
        postListTextView = v.findViewById(R.id.commentsListContentTextView);
        postListAuthorTextView = v.findViewById(R.id.commentsListAuthorTextView);
        postListTimeTextView = v.findViewById(R.id.commentsListTimeTextView);

    }
}
