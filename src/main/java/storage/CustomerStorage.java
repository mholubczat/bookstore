package storage;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerStorage {

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
}