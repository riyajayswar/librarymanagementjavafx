package model;

public class Book {

    private int id;
    private String title;
    private String author;
    private String category;

    private int totalQuantity;
    private int availableQuantity;

    public Book(int id,
                String title,
                String author,
                String category,
                int totalQuantity,
                int availableQuantity) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}