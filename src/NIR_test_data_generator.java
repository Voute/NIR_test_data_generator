import java.io.*;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class NIR_test_data_generator implements Runnable
{

    static final int ITEMS_TO_GENERATE = 10;
    static final String DB_URL = "jdbc:postgresql://localhost:5432/kis_task6";
    static final String DB_USER = "postgres";
    static final String DB_PASS = "postgres";


    final HashMap<Integer,String> ITEM_TYPES = new HashMap<Integer,String>() {{
        put(0, "customer");
        put(1, "order");
        put(2, "reservation");
        put(4, "error");
    }};

    Connection db_con;

    public static void main (String ... args)
    {
        System.out.println("Creating a generator");
        NIR_test_data_generator generator = new NIR_test_data_generator();

        generator.run();


//
//        ResultSet result = conn.createStatement().executeQuery("select * from customer");
    }

    NIR_test_data_generator()
    {
        try {
            System.out.println("Creating a DB connection");
            db_con = getDBConnection();
            System.out.println("Checking a connection");
            System.out.println(db_con.isValid(1000));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        for (int i = 0; i < ITEMS_TO_GENERATE; i++)
        {
            System.out.println("Creating a Customer");
            Customer customer = Customer.nextSampleCustomer(db_con);

            if (customer != null)
            {
                try {
//                    System.out.println("Customer is " + customer.toString());
                    int insertResult = 0;
                    insertResult = insertNewCustomer(customer, db_con);
                    System.out.println(insertResult + " rows are affected");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Connection getDBConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASS);
////            props.setProperty("ssl","true");
        return DriverManager.getConnection(DB_URL, props);

    }

    int insertNewCustomer(Customer customer, Connection conn) throws SQLException
    {
//        INSERT INTO public.customer(
//        customer_id, name, surname, created_when)
//        VALUES (?, ?, ?, ?);
        System.out.println("Inserting a customer");
        String query = "insert into public.customer(customer_id, name, surname, created_when) " +
                "VALUES (" +
                "" + customer.customer_id + "," +
                customer.name.replace("\"", "'") + "," +
                customer.surname.replace("\"", "'") + "," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(customer.created_when.getTime()));
        return statement.executeUpdate();
    }

    class Order
    {
        long order_id;
        long customer_id;
        long status_id;
        Date created_when;
        Date modified_when;
    }
    class OrderItem
    {
        long order_id;
        long item_id;
        int quantity;
    }
    class Reservation
    {
        long order_id;
        long item_id;
        int quantity;
        Date reserved_when;
    }
}
