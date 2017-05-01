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
import in.exun.campusbox.adapters.RVAProfileCreative;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class CreativityCreated extends Fragment {
    public View rootview;
    String mydata;
    public RecyclerView recyclerView;
    CreativeJsonHandler creativeJsonHandler;
    RecyclerView.LayoutManager mLayoutManager;
    RVAProfileCreative rvapc;
    public CreativityCreated()
    {

    }
 public static CreativityCreated instance() {  return new CreativityCreated(); }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_creativity_created,container,false);
        mydata=getArguments().getString("data");
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView=(RecyclerView)rootview.findViewById(R.id.Created_content);
        initialize();
        return rootview;
    }
    public void initialize()
    {
        try {

            JSONObject jsonObject = new JSONObject(mydata);
            jsonObject=new JSONObject( jsonObject.getString("data"));
            JSONObject jsonObject1=new JSONObject(jsonObject.getString("CreativeContents"));

            creativeJsonHandler=new CreativeJsonHandler(new JSONArray(jsonObject1.getString("data")),null);

            if(creativeJsonHandler.length()!=0)
            {
                Log.e( "initialize: ",String.valueOf(creativeJsonHandler.length()) );

                rvapc=new RVAProfileCreative(creativeJsonHandler,getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(rvapc);

            }else {
                recyclerView.setVisibility(View.GONE);
                rootview.findViewById(R.id.nullEntry).setVisibility(View.VISIBLE);
            }

        }catch (Exception e)
        {

        }
    }
}
