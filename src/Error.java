import java.sql.SQLException;
import java.util.Date;

class Error
{
    Date created_when;
    long order_id;
    String stack_trace;
    int error_id;
    String class_name;
    String exception;

    private Error(Date created_when,
                long order_id,
                String stack_trace,
                int error_id,
                String class_name,
                String exception)
    {
        this.created_when = created_when;
        this.order_id = order_id;
        this.stack_trace = stack_trace;
        this.error_id = error_id;
        this.class_name = class_name;
        this.exception = exception;
    }

    static Error generateError(DBclient client, long order_id, int idShift, Date order_created_when)
    {
        try {
            int maxErrorId = client.selectMaxErrorId();
            int errorId = maxErrorId + 1 + idShift;
            Exception exception = generateStacktrace();
            String stack_trace = "";
            for (StackTraceElement element: exception.getStackTrace()) // todo: null exception
            {
                stack_trace += "at " + element.toString() + "\n";
            }
            String class_name = exception.getStackTrace()[0].getClassName();
            String exception_name = exception.toString();

            return new Error(order_created_when,
                            order_id,
                            stack_trace,
                            errorId,
                            class_name,
                            exception_name
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Exception generateStacktrace()
    {
        int i = Random.genRandomInt(0,5);
        switch (i)
        {
            case 0: return DBclient.generateException();
            case 1: return Order.generateException();
            case 2: return OrderItem.generateException();
            case 3: return Email.generateException();
            case 4: return Address.generateException();
            case 5: return Customer.generateException();
        }
        return null;
    }
}
