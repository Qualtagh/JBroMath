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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.quinto.math.BigUtils.getBaseOfPerfectSquare;
import static org.quinto.math.BigUtils.isPerfectSquare;
import static org.quinto.math.BigUtils.isqrt;
import static org.quinto.math.TestUtils.bi;

public class BigUtilsPerfectPowerTest
{
    public BigUtilsPerfectPowerTest()
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
    public void getBaseOfPerfectSquareCasual()
    {
        assertEquals( bi( 0L ), getBaseOfPerfectSquare( bi( 0L ) ) );
        assertEquals( bi( 1L ), getBaseOfPerfectSquare( bi( 1L ) ) );
        assertEquals( bi( 2L ), getBaseOfPerfectSquare( bi( 4L ) ) );
        assertEquals( bi( 3L ), getBaseOfPerfectSquare( bi( 9L ) ) );
        assertEquals( bi( 25L ), getBaseOfPerfectSquare( bi( 625L ) ) );
        assertEquals( null, getBaseOfPerfectSquare( bi( 624L ) ) );
        assertEquals( null, getBaseOfPerfectSquare( bi( -9L ) ) );
        assertEquals( bi( 46341L ), getBaseOfPerfectSquare( bi( 2147488281L ) ) );
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectSquareSpecial()
    {
        for ( BigInteger n : TestUtils.getBigIntegers() )
        {
            BigInteger base = getBaseOfPerfectSquare( n );
           // assertEquals( base, getBaseOfPerfectPower( n, 2 ) );
            if ( base == null )
            {
                assertFalse( isPerfectSquare( n ) );
                try
                {
                    base = isqrt( n );
                }
                catch ( ArithmeticException e )
                {
                    if ( n.signum() >= 0 ) fail( "Error in isqrt of " + n );
                    continue;
                }
                try
                {
                    assertFalse( base.multiply( base ).equals( n ) );
                }
                catch ( ArithmeticException e )
                {
                    fail( "Error in isqrt: " + base + " ^ 2 = " + n );
                }
            }
            else
            {
                assertTrue( isPerfectSquare( n ) );
                assertEquals( isqrt( n ), base );
                assertEquals( n, base.multiply( base ) );
            }
        }
    }

    @Test( timeout = 25000L )
    public void getBaseOfPerfectSquareRange()
    {
        for ( long base = 2L; base < 3037000L; base++ )
        {
            BigInteger n = bi( base * base );
            assertEquals( null, getBaseOfPerfectSquare( n.subtract( BigInteger.ONE ) ) );
            assertEquals( bi( base ), getBaseOfPerfectSquare( n ) );
            assertEquals( null, getBaseOfPerfectSquare( n.add( BigInteger.ONE ) ) );
        }
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectSquareReversed()
    {
        for ( BigInteger base : TestUtils.getBigIntegers() )
        {
            if ( base.compareTo( BigInteger.ONE ) <= 0 ) continue;
            BigInteger n = base.multiply( base );
            assertEquals( null, getBaseOfPerfectSquare( n.subtract( BigInteger.ONE ) ) );
            assertEquals( base, getBaseOfPerfectSquare( n ) );
            assertEquals( null, getBaseOfPerfectSquare( n.add( BigInteger.ONE ) ) );
        }
    }
}