package in.exun.campusbox.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stepstone.stepper.StepperLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.StepAdapter;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.helper.SessionManager;

public class AddEvent extends AppCompatActivity {

    private static final String TAG = "AddEvent";
    StepperLayout stepsLayout;
    StepAdapter mAdapter;
    public String stringTitle, stringSubtitle, stringFees, stringTimeStart, stringTimeEnd, stringDateEnd, stringDateStart;
    public String stringDesc, stringVenue, stringLink, stringName, stringPhone, stringOrgLInk;
    public int intType, intTarget, intFromPeriod, intToPeriod;
    public String stringImage;
    public JSONArray arrTags = new JSONArray();
    private SessionManager session;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        session = new SessionManager(this);
        stepsLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mAdapter = new StepAdapter(getSupportFragmentManager(), this);
        stepsLayout.setAdapter(mAdapter);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

    }

    @Override
    public void onBackPressed() {
        if (stepsLayout.getCurrentStepPosition() != 0)
            stepsLayout.setCurrentStepPosition(stepsLayout.getCurrentStepPosition() - 1);
        else
            finish();
    }

    public void addEvent() {

        pDialog.setMessage("Adding event ...");
        showDialog();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL_ADD_EVENT, getPostJson(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideDialog();
                try {
                    String status = response.getString("status");
                    if (status.equals("Registered Successfully")) {
                        Toast.makeText(AddEvent.this, "Event added succesfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(AddEvent.this, "Couldn't add event", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(AddEvent.this, "Error in internet connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Toast.makeText(AddEvent.this, "Error in internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String token = "Bearer " + session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, TAG);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private JSONObject getPostJson() {
        JSONObject postObj = new JSONObject();
        JSONObject eventObj = new JSONObject();

        if (stringTimeStart.substring(stringTimeStart.indexOf(" ") + 1).equals("AM"))
            intFromPeriod = 0;
        else
            intFromPeriod = 1;
        if (stringTimeEnd.substring(stringTimeEnd.indexOf(" ") + 1).equals("AM"))
            intToPeriod = 0;
        else
            intToPeriod = 1;

        try {

            eventObj.put("audience", intType + "");
            eventObj.put("description", stringDesc);
            eventObj.put("link", stringLink);
            eventObj.put("organiserName", stringName);
            eventObj.put("organiserPhone", stringPhone);
            if (stringOrgLInk != null)
                eventObj.put("organiserLink", stringOrgLInk);
            if (stringLink != null)
                eventObj.put("link", stringLink);
            eventObj.put("price", stringFees);
            eventObj.put("subtitle", stringSubtitle);
            eventObj.put("title", stringTitle);
            eventObj.put("type", intType);
            eventObj.put("venue", stringVenue);
            eventObj.put("fromDate", stringDateStart);
            eventObj.put("fromTime", stringTimeStart.substring(0, 5));
            eventObj.put("fromPeriod", intFromPeriod);
            eventObj.put("toDate", stringDateEnd);
            eventObj.put("toTime", stringTimeEnd.substring(0, 5));
            eventObj.put("toPeriod", intToPeriod);
            eventObj.put("croppedDataUrl", stringImage);

            postObj.put("tags", arrTags);
            postObj.put("event", eventObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postObj;
    }
}
