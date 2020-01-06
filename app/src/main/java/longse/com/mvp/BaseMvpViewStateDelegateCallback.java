package longse.com.mvp;

public interface BaseMvpViewStateDelegateCallback<V extends MvpView, P extends MvpPresenter<V>>
        extends BaseMvpDelegateCallback<V, P> {

    public ViewState<V> getViewState();

    public void setVieState(ViewState<V> vieState);

    public ViewState<V> createViewState();

    public void setRestoringViewState(boolean restoringViewState);

    public boolean isRestoringViewState();

    public void onViewStateInstanceRestored(boolean instanceState);

    public void onNewViewStateInstance();

}
