package in.exun.campusbox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

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

        try {
            JSONArray arr = new JSONArray(extra.getString(AppConstants.TAG_OBJ));
            mCreativeJsonHandler = new CreativeJsonHandler(arr,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.text_test)).setText("Such design\nMuch wow\n10/10 \n" + mCreativeJsonHandler.getTitle(0));
    }
}
