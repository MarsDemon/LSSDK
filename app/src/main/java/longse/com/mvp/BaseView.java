package longse.com.mvp;

public interface BaseView extends MvpView {
    void showProgress();

    void onCompleted();

    void onError(Throwable e);

}
