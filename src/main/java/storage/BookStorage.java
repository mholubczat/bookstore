package storage;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {

    private static final String POSTGRES_JDBC_URL = "jdbc:postgresql://localhost:5432/bookstore";
    private static final String POSTGRES_USER_NAME = "postgres";
    private static final String POSTGRES_USER_PASS = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Postgres driver not found: \n" + e);
        }
        //createDatabase();
    }
    private static Connection initializeDataBaseConnection() {
        try {
            return DriverManager.getConnection(POSTGRES_JDBC_URL, POSTGRES_USER_NAME, POSTGRES_USER_PASS);
        } catch (SQLException e) {
            System.err.println("Server can't initialize database connection: \n" + e);
            throw new RuntimeException("Server can't initialize database connection");
        }
    }

    private static void closeDatabaseResources(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database resources: \n" + e);
            throw new RuntimeException("Error closing database resources");
        }
    }


    public static void createDatabase(){

        final String sqlCreateBookDatabase = "CREATE DATABASE bookstore" +
                " WITH OWNER = postgres " +
                " ENCODING = 'UTF8' CONNECTION LIMIT = -1;";

        final String sqlCreateBookTable = "CREATE TABLE books " +
                "(\n" +
                "book_id serial NOT NULL, \n" +
                "title character varying(128) NOT NULL, \n" +
                "author character varying(128) NOT NULL, \n" +
                "pages_sum bigint, " +
                "year_published integer, " +
                "publishing_house character varying(128), " +
                "PRIMARY KEY (book_id))";

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        try {
            //  preparedStatement = connection.prepareStatement(sqlCreateBookDatabase);
            //  preparedStatement.execute();
            connection.prepareStatement(sqlCreateBookTable).execute();

        } catch (SQLException e) {
            System.err.println("Error creating sql database: \n" + e.getMessage());
            throw new RuntimeException("Error creating sql database");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }

    }



    public Book getBook(long id) {
        final String sqlSelectBook = "SELECT * FROM books WHERE book_id = ?;";


        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        Statement statement = null;

        try {

            preparedStatement = connection.prepareStatement(sqlSelectBook);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setAuthor(resultSet.getString("author"));
                book.setId(resultSet.getLong("book_id"));
                book.setPublishingHouse(resultSet.getString("publishing_house"));
                book.setYearPublished(resultSet.getInt("year_published"));
                book.setPagesSum(resultSet.getInt("pages_sum"));
                book.setTitle(resultSet.getString("title"));
                return book;
            }
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }
        return null;
    }


    public List<Book> getAllBooks() {
        final String sqlGetAllBook = "SELECT * FROM books;";

        Connection connection = initializeDataBaseConnection();
        Statement statement = null;
        List<Book> books = new ArrayList<>();

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetAllBook);
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPagesSum(resultSet.getInt("pages_sum"));
                book.setYearPublished(resultSet.getInt("year_published"));
                book.setPublishingHouse(resultSet.getString("publishing_house"));

                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, statement);
        }


        return books;
    }


    public void addBook(Book book) {
        final String sqlInsertBook = "INSERT INTO books " +
                "(title, author, pages_sum, year_published, publishing_house)" +
                "VALUES (?, ?, ?, ?, ?);";

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sqlInsertBook);

            //System.out.println(preparedStatement.getMetaData());
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPagesSum());
            preparedStatement.setInt(4, book.getYearPublished());
            preparedStatement.setString(5, book.getPublishingHouse());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }

    }
}