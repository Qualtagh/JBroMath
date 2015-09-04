package org.quinto.math;

import java.math.BigInteger;

/**
 * Primality tests.
 */
public class PrimeUtils
{
    /**
     * Deterministic primality test. Polynomial time.
     * <ul><li>Negative number n is considered prime if -n is prime.</li>
     * <li>Numbers 0 and 1 aren't prime.</li>
     * <li>Examples of first prime numbers: 2, 3, 5, 7, 11, 13.</li></ul>
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean isPrime( long n )
    {
        return passesMiller( n );
    }
    
    /**
     * Deterministic primality test. Exponential time in worst case (current implementation, may be improved in future).
     * <ul><li>Negative number n is considered prime if -n is prime.</li>
     * <li>Numbers 0 and 1 aren't prime.</li>
     * <li>Examples of first prime numbers: 2, 3, 5, 7, 11, 13.</li></ul>
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean isPrime( BigInteger n )
    {
        n = n.abs();
        int length = n.bitLength();
        if ( length < 64 ) return isPrime( n.longValue() );
        if ( !n.testBit( 0 ) ) return false;
        // Some number classes have more efficient primality tests.
        // Use trial division if the number is small. Otherwise try to determine number class.
        // Mersenne numbers can be checked by Lucas-Lehmer test.
        if ( isMersenneNumber( n ) ) return passesLucasLehmer( n, length );
        // Fermat numbers can be checked by Pepin's test.
        // All numbers in BigInteger range have been verified
        // and only first 5 of them are prime: 3, 5, 17, 257 and 65537.
        // They are already accepted at this step by isPrime( long ).
        // The rest Fermat numbers are definitely composite.
        if ( isFermatNumber( n ) ) return false;
        // This test is required for Lucas test correctness.
        // Lucas test is performed inside BigInteger.isProbablePrime.
        // Miller-Rabin test are performed there at random bases,
        // so it's not guaranteed that the base 2 would be checked.
        if ( !passesMillerRabin( n, BigUtils.BI_TWO ) ) return false;
        // Probabilistic tests of Miller-Rabin and Lucas for optimization purposes.
        if ( !n.isProbablePrime( 100 ) ) return false;
        // Deterministic check.
        // Baillie-PSW test for now.
        // Should be replaced by some proven polynomial-time deterministic test in future.
        return passesBailliePSW( n );
    }
    
    /**
     * Trial division deterministic test. Exponential time.
     * <p>Implementation is based on <a href="http://stackoverflow.com/questions/2385909/most-elegant-way-to-write-isprime-in-java">this</a> StackOverflow question.</p>
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean passesTrialDivision( long n )
    {
        if ( n < 0L ) n = -n;
        // Values 0, 1 and Long.MIN_VALUE would be rejected at this step.
        if ( n < 2L ) return false;
        if ( n < 4L ) return true;
        if ( ( n & 1L ) == 0L ) return false;
        if ( n % 3L == 0L ) return false;
        long sqrt = ( long )Math.sqrt( n ) + 1L;
        for ( long i = 6L; i <= sqrt; i += 6L ) if ( n % ( i - 1L ) == 0L || n % ( i + 1L ) == 0L ) return false;
        return true;
    }
    
    /**
     * Trial division deterministic test. Exponential time.
     * <p>Implementation is based on <a href="http://stackoverflow.com/questions/2385909/most-elegant-way-to-write-isprime-in-java">this</a> StackOverflow question.</p>
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean passesTrialDivision( BigInteger n )
    {
        n = n.abs();
        if ( !n.testBit( 0 ) ) return n.equals( BigUtils.BI_TWO );
        if ( n.remainder( BigUtils.BI_THREE ).signum() == 0 ) return n.equals( BigUtils.BI_THREE );
        BigInteger b = BigUtils.isqrt( n ).add( BigInteger.ONE );
        boolean switch42 = false;
        for ( BigInteger i = BigUtils.BI_FIVE; i.compareTo( b ) <= 0; i = i.add( switch42 ? BigUtils.BI_FOUR : BigUtils.BI_TWO ), switch42 = !switch42 ) if ( n.remainder( i ).signum() == 0 ) return false;
        return !n.equals( BigInteger.ONE );
    }

    /**
     * Method to check if a given number is a Mersenne number (primality of the number is not checked).
     * Linear time.
     * https://en.wikipedia.org/wiki/Mersenne_prime
     * @param n a checking number
     * @return true iff log<sub>2</sub>( n + 1 ) is a natural number
     */
    public static boolean isMersenneNumber( BigInteger n )
    {
        if ( n.signum() <= 0 ) return false;
        byte bytes[] = n.toByteArray();
        byte b = bytes[ 0 ];
        if ( ( b & ( b + 1 ) ) != 0 ) return false;
        for ( int i = 1; i < bytes.length; i++ ) if ( bytes[ i ] != -1 ) return false;
        return true;
    }

