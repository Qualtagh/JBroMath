package org.quinto.math;

import java.math.BigInteger;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.quinto.math.BigUtils.mod;
import static org.quinto.math.BigUtils.mods;
import static org.quinto.math.TestUtils.bi;
import static org.quinto.math.TestUtils.getBigIntegers;

public class BigUtilsModTest
{
    private static final Random RANDOM = new Random( System.nanoTime() );
    
    public BigUtilsModTest()
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
    public void modCasual()
    {
        assertEquals( bi( 3 ), mod( bi( 8 ), bi( 5 ) ) );
        assertEquals( bi( 3 ), mod( bi( 8 ), bi( -5 ) ) );
        assertEquals( bi( 2 ), mod( bi( -8 ), bi( 5 ) ) );
        assertEquals( bi( 2 ), mod( bi( -8 ), bi( -5 ) ) );
    }
    
    @Test( timeout = 5000L, expected = ArithmeticException.class )
    public void modZero()
    {
        mod( bi( 8 ), bi( 0 ) );
    }

    @Test( timeout = 5000L )
    public void modRandom()
    {
        for ( int i = 0; i < 500; i++ )
        {
            long mod = RANDOM.nextLong();
            BigInteger bimod = bi( mod );
            for ( int j = 0; j < 500; j++ )
            {
                long v = RANDOM.nextLong();
                BigInteger biv = bi( v );
                if ( bimod.signum() == 0 )
                {
                    try
                    {
                        mod( biv, bimod );
                        fail( "Should have failed with ArithmeticException: zero mod" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( biv.mod( bimod.abs() ), mod( biv, bimod ) );
                    assertEquals( biv.mod( bimod.abs() ), mod( biv, bimod.negate() ) );
                    if ( biv.signum() >= 0 )
                    {
                        assertEquals( biv.remainder( bimod.abs() ), mod( biv, bimod ) );
                        assertEquals( biv.remainder( bimod.abs() ), mod( biv, bimod.negate() ) );
                    }
                }
            }
        }
    }

    @Test( timeout = 15000L )
    public void modSpecial()
    {
        BigInteger bis[] = getBigIntegers();
        for ( BigInteger i : bis )
        {
            for ( BigInteger j : bis )
            {
                BigInteger ja = j.abs();
                if ( j.signum() == 0 )
                {
                    try
                    {
                        mod( i, j );
                        fail( "Should have failed with ArithmeticException: zero mod" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    if ( i.signum() >= 0 )
                    {
                        assertEquals( i.remainder( ja ), mod( i, j ) );
                        assertEquals( i.remainder( ja ), mod( i, j.negate() ) );
                    }
                    assertEquals( i.mod( ja ), mod( i, j ) );
                    assertEquals( i.mod( ja ), mod( i, j.negate() ) );
                    assertEquals( mod( mod( i, j ).add( BigInteger.ONE ), j ), mod( i.add( BigInteger.ONE ), j ) );
                    assertEquals( i + " (mod " + j + ") = " + mod( i, j ), mod( mod( i, j ).shiftLeft( 1 ), j ), mod( i.shiftLeft( 1 ), j ) );
                    assertEquals( mod( i, j ), mod( i.remainder( ja ), j ) );
                    assertEquals( mod( i, j ), mod( i.mod( ja ), j ) );
                    assertEquals( mod( i, j ), mod( i, j ).remainder( ja ) );
                    assertEquals( mod( i, j ), mod( i, j ).mod( ja ) );
                    assertEquals( mod( i, j ), mod( i, j.negate() ) );
                    assertTrue( mod( i, j ).signum() >= 0 && mod( i, j ).compareTo( ja ) < 0 );
                    if ( ja.equals( BigInteger.ONE ) ) assertEquals( 0, mod( i, j ).signum() );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modsCasual()
    {
        assertEquals( bi( -2 ), mods( bi( 8 ), bi( 5 ) ) );
        assertEquals( bi( -1 ), mods( bi( 9 ), bi( 5 ) ) );
        assertEquals( bi( 0 ), mods( bi( 10 ), bi( 5 ) ) );
        assertEquals( bi( 1 ), mods( bi( 11 ), bi( 5 ) ) );
        assertEquals( bi( 2 ), mods( bi( 12 ), bi( 5 ) ) );
        assertEquals( bi( -2 ), mods( bi( 13 ), bi( 5 ) ) );
        assertEquals( bi( -2 ), mods( bi( 8 ), bi( -5 ) ) );
        assertEquals( bi( 2 ), mods( bi( -8 ), bi( 5 ) ) );
        assertEquals( bi( 2 ), mods( bi( -8 ), bi( -5 ) ) );
        assertEquals( bi( -2 ), mods( bi( 12 ), bi( 7 ) ) );
        assertEquals( bi( 2 ), mods( bi( 8 ), bi( 6 ) ) );
        assertEquals( bi( 3 ), mods( bi( 9 ), bi( 6 ) ) );
        assertEquals( bi( -2 ), mods( bi( 10 ), bi( 6 ) ) );
        assertEquals( bi( -1 ), mods( bi( 11 ), bi( 6 ) ) );
        assertEquals( bi( 0 ), mods( bi( 12 ), bi( 6 ) ) );
        assertEquals( bi( 1 ), mods( bi( 13 ), bi( 6 ) ) );
        assertEquals( bi( 2 ), mods( bi( 14 ), bi( 6 ) ) );
        assertEquals( bi( 2 ), mods( bi( 8 ), bi( -6 ) ) );
        assertEquals( bi( -2 ), mods( bi( -8 ), bi( 6 ) ) );
        assertEquals( bi( -2 ), mods( bi( -8 ), bi( -6 ) ) );
    }
    
    @Test( timeout = 5000L, expected = ArithmeticException.class )
    public void modsZero()
    {
        assertEquals( null, mods( bi( 8 ), bi( 0 ) ) );
    }

    @Test( timeout = 15000L )
    public void modsSpecial()
    {
        BigInteger bis[] = getBigIntegers();
        for ( BigInteger i : bis )
        {
            for ( BigInteger j : bis )
            {
                BigInteger ja = j.abs();
                if ( j.signum() == 0 )
                {
                    try
                    {
                        mods( i, j );
                        fail( "Should have failed with ArithmeticException: zero mod" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( mods( mods( i, j ).add( BigInteger.ONE ), j ), mods( i.add( BigInteger.ONE ), j ) );
                    assertEquals( mods( mods( i, j ).shiftLeft( 1 ), j ), mods( i.shiftLeft( 1 ), j ) );
                    assertEquals( mods( i, j ), mods( i.remainder( ja ), j ) );
                    assertEquals( mods( i, j ), mods( i.mod( ja ), j ) );
                    assertEquals( mods( i, j ), mods( i, j ).remainder( ja ) );
                    assertEquals( mods( i, j ), mods( i, j.negate() ) );
                    assertEquals( mods( i, j ), mods( mod( i, j ), j ) );
                    assertEquals( mod( i, j ), mod( mods( i, j ), j ) );
                    assertTrue( i + " (mods " + j + ") = " + mods( i, j ), mods( i, j ).compareTo( ja.shiftRight( 1 ).subtract( ja.testBit( 0 ) ? BigInteger.ZERO : BigInteger.ONE ).negate() ) >= 0 && mods( i, j ).compareTo( ja.shiftRight( 1 ) ) <= 0 );
                    if ( ja.equals( BigInteger.ONE ) ) assertEquals( 0, mods( i, j ).signum() );
                }
            }
        }
    }
}