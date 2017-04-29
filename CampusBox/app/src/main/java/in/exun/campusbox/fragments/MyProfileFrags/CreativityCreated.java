package in.exun.campusbox.fragments.MyProfileFrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.exun.campusbox.R;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class CreativityCreated extends Fragment {
    public CreativityCreated()
    {

    }
 public static CreativityCreated instance() {  return new CreativityCreated(); }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_creativity_created,container,false);
        return view;
    }
}
