package in.exun.campusbox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import in.exun.campusbox.R;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

public class SinglePost extends AppCompatActivity {

    private CreativeJsonHandler mCreativeJsonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        Bundle extra = getIntent().getExtras();
        ((TextView)findViewById(R.id.text_test)).setText("Such design\nMuch wow\n10/10 \n" + extra.getInt(AppConstants.TAG_OBJ));


    }
}
