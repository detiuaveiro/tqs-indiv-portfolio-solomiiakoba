/*
 * (C) Copyright 2017 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package tqs;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Library {
    private final List<Book> store = new ArrayList<>();

    public Library() {
    }
    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (Book book : store) {
            if (book.getAuthor().equals(author)) {
                books.add(book);
            }
        }
        return books;
    }
    public void addBook(Book b) {
        store.add(b);
    }
    public List<Book> findBooks(LocalDateTime start, LocalDateTime end) {
        List<Book> books = new ArrayList<>();
        for (Book book : store) {
            LocalDateTime date = book.getPublished();
            if ((date.isEqual(start) || date.isAfter(start)) &&
                    (date.isEqual(end)   || date.isBefore(end))) {
                books.add(book);
            }
        }
        return books;
    }

}