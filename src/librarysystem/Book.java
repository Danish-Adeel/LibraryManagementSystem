/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem;

/**
 *
 * @author Danish
 */
public class Book {
    String id;
    String title;
    String author;
    String publisher;
    String lostprice;
    private Boolean isAvailable;

   
    public Book(String id, String title, String author, String publisher, String lostprice, Boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.lostprice = lostprice;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    

    public String getLostprice() {
        return lostprice;
    }

    public void setLostprice(String lostprice) {
        this.lostprice = lostprice;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    
}
