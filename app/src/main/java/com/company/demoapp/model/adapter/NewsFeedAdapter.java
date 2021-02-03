package com.company.demoapp.model.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.demoapp.R;
import com.company.demoapp.model.dto.NewsArticleModel;
import com.company.demoapp.model.interfaces.IMessages;
import com.company.demoapp.model.utils.Messages;
import com.company.demoapp.model.interfaces.OnRecyclerViewItemClickListener;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private final List<NewsArticleModel> newsArticleModelList;
    private final IMessages messages;
    private final String INPUT_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final String OUT_FORMAT = "hh:mm a, dd-MM-yyyy";
    private OnRecyclerViewItemClickListener<NewsFeedViewHolder, NewsArticleModel> onRecyclerViewItemClickListener;

    public NewsFeedAdapter(List<NewsArticleModel> newsArticleModelList, Context context) {
        this.newsArticleModelList = newsArticleModelList;
        messages = new Messages(context);
    }

    @NonNull
    @Override
    public NewsFeedAdapter.NewsFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_feed_item, parent, false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new NewsFeedAdapter.NewsFeedViewHolder(view);
    }

    public void setOnRecyclerViewItemClickListener(@NonNull OnRecyclerViewItemClickListener<NewsFeedAdapter.NewsFeedViewHolder, NewsArticleModel> onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.NewsFeedViewHolder holder, int position) {
        try {
            NewsArticleModel newsArticleModel = newsArticleModelList.get(position);
            holder.titleTextView.setText(messages.setTextBold(newsArticleModel.getTitle()));
            holder.authorTextView.setText(newsArticleModel.getAuthor());
            holder.descriptionTextView.setText(newsArticleModel.getDescription());
            messages.loadImagePicasso(holder.feedImageView, newsArticleModel.getUrlToImage());
            holder.dateTextView.setText(messages.changeDateFormat(INPUT_FORMAT, OUT_FORMAT, newsArticleModel.getPublishedAt()));

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return newsArticleModelList.size();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class NewsFeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, descriptionTextView, authorTextView, dateTextView;
        ImageView feedImageView, shareImageView;

        NewsFeedViewHolder(View itemView) {
            super(itemView);
            try {
                feedImageView = itemView.findViewById(R.id.image_viewFeed);
                titleTextView = itemView.findViewById(R.id.text_viewTitle);
                descriptionTextView = itemView.findViewById(R.id.text_viewDescription);
                authorTextView = itemView.findViewById(R.id.text_viewAuthor);
                dateTextView = itemView.findViewById(R.id.text_viewDate);
                shareImageView = itemView.findViewById(R.id.image_viewShare);

                itemView.setOnClickListener(this);
                shareImageView.setOnClickListener(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onRecyclerViewItemClick(view, newsArticleModelList.get(getAdapterPosition()));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}



