package in.exun.campusbox.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;

import in.exun.campusbox.R;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class RVACreative extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RVACreative";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private CreativeJsonHandler mCreativeJsonHandler;
    private static MyClickListener myClickListener;
    private int prevCount = 0;
    private boolean flag = false, paging = true;
    private Context context;

    public RVACreative() {
        Log.d(TAG, "RVACreative: " + getItemCount());
    }

    public void updateHandler(CreativeJsonHandler mCreativeJsonHandler) {
        setFlag(false);
        prevCount = this.mCreativeJsonHandler.length();
        this.mCreativeJsonHandler = mCreativeJsonHandler;
        notifyItemInserted(prevCount + 2);
    }

    public RVACreative(Context context, CreativeJsonHandler mCreativeJsonHandler) {
        this.mCreativeJsonHandler = mCreativeJsonHandler;
        this.context = context;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        RVACreative.myClickListener = myClickListener;
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
        ImageView imgAuthor, imgCover, btnBookmark, btnShare;
        TextView textAuthorData, textDate, textTitle, textDesc, btnLike;

        ViewHolder(View view) {
            super(view);
            imgAuthor = (ImageView) view.findViewById(R.id.img_author);
            imgCover = (ImageView) view.findViewById(R.id.img_cover);
            btnBookmark = (ImageView) view.findViewById(R.id.btn_bookmark);
            btnShare = (ImageView) view.findViewById(R.id.btn_share);
            btnLike = (TextView) view.findViewById(R.id.btn_like);
            textAuthorData = (TextView) view.findViewById(R.id.text_post_metadata);
            textDate = (TextView) view.findViewById(R.id.text_date);
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textDesc = (TextView) view.findViewById(R.id.text_desc);

            itemView.setOnClickListener(this);
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_creative, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_creativity, parent, false);
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

            holder.textAuthorData.setText(Html.fromHtml(mCreativeJsonHandler.getAuthorData(counter)), TextView.BufferType.SPANNABLE);
            try {
                holder.textDate.setText(mCreativeJsonHandler.getDate(counter));
            } catch (ParseException e) {
                e.printStackTrace();
                holder.textDate.setText("29 Apr");
            }
            holder.imgCover.setImageBitmap(mCreativeJsonHandler.getCoverImage(counter));
            Glide.with(context)
                    .load(mCreativeJsonHandler.getAuthorImage(counter))
                    .placeholder(R.drawable.ic_account_placeholder)
                    .error(R.drawable.ic_account_placeholder)
                    .into(holder.imgAuthor);
            holder.textTitle.setText(mCreativeJsonHandler.getTitle(counter));
            holder.textDesc.setText(Html.fromHtml(mCreativeJsonHandler.getDesc(counter)));
            if (mCreativeJsonHandler.isAppreciated(counter)) {
                holder.btnLike.setText("Appreciated | " + mCreativeJsonHandler.getAppreciatedCount(counter));
                holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appreciate, 0, 0, 0);
            } else {
                holder.btnLike.setText("Appreciate | " + mCreativeJsonHandler.getAppreciatedCount(counter));
                holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appreciate_empty, 0, 0, 0);
            }

            if (mCreativeJsonHandler.isBookmarked(counter))
                holder.btnBookmark.setImageResource(R.drawable.ic_bookmark);
            else
                holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_empty);

            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCreativeJsonHandler.isAppreciated(holder.getAdapterPosition())) {
                        holder.btnLike.setText("Appreciate | " + (mCreativeJsonHandler.getAppreciatedCount(holder.getAdapterPosition()) - 1));
                        holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appreciate_empty, 0, 0, 0);
                        mCreativeJsonHandler.setAppreciated(holder.getAdapterPosition(), false, (mCreativeJsonHandler.getAppreciatedCount(holder.getAdapterPosition()) - 1));
                    } else {
                        holder.btnLike.setText("Appreciated | " + (mCreativeJsonHandler.getAppreciatedCount(holder.getAdapterPosition()) + 1));
                        holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appreciate, 0, 0, 0);
                        mCreativeJsonHandler.setAppreciated(holder.getAdapterPosition(), true, (mCreativeJsonHandler.getAppreciatedCount(holder.getAdapterPosition()) + 1));
                    }
                    myClickListener.onItemClick(holder.getAdapterPosition(), v, 1);
                }
            });

            holder.btnBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCreativeJsonHandler.isBookmarked(holder.getAdapterPosition())) {
                        holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_empty);
                        mCreativeJsonHandler.setBookmarked(holder.getAdapterPosition(), false);
                    } else {
                        holder.btnBookmark.setImageResource(R.drawable.ic_bookmark);
                        mCreativeJsonHandler.setBookmarked(holder.getAdapterPosition(), false);
                    }
                    myClickListener.onItemClick(holder.getAdapterPosition(), v, 2);
                }
            });

            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(holder.getAdapterPosition(), v, 3);
                }
            });

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
        if (mCreativeJsonHandler == null)
            return 2;
        else if (paging)
            return mCreativeJsonHandler.length() + 2;
        else
            return mCreativeJsonHandler.length() + 1;
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
