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
   private int counter=0;
   private int prevCount=0;
    private boolean flag=false;
    public void setmEventJsonHandler(EventJsonHandler ob)
    {
        this.mEventJsonHandler=ob;
    }
    public EventAdapter(EventJsonHandler mEventJsonHandler,int count)
    {
        this.mEventJsonHandler=mEventJsonHandler;
        this.count=count;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
   {   private RelativeLayout mRelativeLayout;
       private ImageView mAppreciated;
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
          mAppreciated=(ImageView)view.findViewById(R.id.Appreciated_image);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
       if(position==0) {
           holder.mRelativeLayout.setVisibility(View.VISIBLE);
           holder.mRelativeLayout.setOnClickListener(this);
       }
       try {
           holder.mDate.setText(mEventJsonHandler.Date(counter));
       }catch (Exception e)
       {

       }

        holder.mCardView.setOnClickListener(this);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mAppreciated.getContentDescription().toString().equals("notheart")) {
                    holder.mAppreciated.setContentDescription("heart");
                    holder.mAppreciated.setImageResource(R.mipmap.heart);
                }
                else {
                    holder.mAppreciated.setContentDescription("notheart");
                    holder.mAppreciated.setImageResource(R.mipmap.notheart);
                }
                }
        });
        holder.mVenue.setText(mEventJsonHandler.Venue(counter));
        holder.mDesc.setText(mEventJsonHandler.Desc(counter));
        holder.mName.setText(mEventJsonHandler.Title(counter));
        holder.mEventImage.setImageBitmap(mEventJsonHandler.Image(counter));
        Log.e("VALUE",String.valueOf(counter));
        if(position==getItemCount()-1)
        {
            flag=true;
            counter=0;
            prevCount=position;
        }
        else
        counter++;
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onClick(View view) {


    }
    public int getPrevCount()

    {
        return prevCount;
    }
    public void setCount(int count)
    {
     this.count=count;
    }
    public void setFlag(boolean f)
    {
        this.flag=f;
    }

    public boolean getFlag()
    {
        return flag;
    }

}
