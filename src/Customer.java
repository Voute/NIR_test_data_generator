import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

class Customer
{
    static final long INITIAL_ID = 2000000001;
    static final long CUSTOMER_ID_MASK = INITIAL_ID;
    static final int MAX_ORDERS = 10;
    static final int MIN_ORDERS = 10;

    long customer_id;
    String name;
    String surname;
    Address address;
    Email email;
    Date created_when;
    Order[] orders;

    private Customer(long customer_id,
             String name,
             String surname,
             Address address,
             Email email,
             Date created_when,
             Order[] orders
    )
    {
        this.customer_id = customer_id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.created_when = created_when;
        this.orders = orders;
    }

    static Customer nextSampleCustomer(DBclient client)
    {
        System.out.println("Getting a max customer id");

        long maxCustomerId = 0;
        try {
            maxCustomerId = client.selectMaxCustomerId();
            if (maxCustomerId == 0)
            {
                maxCustomerId = INITIAL_ID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("Max customer id = " + maxCustomerId);

        System.out.println("Opening sample data file");
        File sampleDataFile = new File("C:/Users/Admvoute/IdeaProjects/NIR_test_data_generator/src/sample_data.csv");
        BufferedReader reader = null;

        int lineToRead = (int)(maxCustomerId - CUSTOMER_ID_MASK + 1);

        try {
            reader = new BufferedReader(new FileReader(sampleDataFile));
            System.out.println("Reading a line number = " + lineToRead);
            String line;

            int n = 0;
            while ((line = reader.readLine()) != null && n < lineToRead) {
                n++;
            }

            System.out.println("Customer is found");
            String[] customerFields = line.split(";");  //todo: null exception

//                  CSV map: "first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"
            System.out.println("[name: " + customerFields[0] + ", lastname: " + customerFields[1] + " ... ]");

            long customer_id = CUSTOMER_ID_MASK + lineToRead;

            System.out.println("Creating  an address");
            Address address = new Address(customer_id,
                    customerFields[3],
                    customerFields[4],
                    customerFields[6],
                    customerFields[7],
                    customerFields[8]
            );
//                System.out.println("Address is " + address.toString());
            System.out.println("Creating a email");
            Email email = new Email(customer_id, customerFields[10]);
//                System.out.println("Email is " + email.toString());
            Date created_when = Random.genRandomDate(new GregorianCalendar(2017, 0, 1).getTime(),
                                new GregorianCalendar(2017, 11, 31).getTime());
            Order[] orders = generateOrders(client, customer_id, created_when);

            return new Customer(customer_id,
                    customerFields[0],
                    customerFields[1],
                    address,
                    email,
                    created_when,
                    orders
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static Order[] generateOrders(DBclient client, long customer_id, Date customer_created_when)
    {
        int itemsSize = Random.genRandomInt(MIN_ORDERS, MAX_ORDERS);
        Order[] result = new Order[itemsSize];
        for (int i = 0; i < itemsSize; i++)
        {
            result[i] = Order.generateOrder(customer_id, customer_created_when, client, i);

        }
        return result;
    }

    static Exception generateException()
    {
        try {
            throw new IllegalArgumentException();
        }
        catch (Exception e)

        {
            return e;
        }
    }
}