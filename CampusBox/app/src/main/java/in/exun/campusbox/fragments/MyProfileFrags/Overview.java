package in.exun.campusbox.fragments.MyProfileFrags;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.zip.Inflater;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.RVAEvents;
import in.exun.campusbox.adapters.RVAProfileCreative;
import in.exun.campusbox.adapters.RVAProfileEvents;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class Overview extends Fragment{
 public String mydata;
    public TextView textView,textView1,textView2;
    public ImageView imageView;
    public View rootview;


    public Overview(){

    }
public static Overview instance(){return new Overview();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootview= inflater.inflate(R.layout.fragment_overview,container,false);
          mydata=getArguments().getString("data");
        textView=(TextView)rootview.findViewById(R.id.name);
        textView1=(TextView)rootview.findViewById(R.id.college);
        textView2=(TextView)rootview.findViewById(R.id.about_text);

        imageView=(ImageView)rootview.findViewById(R.id.profilepic);



                initialize();
                return rootview;
    }
    public void initialize()
    {   try {
        View view;
        JSONObject jsonObject1;
        JSONObject jsonObject = new JSONObject(mydata);
       jsonObject=new JSONObject( jsonObject.getString("data"));
        Log.e( "initialize: ","getting Image" );
        Glide.with(getActivity().getApplicationContext())
                .load(jsonObject.getString("photo"))
                .placeholder(R.drawable.ic_account_placeholder)
                .error(R.drawable.ic_account_placeholder)
                .into(imageView);

        textView.setText(jsonObject.getString("name"));
        textView1.setText((new JSONObject(jsonObject.getString("college")).getString("name")));
        Log.e( "initialize: ",jsonObject.getString("subtitle") );
        String str=jsonObject.getString("subtitle");
        if(str!=null) {
            Log.e( "initialize: ",str );
            textView2.setText("Apparently, this user prefers to keep an air of mystery about them");
        }
        else {

            textView2.setText(jsonObject.getString("subtitle"));
        }



    }catch (Exception e)
    {
        Log.e( "initialize: ",e.toString() );
    }
    }
}
