package org.quinto.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.quinto.math.MathUtils.ceil;
import static org.quinto.math.MathUtils.floor;
import static org.quinto.math.MathUtils.round;
import static org.quinto.math.MathUtils.trunc;

public class MathUtilsRoundingTest
{
    private static final double DELTA = 0.0001;
    private static final Random RANDOM = new Random( System.nanoTime() );
    private static final double IMMUTABLES[] = { Double.NEGATIVE_INFINITY, -0.0, Double.NaN, 0.0, Double.POSITIVE_INFINITY };
    private static final double SPECIALS[] = { Long.MIN_VALUE, Integer.MIN_VALUE, Short.MIN_VALUE, Byte.MIN_VALUE, -0.0, 0.0, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE };
    private static final double SMALL[] = { Double.MIN_VALUE, Double.MIN_NORMAL, Float.MIN_VALUE, Float.MIN_NORMAL };
    private static final int INT_SPECIALS[] = { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, Integer.MAX_VALUE - 1, Integer.MAX_VALUE };
    
    public MathUtilsRoundingTest()
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
    public void truncZero()
    {
        double values[] = new double[]{ 0.1, 0.2, 0.3, 0.0, -0.0, 1.0, 2.0, 3.0, -0.1, -0.2, -0.3, -1.0, -2.0, -3.0, 1.1, 1.5, 1.6, 2.3, 2.8, -3.5, -5.2, 24.0, 24.4, 24.42, 24.420042, 24.4200000042 };
        for ( double value : values ) assertEquals( ( int )value, trunc( value, 0 ), DELTA );
    }

    @Test( timeout = 5000L )
    public void truncSpecial()
    {
        for ( double value : IMMUTABLES ) for ( int decimal = -30; decimal < 30; decimal++ ) assertEquals( trunc( value, decimal ), value, DELTA );
        for ( double value : SPECIALS ) assertEquals( value, trunc( value, 0 ), DELTA );
        for ( double value : SMALL ) assertEquals( 0.0, trunc( value, 0 ), DELTA );
        for ( int decimal : INT_SPECIALS )
        {
            for ( double value : IMMUTABLES ) assertEquals( "Value: " + value, value, trunc( value, decimal ), DELTA );
            for ( double value : SPECIALS ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, trunc( value, decimal ), DELTA );
            for ( double value : SMALL ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, trunc( value, decimal ), DELTA );
        }
    }
    
    @Test( timeout = 5000L )
    public void truncCasual()
    {
        double values[][] = new double[][]{ { 1, 0.123, 0.1 },
                                            { 2, 0.123, 0.12 },
                                            { 3, 0.123, 0.123 },
                                            { -1, 0.123, 0 },
                                            { -2, 0.123, 0 },
                                            { 1, 5.523, 5.5 },
                                            { 2, 5.523, 5.52 },
                                            { 3, 5.523, 5.523 },
                                            { -1, 5.523, 0 },
                                            { -2, 5.523, 0 },
                                            { 1, 25.62, 25.6 },
                                            { 2, 25.62, 25.62 },
                                            { 3, 25.62, 25.62 },
                                            { -1, 25.62, 20 },
                                            { -2, 25.62, 0 },
                                            { 0, 12.1, 12.0 },
                                            { 0, 12.9, 12.0 },
                                            { 0, 12.5, 12.0 },
                                            { 0, 12.0, 12.0 },
                                            { 2, 12.125, 12.12 },
                                            { -1, 12.1, 10.0 },
                                            { 0, -12.1, -12.0 },
                                            { 0, -12.9, -12.0 },
                                            { 0, -12.5, -12.0 } };
        for ( double tuple[] : values ) assertEquals( tuple[ 2 ], trunc( tuple[ 1 ], ( int )tuple[ 0 ] ), DELTA );
    }
    
