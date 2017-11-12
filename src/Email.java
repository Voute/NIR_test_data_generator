class Email
{
    long customer_id;
    String email;

    Email(long customer_id,
          String email)
    {
        this.customer_id = customer_id;
        this.email = email;
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