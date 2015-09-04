package org.quinto.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Prime numbers sequence generator.
 * @param <T> output data type (Integer, Long, BigInteger).
 */
public class PrimesIterable< T extends Number > implements Iterable< T >
{
    /**
     * The largest prime number in long datatype.
     * Note: the largest prime number in int datatype is {@link java.lang.Integer#MAX_VALUE}.
     * @see java.lang.Integer#MAX_VALUE
     */
    public static final long LAST_LONG_PRIME = 9223372036854775783L;
    
    private static final int INDICES[] = { 1, 7, 11, 13, 17, 19, 23, 29 };
    private static final int CYCLE_LENGTH = 30;
    private static final int CACHE[] = new int[ 512 ];
    private static final BigInteger BI_LAST_LONG_PRIME = BigInteger.valueOf( LAST_LONG_PRIME );
    private static final BigInteger CACHE_LENGTH = BigInteger.valueOf( CACHE.length );
    private static boolean initialized;
    
    private T current;
    private T next;
    private T max;
    private T currentPos;
    private T quantity;
    private PrimesIterableType type;
    private PrimesIterator iterator;
    
    static
    {
        int i = 0;
        for ( int p : getIntegerTotally( CACHE.length ) ) CACHE[ i++ ] = p;
        initialized = true;
    }
    
    private static enum PrimesIterableType
    {
        BIG_INTEGER,
        INTEGER,
        LONG
    }
    
    /**
     * Generate a sequence of BigInteger prime numbers.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>The resulting sequence would contain exactly {@code quantity} numbers.
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( BigInteger bi : getBigIntegerTotally( BigInteger.valueOf( 5 ) ) ) System.out.println( bi );</code><br>
     * The output of this example would contain 5 numbers: 2, 3, 5, 7, 11.
     * @param quantity a quantity of numbers in the resulting sequence
     * @return a sequence of prime numbers
     * @throws IllegalArgumentException if quantity is null
     */
    public static PrimesIterable< BigInteger > getBigIntegerTotally( BigInteger quantity ) throws IllegalArgumentException
    {
        if ( quantity == null ) throw new IllegalArgumentException( "Quantity cannot be null" );
        return new PrimesIterable<>( null, quantity, PrimesIterableType.BIG_INTEGER );
    }
    
    /**
     * Generate a sequence of int prime numbers.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>The resulting sequence would contain exactly {@code quantity} numbers.
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( int i : getIntegerTotally( 5 ) ) System.out.println( i );</code><br>
     * The output of this example would contain 5 numbers: 2, 3, 5, 7, 11.
     * @param quantity a quantity of numbers in the resulting sequence
     * @return a sequence of prime numbers
     */
    public static PrimesIterable< Integer > getIntegerTotally( int quantity )
    {
        return new PrimesIterable<>( null, quantity, PrimesIterableType.INTEGER );
    }
    
    /**
     * Generate a sequence of long prime numbers.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>The resulting sequence would contain exactly {@code quantity} numbers.
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( long l : getLongTotally( 5L ) ) System.out.println( l );</code><br>
     * The output of this example would contain 5 numbers: 2, 3, 5, 7, 11.
     * @param quantity a quantity of numbers in the resulting sequence
     * @return a sequence of prime numbers
     */
    public static PrimesIterable< Long > getLongTotally( long quantity )
    {
        return new PrimesIterable<>( null, quantity, PrimesIterableType.LONG );
    }
    
    /**
     * Generate a sequence of BigInteger prime numbers up to {@code max} value inclusively.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>All the numbers in a resulting sequence would lie in range [ 2 .. {@code max} ] (both sides included).
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( BigInteger bi : getBigIntegerMax( BigInteger.valueOf( 5 ) ) ) System.out.println( bi );</code><br>
     * The output of this example would contain 3 numbers: 2, 3, 5.
     * @param max an upper inclusive bound of the sequence
     * @return a sequence of prime numbers
     * @throws IllegalArgumentException if max is null
     */
    public static PrimesIterable< BigInteger > getBigIntegerMax( BigInteger max ) throws IllegalArgumentException
    {
        if ( max == null ) throw new IllegalArgumentException( "Max cannot be null" );
        return new PrimesIterable<>( max, null, PrimesIterableType.BIG_INTEGER );
    }
    
    /**
     * Generate a sequence of int prime numbers up to {@code max} value inclusively.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>All the numbers in a resulting sequence would lie in range [ 2 .. {@code max} ] (both sides included).
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( int i : getIntegerMax( 5 ) ) System.out.println( i );</code><br>
     * The output of this example would contain 3 numbers: 2, 3, 5.
     * @param max an upper inclusive bound of the sequence
     * @return a sequence of prime numbers
     */
    public static PrimesIterable< Integer > getIntegerMax( int max )
    {
        return new PrimesIterable<>( max, null, PrimesIterableType.INTEGER );
    }
    
