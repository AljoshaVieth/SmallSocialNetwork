package de.aljoshavieth.smallsocialandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CommentsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Comment> comments;
    LayoutInflater inflater;

    public CommentsListAdapter(Context c, ArrayList<Comment> comments) {
        this.context = c;
        this.comments = comments;
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
        return convertView;
    }

}
