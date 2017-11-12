class Address
{
    long customer_id;
    String street_and_building;
    String city;
    String country;
    String zip;
    String phone;

    Address(long customer_id,
            String street_and_building,
            String city,
            String country,
            String zip,
            String phone
    )
    {
        this.customer_id = customer_id;
        this.street_and_building = street_and_building;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.phone = phone;
    }

    static Exception generateException()
    {
        try {
            throw new RuntimeException();
        }
        catch (Exception e)

        {
            return e;
        }
    }
}