package in.exun.campusbox.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import in.exun.campusbox.fragments.EventAdd.Additional;
import in.exun.campusbox.fragments.EventAdd.Basic;
import in.exun.campusbox.fragments.EventAdd.Desc;
import in.exun.campusbox.fragments.EventAdd.Poster;
import in.exun.campusbox.helper.AppConstants;

/**
 * Created by ayush on 01/05/17.
 */

public class StepAdapter extends AbstractFragmentStepAdapter {

    public StepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Bundle b = new Bundle();
        b.putInt(AppConstants.TAG_POSITION_KEY, position);
        switch (position) {
            case 0:
                Basic basic = new Basic();
                basic.setArguments(b);
                return basic;
            case 1:
                Desc desc = new Desc();
                desc.setArguments(b);
                return desc;
            case 2:
                Additional additional = new Additional();
                additional.setArguments(b);
                return additional;
            case 3:
                Poster poster = new Poster();
                poster.setArguments(b);
                return poster;
            default:
                return new Step() {
                    @Override
                    public VerificationError verifyStep() {
                        return null;
                    }

                    @Override
                    public void onSelected() {

                    }

                    @Override
                    public void onError(@NonNull VerificationError error) {

                    }
                };

        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        String title = "";
        StepViewModel.Builder builder = new StepViewModel.Builder(context);


        switch (position) {
            case 0:
                title = "Basic";
                break;
            case 1:
                title = "Description";
                break;
            case 2:
                title = "Additional";
                break;
            case 3:
                title = "Poster";
                break;

        }

        return builder.setTitle(title).create();
    }
}