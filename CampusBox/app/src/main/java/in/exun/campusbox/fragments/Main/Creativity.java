package in.exun.campusbox.fragments.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.activity.SinglePost;
import in.exun.campusbox.adapters.RVACreative;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.helper.DataSet;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Creativity extends Fragment implements View.OnClickListener {

    private static final String TAG = "Creativity";

    private RecyclerView mRecyclerView;
    private RVACreative mAdapter;
    private CreativeJsonHandler mCreativeJsonHandler;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton fabAddCreative;
    private View rootView;
    private LinearLayout contWait;
    private boolean maxLimitReached = false, loading = false;
    private List<String> selectedInts, newSelctions;
    private DataSet dataSet;
    private TextView intItem;
    private CardView intItemContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_creativity, container, false);

        initialise();
        populateCreative();

        return rootView;
    }

    private void initialise() {


        fabAddCreative = (FloatingActionButton) rootView.findViewById(R.id.fab_add_creative);
        contWait = (LinearLayout) rootView.findViewById(R.id.container_creative_wait);
        selectedInts = new ArrayList<>();
        newSelctions = new ArrayList<>();
        dataSet = new DataSet();

        fabAddCreative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add new post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCreative() {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_creative);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RVACreative();
        mRecyclerView.setAdapter(mAdapter);

        fetchCreative();
    }

    private void fetchCreative() {

        updateUI(AppConstants.PROCESS_LOAD);

        String url = AppConstants.URL_CREATIVE + "limit=2&offset=0";
        Log.d(TAG, "fetchCreative: " + url);
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
                                    mCreativeJsonHandler = new CreativeJsonHandler(data, metadata);
                                    mAdapter = new RVACreative(getActivity(), mCreativeJsonHandler);
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
                String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest, TAG);
    }

    public void nextRequest() {

        Log.d(TAG, "nextRequest: Get more data boi");
        updateUI(AppConstants.PROCESS_LOAD_MORE);

        String url = AppConstants.URL_CREATIVE + mCreativeJsonHandler.urlPagination();
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
                            JSONArray data = mCreativeJsonHandler.getData();
                            for (int i = 0; i < newData.length(); i++)
                                data.put(newData.getJSONObject(i));
                            mCreativeJsonHandler = new CreativeJsonHandler(data, meta);
                            Log.d(TAG, "onResponse: " + temp + " " + mCreativeJsonHandler.length());
                            updateUI(AppConstants.PROCESS_SUCCESS);

                            if (temp != 0) {
                                mAdapter.updateHandler(mCreativeJsonHandler);
                                if (mCreativeJsonHandler.getLimit() != temp) {
                                    maxLimitReached = true;
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
                String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(req, TAG);

    }

    private void updateUI(int reqCode) {

        switch (reqCode) {
            case AppConstants.PROCESS_SUCCESS:
                Log.d(TAG, "updateUI: success");
                mRecyclerView.setVisibility(View.VISIBLE);
                fabAddCreative.setVisibility(View.VISIBLE);
                contWait.setVisibility(View.GONE);
                mRecyclerView.addOnScrollListener(onScrollListener);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading = false;
                break;
            case AppConstants.PROCESS_FAILURE:
                Log.d(TAG, "updateUI: fail");
                Toast.makeText(getActivity(), "Error in internet", Toast.LENGTH_SHORT).show();
                fabAddCreative.setVisibility(View.GONE);
                contWait.setVisibility(View.GONE);
                break;
            case AppConstants.PROCESS_LOAD:
                Log.d(TAG, "updateUI: load");
                maxLimitReached = false;
                loading = true;
                mRecyclerView.setVisibility(View.GONE);
                fabAddCreative.setVisibility(View.GONE);
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

        ((RVACreative) mAdapter).setOnItemClickListener(new RVACreative.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {

                Log.d(TAG, "onItemClick: " + position);
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
                    case 4:
                        showFilters();
                        break;
                    case 5:
                        selectedInts = new ArrayList<>();
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

    public void showFilters() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.comp_interests, null);
        updateSelectedFilters(convertView);
        newSelctions = new ArrayList<>();
        newSelctions = selectedInts;
        clickListeners(convertView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setView(convertView)
                .setTitle("Pick a type of creativity")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedInts = new ArrayList<>();
                        mAdapter.updateFilters(selectedInts.size());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true);

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray array = new JSONArray();

                for (int i = 0; i < selectedInts.size(); i++) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id", selectedInts.get(i));
                        object.put("intrested", true);
                        String id = "t" + selectedInts.get(i);
                        int resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
                        String title = ((TextView) convertView.findViewById(resID)).getText().toString();
                        Log.d(TAG, "buildJsonArray: " + title);
                        object.put("title", true);
                        array.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter.updateFilters(selectedInts.size());

                dialog.dismiss();
            }
        });

    }

    private void updateSelectedFilters(View view) {
        for (int i = 0; i < selectedInts.size(); i++) {
            String id = "t" + selectedInts.get(i);
            int resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            ((TextView) view.findViewById(resID)).setTextColor(Color.WHITE);
            id = "rl" + selectedInts.get(i);
            resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            ((CardView) view.findViewById(resID)).setCardBackgroundColor(Color.RED);
        }
    }

    private void clickListeners(View view) {

        view.findViewById(R.id.rl1).setOnClickListener(this);
        view.findViewById(R.id.rl2).setOnClickListener(this);
        view.findViewById(R.id.rl3).setOnClickListener(this);
        view.findViewById(R.id.rl4).setOnClickListener(this);
        view.findViewById(R.id.rl5).setOnClickListener(this);
        view.findViewById(R.id.rl6).setOnClickListener(this);
        view.findViewById(R.id.rl7).setOnClickListener(this);
        view.findViewById(R.id.rl8).setOnClickListener(this);
        view.findViewById(R.id.rl9).setOnClickListener(this);
        view.findViewById(R.id.rl10).setOnClickListener(this);
        view.findViewById(R.id.rl11).setOnClickListener(this);
        view.findViewById(R.id.rl12).setOnClickListener(this);
        view.findViewById(R.id.rl13).setOnClickListener(this);
        view.findViewById(R.id.rl14).setOnClickListener(this);
        view.findViewById(R.id.rl15).setOnClickListener(this);
        view.findViewById(R.id.rl16).setOnClickListener(this);
        view.findViewById(R.id.rl17).setOnClickListener(this);
        view.findViewById(R.id.rl18).setOnClickListener(this);
        view.findViewById(R.id.rl19).setOnClickListener(this);
        view.findViewById(R.id.rl20).setOnClickListener(this);
        view.findViewById(R.id.rl21).setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onClick(View view) {
        intItemContainer = (CardView) view;
        intItem = (TextView) view.findViewById(dataSet.list.get(view.getId()));
        onSelect();
    }

    private void onSelect() {

        if (intItem.getCurrentTextColor() == Color.parseColor("#454545")) {
            intItem.setTextColor(Color.WHITE);
            intItemContainer.setCardBackgroundColor(Color.RED);
            newSelctions.add(getResources().getResourceEntryName(intItem.getId()).substring(1));

        } else {
            intItem.setTextColor(Color.parseColor("#454545"));
            intItemContainer.setCardBackgroundColor(Color.parseColor("#ececec"));
            newSelctions.remove(getResources().getResourceEntryName(intItem.getId()).substring(1));

        }
    }
}
