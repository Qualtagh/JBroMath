package org.quinto.math;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.quinto.math.BigUtils.isqrt;
import static org.quinto.math.TestUtils.bi;

public class BigUtilsRootTest
{
    public BigUtilsRootTest()
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
    public void isqrtCasual()
    {
        long check[][] = new long[][]{ { 0, 0 },
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
                                       { 11, 122 },
                                       { 3010305728L, 9061940581203463322L } };
        for ( long c[] : check )
        {
            BigInteger root = bi( c[ 0 ] );
            BigInteger n = bi( c[ 1 ] );
            assertEquals( root, isqrt( n ) );
           // assertEquals( root, iroot( n, 2 ) );
        }
    }

    @Test( timeout = 5000L )
    public void isqrtBigCasual()
    {
        BigInteger check[][] = new BigInteger[][]{ { bi( "21905167782778634" ), bi( "479836375591683028158994122333779" ) },
                                                   { bi( "4226529163" ), bi( "17863548769377095503" ) },
                                                   { bi( "3037000499" ), bi( "9223372036854775808" ) } };
        for ( BigInteger c[] : check )
        {
            BigInteger root = c[ 0 ];
            BigInteger n = c[ 1 ];
            assertEquals( root, isqrt( n ) );
           // assertEquals( root, iroot( n, 2 ) );
        }
    }

    @Test( timeout = 5000L )
    public void isqrtSpecial()
    {
        for ( BigInteger n : TestUtils.getBigIntegers() )
        {
            BigInteger root;
            if ( n.signum() < 0 )
            {
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
            assertTrue( root + " ^ 2 = " + root.multiply( root ) + " > " + n, root.multiply( root ).compareTo( n ) <= 0 );
            root = root.add( BigInteger.ONE );
            assertTrue( root + " ^ 2 = " + root.multiply( root ) + " <= " + n, root.multiply( root ).compareTo( n ) > 0 );
        }
    }
}