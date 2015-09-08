package org.quinto.math;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.quinto.math.PrimeUtils.*;
import static org.quinto.math.TestUtils.bi;

public class PrimeUtilsTest
{
    // https://oeis.org/A000040
    private static final int PRIMES[] = new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271 };
    private static final Set< Integer > PRIMES_SET = new LinkedHashSet<>( PRIMES.length );
    private static final Set< Integer > COMPOSITES_SET = new LinkedHashSet<>();
    
    static
    {
        for ( int i : PRIMES ) PRIMES_SET.add( i );
        for ( int i = 0; i <= PRIMES[ PRIMES.length - 1 ] + 1; i++ ) if ( !PRIMES_SET.contains( i ) ) COMPOSITES_SET.add( i );
    }
    
    public PrimeUtilsTest()
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
    public void isPrimeCasual()
    {
        for ( int i : PRIMES_SET )
        {
            BigInteger bi = bi( i );
            assertTrue( isPrime( i ) );
            assertTrue( isPrime( bi ) );
            assertTrue( passesTrialDivision( i ) );
            assertTrue( passesTrialDivision( bi ) );
            assertTrue( passesBailliePSW( bi ) );
            if ( i > 2 ) assertTrue( passesLucasPseudoprime( bi ) );
            assertTrue( passesMiller( i ) );
            assertTrue( passesMiller( bi ) );
            if ( isMersenneNumber( bi ) )
            {
                assertTrue( passesLucasLehmer( bi ) );
                assertTrue( isMersennePrime( bi ) );
            }
            if ( isFermatNumber( bi ) ) assertTrue( isFermatPrime( bi ) );
            // Probabilistic. False positives.
            // Should not contain any false negatives.
            assertTrue( passesMillerRabin( bi ) );
            if ( MathUtils.isRelativelyPrime( i, 2 ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_TWO ) );
            if ( MathUtils.isRelativelyPrime( i, 3 ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_THREE ) );
            if ( MathUtils.isRelativelyPrime( i, 4 ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_FOUR ) );
            if ( MathUtils.isRelativelyPrime( i, 5 ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_FIVE ) );
            if ( MathUtils.isRelativelyPrime( i, 9 ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_NINE ) );
            if ( MathUtils.isRelativelyPrime( i, Integer.MAX_VALUE ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_MAX_INT ) );
            if ( MathUtils.isRelativelyPrime( i, Long.MAX_VALUE ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_MAX_LONG ) );
            if ( BigUtils.isRelativelyPrime( bi, BigUtils.BI_MAX_LONG_PLUS_TWO ) ) assertTrue( passesMillerRabin( bi, BigUtils.BI_MAX_LONG_PLUS_TWO ) );
            assertTrue( bi.isProbablePrime( 1 ) );
            assertTrue( bi.isProbablePrime( 10 ) );
            assertTrue( bi.isProbablePrime( 100 ) );
        }
        for ( int i : COMPOSITES_SET )
        {
            BigInteger bi = bi( i );
            assertFalse( isPrime( i ) );
            assertFalse( isPrime( bi ) );
            assertFalse( passesTrialDivision( i ) );
            assertFalse( passesTrialDivision( bi ) );
            assertFalse( passesBailliePSW( bi ) );
            if ( i > 2 && ( i & 1 ) == 1 && !MathUtils.isPerfectSquare( i ) ) assertFalse( passesLucasPseudoprime( bi ) );
            assertFalse( passesMiller( i ) );
            assertFalse( passesMiller( bi ) );
            if ( isMersenneNumber( bi ) ) assertFalse( passesLucasLehmer( bi ) );
            assertFalse( isMersennePrime( bi ) );
            assertFalse( isFermatPrime( bi ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void isPrimeRange()
    {
        for ( int i = 0; i < 1000; i++ )
        {
            boolean composite = i == 0 || i == 1;
            for ( int j = 2; j < i; j++ )
            {
                if ( i % j == 0 )
                {
                    composite = true;
                    break;
                }
            }
            BigInteger bi = bi( i );
            assertEquals( !composite, isPrime( i ) );
            assertEquals( !composite, isPrime( bi ) );
            assertEquals( !composite, passesTrialDivision( i ) );
            assertEquals( !composite, passesTrialDivision( bi ) );
            if ( PRIMES_SET.contains( i ) || COMPOSITES_SET.contains( i ) )
            {
                assertEquals( composite, COMPOSITES_SET.contains( i ) );
                assertEquals( !composite, PRIMES_SET.contains( i ) );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void isPrimeLongSpecial()
    {
        for ( long l : TestUtils.getLongs() )
        {
            boolean prime = isPrime( l );
            assertEquals( prime, isPrime( -l ) );
            assertEquals( prime, passesMiller( l ) );
            assertEquals( prime, passesMiller( -l ) );
            if ( Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE )
            {
                assertEquals( prime, passesTrialDivision( l ) );
                assertEquals( prime, passesTrialDivision( -l ) );
                BigInteger bi = bi( l );
                assertEquals( prime, passesTrialDivision( bi ) );
                assertEquals( prime, passesTrialDivision( bi.negate() ) );
                if ( prime && l > 2L ) for ( int witness = 2; witness < 1000; witness++ ) if ( MathUtils.isRelativelyPrime( l, witness ) ) assertTrue( passesMillerRabin( ( int )l, witness ) );
            }
            if ( l == PrimesIterable.LAST_LONG_PRIME ) assertTrue( prime );
            if ( l == Integer.MAX_VALUE ) assertTrue( prime );
        }
    }
    
    @Test( timeout = 180000L )
    public void isPrimeBigIntegerSpecial()
    {
        BigInteger bis[] = TestUtils.getBigIntegers();
        for ( BigInteger bi : bis )
        {
            boolean prime = isPrime( bi );
            assertEquals( prime, isPrime( bi.negate() ) );
            assertEquals( prime, passesMiller( bi ) );
            assertEquals( prime, passesMiller( bi.negate() ) );
            assertEquals( prime, passesBailliePSW( bi ) );
            assertEquals( prime, passesBailliePSW( bi.negate() ) );
            if ( bi.compareTo( BigUtils.BI_TWO ) > 0 && bi.testBit( 0 ) && !BigUtils.isPerfectSquare( bi ) ) assertEquals( prime, passesLucasPseudoprime( bi ) );
            if ( isMersenneNumber( bi ) )
            {
                assertEquals( prime, passesLucasLehmer( bi ) );
                assertEquals( prime, isMersennePrime( bi ) );
            }
            if ( isFermatNumber( bi ) ) assertEquals( prime, isFermatPrime( bi ) );
            if ( prime )
            {
                assertTrue( bi.isProbablePrime( 1 ) );
                assertTrue( bi.isProbablePrime( 10 ) );
                assertTrue( bi.isProbablePrime( 100 ) );
                if ( bi.compareTo( BigUtils.BI_TWO ) > 0 )
                {
                    for ( BigInteger witness : bis )
                    {
                        if ( BigUtils.isRelativelyPrime( bi, witness ) )
                        {
                            assertTrue( passesMillerRabin( bi, witness ) );
                            if ( bi.bitLength() <= 31 && witness.bitLength() <= 31 )
                            {
                                assertTrue( passesMillerRabin( bi.intValue(), witness.intValue() ) );
                            }
                        }
                    }
                }
            }
            else
            {
                assertFalse( isMersennePrime( bi ) );
                assertFalse( isFermatPrime( bi ) );
            }
        }
    }
    
    @Test( timeout = 5000L )
    public void isMersenneNumberCasual()
    {
        assertTrue( isMersenneNumber( bi( 0 ) ) );
        assertTrue( isMersenneNumber( bi( 1 ) ) );
        assertTrue( isMersenneNumber( bi( 3 ) ) );
        assertTrue( isMersenneNumber( bi( 7 ) ) );
        assertTrue( isMersenneNumber( bi( 127 ) ) );
        assertTrue( isMersenneNumber( bi( Byte.MAX_VALUE ) ) );
        assertTrue( isMersenneNumber( bi( Short.MAX_VALUE ) ) );
        assertTrue( isMersenneNumber( bi( Integer.MAX_VALUE ) ) );
        assertTrue( isMersenneNumber( bi( Long.MAX_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( -1 ) ) );
        assertFalse( isMersenneNumber( bi( -2 ) ) );
        assertFalse( isMersenneNumber( bi( -3 ) ) );
        assertFalse( isMersenneNumber( bi( -4 ) ) );
        assertFalse( isMersenneNumber( bi( -5 ) ) );
        assertFalse( isMersenneNumber( bi( -6 ) ) );
        assertFalse( isMersenneNumber( bi( -7 ) ) );
        assertFalse( isMersenneNumber( bi( -8 ) ) );
        assertFalse( isMersenneNumber( bi( -9 ) ) );
        assertFalse( isMersenneNumber( bi( -Byte.MAX_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( Byte.MIN_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( -Short.MAX_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( Short.MIN_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( -Integer.MAX_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( Integer.MIN_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( -Long.MAX_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( Long.MIN_VALUE ) ) );
        assertFalse( isMersenneNumber( bi( Byte.MIN_VALUE ).negate() ) );
        assertFalse( isMersenneNumber( bi( Short.MIN_VALUE ).negate() ) );
        assertFalse( isMersenneNumber( bi( Integer.MIN_VALUE ).negate() ) );
        assertFalse( isMersenneNumber( bi( Long.MIN_VALUE ).negate() ) );
        assertFalse( isMersenneNumber( bi( 2 ) ) );
        assertFalse( isMersenneNumber( bi( 4 ) ) );
        assertFalse( isMersenneNumber( bi( 5 ) ) );
        assertFalse( isMersenneNumber( bi( 6 ) ) );
        assertFalse( isMersenneNumber( bi( 8 ) ) );
        assertFalse( isMersenneNumber( bi( 9 ) ) );
    }
    
    @Test( timeout = 5000L )
    public void isMersenneNumberRange()
    {
        for ( BigInteger n = BigInteger.ONE; n.bitLength() < 500; n = n.shiftLeft( 1 ) )
        {
            if ( n.compareTo( BigInteger.ONE ) > 0 ) assertFalse( isMersenneNumber( n ) );
            assertTrue( isMersenneNumber( n.subtract( BigInteger.ONE ) ) );
            if ( n.compareTo( BigUtils.BI_TWO ) > 0 ) assertFalse( isMersenneNumber( n.subtract( BigUtils.BI_TWO ) ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void isFermatNumberCasual()
    {
        assertTrue( isFermatNumber( bi( 3 ) ) );
        assertTrue( isFermatNumber( bi( 5 ) ) );
        assertTrue( isFermatNumber( bi( 17 ) ) );
        assertTrue( isFermatNumber( bi( 257 ) ) );
        assertTrue( isFermatNumber( bi( 65537 ) ) );
        assertTrue( isFermatNumber( bi( 4294967297L ) ) );
        assertTrue( isFermatNumber( bi( "18446744073709551617" ) ) );
        assertTrue( isFermatNumber( bi( "340282366920938463463374607431768211457" ) ) );
        assertTrue( isFermatNumber( bi( "115792089237316195423570985008687907853269984665640564039457584007913129639937" ) ) );
        assertFalse( isFermatNumber( bi( 0 ) ) );
        assertFalse( isFermatNumber( bi( -1 ) ) );
        assertFalse( isFermatNumber( bi( -2 ) ) );
        assertFalse( isFermatNumber( bi( -3 ) ) );
        assertFalse( isFermatNumber( bi( -4 ) ) );
        assertFalse( isFermatNumber( bi( -5 ) ) );
        assertFalse( isFermatNumber( bi( -6 ) ) );
        assertFalse( isFermatNumber( bi( -7 ) ) );
        assertFalse( isFermatNumber( bi( -8 ) ) );
        assertFalse( isFermatNumber( bi( -9 ) ) );
        assertFalse( isFermatNumber( bi( 1 ) ) );
        assertFalse( isFermatNumber( bi( 2 ) ) );
        assertFalse( isFermatNumber( bi( 4 ) ) );
        assertFalse( isFermatNumber( bi( 6 ) ) );
        assertFalse( isFermatNumber( bi( 7 ) ) );
        assertFalse( isFermatNumber( bi( 8 ) ) );
        assertFalse( isFermatNumber( bi( 9 ) ) );
        assertFalse( isFermatNumber( bi( 33 ) ) );
    }
    
    @Test( timeout = 5000L )
    public void isFermatNumberRange()
    {
        int p = 1;
        for ( BigInteger n = BigUtils.BI_TWO; p < 2000000; n = n.shiftLeft( p ), p <<= 1 )
        {
            assertFalse( isFermatNumber( n ) );
            assertTrue( isFermatNumber( n.add( BigInteger.ONE ) ) );
            assertFalse( isFermatNumber( n.subtract( BigUtils.BI_TWO ) ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void isFermatPrimeTest()
    {
        assertTrue( isFermatPrime( bi( 3 ) ) );
        assertTrue( isFermatPrime( bi( 5 ) ) );
        assertTrue( isFermatPrime( bi( 17 ) ) );
        assertTrue( isFermatPrime( bi( 257 ) ) );
        assertTrue( isFermatPrime( bi( 65537 ) ) );
        assertFalse( isFermatPrime( bi( 7 ) ) );
        assertFalse( isFermatPrime( bi( 11 ) ) );
        for ( BigInteger bi : TestUtils.getBigIntegers() )
        {
            boolean prime = bi.bitLength() <= 31;
            if ( prime )
            {
                switch ( bi.intValue() )
                {
                    case 3:
                    case 5:
                    case 17:
                    case 257:
                    case 65537:
                        break;
                    default:
                        prime = false;
                }
            }
            assertEquals( prime, isFermatPrime( bi ) );
            assertEquals( prime, isFermatNumber( bi ) && isPrime( bi ) );
            assertEquals( prime, isFermatNumber( bi ) && bi.isProbablePrime( 100 ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void isStrongPseudoprimeCasual()
    {
        assertTrue( passesMillerRabin( 3, 2 ) );
        assertFalse( passesMillerRabin( 9, 2 ) );
        assertFalse( passesMillerRabin( 55, 3 ) );
        assertTrue( passesMillerRabin( 7, 3 ) );
        assertTrue( passesMillerRabin( 5, 4 ) );
        assertFalse( passesMillerRabin( 9, 4 ) );
        // Sloane's A001262:
        assertTrue( passesMillerRabin( 2047, 2 ) );
        assertTrue( passesMillerRabin( 3277, 2 ) );
        assertTrue( passesMillerRabin( 4033, 2 ) );
        // Sloane's A020229:
        assertTrue( passesMillerRabin( 121, 3 ) );
        assertTrue( passesMillerRabin( 1891, 3 ) );
        assertTrue( passesMillerRabin( 8401, 3 ) );
        // Sloane's A020230:
        assertTrue( passesMillerRabin( 341, 4 ) );
        assertTrue( passesMillerRabin( 4033, 4 ) );
        assertTrue( passesMillerRabin( 4371, 4 ) );
    }
    
    @Test( timeout = 5000L )
    public void isStrongPseudoprimeSpecial()
    {
        int ints[] = TestUtils.getInts();
        for ( int n : ints )
        {
            for ( int base : ints )
            {
                if ( n > 1 && ( n & 1 ) == 1 && MathUtils.isRelativelyPrime( n, base ) ) assertEquals( passesMillerRabin( n, base ), passesMillerRabin( bi( n ), bi( base ) ) );
            }
        }
    }
}