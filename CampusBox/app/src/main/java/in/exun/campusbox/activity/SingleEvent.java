package in.exun.campusbox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.RVAEvents;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

public class SingleEvent extends AppCompatActivity {

    private static final String TAG = "SingleEvent";
    EventJsonHandler mEventJsonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        TextView myTV = (TextView)findViewById(R.id.Tags);
        String  textString = "#hello #hello";
        StringTokenizer stringTokenizer= new StringTokenizer(textString);

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
            spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 0,textString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Spannable spannable=Spannable.Factory.getInstance().newSpannable(" ");
                myTV.setText(TextUtils.concat(spanText,spannable,spanText));
        Bundle extra = getIntent().getExtras();
        String url = AppConstants.URL_SINGLE_EVENT + extra.getString(AppConstants.TAG_OBJ);
        Log.d(TAG, "fetchEvent: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                                     Log.e("TAG",response);

                                                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                //String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTE1OTE4NjUsImV4cCI6MTQ5NDE4Mzg2NSwianRpIjoiMTB6WkF5cmk3MzA2TkZnYmtKM0VrZyIsInVzZXJuYW1lIjoiYW51cmFnMTQ1IiwiY29sbGVnZV9pZCI6MX0.YGSpRu6bPUtiNWOExQ_za-OjkKgi_uVIaikDuQXM_cY");

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);


        ((TextView)findViewById(R.id.text_test)).setText("Such design\nMuch wow\n10/10 \n" + /*mEventJsonHandler.getTitle(0)*/"\n\n"+extra.getString(AppConstants.TAG_OBJ));
    }
}
