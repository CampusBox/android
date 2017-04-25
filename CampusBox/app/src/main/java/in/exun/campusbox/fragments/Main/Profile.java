package in.exun.campusbox.fragments.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.MainActivity;

/**
 * Created by Anurag145 on 4/3/2017.
 */

public class Profile extends Fragment {

    Button btnLogout;

    public Profile() {

    }

    public static Profile newInstance() {
        return new Profile();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("message", "layout should inflate");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).signOut();
            }
        });
        return view;
    }
}

