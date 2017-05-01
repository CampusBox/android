package in.exun.campusbox.fragments.MyProfileFrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.RVAFollow;
import in.exun.campusbox.jsonHandlers.FollowJsonHandler;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class Followers extends Fragment {
    public View rootview;
    String mydata;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FollowJsonHandler followJsonHandler;
    RVAFollow rvaf;
    public Followers()
    {

    }
public static Followers instance(){return new Followers();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_followers,container,false);
        mydata=getArguments().getString("data");
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView=(RecyclerView)rootview.findViewById(R.id.Follow);
        initialize();
        return rootview;
    }
    public void initialize()
    {
        try
        {
            JSONObject jsonObject = new JSONObject(mydata);
            jsonObject=new JSONObject( jsonObject.getString("data"));
            JSONObject jsonObject1=new JSONObject(jsonObject.getString("Follower"));
            JSONArray jsonArray=new JSONArray(jsonObject1.getString("data"));
            followJsonHandler=new FollowJsonHandler(jsonArray);
            if(followJsonHandler.Length()!=0)
            {
                rvaf=new RVAFollow(followJsonHandler,getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(rvaf);
            }else
            {
                recyclerView.setVisibility(View.GONE);
                rootview.findViewById(R.id.nullEntry).setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            Log.e( "bc ",e.toString() );
        }
    }
}
