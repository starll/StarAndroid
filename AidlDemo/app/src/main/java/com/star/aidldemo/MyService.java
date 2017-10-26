package com.star.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

    private static String TAG = "MyService >>> ";
    private boolean isDestroy = false;
    private List<Book> mBookList = new ArrayList<>();
    private List<BookListener> mListenerList = new ArrayList<>();

    public MyService() {
    }

    Binder myBinder = new BookManager.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public List<Book> getList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(BookListener listener) throws RemoteException {
            if (listener != null) {
                if (!mListenerList.contains(listener)) {
                    mListenerList.add(listener);
                    Log.e(TAG, "Register Listener");
                } else {
                    Log.e(TAG, "Listener already existed");
                }
            }
            Log.e(TAG, "Current listener size: " + mListenerList.size());
        }

        @Override
        public void unregisterListener(BookListener listener) throws RemoteException {
            if (listener != null) {
                if (mListenerList.contains(listener)) {
                    mListenerList.remove(listener);
                    Log.e(TAG, "Unregister Listener");
                } else {
                    Log.e(TAG, "Listener not found");
                }
            }
            Log.e(TAG, "Current listener size: " + mListenerList.size());
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "iOS"));
        processAddingBook();
    }

    /**
     * 模拟不断添加图书
     */
    private void processAddingBook() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestroy) {
                    try {
                        Thread.sleep(7500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int bookId = mBookList.size() + 1;
                    String bookName = "Book - " + bookId;
                    Book newBook = new Book(bookId, bookName);
                    onPushNewBook(newBook);
                }
            }
        }).start();
    }

    private void onPushNewBook(Book book) {
        mBookList.add(book);
        for (int i = 0; i < mListenerList.size(); i++) {
            BookListener listener = mListenerList.get(i);
            try {
                listener.pushNewBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }
}
