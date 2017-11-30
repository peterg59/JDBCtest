package group.artifact;

import java.sql.*;

public class Book {
	
	private String title, author;
	private Integer id;
	
	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
