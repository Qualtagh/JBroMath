package org.quinto.math;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.quinto.math.TestUtils.bi;
import static org.quinto.math.BigUtils.lcm;
import static org.quinto.math.BigUtils.isRelativelyPrime;

public class BigUtilsGcdTest
{
    public BigUtilsGcdTest()
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
    public void lcmCasual()
    {
        assertEquals( bi( 18 ), lcm( bi( 6 ), bi( 9 ) ) );
        assertEquals( bi( 30 ), lcm( bi( 6 ), bi( 5 ) ) );
        assertEquals( bi( 104 ), lcm( bi( 8 ), bi( 13 ) ) );
        assertEquals( bi( 0 ), lcm( bi( 0 ), bi( 9 ) ) );
    }
    
    @Test( timeout = 15000L )
    public void lcmSpecial()
    {
        BigInteger bis[] = TestUtils.getBigIntegers();
        for ( BigInteger a : bis )
        {
            for ( BigInteger b : bis )
            {
                BigInteger lcm = lcm( a, b );
                BigInteger gcd = a.gcd( b );
                if ( lcm.signum() == 0 )
                {
                    assertTrue( a.signum() == 0 || b.signum() == 0 );
                }
                else
                {
                    assertTrue( lcm.signum() > 0 );
                    assertEquals( 0, lcm.remainder( a ).signum() );
                    assertEquals( 0, lcm.remainder( b ).signum() );
                    assertEquals( a.abs(), a.gcd( lcm ) );
                    assertEquals( lcm, lcm( a.negate(), b ) );
                    assertEquals( lcm, lcm( b, a ) );
                    assertEquals( a.abs(), lcm( a, gcd ) );
                }
                if ( a.signum() == 0 || b.signum() == 0 )
                {
                    assertEquals( 0, lcm.signum() );
                }
                if ( a.abs().equals( b.abs() ) )
                {
                    assertEquals( lcm, a.abs() );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void isRelativelyPrimeCasual()
    {
        assertTrue( isRelativelyPrime( bi( 2 ), bi( 3 ) ) );
        assertTrue( isRelativelyPrime( bi( 4 ), bi( 9 ) ) );
        assertFalse( isRelativelyPrime( bi( 4 ), bi( 6 ) ) );
        assertFalse( isRelativelyPrime( bi( 6 ), bi( 9 ) ) );
        assertTrue( isRelativelyPrime( bi( 3 ), bi( -2 ) ) );
        assertTrue( isRelativelyPrime( bi( 9 ), bi( 4 ) ) );
        assertFalse( isRelativelyPrime( bi( -4 ), bi( 6 ) ) );
        assertFalse( isRelativelyPrime( bi( 9 ), bi( 6 ) ) );
    }

    @Test( timeout = 5000L )
    public void isRelativelyPrimeRange()
    {
        for ( int a = -100; a <= 100; a++ )
        {
            for ( int b = -100; b <= 100; b++ )
            {
                int cd = Math.max( Math.abs( a ), Math.abs( b ) );
                if ( cd > 0 ) for ( ; cd >= 0; cd-- ) if ( a % cd == 0 && b % cd == 0 ) break;
                BigInteger bia = bi( a );
                BigInteger bib = bi( b );
                assertEquals( cd, bia.gcd( bib ).intValue() );
                assertEquals( cd == 1, isRelativelyPrime( bia, bib ) );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void isRelativelyPrimeSpecial()
    {
        BigInteger bis[] = TestUtils.getBigIntegers();
        for ( BigInteger a : bis )
        {
            for ( BigInteger b : bis )
            {
                BigInteger gcd = a.gcd( b );
                assertEquals( gcd.equals( BigInteger.ONE ), isRelativelyPrime( a, b ) );
            }
        }
    }
}