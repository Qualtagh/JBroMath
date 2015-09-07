package org.quinto.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Math functions with BigInteger and BigDecimal arguments.
 */
public class BigUtils
{
    /**
     * BigInteger constants.
     * {@link java.math.BigInteger} contains predefined constants only for values:<ul>
     * <li>0 - {@link java.math.BigInteger#ZERO}</li>
     * <li>1 - {@link java.math.BigInteger#ONE}</li>
     * <li>10 - {@link java.math.BigInteger#TEN}</li></ul>
     * Here are some extra small values defined.
     */
    
    /** Value of 2. Used in {@link PrimeUtils#passesTrialDivision(java.math.BigInteger)}. */
    public static final BigInteger BI_TWO = BigInteger.valueOf( 2 );
    /** Value of 3. Used in {@link #getBaseOfPerfectSquare}. */
    public static final BigInteger BI_THREE = BigInteger.valueOf( 3 );
    /** Value of 4. Used in {@link PrimeUtils#passesTrialDivision(java.math.BigInteger)}. */
    public static final BigInteger BI_FOUR = BigInteger.valueOf( 4 );
    /** Value of 5. Used in {@link PrimeUtils#passesTrialDivision(java.math.BigInteger)}. */
    public static final BigInteger BI_FIVE = BigInteger.valueOf( 5 );
    /** Value of 9. Used in {@link #getBaseOfPerfectSquare}. */
    public static final BigInteger BI_NINE = BigInteger.valueOf( 9 );
    
    /**
     * Value of Integer.MAX_VALUE
     * @see java.lang.Integer#MAX_VALUE
     */
    public static final BigInteger BI_MAX_INT = BigInteger.valueOf( Integer.MAX_VALUE );
    
    /**
     * Value of Integer.MIN_VALUE
     * @see java.lang.Integer#MIN_VALUE
     */
    public static final BigInteger BI_MIN_INT = BigInteger.valueOf( Integer.MIN_VALUE );
    
    /**
     * Value of Long.MAX_VALUE
     * @see java.lang.Long#MAX_VALUE
     */
    public static final BigInteger BI_MAX_LONG = BigInteger.valueOf( Long.MAX_VALUE );
    
    /**
     * Value of Long.MIN_VALUE
     * @see java.lang.Long#MIN_VALUE
     */
    public static final BigInteger BI_MIN_LONG = BigInteger.valueOf( Long.MIN_VALUE );
    
    /**
     * Value of Long.MAX_VALUE + 2.
     * Used in {@link MathUtils#toUnsignedBigInteger}.
     */
    public static final BigInteger BI_MAX_LONG_PLUS_TWO = BI_MAX_LONG.add( BI_TWO );
    
    /**
     * Value of 11 * 17 * 19 * 23 * 31 * 25 * 63.
     * Used in {@link #getBaseOfPerfectSquare}.
     */
    private static final BigInteger PERFECT_SQUARE_FILTER_MODULO = BigInteger.valueOf( 3989930175L );
    
    /**
     * BigDecimal constants.
     * {@link java.math.BigDecimal} contains predefined constants only for values:<ul>
     * <li>0 - {@link java.math.BigDecimal#ZERO}</li>
     * <li>1 - {@link java.math.BigDecimal#ONE}</li>
     * <li>10 - {@link java.math.BigDecimal#TEN}</li></ul>
     * Here are some extra small values defined.
     */
    
    /** Value of 0.5 */
    public static final BigDecimal BD_HALF = new BigDecimal( "0.5" );
    /** Value of 2 */
    public static final BigDecimal BD_TWO = BigDecimal.valueOf( 2 );
    /** Value of 3 */
    public static final BigDecimal BD_THREE = BigDecimal.valueOf( 3 );
    /** Value of 4 */
    public static final BigDecimal BD_FOUR = BigDecimal.valueOf( 4 );
    /** Value of 5 */
    public static final BigDecimal BD_FIVE = BigDecimal.valueOf( 5 );
    
