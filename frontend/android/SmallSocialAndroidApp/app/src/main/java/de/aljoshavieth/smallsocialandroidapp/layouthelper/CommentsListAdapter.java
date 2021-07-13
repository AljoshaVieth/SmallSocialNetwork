package de.aljoshavieth.smallsocialandroidapp.layouthelper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import de.aljoshavieth.smallsocialandroidapp.models.Comment;
import de.aljoshavieth.smallsocialandroidapp.R;
import de.aljoshavieth.smallsocialandroidapp.Utils;

public class CommentsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Comment> comments;
    LayoutInflater inflater;
    private String backgroundColor;

    public CommentsListAdapter(Context c, ArrayList<Comment> comments, String backgroundColor) {
        this.context = c;
        this.comments = comments;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.post_list, parent, false);

        }
        //BIND DATA
        CommentsListHolder holder = new CommentsListHolder(convertView);
        holder.commentsListTextView.setText(comments.get(position).getContent());
        holder.commentsListAuthorTextView.setText("@" + comments.get(position).getAuthor().getName());

        String time = Utils.unixToFormattedTime(comments.get(position).getTime());
        holder.commentsListTimeTextView.setText(time);
        holder.commentsListTextView.getRootView().setBackgroundColor(Color.parseColor(backgroundColor));

        return convertView;
    }

}
