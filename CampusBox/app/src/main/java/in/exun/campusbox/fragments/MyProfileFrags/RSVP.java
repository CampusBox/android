package in.exun.campusbox.fragments.MyProfileFrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.RVAProfileEvents;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 5/1/2017.
 */

public class RSVP extends Fragment {
    EventJsonHandler eventJsonHandler;
    public String mydata;
    RecyclerView.LayoutManager mLayoutManager;
    RVAProfileEvents rvape;
    View rootview;
    public RecyclerView recyclerView;

    public RSVP()
    {

    }
    public static RSVP instance(){return new RSVP();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootview=inflater.inflate(R.layout.fragment_rsvp,container,false);
        mydata=getArguments().getString("data");
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView=(RecyclerView)rootview.findViewById(R.id.Attending_events);
        initialize();
        return rootview;
    }
    public void initialize()
    {
        try{
            JSONObject jsonObject = new JSONObject(mydata);
            jsonObject=new JSONObject( jsonObject.getString("data"));
            JSONObject jsonObject1=new JSONObject(jsonObject.getString("AttendingEvents"));
            eventJsonHandler=new EventJsonHandler(new JSONArray(jsonObject1.getString("data")),null);

            if (eventJsonHandler.Length()!=0)
            {
                rvape=new RVAProfileEvents(eventJsonHandler,getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(rvape);
            }else
            {
                recyclerView.setVisibility(View.GONE);
                rootview.findViewById(R.id.nullEntry).setVisibility(View.VISIBLE);
            }



        }catch (Exception e)
        {

        }

    }
}
