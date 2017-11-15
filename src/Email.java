import java.util.Date;

class Email
{
    long customer_id;
    String email;
    Date created_when;

    Email(long customer_id,
          String email,
          Date created_when)
    {
        this.customer_id = customer_id;
        this.email = email;
        this.created_when = created_when;
    }

    static Exception generateException()
    {
        try {
            throw new IndexOutOfBoundsException();
        }
        catch (Exception e)

        {
            return e;
        }
    }
}