import java.sql.*;

public class NIR_test_data_generator implements Runnable
{

    static final int ITEMS_TO_GENERATE = 500;
    static final String DB_URL = "jdbc:postgresql://localhost:5432/kis_task6";
    static final String DB_USER = "postgres";
    static final String DB_PASS = "postgres";

    DBclient client;

    public static void main (String ... args)
    {
        System.out.println("Creating a generator");
        NIR_test_data_generator generator = new NIR_test_data_generator();

        generator.run();

    }

    NIR_test_data_generator()
    {
        System.out.println("Creating a DBclient");
        client = DBclient.getClient(DB_URL, DB_USER, DB_PASS);
        System.out.println("Checking the connection");
        client.checkConnection();
    }

    @Override
    public void run()
    {
        for (int i = 0; i < ITEMS_TO_GENERATE; i++)
        {
            System.out.println("Creating a Customer");
            Customer customer = Customer.nextSampleCustomer(client);

            if (customer != null)
            {
                try {
                    System.out.println("Inserting a customer");
                    int insertResult = 0;
                    insertResult = client.insertCustomer(customer);
                    System.out.println(insertResult + " rows are affected");
                    System.out.println("Inserting an address");
                    insertResult = client.insertAddress(customer.address);
                    System.out.println(insertResult + " rows are affected");
                    System.out.println("Inserting an email");
                    insertResult = client.insertEmail(customer.email);
                    System.out.println(insertResult + " rows are affected");
                    System.out.println("Inserting customer orders");
                    client.insertOrders(customer.orders);
                    System.out.println(insertResult + " rows are affected");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
