package com.mottc.patrol.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.ImageURL;
import com.mottc.patrol.data.source.local.ImageURLDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowImagesActivity extends AppCompatActivity {

    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String currentUsername;
    private ImageURLDao mImageURLDao;
    private List<String> imageUrls;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        ButterKnife.bind(this);


        mToolbar.setTitle("所有损坏");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        currentUsername = PatrolApplication.getInstance().getCurrentUserName();
        mImageURLDao = PatrolApplication.getInstance().getDaoSession().getImageURLDao();
        imageUrls = new ArrayList<>();

        List<ImageURL> list =
                mImageURLDao
                        .queryBuilder()
                        .where(ImageURLDao.Properties.Manager.eq(currentUsername))
                        .list();

        for (ImageURL imageUrl : list) {
            imageUrls.add(imageUrl.getUrl());
        }


        mGridView.setAdapter(new ImageAdapter(imageUrls, this));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ShowImagesActivity.this, ShowDetailImageActivity.class).putExtra("url", imageUrls.get(position)));
            }
        });


    }
}
