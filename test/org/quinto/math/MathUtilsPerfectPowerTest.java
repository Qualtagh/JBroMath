package org.quinto.math;

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
import static org.quinto.math.MathUtils.NOT_FOUND;
import static org.quinto.math.MathUtils.getBaseOfPerfectCube;
import static org.quinto.math.MathUtils.getBaseOfPerfectPower;
import static org.quinto.math.MathUtils.getBaseOfPerfectSquare;
import static org.quinto.math.MathUtils.icbrt;
import static org.quinto.math.MathUtils.iroot;
import static org.quinto.math.MathUtils.isPerfectCube;
import static org.quinto.math.MathUtils.isPerfectPower;
import static org.quinto.math.MathUtils.isPerfectSquare;
import static org.quinto.math.MathUtils.isqrt;
import static org.quinto.math.MathUtils.powExact;

public class MathUtilsPerfectPowerTest
{
    public MathUtilsPerfectPowerTest()
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
        assertEquals( 0L, getBaseOfPerfectSquare( 0L ) );
        assertEquals( 1L, getBaseOfPerfectSquare( 1L ) );
        assertEquals( 2L, getBaseOfPerfectSquare( 4L ) );
        assertEquals( 3L, getBaseOfPerfectSquare( 9L ) );
        assertEquals( 25L, getBaseOfPerfectSquare( 625L ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectSquare( 624L ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectSquare( -9L ) );
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectSquareSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            long base = getBaseOfPerfectSquare( n );
            assertEquals( base, getBaseOfPerfectPower( n, 2 ) );
            if ( base == NOT_FOUND )
            {
                assertFalse( isPerfectSquare( n ) );
                try
                {
                    base = isqrt( n );
                }
                catch ( ArithmeticException e )
                {
                    if ( n >= 0L ) fail( "Error in isqrt of " + n );
                    continue;
                }
                try
                {
                    assertFalse( Math.multiplyExact( base, base ) == n );
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
                assertEquals( n, Math.multiplyExact( base, base ) );
            }
        }
    }

    @Test( timeout = 1800000L )
    public void getBaseOfPerfectSquareAll()
    {
        for ( long base = 2L; base < 3037000500L; base++ )
        {
            long n = base * base;
            assertEquals( NOT_FOUND, getBaseOfPerfectSquare( n - 1L ) );
            assertEquals( base, getBaseOfPerfectSquare( n ) );
            assertEquals( NOT_FOUND, getBaseOfPerfectSquare( n + 1L ) );
            assertEquals( base, ( long )Math.sqrt( n ) );
        }
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectCubeCasual()
    {
        assertEquals( 0L, getBaseOfPerfectCube( 0L ) );
        assertEquals( 1L, getBaseOfPerfectCube( 1L ) );
        assertEquals( 2L, getBaseOfPerfectCube( 8L ) );
        assertEquals( 3L, getBaseOfPerfectCube( 27L ) );
        assertEquals( 5L, getBaseOfPerfectCube( 125L ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectCube( 124L ) );
        assertEquals( -1L, getBaseOfPerfectCube( -1L ) );
        assertEquals( -2L, getBaseOfPerfectCube( -8L ) );
        assertEquals( -3L, getBaseOfPerfectCube( -27L ) );
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectCubeSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            long base = getBaseOfPerfectCube( n );
            assertEquals( base, ( int )base );
            assertEquals( base, getBaseOfPerfectPower( n, 3 ) );
            if ( base == NOT_FOUND )
            {
                assertEquals( NOT_FOUND, getBaseOfPerfectCube( -n ) );
                assertFalse( isPerfectCube( n ) );
                base = icbrt( n );
                try
                {
                    assertFalse( Math.multiplyExact( Math.multiplyExact( base, base ), base ) == n );
                }
                catch ( ArithmeticException e )
                {
                    fail( "Error in icbrt: " + base + " ^ 3 = " + n );
                }
            }
            else
            {
                if ( n > Long.MIN_VALUE ) assertEquals( -base, getBaseOfPerfectCube( -n ) );
                assertTrue( isPerfectCube( n ) );
                assertEquals( icbrt( n ), base );
                assertEquals( n, Math.multiplyExact( Math.multiplyExact( base, base ), base ) );
            }
        }
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectCubeAll()
    {
        for ( long base = 2L; base < 2097152L; base++ )
        {
            long n = base * base * base;
            assertEquals( NOT_FOUND, getBaseOfPerfectCube( n - 1L ) );
            assertEquals( base, getBaseOfPerfectCube( n ) );
            assertEquals( NOT_FOUND, getBaseOfPerfectCube( n + 1L ) );
            assertEquals( base, ( long )Math.cbrt( n ) );
        }
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectPowerLongIntCasual()
    {
        assertEquals( 0L, getBaseOfPerfectPower( 0L, 4 ) );
        assertEquals( 0L, getBaseOfPerfectPower( 0L, 5 ) );
        assertEquals( 1L, getBaseOfPerfectPower( 1L, 4 ) );
        assertEquals( 1L, getBaseOfPerfectPower( 1L, 5 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( -1L, 4 ) );
        assertEquals( -1L, getBaseOfPerfectPower( -1L, 5 ) );
        assertEquals( 2L, getBaseOfPerfectPower( 16L, 4 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 32L, 4 ) );
        assertEquals( 2L, getBaseOfPerfectPower( 32L, 5 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( -16L, 4 ) );
        assertEquals( -2L, getBaseOfPerfectPower( -32L, 5 ) );
        assertEquals( 3L, getBaseOfPerfectPower( 81L, 4 ) );
        assertEquals( 3L, getBaseOfPerfectPower( 243L, 5 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( -81L, 4 ) );
        assertEquals( -3L, getBaseOfPerfectPower( -243L, 5 ) );
        assertEquals( 5L, getBaseOfPerfectPower( 625L, 4 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 624L, 4 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 9061940576029609983L, 2 ) );
        assertEquals( 3010305728L, getBaseOfPerfectPower( 9061940576029609984L, 2 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 9061940576029609985L, 2 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 1642265158L, 16 ) );
        assertEquals( 3L, getBaseOfPerfectPower( 43046721L, 16 ) );
        assertEquals( 4L, getBaseOfPerfectPower( 4294967296L, 16 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 2697034849180764965L, 32 ) );
        assertEquals( 3L, getBaseOfPerfectPower( 1853020188851841L, 32 ) );
        assertEquals( NOT_FOUND, getBaseOfPerfectPower( 9223372036854775807L, 32 ) );
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectPowerLongIntSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            for ( int power : TestUtils.getInts() )
            {
                long base = getBaseOfPerfectPower( n, power );
                if ( power != 2 ) assertEquals( base, ( int )base );
                if ( power < 2 ) assertEquals( NOT_FOUND, base );
                if ( base == NOT_FOUND )
                {
                    if ( ( power & 1 ) == 1 ) assertEquals( NOT_FOUND, getBaseOfPerfectPower( -n, power ) );
                    assertFalse( isPerfectPower( n, power ) );
                    try
                    {
                        base = iroot( n, power );
                    }
                    catch ( ArithmeticException e )
                    {
                        if ( ( n >= 0L && power != 0 || ( power & 1 ) == 1 ) && ( power >= 0 || n != 0L ) ) fail( "Error in iroot of " + n + " of degree " + power );
                        continue;
                    }
                    try
                    {
                        // power < 2 is not a perfect power, so getBaseOfPerfectPower returns NOT_FOUND, but integer root exists and is correct.
                        assertEquals( base + " ^ " + power + " = " + n, power == 1 || power < 0 || power == 0 && n != 0L, powExact( base, power ) == n );
                    }
                    catch ( ArithmeticException e )
                    {
                        if ( power >= 0 || base != 0L ) fail( "Error in iroot: " + base + " ^ " + power + " = " + n );
                    }
                }
                else
                {
                    if ( ( power & 1 ) == 1 && ( n > Long.MIN_VALUE || power != 3 && power != 7 && power != 9 && power != 21 && power != 63 ) ) assertEquals( -base, getBaseOfPerfectPower( -n, power ) );
                    assertTrue( isPerfectPower( n, power ) );
                    assertEquals( iroot( n, power ), base );
                    assertEquals( n, powExact( base, power ) );
                }
            }
        }
    }

    @Test( timeout = 15000L )
    public void getBaseOfPerfectPowerLongIntAll()
    {
        for ( int power = 3; power < 64; power++ )
        {
            for ( long base = iroot( Long.MAX_VALUE, power ); base >= 2L; base-- )
            {
                long n = powExact( base, power );
                assertEquals( NOT_FOUND, getBaseOfPerfectPower( n - 1L, power ) );
                assertEquals( base, getBaseOfPerfectPower( n, power ) );
                assertEquals( NOT_FOUND, getBaseOfPerfectPower( n + 1L, power ) );
                assertEquals( base, ( long )( Math.pow( n, 1.0 / power ) + 0.5 ) );
                long baseAndPower[] = getBaseOfPerfectPower( n );
                assertNotNull( baseAndPower );
                assertTrue( isPerfectPower( n ) );
                assertTrue( base + " ^ " + power + " = " + n + ", actual: " + baseAndPower[ 0 ] + " ^ " + baseAndPower[ 1 ] + " = " + n, base % baseAndPower[ 0 ] == 0L );
                assertTrue( base + " ^ " + power + " = " + n + ", actual: " + baseAndPower[ 0 ] + " ^ " + baseAndPower[ 1 ] + " = " + n, baseAndPower[ 1 ] % power == 0L );
            }
        }
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectPowerLongCasual()
    {
        assertArrayEquals( new long[]{ 2L, 6L }, getBaseOfPerfectPower( 64L ) );
        assertArrayEquals( new long[]{ -4L, 3L }, getBaseOfPerfectPower( -64L ) );
        assertArrayEquals( new long[]{ 2L, 5L }, getBaseOfPerfectPower( 32L ) );
        assertArrayEquals( new long[]{ -2L, 5L }, getBaseOfPerfectPower( -32L ) );
        assertArrayEquals( new long[]{ 2L, 4L }, getBaseOfPerfectPower( 16L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( -16L ) );
        assertArrayEquals( new long[]{ 2L, 3L }, getBaseOfPerfectPower( 8L ) );
        assertArrayEquals( new long[]{ -2L, 3L }, getBaseOfPerfectPower( -8L ) );
        assertArrayEquals( new long[]{ 2L, 2L }, getBaseOfPerfectPower( 4L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( -4L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 2L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( -2L ) );
        assertArrayEquals( new long[]{ 1L, 2L }, getBaseOfPerfectPower( 1L ) );
        assertArrayEquals( new long[]{ -1L, 3L }, getBaseOfPerfectPower( -1L ) );
        assertArrayEquals( new long[]{ 0L, 2L }, getBaseOfPerfectPower( 0L ) );
        assertArrayEquals( new long[]{ 3L, 4L }, getBaseOfPerfectPower( 81L ) );
        assertArrayEquals( new long[]{ 3L, 5L }, getBaseOfPerfectPower( 243L ) );
        assertArrayEquals( new long[]{ 5L, 4L }, getBaseOfPerfectPower( 625L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 624L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 626L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 1642265158L ) );
        assertArrayEquals( new long[]{ 3L, 16L }, getBaseOfPerfectPower( 43046721L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 9061940576029609983L ) );
        assertArrayEquals( new long[]{ 3010305728L, 2L }, getBaseOfPerfectPower( 9061940576029609984L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 9061940576029609985L ) );
        assertArrayEquals( new long[]{ 2L, 32L }, getBaseOfPerfectPower( 4294967296L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 1642265158L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 2697034849180764965L ) );
        assertArrayEquals( new long[]{ 3L, 32L }, getBaseOfPerfectPower( 1853020188851841L ) );
        assertArrayEquals( null, getBaseOfPerfectPower( 9223372036854775807L ) );
    }

    @Test( timeout = 5000L )
    public void getBaseOfPerfectPowerLongSpecial()
    {
        for ( long n : TestUtils.getLongs() )
        {
            long baseAndPower[] = getBaseOfPerfectPower( n );
            if ( baseAndPower == null )
            {
                assertFalse( isPerfectPower( n ) );
                for ( int power = 2; power < 64; power++ )
                {
                    assertFalse( isPerfectPower( n, power ) );
                    long base;
                    try
                    {
                        base = iroot( n, power );
                    }
                    catch ( ArithmeticException e )
                    {
                        if ( n >= 0L || ( power & 1 ) == 1 ) fail( "Error in iroot of " + n + " of degree " + power );
                        continue;
                    }
                    try
                    {
                        // power < 2 is not a perfect power, so getBaseOfPerfectPower returns NOT_FOUND, but integer root exists and is correct.
                        assertFalse( base + " ^ " + power + " = " + n, powExact( base, power ) == n );
                    }
                    catch ( ArithmeticException e )
                    {
                        fail( "Error in iroot: " + base + " ^ " + power + " = " + n );
                    }
                }
            }
            else
            {
                long base = baseAndPower[ 0 ];
                assertTrue( 2L <= baseAndPower[ 1 ] && baseAndPower[ 1 ] <= 63L );
                int power = ( int )baseAndPower[ 1 ];
                if ( power != 2 ) assertEquals( base, ( int )base );
                if ( power == 63 )
                {
                    assertEquals( Long.MIN_VALUE, n );
                    assertEquals( -2L, base );
                }
                assertTrue( isPerfectPower( n ) );
                assertTrue( isPerfectPower( n, power ) );
                assertEquals( getBaseOfPerfectPower( n, power ), base );
                assertEquals( iroot( n, power ), base );
                assertEquals( n, powExact( base, power ) );
                if ( n != -1L && n != 0L && n != 1L ) for ( int p = power + 1; p < 64; p++ ) assertFalse( getBaseOfPerfectPower( n, p ) + " ^ " + p + " = " + n, isPerfectPower( n, p ) );
            }
        }
    }
}