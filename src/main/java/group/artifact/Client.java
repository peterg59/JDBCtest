package group.artifact;

import java.sql.*;

public class Client {
	
	private String lastName, firstName;
	private Gender gender;
	private Integer id;
	
	public Client(String lastName, String firstName, Gender gender) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public int getId() {
		return id;
	}
	public Gender getGender() {
		return gender;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
