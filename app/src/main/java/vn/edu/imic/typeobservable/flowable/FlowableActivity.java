package vn.edu.imic.typeobservable.flowable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.typeobservable.R;

public class FlowableActivity extends AppCompatActivity {
    /*Flowable : nên được dùng khi Observable tạo ra 1 số lượng
    * lớn các sự kiện, dữ liệu mà Observer có thể xử lý.
    * Flowable có thể được sử dụng khi nguồn sinh ra 10k events và
    * subscriber không thể xử lý hết
    * Ví dụ: Flowable phát ra data là các số từ 1-100 và sử dụng
    * toán tử reduce để thêm tất cả các số, sau đó phát ra giá trị cuối cùng*/
    private static final String TAG = FlowableActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
        Flowable<Integer> integerFlowable = getFlowableObservable();

        SingleObserver<Integer> singleObserver = getFlowableObserver();

        integerFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer number) throws Exception {
                        return result + number;
                    }
                })
                .subscribe(singleObserver);
    }

    private SingleObserver<Integer> getFlowableObserver() {
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };
    }

    private Flowable<Integer> getFlowableObservable() {
        return Flowable.range(1,100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
