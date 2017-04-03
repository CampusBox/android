package github.com.anurag145.campusbox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.anurag145.campusbox.R;

/**
 * Created by Anurag145 on 4/3/2017.
 */

public class Add extends Fragment {

    public Add()
    {

    }
    public static Add newInstance() {
        return new Add();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("message","layout should inflate");
        View view=inflater.inflate(R.layout.add,container,false);
        return view;
    }
}

