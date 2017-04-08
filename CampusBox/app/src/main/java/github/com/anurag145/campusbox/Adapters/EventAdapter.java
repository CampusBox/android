package github.com.anurag145.campusbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import github.com.anurag145.campusbox.R;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements RecyclerView.OnClickListener  {

    static class ViewHolder extends RecyclerView.ViewHolder
   {   private ImageView eventImage;
      public  ViewHolder(View view)
      {
          super(view);
          eventImage=(ImageView)view.findViewById(R.id.event_image);


      }

   }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }
}
