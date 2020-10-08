package com.example.rxjavabasic1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NoteProject extends AppCompatActivity {

    private static final String TAG = "Project1";

    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_project);


        Observable<Note> noteObserable = getNoteObserable();

        DisposableObserver<Note>  noteobserver = getNoteObserver();




        compositeDisposable.add(noteObserable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Note, Note>() {
                    @Override
                    public Note apply(Note note) throws Exception {

                        note.setNote(note.getNote());
                        return note;
                    }
                })
        .subscribeWith(noteobserver));


    }

    private DisposableObserver<Note> getNoteObserver() {
        return new DisposableObserver<Note>() {
            @Override
            public void onNext(Note note) {
                Log.d(TAG, "Note: " + note.getNote());

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All notes are emitted!");

            }
        };
    }

    private Observable<Note> getNoteObserable() {
        final List<Note> notes = prepareNotes();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
               for (Note note : notes) {
                   if (!emitter.isDisposed()) {
                       emitter.onNext(note);
                   }
               }
                   if(!emitter.isDisposed()){
                       emitter.onComplete();
                   }

            }
        });
    }

    private List<Note> prepareNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("buy tooth paste!", 1));
        notes.add(new Note("call brother!", 2));
        notes.add(new Note("watch narcos tonight!", 3));
        notes.add(new Note("pay power bill!", 4));

        return notes;
    }


    class Note {
        String note;
        int id;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Note(String note, int id) {
            this.note = note;
            this.id = id;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}