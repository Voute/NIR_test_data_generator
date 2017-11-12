import java.util.Date;

final class Random {

    static long genRandomLong(long min, long max)
    {
        return (long)( (Math.random() * (max - min)) + min);
    }

    static int genRandomInt(int min, int max)
    {
        return (int)( (Math.random() * (max - min)) + min);
    }

    static Date genRandomDate(Date minDate, Date maxDate)
    {
        long max = maxDate.getTime();
        long min = minDate.getTime();
        return new Date( (long)( (Math.random() * ( max - min )) + min));
    }

}
