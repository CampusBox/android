package in.exun.campusbox.fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.activity.SingleEvent;
import in.exun.campusbox.activity.SinglePost;
import in.exun.campusbox.adapters.CreativeAdapter;
import in.exun.campusbox.adapters.RVAEventsHome;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Home extends Fragment {

    private static final String TAG = "Home";
    private static final int PART_EVENTS = 10;
    private static final int PART_CREATIVITY = 11;

    private View rootView;
    private RecyclerView mRVEvents, mRVContents;
    private LinearLayout contWaitEvents, contWaitCreative;
    private CardView btnMoreEvents, btnMoreCreative;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabAddEvent, fabAddCreative, fabAddUpdate;

    EventJsonHandler mEventJsonHandler;
    CreativeJsonHandler mCreativeJsonHandler;
    private RecyclerView.Adapter mAdapterEvents, mAdapterCreative;
    private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerCreative;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initialise();

        populateEvents();
        populateCreative();

        return rootView;
    }

    private void initialise() {

        contWaitCreative = (LinearLayout) rootView.findViewById(R.id.container_creative_wait);
        contWaitEvents = (LinearLayout) rootView.findViewById(R.id.container_event_wait);
        btnMoreEvents = (CardView) rootView.findViewById(R.id.btn_see_all_events);
        btnMoreCreative = (CardView) rootView.findViewById(R.id.btn_see_all_creative);
        fabMenu = (FloatingActionsMenu) rootView.findViewById(R.id.fab_menu);
        fabAddCreative = (FloatingActionButton) rootView.findViewById(R.id.fab_home_add_creativity);
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fab_home_add_event);
        fabAddUpdate = (FloatingActionButton) rootView.findViewById(R.id.fab_home_add_profile);

        btnMoreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).displayView(1, true);
            }
        });

        btnMoreCreative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).displayView(3, true);
            }
        });

        fabAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add update", Toast.LENGTH_SHORT).show();
                fabMenu.collapse();
            }
        });

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add event", Toast.LENGTH_SHORT).show();
                fabMenu.collapse();
            }
        });

        fabAddCreative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add post", Toast.LENGTH_SHORT).show();
                fabMenu.collapse();
            }
        });
    }

    private void populateEvents() {

        mRVEvents = (RecyclerView) rootView.findViewById(R.id.rv_events);
        mRVEvents.setNestedScrollingEnabled(false);
        mLayoutManagerEvents = new LinearLayoutManager(getApplicationContext());
        mRVEvents.setLayoutManager(mLayoutManagerEvents);
        mAdapterEvents = new RVAEventsHome();
        mRVEvents.setAdapter(mAdapterEvents);

        fetchEvents();
    }

    private void populateCreative() {

        mRVContents = (RecyclerView) rootView.findViewById(R.id.rv_creative);
        mRVContents.setNestedScrollingEnabled(false);
        mLayoutManagerCreative = new LinearLayoutManager(getApplicationContext());
        mRVContents.setLayoutManager(mLayoutManagerCreative);
        mAdapterCreative = new CreativeAdapter();
        mRVContents.setAdapter(mAdapterCreative);

        fetchCreative();
    }

    public void fetchEvents() {

        updateUI(AppConstants.PROCESS_LOAD, PART_EVENTS);

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppConstants.URL_DASH_EVENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.has("data")) {
                                JSONArray data = resp.getJSONArray("data");
                                if (data.length() > 0) {
                                    mEventJsonHandler = new EventJsonHandler(data, null); //Sending null because we don't need pagination here
                                    mAdapterEvents = new RVAEventsHome(mEventJsonHandler);
                                    mRVEvents.setAdapter(mAdapterEvents);
                                    updateUI(AppConstants.PROCESS_SUCCESS, PART_EVENTS);
                                } else {
                                    updateUI(AppConstants.PROCESS_FAILURE, PART_EVENTS);
                                }
                            } else {
                                updateUI(AppConstants.PROCESS_FAILURE, PART_EVENTS);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE, PART_EVENTS);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateUI(AppConstants.PROCESS_FAILURE, PART_EVENTS);
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);

    }

    public void fetchCreative() {

        updateUI(AppConstants.PROCESS_LOAD, PART_CREATIVITY);
        String url = AppConstants.URL_DASH_CONTENTS + "?limit=4&offset=0";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.has("data")) {
                                JSONArray data = resp.getJSONArray("data");
                                if (data.length() > 0) {
                                    mCreativeJsonHandler = new CreativeJsonHandler(data, null); //Sending null because we don't need pagination here
                                    mAdapterCreative = new CreativeAdapter(getActivity(), mCreativeJsonHandler, data.length());
                                    mRVContents.setAdapter(mAdapterCreative);
                                    updateUI(AppConstants.PROCESS_SUCCESS, PART_CREATIVITY);
                                } else {
                                    updateUI(AppConstants.PROCESS_FAILURE, PART_CREATIVITY);
                                }
                            } else {
                                updateUI(AppConstants.PROCESS_FAILURE, PART_CREATIVITY);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE, PART_CREATIVITY);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);
    }

    private void updateUI(int reqCode, int partCode) {

        switch (reqCode) {
            case AppConstants.PROCESS_SUCCESS:
                if (partCode == PART_EVENTS) {
                    mRVEvents.setVisibility(View.VISIBLE);
                    btnMoreEvents.setVisibility(View.VISIBLE);
                    contWaitEvents.setVisibility(View.GONE);
                } else {
                    mRVContents.setVisibility(View.VISIBLE);
                    btnMoreCreative.setVisibility(View.VISIBLE);
                    contWaitCreative.setVisibility(View.GONE);
                }
                fabMenu.setVisibility(View.VISIBLE);
                break;
            case AppConstants.PROCESS_FAILURE:
                Toast.makeText(getActivity(), "Error in internet", Toast.LENGTH_SHORT).show();
                fabMenu.setVisibility(View.GONE);
                if (partCode == PART_EVENTS)
                    contWaitEvents.setVisibility(View.GONE);
                else
                    contWaitCreative.setVisibility(View.GONE);
                break;
            case AppConstants.PROCESS_LOAD:
                if (partCode == PART_EVENTS) {
                    mRVEvents.setVisibility(View.GONE);
                    btnMoreEvents.setVisibility(View.GONE);
                    contWaitEvents.setVisibility(View.VISIBLE);
                } else {
                    mRVContents.setVisibility(View.GONE);
                    btnMoreCreative.setVisibility(View.GONE);
                    contWaitCreative.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((RVAEventsHome) mAdapterEvents).setOnItemClickListener(new RVAEventsHome.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {

                switch (type) {
                    case 0:
                        Intent i = new Intent(getActivity(), SingleEvent.class);
                        i.putExtra(AppConstants.TAG_OBJ, mEventJsonHandler.getSingleData(position));
                        startActivity(i);
                        break;
                    case 1:
                        sendOneWyRequest(AppConstants.URL_APPRECIATE_EVENT + mEventJsonHandler.getId(position));
                        break;
                    case 2:
                        sendOneWyRequest(AppConstants.URL_ATTEND + mEventJsonHandler.getId(position));
                        break;
                    case 3:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this event. https://app.campusbox.org/#!/singleEvent/" + mEventJsonHandler.getId(position));
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                }
            }
        });

        ((CreativeAdapter) mAdapterCreative).setOnItemClickListener(new CreativeAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {
                switch (type) {
                    case 0:
                        Intent i = new Intent(getActivity(), SinglePost.class);
                        i.putExtra(AppConstants.TAG_OBJ, mCreativeJsonHandler.getId(position));
                        startActivity(i);
                        break;
                    case 1:
                        sendOneWyRequest(AppConstants.URL_APPRECIATE_POST + mCreativeJsonHandler.getId(position));
                        break;
                    case 2:
                        sendOneWyRequest(AppConstants.URL_BOOKMARK + mCreativeJsonHandler.getId(position));
                        break;
                    case 3:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this post. https://app.campusbox.org/#!/singleContent/" + mCreativeJsonHandler.getId(position));
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                }
            }
        });
    }

    private void sendOneWyRequest(String url) {
        Log.d(TAG, "sendOneWyRequest: " + url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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
                String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(TAG);
    }
}