    @Test( timeout = 5000L )
    public void truncRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double value = RANDOM.nextDouble() * 400.0 - 150.0;
            BigDecimal d = BigDecimal.valueOf( value );
            for ( int j = -4; j < 30; j++ )
            {
                assertEquals( -trunc( value, j ), trunc( -value, j ), DELTA );
                assertEquals( new BigDecimal( d.movePointRight( j ).toBigInteger() ).movePointLeft( j ).doubleValue(), trunc( value, j ), DELTA );
            }
        }
    }

    @Test( timeout = 5000L )
    public void roundZero()
    {
        double values[] = new double[]{ 0.1, 0.2, 0.3, 0.0, -0.0, 1.0, 2.0, 3.0, -0.1, -0.2, -0.3, -1.0, -2.0, -3.0, 1.1, 1.5, 1.6, 2.3, 2.8, -3.5, -5.2, 24.0, 24.4, 24.42, 24.420042, 24.4200000042 };
        for ( double value : values )
        {
            if ( value >= 0.0 ) assertEquals( "Value: " + value, Math.round( value ), round( value, 0 ), DELTA );
            else assertEquals( "Value: " + value, ( int )( value - 0.5 ), round( value, 0 ), DELTA );
        }
    }

    @Test( timeout = 5000L )
    public void roundSpecial()
    {
        for ( double value : IMMUTABLES ) for ( int decimal = -30; decimal < 30; decimal++ ) assertEquals( value, round( value, decimal ), DELTA );
        for ( double value : SPECIALS ) assertEquals( value, round( value, 0 ), DELTA );
        for ( double value : SMALL ) assertEquals( 0.0, round( value, 0 ), DELTA );
        for ( int decimal : INT_SPECIALS )
        {
            for ( double value : IMMUTABLES ) assertEquals( "Value: " + value, value, round( value, decimal ), DELTA );
            for ( double value : SPECIALS ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, round( value, decimal ), DELTA );
            for ( double value : SMALL ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, round( value, decimal ), DELTA );
        }
    }
    
    @Test( timeout = 5000L )
    public void roundCasual()
    {
        double values[][] = new double[][]{ { 1, 0.123, 0.1 },
                                            { 2, 0.123, 0.12 },
                                            { 3, 0.123, 0.123 },
                                            { -1, 0.123, 0 },
                                            { -2, 0.123, 0 },
                                            { 1, 5.523, 5.5 },
                                            { 2, 5.523, 5.52 },
                                            { 3, 5.523, 5.523 },
                                            { -1, 5.523, 10 },
                                            { -2, 5.523, 0 },
                                            { 1, 25.62, 25.6 },
                                            { 2, 25.62, 25.62 },
                                            { 3, 25.62, 25.62 },
                                            { -1, 25.62, 30 },
                                            { -2, 25.62, 0 },
                                            { 0, 12.1, 12.0 },
                                            { 0, 12.9, 13.0 },
                                            { 0, 12.5, 13.0 },
                                            { 0, 12.0, 12.0 },
                                            { 2, 12.125, 12.13 },
                                            { -1, 12.1, 10.0 },
                                            { 0, -12.1, -12.0 },
                                            { 0, -12.9, -13.0 },
                                            { 0, -12.5, -13.0 } };
        for ( double tuple[] : values ) assertEquals( tuple[ 2 ], round( tuple[ 1 ], ( int )tuple[ 0 ] ), DELTA );
    }
    
    @Test( timeout = 5000L )
    public void roundRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double value = RANDOM.nextDouble() * 400.0 - 150.0;
            BigDecimal d = BigDecimal.valueOf( value );
            for ( int j = -4; j < 30; j++ )
            {
                assertEquals( -round( value, j ), round( -value, j ), DELTA );
                assertEquals( d.setScale( j, RoundingMode.HALF_UP ).doubleValue(), round( value, j ), DELTA );
            }
        }
    }

    @Test( timeout = 5000L )
    public void ceilZero()
    {
        double values[] = new double[]{ 0.1, 0.2, 0.3, 0.0, -0.0, 1.0, 2.0, 3.0, -0.1, -0.2, -0.3, -1.0, -2.0, -3.0, 1.1, 1.5, 1.6, 2.3, 2.8, -3.5, -5.2, 24.0, 24.4, 24.42, 24.420042, 24.4200000042 };
        for ( double value : values ) assertEquals( "Value: " + value, ( int )value + ( value % 1.0 > 0.0 ? 1 : 0 ), ceil( value, 0 ), DELTA );
    }

    @Test( timeout = 5000L )
    public void ceilSpecial()
    {
        for ( double value : IMMUTABLES ) for ( int decimal = -30; decimal < 30; decimal++ ) assertEquals( value, ceil( value, decimal ), DELTA );
        for ( double value : SPECIALS ) assertEquals( "Value: " + value, value, ceil( value, 0 ), DELTA );
        for ( double value : SMALL ) assertEquals( "Value: " + value, 1.0, ceil( value, 0 ), DELTA );
        for ( int decimal : INT_SPECIALS )
        {
            for ( double value : IMMUTABLES ) assertEquals( "Value: " + value, value, ceil( value, decimal ), DELTA );
            for ( double value : SPECIALS ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, ceil( value, decimal ), DELTA );
            for ( double value : SMALL ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, ceil( value, decimal ), DELTA );
        }
    }
    
    @Test( timeout = 5000L )
    public void ceilCasual()
    {
        double values[][] = new double[][]{ { 1, 0.123, 0.2 },
                                            { 2, 0.123, 0.13 },
                                            { 3, 0.123, 0.123 },
                                            { -1, 0.123, 10 },
                                            { -2, 0.123, 100 },
                                            { 1, 5.523, 5.6 },
                                            { 2, 5.523, 5.53 },
                                            { 3, 5.523, 5.523 },
                                            { -1, 5.523, 10 },
                                            { -2, 5.523, 100 },
                                            { 1, 25.62, 25.7 },
                                            { 2, 25.62, 25.62 },
                                            { 3, 25.62, 25.62 },
                                            { -1, 25.62, 30 },
                                            { -2, 25.62, 100 },
                                            { 0, 12.1, 13.0 },
                                            { 0, 12.9, 13.0 },
                                            { 0, 12.5, 13.0 },
                                            { 0, 12.0, 12.0 },
                                            { 2, 12.125, 12.13 },
                                            { -1, 12.1, 20.0 },
                                            { 0, -12.1, -12.0 },
                                            { 0, -12.9, -12.0 },
                                            { 0, -12.5, -12.0 } };
        for ( double tuple[] : values ) assertEquals( tuple[ 2 ], ceil( tuple[ 1 ], ( int )tuple[ 0 ] ), DELTA );
    }
    
    @Test( timeout = 5000L )
    public void ceilRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double value = RANDOM.nextDouble() * 400.0 - 150.0;
            BigDecimal d = BigDecimal.valueOf( value );
            for ( int j = -4; j < 30; j++ ) assertEquals( d.setScale( j, RoundingMode.CEILING ).doubleValue(), ceil( value, j ), DELTA );
        }
    }

    @Test( timeout = 5000L )
    public void floorZero()
    {
        double values[] = new double[]{ 0.1, 0.2, 0.3, 0.0, -0.0, 1.0, 2.0, 3.0, -0.1, -0.2, -0.3, -1.0, -2.0, -3.0, 1.1, 1.5, 1.6, 2.3, 2.8, -3.5, -5.2, 24.0, 24.4, 24.42, 24.420042, 24.4200000042 };
        for ( double value : values ) assertEquals( "Value: " + value, ( int )value - ( value % 1.0 < 0.0 ? 1 : 0 ), floor( value, 0 ), DELTA );
    }

    @Test( timeout = 5000L )
    public void floorSpecial()
    {
        for ( double value : IMMUTABLES ) for ( int decimal = -30; decimal < 30; decimal++ ) assertEquals( value, floor( value, decimal ), DELTA );
        for ( double value : SPECIALS ) assertEquals( "Value: " + value, value, floor( value, 0 ), DELTA );
        for ( double value : SMALL ) assertEquals( "Value: " + value, 0.0, floor( value, 0 ), DELTA );
        for ( int decimal : INT_SPECIALS )
        {
            for ( double value : IMMUTABLES ) assertEquals( "Value: " + value, value, floor( value, decimal ), DELTA );
            for ( double value : SPECIALS ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, floor( value, decimal ), DELTA );
            for ( double value : SMALL ) assertEquals( "Value: " + value, decimal > 0 ? value : 0.0, floor( value, decimal ), DELTA );
        }
    }
    
    @Test( timeout = 5000L )
    public void floorCasual()
    {
        double values[][] = new double[][]{ { 1, 0.123, 0.1 },
                                            { 2, 0.123, 0.12 },
                                            { 3, 0.123, 0.123 },
                                            { -1, 0.123, 0 },
                                            { -2, 0.123, 0 },
                                            { 1, 5.523, 5.5 },
                                            { 2, 5.523, 5.52 },
                                            { 3, 5.523, 5.523 },
                                            { -1, 5.523, 0 },
                                            { -2, 5.523, 0 },
                                            { 1, 25.62, 25.6 },
                                            { 2, 25.62, 25.62 },
                                            { 3, 25.62, 25.62 },
                                            { -1, 25.62, 20 },
                                            { -2, 25.62, 0 },
                                            { 0, 12.1, 12.0 },
                                            { 0, 12.9, 12.0 },
                                            { 0, 12.5, 12.0 },
                                            { 0, 12.0, 12.0 },
                                            { 2, 12.125, 12.12 },
                                            { -1, 12.1, 10.0 },
                                            { 0, -12.1, -13.0 },
                                            { 0, -12.9, -13.0 },
                                            { 0, -12.5, -13.0 } };
        for ( double tuple[] : values ) assertEquals( tuple[ 2 ], floor( tuple[ 1 ], ( int )tuple[ 0 ] ), DELTA );
    }
    
    @Test( timeout = 5000L )
    public void floorRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double value = RANDOM.nextDouble() * 400.0 - 150.0;
            BigDecimal d = BigDecimal.valueOf( value );
            for ( int j = -4; j < 30; j++ ) assertEquals( d.setScale( j, RoundingMode.FLOOR ).doubleValue(), floor( value, j ), DELTA );
        }
    }
}