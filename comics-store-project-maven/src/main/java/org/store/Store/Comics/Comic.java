package org.store.Store.Comics;

import java.time.LocalDate;

public class Comic {
    private final String title;
    private final String author;
    private final Genre genre;
    private final LocalDate releaseDate;
    private int price;
    private int amountInStock;

    public Comic(String title, String author, Genre genre, LocalDate releaseDate, int price, int amountInStock) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.price = price;
        this.amountInStock = amountInStock;
    }


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public int getAmountInStock() {
        return this.amountInStock;
    }
}
