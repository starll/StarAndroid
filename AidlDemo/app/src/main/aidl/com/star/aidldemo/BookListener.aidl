// BookListener.aidl
package com.star.aidldemo;

// Declare any non-default types here with import statements
import com.star.aidldemo.Book;

interface BookListener {
    void pushNewBook(in Book book);
}
