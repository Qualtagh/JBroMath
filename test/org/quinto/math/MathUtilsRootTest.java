package org.quinto.math;

import java.math.BigInteger;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.quinto.math.MathUtils.icbrt;
import static org.quinto.math.MathUtils.iroot;
import static org.quinto.math.MathUtils.isqrt;
import static org.quinto.math.MathUtils.powExact;
import static org.quinto.math.MathUtils.toUnsignedBigInteger;
import static org.quinto.math.MathUtils.uisqrt;
import static org.quinto.math.TestUtils.bi;

public class MathUtilsRootTest
{
    private static final Random RANDOM = new Random( System.nanoTime() );
    
    public MathUtilsRootTest()
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
    public void isqrtIntCasual()
    {
        int check[][] = new int[][]{ { 0, 0 },
                                     { 1, 1 },
                                     { 1, 2 },
                                     { 1, 3 },
                                     { 2, 4 },
                                     { 2, 5 },
                                     { 2, 6 },
                                     { 2, 7 },
                                     { 2, 8 },
                                     { 3, 9 },
                                     { 3, 10 },
                                     { 3, 11 },
                                     { 3, 12 },
                                     { 10, 120 },
                                     { 11, 121 },
                                     { 11, 122 } };
        for ( int c[] : check )
        {
            int root = c[ 0 ];
            int n = c[ 1 ];
            assertEquals( root, isqrt( n ) );
            assertEquals( root, uisqrt( n ) );
            assertEquals( root, iroot( n, 2 ) );
            assertEquals( ( long )root, isqrt( ( long )n ) );
            assertEquals( ( long )root, uisqrt( ( long )n ) );
            assertEquals( ( long )root, iroot( ( long )n, 2 ) );
        }
    }

    @Test( timeout = 5000L )
    public void isqrtIntSpecial()
    {
        for ( int n : TestUtils.getInts() )
        {
            int root;
            if ( n < 0 )
            {
                root = uisqrt( n );
                assertTrue( Integer.compareUnsigned( root * root, n ) <= 0 );
                BigInteger biRoot = toUnsignedBigInteger( root );
                BigInteger bin = toUnsignedBigInteger( n );
                assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
                biRoot = biRoot.add( BigInteger.ONE );
                assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
                try
                {
                    root = isqrt( n );
                    fail( "Should have failed: isqrt on negative n = " + n );
                }
                catch ( ArithmeticException e )
                {
                    continue;
                }
            }
            else root = isqrt( n );
            assertEquals( root, uisqrt( n ) );
            assertTrue( Math.multiplyExact( root, root ) <= n );
            root = Math.incrementExact( root );
            try
            {
                assertTrue( Math.multiplyExact( root, root ) > n );
            }
            catch ( ArithmeticException e )
            {
                // Overflow => root * root > n.
            }
        }
    }

    @Test( timeout = 5000L )
    public void isqrtLongCasual()
    {
        long check[][] = new long[][]{ { 3010305728L, 9061940581203463322L } };
        for ( long c[] : check )
        {
            long root = c[ 0 ];
            long n = c[ 1 ];
            assertEquals( root, isqrt( n ) );
            assertEquals( root, Integer.toUnsignedLong( uisqrt( n ) ) );
            assertEquals( root, iroot( n, 2 ) );
        }
    }