    /**
     * Generate a sequence of long prime numbers up to {@code max} value inclusively.
     * <p>A sequence is generated lazily (next number is calculated only if required).
     * <p>All the numbers in a resulting sequence would lie in range [ 2 .. {@code max} ] (both sides included).
     * <p>The result can be enumerated in a for-loop construction, e.g.:<br>
     * <code>for ( long l : getLongMax( 5L ) ) System.out.println( l );</code><br>
     * The output of this example would contain 3 numbers: 2, 3, 5.
     * @param max an upper inclusive bound of the sequence
     * @return a sequence of prime numbers
     */
    public static PrimesIterable< Long > getLongMax( long max )
    {
        return new PrimesIterable<>( max, null, PrimesIterableType.LONG );
    }
    
    private PrimesIterable( T max, T quantity, PrimesIterableType type )
    {
        this.max = max;
        this.quantity = quantity;
        this.type = type;
        iterator = new PrimesIterator();
        switch ( type )
        {
            case BIG_INTEGER:
                current = ( T )BigInteger.ONE;
                next = ( T )BigUtils.BI_TWO;
                currentPos = ( T )BigInteger.ZERO;
                break;
            case INTEGER:
                current = ( T )Integer.valueOf( 1 );
                next = ( T )Integer.valueOf( 2 );
                currentPos = ( T )Integer.valueOf( 0 );
                break;
            case LONG:
                current = ( T )Long.valueOf( 1L );
                next = ( T )Long.valueOf( 2L );
                currentPos = ( T )Long.valueOf( 0L );
                break;
        }
    }
    
    @Override
    public Iterator< T > iterator()
    {
        return iterator;
    }
    
    private class PrimesIterator implements Iterator< T >
    {
        @Override
        public boolean hasNext()
        {
            if ( max != null )
            {
                switch ( type )
                {
                    case BIG_INTEGER:
                        if ( ( ( BigInteger )next ).compareTo( ( BigInteger )max ) > 0 ) return false;
                        break;
                    case INTEGER:
                        if ( ( Integer )next > ( Integer )max ) return false;
                        break;
                    case LONG:
                        if ( ( Long )next > ( Long )max ) return false;
                        break;
                }
            }
            if ( quantity != null )
            {
                switch ( type )
                {
                    case BIG_INTEGER:
                        if ( ( ( BigInteger )currentPos ).compareTo( ( BigInteger )quantity ) >= 0 ) return false;
                        break;
                    case INTEGER:
                        if ( ( Integer )currentPos >= ( Integer )quantity ) return false;
                        break;
                    case LONG:
                        if ( ( Long )currentPos >= ( Long )quantity ) return false;
                        break;
                }
            }
            return true;
        }

        @Override
        public T next()
        {
            if ( !hasNext() ) throw new NoSuchElementException();
            switch ( type )
            {
                case BIG_INTEGER:
                    current = next;
                    currentPos = ( T )( ( BigInteger )currentPos ).add( BigInteger.ONE );
                    if ( initialized && ( ( BigInteger )currentPos ).compareTo( CACHE_LENGTH ) < 0 ) next = ( T )BigInteger.valueOf( CACHE[ currentPos.intValue() ] );
                    else next = ( T )getNext( ( ( BigInteger )next ).add( BigInteger.ONE ) );
                    return current;
                case INTEGER:
                    current = next;
                    currentPos = ( T )Integer.valueOf( ( Integer )currentPos + 1 );
                    if ( initialized && ( Integer )currentPos < CACHE.length ) next = ( T )Integer.valueOf( CACHE[ currentPos.intValue() ] );
                    else next = ( T )Integer.valueOf( getNext( ( Integer )next + 1 ) );
                    return current;
                case LONG:
                    current = next;
                    currentPos = ( T )Long.valueOf( ( Long )currentPos + 1L );
                    if ( initialized && ( Long )currentPos < CACHE.length ) next = ( T )Long.valueOf( CACHE[ currentPos.intValue() ] );
                    else next = ( T )Long.valueOf( getNext( ( Long )next + 1L ) );
                    return current;
                default:
                    throw new NoSuchElementException();
            }
        }

        @Override
        public void remove()
        {
        }
    }
    
