package in.exun.campusbox.fragments.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.activity.SingleEvent;
import in.exun.campusbox.activity.SinglePost;
import in.exun.campusbox.adapters.RVACreativeHome;
import in.exun.campusbox.adapters.RVAEventsHome;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Anurag145 on 4/3/2017.
 */

public class Search extends Fragment {

    private static final String TAG = "Search";
    private static final int PART_EVENTS = 10;
    private static final int PART_CREATIVE = 11;
    private static final int PART_PROFILE = 12;
    private View rootView;
    private EditText inputSearch;
    private Spinner spinner;

    private RecyclerView mRecyclerView;
    private RVAEventsHome mAdapterEvents;
    private RVACreativeHome mAdapterCreative;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout contaWaitNorm, contaWaitProfile;

    private EventJsonHandler mEventJsonHandler;
    private CreativeJsonHandler mCreativeJsonHandler;
    private TextView imgBack;
    private ImageView btnClose;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        initialise();

        return rootView;
    }

    private void initialise() {

        inputSearch = (EditText) rootView.findViewById(R.id.input_search);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_search);
        contaWaitNorm = (LinearLayout) rootView.findViewById(R.id.container_wait_normal);
        contaWaitProfile = (LinearLayout) rootView.findViewById(R.id.container_wait_profile);
        imgBack = (TextView) rootView.findViewById(R.id.img_back);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_search);
        btnClose = (ImageView) rootView.findViewById(R.id.btn_close);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        updateUI(AppConstants.PROCESS_RESET, spinner.getSelectedItemPosition() + 10);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                    btnClose.setVisibility(View.VISIBLE);
                else {
                    btnClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                updateUI(AppConstants.PROCESS_RESET, spinner.getSelectedItemPosition() + 10);
                AppController.getInstance().cancelPendingRequests(TAG);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (inputSearch.getText().length() > 0) {
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                fetchEvents(inputSearch.getText().toString());
                                break;
                            case 1:
                                fetchCreative(inputSearch.getText().toString());
                                break;
                            case 2:
                                fetchUsers(inputSearch.getText().toString());
                                break;
                        }

                    }

                    inputSearch.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (inputSearch.getText().length() > 0) {
                    switch (position) {
                        case 0:
                            fetchEvents(inputSearch.getText().toString());
                            break;
                        case 1:
                            fetchCreative(inputSearch.getText().toString());
                            break;
                        case 2:
                            fetchUsers(inputSearch.getText().toString());
                            break;
                    }
                } else
                    updateUI(AppConstants.PROCESS_RESET, spinner.getSelectedItemPosition() + 10);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchEvents(String searchString) {

        updateUI(AppConstants.PROCESS_LOAD, PART_EVENTS);
        String url = AppConstants.URL_SEARCH + "events/" + searchString;
        Log.d(TAG, "fetchEvents: " + url);
        AppController.getInstance().cancelPendingRequests(TAG);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
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
                                    mRecyclerView.setAdapter(mAdapterEvents);
                                    updateUI(AppConstants.PROCESS_SUCCESS, PART_EVENTS);
                                } else {
                                    updateUI(AppConstants.PROCESS_NO_DATA, PART_EVENTS);
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

    private void fetchCreative(String searchString) {

        updateUI(AppConstants.PROCESS_LOAD, PART_CREATIVE);
        String url = AppConstants.URL_SEARCH + "creativity/" + searchString;
        Log.d(TAG, "fetchCreative: " + url);
        AppController.getInstance().cancelPendingRequests(TAG);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.has("data")) {
                                JSONArray data = resp.getJSONArray("data");
                                Log.d(TAG, "onResponse: " + data.length());
                                if (data.length() > 0) {
                                    mCreativeJsonHandler = new CreativeJsonHandler(data, null); //Sending null because we don't need pagination here
                                    mAdapterCreative = new RVACreativeHome(getActivity(), mCreativeJsonHandler, data.length());
                                    mRecyclerView.setAdapter(mAdapterCreative);
                                    updateUI(AppConstants.PROCESS_SUCCESS, PART_CREATIVE);
                                } else {
                                    updateUI(AppConstants.PROCESS_NO_DATA, PART_CREATIVE);
                                }
                            } else {
                                updateUI(AppConstants.PROCESS_FAILURE, PART_CREATIVE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE, PART_CREATIVE);
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

    private void fetchUsers(String searchString) {

        updateUI(AppConstants.PROCESS_LOAD, PART_PROFILE);
        String url = AppConstants.URL_SEARCH + "students/" + searchString;
        Log.d(TAG, "fetchUsers: " + url);
        AppController.getInstance().cancelPendingRequests(TAG);

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
                                    mAdapterCreative = new RVACreativeHome(getActivity(), mCreativeJsonHandler, data.length());
                                    mRecyclerView.setAdapter(mAdapterCreative);
                                    updateUI(AppConstants.PROCESS_SUCCESS, PART_PROFILE);
                                } else {
                                    updateUI(AppConstants.PROCESS_NO_DATA, PART_PROFILE);
                                }
                            } else {
                                updateUI(AppConstants.PROCESS_FAILURE, PART_PROFILE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateUI(AppConstants.PROCESS_FAILURE, PART_PROFILE);
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

    private void sendOneWayRequest(String url) {
        Log.d(TAG, "sendOneWyRequest: " + url);
        AppController.getInstance().cancelPendingRequests(TAG);

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

    private void updateUI(int reqCode, int partCode) {

        switch (reqCode) {
            case AppConstants.PROCESS_SUCCESS:
                mRecyclerView.setVisibility(View.VISIBLE);
                if (partCode != PART_PROFILE) {
                    contaWaitNorm.setVisibility(View.GONE);
                    contaWaitProfile.setVisibility(View.GONE);
                } else {
                    contaWaitNorm.setVisibility(View.GONE);
                    contaWaitProfile.setVisibility(View.GONE);
                }
                imgBack.setVisibility(View.GONE);
                break;
            case AppConstants.PROCESS_FAILURE:
                mRecyclerView.setVisibility(View.GONE);
                contaWaitNorm.setVisibility(View.GONE);
                contaWaitProfile.setVisibility(View.GONE);
                imgBack.setVisibility(View.VISIBLE);
                imgBack.setText("Error in internet connection. Please check yand retry.");
                imgBack.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_mood_bad, 0, 0);
                break;
            case AppConstants.PROCESS_LOAD:
                mRecyclerView.setVisibility(View.GONE);
                imgBack.setVisibility(View.GONE);
                if (partCode != PART_PROFILE) {
                    contaWaitNorm.setVisibility(View.VISIBLE);
                    contaWaitProfile.setVisibility(View.GONE);
                } else {
                    contaWaitNorm.setVisibility(View.GONE);
                    contaWaitProfile.setVisibility(View.VISIBLE);
                }
                break;
            case AppConstants.PROCESS_RESET:
                mRecyclerView.setVisibility(View.GONE);
                contaWaitNorm.setVisibility(View.GONE);
                contaWaitProfile.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
                imgBack.setVisibility(View.VISIBLE);
                if (partCode == PART_PROFILE)
                    imgBack.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_me_back, 0, 0);
                else if (partCode == PART_CREATIVE)
                    imgBack.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_creativity_back, 0, 0);
                else
                    imgBack.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_event_back, 0, 0);
                imgBack.setText("Let us know what are you searching for..");
                break;
            case AppConstants.PROCESS_NO_DATA:
                mRecyclerView.setVisibility(View.GONE);
                contaWaitNorm.setVisibility(View.GONE);
                contaWaitProfile.setVisibility(View.GONE);
                imgBack.setVisibility(View.VISIBLE);
                String error = "";
                if (partCode == PART_EVENTS)
                    error = "Couldn't find any events with query";
                else if (partCode == PART_CREATIVE)
                    error = "Couldn't find any posts with query";
                else if (partCode == PART_PROFILE)
                    error = "Couldn't find any user with query";
                Log.d(TAG, "updateUI: " + error);
                imgBack.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_mood_bad, 0, 0);
                imgBack.setText(error);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapterEvents = new RVAEventsHome();
        mAdapterCreative = new RVACreativeHome();

        ((RVAEventsHome) mAdapterEvents).setOnItemClickListener(new RVAEventsHome.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {

                switch (type) {
                    case 0:
                        Intent i = new Intent(getActivity(), SingleEvent.class);
                        i.putExtra(AppConstants.TAG_OBJ, mEventJsonHandler.getId(position));
                        startActivity(i);
                        break;
                    case 1:
                        sendOneWayRequest(AppConstants.URL_APPRECIATE_EVENT + mEventJsonHandler.getId(position));
                        break;
                    case 2:
                        sendOneWayRequest(AppConstants.URL_ATTEND + mEventJsonHandler.getId(position));
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

        ((RVACreativeHome) mAdapterCreative).setOnItemClickListener(new RVACreativeHome.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {
                switch (type) {
                    case 0:
                        Intent i = new Intent(getActivity(), SinglePost.class);
                        i.putExtra(AppConstants.TAG_OBJ, mCreativeJsonHandler.getId(position));
                        startActivity(i);
                        break;
                    case 1:
                        sendOneWayRequest(AppConstants.URL_APPRECIATE_POST + mCreativeJsonHandler.getId(position));
                        break;
                    case 2:
                        sendOneWayRequest(AppConstants.URL_BOOKMARK + mCreativeJsonHandler.getId(position));
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
}
