// BookManager.aidl
package com.star.aidldemo;

// Declare any non-default types here with import statements
import com.star.aidldemo.Book;
import com.star.aidldemo.BookListener;

interface BookManager {
    int add(int a, int b);
    List<Book> getList();
    // 注意参数的类型，既in要写
    void addBook(in Book book);

    void registerListener(BookListener listener);
    void unregisterListener(BookListener listener);

}
