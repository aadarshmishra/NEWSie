package com.example.aadarsh.newsie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Aadarsh on 3/24/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView titleNews,publisher,timeAgo;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            titleNews = itemView.findViewById(R.id.titleNews);
            publisher = itemView.findViewById(R.id.publisher);
            timeAgo = itemView.findViewById(R.id.timeAgo);
        }
    }

    public NewsAdapter(List<News> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final News news = newsList.get(position);
        Glide.with(context).load(news.getImgURL()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
        holder.titleNews.setText(news.getTitle());
        holder.publisher.setText(news.getPublisher());
        holder.timeAgo.setText(news.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Webview.class);
                intent.putExtra("url", news.getURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
