package longse.com.mvp;
import android.support.v4.app.Fragment;


/**
 * A ViewState is , like the name suggests, responsible to store the views state. In other words:
 * The view like an Activity or a Fragment stores his state, like "showing loading animation", showing
 * error view, etc. The goal is to have a views that can easily restore there state after screen
 * @param <V>
 */
public interface ViewState<V extends MvpView> {

    /**
     * Called to apply this viewstate on a given view.
     * @param view The {@link MvpView}
     * @param retained true, if the components like the viewstate and the presenter have been
     *                 retained
     *                 because the {@link Fragment#setRetainInstance(boolean)} has been set to true.
     */
    public void apply(V view, boolean retained);
}