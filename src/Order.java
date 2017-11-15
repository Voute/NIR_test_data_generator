import java.sql.SQLException;
import java.util.Date;

class Order
{
    static final long MAX_DIFF_CREATED_WITH_CUST_CREATED_WHEN_MILLIS = 604800000; // 7 days
    static final long MAX_DIFF_MODIFIED_WITH_CREATED_WHEN_MILLIS = 1209600000; // 14 days
    static final int MIN_STATUS_ID = 30001;
    static final int MAX_ORDER_ITEMS = 10;
    static final int MIN_ORDER_ITEMS = 1;
    static final int MAX_ERRORS = 6;
    static final int MIN_ERRORS = 0;
    static final long INITIAL_ID = 30000000;

    long order_id;
    long customer_id;
    long status_id;
    Date created_when;
    Date modified_when;
    OrderItem[] order_items;
    Error[] errors;

    private Order(long order_id,
            long customer_id,
            long status_id,
            Date created_when,
            Date modified_when,
            OrderItem[] order_items,
            Error[] errors
    )
    {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.status_id = status_id;
        this.created_when = created_when;
        this.modified_when = modified_when;
        this.order_items = order_items;
        this.errors = errors;
    }

    static Order generateOrder(long customer_id, Date customer_created_when, DBclient client, int idShift)
    {
        System.out.println("Getting a max order id");
        try {
            long maxOrderId = client.selectMaxOrderId();
            if (maxOrderId == 0 )
            {
                maxOrderId = INITIAL_ID;
            }
            System.out.println("Max order id = " + maxOrderId);
            long orderId = maxOrderId + 1 + idShift;
            System.out.println("Randomizing a status");
            int status = randomizeStatus(client);
            System.out.println("Status = " + status);
            System.out.println("Randomizing a created_when date");

            Date max_creation_date = new Date(customer_created_when.getTime() + MAX_DIFF_CREATED_WITH_CUST_CREATED_WHEN_MILLIS);
            if (max_creation_date.getTime() > NIR_test_data_generator.current_date.getTime())
            {
                max_creation_date = NIR_test_data_generator.current_date;
            }

            Date created_when = Random.genRandomDate(customer_created_when, max_creation_date);
            System.out.println("Created_when = " + created_when.toString());
            System.out.println("Randomizing a modified_when date");

            Date max_modification_date = new Date(customer_created_when.getTime() + MAX_DIFF_MODIFIED_WITH_CREATED_WHEN_MILLIS);
            if (max_modification_date.getTime() > NIR_test_data_generator.current_date.getTime())
            {
                max_modification_date = NIR_test_data_generator.current_date;
            }

            Date modified_when = Random.genRandomDate(created_when, max_modification_date);
            System.out.println("Modified_when = " + modified_when.toString());
            System.out.println("Generating order items");
            OrderItem[] items = generateOrderItems(orderId, client, created_when);
            Error[] errors = generateErrors(client, orderId, created_when);

            return new Order(orderId,
                    customer_id,
                    status,
                    created_when,
                    modified_when,
                    items,
                    errors
            );
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    static OrderItem[] generateOrderItems(long order_id, DBclient client, Date order_created_when)
    {
        int itemsSize = Random.genRandomInt(MIN_ORDER_ITEMS, MAX_ORDER_ITEMS);
        OrderItem[] result = new OrderItem[itemsSize];
        for (int i = 0; i < itemsSize; i++)
        {
            result[i] = OrderItem.generateOrderItem(order_id, client, order_created_when);
        }
        return result;
    }

    static Error[] generateErrors(DBclient client, long order_id, Date order_created_when)
    {
        int itemsSize = Random.genRandomInt(MIN_ERRORS, MAX_ERRORS);
        Error[] result = new Error[itemsSize];
        for (int i = 0; i < itemsSize; i++)
        {
            result[i] = Error.generateError(client, order_id, i, order_created_when);
        }
        return result;
    }

    static int randomizeStatus(DBclient client) throws SQLException
    {
        String query = "select MAX(status_id) from order_status;";
        int maxStatusId = client.selectMaxStatusId();  // todo: zero exception
        return Random.genRandomInt(MIN_STATUS_ID, maxStatusId);
    }

    static Exception generateException()
    {
        try {
            throw new NullPointerException();
        }
        catch (Exception e)

        {
            return e;
        }
    }
}