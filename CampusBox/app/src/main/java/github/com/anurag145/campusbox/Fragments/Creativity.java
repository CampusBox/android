package github.com.anurag145.campusbox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.anurag145.campusbox.R;

/**
 * Created by Anurag145 on 4/2/2017.
 */

public class Creativity extends Fragment {

    public Creativity()
    {

    }
    public static Creativity newInstance()
    {
        return new Creativity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.creativity,container,false);
     return view;
    }
}
