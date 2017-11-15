import java.sql.*;
import java.util.Properties;

class DBclient
{
    Connection connection;

    DBclient (Connection connection)
    {
        this.connection = connection;
    }

    public static DBclient getClient(String url, String user, String pass)
    {
        try {
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", pass);
////            props.setProperty("ssl","true");
            return new DBclient(DriverManager.getConnection(url, props));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void checkConnection()
    {
        try {
            System.out.println(connection.isValid(1000));
        } catch (SQLException e) {
            System.out.println("DB is unavailable");
            e.printStackTrace();
        }
    }

    int insertEmail(Email email) throws SQLException
    {
//        Script example:
//        INSERT INTO public.email(
//            customer_id, email, created_when)
//        VALUES (?, ?);
        String query = "insert into public.email(customer_id, email, created_when) " +
                "VALUES (" +
                email.customer_id + "," +
                addQuotes(email.email) + "," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(email.created_when.getTime()));
        return statement.executeUpdate();
    }

    int insertCustomer(Customer customer) throws SQLException
    {
//        Script example:
//        INSERT INTO public.customer(
//        customer_id, name, surname, created_when)
//        VALUES (?, ?, ?, ?);
        String query = "insert into public.customer(customer_id, name, surname, created_when) " +
                "VALUES (" +
                customer.customer_id + "," +
                addQuotes(customer.name) + "," +
                addQuotes(customer.surname) + "," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(customer.created_when.getTime()));
        return statement.executeUpdate();
    }

    int insertAddress(Address address) throws SQLException
    {
//        Script example:
//        INSERT INTO public.address(
//            customer_id, street_and_building, building, city, country, zip, phone, created_when)
//        VALUES (?, ?, ?, ?, ?, ?, ?);
        String query = "insert into public.address(customer_id, street_and_building, city, country, zip, phone, created_when) " +
                "VALUES (" +
                address.customer_id + "," +
                addQuotes(address.street_and_building) + "," +
                addQuotes(address.city) + "," +
                addQuotes(address.country) + "," +
                addQuotes(address.zip) + "," +
                addQuotes(address.phone) + "," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(address.created_when.getTime()));
        return statement.executeUpdate();
    }

    void insertOrders(Order[] orders) throws SQLException
    {
        for (Order order: orders)
        {
            System.out.println("Inserting order #" + order.order_id);
            int insertResult = insertOrder(order);
            System.out.println(insertResult + " rows are affected");
            System.out.println("Inserting order items");
            insertOrderItems(order.order_items);
            System.out.println(insertResult + " rows are affected");
            System.out.println("Inserting errors");
            insertErrors(order.errors);
        }
    }

    int insertOrder(Order order) throws SQLException
    {
//        Script example:
//        INSERT INTO public.orders(
//            order_id, customer_id, status_id, created_when, modified_when)
//        VALUES (?, ?, ?, ?, ?);
        String query = "insert into public.orders(order_id, customer_id, status_id, created_when, modified_when) " +
                "VALUES (" +
                order.order_id + "," +
                order.customer_id + "," +
                order.status_id + "," +
                "?," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(order.created_when.getTime()));
        statement.setDate(2, new java.sql.Date(order.modified_when.getTime()));
        return statement.executeUpdate();
    }

    void insertOrderItems(OrderItem[] orderItems) throws SQLException
    {
        for (OrderItem orderItem: orderItems)
        {
            System.out.println("Inserting orderItem #" + orderItem.item_id);
            int insertResult = insertOrderItem(orderItem);
            System.out.println(insertResult + " rows are affected");
        }
    }

    int insertOrderItem(OrderItem orderItem) throws SQLException
    {
//        Script example:
//        INSERT INTO public.order_item(
//            order_id, item_id, quantity, created_when)
//        VALUES (?, ?, ?);
        String query = "insert into public.order_item(order_id, item_id, quantity, created_when) " +
                "VALUES (" +
                orderItem.order_id + "," +
                orderItem.item_id + "," +
                orderItem.quantity + "," +
                "?);";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(orderItem.created_when.getTime()));
        return statement.executeUpdate();
    }

    void insertErrors(Error[] errors) throws SQLException
    {
        for (Error error: errors)
        {
            System.out.println("Inserting error #" + error.error_id);
            int insertResult = insertError(error);
            System.out.println(insertResult + " rows are affected");
        }
    }

    int insertError(Error error) throws SQLException
    {
//        Script example:
//        INSERT INTO public.error(
//            created_when, order_id, stack_trace, error_id, class, exception)
//        VALUES (?, ?, ?, ?, ?, ?);

        String query = "insert into public.error(created_when, order_id, stack_trace, error_id, class, exception) " +
                "VALUES (" +
                "?," +
                error.order_id + "," +
                addQuotes(error.stack_trace) + "," +
                error.error_id + "," +
                addQuotes(error.class_name) + "," +
                addQuotes(error.exception) +
                ");";
        System.out.println("Query to execute:");
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(error.created_when.getTime()));
        return statement.executeUpdate();
    }

    long selectMaxCustomerId() throws SQLException
    {
        String query = "select MAX(customer_id) from customer;";

        ResultSet result = connection.createStatement().executeQuery(query);
        if (result.next())
        {
            return result.getLong(1);
        }
        return 0;
    }

    long selectMaxOrderId() throws SQLException
    {
        String query = "select MAX(order_id) from orders;";

        ResultSet result = connection.createStatement().executeQuery(query);
        if (result.next())
        {
            return result.getLong(1);
        }
        return 0;
    }

    long selectMaxItemId() throws SQLException
    {
        String query = "select MAX(item_id) from warehouse_item;";

        ResultSet result = connection.createStatement().executeQuery(query);
        if (result.next())
        {
            return result.getLong(1);
        }
        return 0;
    }

    int selectMaxStatusId() throws SQLException
    {
        String query = "select MAX(status_id) from order_status;";
        ResultSet result = connection.createStatement().executeQuery(query);
        if (result.next())
        {
            return result.getInt(1);
        }
        return 0;
    }

    int selectMaxErrorId() throws SQLException
    {
        String query = "select MAX(error_id) from error;";
        ResultSet result = connection.createStatement().executeQuery(query);
        if (result.next())
        {
            return result.getInt(1);
        }
        return 0;
    }

    String addQuotes(String s)
    {
        return "'" + s + "'";
    }

    static Exception generateException()
    {
        try {
            throw new SQLException();
        }
        catch (Exception e)

        {
            return e;
        }
    }
}
