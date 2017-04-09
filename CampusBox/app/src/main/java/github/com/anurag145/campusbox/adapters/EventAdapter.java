package github.com.anurag145.campusbox.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import github.com.anurag145.campusbox.R;
import github.com.anurag145.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements RecyclerView.OnClickListener  {
   private EventJsonHandler mEventJsonHandler;
   private int count;
    public EventAdapter(EventJsonHandler mEventJsonHandler,int count)
    {
        this.mEventJsonHandler=mEventJsonHandler;
        this.count=count;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
   {   private RelativeLayout mRelativeLayout;
       private CardView  mCardView;
       private ImageView mEventImage;
       private TextView  mDate;
       private TextView  mName;
       private TextView  mVenue;
       private TextView  mDesc;
       private LinearLayout mLinearLayout;
      public  ViewHolder(View view)
      {
          super(view);
          mRelativeLayout=(RelativeLayout)view.findViewById(R.id.filter);
          mCardView=(CardView)view.findViewById(R.id.eventCard);
          mDate=(TextView)view.findViewById(R.id.date);
          mName=(TextView)view.findViewById(R.id.event_name);
          mVenue=(TextView)view.findViewById(R.id.venue);
          mDesc=(TextView)view.findViewById(R.id.event_desc);
          mEventImage=(ImageView)view.findViewById(R.id.event_image);
          mLinearLayout=(LinearLayout)view.findViewById(R.id.Appreciated);


      }

   }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       if(position==0) {
           holder.mRelativeLayout.setVisibility(View.VISIBLE);
           holder.mRelativeLayout.setOnClickListener(this);
       }
       try {
           holder.mDate.setText(mEventJsonHandler.Date(position));
       }catch (Exception e)
       {

       }
        holder.mCardView.setOnClickListener(this);
        holder.mLinearLayout.setOnClickListener(this);
        holder.mVenue.setText(mEventJsonHandler.Venue(position));
        holder.mDesc.setText(mEventJsonHandler.Desc(position));
        holder.mName.setText(mEventJsonHandler.Title(position));
        holder.mEventImage.setImageBitmap(mEventJsonHandler.Image(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onClick(View view) {

        Log.d("onClick",String.valueOf(view.getId()));
    }

}