    /**
     * Seeking of the next prime number that is greater than or equal to n.<br>
     * Examples: getNext( 7 ) = 7, getNext( 9 ) = 11.
     * <p>For negative n, -getNext( -n ) is returned.
     * <p>The value of n cannot be too large or too small to overflow or underflow the search.
     * Integer.MAX_VALUE is prime, so getNext( Integer.MAX_VALUE ) = Integer.MAX_VALUE.
     * The only exclusion is Integer.MIN_VALUE which would return the largest prime number
     * in integer datatype, that is getNext( Integer.MIN_VALUE ) = Integer.MAX_VALUE.
     * @author Howard Hinnant
     * http://stackoverflow.com/questions/4475996/given-prime-number-n-compute-the-next-prime
     * @param n lower inclusive limit of the searching prime number
     * @return minimal prime number p such that p &ge; n
     */
    public static int getNext( int n )
    {
        if ( n < 6 )
        {
            if ( n < 0 )
            {
                // Integer.MIN_VALUE cannot be negated.
                if ( n == Integer.MIN_VALUE ) return Integer.MAX_VALUE;
                return -getNext( -n );
            }
            switch ( n )
            {
                case 0:
                case 1:
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                case 5:
                    return 5;
            }
        }
        int k0 = n / CYCLE_LENGTH;
        int in = Arrays.binarySearch( INDICES, n - k0 * CYCLE_LENGTH );
        if ( in < 0 ) in = -1 - in;
        n = k0 * CYCLE_LENGTH + INDICES[ in ];
        while ( !PrimeUtils.isPrime( n ) )
        {
            in++;
            if ( in == INDICES.length )
            {
                k0++;
                in = 0;
            }
            n = k0 * CYCLE_LENGTH + INDICES[ in ];
        }
        return n;
    }
    
    /**
     * Seeking of the next prime number that is greater than or equal to n.<br>
     * Examples: getNext( 7 ) = 7, getNext( 9 ) = 11.
     * <p>For negative n, -getNext( -n ) is returned.
     * <p>If n is too large (n &gt; {@link #LAST_LONG_PRIME}) then the search overflows
     * and the negative prime number is returned. Its absolute value would be the largest
     * possible to represent in long datatype.
     * <p>If n is negative and its absolute value is too small then the search underflows
     * and the largest long prime number is returned.
     * @author Howard Hinnant
     * http://stackoverflow.com/questions/4475996/given-prime-number-n-compute-the-next-prime
     * @param n lower inclusive limit of the searching prime number
     * @return minimal prime number p such that p &ge; n
     */
    public static long getNext( long n )
    {
        if ( n < 6L )
        {
            if ( n < 0L )
            {
                // Long.MIN_VALUE cannot be negated.
                if ( n == Long.MIN_VALUE ) return LAST_LONG_PRIME;
                return -getNext( -n );
            }
            switch ( ( int )n )
            {
                case 0:
                case 1:
                case 2:
                    return 2L;
                case 3:
                    return 3L;
                case 4:
                case 5:
                    return 5L;
            }
        }
        // Return first number from the beginning if n is too large.
        if ( n > LAST_LONG_PRIME ) return -LAST_LONG_PRIME;
        long k0 = n / CYCLE_LENGTH;
        int in = Arrays.binarySearch( INDICES, ( int )( n - k0 * CYCLE_LENGTH ) );
        if ( in < 0 ) in = -1 - in;
        n = k0 * CYCLE_LENGTH + INDICES[ in ];
        while ( !PrimeUtils.isPrime( n ) )
        {
            in++;
            if ( in == INDICES.length )
            {
                k0++;
                in = 0;
            }
            n = k0 * CYCLE_LENGTH + INDICES[ in ];
        }
        return n;
    }
    
    /**
     * Seeking of the next prime number that is greater than or equal to n.<br>
     * Examples: getNext( 7 ) = 7, getNext( 9 ) = 11.
     * <p>For negative n, -getNext( -n ) is returned.
     * <p>If n is null then null is returned.
     * @author Howard Hinnant
     * http://stackoverflow.com/questions/4475996/given-prime-number-n-compute-the-next-prime
     * @param n lower inclusive limit of the searching prime number
     * @return minimal prime number p such that p &ge; n, or null if n is null
     */
    public static BigInteger getNext( BigInteger n )
    {
        if ( n == null ) return null;
        if ( n.signum() < 0 ) return getNext( n.negate() ).negate();
        if ( n.compareTo( BI_LAST_LONG_PRIME ) <= 0 ) return BigInteger.valueOf( getNext( n.longValue() ) );
        BigInteger cycleLength = BigInteger.valueOf( CYCLE_LENGTH );
        BigInteger k[] = n.divideAndRemainder( cycleLength );
        BigInteger k0 = k[ 0 ];
        BigInteger r = k[ 1 ];
        int in = Arrays.binarySearch( INDICES, r.intValue() );
        if ( in < 0 ) in = -1 - in;
        n = n.add( BigInteger.valueOf( INDICES[ in ] ) ).subtract( r );
        while ( !PrimeUtils.isPrime( n ) )
        {
            in++;
            if ( in == INDICES.length )
            {
                k0 = k0.add( BigInteger.ONE );
                in = 0;
            }
            n = k0.multiply( cycleLength ).add( BigInteger.valueOf( INDICES[ in ] ) );
        }
        return n;
    }
}