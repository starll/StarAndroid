package com.star.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity >>> ";
    private List<Book> mBookList = new ArrayList<>();

    private BookManager mBookManager;
    private BookListener mListener = new BookListener.Stub() {
        @Override
        public void pushNewBook(Book book) throws RemoteException {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.e(TAG, "New book coming ...");
            } else {
                Log.e(TAG, "New book comming ... in son thread");
            }
        }
    };
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过service以获取Aidl对象
            mBookManager = BookManager.Stub.asInterface(service);

            try {
                Log.e(TAG, mBookManager.add(1, 5) + "");

                mBookList = mBookManager.getList();
                Log.e(TAG, "BookList: " + mBookList.toString());

                Book newBook = new Book(3, "POS");
                mBookManager.addBook(newBook);

                mBookList = mBookManager.getList();
                Log.e(TAG, "After adding: " + mBookList);

                mBookManager.registerListener(mListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, MyService.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                mBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(conn);
        super.onDestroy();
    }
}
