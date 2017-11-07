import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

class Customer
{
    static final long CUSTOMER_ID_MASK = 2000000001l;
    static final int CUSTOMER_MAX = 501;

    long customer_id;
    String name;
    String surname;
    Address address;
    Email email;
    Date created_when;

    Customer(long customer_id,
             String name,
             String surname,
             Address address,
             Email email,
             Date created_when
    )
    {
        this.customer_id = customer_id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.created_when = created_when;
    }

    Customer(Connection dbConn)
    {

    }

    static Customer nextSampleCustomer(Connection dbConn)
    {
        System.out.println("Getting max customer id");
        long maxCustomerId = selectMaxCustomerId(dbConn);
        System.out.println("Max customer id = " + maxCustomerId);

        if (maxCustomerId >= CUSTOMER_ID_MASK && maxCustomerId < (long)(CUSTOMER_MAX + CUSTOMER_ID_MASK)) {
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
                String[] customerFields = line.split(",");

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

                return new Customer(customer_id,
                        customerFields[0],
                        customerFields[1],
                        address,
                        email,
                        Random.genRandomDate(new GregorianCalendar(2017, 0, 1).getTime(),
                                new GregorianCalendar(2017, 11, 31).getTime())
                );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    static long selectMaxCustomerId(Connection dbConn)
    {
        String query = "select MAX(customer_id) from customer;";

        try {
            ResultSet result = dbConn.createStatement().executeQuery(query);
            if (result.next())
            {
                return result.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


}