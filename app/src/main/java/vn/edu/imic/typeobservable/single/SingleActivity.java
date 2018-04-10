package vn.edu.imic.typeobservable.single;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.typeobservable.R;
import vn.edu.imic.typeobservable.model.Note;

public class SingleActivity extends AppCompatActivity {
    /*Single luôn chỉ phát ra 1 giá trị hoặc error.
    Trường hợp sử dụng single để thực hiện cuộc gọi mạng để lấy về
    response, response sẽ được sử dụng 1 lần
    Ví dụ luôn phát ra một Note duy nhất, note có thể được phát ra từ database
    *SingleObserver không có onNext() để phát ra dữ liệu thay vào đó dữ liệu
    * sẽ được nhận trong onSuccess().
     *  */
    private static final String TAG = SingleActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Single<Note> noteObservable = getNoteObservable();

        SingleObserver<Note> noteSingleObserver = getNoteSingleObserver();

        //Subscribe
        noteObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(noteSingleObserver);
    }

    private SingleObserver<Note> getNoteSingleObserver() {
        return new SingleObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess: " + note.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };
    }

    /**
     * @return
     */
    private Single<Note> getNoteObservable() {
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Buy milk");
                emitter.onSuccess(note);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private List<Note> prepareNote() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "Note1"));
        notes.add(new Note(2, "Note2"));
        notes.add(new Note(3, "Note3"));
        notes.add(new Note(4, "Note4"));
        notes.add(new Note(5, "Note5"));
        return notes;
    }

}
