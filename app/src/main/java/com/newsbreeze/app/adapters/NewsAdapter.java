package com.newsbreeze.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newsbreeze.app.R;
import com.newsbreeze.app.databinding.NewsDataCardBinding;
import com.newsbreeze.app.helpers.DatabaseHelper;
import com.newsbreeze.app.helpers.Konstant;
import com.newsbreeze.app.models.Article;
import com.newsbreeze.app.ui.NewsContentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>  {

    private Context context;
    List<Article> articleList;
    DatabaseHelper databaseHelper;

    public NewsAdapter(Context context,List<Article> articleList){
        this.context = context;
        this.articleList = articleList;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(NewsDataCardBinding.
                inflate(LayoutInflater.from(parent.getContext())
                        ,parent
                        ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        databaseHelper = new DatabaseHelper(context);
        Article article = articleList.get(position);

        //Checking null is json data
        if (article.getUrlToImage()!=null){
            Glide.with(context).load(article.getUrlToImage()).into(holder.binding.image);
        }
        if (article.getTitle()!=null){
            holder.binding.title.setText(article.getTitle());
        }

        if (article.getDescription()!=null){
            holder.binding.description.setText(Html.fromHtml(article.getDescription()));
        }

        // Splitting the data from 2021-06-10T04:51:58Z to 2021-06-10
        String[] str = article.getPublishedAt().split("T");
        holder.binding.date.setText(str[0]);



        holder.binding.readBtn.setOnClickListener(v->{
            Intent intent = new Intent(context, NewsContentActivity.class);
            intent.putExtra(Konstant.KEY_IMAGE_URL,article.getUrlToImage());
            intent.putExtra(Konstant.KEY_TITLE,article.getTitle());
            intent.putExtra(Konstant.KEY_SOURCE_NAME,article.getSource().getName());
            intent.putExtra(Konstant.KEY_AUTHOR,article.getAuthor());
            intent.putExtra(Konstant.KEY_DESCRIPTION,article.getDescription());
            intent.putExtra(Konstant.KEY_CONTENT_URL,article.getUrl());
            intent.putExtra(Konstant.KEY_CONTENT,article.getContent());
            intent.putExtra(Konstant.KEY_DATE,article.getPublishedAt());
            context.startActivity(intent);

        });

        holder.binding.saveBtn.setOnClickListener(v ->
            bookmarkSelector(article.getTitle(),
                    article.getUrlToImage(),
                    article.getDescription(),
                    article.getAuthor(),
                    article.getContent(),
                    article.getUrl(),
                    article.getPublishedAt(),
                    holder));


        holder.binding.bookmarkBtn.setOnClickListener(v ->
           bookmarkSelector(article.getTitle(),
                   article.getUrlToImage(),
                   article.getDescription(),
                   article.getAuthor(),
                   article.getContent(),
                   article.getUrl(),
                   article.getPublishedAt(),
                   holder));


        // If the article is bookmarked it will checklist the item in recyclerview
        if (databaseHelper.getSpecificItemsID(article.getPublishedAt())){
            holder.binding.bookmarkBtn.setCardBackgroundColor(context.getResources().getColor(R.color.green));
        }


    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private NewsDataCardBinding binding;

        public ViewHolder(NewsDataCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    //Bookmark selector
    private void bookmarkSelector(
            String title,
            String image_url,
            String description,
            String author,
            String content,
            String url,
            String date,
            ViewHolder holder){

        if (databaseHelper.getSpecificItemsID(date)){
            DeleteDataFromDB(date,holder);
        }else {

            //Adding the specific data in sql db
            AddDataToDB(title,
                    image_url,
                    description,
                    author,
                    content,
                    url,
                    date,holder);
        }
    }

    private void DeleteDataFromDB( String date, ViewHolder holder) {
        databaseHelper.deleteDataFromDB( date);
        holder.binding.bookmarkBtn.setCardBackgroundColor(context.getResources().getColor(R.color.deep_grey));
        notifyDataSetChanged();
        Toast.makeText(context,"Removed from Bookmark",Toast.LENGTH_SHORT).show();

    }




    private void AddDataToDB(String title,
                             String image_url,
                             String description,
                             String author,
                             String content,
                             String url,
                             String date,ViewHolder holder) {
        boolean insertData = databaseHelper.addData(title,image_url,
                description,author,content,url,date);

        if (insertData){

            holder.binding.bookmarkBtn.setCardBackgroundColor(context.getResources().getColor(R.color.green));
            Toast.makeText(context,"Added to Bookmark",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }else {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
        }

    }



}
