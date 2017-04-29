package in.exun.campusbox.adapters;


import android.support.v7.widget.CardView;
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

public class RVAEvents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RVAEvents";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private EventJsonHandler mEventJsonHandler;
    private static MyClickListener myClickListener;
    private int prevCount = 0;
    private boolean flag = false, paging = true;

    public RVAEvents() {
        Log.d(TAG, "RVAEvents: " + getItemCount());
    }

    public void updateHandler(EventJsonHandler mEventJsonHandler, int addedCount) {
        setFlag(false);
        prevCount = this.mEventJsonHandler.Length();
        this.mEventJsonHandler = mEventJsonHandler;
        notifyItemInserted(prevCount + 2);
    }

    public RVAEvents(EventJsonHandler mEventJsonHandler) {
        this.mEventJsonHandler = mEventJsonHandler;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        RVAEvents.myClickListener = myClickListener;
    }

    public void removeEnd() {
        paging = false;
        notifyItemRemoved(getItemCount()-1);

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        HeaderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), itemView, 4);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), itemView, 4);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private CardView cardView;
        private ImageView imgLike;
        private ImageView imgEvent;
        private TextView textDate, textTitle, textVenue, textDesc, textLike;
        private LinearLayout btnLike, btnRSVP, optRSVP, optAttending;

        ViewHolder(View view) {
            super(view);
            cardView=(CardView)view.findViewById(R.id.eventCard);
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

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition() - 1, v, 0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_event, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comp_load, parent, false);
            return new FooterViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holderB, int position) {

        if (holderB instanceof ViewHolder) {
            //cast holder to VHHeader and set data for header.
            final ViewHolder holder = (ViewHolder) holderB;
            final int counter = holder.getAdapterPosition() - 1;

            try {
                holder.textDate.setText(mEventJsonHandler.getDate(counter));
            } catch (Exception e) {
                holder.textDate.setText("TBA");
            }

            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEventJsonHandler.isAppreciated(counter)) {
                        holder.imgLike.setImageResource(R.drawable.ic_appreciate_empty);
                        holder.textLike.setText("Appreciate");
                        mEventJsonHandler.setAppreciated(counter, false);
                    } else {
                        holder.imgLike.setImageResource(R.drawable.ic_appreciate);
                        holder.textLike.setText("Appreciated");
                        mEventJsonHandler.setAppreciated(counter, true);
                    }
                    myClickListener.onItemClick(holder.getAdapterPosition(), view, 1);
                }
            });

            holder.btnRSVP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEventJsonHandler.isAttending(counter)) {
                        holder.optAttending.setVisibility(View.GONE);
                        holder.optRSVP.setVisibility(View.VISIBLE);
                        mEventJsonHandler.setAttending(counter, false);
                    } else {
                        holder.optAttending.setVisibility(View.VISIBLE);
                        holder.optRSVP.setVisibility(View.GONE);
                        mEventJsonHandler.setAttending(counter, true);
                    }
                    myClickListener.onItemClick(counter,view, 2);
                }
            });


            if (mEventJsonHandler.isAttending(holder.getAdapterPosition())) {
                holder.optAttending.setVisibility(View.VISIBLE);
                holder.optRSVP.setVisibility(View.GONE);
            } else {
                holder.optAttending.setVisibility(View.GONE);
                holder.optRSVP.setVisibility(View.VISIBLE);
            }

            if (mEventJsonHandler.isAppreciated(counter)) {
                holder.imgLike.setImageResource(R.drawable.ic_appreciate);
                holder.textLike.setText("Appreciated");
            } else {
                holder.imgLike.setImageResource(R.drawable.ic_appreciate_empty);
                holder.textLike.setText("Appreciate");
            }
            Log.e("Tag",String.valueOf(counter));
            Log.e("Tag", String.valueOf( holder.cardView.getId()));

            holder.cardView.setContentDescription(String.valueOf(mEventJsonHandler.getId(counter)));
            holder.textVenue.setText(mEventJsonHandler.getVenue(counter));
            holder.textDesc.setText(mEventJsonHandler.getDesc(counter));
            holder.textTitle.setText(mEventJsonHandler.getTitle(counter));
            holder.imgEvent.setImageBitmap(mEventJsonHandler.getImage(counter));

            if (counter == getItemCount() - 2 && paging) {
                flag = true;
                prevCount = counter;
            }
        } else if (holderB instanceof FooterViewHolder) {
            if (!flag) {
                flag = true;
                prevCount = getItemCount() - 3;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (isPositionFooter(position) && paging) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == (getItemCount() - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v, int type);
    }

    @Override
    public int getItemCount() {
        if (mEventJsonHandler == null)
            return 2;
        else if (paging)
            return mEventJsonHandler.Length() + 2;
        else
            return mEventJsonHandler.Length() + 1;
    }

    public int getPrevCount() {
        return prevCount;
    }

    public void setFlag(boolean f) {
        this.flag = f;
    }

    public boolean getFlag() {
        return flag;
    }

}