    /**
     * Given integer number n, find integer number s such that s<sup>2</sup> = n.<br>
     * Return null if such number s doesn't exists.
     * <p>Perfect square: http://en.wikipedia.org/wiki/Square_number
     * <p>The implementation is a Java port of this code:<br>
     * http://cpansearch.perl.org/src/DANAJ/Math-Prime-Util-0.11/factor.c
     * @param n an integer square number
     * @return integer number s such that s<sup>2</sup> = n<br>
     * or null if such number s doesn't exist
     */
    public static BigInteger getBaseOfPerfectSquare( BigInteger n )
    {
        int signum = n.signum();
        // Negative value cannot be square.
        if ( signum <= 0 ) return signum == 0 ? BigInteger.ZERO : null;
        if ( n.bitLength() <= 63 )
        {
            long ret = MathUtils.getBaseOfPerfectSquare( n.longValue() );
            return ret == MathUtils.NOT_FOUND ? null : BigInteger.valueOf( ret );
        }
        int m = n.intValue() & 127;
        // After this step 82% of non-squares would be rejected.
        if ( ( ( m * 0x8BC40D7D ) & ( m * 0xA1E2F5D1 ) & 0x14020A ) != 0 ) return null;
        // If n = m * 2 ^ q then q must be even. Otherwise n cannot be a perfect square.
        if ( ( n.getLowestSetBit() & 1 ) == 1 ) return null;
        // Here is a Bloom filter applied. It allows to reject 99.92% of non-squares.
        // Information about this Bloom filter:
        // http://mersenneforum.org/showpost.php?p=110896
        int lm = n.mod( PERFECT_SQUARE_FILTER_MODULO ).intValue();
        m = MathUtils.remainderUnsigned( lm, 63 );
        if ( ( ( m * 0x3D491DF7 ) & ( m * 0xC824A9F9 ) & 0x10F14008 ) != 0 ) return null;
        m = MathUtils.remainderUnsigned( lm, 25 );
        if ( ( ( m * 0x1929FC1B ) & ( m * 0x4C9EA3B2 ) & 0x51001005 ) != 0 ) return null;
        m = 0xD10D829A * MathUtils.remainderUnsigned( lm, 31 );
        if ( ( m & ( m + 0x672A5354 ) & 0x21025115 ) != 0 ) return null;
        m = MathUtils.remainderUnsigned( lm, 23 );
        if ( ( ( m * 0x7BD28629 ) & ( m * 0xE7180889 ) & 0xF8300 ) != 0 ) return null;
        m = MathUtils.remainderUnsigned( lm, 19 );
        if ( ( ( m * 0x1B8BEAD3 ) & ( m * 0x4D75A124 ) & 0x4280082B ) != 0 ) return null;
        m = MathUtils.remainderUnsigned( lm, 17 );
        if ( ( ( m * 0x6736F323 ) & ( m * 0x9B1D499 ) & 0xC0000300 ) != 0 ) return null;
        m = MathUtils.remainderUnsigned( lm, 11 );
        if ( ( ( m * 0xABF1A3A7 ) & ( m * 0x2612BF93 ) & 0x45854000 ) != 0 ) return null;
        BigInteger ret = isqrt( n );
        if ( ret.multiply( ret ).equals( n ) ) return ret;
        return null;
    }
    
    /**
     * Determine if a given number n is perfect square.<br>
     * http://en.wikipedia.org/wiki/Square_number
     * @param n number to check
     * @return true if and only if there exists integer number s such that s<sup>2</sup> = n
     */
    public static boolean isPerfectSquare( BigInteger n )
    {
        return getBaseOfPerfectSquare( n ) != null;
    }
    
    /**
     * Returns integer square root of n.
     * <p>Integer square root: http://en.wikipedia.org/wiki/Integer_square_root
     * <p>The greatest integer less than or equal to the square root of n. Example:<br>
     * isqrt( 27 ) = 5 because 5 * 5 = 25 &le; 27 and 6 * 6 = 36 &gt; 27
     * @param n radicand
     * @return trunc( sqrt( n ) )
     * @throws ArithmeticException if n &lt; 0
     */
    public static BigInteger isqrt( BigInteger n ) throws ArithmeticException
    {
        int signum = n.signum();
        if ( signum <= 0 )
        {
            if ( signum == 0 ) return BigInteger.ZERO;
            throw new ArithmeticException( "Square root of negative number is undefined" );
        }
        int bitLength = n.bitLength();
        if ( bitLength <= 63 ) return BigInteger.valueOf( MathUtils.isqrt( n.longValue() ) );
        // The source of the code below and explanations:
        // http://www.codecodex.com/wiki/Calculate_an_integer_square_root#Java
        // Last even numbered bit in first 64 bits.
        int bit = Math.max( 0, ( bitLength - 63 ) & 0xFFFFFFFE );
        BigInteger root = BigInteger.valueOf( MathUtils.uisqrt( n.shiftRight( bit ).longValue() ) & 0xFFFFFFFFL );
        bit >>>= 1;
        root = root.shiftLeft( bit );
        while ( bit != 0 )
        {
            bit--;
            BigInteger rootHigh = root.setBit( bit );
            if ( rootHigh.multiply( rootHigh ).compareTo( n ) <= 0 ) root = rootHigh;
        }
        return root;
    }
    
    /**
     * Signed mod.<br>
     * The value returned lies in range [ -( |m| - 1 ) / 2 .. |m| / 2 ].<br>
     * If m = 0 then ArithmeticException is thrown.
     * @param v a value
     * @param m a modulus
     * @return v (mod m)
     * @throws ArithmeticException when m = 0
     */
    public static BigInteger mods( BigInteger v, BigInteger m ) throws ArithmeticException
    {
        int signum = m.signum();
        if ( signum == 0 ) throw new ArithmeticException( "Zero modulus" );
        if ( signum < 0 ) m = m.negate();
        v = v.remainder( m );
        if ( v.compareTo( m.shiftRight( 1 ) ) > 0 ) v = v.subtract( m );
        else if ( v.compareTo( m.subtract( BigInteger.ONE ).shiftRight( 1 ).negate() ) < 0 ) v = v.add( m );
        return v;
    }
    
    /**
     * Returns v (mod m).<br>
     * The value returned lies in range [ 0 .. |m| - 1 ].<br>
     * mod( x, m ) = mod( x, -m ).<br>
     * This method differs from {@link java.math.BigInteger#mod} in that it supports negative modulus.<br>
     * If m = 0 then ArithmeticException is thrown.
     * @param v a value
     * @param m a modulus
     * @return v (mod m)
     * @throws ArithmeticException when m = 0
     */
    public static BigInteger mod( BigInteger v, BigInteger m ) throws ArithmeticException
    {
        return v.mod( m.abs() );
    }
}