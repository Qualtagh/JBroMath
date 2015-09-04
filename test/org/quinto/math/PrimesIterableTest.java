package org.quinto.math;

import java.math.BigInteger;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.quinto.math.PrimeUtils.isPrime;
import static org.quinto.math.PrimesIterable.*;
import static org.quinto.math.TestUtils.bi;

public class PrimesIterableTest
{
    private static final int FIRST_PRIMES[] = new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71 };
    
    public PrimesIterableTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
    @Test( timeout = 5000L )
    public void getBigIntegerTotallyCasual()
    {
        int i = 0;
        for ( BigInteger bi : getBigIntegerTotally( bi( 5 ) ) )
        {
            assertEquals( FIRST_PRIMES[ i ], bi.intValue() );
            i++;
        }
        Iterator< BigInteger > it = getBigIntegerTotally( bi( 1 ) ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, it.next().intValue() );
        assertFalse( it.hasNext() );
        it = getBigIntegerTotally( bi( 0 ) ).iterator();
        assertFalse( it.hasNext() );
        it = getBigIntegerTotally( bi( -5 ) ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( BigInteger bi : getBigIntegerTotally( bi( "10000000000000000000000000000000000000000000000" ) ) )
        {
            assertEquals( FIRST_PRIMES[ i ], bi.intValue() );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L, expected = IllegalArgumentException.class )
    public void getBigIntegerTotallyError()
    {
        getBigIntegerTotally( null );
    }
    
    @Test( timeout = 5000L )
    public void getLongTotallyCasual()
    {
        int i = 0;
        for ( long l : getLongTotally( 5L ) )
        {
            assertEquals( FIRST_PRIMES[ i ], ( int )l );
            i++;
        }
        Iterator< Long > it = getLongTotally( 2L ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, it.next().intValue() );
        assertTrue( it.hasNext() );
        assertEquals( 3, it.next().intValue() );
        assertFalse( it.hasNext() );
        it = getLongTotally( 0L ).iterator();
        assertFalse( it.hasNext() );
        it = getLongTotally( -5L ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( long l : getLongTotally( Long.MAX_VALUE ) )
        {
            assertEquals( FIRST_PRIMES[ i ], ( int )l );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L )
    public void getIntTotallyCasual()
    {
        int i = 0;
        for ( int l : getIntegerTotally( 5 ) )
        {
            assertEquals( FIRST_PRIMES[ i ], l );
            i++;
        }
        Iterator< Integer > it = getIntegerTotally( 2 ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, ( int )it.next() );
        assertTrue( it.hasNext() );
        assertEquals( 3, ( int )it.next() );
        assertFalse( it.hasNext() );
        it = getIntegerTotally( 0 ).iterator();
        assertFalse( it.hasNext() );
        it = getIntegerTotally( -5 ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( int l : getIntegerTotally( Integer.MAX_VALUE ) )
        {
            assertEquals( FIRST_PRIMES[ i ], l );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L )
    public void getBigIntegerMaxCasual()
    {
        int i = 0;
        for ( BigInteger bi : getBigIntegerMax( bi( 5 ) ) )
        {
            assertEquals( FIRST_PRIMES[ i ], bi.intValue() );
            i++;
        }
        Iterator< BigInteger > it = getBigIntegerMax( bi( 3 ) ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, it.next().intValue() );
        assertTrue( it.hasNext() );
        assertEquals( 3, it.next().intValue() );
        assertFalse( it.hasNext() );
        it = getBigIntegerMax( bi( 0 ) ).iterator();
        assertFalse( it.hasNext() );
        it = getBigIntegerMax( bi( 1 ) ).iterator();
        assertFalse( it.hasNext() );
        it = getBigIntegerMax( bi( 2 ) ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, it.next().intValue() );
        assertFalse( it.hasNext() );
        it = getBigIntegerMax( bi( -5 ) ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( BigInteger bi : getBigIntegerMax( bi( "10000000000000000000000000000000000000000000000" ) ) )
        {
            assertEquals( FIRST_PRIMES[ i ], bi.intValue() );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L, expected = IllegalArgumentException.class )
    public void getBigIntegerMaxError()
    {
        getBigIntegerMax( null );
    }
    
    @Test( timeout = 5000L )
    public void getLongMaxCasual()
    {
        int i = 0;
        for ( long l : getLongMax( 5L ) )
        {
            assertEquals( FIRST_PRIMES[ i ], ( int )l );
            i++;
        }
        Iterator< Long > it = getLongMax( 2L ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, it.next().intValue() );
        assertFalse( it.hasNext() );
        it = getLongMax( 0L ).iterator();
        assertFalse( it.hasNext() );
        it = getLongMax( 1L ).iterator();
        assertFalse( it.hasNext() );
        it = getLongMax( -5L ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( long l : getLongMax( Long.MAX_VALUE ) )
        {
            assertEquals( FIRST_PRIMES[ i ], ( int )l );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L )
    public void getIntMaxCasual()
    {
        int i = 0;
        for ( int l : getIntegerMax( 5 ) )
        {
            assertEquals( FIRST_PRIMES[ i ], l );
            i++;
        }
        Iterator< Integer > it = getIntegerMax( 2 ).iterator();
        assertTrue( it.hasNext() );
        assertEquals( 2, ( int )it.next() );
        assertFalse( it.hasNext() );
        it = getIntegerMax( 0 ).iterator();
        assertFalse( it.hasNext() );
        it = getIntegerMax( 1 ).iterator();
        assertFalse( it.hasNext() );
        it = getIntegerMax( -5 ).iterator();
        assertFalse( it.hasNext() );
        i = 0;
        for ( int l : getIntegerMax( Integer.MAX_VALUE ) )
        {
            assertEquals( FIRST_PRIMES[ i ], l );
            i++;
            if ( i == FIRST_PRIMES.length ) break;
        }
    }
    
    @Test( timeout = 5000L )
    public void getNextBigIntegerCasual()
    {
        assertEquals( bi( 2 ), getNext( bi( 0 ) ) );
        assertEquals( bi( 2 ), getNext( bi( 1 ) ) );
        assertEquals( bi( 2 ), getNext( bi( 2 ) ) );
        assertEquals( bi( 3 ), getNext( bi( 3 ) ) );
        assertEquals( bi( 5 ), getNext( bi( 4 ) ) );
        assertEquals( bi( 5 ), getNext( bi( 5 ) ) );
        assertEquals( bi( 7 ), getNext( bi( 6 ) ) );
        assertEquals( bi( 7 ), getNext( bi( 7 ) ) );
        assertEquals( bi( 11 ), getNext( bi( 8 ) ) );
        assertEquals( bi( 11 ), getNext( bi( 9 ) ) );
        assertEquals( bi( 11 ), getNext( bi( 10 ) ) );
        assertEquals( bi( -2 ), getNext( bi( -1 ) ) );
        assertEquals( bi( -2 ), getNext( bi( -2 ) ) );
        assertEquals( bi( -3 ), getNext( bi( -3 ) ) );
        assertEquals( bi( -5 ), getNext( bi( -4 ) ) );
        assertEquals( bi( 9223372036854775783L ), getNext( bi( 9223372036854775782L ) ) );
        assertEquals( bi( 9223372036854775783L ), getNext( bi( 9223372036854775783L ) ) );
        assertEquals( bi( "9223372036854775837" ), getNext( bi( 9223372036854775784L ) ) );
        assertEquals( bi( "9223372036854775907" ), getNext( bi( "9223372036854775838" ) ) );
        assertEquals( null, getNext( null ) );
    }
    
    @Test( timeout = 5000L )
    public void getNextBigIntegerRange()
    {
        BigInteger prev = null;
        for ( int i = 0; i < 1000; i++ )
        {
            BigInteger bi = bi( i );
            BigInteger current = getNext( bi );
            assertTrue( isPrime( current ) );
            assertTrue( current.compareTo( bi ) >= 0 );
            assertEquals( isPrime( bi ), bi.equals( current ) );
            if ( i > 0 ) assertEquals( current.negate(), getNext( bi.negate() ) );
            if ( current.intValue() == i && prev != null && prev.intValue() != i - 1 ) assertEquals( prev, current );
            prev = current;
        }
    }
    
    @Test( timeout = 10000L )
    public void getNextBigIntegerSpecial()
    {
        for ( BigInteger bi : TestUtils.getBigIntegers() )
        {
            BigInteger current = getNext( bi );
            assertTrue( isPrime( current ) );
            if ( bi.signum() != 0 ) assertEquals( current.negate(), getNext( bi.negate() ) );
            boolean isPrime = isPrime( bi );
            assertEquals( isPrime, bi.equals( current ) );
            if ( isPrime )
            {
                if ( bi.signum() > 0 ) assertTrue( getNext( bi.add( BigInteger.ONE ) ).compareTo( bi ) > 0 );
                else assertTrue( getNext( bi.subtract( BigInteger.ONE ) ).compareTo( bi ) < 0 );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void getNextLongCasual()
    {
        assertEquals( 2L, getNext( 0L ) );
        assertEquals( 2L, getNext( 1L ) );
        assertEquals( 2L, getNext( 2L ) );
        assertEquals( 3L, getNext( 3L ) );
        assertEquals( 5L, getNext( 4L ) );
        assertEquals( 5L, getNext( 5L ) );
        assertEquals( 7L, getNext( 6L ) );
        assertEquals( 7L, getNext( 7L ) );
        assertEquals( 11L, getNext( 8L ) );
        assertEquals( 11L, getNext( 9L ) );
        assertEquals( 11L, getNext( 10L ) );
        assertEquals( -2L, getNext( -1L ) );
        assertEquals( -2L, getNext( -2L ) );
        assertEquals( -3L, getNext( -3L ) );
        assertEquals( -5L, getNext( -4L ) );
        assertEquals( 9223372036854775783L, getNext( 9223372036854775782L ) );
        assertEquals( 9223372036854775783L, getNext( 9223372036854775783L ) );
        assertEquals( -9223372036854775783L, getNext( 9223372036854775784L ) );
        assertEquals( -9223372036854775783L, getNext( Long.MAX_VALUE ) );
        assertEquals( -9223372036854775783L, getNext( -9223372036854775782L ) );
        assertEquals( -9223372036854775783L, getNext( -9223372036854775783L ) );
        assertEquals( 9223372036854775783L, getNext( -9223372036854775784L ) );
        assertEquals( 9223372036854775783L, getNext( -Long.MAX_VALUE ) );
        assertEquals( 9223372036854775783L, getNext( Long.MIN_VALUE ) );
    }
    
    @Test( timeout = 5000L )
    public void getNextLongRange()
    {
        long prev = -1L;
        for ( long l = 0L; l < 1000L; l++ )
        {
            long current = getNext( l );
            assertTrue( isPrime( current ) );
            assertTrue( current >= l );
            assertEquals( isPrime( l ), l == current );
            if ( l > 0L ) assertEquals( -current, getNext( -l ) );
            if ( current == l && prev >= 0L && prev != l - 1L ) assertEquals( prev, current );
            prev = current;
        }
    }
    
    @Test( timeout = 10000L )
    public void getNextLongSpecial()
    {
        for ( long l : TestUtils.getLongs() )
        {
            long current = getNext( l );
            assertTrue( isPrime( current ) );
            if ( l != 0L && l != Long.MIN_VALUE ) assertEquals( "For " + l, -current, getNext( -l ) );
            boolean isPrime = isPrime( l );
            assertEquals( isPrime, l == current );
            if ( isPrime )
            {
                if ( l > 0L )
                {
                    if ( l < PrimesIterable.LAST_LONG_PRIME ) assertTrue( "For " + l, getNext( l + 1L ) > l );
                }
                else
                {
                    if ( l > -PrimesIterable.LAST_LONG_PRIME ) assertTrue( "For " + l, getNext( l - 1L ) < l );
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void getNextIntCasual()
    {
        assertEquals( 2, getNext( 0 ) );
        assertEquals( 2, getNext( 1 ) );
        assertEquals( 2, getNext( 2 ) );
        assertEquals( 3, getNext( 3 ) );
        assertEquals( 5, getNext( 4 ) );
        assertEquals( 5, getNext( 5 ) );
        assertEquals( 7, getNext( 6 ) );
        assertEquals( 7, getNext( 7 ) );
        assertEquals( 11, getNext( 8 ) );
        assertEquals( 11, getNext( 9 ) );
        assertEquals( 11, getNext( 10 ) );
        assertEquals( -2, getNext( -1 ) );
        assertEquals( -2, getNext( -2 ) );
        assertEquals( -3, getNext( -3 ) );
        assertEquals( -5, getNext( -4 ) );
        assertEquals( Integer.MAX_VALUE, getNext( Integer.MAX_VALUE - 1 ) );
        assertEquals( Integer.MAX_VALUE, getNext( Integer.MAX_VALUE ) );
        assertEquals( -Integer.MAX_VALUE, getNext( -Integer.MAX_VALUE ) );
        assertEquals( -Integer.MAX_VALUE, getNext( -Integer.MAX_VALUE + 1 ) );
        assertEquals( Integer.MAX_VALUE, getNext( Integer.MIN_VALUE ) );
    }
    
    @Test( timeout = 5000L )
    public void getNextIntRange()
    {
        int prev = -1;
        for ( int l = 0; l < 1000; l++ )
        {
            int current = getNext( l );
            assertTrue( isPrime( current ) );
            assertTrue( current >= l );
            assertEquals( isPrime( l ), l == current );
            if ( l > 0 ) assertEquals( -current, getNext( -l ) );
            if ( current == l && prev >= 0 && prev != l - 1 ) assertEquals( prev, current );
            prev = current;
        }
    }
    
    @Test( timeout = 10000L )
    public void getNextIntSpecial()
    {
        for ( int l : TestUtils.getInts() )
        {
            int current = getNext( l );
            assertTrue( isPrime( current ) );
            if ( l != 0 && l != Integer.MIN_VALUE ) assertEquals( "For " + l, -current, getNext( -l ) );
            boolean isPrime = isPrime( l );
            assertEquals( isPrime, l == current );
            if ( isPrime )
            {
                if ( l > 0 )
                {
                    if ( l < Integer.MAX_VALUE ) assertTrue( "For " + l, getNext( l + 1 ) > l );
                }
                else
                {
                    if ( l > -Integer.MAX_VALUE ) assertTrue( "For " + l, getNext( l - 1 ) < l );
                }
            }
        }
    }
}