package vn.edu.imic.typeobservable.observable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.typeobservable.R;
import vn.edu.imic.typeobservable.model.Note;

public class ObservableActivity extends AppCompatActivity {
    /*Obervable - Observer
    * Observable có thể phát ra 1 hoặc nhiều item
    * Ví dụ phát ra từng item Note, ta cũng có thể phát
    * ra một danh sách note cùng 1 lúc nhưng nếu muốn biến đổi
    * toán tử trên mỗi note thì phát ra mỗi note sẽ tốt hơn*/
    private Disposable disposable;
    private static final String TAG = ObservableActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<Note> noteObservable = getNoteObservable();

        Observer<Note> noteObserver = getNoteObserver();

        noteObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(noteObserver);
    }

    private Observer<Note> getNoteObserver() {
        return new Observer<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onNext(Note note) {
                Log.d(TAG, "onNext: " + note.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    /**
     *
     * @return
     */
    private Observable<Note> getNoteObservable() {
        final List<Note> notes = prepareNote();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes){
                    if (!emitter.isDisposed()){
                        emitter.onNext(note);
                    }
                }
                if (!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Note> prepareNote() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1,"Note1"));
        notes.add(new Note(2,"Note2"));
        notes.add(new Note(3,"Note3"));
        notes.add(new Note(4,"Note4"));
        notes.add(new Note(5,"Note5"));
        return notes;
    }
}
