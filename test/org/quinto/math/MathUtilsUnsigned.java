package org.quinto.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.quinto.math.MathUtils.divideUnsigned;
import static org.quinto.math.MathUtils.remainderUnsigned;
import static org.quinto.math.MathUtils.toUnsignedBigInteger;

public class MathUtilsUnsigned
{
    public MathUtilsUnsigned()
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
    public void remainderUnsignedIntCasual()
    {
        assertEquals( 36, remainderUnsigned( -2147479015, 63 ) );
        assertEquals( 6, remainderUnsigned( -2147479015, 25 ) );
    }
    
    @Test( timeout = 5000L )
    public void remainderUnsignedIntSpecial()
    {
        int ints[] = TestUtils.getInts();
        for ( int dividend : ints )
        {
            for ( int divisor : ints )
            {
                if ( divisor == 0 )
                {
                    try
                    {
                        remainderUnsigned( dividend, divisor );
                        fail( "Should have failed with ArithmeticException: division by zero" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( dividend + " % " + divisor, Integer.remainderUnsigned( dividend, divisor ), remainderUnsigned( dividend, divisor ) );
                    assertEquals( dividend + " % " + divisor, ( int )( Integer.toUnsignedLong( dividend ) % Integer.toUnsignedLong( divisor ) ), remainderUnsigned( dividend, divisor ) );
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void remainderUnsignedLongCasual()
    {
        assertEquals( 48L, remainderUnsigned( -2147479015L, 63L ) );
    }
    
    @Test( timeout = 5000L )
    public void remainderUnsignedLongSpecial()
    {
        long longs[] = TestUtils.getLongs();
        for ( long dividend : longs )
        {
            for ( long divisor : longs )
            {
                if ( divisor == 0L )
                {
                    try
                    {
                        remainderUnsigned( dividend, divisor );
                        fail( "Should have failed with ArithmeticException: division by zero" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( dividend + " % " + divisor, Long.remainderUnsigned( dividend, divisor ), remainderUnsigned( dividend, divisor ) );
                    assertEquals( dividend + " % " + divisor, toUnsignedBigInteger( dividend ).remainder( toUnsignedBigInteger( divisor ).abs() ).longValue(), remainderUnsigned( dividend, divisor ) );
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void divideUnsignedIntCasual()
    {
        assertEquals( 34087115, divideUnsigned( -2147479015, 63 ) );
        assertEquals( 85899531, divideUnsigned( -2147479015, 25 ) );
        assertEquals( 2147483646, divideUnsigned( -3, 2 ) );
        assertEquals( 330382098, divideUnsigned( -16, 13 ) );
        assertEquals( 306783377, divideUnsigned( -16, 14 ) );
        assertEquals( 2, divideUnsigned( -1, 2147483647 ) );
        assertEquals( 2, divideUnsigned( -2, 2147483647 ) );
        assertEquals( 1, divideUnsigned( -3, 2147483647 ) );
        assertEquals( 1, divideUnsigned( -16, 2147483647 ) );
        assertEquals( 1, divideUnsigned( -16, 2147483646 ) );
    }
    
    @Test( timeout = 5000L )
    public void divideUnsignedIntSpecial()
    {
        int ints[] = TestUtils.getInts();
        for ( int dividend : ints )
        {
            for ( int divisor : ints )
            {
                if ( divisor == 0 )
                {
                    try
                    {
                        divideUnsigned( dividend, divisor );
                        fail( "Should have failed with ArithmeticException: division by zero" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( dividend + " / " + divisor, Integer.divideUnsigned( dividend, divisor ), divideUnsigned( dividend, divisor ) );
                    assertEquals( dividend + " / " + divisor, ( int )( Integer.toUnsignedLong( dividend ) / Integer.toUnsignedLong( divisor ) ), divideUnsigned( dividend, divisor ) );
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void divideUnsignedLongCasual()
    {
        assertEquals( 292805461453366231L, divideUnsigned( -2147479015L, 63L ) );
    }
    
    @Test( timeout = 5000L )
    public void divideUnsignedLongSpecial()
    {
        long longs[] = TestUtils.getLongs();
        for ( long dividend : longs )
        {
            for ( long divisor : longs )
            {
                if ( divisor == 0L )
                {
                    try
                    {
                        divideUnsigned( dividend, divisor );
                        fail( "Should have failed with ArithmeticException: division by zero" );
                    }
                    catch ( ArithmeticException e )
                    {
                    }
                }
                else
                {
                    assertEquals( dividend + " / " + divisor, Long.divideUnsigned( dividend, divisor ), divideUnsigned( dividend, divisor ) );
                    assertEquals( dividend + " / " + divisor, toUnsignedBigInteger( dividend ).divide( toUnsignedBigInteger( divisor ).abs() ).longValue(), divideUnsigned( dividend, divisor ) );
                }
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void toUnsignedBigIntegerIntSpecial()
    {
        for ( int value : TestUtils.getInts() )
        {
            assertEquals( new BigInteger( Integer.toUnsignedString( value ) ), toUnsignedBigInteger( value ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void toUnsignedBigIntegerLongSpecial()
    {
        for ( long value : TestUtils.getLongs() )
        {
            assertEquals( new BigInteger( Long.toUnsignedString( value ) ), toUnsignedBigInteger( value ) );
        }
    }
}