package org.quinto.math;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.quinto.math.MathUtils.NOT_FOUND;
import static org.quinto.math.MathUtils.gcd;
import static org.quinto.math.MathUtils.mod;
import static org.quinto.math.MathUtils.modAdd;
import static org.quinto.math.MathUtils.modDivide;
import static org.quinto.math.MathUtils.modInverse;
import static org.quinto.math.MathUtils.modMultiply;
import static org.quinto.math.MathUtils.modPow;
import static org.quinto.math.MathUtils.modSubtract;
import static org.quinto.math.MathUtils.mods;

public class MathUtilsModTest
{
    private static final Random RANDOM = new Random( System.nanoTime() );
    private static final int INTS[] = new int[]{ Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    private static final long LONGS[] = new long[]{ Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2, Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    
    public MathUtilsModTest()
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
    public void modIntCasual()
    {
        assertEquals( 3, mod( 8, 5 ) );
        assertEquals( 3, mod( 8, -5 ) );
        assertEquals( 2, mod( -8, 5 ) );
        assertEquals( 2, mod( -8, -5 ) );
        assertEquals( NOT_FOUND, mod( 8, 0 ) );
    }

    @Test( timeout = 5000L )
    public void modIntRandom()
    {
        for ( int i = 0; i < 500; i++ )
        {
            int mod = RANDOM.nextInt();
            for ( int j = 0; j < 500; j++ )
            {
                int v = RANDOM.nextInt( Integer.MAX_VALUE );
                if ( mod == 0 ) assertEquals( NOT_FOUND, mod( v, mod ) );
                else
                {
                    assertEquals( v % mod, mod( v, mod ) );
                    assertEquals( v % mod, mod( v, -mod ) );
                    assertEquals( BigInteger.valueOf( v ).mod( BigInteger.valueOf( mod ).abs() ).intValue(), mod( v, mod ) );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modIntSpecial()
    {
        for ( int i : INTS )
        {
            for ( int j : INTS )
            {
                if ( j == 0 ) assertEquals( NOT_FOUND, mod( i, j ) );
                else
                {
                    if ( i >= 0 )
                    {
                        assertEquals( i % j, mod( i, j ) );
                        assertEquals( i % j, mod( i, -j ) );
                    }
                    if ( i < Integer.MAX_VALUE && j < Integer.MAX_VALUE ) assertEquals( mod( mod( i, j ) + 1, j ), mod( i + 1, j ) );
                    if ( Integer.MIN_VALUE / 2 < i && i <= Integer.MAX_VALUE / 2 && Integer.MIN_VALUE / 2 < j && j <= Integer.MAX_VALUE / 2 ) assertEquals( i + " (mod " + j + ") = " + mod( i, j ), mod( mod( i, j ) * 2, j ), mod( i * 2, j ) );
                    assertEquals( mod( i, j ), mod( i % j, j ) );
                    assertEquals( mod( i, j ), mod( i, j ) % j );
                    assertEquals( mod( i, j ), mod( i, -j ) );
                    assertEquals( BigInteger.valueOf( i ).mod( BigInteger.valueOf( j ).abs() ).intValue(), mod( i, j ) );
                    assertTrue( mod( i, j ) >= 0 && mod( i, j ) <= Math.abs( j ) - 1 );
                    if ( j == 1 ) assertEquals( 0, mod( i, j ) );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modLongCasual()
    {
        assertEquals( 3L, mod( 8L, 5L ) );
        assertEquals( 3L, mod( 8L, -5L ) );
        assertEquals( 2L, mod( -8L, 5L ) );
        assertEquals( 2L, mod( -8L, -5L ) );
        assertEquals( NOT_FOUND, mod( 8L, 0L ) );
    }

    @Test( timeout = 5000L )
    public void modLongRandom()
    {
        for ( int i = 0; i < 500; i++ )
        {
            long mod = RANDOM.nextLong();
            for ( int j = 0; j < 500; j++ )
            {
                long v = RANDOM.nextLong();
                if ( v < 0L )
                {
                    if ( v == Long.MIN_VALUE ) continue;
                    v = -v;
                }
                if ( mod == 0L ) assertEquals( NOT_FOUND, mod( v, mod ) );
                else
                {
                    assertEquals( v % mod, mod( v, mod ) );
                    assertEquals( v % mod, mod( v, -mod ) );
                    assertEquals( BigInteger.valueOf( v ).mod( BigInteger.valueOf( mod ).abs() ).longValue(), mod( v, mod ) );
                }
                int iv;
                int imod;
                try
                {
                    iv = Math.toIntExact( v );
                    imod = Math.toIntExact( mod );
                }
                catch ( ArithmeticException e )
                {
                    continue;
                }
                assertEquals( mod( iv, imod ), mod( v, mod ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void modLongSpecial()
    {
        for ( long i : LONGS )
        {
            for ( long j : LONGS )
            {
                if ( j == 0L ) assertEquals( NOT_FOUND, mod( i, j ) );
                else
                {
                    if ( i >= 0L )
                    {
                        assertEquals( i % j, mod( i, j ) );
                        assertEquals( i % j, mod( i, -j ) );
                    }
                    if ( i < Long.MAX_VALUE && j < Long.MAX_VALUE) assertEquals( mod( mod( i, j ) + 1, j ), mod( i + 1, j ) );
                    if ( Long.MIN_VALUE / 2 < i && i <= Long.MAX_VALUE / 2 && Long.MIN_VALUE / 2 < j && j <= Long.MAX_VALUE / 2 ) assertEquals( mod( mod( i, j ) * 2, j ), mod( i * 2, j ) );
                    assertEquals( mod( i, j ), mod( i % j, j ) );
                    assertEquals( mod( i, j ), mod( i, j ) % j );
                    assertEquals( mod( i, j ), mod( i, -j ) );
                    assertEquals( BigInteger.valueOf( i ).mod( BigInteger.valueOf( j ).abs() ).longValue(), mod( i, j ) );
                    assertTrue( mod( i, j ) >= 0 && mod( i, j ) <= Math.abs( j ) - 1 );
                    if ( j == 1L ) assertEquals( 0L, mod( i, j ) );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modsIntCasual()
    {
        assertEquals( -2, mods( 8, 5 ) );
        assertEquals( -1, mods( 9, 5 ) );
        assertEquals( 0, mods( 10, 5 ) );
        assertEquals( 1, mods( 11, 5 ) );
        assertEquals( 2, mods( 12, 5 ) );
        assertEquals( -2, mods( 13, 5 ) );
        assertEquals( -2, mods( 8, -5 ) );
        assertEquals( 2, mods( -8, 5 ) );
        assertEquals( 2, mods( -8, -5 ) );
        assertEquals( -2, mods( 12, 7 ) );
        assertEquals( 2, mods( 8, 6 ) );
        assertEquals( 3, mods( 9, 6 ) );
        assertEquals( -2, mods( 10, 6 ) );
        assertEquals( -1, mods( 11, 6 ) );
        assertEquals( 0, mods( 12, 6 ) );
        assertEquals( 1, mods( 13, 6 ) );
        assertEquals( 2, mods( 14, 6 ) );
        assertEquals( 2, mods( 8, -6 ) );
        assertEquals( -2, mods( -8, 6 ) );
        assertEquals( -2, mods( -8, -6 ) );
        assertEquals( NOT_FOUND, mods( 8, 0 ) );
    }

    @Test( timeout = 5000L )
    public void modsIntSpecial()
    {
        for ( int i : INTS )
        {
            for ( int j : INTS )
            {
                if ( j == 0 ) assertEquals( NOT_FOUND, mods( i, j ) );
                else
                {
                    if ( i < Integer.MAX_VALUE && j < Integer.MAX_VALUE ) assertEquals( mods( mods( i, j ) + 1, j ), mods( i + 1, j ) );
                    if ( Integer.MIN_VALUE / 2 < i && i <= Integer.MAX_VALUE / 2 && Integer.MIN_VALUE / 2 < j && j <= Integer.MAX_VALUE / 2 ) assertEquals( mods( mods( i, j ) * 2, j ), mods( i * 2, j ) );
                    assertEquals( mods( i, j ), mods( i % j, j ) );
                    assertEquals( mods( i, j ), mods( i, j ) % j );
                    assertEquals( mods( i, j ), mods( i, -j ) );
                    assertEquals( mods( i, j ), mods( mod( i, j ), j ) );
                    assertEquals( mod( i, j ), mod( mods( i, j ), j ) );
                    assertTrue( i + " (mods " + j + ") = " + mods( i, j ), mods( i, j ) >= -( Math.abs( j / 2 ) - 1 + ( j & 1 ) ) && mods( i, j ) <= Math.abs( j / 2 ) );
                    if ( j == 1 ) assertEquals( 0, mods( i, j ) );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modsLongCasual()
    {
        assertEquals( -2L, mods( 8L, 5L ) );
        assertEquals( -1L, mods( 9L, 5L ) );
        assertEquals( 0L, mods( 10L, 5L ) );
        assertEquals( 1L, mods( 11L, 5L ) );
        assertEquals( 2L, mods( 12L, 5L ) );
        assertEquals( -2L, mods( 13L, 5L ) );
        assertEquals( -2L, mods( 8L, -5L ) );
        assertEquals( 2L, mods( -8L, 5L ) );
        assertEquals( 2L, mods( -8L, -5L ) );
        assertEquals( -2L, mods( 12L, 7L ) );
        assertEquals( 2L, mods( 8L, 6L ) );
        assertEquals( 3L, mods( 9L, 6L ) );
        assertEquals( -2L, mods( 10L, 6L ) );
        assertEquals( -1L, mods( 11L, 6L ) );
        assertEquals( 0L, mods( 12L, 6L ) );
        assertEquals( 1L, mods( 13L, 6L ) );
        assertEquals( 2L, mods( 14L, 6L ) );
        assertEquals( 2L, mods( 8L, -6L ) );
        assertEquals( -2L, mods( -8L, 6L ) );
        assertEquals( -2L, mods( -8L, -6L ) );
        assertEquals( NOT_FOUND, mods( 8L, 0L ) );
    }

    @Test( timeout = 5000L )
    public void modsLongSpecial()
    {
        for ( long i : LONGS )
        {
            for ( long j : LONGS )
            {
                if ( j == 0L ) assertEquals( NOT_FOUND, mods( i, j ) );
                else
                {
                    if ( i < Long.MAX_VALUE && j < Long.MAX_VALUE ) assertEquals( mods( mods( i, j ) + 1, j ), mods( i + 1, j ) );
                    if ( Long.MIN_VALUE / 2 < i && i <= Long.MAX_VALUE / 2 && Long.MIN_VALUE / 2 < j && j <= Long.MAX_VALUE / 2 ) assertEquals( mods( mods( i, j ) * 2, j ), mods( i * 2, j ) );
                    assertEquals( mods( i, j ), mods( i % j, j ) );
                    assertEquals( mods( i, j ), mods( i, j ) % j );
                    assertEquals( mods( i, j ), mods( i, -j ) );
                    assertEquals( mods( i, j ), mods( mod( i, j ), j ) );
                    assertEquals( mod( i, j ), mod( mods( i, j ), j ) );
                    assertTrue( i + " (mods " + j + ") = " + mods( i, j ), mods( i, j ) >= -( Math.abs( j / 2 ) - 1 + ( j & 1 ) ) && mods( i, j ) <= Math.abs( j / 2 ) );
                    if ( j == 1L ) assertEquals( 0L, mods( i, j ) );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modAddIntCasual()
    {
        assertEquals( 2, modAdd( 8, 5, 11 ) );
        assertEquals( 1, modAdd( 9, 5, 13 ) );
        assertEquals( 0, modAdd( 10, 5, 15 ) );
        assertEquals( 1, modAdd( 11, 5, 3 ) );
        assertEquals( NOT_FOUND, modAdd( 8, 7, 0 ) );
    }
    
    @Test( timeout = 15000L )
    public void modAddIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int b : values )
            {
                for ( int m : values )
                {
                    int value = modAdd( a, b, m );
                    if ( m == 0 ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modAdd( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modAdd( a, b % m, m ) );
                        assertEquals( value, modAdd( b, a, m ) );
                        assertEquals( BigInteger.valueOf( a ).add( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).intValue(), value );
                        assertTrue( 0 <= value && value <= Math.abs( m ) - 1 );
                        if ( m == 1 ) assertEquals( 0, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modAddLongCasual()
    {
        assertEquals( 2L, modAdd( 8L, 5L, 11L ) );
        assertEquals( 1L, modAdd( 9L, 5L, 13L ) );
        assertEquals( 0L, modAdd( 10L, 5L, 15L ) );
        assertEquals( 1L, modAdd( 11L, 5L, 3L ) );
        assertEquals( NOT_FOUND, modAdd( 8L, 7L, 0L ) );
    }
    
    @Test( timeout = 15000L )
    public void modAddLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long b : values )
            {
                for ( long m : values )
                {
                    long value = modAdd( a, b, m );
                    if ( m == 0L ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modAdd( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modAdd( a, b % m, m ) );
                        assertEquals( value, modAdd( b, a, m ) );
                        assertEquals( BigInteger.valueOf( a ).add( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).longValue(), value );
                        assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                        if ( m == 1L ) assertEquals( 0L, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modSubtractIntCasual()
    {
        assertEquals( 3, modSubtract( 8, 5, 11 ) );
        assertEquals( 4, modSubtract( 9, 5, 13 ) );
        assertEquals( 10, modSubtract( 5, 10, 15 ) );
        assertEquals( 0, modSubtract( 11, 5, 3 ) );
        assertEquals( NOT_FOUND, modSubtract( 8, 7, 0 ) );
    }
    
    @Test( timeout = 15000L )
    public void modSubtractIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int b : values )
            {
                for ( int m : values )
                {
                    int value = modSubtract( a, b, m );
                    if ( m == 0 ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modSubtract( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modSubtract( a, b % m, m ) );
                        assertEquals( value, modSubtract( a % m, b, m ) );
                        assertEquals( mod( a, m ), modAdd( value, b, m ) );
                        assertEquals( BigInteger.valueOf( a ).subtract( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).intValue(), value );
                        assertTrue( 0 <= value && value <= Math.abs( m ) - 1 );
                        if ( m == 1 ) assertEquals( 0, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modSubtractLongCasual()
    {
        assertEquals( 3L, modSubtract( 8L, 5L, 11L ) );
        assertEquals( 4L, modSubtract( 9L, 5L, 13L ) );
        assertEquals( 10L, modSubtract( 5L, 10L, 15L ) );
        assertEquals( 0L, modSubtract( 11L, 5L, 3L ) );
        assertEquals( NOT_FOUND, modSubtract( 8L, 7L, 0L ) );
    }
    
    @Test( timeout = 15000L )
    public void modSubtractLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long b : values )
            {
                for ( long m : values )
                {
                    long value = modSubtract( a, b, m );
                    if ( m == 0L ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modSubtract( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modSubtract( a, b % m, m ) );
                        assertEquals( value, modSubtract( a % m, b, m ) );
                        assertEquals( mod( a, m ), modAdd( value, b, m ) );
                        assertEquals( BigInteger.valueOf( a ).subtract( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).longValue(), value );
                        assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                        if ( m == 1L ) assertEquals( 0L, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modMultiplyIntCasual()
    {
        assertEquals( 7, modMultiply( 8, 5, 11 ) );
        assertEquals( 6, modMultiply( 9, 5, 13 ) );
        assertEquals( 5, modMultiply( 5, 10, 15 ) );
        assertEquals( 1, modMultiply( 11, 5, 3 ) );
        assertEquals( NOT_FOUND, modMultiply( 8, 7, 0 ) );
    }
    
    @Test( timeout = 15000L )
    public void modMultiplyIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int b : values )
            {
                for ( int m : values )
                {
                    int value = modMultiply( a, b, m );
                    if ( m == 0 ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modMultiply( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modMultiply( a, b % m, m ) );
                        assertEquals( value, modMultiply( b, a, m ) );
                        assertEquals( BigInteger.valueOf( a ).multiply( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).intValue(), value );
                        assertTrue( 0 <= value && value <= Math.abs( m ) - 1 );
                        if ( m == 1 ) assertEquals( 0, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modMultiplyLongCasual()
    {
        assertEquals( 7L, modMultiply( 8L, 5L, 11L ) );
        assertEquals( 6L, modMultiply( 9L, 5L, 13L ) );
        assertEquals( 5L, modMultiply( 5L, 10L, 15L ) );
        assertEquals( 1L, modMultiply( 11L, 5L, 3L ) );
        assertEquals( NOT_FOUND, modMultiply( 8L, 7L, 0L ) );
    }
    
    @Test( timeout = 15000L )
    public void modMultiplyLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long b : values )
            {
                for ( long m : values )
                {
                    long value = modMultiply( a, b, m );
                    if ( m == 0L ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modMultiply( a, b, -m ) );
                        assertEquals( value, value % m );
                        assertEquals( value, modMultiply( a, b % m, m ) );
                        assertEquals( value, modMultiply( b, a, m ) );
                        assertEquals( BigInteger.valueOf( a ).multiply( BigInteger.valueOf( b ) ).mod( BigInteger.valueOf( m ).abs() ).longValue(), value );
                        assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                        if ( m == 1L ) assertEquals( 0L, value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modDivideIntCasual()
    {
        assertArrayEquals( new int[]{ 3, 4, 2 }, modDivide( 6, 2, 8 ) );
        assertArrayEquals( new int[]{ 6, 11, 1 }, modDivide( 8, 5, 11 ) );
        assertArrayEquals( new int[]{ 7, 13, 1 }, modDivide( 9, 5, 13 ) );
        assertArrayEquals( new int[]{ 2, 3, 5 }, modDivide( 5, 10, 15 ) );
        assertArrayEquals( new int[]{ 1, 3, 1 }, modDivide( 11, 5, 3 ) );
        assertArrayEquals( new int[]{ NOT_FOUND, 0, 0 }, modDivide( 8, 7, 0 ) );
    }
    
    @Test( timeout = 120000L )
    public void modDivideIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int b : values )
            {
                for ( int m : values )
                {
                    int solution[] = modDivide( a, b, m );
                    int value = solution[ 0 ];
                    int increment = solution[ 1 ];
                    if ( m == 0 ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertArrayEquals( solution, modDivide( a, b, -m ) );
                        assertArrayEquals( solution, modDivide( a, b % m, m ) );
                        assertArrayEquals( solution, modDivide( a % m, b, m ) );
                        assertArrayEquals( "( " + a + " / " + b + " )( mod " + m + " )", modDivideCheck( a, b, m ), solution );
                        Set< Integer > expected = Math.abs( ( long )m ) < 1000000L ? checkDivision( a, b, m ) : null;
                        if ( value == NOT_FOUND )
                        {
                            if ( expected != null ) assertEquals( "( " + a + " / " + b + " )( mod " + m + " )", 0, expected.size() );
                        }
                        else
                        {
                            for ( int k = Math.max( -solution[ 2 ], - 1000000 ); k < 0; k++ )
                            {
                                if ( value != value % m ) fail( "( " + a + " / " + b + " )( mod " + m + " ) = " + value + " != " + ( value % m ) );
                                if ( expected != null ) assertTrue( "( " + a + " / " + b + " )( mod " + m + " )", expected.remove( value ) );
                                int mod = ( int )( ( long )value * b % m );
                                if ( mod < 0 )
                                {
                                    if ( m < 0 ) mod -= m;
                                    else mod += m;
                                }
                                int expectedMod = a % m;
                                if ( expectedMod < 0 )
                                {
                                    if ( m < 0 ) expectedMod -= m;
                                    else expectedMod += m;
                                }
                                if ( expectedMod != mod ) fail( "( " + a + " / " + b + " )( mod " + m + " ) = " + value + ", ( " + value + " * b )( mod " + m + " ) = " + mod + ", expected: " + expectedMod );
                                if ( 0 > value || m >= 0 && value > m - 1 || m < 0 && value < m + 1 ) fail( "( " + a + " / " + b + " )( mod " + m + " ) = " + value + " out of range" );
                                if ( m == 1 ) assertEquals( 0, value );
                                if ( a == 1 ) assertEquals( value, modInverse( b, m ) );
                                value += increment;
                            }
                        }
                        if ( expected != null ) assertEquals( 0, expected.size() );
                    }
                }
            }
        }
    }
    
    /**
     * Exponential-time (on bit-length of m) algorithm of finding division of a by b mod m.
     * @param a the dividend
     * @param b the divisor
     * @param m the modulus
     * @return a set of solutions x such that ( b * x )( mod m ) = a( mod m )
     */
    public static Set< Integer > checkDivision( int a, int b, int m )
    {
        Set< Integer > ret = new HashSet<>( m == 0 ? 0 : gcd( b, m ) );
        if ( m == 0 ) return ret;
        m = Math.abs( m );
        a %= m;
        b %= m;
        if ( a < 0 ) a += m;
        if ( b < 0 ) b += m;
        if ( b == 0 && a != 0 ) return ret;
        if ( m == Integer.MIN_VALUE )
        {
            for ( int c = 0; c >= 0; c++ ) if ( ( b * c ) % m == a ) ret.add( c );
        }
        else for ( int c = 0; c < m; c++ ) if ( ( ( long )b * c ) % m == a ) ret.add( c );
        return ret;
    }
    
    /**
     * Straightforward algorithm of solving ( b * x )( mod m ) = a( mod m ).
     * @param a the dividend
     * @param b the divisor
     * @param m the modulus
     * @return a tuple ( first solution x<sub>0</sub>, increment, quantity ),
     * which produces a set of solutions of equation ( x * b )( mod m ) = a
     * in the form x = x<sub>0</sub> + increment * k, where k is integer, 0 &le; k &lt; quantity.<br>
     * If there's no solution, a tuple ( NOT_FOUND, 0, 0 ) is returned.
     */
    public static int[] modDivideCheck( int a, int b, int m )
    {
        if ( m == 0 ) return new int[]{ NOT_FOUND, 0, 0 };
        int gcd = gcd( b, m );
        if ( a % gcd != 0 ) return new int[]{ NOT_FOUND, 0, 0 };
        a /= gcd;
        b /= gcd;
        m /= gcd;
        int inverse = modInverse( b, m );
        if ( inverse == NOT_FOUND ) return new int[]{ NOT_FOUND, 0, 0 };
        int ret = modMultiply( a, inverse, m );
        return new int[]{ ret, Math.abs( m ), gcd };
    }
    
    /**
     * Straightforward algorithm of solving ( b * x )( mod m ) = a( mod m ).
     * @param a the dividend
     * @param b the divisor
     * @param m the modulus
     * @return a tuple ( first solution x<sub>0</sub>, increment, quantity ),
     * which produces a set of solutions of equation ( x * b )( mod m ) = a
     * in the form x = x<sub>0</sub> + increment * k, where k is integer, 0 &le; k &lt; quantity.<br>
     * If there's no solution, a tuple ( NOT_FOUND, 0, 0 ) is returned.
     */
    public static long[] modDivideCheck( long a, long b, long m )
    {
        if ( m == 0L ) return new long[]{ NOT_FOUND, 0L, 0L };
        long gcd = gcd( b, m );
        if ( a % gcd != 0L ) return new long[]{ NOT_FOUND, 0L, 0L };
        a /= gcd;
        b /= gcd;
        m /= gcd;
        long inverse = modInverse( b, m );
        if ( inverse == NOT_FOUND ) return new long[]{ NOT_FOUND, 0L, 0L };
        long ret = modMultiply( a, inverse, m );
        return new long[]{ ret, Math.abs( m ), gcd };
    }

    @Test( timeout = 5000L )
    public void modDivideLongCasual()
    {
        assertArrayEquals( new long[]{ 3L, 4L, 2L }, modDivide( 6L, 2L, 8L ) );
        assertArrayEquals( new long[]{ 6L, 11L, 1L }, modDivide( 8L, 5L, 11L ) );
        assertArrayEquals( new long[]{ 7L, 13L, 1L }, modDivide( 9L, 5L, 13L ) );
        assertArrayEquals( new long[]{ 2L, 3L, 5L }, modDivide( 5L, 10L, 15L ) );
        assertArrayEquals( new long[]{ 1L, 3L, 1L }, modDivide( 11L, 5L, 3L ) );
        assertArrayEquals( new long[]{ NOT_FOUND, 0L, 0L }, modDivide( 8L, 7L, 0L ) );
    }
    
    @Test( timeout = 600000L )
    public void modDivideLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long b : values )
            {
                for ( long m : values )
                {
                    long solution[] = modDivide( a, b, m );
                    long value = solution[ 0 ];
                    long increment = solution[ 1 ];
                    if ( m == 0L ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertArrayEquals( solution, modDivide( a, b, -m ) );
                        assertArrayEquals( solution, modDivide( a, b % m, m ) );
                        assertArrayEquals( solution, modDivide( a % m, b, m ) );
                        assertArrayEquals( "( " + a + " / " + b + " )( mod " + m + " )", modDivideCheck( a, b, m ), solution );
                        if ( value == NOT_FOUND )
                        {
                            if ( ( int )m == m ) assertArrayEquals( "( " + a + " / " + b + " )( mod " + m + " )", new int[]{ NOT_FOUND, 0, 0 }, modDivide( ( int )( a % m ), ( int )( b % m ), ( int )m ) );
                        }
                        else
                        {
                            for ( long k = Math.max( -solution[ 2 ], -1000000L ); k < 0L; k++ )
                            {
                                assertEquals( value, value % m );
                                assertEquals( mod( a, m ), modMultiply( value, b, m ) );
                                assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                                if ( m == 1L ) assertEquals( 0L, value );
                                if ( a == 1L ) assertEquals( value, modInverse( b, m ) );
                                value += increment;
                            }
                        }
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modInverseIntCasual()
    {
        assertEquals( 7, modInverse( 8, 11 ) );
        assertEquals( 3, modInverse( 9, 13 ) );
        assertEquals( NOT_FOUND, modInverse( 5, 15 ) );
        assertEquals( 2, modInverse( 11, 3 ) );
        assertEquals( NOT_FOUND, modInverse( 8, 0 ) );
    }
    
    @Test( timeout = 5000L )
    public void modInverseIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int m : values )
            {
                int value = modInverse( a, m );
                if ( m == 0 ) assertEquals( NOT_FOUND, value );
                else
                {
                    assertEquals( value, modInverse( a, -m ) );
                    assertEquals( value, modInverse( a % m, m ) );
                    int expected;
                    try
                    {
                        expected = BigInteger.valueOf( a ).modInverse( BigInteger.valueOf( m ).abs() ).intValue();
                    }
                    catch ( ArithmeticException e )
                    {
                        expected = NOT_FOUND;
                    }
                    assertEquals( expected, value );
                    if ( value == NOT_FOUND )
                    {
                        if ( a % m != 0 ) assertFalse( PrimeUtils.isPrime( m ) );
                        assertTrue( gcd( a, m ) != 1 );
                    }
                    else
                    {
                        if ( value == 0 ) assertFalse( PrimeUtils.isPrime( m ) );
                        assertEquals( 1, gcd( a, m ) );
                        assertEquals( value, value % m );
                        assertEquals( 1 % m, modMultiply( value, a, m ) );
                        assertTrue( 0 <= value && value <= Math.abs( m ) - 1 );
                    }
                    if ( m == 1 ) assertEquals( 0, value );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modInverseLongCasual()
    {
        assertEquals( 7L, modInverse( 8L, 11L ) );
        assertEquals( 3L, modInverse( 9L, 13L ) );
        assertEquals( NOT_FOUND, modInverse( 5L, 15L ) );
        assertEquals( 2L, modInverse( 11L, 3L ) );
        assertEquals( NOT_FOUND, modInverse( 8L, 0L ) );
    }
    
    @Test( timeout = 5000L )
    public void modInverseLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long m : values )
            {
                long value = modInverse( a, m );
                if ( m == 0L ) assertEquals( NOT_FOUND, value );
                else
                {
                    assertEquals( value, modInverse( a, -m ) );
                    assertEquals( value, modInverse( a % m, m ) );
                    long expected;
                    try
                    {
                        expected = BigInteger.valueOf( a ).modInverse( BigInteger.valueOf( m ).abs() ).longValue();
                    }
                    catch ( ArithmeticException e )
                    {
                        expected = NOT_FOUND;
                    }
                    assertEquals( expected, value );
                    if ( value == NOT_FOUND )
                    {
                        if ( a % m != 0L ) assertFalse( PrimeUtils.isPrime( m ) );
                        assertTrue( gcd( a, m ) != 1L );
                    }
                    else
                    {
                        if ( value == 0L ) assertFalse( PrimeUtils.isPrime( m ) );
                        assertEquals( 1L, gcd( a, m ) );
                        assertEquals( value, value % m );
                        assertEquals( 1L % m, modMultiply( value, a, m ) );
                        assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                    }
                    if ( m == 1L ) assertEquals( 0L, value );
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modPowIntCasual()
    {
        assertEquals( 9, modPow( 8, 2, 11 ) );
        assertEquals( 1, modPow( 9, 3, 13 ) );
        assertEquals( 10, modPow( 5, 4, 15 ) );
        assertEquals( 2, modPow( 11, 5, 3 ) );
        assertEquals( NOT_FOUND, modPow( 8, 8, 0 ) );
    }
    
    @Test( timeout = 60000L )
    public void modPowIntRandom()
    {
        int values[] = TestUtils.getInts();
        for ( int a : values )
        {
            for ( int b : values )
            {
                for ( int m : values )
                {
                    int value = modPow( a, b, m );
                    if ( m == 0 ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modPow( a, b, -m ) );
                        assertEquals( value, modPow( a % m, b, m ) );
                        int expected;
                        try
                        {
                            expected = BigInteger.valueOf( a ).modPow( BigInteger.valueOf( b ), BigInteger.valueOf( m ).abs() ).intValue();
                        }
                        catch ( ArithmeticException e )
                        {
                            expected = NOT_FOUND;
                        }
                        assertEquals( "( " + a + " ^ " + b + " )( mod " + m + " )", expected, value );
                        if ( value == NOT_FOUND )
                        {
                            if ( a % m != 0 ) assertFalse( PrimeUtils.isPrime( m ) );
                            assertTrue( gcd( a, m ) != 1 );
                        }
                        else
                        {
                            if ( b < 0 && b > Integer.MIN_VALUE )
                            {
                                assertEquals( "( " + a + " ^ " + b + " )( mod " + m + " )", 1 % m, modMultiply( value, modPow( a, -b, m ), m ) );
                                assertEquals( value, modInverse( modPow( a, -b, m ), m ) );
                                assertEquals( value, modPow( modInverse( a, m ), -b, m ) );
                            }
                            assertEquals( value, value % m );
                            assertTrue( 0 <= value && value <= Math.abs( m ) - 1 );
                        }
                        if ( m == 1 ) assertEquals( 0, value );
                        if ( b == -1 ) assertEquals( modInverse( a, m ), value );
                        if ( b == 0 ) assertEquals( 1 % m, value );
                        if ( b == 1 ) assertEquals( mod( a, m ), value );
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void modPowLongCasual()
    {
        assertEquals( 9L, modPow( 8L, 2L, 11L ) );
        assertEquals( 1L, modPow( 9L, 3L, 13L ) );
        assertEquals( 10L, modPow( 5L, 4L, 15L ) );
        assertEquals( 2L, modPow( 11L, 5L, 3L ) );
        assertEquals( NOT_FOUND, modPow( 8L, 8L, 0L ) );
    }
    
    @Test( timeout = 120000L )
    public void modPowLongRandom()
    {
        long values[] = TestUtils.getLongs();
        for ( long a : values )
        {
            for ( long b : values )
            {
                for ( long m : values )
                {
                    long value = modPow( a, b, m );
                    if ( m == 0L ) assertEquals( NOT_FOUND, value );
                    else
                    {
                        assertEquals( value, modPow( a, b, -m ) );
                        assertEquals( value, modPow( a % m, b, m ) );
                        long expected;
                        try
                        {
                            expected = BigInteger.valueOf( a ).modPow( BigInteger.valueOf( b ), BigInteger.valueOf( m ).abs() ).longValue();
                        }
                        catch ( ArithmeticException e )
                        {
                            expected = NOT_FOUND;
                        }
                        assertEquals( "( " + a + " ^ " + b + " )( mod " + m + " )", expected, value );
                        if ( value == NOT_FOUND )
                        {
                            if ( a % m != 0L ) assertFalse( PrimeUtils.isPrime( m ) );
                            assertTrue( gcd( a, m ) != 1L );
                        }
                        else
                        {
                            if ( b < 0L && b > Long.MIN_VALUE )
                            {
                                assertEquals( "( " + a + " ^ " + b + " )( mod " + m + " )", 1L % m, modMultiply( value, modPow( a, -b, m ), m ) );
                                assertEquals( value, modInverse( modPow( a, -b, m ), m ) );
                                assertEquals( value, modPow( modInverse( a, m ), -b, m ) );
                            }
                            assertEquals( value, value % m );
                            assertTrue( 0L <= value && value <= Math.abs( m ) - 1L );
                        }
                        if ( m == 1L ) assertEquals( 0L, value );
                        if ( b == -1L ) assertEquals( modInverse( a, m ), value );
                        if ( b == 0L ) assertEquals( 1L % m, value );
                        if ( b == 1L ) assertEquals( mod( a, m ), value );
                    }
                }
            }
        }
    }
}