    /**
     * Lucas-Lehmer deterministic primality test for Mersenne numbers. Polynomial time.
     * @param n a Mersenne number M<sub>p</sub> = 2<sup>p</sup> - 1
     * @param p a base of n, i.e. p = log<sub>2</sub>( n + 1 )
     * @return true iff n is prime
     */
    private static boolean passesLucasLehmer( BigInteger n, int p )
    {
        if ( p <= 1 ) return false;
        if ( p <= 3 ) return true;
        // Mersenne number can be prime only if its base is prime.
        if ( !isPrime( p ) ) return false;
        // Optimization: Mersenne numbers with prime base p in the form 4 * k + 3 are composite if 2 * p + 1 is prime.
        // http://mathworld.wolfram.com/MersennePrime.html
        if ( ( p & 3 ) == 3 && isPrime( ( ( long )p << 1 ) + 1L ) ) return false;
        // Lucas-Lehmer primality test.
        BigInteger x = BigUtils.BI_FOUR;
        while ( p > 2 )
        {
            x = x.modPow( BigUtils.BI_TWO, n );
            if ( x.signum() == 0 ) x = n.subtract( BigUtils.BI_TWO );
            else if ( x.equals( BigInteger.ONE ) ) x = n.subtract( BigInteger.ONE );
            else x = x.subtract( BigUtils.BI_TWO );
            p--;
        }
        return x.signum() == 0;
    }

    /**
     * Mersenne numbers deterministic primality test
     * (Mersenne number is an integer in the form M<sub>p</sub> = 2<sup>p</sup> - 1).
     * Polynomial time.
     * @param n a checking number
     * @return true iff n is a Mersenne number and n is prime
     */
    public static boolean isMersennePrime( BigInteger n )
    {
        if ( !isMersenneNumber( n ) ) return false;
        return passesLucasLehmer( n, n.bitLength() );
    }

    /**
     * Method to check if a given number is a Fermat number. Linear time.
     * https://en.wikipedia.org/wiki/Fermat_number
     * @param f a checking number
     * @return true iff there exists a non-negative integer n such that f = 2<sup>2<sup>^n</sup></sup> + 1
     */
    public static boolean isFermatNumber( BigInteger f )
    {
        if ( f.signum() <= 0 ) return false;
        byte bytes[] = f.toByteArray();
        int bLength = bytes.length - 1;
        if ( bLength == 0 )
        {
            switch ( bytes[ 0 ] )
            {
                case 3:
                case 5:
                case 17:
                    return true;
                default:
                    return false;
            }
        }
        // Checking if bLength is a power of 2.
        // http://en.wikipedia.org/wiki/Power_of_two#Fast_algorithm_to_check_if_a_positive_number_is_a_power_of_two
        if ( ( bLength & ( bLength - 1 ) ) != 0 ) return false;
        if ( bytes[ 0 ] != 1 || bytes[ bLength ] != 1 ) return false;
        for ( int i = 1; i < bLength; i++ ) if ( bytes[ i ] != 0 ) return false;
        return true;
    }

