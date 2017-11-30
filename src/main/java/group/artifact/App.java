package group.artifact;

import java.sql.*;

public class App {

	public static void main(String[] args) throws SQLException {
		String url = "jdbc:postgresql://formation.iocean.fr:5432/formation12";
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet resultSet = null;
		String str = null;
		try {
			conn = DriverManager.getConnection(url, "formation12", "formationSQL");
			conn.setAutoCommit(false);
			try {
				stmt = conn.createStatement();
				
				/*Suppression des tables*/
				
				stmt.executeUpdate("DROP TABLE IF EXISTS achat");
				stmt.executeUpdate("DROP TABLE IF EXISTS client");
				stmt.executeUpdate("DROP TABLE IF EXISTS book");
				
				/*Suppression du type*/
				
				stmt.executeUpdate("DROP TYPE IF EXISTS gender");
				
				/*Creation des tables*/
				
				stmt.executeUpdate("CREATE TABLE book(id bigserial PRIMARY KEY, title varchar(255) NOT NULL, author varchar(255))");
				stmt.executeUpdate("CREATE TYPE gender AS ENUM ('M', 'F')");
				stmt.executeUpdate("CREATE TABLE client(id bigserial PRIMARY KEY, lastname varchar(255) NOT NULL, firstname varchar(255) NOT NULL, gender gender NOT NULL, fk_book bigint, FOREIGN KEY (fk_book) REFERENCES book(id) )");
				stmt.executeUpdate("CREATE TABLE achat(fk_book bigint, fk_client bigint, FOREIGN KEY (fk_book) REFERENCES book(id), FOREIGN KEY (fk_client) REFERENCES client(id) )");
				
				/*Remplissage des tables*/
				
				Book book1 = new Book("Le seigneur des anneaux", "J.R.R Tolkien");
				Book book2 = new Book("Dragon Ball", "Akira Toriyama");
				Book book3 = new Book("One Piece", "Eichiro Oda");
				Book book4 = new Book("Star Wars", "George Lucas");
				Book book5 = new Book("Spiderman", "Stan Lee");
				
				insertToBook(stmt, book1);
				insertToBook(stmt, book2);
				insertToBook(stmt, book3);
				insertToBook(stmt, book4);
				insertToBook(stmt, book5);
				
				Client client1 = new Client("Guyard", "Pierre", Gender.M);
				Client client2 = new Client("Joseph", "Caleb", Gender.M);
				Client client3 = new Client("Tan", "Julie", Gender.F);
				
				insertToClient(stmt, client1);
				insertToClient(stmt, client2);
				insertToClient(stmt, client3);
				
				insertToAchat(stmt, book1, client1);
				insertToAchat(stmt, book1, client2);
				insertToAchat(stmt, book1, client3);
				insertToAchat(stmt, book5, client2);
				insertToAchat(stmt, book4, client1);

				
				/*Quels livres ont été achetés par un certain client*/
				
				str = "Joseph";
				pstmt = conn.prepareStatement("SELECT title, author from client, book, achat where client.lastname = ? and fk_client = client.id and book.id = achat.fk_book ");
				pstmt.setString(1, str);
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					   String title = resultSet.getString("title");
					   String author = resultSet.getString("author");
					   System.out.println(author + ", " + title);
				}
				
				/*Quels clients ont acheté un certain livre*/
				
				str = "Star Wars";
				pstmt2 = conn.prepareStatement("SELECT lastname, firstname from client, book, achat where book.title = ? and fk_client = client.id and book.id = achat.fk_book ");
				pstmt2.setString(1, str);
				resultSet = pstmt2.executeQuery();
				while (resultSet.next()) {
					   String lastname = resultSet.getString("lastname");
					   String firstname = resultSet.getString("firstname");
					   System.out.println(lastname + ", " + firstname);
				}
				conn.commit();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				conn.rollback();
			} finally {
				conn.setAutoCommit(true);
				stmt.close();
				pstmt.close();
				pstmt2.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
	
	public static void insertToBook(Statement stmt , Book book) throws SQLException {
		stmt.executeUpdate("INSERT INTO book (title, author) VALUES ('" + book.getTitle()+"', '"+ book.getAuthor() +"')", Statement.RETURN_GENERATED_KEYS);
		ResultSet generatedKeys = stmt.getGeneratedKeys();
		generatedKeys.next();
		int id = generatedKeys.getInt("id");
		book.setId(id);
	}
	
	public static void insertToClient(Statement stmt , Client client) throws SQLException {
		stmt.executeUpdate("INSERT INTO client (lastName, firstName, gender) VALUES ('" + client.getLastName()+"', '"+ client.getFirstName() +"', '" + client.getGender() +"')",Statement.RETURN_GENERATED_KEYS);
		ResultSet generatedKeys = stmt.getGeneratedKeys();
		generatedKeys.next();
		int id = generatedKeys.getInt("id");
		client.setId(id);
	}
	
	public static void insertToAchat(Statement stmt, Book book, Client client) throws SQLException {
		stmt.executeUpdate("INSERT INTO achat (fk_book, fk_client) VALUES ('"+ book.getId() +"', '"+ client.getId() + "')");
	}

}
