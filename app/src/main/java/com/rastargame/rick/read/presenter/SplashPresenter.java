package com.rastargame.rick.read.presenter;
import com.rastargame.rick.read.base.RxPresenter;
import com.rastargame.rick.read.model.entity.SplashEntity;
import com.rastargame.rick.read.model.http.CommonSubscriber;
import com.rastargame.rick.read.model.http.DataManagerModel;
import com.rastargame.rick.read.utils.RxUtil;
import org.reactivestreams.Publisher;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class SplashPresenter extends RxPresenter<SplashContract.View> implements SplashContract.Presenter {

    private DataManagerModel mDataMangerModel;

    @Inject
    public SplashPresenter(DataManagerModel dataManagerModel) {
        this.mDataMangerModel = dataManagerModel;
    }

    @Override
    public void getSplash(String deviceId) {
        String client = "android";
        final String version = "1.3.0";
        Long time =  System.currentTimeMillis()/1000;
        addSubscribe(mDataMangerModel.getSplash(client, version, time,deviceId)
            .compose(RxUtil.<SplashEntity>rxSchedulerHelper())
            .flatMap(new Function<SplashEntity, Publisher<SplashEntity>>() {
                @Override
                public Publisher<SplashEntity> apply(SplashEntity splashEntity) throws Exception {
                    return Flowable.just(splashEntity);
                }
            }).subscribeWith(new CommonSubscriber<SplashEntity>(view) {
                    @Override
                    public void onNext(SplashEntity splashEntity) {
                        if (splashEntity.getStatus().equals("ok")) {
                            view.showBackground(splashEntity.getImages().get(0));
                        } else {
                            view.showErrorMsg("背景获取失败");
                        }
                    }
                })
        );
    }
}