    @Test( timeout = 5000L )
    public void isqrtLongSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            long root;
            if ( n < 0L )
            {
                root = Integer.toUnsignedLong( uisqrt( n ) );
                assertTrue( Long.compareUnsigned( root * root, n ) <= 0 );
                BigInteger biRoot = toUnsignedBigInteger( root );
                BigInteger bin = toUnsignedBigInteger( n );
                assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
                biRoot = biRoot.add( BigInteger.ONE );
                assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
                try
                {
                    root = isqrt( n );
                    fail( "Should have failed: isqrt on negative n = " + n );
                }
                catch ( ArithmeticException e )
                {
                    continue;
                }
            }
            else root = isqrt( n );
            assertEquals( root, Integer.toUnsignedLong( uisqrt( n ) ) );
            assertTrue( Math.multiplyExact( root, root ) <= n );
            root = Math.incrementExact( root );
            try
            {
                assertTrue( root + " ^ 2 = " + n, Math.multiplyExact( root, root ) > n );
            }
            catch ( ArithmeticException e )
            {
                // Overflow => root * root > n.
            }
        }
    }

    @Test( timeout = 5000L )
    public void isqrtLongBig()
    {
        long n = 0x7FFF000000000000L;
        for ( int i = 0; i < 10000000; i++, n-- )
        {
            long root = isqrt( n );
            assertEquals( root, Integer.toUnsignedLong( uisqrt( n ) ) );
            BigInteger biRoot = bi( root );
            BigInteger bin = bi( n );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
            biRoot = biRoot.add( BigInteger.ONE );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
        }
    }

    @Test( timeout = 5000L )
    public void isqrtLongRandom()
    {
        for ( int i = 0; i < 10000000; i++ )
        {
            long n = ( ( long )( ( RANDOM.nextInt( 0xFFFF ) + 1 ) & 0x7FFF ) << 48 ) + ( ( long )( RANDOM.nextInt() & 0xFFFF ) << 32 ) + RANDOM.nextInt();
            long root = isqrt( n );
            assertEquals( root, Integer.toUnsignedLong( uisqrt( n ) ) );
            BigInteger biRoot = bi( root );
            BigInteger bin = bi( n );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
            biRoot = biRoot.add( BigInteger.ONE );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
        }
    }

    @Test( timeout = 5000L )
    public void uisqrtLongBig()
    {
        long n = 0xFFFF000000000000L;
        for ( int i = 0; i < 10000000; i++, n-- )
        {
            int root = uisqrt( n );
            BigInteger biRoot = toUnsignedBigInteger( root );
            BigInteger bin = toUnsignedBigInteger( n );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
            biRoot = biRoot.add( BigInteger.ONE );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
        }
    }

    @Test( timeout = 5000L )
    public void uisqrtLongRandom()
    {
        for ( int i = 0; i < 10000000; i++ )
        {
            long n = ( ( long )( RANDOM.nextInt( 0xFFFF ) + 1 ) << 48 ) + ( ( long )( RANDOM.nextInt() & 0xFFFF ) << 32 ) + RANDOM.nextInt();
            int root = uisqrt( n );
            BigInteger biRoot = toUnsignedBigInteger( root );
            BigInteger bin = toUnsignedBigInteger( n );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) <= 0 );
            biRoot = biRoot.add( BigInteger.ONE );
            assertTrue( biRoot.multiply( biRoot ).compareTo( bin ) > 0 );
        }
    }

    @Test( timeout = 5000L )
    public void icbrtIntCasual()
    {
        int check[][] = new int[][]{ { -2, -12 },
                                     { -2, -9 },
                                     { -2, -8 },
                                     { -1, -7 },
                                     { -1, -5 },
                                     { -1, -2 },
                                     { -1, -1 },
                                     { 0, 0 },
                                     { 1, 1 },
                                     { 1, 2 },
                                     { 1, 3 },
                                     { 1, 4 },
                                     { 1, 5 },
                                     { 1, 6 },
                                     { 1, 7 },
                                     { 2, 8 },
                                     { 2, 9 },
                                     { 2, 10 },
                                     { 2, 11 },
                                     { 2, 12 },
                                     { 4, 120 },
                                     { 4, 121 },
                                     { 4, 122 },
                                     { 4, 123 },
                                     { 4, 124 },
                                     { 5, 125 },
                                     { 5, 126 } };
        for ( int c[] : check )
        {
            int root = c[ 0 ];
            int n = c[ 1 ];
            assertEquals( root, icbrt( n ) );
            assertEquals( root, iroot( n, 3 ) );
            assertEquals( ( long )root, icbrt( ( long )n ) );
            assertEquals( ( long )root, iroot( ( long )n, 3 ) );
        }
    }

    @Test( timeout = 5000L )
    public void icbrtIntSpecial()
    {
        for ( int n : TestUtils.getInts() )
        {
            int root = icbrt( n );
            if ( n >= 0 )
            {
                assertTrue( powExact( root, 3 ) <= n );
                root = Math.incrementExact( root );
                try
                {
                    assertTrue( powExact( root, 3 ) > n );
                }
                catch ( ArithmeticException e )
                {
                    // Overflow => root ^ 3 > n.
                }
            }
            else
            {
                assertTrue( powExact( root, 3 ) >= n );
                root = Math.decrementExact( root );
                try
                {
                    assertTrue( powExact( root, 3 ) < n );
                }
                catch ( ArithmeticException e )
                {
                    // Underflow => root ^ 3 < n.
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void icbrtLongSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            long root = icbrt( n );
            if ( n >= 0L )
            {
                assertTrue( powExact( root, 3 ) <= n );
                root = Math.incrementExact( root );
                try
                {
                    assertTrue( powExact( root, 3 ) > n );
                }
                catch ( ArithmeticException e )
                {
                    // Overflow => root ^ 3 > n.
                }
            }
            else
            {
                assertTrue( powExact( root, 3 ) >= n );
                root = Math.decrementExact( root );
                try
                {
                    assertTrue( powExact( root, 3 ) < n );
                }
                catch ( ArithmeticException e )
                {
                    // Underflow => root ^ 3 < n.
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void irootIntCasual()
    {
        int check[][] = new int[][]{ { 3, 16, 1642265158 },
                                     { 5, 3, 126 },
                                     { 1, 6, 63 },
                                     { 2, 6, 64 },
                                     { 2, 6, 65 },
                                     { 2, 5, 242 },
                                     { 3, 5, 243 },
                                     { 3, 5, 244 } };
        for ( int c[] : check )
        {
            int root = c[ 0 ];
            int degree = c[ 1 ];
            int n = c[ 2 ];
            assertEquals( root + " ^ " + degree + " <= " + n, root, iroot( n, degree ) );
            assertEquals( ( long )root, iroot( ( long )n, degree ) );
            if ( degree % 2 != 0 )
            {
                assertEquals( root + " ^ " + degree + " <= " + n, -root, iroot( -n, degree ) );
                assertEquals( -( long )root, iroot( -( long )n, degree ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void irootIntSpecial()
    {
        for ( int n : TestUtils.getInts() )
        {
            for ( int degree : TestUtils.getInts() )
            {
                int root;
                if ( n < 0 && degree % 2 == 0 )
                {
                    try
                    {
                        root = iroot( n, degree );
                        fail( "Should have failed: iroot of even degree on negative n = " + n );
                    }
                    catch ( ArithmeticException e )
                    {
                        continue;
                    }
                }
                else if ( degree <= 0 )
                {
                    double res = Math.pow( n, 1.0 / degree );
                    try
                    {
                        root = iroot( n, degree );
                    }
                    catch ( ArithmeticException e )
                    {
                        assertFalse( Double.isFinite( res ) );
                        continue;
                    }
                    if ( n == -1 ) assertEquals( -1, root );
                    else assertEquals( root + " ^ " + degree + " <= " + n + ", expected root = " + res, ( int )res, root );
                    continue;
                }
                else root = iroot( n, degree );
                if ( n == 0 ) assertEquals( 0, root );
                else if ( n > 0 )
                {
                    assertTrue( root + " ^ " + degree + " <= " + n, powExact( root, degree ) <= n );
                    try
                    {
                        root = Math.incrementExact( root );
                    }
                    catch ( ArithmeticException e )
                    {
                        // root = MAX_VALUE => degree = 1 => ( root + 1 ) ^ degree > n.
                        assertEquals( 1, degree );
                        continue;
                    }
                    try
                    {
                        assertTrue( powExact( root, degree ) > n );
                    }
                    catch ( ArithmeticException e )
                    {
                        // Overflow => root ^ degree > n.
                    }
                }
                else
                {
                    assertTrue( powExact( root, degree ) >= n );
                    try
                    {
                        root = Math.decrementExact( root );
                    }
                    catch ( ArithmeticException e )
                    {
                        // root = MIN_VALUE => degree = 1 => ( root - 1 ) ^ degree < n.
                        assertEquals( 1, degree );
                        continue;
                    }
                    try
                    {
                        assertTrue( powExact( root, degree ) < n );
                    }
                    catch ( ArithmeticException e )
                    {
                        // Underflow => root ^ degree < n.
                    }
                }
            }
        }
    }

    @Test( timeout = 5000L )
    public void irootLongSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            for ( int degree : TestUtils.getInts() )
            {
                long root;
                if ( n < 0L && degree % 2 == 0 )
                {
                    try
                    {
                        root = iroot( n, degree );
                        fail( "Should have failed: iroot of even degree on negative n = " + n );
                    }
                    catch ( ArithmeticException e )
                    {
                        continue;
                    }
                }
                else if ( degree <= 0 )
                {
                    double res = Math.pow( n, 1.0 / degree );
                    try
                    {
                        root = iroot( n, degree );
                    }
                    catch ( ArithmeticException e )
                    {
                        assertFalse( Double.isFinite( res ) );
                        continue;
                    }
                    if ( n == -1L ) assertEquals( -1L, root );
                    else assertEquals( root + " ^ " + degree + " <= " + n + ", expected root = " + res, ( long )res, root );
                    continue;
                }
                else root = iroot( n, degree );
                if ( n == 0L ) assertEquals( 0L, root );
                else if ( n > 0L )
                {
                    assertTrue( root + " ^ " + degree + " <= " + n, powExact( root, degree ) <= n );
                    try
                    {
                        root = Math.incrementExact( root );
                    }
                    catch ( ArithmeticException e )
                    {
                        // root = MAX_VALUE => degree = 1 => ( root + 1 ) ^ degree > n.
                        assertEquals( 1, degree );
                        continue;
                    }
                    try
                    {
                        assertTrue( powExact( root, degree ) > n );
                    }
                    catch ( ArithmeticException e )
                    {
                        // Overflow => root ^ degree > n.
                    }
                }
                else
                {
                    assertTrue( powExact( root, degree ) >= n );
                    try
                    {
                        root = Math.decrementExact( root );
                    }
                    catch ( ArithmeticException e )
                    {
                        // root = MIN_VALUE => degree = 1 => ( root - 1 ) ^ degree < n.
                        assertEquals( 1, degree );
                        continue;
                    }
                    try
                    {
                        assertTrue( powExact( root, degree ) < n );
                    }
                    catch ( ArithmeticException e )
                    {
                        // Underflow => root ^ degree < n.
                    }
                }
            }
        }
    }
}