package longse.com.herospeed.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.longse.freeipfunction.manager.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.adapter.AdapterDevicesRecycle;
import longse.com.herospeed.base.ConstAction;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.bean.DeviceInfoBean;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.eventbus.EventFeiendsFragment;
import longse.com.herospeed.interfaces.startDragListener;
import longse.com.herospeed.popupWindow.MainRightPopWindow;
import longse.com.herospeed.tools.BarTools;
import longse.com.herospeed.tools.ImageTool;
import longse.com.herospeed.tools.RecyclerViewDividerTool;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.TitleView;

/**
 * Created by LY on 2017/10/25.
 */

public class DevicesFragment extends BaseFragment implements startDragListener {

    @BindView(R.id.devices_title)
    TitleView devicesTitle;
    @BindView(R.id.device_recycle)
    RecyclerView deviceRecycle;
    @BindView(R.id.devices_bar_statue)
    View devicesBarStatue;

    private MainRightPopWindow rightPopWindow;
    //拖动
    private ItemTouchHelper itemTouchHelper;

    private static DevicesFragment devicesFragment;
    private AdapterDevicesRecycle adapterDevices;
    private List<DeviceInfoBean> devicesList;

    public static DevicesFragment newInstance() {
        if (devicesFragment == null) {
            devicesFragment = new DevicesFragment();
        }
        return devicesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, null);
        ButterKnife.bind(this, view);
        //注册eventBus
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        lodingDevices();
    }

    private void initView() {
        devicesTitle.setRightIcon(R.drawable.point);
        devicesTitle.setLeftIcon(R.drawable.menu_img);
        rightPopWindow = new MainRightPopWindow(getActivity());

        //*******************statusbar star*******************//
        //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) devicesBarStatue.getLayoutParams();
        // 控件的宽强制设成30
        linearParams.height = BarTools.getActionBarHeight(getActivity()) / 2;
        //使设置好的布局参数应用到控件
        devicesBarStatue.setLayoutParams(linearParams);
        //*******************statusbar end*******************//


//        //滑动冲突
//        deviceRecycle.setNestedScrollingEnabled(false);
        //recycle 显示模式
        deviceRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        //item之间的间距
        deviceRecycle.addItemDecoration(new RecyclerViewDividerTool(ImageTool.dp2px(5f, getActivity())));
    }

    private void initListener() {
        devicesTitle.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventFeiendsFragment(Constants.SHOWMENU));
            }
        });

        devicesTitle.setRightIconOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                rightPopWindow.showAtLocation(devicesTitle, Gravity.TOP | Gravity.RIGHT, 0, 0);
            }
        });

        rightPopWindow.setAddFriendsAdnDeviceOnClickLicener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightPopWindow.isShowing()) {
                    rightPopWindow.dismiss();
                }
                SkipActivity.startSearchActivity(getActivity());
            }
        });

        rightPopWindow.setSignOutOnClickLicener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightPopWindow.isShowing()) {
                    rightPopWindow.dismiss();
                }
            }
        });
    }

    private void lodingDevices() {
        LogUtil.e("DeviceFragment==isLogin=", String.valueOf(SharedUtils.getBoolean(getContext(), Constants.CACHE_ISLOGIN, false)));
        if (SharedUtils.getBoolean(getContext(), Constants.CACHE_ISLOGIN, false)) {
            if (devicesList == null) {
                showProgress();
            }
            RequestManager.getInstance().GetAllDevice();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        if (event == null) return;
        LogUtil.e("DevicesFragment==", event.toString());
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                if (event.getAction().equals(ConstAction.ACTION_ALLDEVICES)) {
                    dismissProgress();
                    devicesList = event.getDevicesList();
                    if (devicesList == null) return;
                    initRecycle(devicesList);
                }
                break;

            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_ALLDEVICES)) {
                    if (event.getObject() != null) {
                        Toast.makeText(getActivity(), (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(), "Sign in failure", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(getActivity(), "The network or server error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initRecycle(final List<DeviceInfoBean> devicesLists) {

        adapterDevices = new AdapterDevicesRecycle(devicesLists);
        adapterDevices.setDraglistener(this);
        deviceRecycle.setAdapter(adapterDevices);

        //itemHelper的回调
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

            /**
             * 官方文档的说明如下：
             * o control which actions user can take on each view, you should override getMovementFlags(RecyclerView, ViewHolder)
             * and return appropriate set of direction flags. (LEFT, RIGHT, START, END, UP, DOWN).
             * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
             * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
             * 不过点入到源码里面有惊喜。源码标记方向如下：
             *  public static final int UP = 1     0001
             *  public static final int DOWN = 1 << 1; （位运算：值其实就是2）0010
             *  public static final int LEFT = 1 << 2   左 值是4
             *  public static final int RIGHT = 1 << 3  右 值是8
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //也就是说返回值是组合式的
                //makeMovementFlags (int dragFlags, int swipeFlags)，看下面的解释说明
                int swipFlag = 0;
                //如果也监控左右方向的话，swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                int dragflag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //等价于：0001&0010;多点触控标记触屏手指的顺序和个数也是这样标记哦
                return makeMovementFlags(dragflag, swipFlag);

                /**
                 * 备注：由getMovementFlags可以联想到setMovementFlags，不过文档么有这个方法，但是：
                 * 有 makeMovementFlags (int dragFlags, int swipeFlags)
                 * Convenience method to create movement flags.便捷方法创建moveMentFlag
                 * For instance, if you want to let your items be drag & dropped vertically and swiped left to be dismissed,
                 * you can call this method with: makeMovementFlags(UP | DOWN, LEFT);
                 * 这个recyclerview的文档写的简直完美，示例代码都弄好了！！！
                 * 如果你想让item上下拖动和左边滑动删除，应该这样用： makeMovementFlags(UP | DOWN, LEFT)
                 */

                //拓展一下：如果只想上下的话：makeMovementFlags（UP | DOWN, 0）,标记方向的最小值1
            }


            /**
             * 官方文档的说明如下
             * If user drags an item, ItemTouchHelper will call onMove(recyclerView, dragged, target). Upon receiving this callback,
             * you should move the item from the old position (dragged.getAdapterPosition()) to new position (target.getAdapterPosition())
             * in your adapter and also call notifyItemMoved(int, int).
             * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
             * viewHolde:正在拖动item
             * target：要拖到的目标
             * @return true 表示消费事件
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapterDevices.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(devicesLists, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }


            /**
             * 谷歌官方文档说明如下：
             * 这个看了一下主要是做左右拖动的回调
             * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls onSwiped(ViewHolder, int).
             * At this point, you should update your adapter (e.g. remove the item) and call related Adapter#notify event.
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //暂不处理
            }


            /**
             * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
             * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
             * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
             * @param recyclerView
             * @param current
             * @param target
             * @return
             */
            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return true;
            }


            /**
             * 官方文档说明如下：
             * Returns whether ItemTouchHelper should start a drag and drop operation if an item is long pressed.
             * 是否开启长按 拖动
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                //return true后，可以实现长按拖动排序和拖动动画了
                return false;
            }
        };


        //1.创建item helper
        itemTouchHelper = new ItemTouchHelper(callback);
        //2.绑定到recyclerview上面去
        itemTouchHelper.attachToRecyclerView(deviceRecycle);
        //3.在ItemHelper的接口回调中过滤开启长按拖动，拓展其他操作
    }

    @Override
    public void startDragItem(RecyclerView.ViewHolder holder) {
        //谷歌官方文档如下：
        //开启拖动我们给他的holder，但是默认
        // 是长按拖动，直接代码调用拖动要先禁止长按拖动
        //Starts dragging the provided ViewHolder. By default,
        // ItemTouchHelper starts a drag when a View is long pressed.
        // You can disable that behavior by overriding isLongPressDragEnabled().
        itemTouchHelper.startDrag(holder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
