package org.quinto.math;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.quinto.math.MathUtils.NOT_FOUND;
import static org.quinto.math.MathUtils.egcd;
import static org.quinto.math.MathUtils.gcd;
import static org.quinto.math.MathUtils.lcm;
import static org.quinto.math.MathUtils.lcmExact;
import static org.quinto.math.MathUtils.isRelativelyPrime;
import static org.quinto.math.TestUtils.bi;

public class MathUtilsGcdTest
{
    public MathUtilsGcdTest()
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
    public void gcdIntCasual()
    {
        assertEquals( 3, gcd( 6, 9 ) );
        assertEquals( 1, gcd( 6, 5 ) );
        assertEquals( 1, gcd( 8, 13 ) );
        assertEquals( 9, gcd( 0, 9 ) );
    }

    @Test( timeout = 5000L )
    public void gcdIntTest()
    {
        int ints[] = TestUtils.getInts();
        for ( int a : ints )
        {
            for ( int b : ints )
            {
                int gcd = gcd( a, b );
                if ( gcd == 0 )
                {
                    assertEquals( 0, a );
                    assertEquals( 0, b );
                }
                else if ( gcd < 0 )
                {
                    assertTrue( a == 0 && b == Integer.MIN_VALUE || a == Integer.MIN_VALUE && b == 0 || a == Integer.MIN_VALUE && b == Integer.MIN_VALUE );
                    assertEquals( Integer.MIN_VALUE, gcd );
                }
                else
                {
                    assertEquals( 0, a % gcd );
                    assertEquals( 0, b % gcd );
                    assertEquals( gcd, gcd( -a, b ) );
                    assertEquals( gcd, gcd( b, a ) );
                    assertEquals( 1, gcd( a / gcd, b / gcd ) );
                    if ( a == 0 ) assertEquals( gcd, Math.abs( b ) );
                    if ( b == 0 ) assertEquals( gcd, Math.abs( a ) );
                }
                assertEquals( bi( a ).gcd( bi( b ) ).intValue(), gcd );
                assertEquals( gcd == 1, isRelativelyPrime( a, b ) );
                assertEquals( gcd == 1, isRelativelyPrime( b, a ) );
                if ( Math.abs( a ) == Math.abs( b ) ) assertEquals( gcd, Math.abs( a ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void gcdLongCasual()
    {
        assertEquals( 3L, gcd( 6L, 9L ) );
        assertEquals( 1L, gcd( 6L, 5L ) );
        assertEquals( 1L, gcd( 8L, 13L ) );
        assertEquals( 9L, gcd( 0L, 9L ) );
    }

    @Test( timeout = 5000L )
    public void gcdLongTest()
    {
        long longs[] = TestUtils.getLongs();
        for ( long a : longs )
        {
            for ( long b : longs )
            {
                long gcd = gcd( a, b );
                if ( gcd == 0L )
                {
                    assertEquals( 0L, a );
                    assertEquals( 0L, b );
                }
                else if ( gcd < 0L )
                {
                    assertTrue( a == 0L && b == Long.MIN_VALUE || a == Long.MIN_VALUE && b == 0L || a == Long.MIN_VALUE && b == Long.MIN_VALUE );
                    assertEquals( Long.MIN_VALUE, gcd );
                }
                else
                {
                    assertEquals( 0L, a % gcd );
                    assertEquals( 0L, b % gcd );
                    assertEquals( gcd, gcd( -a, b ) );
                    assertEquals( gcd, gcd( b, a ) );
                    assertEquals( 1L, gcd( a / gcd, b / gcd ) );
                    if ( a == 0L ) assertEquals( gcd, Math.abs( b ) );
                    if ( b == 0L ) assertEquals( gcd, Math.abs( a ) );
                    int ia = ( int )a;
                    int ib = ( int )b;
                    if ( ia == a && ib == b ) assertEquals( gcd, Math.abs( ( long )gcd( ia, ib ) ) );
                }
                assertEquals( bi( a ).gcd( bi( b ) ).longValue(), gcd );
                assertEquals( gcd == 1L, isRelativelyPrime( a, b ) );
                assertEquals( gcd == 1L, isRelativelyPrime( b, a ) );
                if ( Math.abs( a ) == Math.abs( b ) ) assertEquals( gcd, Math.abs( a ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void lcmIntCasual()
    {
        assertEquals( 18, lcm( 6, 9 ) );
        assertEquals( 30, lcm( 6, 5 ) );
        assertEquals( 104, lcm( 8, 13 ) );
        assertEquals( 0, lcm( 0, 9 ) );
    }

    @Test( timeout = 5000L )
    public void lcmIntTest()
    {
        int ints[] = TestUtils.getInts();
        for ( int a : ints )
        {
            for ( int b : ints )
            {
                int lcm = lcm( a, b );
                int control;
                try
                {
                    control = lcmExact( a, b );
                }
                catch ( ArithmeticException e )
                {
                    control = NOT_FOUND;
                }
                if ( lcm == 0 )
                {
                    assertTrue( a == 0 || b == 0 );
                    assertEquals( lcm, control );
                }
                else if ( lcm < 0 )
                {
                    assertFalse( a == 0 || b == 0 );
                    assertTrue( a == Integer.MIN_VALUE || b == Integer.MIN_VALUE );
                    assertEquals( Integer.MIN_VALUE, lcm );
                    assertEquals( NOT_FOUND, control );
                }
                else
                {
                    if ( control != NOT_FOUND )
                    {
                        assertEquals( lcm, control );
                        assertEquals( 0, lcm % a );
                        assertEquals( 0, lcm % b );
                        assertEquals( control, lcmExact( -a, b ) );
                        assertEquals( control, lcmExact( b, a ) );
                        assertEquals( Math.abs( a ), gcd( a, lcm( a, b ) ) );
                        assertEquals( Math.abs( a ), gcd( a, lcmExact( a, b ) ) );
                    }
                    assertEquals( lcm, lcm( -a, b ) );
                    assertEquals( lcm, lcm( b, a ) );
                    assertEquals( Math.abs( a ), lcm( a, gcd( a, b ) ) );
                    assertEquals( Math.abs( a ), lcmExact( a, gcd( a, b ) ) );
                }
                if ( a == 0 || b == 0 )
                {
                    assertEquals( 0, lcm );
                    assertEquals( lcm, control );
                }
                else if ( a == Integer.MIN_VALUE || b == Integer.MIN_VALUE )
                {
                    assertEquals( Integer.MIN_VALUE, lcm );
                    assertEquals( NOT_FOUND, control );
                }
                if ( Math.abs( a ) == Math.abs( b ) )
                {
                    assertEquals( lcm, Math.abs( a ) );
                    if ( a == Integer.MIN_VALUE ) assertEquals( NOT_FOUND, control );
                    else assertEquals( lcm, control );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void lcmLongCasual()
    {
        assertEquals( 18L, lcm( 6L, 9L ) );
        assertEquals( 30L, lcm( 6L, 5L ) );
        assertEquals( 104L, lcm( 8L, 13L ) );
        assertEquals( 0L, lcm( 0L, 9L ) );
    }

    @Test( timeout = 5000L )
    public void lcmLongTest()
    {
        long longs[] = TestUtils.getLongs();
        for ( long a : longs )
        {
            for ( long b : longs )
            {
                long lcm = lcm( a, b );
                long control;
                try
                {
                    control = lcmExact( a, b );
                }
                catch ( ArithmeticException e )
                {
                    control = NOT_FOUND;
                }
                if ( lcm == 0L )
                {
                    assertTrue( a == 0L || b == 0L );
                    assertEquals( lcm, control );
                }
                else if ( lcm < 0L )
                {
                    assertFalse( a == 0L || b == 0L );
                    assertTrue( a == Long.MIN_VALUE || b == Long.MIN_VALUE );
                    assertEquals( Long.MIN_VALUE, lcm );
                    assertEquals( NOT_FOUND, control );
                }
                else
                {
                    if ( control != NOT_FOUND )
                    {
                        assertEquals( lcm, control );
                        assertEquals( 0L, lcm % a );
                        assertEquals( 0L, lcm % b );
                        assertEquals( control, lcmExact( -a, b ) );
                        assertEquals( control, lcmExact( b, a ) );
                        assertEquals( Math.abs( a ), gcd( a, lcm( a, b ) ) );
                        assertEquals( Math.abs( a ), gcd( a, lcmExact( a, b ) ) );
                    }
                    assertEquals( lcm, lcm( -a, b ) );
                    assertEquals( lcm, lcm( b, a ) );
                    assertEquals( Math.abs( a ), lcm( a, gcd( a, b ) ) );
                    assertEquals( Math.abs( a ), lcmExact( a, gcd( a, b ) ) );
                }
                if ( a == 0L || b == 0L )
                {
                    assertEquals( 0L, lcm );
                    assertEquals( lcm, control );
                }
                else if ( a == Long.MIN_VALUE || b == Long.MIN_VALUE )
                {
                    assertEquals( Long.MIN_VALUE, lcm );
                    assertEquals( NOT_FOUND, control );
                }
                if ( Math.abs( a ) == Math.abs( b ) )
                {
                    assertEquals( lcm, Math.abs( a ) );
                    if ( a == Long.MIN_VALUE ) assertEquals( NOT_FOUND, control );
                    else assertEquals( lcm, control );
                }
                int ia = ( int )a;
                int ib = ( int )b;
                if ( ia == a && ib == b )
                {
                    int icontrol;
                    try
                    {
                        icontrol = lcmExact( ia, ib );
                    }
                    catch ( ArithmeticException e )
                    {
                        icontrol = NOT_FOUND;
                    }
                    if ( icontrol != NOT_FOUND ) assertEquals( lcm, Math.abs( ( long )icontrol ) );
                    assertEquals( lcm, control );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void egcdIntCasual()
    {
        assertArrayEquals( new int[]{ 3, -1, 1 }, egcd( 6, 9 ) );
        assertArrayEquals( new int[]{ 1, 1, -1 }, egcd( 6, 5 ) );
        assertArrayEquals( new int[]{ 1, 5, -3 }, egcd( 8, 13 ) );
        assertArrayEquals( new int[]{ 9, 0, 1 }, egcd( 0, 9 ) );
    }

    @Test( timeout = 5000L )
    public void egcdIntTest()
    {
        int ints[] = TestUtils.getInts();
        for ( int a : ints )
        {
            for ( int b : ints )
            {
                int egcd[] = egcd( a, b );
                int gcd = egcd[ 0 ];
                int x = egcd[ 1 ];
                int y = egcd[ 2 ];
                assertEquals( gcd( a, b ), gcd );
                assertEquals( gcd, BigInteger.valueOf( a ).multiply( BigInteger.valueOf( x ) ).add( BigInteger.valueOf( b ).multiply( BigInteger.valueOf( y ) ) ).intValueExact() );
                if ( gcd == 0 )
                {
                    assertEquals( 0, a );
                    assertEquals( 0, b );
                }
                else if ( gcd < 0 )
                {
                    assertTrue( a == 0 && b == Integer.MIN_VALUE || a == Integer.MIN_VALUE && b == 0 || a == Integer.MIN_VALUE && b == Integer.MIN_VALUE );
                    assertEquals( Integer.MIN_VALUE, gcd );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void egcdLongCasual()
    {
        assertArrayEquals( new long[]{ 3L, -1L, 1L }, egcd( 6L, 9L ) );
        assertArrayEquals( new long[]{ 1L, 1L, -1L }, egcd( 6L, 5L ) );
        assertArrayEquals( new long[]{ 1L, 5L, -3L }, egcd( 8L, 13L ) );
        assertArrayEquals( new long[]{ 9L, 0L, 1L }, egcd( 0L, 9L ) );
    }

    @Test( timeout = 5000L )
    public void egcdLongTest()
    {
        long longs[] = TestUtils.getLongs();
        for ( long a : longs )
        {
            for ( long b : longs )
            {
                long egcd[] = egcd( a, b );
                long gcd = egcd[ 0 ];
                long x = egcd[ 1 ];
                long y = egcd[ 2 ];
                assertEquals( gcd( a, b ), gcd );
                assertEquals( gcd, BigInteger.valueOf( a ).multiply( BigInteger.valueOf( x ) ).add( BigInteger.valueOf( b ).multiply( BigInteger.valueOf( y ) ) ).longValueExact() );
                if ( gcd == 0L )
                {
                    assertEquals( 0L, a );
                    assertEquals( 0L, b );
                }
                else if ( gcd < 0L )
                {
                    assertTrue( a == 0L && b == Long.MIN_VALUE || a == Long.MIN_VALUE && b == 0L || a == Long.MIN_VALUE && b == Long.MIN_VALUE );
                    assertEquals( Long.MIN_VALUE, gcd );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void isRelativelyPrimeIntCasual()
    {
        assertTrue( isRelativelyPrime( 2, 3 ) );
        assertTrue( isRelativelyPrime( 4, 9 ) );
        assertFalse( isRelativelyPrime( 4, 6 ) );
        assertFalse( isRelativelyPrime( 6, 9 ) );
        assertTrue( isRelativelyPrime( 3, -2 ) );
        assertTrue( isRelativelyPrime( 9, 4 ) );
        assertFalse( isRelativelyPrime( -4, 6 ) );
        assertFalse( isRelativelyPrime( 9, 6 ) );
    }

    @Test( timeout = 5000L )
    public void isRelativelyPrimeIntRange()
    {
        for ( int a = -100; a <= 100; a++ )
        {
            for ( int b = -100; b <= 100; b++ )
            {
                int cd = Math.max( Math.abs( a ), Math.abs( b ) );
                if ( cd > 0 ) for ( ; cd >= 0; cd-- ) if ( a % cd == 0 && b % cd == 0 ) break;
                assertEquals( cd, gcd( a, b ) );
                assertEquals( cd == 1, isRelativelyPrime( a, b ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void isRelativelyPrimeLongCasual()
    {
        assertTrue( isRelativelyPrime( 2L, 3L ) );
        assertTrue( isRelativelyPrime( 4L, 9L ) );
        assertFalse( isRelativelyPrime( 4L, 6L ) );
        assertFalse( isRelativelyPrime( 6L, 9L ) );
        assertTrue( isRelativelyPrime( 3L, -2L ) );
        assertTrue( isRelativelyPrime( 9L, 4L ) );
        assertFalse( isRelativelyPrime( -4L, 6L ) );
        assertFalse( isRelativelyPrime( 9L, 6L ) );
    }
}