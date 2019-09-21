package models;

public class Book {
    private long bookId;
    private String title;
    private String author;
    private Integer pagesSum;
    private Integer yearPublished;
    private String publishingHouse;

    public long getId() {
        return bookId;
    }

    public void setId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPagesSum() {
        return pagesSum;
    }

    public void setPagesSum(Integer pagesSum) {
        this.pagesSum = pagesSum;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }
}