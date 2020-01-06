package longse.com.herospeed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import longse.com.herospeed.R;
import longse.com.herospeed.bean.ScreenBean;
import longse.com.herospeed.interfaces.startDragListener;

/**
 * Created by LY on 2017/11/15.
 */

public class AdapterScreenRecycle extends RecyclerView.Adapter<AdapterScreenRecycle.ViewHolder> {

    private Context context;
    private List<ScreenBean> mValues;
    private long oneClick;
    private int mScreenNum = 4;
    private static final int TIME_INTERVAL = 1000;

    startDragListener draglistener;
    //set接口回调
    public void setDraglistener(startDragListener draglistener) {
        this.draglistener = draglistener;
    }

    public void screenNum(int num) {
        this.mScreenNum = num;
    }

    public AdapterScreenRecycle(List<ScreenBean> items) {
        mValues = items;
    }

    @Override
    public AdapterScreenRecycle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_screen, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);

        holder.tvName.setText(holder.mItem.getName());

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                draglistener.startDragItem(holder);
                return false;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oneClick + TIME_INTERVAL > System.currentTimeMillis()) {
                    Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                    oneClick = System.currentTimeMillis();
                }
            }
        });

        Glide.with(context).
                load(holder.mItem.getImg()).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                thumbnail(0.5f).
                priority(Priority.HIGH).
                placeholder(R.drawable.pikachu_sit).
                error(R.drawable.pikachu_sit).
                fallback(R.drawable.pikachu_sit).
                into(holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return mScreenNum;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        ScreenBean mItem;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
