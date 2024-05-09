package entities;

import java.util.List;

public class Book {

    private final String isbn;
    private final String title;
    private final List<String> authors;
    private final int year;
    private final String city;
    private final String publisher;
    private final int edition;


    public Book(String isbn, String title, List<String> authors, int year, String city, String publisher, int edition) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.city = city;
        this.publisher = publisher;
        this.edition = edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public int getYear() {
        return year;
    }

    public String getCity() {
        return city;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getEdition() {
        return edition;
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Author(en): %s, Year: %d, ISBN: %s | [Book]\n", title, authors, year, isbn);
    }

}
