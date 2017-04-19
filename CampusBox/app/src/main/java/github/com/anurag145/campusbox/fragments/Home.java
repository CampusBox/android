package github.com.anurag145.campusbox.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import github.com.anurag145.campusbox.R;
import github.com.anurag145.campusbox.adapters.EventAdapter;
import github.com.anurag145.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Home extends Fragment {
    private Context context;

    String s= "<p>Soch se farq padta hai...</p><p><br></p><p>Koi barish me hansta hai</p><p>Koi barish me rota hai</p><p>Koi dhup se dar ke bhaage</p><p>Koi dhup me tan tapata hai</p><p><br></p><p> </p><p><span style=\"letter-spacing: 0.02em;\">Kisi ki jaan paise me</span><br></p><p>Koi paise se marta hai</p><p>Udhta panchi aaram chahe</p><p>Chalne wale ko udhna hai</p><p> </p><p><br></p><p> </p><p>Kaam chahe ghar baetha naujawan</p><p>Chutti ko kaam waala tarasta hai</p><p><span style=\"letter-spacing: 0.02em;\">Koi nafrat faelata duniya me</span><br></p><p>Koi sirf prem par jeeta hai</p><p><br></p><p> </p><p>Soch se farq padta hai...</p>";
    StringTokenizer stringTokenizer= new StringTokenizer(s,"</p>");
    public Home()
    {

    }
    public static Home newInstance() {
        return new Home();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("message","layout should inflate");
       View view=inflater.inflate(R.layout.handler,container,false);
        WebView webView= (WebView)view.findViewById(R.id.creativity_desc);
        webView.getSettings().setJavaScriptEnabled(true);
         webView.loadData("<!DOCTYPE html> <html><head></head><body>"+s+"+</body></html>","text/html; charset=utf-8","UTF-8");
        context=getContext();
        //requestWithSomeHttpHeaders();
        return view;
    }
    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://app.campusbox.org/api/public/contents?limit=4&offset=0";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response",response);
                        int scrollpostion =0;
                       /* mLayoutManager=new GridLayoutManager(getActivity(),1);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.scrollToPosition(0);
                        EventJsonHandler mEventJsonHandler=new EventJsonHandler(response);
                        EventAdapter ob = new EventAdapter( mEventJsonHandler,mEventJsonHandler.Length());
                        mRecyclerView.setAdapter(ob);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        */


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
}
