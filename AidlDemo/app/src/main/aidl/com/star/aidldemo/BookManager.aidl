// BookManager.aidl
package com.star.aidldemo;

// Declare any non-default types here with import statements
import com.star.aidldemo.Book;

interface BookManager {
        int add(int a, int b);
    List<Book> getList();
    // 注意参数的类型，既in要写
    void addBook(in Book book);
}
