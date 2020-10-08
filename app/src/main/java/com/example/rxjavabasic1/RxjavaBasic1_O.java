package com.example.rxjavabasic1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxjavaBasic1_O extends AppCompatActivity {

    private String TAG = "MainActivity";

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Observable<String> itmeobservable = Observable.just("Cat" , "Cow", "Pig" ,"Parrot" ,"Rat" ,"Tiger");

        Observer<String> itemobserver = getItemObserver();

        itmeobservable
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toUpperCase().startsWith("C");
                    }
                })
                .subscribe(itemobserver);



    }




    private Observer<String> getItemObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);

            }

            @Override
            public void onError(Throwable e) {

                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(disposable != null){
            disposable.dispose();
        }


    }
}