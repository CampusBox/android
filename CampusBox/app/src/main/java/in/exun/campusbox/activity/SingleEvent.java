package in.exun.campusbox.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.iojjj.rcbs.RoundedCornersBackgroundSpan;

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
import in.exun.campusbox.helper.SessionManager;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;
import mabbas007.tagsedittext.TagsEditText;

public class SingleEvent extends AppCompatActivity {

    private static final String TAG = "SingleEvent";
    EventJsonHandler mEventJsonHandler;
    SessionManager sessionManager;
    private final int data=0;
    private CardView cardView;
    private ImageView imgLike, imgEvent, btnShare;
    private TextView textDate, textTitle, textVenue, textsub, textLike,textby,textDesc,textCost,textTags;
    private LinearLayout btnLike, btnRSVP, optRSVP, optAttending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        sessionManager= new SessionManager(getApplicationContext());
        Bundle extra = getIntent().getExtras();
        requestData( AppConstants.URL_SINGLE_EVENT+extra.getString(AppConstants.TAG_OBJ.trim()));
        Log.e(TAG, "onCreate:  " );
    }

    public void fillData()
    { InitialiseData();
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this event. https://app.campusbox.org/#!/singleEvent/" + mEventJsonHandler.getId(data));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Boolean.parseBoolean(textLike.getContentDescription().toString())) {
                    imgLike.setImageResource(R.drawable.ic_appreciate_empty);
                    textLike.setText("Appreciate");
                    sendOneWyRequest(Request.Method.DELETE,AppConstants.URL_APPRECIATE_EVENT + mEventJsonHandler.getId(data));
                } else {
                    imgLike.setImageResource(R.drawable.ic_appreciate);
                    textLike.setText("Appreciated");
                    sendOneWyRequest(Request.Method.POST,AppConstants.URL_APPRECIATE_EVENT + mEventJsonHandler.getId(data));
                }
               textLike.setContentDescription(String.valueOf(!Boolean.parseBoolean(textLike.getContentDescription().toString())));
            }

        });
        btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optAttending.getVisibility()==View.VISIBLE) {
                    optAttending.setVisibility(View.GONE);
                    optRSVP.setVisibility(View.VISIBLE);
                    sendOneWyRequest(Request.Method.POST,AppConstants.URL_ATTEND+mEventJsonHandler.getId(data));
                } else {
                    optAttending.setVisibility(View.VISIBLE);
                    optRSVP.setVisibility(View.GONE);
                    sendOneWyRequest(Request.Method.DELETE,AppConstants.URL_ATTEND+mEventJsonHandler.getId(data));
                }

            }
        });

        if (mEventJsonHandler.isAttending(data)) {
            optAttending.setVisibility(View.GONE);
            optRSVP.setVisibility(View.VISIBLE);
        } else {
            optAttending.setVisibility(View.VISIBLE);
            optRSVP.setVisibility(View.GONE);
        }

        if (mEventJsonHandler.isAppreciated(data)) {
            imgLike.setImageResource(R.drawable.ic_appreciate);
            textLike.setText("Appreciated");
            textLike.setContentDescription(String.valueOf(mEventJsonHandler.isAppreciated(data)));
        } else {
            imgLike.setImageResource(R.drawable.ic_appreciate_empty);
            textLike.setText("Appreciate");
            textLike.setContentDescription(String.valueOf(mEventJsonHandler.isAppreciated(data)));
        }
        try {
            textDate.setText(mEventJsonHandler.getDate(data));
        } catch (Exception e) {
            textDate.setText("TBA");
        }

        textVenue.setText(mEventJsonHandler.getVenue(data));
        textsub.setText(mEventJsonHandler.getDesc(data));
        textTitle.setText(mEventJsonHandler.getTitle(data));
        imgEvent.setImageBitmap(mEventJsonHandler.getImage(data));
        textCost.setText(mEventJsonHandler.getCost(data));
        textby.setText(mEventJsonHandler.getAuthorName(data));
        String str=mEventJsonHandler.getTag(data);
        Log.e(TAG, "fillData: "+str );
        textDesc.setText("\n\n"+Html.fromHtml(mEventJsonHandler.getDescription(data))+"\n\n");
        StringTokenizer st= new StringTokenizer(str); int count=0;
        try {
            RoundedCornersBackgroundSpan.TextPartsBuilder ob= new RoundedCornersBackgroundSpan.TextPartsBuilder(this)
                    .setTextPadding(8)
                    .setCornersRadius(6)
                    .setTextPaddingRes(R.dimen.padding_span)
                    .setCornersRadiusRes(R.dimen.radius_span)
                    .setSeparator(RoundedCornersBackgroundSpan.DEFAULT_SEPARATOR);

            while(st.hasMoreTokens())
            {   Log.e(TAG, "fillData: " );
               ob.addTextPart("#"+st.nextToken(),Color.parseColor("#ececec"));
                ob.addTextPart(" ");
            }

            final Spannable spannable= ob.build();
          textTags.setText(spannable);


        }catch (Exception e)
        {

        }


    }
    public void InitialiseData()
    { cardView=(CardView)findViewById(R.id.eventCard);
        imgLike = (ImageView) findViewById(R.id.Appreciated_image);
        textDate = (TextView) findViewById(R.id.date);
        textTitle = (TextView) findViewById(R.id.event_name);
        textVenue = (TextView) findViewById(R.id.venue);
        textsub = (TextView) findViewById(R.id.event_desc);
        textDesc = (TextView) findViewById(R.id.desc);
        textby = (TextView) findViewById(R.id.by);
        textCost=(TextView)findViewById(R.id.cost);
        textTags=(TextView)findViewById(R.id.tag);
        textLike = (TextView) findViewById(R.id.text_like);
        imgEvent = (ImageView)findViewById(R.id.event_image);
        btnLike = (LinearLayout) findViewById(R.id.btn_like);
        btnRSVP = (LinearLayout) findViewById(R.id.btn_rsvp);
        btnShare = (ImageView) findViewById(R.id.btn_share);
        optRSVP = (LinearLayout) findViewById(R.id.container_rsvp);
        optAttending = (LinearLayout) findViewById(R.id.container_attending);

    }
    public void  requestData(String url)
    {
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try { Log.e(TAG, "onResponse: " );
                            JSONObject resp = new JSONObject(response);

                            JSONArray data = resp.getJSONArray("data");

                            mEventJsonHandler = new EventJsonHandler(data, null);

                            fillData();
                        }catch(Exception e)
                        {

                        }

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
                String token = "Bearer "+sessionManager.getLoginToken()  ;
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);

    }
    private void sendOneWyRequest(int request,String url) {
        Log.d(TAG, "sendOneWyRequest: " + url);

        StringRequest postRequest = new StringRequest(request, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                String token = "Bearer " + sessionManager.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization",token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);
    }

}
