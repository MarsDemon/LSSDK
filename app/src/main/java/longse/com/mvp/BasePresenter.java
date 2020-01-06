package longse.com.mvp;

import longse.com.herospeed.base.MyApplication;

public class BasePresenter<V extends BaseView> extends MvpBasePresenter<V> {

    private MyApplication application;

    @Override
    public boolean isViewAttached() {
        return super.isViewAttached();
    }
}
