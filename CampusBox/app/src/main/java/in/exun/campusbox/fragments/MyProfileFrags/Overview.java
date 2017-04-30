package in.exun.campusbox.fragments.MyProfileFrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.zip.Inflater;

import in.exun.campusbox.R;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class Overview extends Fragment{
 public String mydata;
    public TextView textView,textView1;
    public ImageView imageView;
    public RecyclerView recyclerView,recyclerView1,recyclerView2,recyclerView3;
    public Overview(){

    }
public static Overview instance(){return new Overview();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_overview,container,false);
          mydata=getArguments().getString("data");
        textView=(TextView)view.findViewById(R.id.name);
        textView1=(TextView)view.findViewById(R.id.college);
        imageView=(ImageView)view.findViewById(R.id.profilepic);
                return view;
    }
    public void initialize()
    {   try {


        JSONObject jsonObject = new JSONObject(mydata);
        Glide.with(getActivity().getApplicationContext())
                .load(jsonObject.getString("photo"))
                .placeholder(R.drawable.ic_account_placeholder)
                .error(R.drawable.ic_account_placeholder)
                .into(imageView);


    }catch (Exception e)
    {

    }
    }
}
