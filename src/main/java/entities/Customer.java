package entities;

import java.util.List;

public class Customer {

    private final long id;
    private final List<BookCopy> bookCopies;
    private String lastName;
    private String firstName;
    private String address;
    private String zipCode;
    private String city;
    private boolean feesPayed;


    public Customer(long id, List<BookCopy> bookCopies, String lastName, String firstName) {
        this.id = id;
        this.bookCopies = bookCopies;
        this.feesPayed = true;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Customer(long id, List<BookCopy> bookCopies, String lastName, String firstName, String address, String zipCode, String city) {
        this.id = id;
        this.bookCopies = bookCopies;
        this.feesPayed = true;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isFeesPayed() {
        return feesPayed;
    }

    public void setFeesPayed(boolean feesPayed) {
        this.feesPayed = feesPayed;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Last name: %s, First name: %s, FeesPayed-Status: %b, Number of books lent: %d | [Customer] \n", id, lastName, firstName, isFeesPayed(), getBookCopies().size());
    }
}
