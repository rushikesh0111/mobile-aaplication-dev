package com.example.abc.newsaggregator.adapters;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.newsaggregator.activities.MainActivity;
import com.example.abc.newsaggregator.models.ArticleModel;
import com.example.abc.newsaggregator.controllers.DateTimeUtils;
import com.example.abc.newsaggregator.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private final MainActivity mainActivity;
    private final List<ArticleModel> articles;

    //
    //
    //
    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleTitle;
        TextView lastUpdatedDate;
        TextView author;
        ImageView image;
        TextView description;
        TextView articleCount;

        public ArticleViewHolder(@NonNull View itemView) {

            super(itemView);

            articleTitle = itemView.findViewById(R.id.article_title);
            lastUpdatedDate = itemView.findViewById(R.id.article_date);

            author = itemView.findViewById(R.id.article_author);
            image = itemView.findViewById(R.id.article_image);

            description = itemView.findViewById(R.id.article_description);
            articleCount = itemView.findViewById(R.id.article_number);

            description.setMovementMethod(new ScrollingMovementMethod());   // this will show Scrollable description
        }
    }

    //
    //
    //
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleModel article = articles.get(position);

        setTextField(holder.articleTitle, article.getTitle());
        setTextField(holder.author, article.getAuthor());
        setTextField(holder.description, article.getDescription());

        holder.articleCount.setText(
                String.format(Locale.getDefault(), "%d of %d", position + 1, articles.size()));

        if (article.getPublishedAt() != null) {
            holder.lastUpdatedDate.setText(DateTimeUtils.formatDateTime(article.getPublishedAt()));
            holder.lastUpdatedDate.setVisibility(View.VISIBLE);
        } else {
            holder.lastUpdatedDate.setVisibility(View.GONE);
        }

        if (article.getUrlToImage() != null) {
            Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.noimage);
        }

        holder.articleTitle.setOnClickListener(v -> onClick(article.getUrl()));
        holder.image.setOnClickListener(v -> onClick(article.getUrl()));
        holder.description.setOnClickListener(v -> onClick(article.getUrl()));
    }

    //
    //
    //
    public ArticleAdapter(MainActivity mainActivity, List<ArticleModel> articles) {
        this.mainActivity = mainActivity;
        this.articles = articles;
    }

    //
    //
    //
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ArticleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_entry, parent, false));
    }

    //
    //
    //this method will get the count of the articles
    @Override
    public int getItemCount() {

        return articles.size();
    }

    //
    //
    //
    public void onClick(String url) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
                mainActivity.startActivity(intent);
            }
        }
    }

    //
    //
    //
    private void setTextField(TextView textView, String content) {
        if (content != null) {
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

}
