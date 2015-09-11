package org.quinto.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fraction extends Number implements Comparable
{
    public static final Fraction NaN = new Fraction( 0L, 0L, false );
    public static final Fraction POSITIVE_INFINITY = new Fraction( 1L, 0L, false );
    public static final Fraction NEGATIVE_INFINITY = new Fraction( -1L, 0L, false );
    public static final Fraction ZERO = new Fraction( 0L, 1L, false );
    public static final Fraction ONE = new Fraction( 1L, 1L, false );
    
    private static final Pattern DOUBLE_MINUS_PATTERN = Pattern.compile( "--" );
    private static final Pattern PLUS_MINUS_PATTERN = Pattern.compile( "\\+-" );
    private static final Pattern SPACE_PATTERN = Pattern.compile( "\\p{Space}" );
    private static final Pattern FLOAT_NUMBER_PATTERN = Pattern.compile( "(\\d++)\\.(\\d++)" );

    private long numerator;
    private long denominator;
    private transient Integer hash;

    private Fraction( long numerator, long denominator, boolean reduce )
    {
        this.numerator = numerator;
        this.denominator = denominator;
        if ( reduce ) reduce();
    }
    
    public static Fraction valueOf( long numerator, long denominator )
    {
        return valueOf( numerator, denominator, true );
    }
    
    private static Fraction valueOf( long numerator, long denominator, boolean reduce )
    {
        if ( denominator == 0L ) return numerator > 0L ? POSITIVE_INFINITY : numerator < 0L ? NEGATIVE_INFINITY : NaN;
        if ( numerator == 0L ) return ZERO;
        if ( numerator == denominator ) return ONE;
        return new Fraction( numerator, denominator, reduce );
    }

    public long getDenominator()
    {
        return denominator;
    }

    public long getNumerator()
    {
        return numerator;
    }

    @Override
    public double doubleValue()
    {
        return ( double )numerator / denominator;
    }

    @Override
    public float floatValue()
    {
        return ( float )doubleValue();
    }

    @Override
    public long longValue()
    {
        return denominator == 0L ? ( long )doubleValue() : numerator / denominator;
    }
    
    @Override
    public int intValue()
    {
        long ret = longValue();
        if ( ret > Integer.MAX_VALUE ) return Integer.MAX_VALUE;
        if ( ret < Integer.MIN_VALUE ) return Integer.MIN_VALUE;
        return ( int )ret;
    }

    @Override
    public String toString()
    {
        return denominator == Long.MIN_VALUE ? -numerator + "/9223372036854775808" : numerator + "/" + denominator;
    }

    public String toStringAfterDivision()
    {
        return denominator == Long.MIN_VALUE ? -numerator + "*9223372036854775808" : numerator + "*" + denominator;
    }

    private void reduce()
    {
        if ( denominator == 0L ) numerator = numerator > 0L ? 1L : numerator < 0L ? -1L : 0L;
        else if ( numerator == 0L ) denominator = 1L;
        else
        {
            long gcd = MathUtils.gcd( numerator, denominator );
            numerator /= gcd;
            denominator /= gcd;
            if ( denominator < 0L && denominator != Long.MIN_VALUE && numerator != Long.MIN_VALUE )
            {
                denominator = -denominator;
                numerator = -numerator;
            }
        }
    }

    @Override
    public Fraction clone()
    {
        return valueOf( numerator, denominator, false );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj ) return true;
        if ( !( obj instanceof Number ) ) return false;
        if ( obj instanceof Fraction )
        {
            Fraction rd = ( Fraction )obj;
            return numerator == rd.numerator && denominator == rd.denominator;
        }
        Number n = ( Number )obj;
        double nd = n.doubleValue();
        double d = doubleValue();
        if ( Double.isNaN( nd ) ) return Double.isNaN( d );
        return nd == d;
    }

    @Override
    public int compareTo( Object obj )
    {
        if ( this == obj ) return 0;
        if ( !( obj instanceof Number ) ) return 1;
        Number n = ( Number )obj;
        return Double.compare( doubleValue(), n.doubleValue() );
    }

    @Override
    public int hashCode()
    {
        if ( hash == null ) hash = ( int )( numerator + 31 * denominator );
        return hash;
    }
    
    public int signum()
    {
        return denominator == 0L ? ( int )numerator : Long.signum( numerator ) * Long.signum( denominator );
    }

    public static Fraction multiply( Fraction a, Fraction b ) throws ArithmeticException
    {
        if ( a == null || b == null ) return null;
        // 1. a = NaN -> NaN        0 x -> 0
        // 2. a = +Inf              1
        // 2.1. b = NaN -> NaN      1 0 -> 0
        // 2.2. b = +Inf -> +Inf    1 1 -> 1
        // 2.3. b = -Inf -> -Inf    1 - -> -
        // 2.4. b = +Fin -> +Inf    1 1 -> 1
        // 2.5. b = -Fin -> -Inf    1 - -> -
        // 2.6. b = 0 -> NaN        1 0 -> 0
        // 3. a = -Inf              -
        // 3.1. b = NaN -> NaN      - 0 -> 0
        // 3.2. b = +Inf -> -Inf    - 1 -> -
        // 3.3. b = -Inf -> +Inf    - - -> 1
        // 3.4. b = +Fin -> -Inf    - 1 -> -
        // 3.5. b = -Fin -> +Inf    - - -> 1
        // 3.6. b = 0 -> NaN        - 0 -> 0
        if ( a.denominator == 0L ) return valueOf( a.numerator * b.signum(), 0L, false );
        // 1. b = NaN -> NaN        0 x -> 0
        // 2. b = +Inf              1
        // 2.1. a = +Fin -> +Inf    1 1 -> 1
        // 2.2. a = -Fin -> -Inf    1 - -> -
        // 2.3. a = 0 -> NaN        1 0 -> 0
        // 3. b = -Inf              -
        // 3.1. a = +Fin -> -Inf    - 1 -> -
        // 3.2. a = -Fin -> +Inf    - - -> 1
        // 3.3. a = 0 -> NaN        - 0 -> 0
        if ( b.denominator == 0L ) return valueOf( b.numerator * a.signum(), 0L, false );
        if ( a.numerator == 0L || b.numerator == 0L ) return ZERO;
        long n1 = a.numerator;
        long d1 = b.denominator;
        long gcd = MathUtils.gcd( n1, d1 );
        n1 /= gcd;
        d1 /= gcd;
        long n2 = b.numerator;
        long d2 = a.denominator;
        gcd = MathUtils.gcd( n2, d2 );
        n2 /= gcd;
        d2 /= gcd;
        return valueOf( Math.multiplyExact( n1, n2 ), Math.multiplyExact( d1, d2 ), false );
    }

    public static Fraction divide( Fraction a, Fraction b ) throws ArithmeticException
    {
        return multiply( a, reciprocal( b ) );
    }

    public static Fraction subtract( Fraction a, Fraction b ) throws ArithmeticException
    {
        return add( a, negate( b ) );
    }

    public static Fraction add( Fraction a, Fraction b ) throws ArithmeticException
    {
        if ( a == null || b == null ) return null;
        // 1. a = NaN               0
        // 1.1. b = NaN -> NaN      0 0 -> 0
        // 1.2. b = +Inf -> NaN     0 1 -> 0
        // 1.3. b = -Inf -> NaN     0 - -> 0
        // 1.4. b = Fin -> NaN      0 s -> 0
        // 2. a = +Inf              1
        // 2.1. b = NaN -> NaN      1 0 -> 0
        // 2.2. b = +Inf -> +Inf    1 1 -> 1
        // 2.3. b = -Inf -> NaN     1 - -> 0
        // 2.4. b = Fin -> +Inf     1 s -> 1
        // 3. a = -Inf              -
        // 3.1. b = NaN -> NaN      - 0 -> 0
        // 3.2. b = +Inf -> NaN     - 1 -> 0
        // 3.3. b = -Inf -> -Inf    - - -> -
        // 3.4. b = Fin -> -Inf     - s -> -
        if ( a.denominator == 0L ) return b.denominator == 0L && b.numerator != a.numerator ? NaN : a;
        if ( b.denominator == 0L ) return b;
        if ( a.numerator == 0L ) return b;
        if ( b.numerator == 0L ) return a;
        long d1 = a.denominator;
        long d2 = b.denominator;
        long gcd = MathUtils.gcd( d1, d2 );
        d1 /= gcd;
        d2 /= gcd;
        try
        {
            if ( gcd == Long.MIN_VALUE ) return valueOf( Math.addExact( a.numerator, b.numerator ), Long.MIN_VALUE, true );
            return valueOf( Math.addExact( Math.multiplyExact( a.numerator, d2 ), Math.multiplyExact( b.numerator, d1 ) ), Math.multiplyExact( a.denominator, d2 ), true );
        }
        catch ( ArithmeticException e )
        {
            // Overflow.
            BigInteger n1 = BigInteger.valueOf( a.numerator );
            BigInteger n2 = BigInteger.valueOf( b.numerator );
            BigInteger b1 = BigInteger.valueOf( d1 );
            BigInteger b2 = BigInteger.valueOf( d2 );
            BigInteger g = BigInteger.valueOf( gcd );
            if ( b1.signum() < 0 )
            {
                b1 = b1.negate();
                n1 = n1.negate();
            }
            if ( b2.signum() < 0 )
            {
                b2 = b2.negate();
                n2 = n2.negate();
            }
            n1 = n1.multiply( b2 ).add( n2.multiply( b1 ) );
            b1 = b1.multiply( b2 ).multiply( g );
            g = n1.gcd( b1 );
            n1 = n1.divide( g );
            b1 = b1.divide( g );
            if ( n1.signum() == 0 ) return ZERO;
            if ( gcd == Long.MIN_VALUE )
            {
                b1 = b1.negate();
                n1 = n1.negate();
            }
            if ( n1.equals( BigUtils.BI_MAX_LONG_PLUS_ONE ) ) return valueOf( Long.MIN_VALUE, -b1.longValueExact(), false );
            if ( b1.equals( BigUtils.BI_MAX_LONG_PLUS_ONE ) ) return valueOf( -n1.longValueExact(), Long.MIN_VALUE, false );
            return valueOf( n1.longValueExact(), b1.longValueExact(), false );
        }
    }

    public static Fraction negate( Fraction a )
    {
        if ( a == null ) return null;
        if ( a.numerator == Long.MIN_VALUE ) return valueOf( a.numerator, -a.denominator, false );
        return valueOf( -a.numerator, a.denominator, false );
    }

    public static Fraction reciprocal( Fraction a )
    {
        if ( a == null ) return null;
        return valueOf( a.denominator, a.numerator, false );
    }

    private static String replace( String s, int start, int end, String t )
    {
        StringBuilder sb = new StringBuilder( s.substring( 0, start ) );
        sb.append( t );
        sb.append( s.substring( end + 1 ) );
        return sb.toString();
    }

    public static double calculate( String expression )
    {
        return eval( expression ).doubleValue();
    }

    public static Fraction eval( String expression )
    {
        if ( expression == null ) return null;
        expression = SPACE_PATTERN.matcher( expression ).replaceAll( "" );
        expression = expression.replace( ',', '.' );
        Matcher floatMatcher = FLOAT_NUMBER_PATTERN.matcher( expression );
        int start = 0;
        while ( floatMatcher.find( start ) )
        {
            start = floatMatcher.end();
            if ( floatMatcher.groupCount() >= 2 )
            {
                String integralString = floatMatcher.group( 1 );
                long integral = Long.parseLong( integralString );
                String fractionalString = floatMatcher.group( 2 );
                long fractional = Long.parseLong( fractionalString );
                int pow = fractionalString.length();
                long fraction = 1L;
                for ( int i = 0; i < pow; i++ ) fraction *= 10L;
                Fraction rd = valueOf( integral * fraction + fractional, fraction, true );
                String representation = rd.toString();
                StringBuilder sb = new StringBuilder( representation.length() + 2 );
                sb.append( '(' );
                sb.append( representation );
                sb.append( ')' );
                expression = floatMatcher.replaceFirst( sb.toString() );
            }
            floatMatcher.reset( expression );
        }
        int idx = expression.indexOf( '(' );
        while ( idx >= 0 )
        {
            int idx3 = expression.indexOf( ')', idx + 1 );
            if ( idx3 < 0 ) return null;
            int idx2 = expression.indexOf( '(', idx + 1 );
            if ( idx2 >= 0 && idx3 > idx2 )
            {
                idx = idx2;
                continue;
            }
            Fraction rd = evalNoBrackets( expression.substring( idx + 1, idx3 ) );
            boolean afterDivision = idx > 0 && expression.charAt( idx - 1 ) == '/';
            expression = replace( expression, idx, idx3, afterDivision ? rd.toStringAfterDivision() : rd.toString() );
            idx = expression.indexOf( '(' );
        }
        return evalNoBrackets( expression );
    }

    private static Fraction evalNoBrackets( String expression )
    {
        while ( expression.startsWith( "--" ) ) expression = expression.substring( 2 );
        expression = DOUBLE_MINUS_PATTERN.matcher( expression ).replaceAll( "+" );
        expression = PLUS_MINUS_PATTERN.matcher( expression ).replaceAll( "-" );
        int length = expression.length();
        StringBuilder sb = new StringBuilder();
        ArrayList< String > mds = new ArrayList<>();
        ArrayList< Character > ops = new ArrayList<>();
        boolean prevCharWasDigit = false;
        for ( int i = 0; i < length; i++ )
        {
            char c = expression.charAt( i );
            if ( c == '+' || ( prevCharWasDigit && c == '-' ) )
            {
                if ( sb.length() == 0 ) return null;
                mds.add( sb.toString() );
                sb = new StringBuilder();
                ops.add( c );
            }
            else
            {
                sb.append( c );
                prevCharWasDigit = Character.isDigit( c );
            }
        }
        if ( sb.length() == 0 ) return null;
        mds.add( sb.toString() );
        Fraction ret = evalMultiplyAndDivide( mds.get( 0 ) );
        for ( int i = 1; i < mds.size(); i++ )
        {
            Fraction rd = evalMultiplyAndDivide( mds.get( i ) );
            ret = ops.get( i - 1 ) == '-' ? Fraction.subtract( ret, rd ) : Fraction.add( ret, rd );
        }
        return ret;
    }

    private static Fraction evalMultiplyAndDivide( String expression )
    {
        int length = expression.length();
        int idx = expression.indexOf( '*' );
        int idx2 = expression.indexOf( '/' );
        idx = Math.min( idx < 0 ? length : idx, idx2 < 0 ? length : idx2 );
        String firstNumber = expression.substring( 0, idx );
        long number;
        try
        {
            number = Long.parseLong( firstNumber );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
        Fraction ret = valueOf( number, 1L, false );
        if ( idx == length ) return ret;
        StringBuilder sb = new StringBuilder();
        char op = expression.charAt( idx );
        for ( int i = idx + 1; i < length; i++ )
        {
            char c = expression.charAt( i );
            if ( c == '/' || c == '*' )
            {
                if ( sb.length() == 0 ) return null;
                try
                {
                    number = Long.parseLong( sb.toString() );
                }
                catch ( NumberFormatException e )
                {
                    return null;
                }
                sb = new StringBuilder();
                Fraction rd = valueOf( number, 1L, false );
                ret = op == '/' ? Fraction.divide( ret, rd ) : Fraction.multiply( ret, rd );
                op = c;
            }
            else sb.append( c );
        }
        if ( sb.length() == 0 ) return null;
        try
        {
            number = Long.parseLong( sb.toString() );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
        Fraction rd = valueOf( number, 1L, false );
        ret = op == '/' ? Fraction.divide( ret, rd ) : Fraction.multiply( ret, rd );
        return ret;
    }
}