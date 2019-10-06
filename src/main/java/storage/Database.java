package storage;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import models.Book;
import models.Customer;
import models.Order;
import models.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

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


    public static void createDatabase() {

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


    public Book getBookFromDB(long id) {
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
                book.setBookId(resultSet.getLong("book_id"));
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
                book.setBookId(resultSet.getLong("book_id"));
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

    public Customer getCustomer(long customerId) {
        final String sqlSelectCustomer = "SELECT * FROM customers WHERE customer_id = ?;";


        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        Statement statement = null;

        try {

            preparedStatement = connection.prepareStatement(sqlSelectCustomer);
            preparedStatement.setLong(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setName(resultSet.getString("name"));
                customer.setCustomerId(customerId);

                return customer;
            }
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }
        return null;
    }


    public List<Customer> getAllCustomers() {
        final String sqlGetAllCustomers = "SELECT * FROM customers;";

        Connection connection = initializeDataBaseConnection();
        Statement statement = null;
        List<Customer> customers = new ArrayList<>();

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetAllCustomers);
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getLong("customer_id"));
                customer.setName(resultSet.getString("name"));


                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, statement);
        }


        return customers;
    }


    public void addCustomer(Customer customer) {
        final String sqlInsertCustomer = "INSERT INTO customers " +
                "(name)" +
                "VALUES (?);";

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sqlInsertCustomer);

            //System.out.println(preparedStatement.getMetaData());
            preparedStatement.setString(1, customer.getName());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }

    }

    public Order getOrder(long orderId) {
        final String sqlSelectOrder = "SELECT * FROM orders WHERE order_id = ?;";
        final String sqlGetOrderItems = "SELECT*FROM order_items WHERE order_id = ?";

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatementOrder = null;
        PreparedStatement preparedStatementItems = null;
        try {

            preparedStatementOrder = connection.prepareStatement(sqlSelectOrder);
            preparedStatementOrder.setLong(1, orderId);

            ResultSet resultSet = preparedStatementOrder.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(orderId);
                order.setOrderDate(resultSet.getDate("order_date"));
                order.setCustomer(getCustomer(resultSet.getInt("customer_id")));
                order.setOrderItems(getOrderItems(orderId));
                return order;
            }
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatementOrder);
        }
        return null;
    }

    public List<Order> getAllOrders() {
        final String sqlGetAllOrders = "SELECT*FROM orders";



        Connection connection = initializeDataBaseConnection();
        Statement statement = null;

        List<Order> orders = new ArrayList<>();

        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlGetAllOrders);
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getLong("order_id"));

                order.setCustomer(getCustomer(resultSet.getLong("customer_id")));
                order.setOrderDate(resultSet.getDate("order_date"));



                order.setOrderItems(getOrderItems(resultSet.getLong("order_id")));
                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, statement);
        }
        return orders;
    }

    public void addOrder(Order order) {
        final String sqlInsertOrder = "INSERT INTO orders " +
                "(customer_id, order_date)" +
                "VALUES (?,?);";


        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatementOrder = null;


        try {
            preparedStatementOrder = connection.prepareStatement(sqlInsertOrder);
            //System.out.println(preparedStatement.getMetaData());
            preparedStatementOrder.setLong(1, order.getCustomer().getCustomerId());
            preparedStatementOrder.setDate(2, order.getOrderDate());
            preparedStatementOrder.executeUpdate();


        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatementOrder);

        }
    }

    public void addOrderItem(long orderId, OrderItem orderItem) {

        final String sqlInsertOrderItems = "INSERT INTO order_items " +
                "(order_id, book_id, quantity)" +
                "VALUES (?,?,?);";

        Connection connection = initializeDataBaseConnection();

        PreparedStatement preparedStatementOrderItems = null;

        try {

            preparedStatementOrderItems = connection.prepareStatement(sqlInsertOrderItems);
            preparedStatementOrderItems.setLong(1, orderId);
            preparedStatementOrderItems.setLong(2, orderItem.getBook().getBookId());
            preparedStatementOrderItems.setInt(3, orderItem.getQuantity());
            preparedStatementOrderItems.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {

            closeDatabaseResources(connection, preparedStatementOrderItems);
        }

    }
    public ArrayList<OrderItem> getOrderItems(long order_id) {
        final String sqlGetOrderItems = "SELECT*FROM order_items WHERE order_id = ?";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sqlGetOrderItems);
                preparedStatement.setLong(1, order_id);

                ResultSet orderItemSet = preparedStatement.executeQuery();
                while (orderItemSet.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(orderItemSet.getLong("order_item_id"));
                    item.setQuantity(orderItemSet.getInt("quantity"));
                    item.setBook(getBookFromDB(orderItemSet.getLong("book_id")));
                    orderItems.add(item);
                }
        } catch (SQLException e) {
            System.err.println("Error invoking sql query: \n" + e.getMessage());
            throw new RuntimeException("Error invoking sql query");
        } finally {
            closeDatabaseResources(connection, preparedStatement);
        }
        return orderItems;
    }
}
