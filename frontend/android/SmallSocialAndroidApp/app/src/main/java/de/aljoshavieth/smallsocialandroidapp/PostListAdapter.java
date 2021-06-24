package de.aljoshavieth.smallsocialandroidapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

public class PostListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Post> posts;
    LayoutInflater inflater;

    public PostListAdapter(Context c, ArrayList<Post> posts) {
        this.context = c;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
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
        PostListHolder holder = new PostListHolder(convertView);
        holder.postListTextView.setText(posts.get(position).getContent());
        holder.postListAuthorTextView.setText("@" + posts.get(position).getAuthor().getName());
        //java.util.Date date=new java.util.Date((long)posts.get(position).getTime()*1000);

        String time = Utils.unixToFormattedTime(posts.get(position).getTime());



        //SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.GERMAN);
        //String time = dt.format(date);

        holder.postListTimeTextView.setText(time);
        holder.postListTextView.getRootView().setBackgroundColor(Color.parseColor(posts.get(position).getColor()));
        return convertView;
    }

}
