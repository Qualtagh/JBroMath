package org.quinto.math;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class TestUtils
{
    private static final Random RANDOM = new Random( System.nanoTime() );
    private static final int INTS[] = new int[]{ Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    private static final long LONGS[] = new long[]{ Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2, -9223372036854775782L, -9223372036854775783L, -9223372036854775784L, 9223372036854775782L, 9223372036854775783L, 9223372036854775784L, Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    private static final BigInteger BIG_INTEGERS[];
    
    static
    {
        Set< BigInteger > bis = new LinkedHashSet<>();
        for ( long l : LONGS )
        {
            BigInteger bi = BigInteger.valueOf( l );
            for ( int i = 1; i < 4; i++ )
            {
                bis.add( bi );
                bis.add( bi.add( BigInteger.ONE ) );
                bis.add( bi.add( BigUtils.BI_TWO ) );
                bis.add( bi.add( BigUtils.BI_THREE ) );
                bis.add( bi.add( BigUtils.BI_FOUR ) );
                bis.add( bi.add( BigUtils.BI_FIVE ) );
                bis.add( bi.subtract( BigInteger.ONE ) );
                bis.add( bi.subtract( BigUtils.BI_TWO ) );
                bis.add( bi.subtract( BigUtils.BI_THREE ) );
                bis.add( bi.subtract( BigUtils.BI_FOUR ) );
                bis.add( bi.subtract( BigUtils.BI_FIVE ) );
                bi = bi.shiftLeft( 1 );
            }
        }
        BIG_INTEGERS = bis.toArray( new BigInteger[ bis.size() ] );
    }
    
    public static int[] getInts()
    {
        int consecutive[] = new int[ 50 ];
        for ( int i = 0; i < consecutive.length; i++ ) consecutive[ i ] = i - consecutive.length / 3;
        int random[] = new int[ 50 ];
        for ( int i = 0; i < random.length; i++ ) random[ i ] = RANDOM.nextInt();
        return concat( consecutive, random, INTS );
    }
    
    public static int[] concat( int[]... arrays )
    {
        int length = 0;
        for ( int array[] : arrays ) length += array.length;
        int ret[] = new int[ length ];
        int shift = 0;
        for ( int array[] : arrays )
        {
            System.arraycopy( array, 0, ret, shift, array.length );
            shift += array.length;
        }
        return ret;
    }
    
    public static long[] getLongs()
    {
        long consecutive[] = new long[ 50 ];
        for ( int i = 0; i < consecutive.length; i++ ) consecutive[ i ] = i - consecutive.length / 3;
        long random[] = new long[ 50 ];
        for ( int i = 0; i < random.length; i++ ) random[ i ] = RANDOM.nextLong();
        return concat( consecutive, random, LONGS );
    }
    
    public static long[] concat( long[]... arrays )
    {
        int length = 0;
        for ( long array[] : arrays ) length += array.length;
        long ret[] = new long[ length ];
        int shift = 0;
        for ( long array[] : arrays )
        {
            System.arraycopy( array, 0, ret, shift, array.length );
            shift += array.length;
        }
        return ret;
    }
    
    public static BigInteger[] getBigIntegers()
    {
        BigInteger consecutive[] = new BigInteger[ 50 ];
        for ( int i = 0; i < consecutive.length; i++ ) consecutive[ i ] = BigInteger.valueOf( i - consecutive.length / 3 );
        BigInteger random[] = new BigInteger[ 50 ];
        for ( int i = 0; i < random.length; i++ ) random[ i ] = BigInteger.valueOf( RANDOM.nextLong() );
        BigInteger probablePrimes[] = new BigInteger[ 300 ];
        for ( int i = 0; i < probablePrimes.length; i++ ) probablePrimes[ i ] = BigInteger.probablePrime( Math.max( i / 2, 2 ), RANDOM );
        for ( int i = 0; i < probablePrimes.length; i++ ) if ( RANDOM.nextInt( 10 ) == 0 ) probablePrimes[ i ] = probablePrimes[ i ].negate();
        BigInteger longRandom[] = new BigInteger[ 50 ];
        for ( int i = 0; i < longRandom.length; i++ )
        {
            StringBuilder sb = new StringBuilder( i + 1 );
            for ( int j = 0; j <= i; j++ )
            {
                if ( j == 0 ) sb.append( ( char )( RANDOM.nextInt( 9 ) + '1' ) );
                else sb.append( ( char )( RANDOM.nextInt( 10 ) + '0' ) );
            }
            longRandom[ i ] = new BigInteger( sb.toString() );
        }
        for ( int i = 0; i < longRandom.length; i++ ) if ( RANDOM.nextInt( 10 ) == 0 ) longRandom[ i ] = longRandom[ i ].negate();
        return concat( consecutive, random, probablePrimes, longRandom, BIG_INTEGERS );
    }
    
    public static BigInteger[] concat( BigInteger[]... arrays )
    {
        int length = 0;
        for ( BigInteger array[] : arrays ) length += array.length;
        BigInteger ret[] = new BigInteger[ length ];
        int shift = 0;
        for ( BigInteger array[] : arrays )
        {
            System.arraycopy( array, 0, ret, shift, array.length );
            shift += array.length;
        }
        return ret;
    }
    
    public static File getResourceFile( String name )
    {
        URL url = TestUtils.class.getResource( "/" + TestUtils.class.getPackage().getName().replace( '.', '/' ) + "/" + name );
        return new File( url.getFile() );
    }
    
    public static BigInteger bi( long l )
    {
        return BigInteger.valueOf( l );
    }
    
    public static BigInteger bi( String s )
    {
        return new BigInteger( s );
    }
}