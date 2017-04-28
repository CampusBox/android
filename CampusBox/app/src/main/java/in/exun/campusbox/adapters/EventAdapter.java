package in.exun.campusbox.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.exun.campusbox.R;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private static final String TAG = "EventAdapter";
    private EventJsonHandler mEventJsonHandler;
    private static MyClickListener myClickListener;
    private int count;
    private int prevCount = 0;
    private boolean flag = false;

    public EventAdapter() {
    }

    public void setmEventJsonHandler(EventJsonHandler ob) {
        this.mEventJsonHandler = ob;
    }

    public EventAdapter(EventJsonHandler mEventJsonHandler, int count) {
        this.mEventJsonHandler = mEventJsonHandler;
        this.count = count;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        EventAdapter.myClickListener = myClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView imgLike;
        private ImageView imgEvent;
        private TextView textDate, textTitle, textVenue, textDesc, textLike;
        private LinearLayout btnLike, btnRSVP, optRSVP, optAttending;

        ViewHolder(View view) {
            super(view);
            imgLike = (ImageView) view.findViewById(R.id.Appreciated_image);
            textDate = (TextView) view.findViewById(R.id.date);
            textTitle = (TextView) view.findViewById(R.id.event_name);
            textVenue = (TextView) view.findViewById(R.id.venue);
            textDesc = (TextView) view.findViewById(R.id.event_desc);
            textLike = (TextView) view.findViewById(R.id.text_like);
            imgEvent = (ImageView) view.findViewById(R.id.event_image);
            btnLike = (LinearLayout) view.findViewById(R.id.btn_like);
            btnRSVP = (LinearLayout) view.findViewById(R.id.btn_rsvp);
            optRSVP = (LinearLayout) view.findViewById(R.id.container_rsvp);
            optAttending = (LinearLayout) view.findViewById(R.id.container_attending);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), itemView, 0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: inflated");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);

        return new ViewHolder(view);
    }

    public interface MyClickListener {
        void onItemClick(int position, View v, int type);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int counter = holder.getAdapterPosition();

        Log.d(TAG, "onBindViewHolder: " + counter);

        try {
            holder.textDate.setText(mEventJsonHandler.getDate(counter));
        } catch (Exception e) {
            holder.textDate.setText("TBA");
        }

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventJsonHandler.isAppreciated(holder.getAdapterPosition())) {
                    holder.imgLike.setImageResource(R.drawable.ic_appreciate_empty);
                    holder.textLike.setText("Appreciate");
                    mEventJsonHandler.setAppreciated(holder.getAdapterPosition(), false);
                } else {
                    holder.imgLike.setImageResource(R.drawable.ic_appreciate);
                    holder.textLike.setText("Appreciated");
                    mEventJsonHandler.setAppreciated(holder.getAdapterPosition(), true);
                }
                myClickListener.onItemClick(holder.getAdapterPosition(), view, 1);
            }
        });

        holder.btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventJsonHandler.isAttending(holder.getAdapterPosition())) {
                    holder.optAttending.setVisibility(View.GONE);
                    holder.optRSVP.setVisibility(View.VISIBLE);
                    mEventJsonHandler.setAttending(holder.getAdapterPosition(), false);
                } else {
                    holder.optAttending.setVisibility(View.VISIBLE);
                    holder.optRSVP.setVisibility(View.GONE);
                    mEventJsonHandler.setAttending(holder.getAdapterPosition(), true);
                }
                myClickListener.onItemClick(holder.getAdapterPosition(), view, 2);
            }
        });


        if (mEventJsonHandler.isAttending(holder.getAdapterPosition())) {
            holder.optAttending.setVisibility(View.VISIBLE);
            holder.optRSVP.setVisibility(View.GONE);
        } else {
            holder.optAttending.setVisibility(View.GONE);
            holder.optRSVP.setVisibility(View.VISIBLE);
        }

        if (mEventJsonHandler.isAppreciated(holder.getAdapterPosition())) {
            holder.imgLike.setImageResource(R.drawable.ic_appreciate);
            holder.textLike.setText("Appreciated");
        } else {
            holder.imgLike.setImageResource(R.drawable.ic_appreciate_empty);
            holder.textLike.setText("Appreciate");
        }

        holder.textVenue.setText(mEventJsonHandler.getVenue(counter));
        holder.textDesc.setText(mEventJsonHandler.getDesc(counter));
        holder.textTitle.setText(mEventJsonHandler.getTitle(counter));
        holder.imgEvent.setImageBitmap(mEventJsonHandler.getImage(counter));
        if (holder.getAdapterPosition() == getItemCount() - 1 && mEventJsonHandler.isAllowedPagination()) {
            flag = true;
            prevCount = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public int getPrevCount()

    {
        return prevCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFlag(boolean f) {
        this.flag = f;
    }

    public boolean getFlag() {
        return flag;
    }

}
