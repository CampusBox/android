package github.com.anurag145.campusbox.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import github.com.anurag145.campusbox.R;
import github.com.anurag145.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements RecyclerView.OnClickListener  {
    EventJsonHandler mEventJsonHandler;
    public EventAdapter(EventJsonHandler mEventJsonHandler)
    {
        this.mEventJsonHandler=mEventJsonHandler;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
   {   private CardView  mCardView;
       private ImageView mEventImage;
       private TextView  mDate;
       private TextView  mName;
       private TextView  mVenue;
       private TextView  mDesc;
      public  ViewHolder(View view)
      {
          super(view);
          mCardView=(CardView)view.findViewById(R.id.eventCard);
          mDate=(TextView)view.findViewById(R.id.date);
          mName=(TextView)view.findViewById(R.id.event_name);
          mVenue=(TextView)view.findViewById(R.id.venue);
          mDesc=(TextView)view.findViewById(R.id.event_desc);
          mEventImage=(ImageView)view.findViewById(R.id.event_image);


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
