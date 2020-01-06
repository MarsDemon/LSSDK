package longse.com.herospeed.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.adapter.AdapterScreenRecycle;
import longse.com.herospeed.bean.ScreenBean;
import longse.com.herospeed.interfaces.startDragListener;
import longse.com.herospeed.tools.ImageTool;
import longse.com.herospeed.tools.RecyclerViewDividerTool;
import longse.com.herospeed.widget.TitleView;

public class ScreenActivity extends BaseActivity implements startDragListener {

    @BindView(R.id.screen_title)
    TitleView screenTitle;
    @BindView(R.id.screen_recycle)
    RecyclerView screenRecycleView;
    @BindView(R.id.one)
    TextView one;
    @BindView(R.id.four)
    TextView four;
    @BindView(R.id.nine)
    TextView nine;
    @BindView(R.id.sixteen)
    TextView sixteen;
    @BindView(R.id.activity_screen)
    LinearLayout activityScreen;

    private static final int ONE = 1;
    private static final int FOUR = 4;
    private static final int NINE = 9;
    private static final int SIXTEEN = 16;

    private StaggeredGridLayoutManager gridLayoutManager;
    private AdapterScreenRecycle screenRecycleAdapter;
    private ItemTouchHelper itemTouchHelper;//拖动
    private List<ScreenBean> screenList;


    @Override
    protected void initBefore() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_screen;
    }

    @Override
    protected void initView() {
        super.initView();
        screenTitle.setLeftFinish(this);

        initRecycle();

        initTouch();

    }

    private void initTouch() {
        //itemHelper的回调
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipFlag = 0;
                int dragflag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragflag, swipFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                screenRecycleAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(screenList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //暂不处理
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //return true后，可以实现长按拖动排序和拖动动画了
                return false;
            }
        };

        //1.创建item helper
        itemTouchHelper = new ItemTouchHelper(callback);
        //2.绑定到recyclerview上面去
        itemTouchHelper.attachToRecyclerView(screenRecycleView);
        //3.在ItemHelper的接口回调中过滤开启长按拖动，拓展其他操作
    }

    private void initRecycle() {
        screenList = new ArrayList<ScreenBean>();
        for (int i = 1; i < 17; i++) {
            ScreenBean sb = new ScreenBean(i + "", "");
            screenList.add(sb);
        }
        screenRecycleAdapter = new AdapterScreenRecycle(screenList);

        //recycle 显示模式
        gridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setSpanCount(2);

        screenRecycleView.setLayoutManager(gridLayoutManager);
        screenRecycleAdapter.setDraglistener(this);
        //item之间的间距
        screenRecycleView.addItemDecoration(new RecyclerViewDividerTool(ImageTool.dp2px(5f, context)));
        screenRecycleView.setAdapter(screenRecycleAdapter);

    }


    private void refreshRecycle(int screenNum) {
        final int i = (int) Math.sqrt(screenNum);
        if (i == 1) {
            screenRecycleAdapter.screenNum(1);
        } else if (i == 2) {
            screenRecycleAdapter.screenNum(4);
        } else if (i == 3) {
            screenRecycleAdapter.screenNum(9);
        } else if (i == 4) {
            screenRecycleAdapter.screenNum(16);
        }
        gridLayoutManager.setSpanCount(i);
        screenRecycleAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.one, R.id.four, R.id.nine, R.id.sixteen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                refreshRecycle(ONE);
                break;
            case R.id.four:
                refreshRecycle(FOUR);
                break;
            case R.id.nine:
                refreshRecycle(NINE);
                break;
            case R.id.sixteen:
                refreshRecycle(SIXTEEN);
                break;
        }
    }

    @Override
    public void startDragItem(RecyclerView.ViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }
}
