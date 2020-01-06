package longse.com.herospeed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import longse.com.herospeed.R;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.bean.DeviceInfoBean;
import longse.com.herospeed.interfaces.startDragListener;

/**
 * Created by LY on 2017/11/15.
 */

public class AdapterDevicesRecycle extends RecyclerView.Adapter<AdapterDevicesRecycle.ViewHolder> {
    private Context context;
    private List<DeviceInfoBean> devices;

    startDragListener draglistener;
    //set接口回调
    public void setDraglistener(startDragListener draglistener) {
        this.draglistener = draglistener;
    }

    public AdapterDevicesRecycle(List<DeviceInfoBean> deviceSize) {
        this.devices = deviceSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_devices, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = devices.get(position);
        holder.deviceName.setText(holder.mItem.getDeviceName());
        holder.deviceType.setText(holder.mItem.getEquoModleStr());
        holder.deviceStatus.setText(holder.mItem.getIfOnLine().equals("1") ? "off" : "on");
        holder.cardLl.setTag(position);

        holder.cardLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                draglistener.startDragItem(holder);
                return false;
            }
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //注意：这里down和up都会回调该方法
//                if (event.getAction()== MotionEvent.ACTION_DOWN) {
//                    draglistener.startDragItem(holder);
//                }
//                return false;
//            }
        });

        holder.cardLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p= (int) v.getTag();
                SkipActivity.startDeviceDetailActivity(context,devices.get(p));
            }
        });


        if ("IPC".equals(holder.mItem.getEquoModleStr())) {
            if ("IPC_5".equals(holder.mItem.getDeviceDetatilType())) {
                Glide.with(context).
                        load(R.drawable.icon_fish_3x).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        thumbnail(0.5f).
                        priority(Priority.HIGH).
                        placeholder(R.drawable.pikachu_sit).
                        error(R.drawable.pikachu_sit).
                        fallback(R.drawable.pikachu_sit).
                        into(holder.deviceImg);
            } else {
                Glide.with(context).
                        load(R.drawable.icon_ipc_3x).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        thumbnail(0.5f).
                        priority(Priority.HIGH).
                        placeholder(R.drawable.pikachu_sit).
                        error(R.drawable.pikachu_sit).
                        fallback(R.drawable.pikachu_sit).
                        into(holder.deviceImg);
            }
        } else {
            Glide.with(context).
                    load(R.drawable.icon_nvr_3x).
                    diskCacheStrategy(DiskCacheStrategy.RESULT).
                    thumbnail(0.5f).
                    priority(Priority.HIGH).
                    placeholder(R.drawable.pikachu_sit).
                    error(R.drawable.pikachu_sit).
                    fallback(R.drawable.pikachu_sit).
                    into(holder.deviceImg);
        }


    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        DeviceInfoBean mItem;

        @BindView(R.id.card_ll)
        LinearLayout cardLl;
        @BindView(R.id.device_img)
        ImageView deviceImg;
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_status)
        TextView deviceStatus;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
