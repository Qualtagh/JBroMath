package org.quinto.math;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.quinto.math.MathUtils.round;
import static org.quinto.math.MathUtils.toArabicNumeralsDouble;
import static org.quinto.math.MathUtils.toArabicNumeralsExcelInt;
import static org.quinto.math.MathUtils.toArabicNumeralsInt;
import static org.quinto.math.MathUtils.toBigDecimal;
import static org.quinto.math.MathUtils.toPlainString;
import static org.quinto.math.MathUtils.toRomanNumeralsExcelString;
import static org.quinto.math.MathUtils.toRomanNumeralsString;
import static org.quinto.math.MathUtils.toRoundedString;

public class MathUtilsConversionTest
{
    private static final double EXACT = -1.0;
    private static final Random RANDOM = new Random( System.nanoTime() );
    private static final int INTS[] = new int[]{ Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, 0, 1, 2, 3, 5, 8, 10, 20, 100, 300, 500, -1, -2, -3, -5, -8, -10, -20, -100, -300, -500 };
    
    public MathUtilsConversionTest()
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
    public void toPlainStringCasual()
    {
        LinkedHashMap< Double, String > mapping = new LinkedHashMap<>();
        mapping.put( Double.NaN, "NaN" );
        mapping.put( Double.POSITIVE_INFINITY, "Infinity" );
        mapping.put( 0.0, "0" );
        mapping.put( 0.000000000000000000000000000000001, "0.000000000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000000000001, "0.00000000000000000000000000000001" );
        mapping.put( 0.0000000000000000000000000000001, "0.0000000000000000000000000000001" );
        mapping.put( 0.000000000000000000000000000001, "0.000000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000000001, "0.00000000000000000000000000001" );
        mapping.put( 0.0000000000000000000000000001, "0.0000000000000000000000000001" );
        mapping.put( 0.000000000000000000000000001, "0.000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000001, "0.00000000000000000000000001" );
        mapping.put( 0.0000000000000000000000001, "0.0000000000000000000000001" );
        mapping.put( 0.000000000000000000000001, "0.000000000000000000000001" );
        mapping.put( 0.00000000000000000000001, "0.00000000000000000000001" );
        mapping.put( 0.0000000000000000000001, "0.0000000000000000000001" );
        mapping.put( 0.000000000000000000001, "0.000000000000000000001" );
        mapping.put( 0.00000000000000000001, "0.00000000000000000001" );
        mapping.put( 0.0000000000000000001, "0.0000000000000000001" );
        mapping.put( 0.000000000000000001, "0.000000000000000001" );
        mapping.put( 0.00000000000000001, "0.00000000000000001" );
        mapping.put( 0.0000000000000001, "0.0000000000000001" );
        mapping.put( 0.000000000000001, "0.000000000000001" );
        mapping.put( 0.00000000000001, "0.00000000000001" );
        mapping.put( 0.0000000000001, "0.0000000000001" );
        mapping.put( 0.000000000001, "0.000000000001" );
        mapping.put( 0.00000000001, "0.00000000001" );
        mapping.put( 0.0000000001, "0.0000000001" );
        mapping.put( 0.000000001, "0.000000001" );
        mapping.put( 0.00000001, "0.00000001" );
        mapping.put( 0.0000001, "0.0000001" );
        mapping.put( 0.000001, "0.000001" );
        mapping.put( 0.00001, "0.00001" );
        mapping.put( 0.0001, "0.0001" );
        mapping.put( 0.001, "0.001" );
        mapping.put( 0.01, "0.01" );
        mapping.put( 0.1, "0.1" );
        mapping.put( 1.0, "1" );
        mapping.put( 1.1, "1.1" );
        mapping.put( 1.000000001, "1.000000001" );
        mapping.put( 23.000000001, "23.000000001" );
        mapping.put( 1e10, "10000000000" );
        mapping.put( 230e10, "2300000000000" );
        mapping.put( 5e20, "500000000000000000000" );
        for ( Map.Entry< Double, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            double v = e.getKey();
            assertEquals( s, toPlainString( v ) );
            if ( !Double.isNaN( v ) ) assertEquals( "-" + s, toPlainString( -v ) );
            assertEquals( v, Double.parseDouble( s ), EXACT );
        }
    }

    @Test( timeout = 5000L )
    public void toPlainStringRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double v = RANDOM.nextDouble() * 1e20 - 1.5e15;
            String s = toPlainString( v );
            assertEquals( v, Double.parseDouble( s ), EXACT );
            assertFalse( s.toLowerCase().contains( "e" ) );
        }
    }
    
    @Test( timeout = 5000L )
    public void toRoundedStringNull()
    {
        for ( int n : INTS ) assertNull( toRoundedString( null, n ) );
    }

    @Test( timeout = 5000L )
    public void toRoundedStringCasual()
    {
        LinkedHashMap< Double, String > mapping = new LinkedHashMap<>();
        mapping.put( Double.NaN, "NaN" );
        mapping.put( Double.POSITIVE_INFINITY, "Infinity" );
        mapping.put( 0.0, "0" );
        mapping.put( 0.000000000000000000000000000000001, "0.000000000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000000000001, "0.00000000000000000000000000000001" );
        mapping.put( 0.0000000000000000000000000000001, "0.0000000000000000000000000000001" );
        mapping.put( 0.000000000000000000000000000001, "0.000000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000000001, "0.00000000000000000000000000001" );
        mapping.put( 0.0000000000000000000000000001, "0.0000000000000000000000000001" );
        mapping.put( 0.000000000000000000000000001, "0.000000000000000000000000001" );
        mapping.put( 0.00000000000000000000000001, "0.00000000000000000000000001" );
        mapping.put( 0.0000000000000000000000001, "0.0000000000000000000000001" );
        mapping.put( 0.000000000000000000000001, "0.000000000000000000000001" );
        mapping.put( 0.00000000000000000000001, "0.00000000000000000000001" );
        mapping.put( 0.0000000000000000000001, "0.0000000000000000000001" );
        mapping.put( 0.000000000000000000001, "0.000000000000000000001" );
        mapping.put( 0.00000000000000000001, "0.00000000000000000001" );
        mapping.put( 0.0000000000000000001, "0.0000000000000000001" );
        mapping.put( 0.000000000000000001, "0.000000000000000001" );
        mapping.put( 0.00000000000000001, "0.00000000000000001" );
        mapping.put( 0.0000000000000001, "0.0000000000000001" );
        mapping.put( 0.000000000000001, "0.000000000000001" );
        mapping.put( 0.00000000000001, "0.00000000000001" );
        mapping.put( 0.0000000000001, "0.0000000000001" );
        mapping.put( 0.000000000001, "0.000000000001" );
        mapping.put( 0.00000000001, "0.00000000001" );
        mapping.put( 0.0000000001, "0.0000000001" );
        mapping.put( 0.000000001, "0.000000001" );
        mapping.put( 0.00000001, "0.00000001" );
        mapping.put( 0.0000001, "0.0000001" );
        mapping.put( 0.000001, "0.000001" );
        mapping.put( 0.00001, "0.00001" );
        mapping.put( 0.0001, "0.0001" );
        mapping.put( 0.001, "0.001" );
        mapping.put( 0.01, "0.01" );
        mapping.put( 0.1, "0.1" );
        mapping.put( 1.0, "1" );
        mapping.put( 1.1, "1.1" );
        mapping.put( 1.000000001, "1.000000001" );
        mapping.put( 23.000000001, "23.000000001" );
        mapping.put( 1e10, "10000000000" );
        mapping.put( 230e10, "2300000000000" );
        mapping.put( 5e20, "500000000000000000000" );
        for ( Map.Entry< Double, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            double v = e.getKey();
            assertEquals( s, toRoundedString( v, 64 ) );
            if ( !Double.isNaN( v ) && v != 0.0 ) assertEquals( "-" + s, toRoundedString( -v, 64 ) );
            assertEquals( v, Double.parseDouble( s ), EXACT );
            assertEquals( toPlainString( v ), toRoundedString( v, 64 ) );
            float f = ( float )v;
            if ( Float.toString( f ).equals( Double.toString( v ) ) )
            {
                assertEquals( s, toRoundedString( f, 64 ) );
                assertEquals( f, Float.parseFloat( s ), EXACT );
            }
        }
    }

    @Test( timeout = 5000L )
    public void toRoundedStringPrecision()
    {
        double d = 12345.6789;
        LinkedHashMap< Integer, String > mapping = new LinkedHashMap<>();
        mapping.put( Integer.MIN_VALUE, "0" );
        mapping.put( -100000, "0" );
        mapping.put( -100, "0" );
        mapping.put( -5, "0" );
        mapping.put( -4, "10000" );
        mapping.put( -3, "12000" );
        mapping.put( -2, "12300" );
        mapping.put( -1, "12350" );
        mapping.put( 0, "12346" );
        mapping.put( 1, "12345.7" );
        mapping.put( 2, "12345.68" );
        mapping.put( 3, "12345.679" );
        mapping.put( 4, "12345.6789" );
        mapping.put( 5, "12345.6789" );
        mapping.put( 100, "12345.6789" );
        mapping.put( 100000, "12345.6789" );
        mapping.put( Integer.MAX_VALUE, "12345.6789" );
        for ( Map.Entry< Integer, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            int p = e.getKey();
            assertEquals( s, toRoundedString( d, p ) );
            if ( !s.equals( "0" ) ) assertEquals( "-" + s, toRoundedString( -d, p ) );
            assertEquals( s, toPlainString( round( d, p ) ) );
        }
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalDoubleNaN()
    {
        toBigDecimal( Double.NaN );
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalDoublePositiveInfinity()
    {
        toBigDecimal( Double.POSITIVE_INFINITY );
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalDoubleNegativeInfinity()
    {
        toBigDecimal( Double.NEGATIVE_INFINITY );
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalFloatNaN()
    {
        toBigDecimal( Float.NaN );
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalFloatPositiveInfinity()
    {
        toBigDecimal( Float.POSITIVE_INFINITY );
    }
    
    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void toBigDecimalFloatNegativeInfinity()
    {
        toBigDecimal( Float.NEGATIVE_INFINITY );
    }
    
    @Test( timeout = 5000L )
    public void toBigDecimalNull()
    {
        assertEquals( null, toBigDecimal( null ) );
    }

    @Test( timeout = 5000L )
    public void toBigDecimalCasual()
    {
        LinkedHashMap< Number, BigDecimal > mapping = new LinkedHashMap<>();
        mapping.put( 0, BigDecimal.ZERO );
        mapping.put( 1, BigDecimal.ONE );
        mapping.put( 10, BigDecimal.TEN );
        mapping.put( 0.5, BigUtils.BD_HALF );
        mapping.put( 1E22, BigDecimal.valueOf( 1E22 ) );
        mapping.put( 1E22F, new BigDecimal( "1E22" ) );
        mapping.put( 1E23F, new BigDecimal( "1E23" ) );
        for ( Map.Entry< Number, BigDecimal > e : mapping.entrySet() )
        {
            BigDecimal bd = e.getValue();
            Number n = e.getKey();
            assertTrue( bd.compareTo( toBigDecimal( n ) ) == 0 );
            if ( n instanceof Integer )
            {
                assertTrue( bd.negate().compareTo( toBigDecimal( -n.intValue() ) ) == 0 );
                assertTrue( bd.compareTo( toBigDecimal( BigInteger.valueOf( n.intValue() ) ) ) == 0 );
                assertTrue( bd.negate().compareTo( toBigDecimal( BigInteger.valueOf( n.intValue() ).negate() ) ) == 0 );
                assertEquals( n.intValue(), toBigDecimal( n ).intValue() );
            }
            assertTrue( bd.compareTo( toBigDecimal( n.floatValue() ) ) == 0 );
            assertTrue( bd.negate().compareTo( toBigDecimal( -n.floatValue() ) ) == 0 );
            if ( !( n instanceof Float ) )
            {
                assertTrue( bd.compareTo( toBigDecimal( n.doubleValue() ) ) == 0 );
                assertTrue( bd.negate().compareTo( toBigDecimal( -n.doubleValue() ) ) == 0 );
                assertEquals( n.doubleValue(), toBigDecimal( n ).doubleValue(), EXACT );
            }
            assertEquals( n.doubleValue(), toBigDecimal( n.doubleValue() ).doubleValue(), EXACT );
        }
    }

    @Test( timeout = 5000L )
    public void toBigDecimalRandom()
    {
        for ( int i = 0; i < 50; i++ )
        {
            double v = RANDOM.nextDouble() * 1e20 - 1.5e15;
            assertEquals( v, toBigDecimal( v ).doubleValue(), EXACT );
        }
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanClassicNegative()
    {
        toRomanNumeralsString( -1, false );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanClassicBig()
    {
        toRomanNumeralsString( 4000, false );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanShortestNegative()
    {
        toRomanNumeralsString( -1, true );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanShortestBig()
    {
        toRomanNumeralsString( 4000, true );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanExcelNegative()
    {
        toRomanNumeralsExcelString( -1, 0 );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanExcelBig()
    {
        toRomanNumeralsExcelString( 4000, 0 );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanDoubleNegative()
    {
        toRomanNumeralsString( -1e6, false );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanDoubleBig()
    {
        toRomanNumeralsString( 1e6, false );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanDoubleShortestNegative()
    {
        toRomanNumeralsString( -1e6, true );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanDoubleShortestBig()
    {
        toRomanNumeralsString( 1e6, true );
    }

    @Test( timeout = 5000L )
    public void decimalToRomanCasual()
    {
        LinkedHashMap< Integer, String > mapping = new LinkedHashMap<>();
        mapping.put( 0, "N" );
        mapping.put( 1, "I" );
        mapping.put( 2, "II" );
        mapping.put( 3, "III" );
        mapping.put( 4, "IV" );
        mapping.put( 5, "V" );
        mapping.put( 6, "VI" );
        mapping.put( 7, "VII" );
        mapping.put( 8, "VIII" );
        mapping.put( 9, "IX" );
        mapping.put( 10, "X" );
        mapping.put( 11, "XI" );
        mapping.put( 12, "XII" );
        mapping.put( 20, "XX" );
        mapping.put( 30, "XXX" );
        mapping.put( 40, "XL" );
        mapping.put( 41, "XLI" );
        mapping.put( 50, "L" );
        mapping.put( 60, "LX" );
        mapping.put( 70, "LXX" );
        mapping.put( 80, "LXXX" );
        mapping.put( 88, "LXXXVIII" );
        mapping.put( 90, "XC" );
        mapping.put( 100, "C" );
        mapping.put( 200, "CC" );
        mapping.put( 300, "CCC" );
        mapping.put( 400, "CD" );
        mapping.put( 500, "D" );
        mapping.put( 600, "DC" );
        mapping.put( 700, "DCC" );
        mapping.put( 800, "DCCC" );
        mapping.put( 888, "DCCCLXXXVIII" );
        mapping.put( 900, "CM" );
        mapping.put( 1000, "M" );
        mapping.put( 1984, "MCMLXXXIV" );
        mapping.put( 2000, "MM" );
        mapping.put( 3000, "MMM" );
        mapping.put( 3888, "MMMDCCCLXXXVIII" );
        mapping.put( 3999, "MMMCMXCIX" );
        for ( Map.Entry< Integer, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            int n = e.getKey();
            assertEquals( s, toRomanNumeralsString( n, false ) );
            assertEquals( s, toRomanNumeralsString( ( double )n, false ) );
            if ( n > 0 )
            {
                assertEquals( s, toRomanNumeralsExcelString( n, 0 ) );
                assertEquals( n, toArabicNumeralsExcelInt( s ) );
            }
            else
            {
                for ( int mode = 0; mode < 5; mode++ ) assertEquals( "", toRomanNumeralsExcelString( n, mode ) );
                assertEquals( n, toArabicNumeralsExcelInt( "" ) );
            }
            assertEquals( n, toArabicNumeralsInt( s ) );
            assertEquals( n, ( int )toArabicNumeralsDouble( s ) );
        }
    }

    @Test( timeout = 5000L )
    public void decimalToRomanShortest()
    {
        LinkedHashMap< Integer, String > mapping = new LinkedHashMap<>();
        mapping.put( 0, "N" );
        mapping.put( 1, "I" );
        mapping.put( 2, "II" );
        mapping.put( 3, "III" );
        mapping.put( 4, "IV" );
        mapping.put( 5, "V" );
        mapping.put( 6, "VI" );
        mapping.put( 7, "VII" );
        mapping.put( 8, "IIX" );
        mapping.put( 9, "IX" );
        mapping.put( 10, "X" );
        mapping.put( 11, "XI" );
        mapping.put( 12, "XII" );
        mapping.put( 20, "XX" );
        mapping.put( 30, "XXX" );
        mapping.put( 40, "XL" );
        mapping.put( 41, "XLI" );
        mapping.put( 50, "L" );
        mapping.put( 60, "LX" );
        mapping.put( 70, "LXX" );
        mapping.put( 80, "XXC" );
        mapping.put( 88, "IIXC" );
        mapping.put( 90, "XC" );
        mapping.put( 100, "C" );
        mapping.put( 200, "CC" );
        mapping.put( 300, "CCC" );
        mapping.put( 400, "CD" );
        mapping.put( 500, "D" );
        mapping.put( 600, "DC" );
        mapping.put( 700, "DCC" );
        mapping.put( 800, "CCM" );
        mapping.put( 888, "IIXCM" );
        mapping.put( 900, "CM" );
        mapping.put( 1000, "M" );
        mapping.put( 1984, "MIVXM" );
        mapping.put( 2000, "MM" );
        mapping.put( 3000, "MMM" );
        mapping.put( 3888, "MMMIIXCM" );
        mapping.put( 3999, "MMMIM" );
        for ( Map.Entry< Integer, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            int n = e.getKey();
            assertEquals( s, toRomanNumeralsString( n, true ) );
            assertEquals( s, toRomanNumeralsString( ( double )n, true ) );
            assertEquals( n, toArabicNumeralsExcelInt( "N".equals( s ) ? "" : s ) );
            assertEquals( n, ( int )toArabicNumeralsDouble( s ) );
        }
    }

    @Test( timeout = 5000L )
    public void decimalToRomanDoubleCasual()
    {
        LinkedHashMap< Double, String > mapping = new LinkedHashMap<>();
        mapping.put( 0.0, "N" );
        mapping.put( 1.0, "I" );
        mapping.put( 2.0, "II" );
        mapping.put( 3.0, "III" );
        mapping.put( 4.0, "IV" );
        mapping.put( 5.0, "V" );
        mapping.put( 6.0, "VI" );
        mapping.put( -7.0, "-VII" );
        mapping.put( -7.5, "-VIIS" );
        mapping.put( 7.5, "VIIS" );
        mapping.put( 5005.5, "MMMMMVS" );
        mapping.put( 998.5, "CMXCVIIIS" );
        for ( Map.Entry< Double, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            double n = e.getKey();
            assertEquals( s, toRomanNumeralsString( n, false ) );
            assertEquals( n, toArabicNumeralsDouble( s ), EXACT );
        }
    }

    @Test( timeout = 5000L )
    public void decimalToRomanDoubleShortest()
    {
        LinkedHashMap< Double, String > mapping = new LinkedHashMap<>();
        mapping.put( 0.0, "N" );
        mapping.put( 1.0, "I" );
        mapping.put( 2.0, "II" );
        mapping.put( 3.0, "III" );
        mapping.put( 4.0, "IV" );
        mapping.put( 5.0, "V" );
        mapping.put( 6.0, "VI" );
        mapping.put( -7.0, "-VII" );
        mapping.put( -7.5, "-VIIS" );
        mapping.put( 7.5, "VIIS" );
        mapping.put( 5005.5, "MMMMMVS" );
        mapping.put( 998.5, "SIM" );
        for ( Map.Entry< Double, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            double n = e.getKey();
            assertEquals( s, toRomanNumeralsString( n, true ) );
            assertEquals( n, toArabicNumeralsDouble( s ), EXACT );
        }
    }

    @Test( timeout = 15000L )
    public void decimalToRomanDouble()
    {
        for ( double i = -1e5; i < 1e5; i += 0.5 )
        {
            assertEquals( i, toArabicNumeralsDouble( toRomanNumeralsString( i, false ) ), EXACT );
            assertEquals( i, toArabicNumeralsDouble( toRomanNumeralsString( i, true ) ), EXACT );
        }
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalNull()
    {
        toArabicNumeralsInt( null );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalEmpty()
    {
        toArabicNumeralsInt( "" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalBlank()
    {
        toArabicNumeralsInt( " " );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalWrong()
    {
        toArabicNumeralsInt( "IIII" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalMinus()
    {
        toArabicNumeralsInt( "-D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalMinusInside()
    {
        toArabicNumeralsInt( "D-D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalSpaceInside()
    {
        toArabicNumeralsInt( "D D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalHalf()
    {
        toArabicNumeralsInt( "S" );
    }

    @Test( timeout = 5000L )
    public void romanToDecimalCasual()
    {
        for ( int i = 0; i < 4000; i++ )
        {
            if ( i > 0 )
            {
                assertEquals( i, toArabicNumeralsExcelInt( toRomanNumeralsString( i, false ) ) );
                assertEquals( i, toArabicNumeralsExcelInt( toRomanNumeralsString( i, true ) ) );
            }
            else assertEquals( i, toArabicNumeralsExcelInt( "" ) );
            for ( int mode = 0; mode < 5; mode++ ) assertEquals( i, toArabicNumeralsExcelInt( toRomanNumeralsExcelString( i, mode ) ) );
            assertEquals( i, toArabicNumeralsInt( toRomanNumeralsString( i, false ) ) );
            assertEquals( i, toArabicNumeralsExcelInt( i == 0 ? "" : toRomanNumeralsString( i, true ) ) );
            assertEquals( i, ( int )toArabicNumeralsDouble( toRomanNumeralsString( i, false ) ) );
            assertEquals( i, ( int )toArabicNumeralsDouble( toRomanNumeralsString( i, true ) ) );
            double d = i;
            assertEquals( d, toArabicNumeralsDouble( toRomanNumeralsString( d, false ) ), EXACT );
            assertEquals( d, toArabicNumeralsDouble( toRomanNumeralsString( d, true ) ), EXACT );
        }
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalRawSpaceInside()
    {
        toArabicNumeralsExcelInt( "D D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalRawMinusInside()
    {
        toArabicNumeralsExcelInt( "D-D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalRawZero()
    {
        toArabicNumeralsExcelInt( "N" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalRawHalf()
    {
        toArabicNumeralsExcelInt( "S" );
    }

    @Test( timeout = 5000L )
    public void romanToDecimalRawCasual()
    {
        LinkedHashMap< Integer, String > mapping = new LinkedHashMap<>();
        mapping.put( 0, "" );
        mapping.put( 0, " " );
        mapping.put( 4, "IIII" );
        mapping.put( 4, "  IIII " );
        mapping.put( 1490, "MXD" );
        mapping.put( 5000, "MMMMM" );
        mapping.put( 1006, "MVI" );
        mapping.put( 1004, "MIV" );
        mapping.put( 957, "LMVII" );
        mapping.put( 890, "XCM" );
        mapping.put( 8, "IIX" );
        mapping.put( 18, "IIXX" );
        mapping.put( 18, "XIIX" );
        mapping.put( 18, "XVIII" );
        mapping.put( 80, "XXC" );
        mapping.put( -500, "DDDM" );
        mapping.put( 0, "DDM" );
        mapping.put( -500, "-D" );
        mapping.put( 3, "IVIX" );
        mapping.put( 7, "IVIXIV" );
        mapping.put( 12, "IVIXIX" );
        mapping.put( 982, "IVIXIM" );
        mapping.put( 1981, "IVIXIMIM" );
        for ( Map.Entry< Integer, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            int n = e.getKey();
            assertEquals( n, toArabicNumeralsExcelInt( s ) );
            assertEquals( n, ( int )toArabicNumeralsDouble( s ) );
        }
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalDoubleSpaceInside()
    {
        toArabicNumeralsDouble( "D D" );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void romanToDecimalDoubleMinusInside()
    {
        toArabicNumeralsDouble( "D-D" );
    }

    @Test( timeout = 5000L )
    public void romanToDecimalDoubleCasual()
    {
        LinkedHashMap< Double, String > mapping = new LinkedHashMap<>();
        mapping.put( 0.0, "" );
        mapping.put( 0.0, " " );
        mapping.put( 0.0, "NNN" );
        mapping.put( 4.0, "IIII" );
        mapping.put( 4.0, "  IIII " );
        mapping.put( 1490.0, "MXD" );
        mapping.put( 5000.0, "MMMMM" );
        mapping.put( 1006.0, "MNVI" );
        mapping.put( 1004.0, "MNIV" );
        mapping.put( 957.0, "LMVII" );
        mapping.put( 890.0, "XCM" );
        mapping.put( 8.0, "IIX" );
        mapping.put( 18.0, "IIXX" );
        mapping.put( 18.0, "XIIX" );
        mapping.put( 18.0, "XVIII" );
        mapping.put( 80.0, "XXC" );
        mapping.put( -500.0, "DDDM" );
        mapping.put( 0.0, "DDM" );
        mapping.put( -500.0, "-D" );
        mapping.put( 3.0, "IVIX" );
        mapping.put( 7.0, "IVIXIV" );
        mapping.put( 12.0, "IVIXIX" );
        mapping.put( 982.0, "IVIXIM" );
        mapping.put( 1981.0, "IVIXIMIM" );
        mapping.put( 999.5, "SM" );
        mapping.put( 1.5, "SSS" );
        mapping.put( 1.5, "IS" );
        for ( Map.Entry< Double, String > e : mapping.entrySet() )
        {
            String s = e.getValue();
            double n = e.getKey();
            assertEquals( n, toArabicNumeralsDouble( s ), EXACT );
        }
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanExcelModeNegative()
    {
        toRomanNumeralsExcelString( -1, 0 );
    }

    @Test( expected = NumberFormatException.class, timeout = 5000L )
    public void decimalToRomanExcelModeBig()
    {
        toRomanNumeralsExcelString( 4000, 0 );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void decimalToRomanExcelModeLow()
    {
        toRomanNumeralsExcelString( 1, -1 );
    }

    @Test( expected = IllegalArgumentException.class, timeout = 5000L )
    public void decimalToRomanExcelModeHigh()
    {
        toRomanNumeralsExcelString( 1, 5 );
    }

    @Test( timeout = 5000L )
    public void decimalToRomanExcelMode() throws IOException
    {
        // This file contains output of Microsoft Excel's ROMAN( number, mode ) function.
        File file = TestUtils.getResourceFile( "roman.csv" );
        try ( FileReader fr = new FileReader( file );
              BufferedReader br = new BufferedReader( fr ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                String values[] = line.split( ";" );
                int n = Integer.parseInt( values[ 0 ] );
                for ( int mode = 0; mode < 5; mode++ ) assertEquals( values.length > mode + 1 ? values[ mode + 1 ] : "", toRomanNumeralsExcelString( n, mode ) );
            }
        }
    }
    
    /**
     * Exponential-time brute-force algorithm of finding the shortest Roman numerals string representing a value of n.
     * @param n a number to be converted
     * @return shortest Roman numerals string representing a value of n
     */
    public static String toRomanNumeralsShortestString( double n )
    {
        String s = toRomanNumeralsString( n, true );
        int maxlen = s.length();
        long to = 1L << ( maxlen << 2 );
        for ( long c = 0L; c < to; c++ )
        {
            String can = BigInteger.valueOf( c ).toString( 9 );
            if ( can.length() == maxlen ) break;
            if ( can.indexOf( '0' ) >= 0 ) continue;
            can = can.replace( '0', 'N' ).replace( '1', 'S' ).replace( '2', 'I' ).replace( '3', 'V' ).replace( '4', 'X' ).replace( '5', 'L' ).replace( '6', 'C' ).replace( '7', 'D' ).replace( '8', 'M' );
            double m = toArabicNumeralsDouble( can );
            if ( m == n ) return can;
        }
        return s;
    }

    @Test( timeout = 60000L )
    public void decimalToRomanShortestDoubleRandom()
    {
        for ( int i = 0; i < 20; i++ )
        {
            double d = RANDOM.nextInt( 2000 ) * 0.5;
            String shortest = toRomanNumeralsShortestString( d );
            String candidate = toRomanNumeralsString( d, true );
            assertEquals( shortest.length(), candidate.length() );
            assertEquals( d, toArabicNumeralsDouble( shortest ), EXACT );
            assertEquals( d, toArabicNumeralsDouble( candidate ), EXACT );
        }
    }
    
    /**
     * Exponential-time brute-force algorithm of finding the shortest Roman numerals string representing a value of n.
     * @param n a number to be converted
     * @return shortest Roman numerals string representing a value of n
     */
    public static String toRomanNumeralsShortestString( int n )
    {
        String s = toRomanNumeralsString( n, true );
        int maxlen = s.length();
        long to = 1L << ( maxlen << 2 );
        for ( long c = 0L; c < to; c++ )
        {
            String can = Long.toOctalString( c );
            if ( can.length() == maxlen ) break;
            if ( can.indexOf( '0' ) >= 0 ) continue;
            can = can.replace( '0', 'N' ).replace( '1', 'I' ).replace( '2', 'V' ).replace( '3', 'X' ).replace( '4', 'L' ).replace( '5', 'C' ).replace( '6', 'D' ).replace( '7', 'M' );
            int m = toArabicNumeralsExcelInt( can );
            if ( m == n ) return can;
        }
        return s;
    }

    @Test( timeout = 60000L )
    public void decimalToRomanShortestIntRandom()
    {
        for ( int i = 0; i < 100; i++ )
        {
            int d = RANDOM.nextInt( 1999 ) + 1;
            String shortest = toRomanNumeralsShortestString( d );
            String candidate = toRomanNumeralsString( d, true );
            assertEquals( shortest.length(), candidate.length() );
            assertEquals( d, toArabicNumeralsExcelInt( shortest ) );
            assertEquals( d, toArabicNumeralsExcelInt( candidate ) );
        }
    }

    @Test( timeout = 5000L )
    public void decimalToRomanShortestInt() throws IOException
    {
        // This file was generated by function toRomanNumeralsShortestString (brute-force).
        File file = TestUtils.getResourceFile( "shortestInt.csv" );
        try ( FileReader fr = new FileReader( file );
              BufferedReader br = new BufferedReader( fr ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                String values[] = line.split( ";" );
                int n = Integer.parseInt( values[ 0 ] );
                assertEquals( values[ 1 ].length(), toRomanNumeralsString( n, true ).length() );
            }
        }
    }

    @Test( timeout = 5000L )
    public void decimalToRomanShortestDouble() throws IOException
    {
        // This file was generated by function toRomanNumeralsShortestString (brute-force).
        File file = TestUtils.getResourceFile( "shortestDouble.csv" );
        try ( FileReader fr = new FileReader( file );
              BufferedReader br = new BufferedReader( fr ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                String values[] = line.split( ";" );
                double n = Double.parseDouble( values[ 0 ] );
                assertEquals( values[ 1 ].length(), toRomanNumeralsString( n, true ).length() );
            }
        }
    }
}