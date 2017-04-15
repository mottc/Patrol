package com.mottc.patrol.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mottc.patrol.R;
import com.mottc.patrol.issued.IssuedActivity;
import com.mottc.patrol.manager.dummy.DummyContent;
import com.mottc.patrol.staff.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagerActivity extends AppCompatActivity implements IssuedFragment.OnListFragmentInteractionListener,ExamineFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.bnv_menu)
    BottomNavigationView mBnvMenu;

    private MenuItem mMenuItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        mToolbar.setTitle("管理");
        setSupportActionBar(mToolbar);
        setupViewPager(mViewpager);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBnvMenu.getMenu().getItem(0).setChecked(false);
                }
                mBnvMenu.getMenu().getItem(position).setChecked(true);
                mMenuItem = mBnvMenu.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_patrol:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.action_task:
                        mViewpager.setCurrentItem(1);

                        break;
                    case R.id.action_me:
                        mViewpager.setCurrentItem(2);

                        break;
                }
                return true;
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ExamineFragment.newInstance(),getString(R.string.examine));
        adapter.addFragment(IssuedFragment.newInstance(),getString(R.string.issued));
        adapter.addFragment(ManagerAccountFragment.newInstance(),getString(R.string.account));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        startActivity(new Intent(this, IssuedActivity.class));
    }
}
