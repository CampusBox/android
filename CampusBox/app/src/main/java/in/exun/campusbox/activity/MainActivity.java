package in.exun.campusbox.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import in.exun.campusbox.R;
import in.exun.campusbox.fragments.Main.Creativity;
import in.exun.campusbox.fragments.Main.Events;
import in.exun.campusbox.fragments.Main.Home;
import in.exun.campusbox.fragments.Main.Profile;
import in.exun.campusbox.fragments.Main.Search;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    LinearLayout navHome, navEvent, navSearch, navCreativity, navMe;
    public SessionManager session;
    public Fragment fragment;
    private int currIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            String url = intent.getData().toString();
            if (url.contains("/singleEvent/")){
                String id = url.substring(url.indexOf("Event/") + 6);
                Log.d(TAG, "onCreate: Event "+ id) ;
                Intent i = new Intent(this, SingleEvent.class);
                i.putExtra(AppConstants.TAG_OBJ, id);
                startActivity(i);
            } else if (url.contains("/singleContent/")){
                String id = url.substring(url.indexOf("Content/") + 8);
                Log.d(TAG, "onCreate: Creativity "+ id) ;
                Intent i = new Intent(this, SinglePost.class);
                i.putExtra(AppConstants.TAG_OBJ, id);
                startActivity(i);
            }

        }
        setupBottomNav();

    }

    private void initialise() {

        session = new SessionManager(getApplicationContext());
        displayView(1, false);
    }

    private void setupBottomNav() {

        navHome = (LinearLayout) findViewById(R.id.nav_home);
        navEvent = (LinearLayout) findViewById(R.id.nav_event);
        navSearch = (LinearLayout) findViewById(R.id.nav_search);
        navCreativity = (LinearLayout) findViewById(R.id.nav_creativity);
        navMe = (LinearLayout) findViewById(R.id.nav_me);

        navHome.setOnClickListener(this);
        navEvent.setOnClickListener(this);
        navSearch.setOnClickListener(this);
        navCreativity.setOnClickListener(this);
        navMe.setOnClickListener(this);

    }

    public void displayView(int index, boolean allow) {
        fragment = new Fragment();

        if (index != currIndex) {

            switch (index) {
                case 0:
                    fragment = new Home();
                    setSelectedNav((LinearLayout) findViewById(R.id.nav_home));
                    break;
                case 1:
                    fragment = new Events();
                    setSelectedNav((LinearLayout) findViewById(R.id.nav_event));
                    break;
                case 2:
                    fragment = new Search();
                    setSelectedNav((LinearLayout) findViewById(R.id.nav_search));
                    break;
                case 3:
                    fragment = new Creativity();
                    setSelectedNav((LinearLayout) findViewById(R.id.nav_creativity));
                    break;
                case 4:
                    fragment = new Profile();
                    setSelectedNav((LinearLayout) findViewById(R.id.nav_me));
                    break;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragment != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.disallowAddToBackStack();
                if (allow)
                    if (index > currIndex)
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    else
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
                invalidateOptionsMenu();
            }

            currIndex = index;
        }

    }

    public void signOut() {
        Log.d(TAG, "onTabSelected: Signing out");
        session.setLoginToken("-1");
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_home:
                displayView(0, true);
                break;
            case R.id.nav_event:
                displayView(1, true);
                break;
            case R.id.nav_search:
                displayView(2, true);
                break;
            case R.id.nav_creativity:
                displayView(3, true);
                break;
            case R.id.nav_me:
                displayView(4, true);
                break;
        }
    }

    private void setSelectedNav(View selectedNav) {

        switch (currIndex) {
            case 0:
                ((LinearLayout) findViewById(R.id.nav_home)).setBackgroundColor(Color.parseColor("#0570C0"));
            case 1:
                ((LinearLayout) findViewById(R.id.nav_event)).setBackgroundColor(Color.parseColor("#0570C0"));
            case 2:
                ((LinearLayout) findViewById(R.id.nav_search)).setBackgroundColor(Color.parseColor("#0570C0"));
            case 3:
                ((LinearLayout) findViewById(R.id.nav_creativity)).setBackgroundColor(Color.parseColor("#0570C0"));
            case 4:
                ((LinearLayout) findViewById(R.id.nav_me)).setBackgroundColor(Color.parseColor("#0570C0"));
        }

        selectedNav.setBackgroundColor(Color.parseColor("#06558F"));
    }
}
