package longse.com.herospeed.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by LY on 2017/11/17.
 * 拖动排序接口
 */

public interface startDragListener {
    //触摸Item，开启拖动的接口
    void startDragItem(RecyclerView.ViewHolder holder);
}
