package entities;

import java.util.Date;

public class BookCopy {

    private final long id;
    private final Book book;
    private final Date addedToLibrary;
    private String shelfLocation;
    private Date lentDate;
    private boolean lent;

    public BookCopy(long id, Book book) {
        this.id = id;
        this.book = book;
        this.addedToLibrary = new Date();
        this.lent = false;
    }

    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public Date getAddedToLibrary() {
        return addedToLibrary;
    }

    public boolean isLent() {
        return lent;
    }

    public void setLent(boolean lent) {
        this.lent = lent;
    }

    public Date getLentDate() {
        return lentDate;
    }

    public void setLentDate(Date lentDate) {
        this.lentDate = lentDate;
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Author(en): %s, ISBN: %s, ID: %d, Shelf location: %s, Lent status: %b, Lent Date: %s\n", book.getTitle(), book.getAuthors(), book.getIsbn(), id, shelfLocation, lent, lentDate);
    }
}
