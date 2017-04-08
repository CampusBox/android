package github.com.anurag145.campusbox.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Events extends Fragment {
    private  String MYJSON;
    private Context context;
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
        try {
            context=getContext();
           requestWithSomeHttpHeaders();
        }catch (Exception e)
        {

        }

        return view;
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
                        System.out.println(response);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTA3MTM4NzgsImV4cCI6MTQ5MzMwNTg3OCwianRpIjoiNDVuSW13bDZOQkpCcjJETlV2b1BoNyIsInVzZXJuYW1lIjoiY2hhd2xhYWRpdHlhOCIsImNvbGxlZ2VfaWQiOjF9.BF3KyogbSyBN0fY5VwBwgX88z4NIePTqleI9Y7dOLTg");

                return params;
            }
        };

        queue.add(postRequest);

    }
}
