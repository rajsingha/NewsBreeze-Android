package com.newsbreeze.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.newsbreeze.app.adapters.SavedNewsAdapter;
import com.newsbreeze.app.databinding.SavedActivityBinding;
import com.newsbreeze.app.helpers.DatabaseHelper;
import com.newsbreeze.app.models.Article;

import java.util.ArrayList;
import java.util.Collections;

public class SavedActivity extends AppCompatActivity {

    SavedActivityBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SavedActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        databaseHelper = new DatabaseHelper(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        //At the initial non-filtered data will appeared in recyclerView
        displayNotes("");

        binding.searchBar.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (binding.searchBar.getRight() - binding.searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    binding.searchBar.setText("");
                    displayNotes("");
                }
            }
            return false;
        });


        //By pressing the enter button on keyboard the filter process will start
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String filter = binding.searchBar.getText().toString();
                displayNotes(filter);
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.searchBar.getWindowToken(), 0);
            return true;
        });
    }

    public void displayNotes(String filter) {
        ArrayList<Article> arrayList = new ArrayList<>(databaseHelper.getArticleData());
        if (filter.isEmpty()){
            // Not Filtered Data
            binding.recyclerView.setAdapter(new SavedNewsAdapter(this,arrayList));
        }else {

            // Filter the data by title of the article
            for (Article article:arrayList){
                if (article.getTitle().toLowerCase().startsWith(filter)){
                    binding.recyclerView.setAdapter(new SavedNewsAdapter(this,
                            Collections.singletonList(article)));
                }
            }
        }


    }
}
