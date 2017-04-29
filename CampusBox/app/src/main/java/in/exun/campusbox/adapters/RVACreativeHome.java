package in.exun.campusbox.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class RVACreativeHome extends RecyclerView.Adapter<RVACreativeHome.ViewHolder> {
    private static final String TAG = "RVACreativeHome";
    private CreativeJsonHandler mCreativeJsonHandler;
    private static MyClickListener myClickListener;
    private int count;
    private int prevCount = 0;
    private boolean flag = false;
    private Context context;

    public RVACreativeHome() {
    }

    public void setmEventJsonHandler(CreativeJsonHandler mCreativeJsonHandler) {
        this.mCreativeJsonHandler = mCreativeJsonHandler;
    }

    public RVACreativeHome(Context context, CreativeJsonHandler mEventJsonHandler, int count) {
        this.context = context;
        this.mCreativeJsonHandler = mEventJsonHandler;
        this.count = count;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        RVACreativeHome.myClickListener = myClickListener;
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
            myClickListener.onItemClick(getAdapterPosition(), itemView, 0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_creativity, parent, false);

        return new ViewHolder(view);
    }

    public interface MyClickListener {
        void onItemClick(int position, View v, int type);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int counter = holder.getAdapterPosition();

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
