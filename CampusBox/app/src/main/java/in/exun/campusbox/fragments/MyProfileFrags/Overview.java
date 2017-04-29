package in.exun.campusbox.fragments.MyProfileFrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import in.exun.campusbox.R;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class Overview extends Fragment{

    public Overview(){

    }
public static Overview instance(){return new Overview();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_overview,container,false);

                return view;
    }
}
