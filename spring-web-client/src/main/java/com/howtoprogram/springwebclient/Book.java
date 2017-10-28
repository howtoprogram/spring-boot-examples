package com.howtoprogram.springwebclient;

public class Book {
  private Long id;
  private String name;
  private String author;
  // all getters and setters


  public Book() {

  }

  public Long getId() {
    return id;
  }

  public Book(String name, String author) {
    super();
    this.name = name;
    this.author = author;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public String toString() {
    return "Book [id=" + id + ", name=" + name + ", author=" + author + "]";
  }


}