    /**
     * Fermat number deterministic primality test
     * (Fermat number is an integer in the form F<sub>n</sub> = 2<sup>2<sup>^n</sup></sup> + 1).
     * Constant time.
     * @param f a checking number
     * @return true iff f is a Fermat number and f is prime
     */
    public static boolean isFermatPrime( BigInteger f )
    {
        if ( f.signum() <= 0 ) return false;
        if ( f.bitLength() <= 31 )
        {
            // The only prime Fermat numbers in BigInteger range
            // are the first five: 3, 5, 17, 257, 65537.
            switch ( f.intValue() )
            {
                case 3:
                case 5:
                case 17:
                case 257:
                case 65537:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Lucas probabilistic polynomial-time primality test.
     * This is a method {@code passesLucasLehmer} from a standard Java class BigInteger.
     * Used in Baillie-PSW primality test.
     * @see java.math.BigInteger#passesLucasLehmer
     * @param n an odd integer greater than one, not a strong pseudoprime to base 2, not a perfect square
     * @return false if n is composite, true if n is a probable prime (with high probability)
     */
    public static boolean passesLucas( BigInteger n )
    {
        int z = 5;
        while ( jacobiSymbol( z, n ) != -1 ) z = z < 0 ? 2 - z : -( z + 2 );
        BigInteger d = BigInteger.valueOf( z );
        BigInteger u = BigInteger.ONE;
        BigInteger u2;
        BigInteger v = BigInteger.ONE;
        BigInteger v2;
        BigInteger k = n.add( BigInteger.ONE );
        for ( int i = k.bitLength() - 2; i >= 0; i-- )
        {
            u2 = u.multiply( v ).mod( n );
            v2 = v.multiply( v ).add( d.multiply( u.multiply( u ) ) ).mod( n );
            if ( v2.testBit( 0 ) ) v2 = v2.subtract( n );
            v2 = v2.shiftRight( 1 );
            u = u2;
            v = v2;
            if ( k.testBit( i ) )
            {
                u2 = u.add( v ).mod( n );
                if ( u2.testBit( 0 ) ) u2 = u2.subtract( n );
                u2 = u2.shiftRight( 1 );
                v2 = v.add( d.multiply( u ) ).mod( n );
                if ( v2.testBit( 0 ) ) v2 = v2.subtract( n );
                v2 = v2.shiftRight( 1 );
                u = u2;
                v = v2;
            }
        }
        return u.mod( n ).signum() == 0;
    }

    /**
     * Jacobi symbol p/n calculation.
     * This method is taken from standard Java class BigInteger.
     * Origin: Colin Plumb's C library.
     * https://en.wikipedia.org/wiki/Jacobi_symbol
     * @see java.math.BigInteger#jacobiSymbol
     * @param n an odd integer greater than one, not a perfect square
     */
    private static int jacobiSymbol( int p, BigInteger n )
    {
        if ( p == 0 ) return 0;
        int u = n.intValue();
        int j = 1;
        if ( p < 0 )
        {
            p = -p;
            int n8 = u & 7;
            if ( n8 == 3 || n8 == 7 ) j = -j;
        }
        while ( ( p & 3 ) == 0 ) p >>= 2;
        if ( ( p & 1 ) == 0 )
        {
            p >>= 1;
            if ( ( ( u ^ ( u >> 1 ) ) & 2 ) != 0 ) j = -j;
        }
        if ( p == 1 ) return j;
        if ( ( p & u & 2 ) != 0 ) j = -j;
        u = n.mod( BigInteger.valueOf( p ) ).intValue();
        while ( u != 0 )
        {
            while ( ( u & 3 ) == 0 ) u >>= 2;
            if ( ( u & 1 ) == 0 )
            {
                u >>= 1;
                if ( ( ( p ^ ( p >> 1 ) ) & 2 ) != 0 ) j = -j;
            }
            if ( u == 1 ) return j;
            int t = u;
            u = p;
            p = t;
            if ( ( u & p & 2 ) != 0 ) j = -j;
            u %= p;
        }
        return 0;
    }

    /**
     * Strong pseudoprimality test with a given base.
     * <p>https://en.wikipedia.org/wiki/Strong_pseudoprime</p>
     * @param n an odd integer greater than one
     * @param base a checking pseudoprimality base
     * @return true iff n is prime or n is strong pseudoprime to a given base
     */
    public static boolean passesMillerRabin( int n, int base )
    {
        int nMinusOne = n - 1;
        int a = Integer.numberOfTrailingZeros( nMinusOne );
        int m = nMinusOne >> a;
        return internalPassesMillerRabin( n, base, nMinusOne, m, a );
    }

    /**
     * Strong pseudoprimality test with a given base.
     * This method is an adaptation of the method {@code passesMillerRabin} of a standard Java class BigInteger.
     * The difference is that the base is defined as an argument and is not generated randomly.
     * @param n an odd integer greater than one
     * @param base a checking pseudoprimality base
     * @return true iff n is prime or n is strong pseudoprime to a given base
     */
    public static boolean passesMillerRabin( BigInteger n, BigInteger base )
    {
        BigInteger nMinusOne = n.subtract( BigInteger.ONE );
        BigInteger m = nMinusOne;
        int a = m.getLowestSetBit();
        m = m.shiftRight( a );
        return internalPassesMillerRabin( n, base, nMinusOne, m, a );
    }
    
    private static boolean internalPassesMillerRabin( int n, int base, int nMinusOne, int m, int a )
    {
        int j = 0;
        int z = MathUtils.modPow( base, m, n );
        while ( ( j != 0 || z != 1 ) && z != nMinusOne )
        {
            if ( j > 0 && z == 1 ) return false;
            j++;
            if ( j == a ) return false;
            z = MathUtils.modPow( z, 2, n );
        }
        return true;
    }
    
    private static boolean internalPassesMillerRabin( BigInteger n, BigInteger base, BigInteger nMinusOne, BigInteger m, int a )
    {
        int j = 0;
        BigInteger z = base.modPow( m, n );
        while ( ( j != 0 || !z.equals( BigInteger.ONE ) ) && !z.equals( nMinusOne ) )
        {
            if ( j > 0 && z.equals( BigInteger.ONE ) ) return false;
            j++;
            if ( j == a ) return false;
            z = z.modPow( BigUtils.BI_TWO, n );
        }
        return true;
    }
    
    /**
     * Deterministic Miller primality test. Polynomial time.
     * <p>This test is correct for any integer in {@code long} range and does not rely on Riemann hypothesis.
     * <a href="https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test#Deterministic_variants_of_the_test">Verified</a> for all long int numbers.
     * A probabilistic Miller-Rabin test is implemented in a standard Java class BigInteger.</p>
     * @see java.math.BigInteger#isProbablePrime
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean passesMiller( long n )
    {
        if ( n < 0L ) n = -n;
        // Long.MIN_VALUE would be rejected here too.
        if ( ( n & 1L ) == 0L ) return n == 2L;
        // Smallest odd number that requires q Miller-Rabin primality tests.
        // Sloane's A006945: http://oeis.org/A006945
        int q;
        if ( n < 2152302898747L )
        {
            if ( n < 1373653L )
            {
                if ( n < 2047L )
                {
                    if ( n < 9L ) return n > 1L;
                    else q = 1;
                }
                else q = 2;
            }
            else
            {
                if ( n < 3215031751L ) q = n < 25326001L ? 3 : 4;
                else q = 5;
            }
        }
        else if ( n < 341550071728321L ) q = n < 3474749660383L ? 6 : 7;
        else q = n < 3825123056546413051L ? 9 : 12;
        long nMinusOne = n - 1;
        int s = Long.numberOfTrailingZeros( nMinusOne );
        long r = nMinusOne >> s;
        if ( n <= Integer.MAX_VALUE )
        {
            for ( int witness : PrimesIterable.getIntegerTotally( q ) ) if ( !internalPassesMillerRabin( ( int )n, witness, ( int )nMinusOne, ( int )r, s ) ) return false;
        }
        else
        {
            BigInteger br = BigInteger.valueOf( r );
            BigInteger bn = BigInteger.valueOf( n );
            for ( BigInteger witness : PrimesIterable.getBigIntegerTotally( BigInteger.valueOf( q ) ) )
            {
                BigInteger bRemainder = witness.modPow( br, bn );
                long remainder = bRemainder.longValue();
                int j = 0;
                while ( ( j > 0 || remainder != 1L ) && remainder != nMinusOne )
                {
                    if ( j > 0 && remainder == 1L ) return false;
                    j++;
                    if ( j == s ) return false;
                    bRemainder = bRemainder.modPow( BigUtils.BI_TWO, bn );
                    remainder = bRemainder.longValue();
                }
            }
        }
        return true;
    }
    
    /**
     * Deterministic Miller primality test. Polynomial time.
     * <p>This test relies on the truth of the generalized Riemann hypothesis (GRH) which is not proved yet.
     * A probabilistic Miller-Rabin test is implemented in a standard Java class BigInteger and does not rely on GRH.</p>
     * @see java.math.BigInteger#isProbablePrime
     * @param n a number to check for primality
     * @return true if and only if absolute value of n is prime
     */
    public static boolean passesMiller( BigInteger n )
    {
        n = n.abs();
        if ( !n.testBit( 0 ) ) return n.equals( BigUtils.BI_TWO );
        long q = n.bitLength();
        if ( q < 64 ) return passesMiller( n.longValue() );
        // A number is prime if it's strong pseudoprime to all bases from 2 to 2 * log( n ) ^ 2.
        q *= q;
        BigInteger nMinusOne = n.subtract( BigInteger.ONE );
        BigInteger m = nMinusOne;
        int a = m.getLowestSetBit();
        m = m.shiftRight( a );
        for ( BigInteger witness : PrimesIterable.getBigIntegerMax( BigInteger.valueOf( q ).shiftLeft( 1 ) ) ) if ( !internalPassesMillerRabin( n, witness, nMinusOne, m, a ) ) return false;
        return true;
    }
    
    /**
     * Probabilistic Miller-Rabin primality test. Polynomial time.
     * <p>This test does not rely on GRH.</p>
     * @see java.math.BigInteger#isProbablePrime
     * @param n a number to check for primality
     * @return false if n is composite, true if n is a probable prime
     */
    public static boolean passesMillerRabin( BigInteger n )
    {
        // Maximal certainity according to the implementation.
        return n.isProbablePrime( 100 );
    }

    /**
     * Probabilistic Baillie-Pomerance-Selfridge-Wagstaff primality test. Polynomial time.
     * http://mathworld.wolfram.com/Baillie-PSWPrimalityTest.html
     * There are no composite numbers found that pass this test yet.
     * All numbers in {@code long} range are verified.
     * It's assumed that there are no such numbers less than 10<sup>10000</sup> (otherwise they would have been already found).
     * http://en.wikipedia.org/wiki/Baillie%E2%80%93PSW_primality_test
     * @param n a checking number
     * @return false if n is composite, true if n is a probable prime (with high probability)
     */
    public static boolean passesBailliePSW( BigInteger n )
    {
        n = n.abs();
        if ( n.bitLength() <= 31 )
        {
            int nn = n.intValue();
            if ( nn <= 1 ) return false;
            if ( nn <= 3 ) return true;
            if ( ( nn & 1 ) == 0 ) return false;
            if ( MathUtils.isPerfectSquare( nn ) ) return false;
            if ( !passesMillerRabin( nn, 2 ) ) return false;
        }
        else
        {
            if ( !n.testBit( 0 ) ) return false;
            if ( !passesMillerRabin( n, BigUtils.BI_TWO ) ) return false;
            if ( BigUtils.isPerfectSquare( n ) ) return false;
        }
        return passesLucas( n );
    }
    
    /**
     * Gaussian integer primality test.
     * <p>Gaussian integer is a complex number whose real and imaginary parts are both integers.
     * Prime gaussian number is a gaussian non-zero integer that has no divisors except the trivial ones.
     * Divisors of 1 are: 1, -1, i and -i. Trivial divisors of n are divisors of 1 and their multiplications by n.
     * https://en.wikipedia.org/wiki/Gaussian_integer</p>
     * @param real a real part of a checking number
     * @param imaginary an imaginary part of a checking number
     * @return true iff real + i * imaginary is prime
     */
    public static boolean isGaussianPrime( long real, long imaginary )
    {
        // Gauss criteria:
        // Gaussian integer a + b * i is prime iff one of the conditions holds:
        // 1. One of numbers a or b equals to zero, another number is a prime in the form 4 * n + 3 or -4 * n - 3 (n is some natural number);
        // 2. Both a and b aren't zero and the norm a * a + b * b is a prime number.
        if ( real == 0L ) return Math.abs( imaginary ) % 4L == 3L && isPrime( imaginary );
        if ( imaginary == 0L ) return Math.abs( real ) % 4L == 3L && isPrime( real );
        return isPrime( real * real + imaginary * imaginary );
    }
    
    /**
     * Gaussian integer primality test.
     * <p>Gaussian integer is a complex number whose real and imaginary parts are both integers.
     * Prime gaussian number is a gaussian non-zero integer that has no divisors except the trivial ones.
     * Divisors of 1 are: 1, -1, i and -i. Trivial divisors of n are divisors of 1 and their multiplications by n.
     * https://en.wikipedia.org/wiki/Gaussian_integer</p>
     * @param real a real part of a checking number
     * @param imaginary an imaginary part of a checking number
     * @return true iff real + i * imaginary is prime
     */
    public static boolean isGaussianPrime( BigInteger real, BigInteger imaginary )
    {
        // Gauss criteria:
        // Gaussian integer a + b * i is prime iff one of the conditions holds:
        // 1. One of numbers a or b equals to zero, another number is a prime in the form 4 * n + 3 or -4 * n - 3 (n is some natural number);
        // 2. Both a and b aren't zero and the norm a * a + b * b is a prime number.
        if ( real.signum() == 0 )
        {
            imaginary = imaginary.abs();
            return imaginary.testBit( 0 ) && imaginary.testBit( 1 ) && isPrime( imaginary );
        }
        if ( imaginary.signum() == 0 )
        {
            real = real.abs();
            return real.testBit( 0 ) && real.testBit( 1 ) && isPrime( real );
        }
        return isPrime( real.multiply( real ).add( imaginary.multiply( imaginary ) ) );
    }
}