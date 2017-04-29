package in.exun.campusbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import in.exun.campusbox.R;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.SessionManager;

public class Splash extends AppCompatActivity {

    // Session Manager
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Session Manager
        session = new SessionManager(getApplicationContext());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (session.getLoginToken().equals("-1")){
                  //  startActivity(new Intent(Splash.this,MainActivity.class).putExtra(AppConstants.TAG_TOKEN,session.getLoginToken()));

                    startActivity(new Intent(Splash.this,Login.class));
                } else {
                    startActivity(new Intent(Splash.this,MainActivity.class).putExtra(AppConstants.TAG_TOKEN,session.getLoginToken()));
                }

                finish();

            }
        }, 2000);
    }
}
