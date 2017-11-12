import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class OrderItem
{
    static final long MIN_ITEM_ID = 1000000001;
    static final int MAX_QUANTITY = 3;
    static final int MIN_QUANTITY = 1;
    static final long INITIAL_ID = 30000000;

    long order_id;
    long item_id;
    int quantity;

    private OrderItem(long order_id,
                    long item_id,
                    int quantity
            )
    {
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
    }

    static OrderItem generateOrderItem(long order_id, DBclient client)
    {
        System.out.println("Getting a max item id");
        try {
            long maxItemId = client.selectMaxItemId();
            if (maxItemId == 0)
            {
                maxItemId = INITIAL_ID;
            }
            System.out.println("Max item id  = " + maxItemId);
            System.out.println("Randomizing an item id");
            long itemId = Random.genRandomLong(MIN_ITEM_ID, maxItemId);
            int quantity = Random.genRandomInt(MIN_QUANTITY, MAX_QUANTITY);
            return new OrderItem(order_id, itemId, quantity);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Exception generateException()
    {
        try {
            throw new ClassNotFoundException();
        }
        catch (Exception e)
        {
            return e;
        }
    }
}
