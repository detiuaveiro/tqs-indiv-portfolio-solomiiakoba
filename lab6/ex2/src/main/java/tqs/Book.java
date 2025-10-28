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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Book {
    private LocalDateTime published;
    private String author;
    private String title;

    public Book(String author, String title, LocalDateTime published) {
        this.published = published;
        this.author = author;
        this.title = title;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "published=" + published +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}