package in.exun.campusbox.fragments.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.adapters.EventAdapter;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Events extends Fragment {

    private Context context;
    private RecyclerView mRecyclerView;
    private int mcount;
    EventAdapter ob;

    EventJsonHandler mEventJsonHandler;
    EventJsonHandler eventJsonHandler = null;
    private RecyclerView.LayoutManager mLayoutManager;

    public Events() {


    }

    public static Events newInstance() {
        return new Events();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_list);

        try {
            context = getContext();
            requestWithSomeHttpHeaders();
        } catch (Exception e) {

        }

        return view;
    }

    public void nextRequest() {
        Log.e("yeah", "hmm");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://app.campusbox.org/api/public/events?" + mEventJsonHandler.urlPagination();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        eventJsonHandler = mEventJsonHandler;
                        mEventJsonHandler = new EventJsonHandler(response);
                        int temp = mEventJsonHandler.Length();

                        if (temp != 0)

                        {
                            ob.setFlag(false);
                            ob.setmEventJsonHandler(mEventJsonHandler);
                            ob.setCount(ob.getPrevCount() + temp + 1);
                            ob.notifyItemInserted(ob.getPrevCount() + 1);
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
                Map<String, String> params = new HashMap<String, String>();String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };
        queue.add(getRequest);
    }

    public void requestWithSomeHttpHeaders() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://app.campusbox.org/api/public/events?limit=4&offset=0";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("damn", "damn");
                        mLayoutManager = new GridLayoutManager(getActivity(), 1);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.scrollToPosition(0);
                        mEventJsonHandler = new EventJsonHandler(response);
                        mcount = mEventJsonHandler.Length();
                        ob = new EventAdapter(mEventJsonHandler, mcount);
                        mRecyclerView.setAdapter(ob);
                        mRecyclerView.addOnScrollListener(onScrollListener);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();String token = "Bearer " + ((MainActivity) getActivity()).session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);

                return params;
            }
        };

        queue.add(postRequest);

    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {

            GridLayoutManager mGridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            if (!ob.getFlag())
                return;
            int visible = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
            if (visible == ob.getPrevCount())
                nextRequest();


        }

    };

}
