package in.exun.campusbox.fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.activity.SingleEvent;
import in.exun.campusbox.adapters.RVAEvents;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Events extends Fragment {

    private static final String TAG = "Events";

    private RecyclerView mRecyclerView;
    private RVAEvents mAdapter;
    private EventJsonHandler mEventJsonHandler;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton fabAddEvents;
    private View rootView;
    private LinearLayout contWait;
    private boolean maxLimitReached = false, loading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_events, container, false);

        initialise();
        populateEvents();

        return rootView;
    }

    private void initialise() {


        fabAddEvents = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event);
        contWait = (LinearLayout) rootView.findViewById(R.id.container_event_wait);

        fabAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add new event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateEvents() {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_events);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RVAEvents();
        mRecyclerView.setAdapter(mAdapter);

        fetchEvents();
    }

    public void nextRequest() {

        Log.d(TAG, "nextRequest: Get more data boi");
        updateUI(AppConstants.PROCESS_LOAD_MORE);

        String url = AppConstants.URL_EVENTS + mEventJsonHandler.urlPagination();
        Log.d(TAG, "nextRequest: " + url);

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resp = new JSONObject(response);
                            JSONArray newData = resp.getJSONArray("data");
                            int temp = newData.length();
                            JSONObject meta = resp.getJSONObject("meta");
                            JSONArray data = mEventJsonHandler.getData();
                            for (int i = 0; i < newData.length(); i++)
                                data.put(newData.getJSONObject(i));
                            mEventJsonHandler = new EventJsonHandler(data,meta);
                            Log.d(TAG, "onResponse: " + temp + " " + mEventJsonHandler.Length());
                            updateUI(AppConstants.PROCESS_SUCCESS);

                            if (temp != 0) {
                                mAdapter.updateHandler(mEventJsonHandler, temp);
                                if (mEventJsonHandler.getLimit() != temp) {maxLimitReached = true;
                                    Log.d(TAG, "onResponse: Data limit reached");
                                    mAdapter.removeEnd();
                                }
                            } else {
                                Log.d(TAG, "onResponse: Data limit reached");
                                mAdapter.removeEnd();
                                maxLimitReached = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
               // String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTE1OTE4NjUsImV4cCI6MTQ5NDE4Mzg2NSwianRpIjoiMTB6WkF5cmk3MzA2TkZnYmtKM0VrZyIsInVzZXJuYW1lIjoiYW51cmFnMTQ1IiwiY29sbGVnZV9pZCI6MX0.YGSpRu6bPUtiNWOExQ_za-OjkKgi_uVIaikDuQXM_cY");

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(req, TAG);

    }

    public void fetchEvents() {

        updateUI(AppConstants.PROCESS_LOAD);

        String url = AppConstants.URL_EVENTS + "limit=2&offset=0";
        Log.d(TAG, "fetchEvents: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.has("data")) {
                                JSONArray data = resp.getJSONArray("data");
                                JSONObject metadata = resp.getJSONObject("meta");
                                if (data.length() > 0) {
                                    mEventJsonHandler = new EventJsonHandler(data, metadata);
                                    mAdapter = new RVAEvents(mEventJsonHandler);
                                    mRecyclerView.setAdapter(mAdapter);
                                    updateUI(AppConstants.PROCESS_SUCCESS);
                                } else {
                                    updateUI(AppConstants.PROCESS_FAILURE);
                                }
                            } else {
                                updateUI(AppConstants.PROCESS_FAILURE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "error => " + error.toString());
                        updateUI(AppConstants.PROCESS_FAILURE);
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

    }

    private void updateUI(int reqCode) {

//        Log.d(TAG, "updateUI: " + reqCode);
        switch (reqCode) {
            case AppConstants.PROCESS_SUCCESS:
                Log.d(TAG, "updateUI: success");
                mRecyclerView.setVisibility(View.VISIBLE);
                fabAddEvents.setVisibility(View.VISIBLE);
                contWait.setVisibility(View.GONE);
                mRecyclerView.addOnScrollListener(onScrollListener);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading = false;
                break;
            case AppConstants.PROCESS_FAILURE:
                Log.d(TAG, "updateUI: fail");
                Toast.makeText(getActivity(), "Error in internet", Toast.LENGTH_SHORT).show();
                fabAddEvents.setVisibility(View.GONE);
                contWait.setVisibility(View.GONE);
                break;
            case AppConstants.PROCESS_LOAD:
                Log.d(TAG, "updateUI: load");
                maxLimitReached = false;
                loading = true;
                mRecyclerView.setVisibility(View.GONE);
                fabAddEvents.setVisibility(View.GONE);
                contWait.setVisibility(View.VISIBLE);
                break;
            case AppConstants.PROCESS_LOAD_MORE:
                Log.d(TAG, "updateUI: load more");
                contWait.setVisibility(View.GONE);
                loading = true;
                break;
        }
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {

            if (!loading) {
                if (mAdapter.getFlag() && !maxLimitReached) {
                    int visible = mLayoutManager.findLastCompletelyVisibleItemPosition() - 2;
                    if (visible == mAdapter.getPrevCount())
                        nextRequest();
                    else
                        Log.d(TAG, "onScrolled: " + visible + " != " + mAdapter.getPrevCount());
                } else if (maxLimitReached) {
                    mRecyclerView.removeOnScrollListener(onScrollListener);
                    Log.d(TAG, "onScrolled: All data got, exiting paging");
                } else if (!mAdapter.getFlag())
                    Log.d(TAG, "onScrolled: Flag negative");
            } else {
                Log.d(TAG, "onScrolled: Loading....");
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();

        ((RVAEvents) mAdapter).setOnItemClickListener(new RVAEvents.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {

                switch (type) {
                    case 0:
                        try {
                            Log.e("Anurag",String.valueOf(v.getId()));
                            Intent i = new Intent(getActivity(), SingleEvent.class);
                            i.putExtra(AppConstants.TAG_OBJ, v.getContentDescription());
                            startActivity(i);
                        }catch (Exception e)

                        {
                            Log.e("Tag",e.toString());
                        }
                        break;
                    case 1:
                        sendOneWyRequest(AppConstants.URL_APPRECIATE_EVENT + mEventJsonHandler.getId(position));
                        break;
                    case 2:
                        sendOneWyRequest(AppConstants.URL_ATTEND + mEventJsonHandler.getId(position));
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "Filter", Toast.LENGTH_SHORT).show();
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
              //  String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTE1OTE4NjUsImV4cCI6MTQ5NDE4Mzg2NSwianRpIjoiMTB6WkF5cmk3MzA2TkZnYmtKM0VrZyIsInVzZXJuYW1lIjoiYW51cmFnMTQ1IiwiY29sbGVnZV9pZCI6MX0.YGSpRu6bPUtiNWOExQ_za-OjkKgi_uVIaikDuQXM_cY");

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
