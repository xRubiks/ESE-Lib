package entities;

import java.util.Date;

public class BookCopy {

    private final long id;
    private final Book book;
    private final Date addedToLibrary;
    private String shelfLocation;
    private Date lentDate;
    private boolean lent;

    public BookCopy(long id, Book book, Date addedToLibrary) {
        this.id = id;
        this.book = book;
        this.addedToLibrary = addedToLibrary;
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
}
