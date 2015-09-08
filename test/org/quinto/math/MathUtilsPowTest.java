package org.quinto.math;

import java.math.BigDecimal;
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
import static org.quinto.math.MathUtils.pow;
import static org.quinto.math.MathUtils.pow10;
import static org.quinto.math.MathUtils.powExact;

public class MathUtilsPowTest
{
    private static final double DELTA = 0.0001;
    private static final double EXACT = -1.0;
    private static final Random RANDOM = new Random( System.nanoTime() );
    private static final double DOUBLES[] = new double[]{ Double.NEGATIVE_INFINITY, -0.0, Double.NaN, 0.0, Double.POSITIVE_INFINITY,
                                                          Long.MIN_VALUE, Integer.MIN_VALUE, Short.MIN_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE,
                                                          Double.MIN_VALUE, Double.MIN_NORMAL, Float.MIN_VALUE, Float.MIN_NORMAL,
                                                          0.5, 0.1, 0.2, 0.8, 1.1, 1.2, 1.5, 1.8, 2.0, 1.2, 2.2, 2.5, 2.8, 33.0, 33.1, 33.5, 33.8,
                                                          -0.5, -0.1, -0.2, -0.8, -1.1, -1.2, -1.5, -1.8, -2.0, -1.2, -2.2, -2.5, -2.8, -33.0, -33.1, -33.5, -33.8 };
    private static final int INTS[] = new int[]{ Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    private static final long LONGS[] = new long[]{ Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2, Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    
    public MathUtilsPowTest()
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
    public void pow10Casual()
    {
        assertEquals( 1.0, pow10( 0 ), EXACT );
        assertEquals( 10.0, pow10( 1 ), EXACT );
        assertEquals( 100.0, pow10( 2 ), EXACT );
        assertEquals( 1000.0, pow10( 3 ), EXACT );
        assertEquals( 1e5, pow10( 5 ), EXACT );
        assertEquals( 1e23, pow10( 23 ), EXACT );
        assertEquals( 1e105, pow10( 105 ), EXACT );
        assertEquals( 0.1, pow10( -1 ), EXACT );
        assertEquals( 0.01, pow10( -2 ), EXACT );
        assertEquals( 0.001, pow10( -3 ), EXACT );
        assertEquals( 1e-5, pow10( -5 ), EXACT );
        assertEquals( 1e-23, pow10( -23 ), EXACT );
        assertEquals( 1e-105, pow10( -105 ), EXACT );
    }

    @Test( timeout = 5000L )
    public void pow10Ten()
    {
        for ( int i = -325; i < 311; i++ ) assertEquals( Double.parseDouble( "1e" + i ), pow10( i ), EXACT );
        for ( int i = -325; i < 311; i++ ) assertEquals( BigDecimal.ONE.scaleByPowerOfTen( i ).doubleValue(), pow10( i ), EXACT );
    }

    @Test( timeout = 5000L )
    public void pow10Special()
    {
        assertEquals( 0.0, pow10( Integer.MIN_VALUE ), EXACT );
        assertEquals( 0.0, pow10( -5001 ), EXACT );
        assertEquals( 0.1, pow10( -1 ), EXACT );
        assertEquals( 1.0, pow10( 0 ), EXACT );
        assertEquals( 10.0, pow10( 1 ), EXACT );
        assertEquals( Double.POSITIVE_INFINITY, pow10( 5000 ), EXACT );
        assertEquals( Double.POSITIVE_INFINITY, pow10( Integer.MAX_VALUE ), EXACT );
    }

    @Test( timeout = 5000L )
    public void powDoubleIntTen()
    {
        for ( int i = -325; i < 311; i++ ) assertEquals( Double.parseDouble( "1e" + i ), pow( 10.0, i ), EXACT );
        for ( int i = -325; i < 311; i++ ) assertEquals( BigDecimal.ONE.scaleByPowerOfTen( i ).doubleValue(), pow( 10.0, i ), EXACT );
    }

    @Test( timeout = 5000L )
    public void powDoubleIntRandom()
    {
        for ( int k = 0; k < 500; k++ )
        {
            double d = RANDOM.nextDouble() * 400.0 - 150.0;
            for ( int i = 0; i < 30; i++ )
            {
                assertEquals( Math.pow( d, i ), pow( d, i ), Math.max( Math.pow( Math.abs( d ), i ), 1.0 ) * DELTA );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void powDoubleIntSpecial()
    {
        // Special cases from Math.pow javadoc:
        // If the second argument is positive or negative zero, then the result is 1.0.
        for ( double d : DOUBLES ) assertEquals( 1.0, pow( d, 0 ), EXACT );
        // If the second argument is 1.0, then the result is the same as the first argument.
        for ( double d : DOUBLES ) assertEquals( d, pow( d, 1 ), EXACT );
        // If the second argument is NaN, then the result is NaN. <- Impossible with int.
        // If the first argument is NaN and the second argument is nonzero, then the result is NaN.
        for ( int i : INTS ) if ( i != 0 ) assertEquals( Double.NaN, pow( Double.NaN, i ), EXACT );
        // If the absolute value of the first argument is greater than 1 and the second argument is positive infinity, or
        // the absolute value of the first argument is less than 1 and the second argument is negative infinity, then the result is positive infinity.
        for ( double d : DOUBLES ) if ( Math.abs( d ) > 1.0 ) assertEquals( "Math.pow: " + Math.pow( d, Double.POSITIVE_INFINITY ), Double.POSITIVE_INFINITY, pow( d, Integer.MAX_VALUE - 1 ), EXACT );
        for ( double d : DOUBLES ) if ( Math.abs( d ) < 1.0 ) assertEquals( Double.POSITIVE_INFINITY, pow( d, Integer.MIN_VALUE ), EXACT );
        // If the absolute value of the first argument is greater than 1 and the second argument is negative infinity, or
        // the absolute value of the first argument is less than 1 and the second argument is positive infinity, then the result is positive zero.
        for ( double d : DOUBLES ) if ( Math.abs( d ) > 1.0 ) assertEquals( 0.0, pow( d, Integer.MIN_VALUE ), EXACT );
        for ( double d : DOUBLES ) if ( Math.abs( d ) < 1.0 ) assertEquals( 0.0, pow( d, Integer.MAX_VALUE - 1 ), EXACT );
        // Note: Integer.MAX_VALUE isn't actually an infinity, so its parity affects the sign of resulting zero.
        for ( double d : DOUBLES ) if ( Math.abs( d ) < 1.0 ) assertTrue( pow( d, Integer.MAX_VALUE ) == 0.0 );
        // If the absolute value of the first argument equals 1 and the second argument is infinite, then the result is NaN. <- Impossible with int.
        // If the first argument is positive zero and the second argument is greater than zero, or
        // the first argument is positive infinity and the second argument is less than zero, then the result is positive zero.
        for ( int i : INTS ) if ( i > 0 ) assertEquals( 0.0, pow( 0.0, i ), EXACT );
        for ( int i : INTS ) if ( i < 0 ) assertEquals( 0.0, pow( Double.POSITIVE_INFINITY, i ), EXACT );
        // If the first argument is positive zero and the second argument is less than zero, or
        // the first argument is positive infinity and the second argument is greater than zero, then the result is positive infinity.
        for ( int i : INTS ) if ( i > 0 ) assertEquals( Double.POSITIVE_INFINITY, pow( Double.POSITIVE_INFINITY, Integer.MAX_VALUE ), EXACT );
        for ( int i : INTS ) if ( i < 0 ) assertEquals( Double.POSITIVE_INFINITY, pow( 0.0, Integer.MIN_VALUE ), EXACT );
        // If the first argument is negative zero and the second argument is greater than zero but not a finite odd integer, or
        // the first argument is negative infinity and the second argument is less than zero but not a finite odd integer, then the result is positive zero.
        for ( int i : INTS ) if ( i > 0 && ( i & 1 ) == 0 ) assertEquals( 0.0, pow( -0.0, i ), EXACT );
        for ( int i : INTS ) if ( i < 0 && ( i & 1 ) == 0 ) assertEquals( 0.0, pow( Double.NEGATIVE_INFINITY, i ), EXACT );
        // If the first argument is negative zero and the second argument is a positive finite odd integer, or
        // the first argument is negative infinity and the second argument is a negative finite odd integer, then the result is negative zero.
        for ( int i : INTS ) if ( i > 0 && ( i & 1 ) == 1 ) assertEquals( -0.0, pow( -0.0, i ), EXACT );
        for ( int i : INTS ) if ( i < 0 && ( i & 1 ) == 1 ) assertEquals( -0.0, pow( Double.NEGATIVE_INFINITY, i ), EXACT );
        // If the first argument is negative zero and the second argument is less than zero but not a finite odd integer, or
        // the first argument is negative infinity and the second argument is greater than zero but not a finite odd integer, then the result is positive infinity.
        for ( int i : INTS ) if ( i > 0 && ( i & 1 ) == 0 ) assertEquals( Double.POSITIVE_INFINITY, pow( Double.NEGATIVE_INFINITY, i ), EXACT );
        for ( int i : INTS ) if ( i < 0 && ( i & 1 ) == 0 ) assertEquals( Double.POSITIVE_INFINITY, pow( -0.0, i ), EXACT );
        // If the first argument is negative zero and the second argument is a negative finite odd integer, or
        // the first argument is negative infinity and the second argument is a positive finite odd integer, then the result is negative infinity.
        for ( int i : INTS ) if ( i > 0 && ( i & 1 ) == 1 ) assertEquals( Double.NEGATIVE_INFINITY, pow( Double.NEGATIVE_INFINITY, i ), EXACT );
        for ( int i : INTS ) if ( i < 0 && ( i & 1 ) == 1 ) assertEquals( Double.NEGATIVE_INFINITY, pow( -0.0, i ), EXACT );
        for ( double d : DOUBLES )
        {
            // If the first argument is finite and less than zero
            if ( d < 0.0 && Double.isFinite( d ) )
            {
                for ( int i : INTS )
                {
                    // if the second argument is a finite even integer, the result is equal to the result of raising the absolute value of the first argument to the power of the second argument
                    if ( ( i & 1 ) == 0 ) assertEquals( pow( -d, i ), pow( d, i ), EXACT );
                    // if the second argument is a finite odd integer, the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument
                    else assertEquals( -pow( -d, i ), pow( d, i ), EXACT );
                    // if the second argument is finite and not an integer, then the result is NaN. <- Impossible with int.
                }
            }
        }
        // If both arguments are integers, then the result is exactly equal to the mathematical result of raising the first argument to the power
        // of the second argument if that result can in fact be represented exactly as a {@code double} value. <- Casual test.
    }
    
    @Test( timeout = 5000L )
    public void powDoubleIntCasual()
    {
        assertEquals( 0.25, pow( 0.5, 2 ), EXACT );
        assertEquals( 1.0, pow( 1.0, 3 ), EXACT );
        assertEquals( 81.0, pow( 3.0, 4 ), EXACT );
        for ( int i : INTS ) for ( double d : DOUBLES ) assertEquals( Math.pow( d, i ), pow( d, i ), Math.min( Math.max( Math.pow( Math.abs( d ), i ), 1.0 ) * DELTA, 10e290 ) );
    }
    
    @Test( timeout = 5000L )
    public void powIntIntRandom()
    {
        BigInteger mod = BigUtils.BI_MAX_INT.add( BigInteger.ONE ).shiftLeft( 1 );
        for ( int i = 0; i < 100; i++ )
        {
            int base = RANDOM.nextInt();
            for ( int j = 0; j < 100; j++ )
            {
                int power = RANDOM.nextInt( Integer.MAX_VALUE );
                BigInteger expected = BigInteger.valueOf( base ).modPow( BigInteger.valueOf( power ), mod );
                assertEquals( i + " ^ " + j, expected.intValue(), pow( base, power ) );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void powIntIntExact()
    {
        BigInteger mod = BigUtils.BI_MAX_INT.add( BigInteger.ONE ).shiftLeft( 1 );
        for ( int i : INTS ) for ( int j : INTS ) if ( j >= 0 )
        {
            BigInteger expected = BigInteger.valueOf( i ).modPow( BigInteger.valueOf( j ), mod );
            assertEquals( i + " ^ " + j, expected.intValue(), pow( i, j ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void powIntIntCasual()
    {
        assertEquals( 3, pow( 3, 1 ) );
        assertEquals( 25, pow( 5, 2 ) );
        assertEquals( 729, pow( 9, 3 ) );
        double upper = Math.log( Integer.MAX_VALUE );
        for ( int i : INTS ) if ( i > -10000 && i < 10000 )
        {
            int up = ( int )( upper / Math.log( Math.abs( i ) ) );
            if ( up == 0 ) up = Integer.MAX_VALUE;
            for ( int j : INTS ) if ( j >= ( i == 0 ? 0 : -up ) && j <= up ) assertEquals( i + " ^ " + j, ( int )Math.pow( i, j ), pow( i, j ) );
        }
    }
    
    @Test( expected = ArithmeticException.class, timeout = 5000L )
    public void powIntIntException()
    {
        pow( 0, -1 );
    }
    
    @Test( timeout = 5000L )
    public void powIntIntSpecial()
    {
        for ( int i : INTS ) if ( i != 0 ) for ( int j : INTS ) if ( j < 0 ) assertEquals( i == 1 ? 1 : i == -1 ? ( ( j & 1 ) == 1 ? -1 : 1 ) : 0, pow( i, j ) );
    }
    
    @Test( timeout = 5000L )
    public void powLongIntRandom()
    {
        BigInteger mod = BigUtils.BI_MAX_LONG.add( BigInteger.ONE ).shiftLeft( 1 );
        for ( int i = 0; i < 100; i++ )
        {
            long base = RANDOM.nextLong();
            for ( int j = 0; j < 100; j++ )
            {
                int power = RANDOM.nextInt( Integer.MAX_VALUE );
                BigInteger expected = BigInteger.valueOf( base ).modPow( BigInteger.valueOf( power ), mod );
                assertEquals( i + " ^ " + j, expected.longValue(), pow( base, power ) );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void powLongIntExact()
    {
        BigInteger mod = BigUtils.BI_MAX_LONG.add( BigInteger.ONE ).shiftLeft( 1 );
        for ( long i : LONGS ) for ( int j : INTS ) if ( j >= 0 )
        {
            BigInteger expected = BigInteger.valueOf( i ).modPow( BigInteger.valueOf( j ), mod );
            assertEquals( i + " ^ " + j, expected.longValue(), pow( i, j ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void powLongIntCasual()
    {
        assertEquals( 3L, pow( 3L, 1 ) );
        assertEquals( 25L, pow( 5L, 2 ) );
        assertEquals( 729L, pow( 9L, 3 ) );
        double upper = Math.log( Long.MAX_VALUE );
        for ( long i : LONGS ) if ( i > -10000L && i < 10000L )
        {
            int up = ( int )( upper / Math.log( Math.abs( i ) ) );
            if ( up == 0 ) up = Integer.MAX_VALUE;
            for ( int j : INTS ) if ( j >= ( i == 0L ? 0 : -up ) && j <= up ) assertEquals( i + " ^ " + j, ( long )Math.pow( i, j ), pow( i, j ) );
        }
    }
    
    @Test( expected = ArithmeticException.class, timeout = 5000L )
    public void powLongIntException()
    {
        pow( 0L, -1 );
    }
    
    @Test( timeout = 5000L )
    public void powLongIntSpecial()
    {
        for ( long i : LONGS ) if ( i != 0L ) for ( int j : INTS ) if ( j < 0 ) assertEquals( i == 1 ? 1L : i == -1 ? ( ( j & 1 ) == 1 ? -1L : 1L ) : 0L, pow( i, j ) );
    }
    
    @Test( timeout = 5000L )
    public void powExactIntIntRandom()
    {
        for ( int i = 0; i < 100; i++ )
        {
            int base = RANDOM.nextInt();
            for ( int j = 0; j < 100; j++ )
            {
                int power = RANDOM.nextInt( Integer.MAX_VALUE );
                boolean overflow = BigInteger.valueOf( 31 - Integer.numberOfLeadingZeros( Math.abs( base ) ) ).multiply( BigInteger.valueOf( power ) ).compareTo( BigInteger.valueOf( 32 ) ) > 0;
                BigInteger expected = overflow ? null : BigInteger.valueOf( base ).pow( power );
                if ( expected != null && expected.bitLength() <= 31 ) assertEquals( base + " ^ " + power, expected.intValue(), powExact( base, power ) );
                else
                {
                    try
                    {
                        powExact( base, power );
                        fail( "Should overflow: " + base + " ^ " + power );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void powExactIntIntCasual()
    {
        assertEquals( 3, powExact( 3, 1 ) );
        assertEquals( 25, powExact( 5, 2 ) );
        assertEquals( 729, powExact( 9, 3 ) );
    }
    
    @Test( timeout = 5000L )
    public void powExactIntIntExact()
    {
        for ( int base : INTS )
        {
            for ( int power : INTS )
            {
                if ( power < 0 ) continue;
                boolean overflow = BigInteger.valueOf( 31 - Integer.numberOfLeadingZeros( Math.abs( base ) ) ).multiply( BigInteger.valueOf( power ) ).compareTo( BigInteger.valueOf( 32 ) ) > 0;
                BigInteger expected = overflow ? null : BigInteger.valueOf( base ).pow( power );
                if ( expected != null && expected.bitLength() <= 31 )
                {
                    int value = powExact( base, power );
                    assertEquals( base + " ^ " + power, expected.intValue(), value );
                    assertEquals( base + " ^ " + power, ( int )Math.pow( base, power ), value );
                    assertEquals( base + " ^ " + power, pow( base, power ), value );
                }
                else
                {
                    try
                    {
                        powExact( base, power );
                        fail( "Should overflow: " + base + " ^ " + power );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
            }
        }
    }
    
    @Test( expected = ArithmeticException.class, timeout = 5000L )
    public void powExactIntIntException()
    {
        powExact( 0, -1 );
    }
    
    @Test( timeout = 5000L )
    public void powExactIntIntSpecial()
    {
        for ( int i : INTS ) if ( i != 0 ) for ( int j : INTS ) if ( j < 0 ) assertEquals( i == 1 ? 1 : i == -1 ? ( ( j & 1 ) == 1 ? -1 : 1 ) : 0, powExact( i, j ) );
    }
    
    @Test( timeout = 5000L )
    public void powExactLongIntRandom()
    {
        for ( int i = 0; i < 100; i++ )
        {
            long base = RANDOM.nextLong();
            for ( int j = 0; j < 100; j++ )
            {
                int power = RANDOM.nextInt( Integer.MAX_VALUE );
                boolean overflow = BigInteger.valueOf( 63 - Long.numberOfLeadingZeros( Math.abs( base ) ) ).multiply( BigInteger.valueOf( power ) ).compareTo( BigInteger.valueOf( 64 ) ) > 0;
                BigInteger expected = overflow ? null : BigInteger.valueOf( base ).pow( power );
                if ( expected != null && expected.bitLength() <= 63 ) assertEquals( base + " ^ " + power, expected.longValue(), powExact( base, power ) );
                else
                {
                    try
                    {
                        powExact( base, power );
                        fail( "Should overflow: " + base + " ^ " + power );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void powExactLongIntCasual()
    {
        assertEquals( 3L, powExact( 3L, 1 ) );
        assertEquals( 25L, powExact( 5L, 2 ) );
        assertEquals( 729L, powExact( 9L, 3 ) );
    }
    
    @Test( timeout = 5000L )
    public void powExactLongIntExact()
    {
        for ( long base : LONGS )
        {
            for ( int power : INTS )
            {
                if ( power < 0 ) continue;
                boolean overflow = BigInteger.valueOf( 63 - Long.numberOfLeadingZeros( Math.abs( base ) ) ).multiply( BigInteger.valueOf( power ) ).compareTo( BigInteger.valueOf( 64 ) ) > 0;
                BigInteger expected = overflow ? null : BigInteger.valueOf( base ).pow( power );
                if ( expected != null && expected.bitLength() <= 63 )
                {
                    long value = powExact( base, power );
                    assertEquals( base + " ^ " + power, expected.longValue(), value );
                    if ( value < 10000000000000000L ) assertEquals( base + " ^ " + power, power == 1 ? base : ( long )Math.pow( base, power ), value );
                    assertEquals( base + " ^ " + power, pow( base, power ), value );
                }
                else
                {
                    try
                    {
                        powExact( base, power );
                        fail( "Should overflow: " + base + " ^ " + power );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
            }
        }
    }
    
    @Test( expected = ArithmeticException.class, timeout = 5000L )
    public void powExactLongIntException()
    {
        powExact( 0L, -1 );
    }
    
    @Test( timeout = 5000L )
    public void powExactLongIntSpecial()
    {
        for ( long i : LONGS ) if ( i != 0 ) for ( int j : INTS ) if ( j < 0 ) assertEquals( i == 1L ? 1L : i == -1L ? ( ( j & 1 ) == 1 ? -1L : 1L ) : 0L, powExact( i, j ) );
    }
}