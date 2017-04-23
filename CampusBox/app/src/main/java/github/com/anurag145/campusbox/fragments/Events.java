package github.com.anurag145.campusbox.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import github.com.anurag145.campusbox.R;
import github.com.anurag145.campusbox.adapters.EventAdapter;
import github.com.anurag145.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Events extends Fragment {
    private  String MYJSON;
    private Context context;
    private RecyclerView mRecyclerView;
    private int mcount;
    EventAdapter ob;
    EventJsonHandler mEventJsonHandler;
    private RecyclerView.LayoutManager mLayoutManager;
    public Events()
    {


    }
    public static Events newInstance() {
        return new Events();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.events,container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.event_list);

                try {
            context=getContext();
           requestWithSomeHttpHeaders();
        }catch (Exception e)
        {

        }

        return view;
    }
    public void nextRequest()
    {   Log.e("yeah","hmm");
        RequestQueue queue=Volley.newRequestQueue(context);
        String url="https://app.campusbox.org/api/public/events?offset=3";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     mEventJsonHandler= new EventJsonHandler(response);
                        int temp= mEventJsonHandler.Length();

                        if(temp!=0)

                        {   ob.setFlag(false);
                            ob.setmEventJsonHandler(mEventJsonHandler);
                            Log.e("countprev",String.valueOf(ob.getPrevCount()+temp +1));
                            ob.setCount(ob.getPrevCount()+temp+1);
                            ob.notifyItemInserted(ob.getPrevCount()+1);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){@Override
        public Map<String, String> getHeaders()  {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTA3MTM4NzgsImV4cCI6MTQ5MzMwNTg3OCwianRpIjoiNDVuSW13bDZOQkpCcjJETlV2b1BoNyIsInVzZXJuYW1lIjoiY2hhd2xhYWRpdHlhOCIsImNvbGxlZ2VfaWQiOjF9.BF3KyogbSyBN0fY5VwBwgX88z4NIePTqleI9Y7dOLTg");

        return params;
    }
    };
    queue.add(getRequest);
    }
    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://app.campusbox.org/api/public/events";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        mLayoutManager=new GridLayoutManager(getActivity(),1);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.scrollToPosition(0);
                        mEventJsonHandler =new EventJsonHandler(response);
                        mcount=mEventJsonHandler.Length();
                        ob = new EventAdapter( mEventJsonHandler,mcount);
                        mRecyclerView.setAdapter(ob);
                        mRecyclerView.addOnScrollListener(onScrollListener);
                        mRecyclerView.setVisibility(View.VISIBLE);



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTA3MTM4NzgsImV4cCI6MTQ5MzMwNTg3OCwianRpIjoiNDVuSW13bDZOQkpCcjJETlV2b1BoNyIsInVzZXJuYW1lIjoiY2hhd2xhYWRpdHlhOCIsImNvbGxlZ2VfaWQiOjF9.BF3KyogbSyBN0fY5VwBwgX88z4NIePTqleI9Y7dOLTg");

                return params;
            }
        };

        queue.add(postRequest);

    }
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {

            GridLayoutManager mGridLayoutManager=(GridLayoutManager)mRecyclerView.getLayoutManager();
            if(!ob.getFlag())
                return;
            int visible =mGridLayoutManager.findLastCompletelyVisibleItemPosition();
             if(visible==ob.getPrevCount())
                 nextRequest();


        }

    };

}
