package com.newsbreeze.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newsbreeze.app.databinding.SavedNewsDataCardBinding;
import com.newsbreeze.app.helpers.DatabaseHelper;
import com.newsbreeze.app.helpers.Konstant;
import com.newsbreeze.app.models.Article;
import com.newsbreeze.app.ui.NewsContentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.ViewHolder> {

    private Context context;
    List<Article> articles;
    DatabaseHelper databaseHelper;

    public SavedNewsAdapter(Context context,List<Article> articleList){
        this.context =context;
        this.articles =articleList;

    }

    @Override
    public SavedNewsAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new SavedNewsAdapter.ViewHolder(SavedNewsDataCardBinding.
                inflate(LayoutInflater.from(parent.getContext())
                        ,parent
                        ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        databaseHelper = new DatabaseHelper(context);
        Article data = articles.get(position);

        Glide.with(context).load(data.getUrlToImage()).into(holder.binding.image);
        holder.binding.title.setText(data.getTitle());

        // Splitting the data from 2021-06-10T04:51:58Z to 2021-06-10
        String[] str = data.getPublishedAt().split("T");
        holder.binding.dateNauthor.setText(str[0]+" . "+data.getAuthor());

        holder.binding.imgCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsContentActivity.class);
            intent.putExtra(Konstant.KEY_IMAGE_URL,data.getUrlToImage());
            intent.putExtra(Konstant.KEY_TITLE,data.getTitle());
            intent.putExtra(Konstant.KEY_AUTHOR,data.getAuthor());
            intent.putExtra(Konstant.KEY_DESCRIPTION,data.getDescription());
            intent.putExtra(Konstant.KEY_CONTENT_URL,data.getUrl());
            intent.putExtra(Konstant.KEY_CONTENT,data.getContent());
            intent.putExtra(Konstant.KEY_DATE,data.getPublishedAt());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private SavedNewsDataCardBinding binding;

        public ViewHolder(SavedNewsDataCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
