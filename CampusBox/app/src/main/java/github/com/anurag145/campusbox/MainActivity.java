package github.com.anurag145.campusbox;

import android.app.Dialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import github.com.anurag145.campusbox.Fragments.Add;
import github.com.anurag145.campusbox.Fragments.Creativity;
import github.com.anurag145.campusbox.Fragments.Events;
import github.com.anurag145.campusbox.Fragments.Home;
import github.com.anurag145.campusbox.Fragments.Me;
import github.com.anurag145.campusbox.Fragments.Search;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;
    TabLayout mTabLayout2;
    Bundle item;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        item=savedInstanceState;
       mTabLayout2=(TabLayout)findViewById(R.id.tabs2);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);
        mTabLayout2.setSelectedTabIndicatorHeight(0);
        mTabLayout2.addTab(mTabLayout2.newTab().setText("search"));
        mTabLayout2.addTab(mTabLayout2.newTab().setText("add"));
        mTabLayout2.addTab(mTabLayout2.newTab().setText("Me"));

    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {

       int count=3;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Log.d("message ","hello"+String.valueOf(" "+position));
            switch (position) {
                case 0:
                    return Home.newInstance();
                case 1:
                    return Events.newInstance();
                case 2:
                    return Creativity.newInstance();

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Events";
                case 2:
                    return "Creativity";

            }
            return "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
