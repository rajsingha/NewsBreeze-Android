package com.newsbreeze.app.ui;


import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.newsbreeze.app.R;
import com.newsbreeze.app.databinding.NewsContentActivityBinding;
import com.newsbreeze.app.helpers.DatabaseHelper;
import com.newsbreeze.app.helpers.Konstant;



public class NewsContentActivity extends AppCompatActivity {

    NewsContentActivityBinding binding;
    DatabaseHelper databaseHelper;
    private Boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewsContentActivityBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(binding.getRoot());
        initView();

    }

    private void initView() {

        databaseHelper = new DatabaseHelper(this);
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });


        //Getting all the data by Intent
        if (getIntent().getStringExtra(Konstant.KEY_IMAGE_URL)!=null){
            Glide.with(this).load(getIntent().getStringExtra(Konstant.KEY_IMAGE_URL)).into(binding.image);
        }
        if (getIntent().getStringExtra(Konstant.KEY_TITLE)!=null){
            binding.title.setText(getIntent().getStringExtra(Konstant.KEY_TITLE));
        }

        if (getIntent().getStringExtra(Konstant.KEY_AUTHOR)!=null){
            binding.author.setText(getIntent().getStringExtra(Konstant.KEY_AUTHOR));
        }
        if (getIntent().getStringExtra(Konstant.KEY_CONTENT)!=null){
            binding.content.setText(Html.fromHtml(getIntent().getStringExtra(Konstant.KEY_CONTENT)));
        }

        // Splitting the data from 2021-06-10T04:51:58Z to 2021-06-10
        String[] str = getIntent().getStringExtra(Konstant.KEY_DATE).split("T");
        binding.date.setText(str[0]);

        if (databaseHelper.getSpecificItemsID(getIntent().getStringExtra(Konstant.KEY_DATE))) {
            isSelected = true;
            binding.bookmarkBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_filled));

        }



        binding.bookmarkBtn.setOnClickListener(v -> {
            if (isSelected){
                databaseHelper.deleteDataFromDB(getIntent().getStringExtra(Konstant.KEY_DATE));
                binding.bookmarkBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_unfilled));
            }else {
                AddDataToDB(getIntent().getStringExtra(Konstant.KEY_TITLE),
                        getIntent().getStringExtra(Konstant.KEY_IMAGE_URL),
                        getIntent().getStringExtra(Konstant.KEY_DESCRIPTION),
                        getIntent().getStringExtra(Konstant.KEY_AUTHOR),
                        getIntent().getStringExtra(Konstant.KEY_CONTENT),
                        getIntent().getStringExtra(Konstant.KEY_CONTENT_URL),
                        getIntent().getStringExtra(Konstant.KEY_DATE));
            }

        });

    }

    private void AddDataToDB(String title,
                             String image_url,
                             String description,
                             String author,
                             String content,
                             String url,
                             String date) {
        boolean insertData = databaseHelper.addData(title,image_url,
                description,author,content,url,date);

        if (insertData){
            binding.bookmarkBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_filled));
            Toast.makeText(this,"Added to Bookmark",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }

    }

}
