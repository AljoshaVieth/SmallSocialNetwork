package de.aljoshavieth.smallsocialandroidapp;

import android.view.View;
import android.widget.TextView;

public class CommentsListHolder {
    TextView commentsListTextView;
    TextView commentsListAuthorTextView;
    TextView commentsListTimeTextView;

    public CommentsListHolder(View v) {
        commentsListTextView = v.findViewById(R.id.commentsListContentTextView);
        commentsListAuthorTextView = v.findViewById(R.id.commentsListAuthorTextView);
        commentsListTimeTextView = v.findViewById(R.id.commentsListTimeTextView);

    }
}
