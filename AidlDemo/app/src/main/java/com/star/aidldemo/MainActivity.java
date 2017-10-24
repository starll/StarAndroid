package com.star.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过service以获取Aidl对象
            mBookManager = BookManager.Stub.asInterface(service);

            try {
                Log.e(TAG, mBookManager.add(1, 5) + "");

                mBookList = mBookManager.getList();
                Log.e(TAG, "BookList: "+mBookList.toString());

                Book newBook = new Book(3,"POS");
                mBookManager.addBook(newBook);

                mBookList = mBookManager.getList();
                Log.e(TAG,"After adding: "+mBookList);

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


}
