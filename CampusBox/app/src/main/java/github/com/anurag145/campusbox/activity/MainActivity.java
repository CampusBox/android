package github.com.anurag145.campusbox.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import github.com.anurag145.campusbox.fragments.Creativity;
import github.com.anurag145.campusbox.fragments.Events;
import github.com.anurag145.campusbox.fragments.Home;
import github.com.anurag145.campusbox.R;

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
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.ic_home_white_24dp).setText("home"));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.ic_event_white_24dp).setText("event"));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.ic_blur_on_white_24dp).setText("Creativity"));
        mViewPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout2.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout2.setSelectedTabIndicatorHeight(0);
        mTabLayout.getTabAt(0).setIcon(R.mipmap.ic_home_white_24dp).setText("home");
        mTabLayout.getTabAt(1).setIcon(R.mipmap.ic_event_white_24dp).setText("event");
        mTabLayout.getTabAt(2).setIcon(R.mipmap.ic_blur_on_white_24dp).setText("Creativity");
        mTabLayout2.addTab(mTabLayout2.newTab().setIcon(R.mipmap.ic_search_white_24dp).setText("search"));
        mTabLayout2.addTab(mTabLayout2.newTab().setIcon(R.mipmap.ic_add_circle_white_24dp).setText("add"));
        mTabLayout2.addTab(mTabLayout2.newTab().setIcon(R.mipmap.ic_person_white_24dp).setText("Me"));

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
