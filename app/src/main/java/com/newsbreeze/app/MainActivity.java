package com.newsbreeze.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.newsbreeze.app.adapters.NewsAdapter;
import com.newsbreeze.app.databinding.ActivityMainBinding;
import com.newsbreeze.app.helpers.Konstant;
import com.newsbreeze.app.models.Article;
import com.newsbreeze.app.models.NewsData;
import com.newsbreeze.app.network.api.ApiClient;
import com.newsbreeze.app.ui.SavedActivity;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private String COUNTRY_CODE = "in";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        binding.savedItems.setOnClickListener(v -> startActivity(new Intent(this, SavedActivity.class)));

        //At the initial non-filtered data will appeared in recyclerView
        getFilteredData("");

        binding.searchBar.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (binding.searchBar.getRight() - binding.searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    binding.searchBar.setText("");
                    getFilteredData("");
                }
            }
            return false;
        });

        //By pressing the enter button on keyboard the filter process will start
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String filter = binding.searchBar.getText().toString();
                getFilteredData(filter);
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.searchBar.getWindowToken(), 0);
            return true;
        });

    }


    // if {filter string is empty it will return all data from json,} else { it will return filtered data by title}
    private void getFilteredData(String filter){
        Call<NewsData> call = ApiClient
                .getInstance()
                .getApi()
                .getNewsData(COUNTRY_CODE, Konstant.INSTANCE.getAPIKey());

            call.enqueue(new Callback<NewsData>() {
                @Override
                public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                    if (response.isSuccessful()) {
                        if (filter.isEmpty()) {
                            recyclerView.setAdapter(new NewsAdapter(
                                    MainActivity.this,
                                    response.body().getArticles()));
                        }else {
                            List<Article> articleList = response.body().getArticles();
                            for (Article data:articleList){
                                if (data.getTitle().toLowerCase().startsWith(filter)){
                                    recyclerView.setAdapter(new NewsAdapter(
                                            MainActivity.this,
                                            Collections.singletonList(data)));
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<NewsData> call, Throwable t) {
                    Toast.makeText(MainActivity.this,
                            "Looks like you don't have an internet connection",Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}