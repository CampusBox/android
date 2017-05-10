package in.exun.campusbox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.exun.campusbox.R;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

public class SinglePost extends AppCompatActivity {

    private CreativeJsonHandler mCreativeJsonHandler;
    String id = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        Bundle extra = getIntent().getExtras();
        id = extra.getString(AppConstants.TAG_OBJ);


    }
}
