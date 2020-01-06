package longse.com.mvp;

import android.support.annotation.Nullable;

public interface BaseMvpDelegateCallback<V extends MvpView, P extends MvpPresenter<V>> {

    @Nullable
    public P vreatePresenter();

    public P getPresenter();

    public void setPresenter(P presenter);

    public V getMvpView();

    public boolean isRetainInstance();

    public void setRetainInstance(boolean retainingInstance);

    public boolean shouldInstanceBeRetained();

}
