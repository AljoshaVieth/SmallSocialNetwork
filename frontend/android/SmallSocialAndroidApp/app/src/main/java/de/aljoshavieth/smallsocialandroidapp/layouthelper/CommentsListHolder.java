package de.aljoshavieth.smallsocialandroidapp.layouthelper;

import android.view.View;
import android.widget.TextView;

import de.aljoshavieth.smallsocialandroidapp.R;

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
