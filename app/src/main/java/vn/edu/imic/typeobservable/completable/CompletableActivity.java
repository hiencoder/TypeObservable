package vn.edu.imic.typeobservable.completable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.typeobservable.R;
import vn.edu.imic.typeobservable.model.Note;

public class CompletableActivity extends AppCompatActivity {
    private static final String TAG = CompletableActivity.class.getSimpleName();
    private Disposable disposable;
    /*Completable observable không phát ra bất kỳ dữ liệu thay vào đó
    * nó thông báo ra trạng thái của task là thành công hay thất bại
    * Observable này có thể được sử dụng khi ta muốn thực hiện một số
    * nhiệm vụ và không mong đợi nhận về một giá trị.
    * Ví dụ trường hợp cập nhật data lên server bằng yêu cầu PUT*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completable);
        Note note = new Note(1,"Do exercises");

        Completable completable = updateNote(note);

        CompletableObserver completableObserver = getCompletableObserver();

        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver);
    }

    /**/
    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Note update successfully");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };
    }

    /**
     *Ví dụ request PUT server
     * @return
     */
    private Completable updateNote(Note note) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if (!emitter.isDisposed()){
                    Thread.sleep(1000);
                    emitter.onComplete();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
