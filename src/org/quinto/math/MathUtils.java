package org.quinto.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Math functions with primitive-type arguments (byte, short, int, long, float, double).
 */
public class MathUtils
{
    /**
     * Array of Roman numeric system terminals.
     * @see #toRomanNumeralsExcelString
     */
    private static final String ROMAN_NUMBERS[][] = { // Classic form of Roman numerals (Microsoft Excel's ROMAN( text, form ) default mode 0)
                                                      { "M",                         "CM", "D",                         "CD", "C",             "XC", "L",             "XL", "X", "IX", "V", "IV", "I", "S" },
                                                      // Microsoft Excel's "More concise" mode 1
                                                      { "M",                   "LM", "CM", "D",                   "LD", "CD", "C",       "VC", "XC", "L",       "VL", "XL", "X", "IX", "V", "IV", "I", "S" },
                                                      // Microsoft Excel's "More concise" mode 2
                                                      { "M",             "XM", "LM", "CM", "D",             "XD", "LD", "CD", "C", "IC", "VC", "XC", "L", "IL", "VL", "XL", "X", "IX", "V", "IV", "I", "S" },
                                                      // Microsoft Excel's "More concise" mode 3
                                                      { "M",       "VM", "XM", "LM", "CM", "D",       "VD", "XD", "LD", "CD", "C", "IC", "VC", "XC", "L", "IL", "VL", "XL", "X", "IX", "V", "IV", "I", "S" },
                                                      // Microsoft Excel's "Minimal" mode 4
                                                      { "M", "IM", "VM", "XM", "LM", "CM", "D", "ID", "VD", "XD", "LD", "CD", "C", "IC", "VC", "XC", "L", "IL", "VL", "XL", "X", "IX", "V", "IV", "I", "S" },
                                                      // Absolutely minimal mode.
                                                      // Each numeral in this array is a subtractive form such that there exists no shorter
                                                      // (or equal-length non-subtractive) form with the same value.
                                                      // Example: IVM = M - V - I = 1000 - 5 - 1 = 994 cannot be represented in 2 charater Roman numeral.
                                                      // And its only representations in 3 characters are IVM and VIM which are both subtractive forms (they are both equivalent).
                                                      // Another example: VXXLM = M - L - X - X - V = 1000 - 50 - 10 - 10 - 5 = 925.
                                                      // It cannot be represented in 4 characters. But there exists non-subtractive form CMXXV,
                                                      // so we don't need VXXLM as an element of this array - it's redundant.
                                                      { "M",     "SM",     "IM",     "SIM",     "IIM",     "SIIM",     "VM",     "SVM",     "IVM",     "SIVM",     "IIVM",
                                                        "XM",    "SXM",    "IXM",    "SIXM",    "IIXM",    "SIIXM",    "VXM",    "SVXM",    "IVXM",    "SIVXM",    "IIVXM",
                                                        "XXM",   "SXXM",   "IXXM",   "SIXXM",   "IIXXM",   "SIIXXM",   "VXXM",   "SVXXM",   "IVXXM",   "SIVXXM",   "IIVXXM",
                                                        "LM",    "SLM",    "ILM",    "SILM",    "IILM",    "SIILM",    "VLM",    "SVLM",    "IVLM",    "SIVLM",    "IIVLM",
                                                        "XLM",   "SXLM",   "IXLM",   "SIXLM",   "IIXLM",   "SIIXLM",   "VXLM",   "SVXLM",   "IVXLM",   "SIVXLM",   "IIVXLM",
                                                        "XXLM",  "SXXLM",  "IXXLM",  "SIXXLM",  "IIXXLM",  "SIIXXLM",

                                                        "CM",    "SCM",    "ICM",    "SICM",    "IICM",    "SIICM",    "VCM",    "SVCM",    "IVCM",    "SIVCM",    "IIVCM",
                                                        "XCM",   "SXCM",   "IXCM",   "SIXCM",   "IIXCM",   "SIIXCM",   "VXCM",   "SVXCM",   "IVXCM",   "SIVXCM",   "IIVXCM",
                                                        "XXCM",  "SXXCM",  "IXXCM",  "SIXXCM",  "IIXXCM",  "SIIXXCM",  "VXXCM",  "SVXXCM",  "IVXXCM",  "SIVXXCM",  "IIVXXCM",
                                                        "LCM",   "SLCM",   "ILCM",   "SILCM",   "IILCM",   "SIILCM",   "VLCM",   "SVLCM",   "IVLCM",   "SIVLCM",   "IIVLCM",
                                                        "XLCM",  "SXLCM",  "IXLCM",  "SIXLCM",  "IIXLCM",  "SIIXLCM",  "VXLCM",  "SVXLCM",  "IVXLCM",  "SIVXLCM",  "IIVXLCM",
                                                        "XXLCM", "SXXLCM", "IXXLCM", "SIXXLCM", "IIXXLCM", "SIIXXLCM",

                                                        "CCM",   "SCCM",   "ICCM",   "SICCM",   "IICCM",   "SIICCM",   "VCCM",   "SVCCM",   "IVCCM",   "SIVCCM",   "IIVCCM",
                                                        "XCCM",  "SXCCM",  "IXCCM",  "SIXCCM",  "IIXCCM",  "SIIXCCM",  "VXCCM",  "SVXCCM",  "IVXCCM",  "SIVXCCM",  "IIVXCCM",
                                                        "XXCCM", "SXXCCM", "IXXCCM", "SIXXCCM", "IIXXCCM", "SIIXXCCM", "VXXCCM", "SVXXCCM", "IVXXCCM", "SIVXXCCM", "IIVXXCCM",

                                                        "D",     "SD",     "ID",     "SID",     "IID",     "SIID",     "VD",     "SVD",     "IVD",     "SIVD",     "IIVD",
                                                        "XD",    "SXD",    "IXD",    "SIXD",    "IIXD",    "SIIXD",    "VXD",    "SVXD",    "IVXD",    "SIVXD",    "IIVXD",
                                                        "XXD",   "SXXD",   "IXXD",   "SIXXD",   "IIXXD",   "SIIXXD",   "VXXD",   "SVXXD",   "IVXXD",   "SIVXXD",   "IIVXXD",
                                                        "LD",    "SLD",    "ILD",    "SILD",    "IILD",    "SIILD",    "VLD",    "SVLD",    "IVLD",    "SIVLD",    "IIVLD",
                                                        "XLD",   "SXLD",   "IXLD",   "SIXLD",   "IIXLD",   "SIIXLD",   "VXLD",   "SVXLD",   "IVXLD",   "SIVXLD",   "IIVXLD",
                                                        "XXLD",  "SXXLD",  "IXXLD",  "SIXXLD",  "IIXXLD",  "SIIXXLD",

                                                        "CD",    "SCD",    "ICD",    "SICD",    "IICD",    "SIICD",    "VCD",    "SVCD",    "IVCD",    "SIVCD",    "IIVCD",
                                                        "XCD",   "SXCD",   "IXCD",   "SIXCD",   "IIXCD",   "SIIXCD",   "VXCD",   "SVXCD",   "IVXCD",   "SIVXCD",   "IIVXCD",
                                                        "XXCD",  "SXXCD",  "IXXCD",  "SIXXCD",  "IIXXCD",  "SIIXXCD",  "VXXCD",  "SVXXCD",  "IVXXCD",  "SIVXXCD",  "IIVXXCD",
                                                        "LCD",   "SLCD",   "ILCD",   "SILCD",   "IILCD",   "SIILCD",   "VLCD",   "SVLCD",   "IVLCD",   "SIVLCD",   "IIVLCD",
                                                        "XLCD",  "SXLCD",  "IXLCD",  "SIXLCD",  "IIXLCD",  "SIIXLCD",  "VXLCD",  "SVXLCD",  "IVXLCD",  "SIVXLCD",  "IIVXLCD",
                                                        "XXLCD", "SXXLCD", "IXXLCD", "SIXXLCD", "IIXXLCD", "SIIXXLCD",

                                                        "C",     "SC",     "IC",     "SIC",     "IIC",     "SIIC",     "VC",     "SVC",     "IVC",     "SIVC",     "IIVC",
                                                        "XC",    "SXC",    "IXC",    "SIXC",    "IIXC",    "SIIXC",    "VXC",    "SVXC",    "IVXC",    "SIVXC",    "IIVXC",
                                                        "XXC",   "SXXC",   "IXXC",   "SIXXC",   "IIXXC",   "SIIXXC",

                                                        "L",     "SL",     "IL",     "SIL",     "IIL",     "SIIL",     "VL",     "SVL",     "IVL",     "SIVL",     "IIVL",
                                                        "XL",    "SXL",    "IXL",    "SIXL",    "IIXL",    "SIIXL",    "VXL",    "SVXL",    "IVXL",    "SIVXL",    "IIVXL",

                                                        "X",     "SX",     "IX",     "SIX",     "IIX",

                                                        "V",     "SV",     "IV",     "SIV",

                                                        "I",

                                                        "S" } };
    
    /**
     * Decimal values of Roman numerals from {@link #ROMAN_NUMBERS}.<br>
     * Values are doubled to allow S (semis - half) representation as int.
     */
    private static final int DECIMAL_NUMBERS[][];
    
    /**
     * Regular expressions pattern for proper (classic) Roman numerals.
     * @see #toArabicNumeralsInt
     */
    private static final Pattern ROMAN_NUMBER = Pattern.compile( "^M{0,3}(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$", Pattern.CASE_INSENSITIVE );
    
    /**
     * Array of largest roots in int range.<br>
     * If r = MAX_ROOTS_INT[ i ] then r<sup>i</sup> &le; {@link java.lang.Integer#MAX_VALUE} and ( r + 1 )<sup>i</sup> &gt; {@link java.lang.Integer#MAX_VALUE}.
     * @see #iroot(int, int)
     */
    private static final int MAX_ROOTS_INT[] = { 2147483647, 2147483647, 46340, 1290, 215, 73, 35, 21, 14, 10, 8, 7, 5, 5, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1 };
    
    /**
     * Array of largest roots in long range.<br>
     * If r = MAX_ROOTS_LONG[ i ] then r<sup>i</sup> &le; {@link java.lang.Long#MAX_VALUE} and ( r + 1 )<sup>i</sup> &gt; {@link java.lang.Long#MAX_VALUE}.
     * @see #iroot(long, int)
     */
    private static final long MAX_ROOTS_LONG[] = { 9223372036854775807L, 9223372036854775807L, 3037000499L, 2097151L, 55108L, 6208L, 1448L, 511L, 234L, 127L, 78L, 52L, 38L, 28L, 22L, 18L, 15L, 13L, 11L, 9L, 8L, 7L, 7L, 6L, 6L, 5L, 5L, 5L, 4L, 4L, 4L, 4L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 1L };
    
    /**
     * List of prime numbers less than 32. Used in {@link #getBaseOfPerfectPower(long)}.
     */
    private static final int SMALL_PRIMES[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
    
    /**
     * Packed array of allowed perfect square residues mod 255.<br>
     * Based on this discussion:<br>
     * http://stackoverflow.com/questions/295579/fastest-way-to-determine-if-an-integers-square-root-is-an-integer
     */
    private static final int CAN_255_RESIDUE_BE_PERFECT_SQUARE[] = { -935226302, 671109384, -1509405694, 136384704, 25215264, 38928420, 403439640, 1142981121, -1870452604, 1342218769, 1276155908, 272769408, 50430528, 77856840, 806879280, -2009005053 };

    /**
     * Array of mods to check when searching for base of perfect power.<br>
     * Let m be PERFECT_POWER_MODS[ power ], then m is the best mod for chosen power
     * such that 2 &le; m &le; 64. "Best" means that the quantity of distinct
     * n<sup>power</sup> (mod m) is minimal (n is integer, 0 &le; n &le; 63).
     * So these mods filter out as much numbers as possible when checking if the number
     * is a perfect square (assuming mod lies in range [ 2, 64 ], otherwise residues
     * cannot be packed in one long int constant).
     */
    private static final int PERFECT_POWER_MODS[] = { 64, 64, 48, 63, 48, 50, 63, 49,
                                                      64, 54, 44, 46, 63, 53, 49, 61,
                                                      64, 64, 63, 64, 61, 49, 46, 47,
                                                      64, 50, 53, 54, 58, 59, 61, 64,
                                                      64, 46, 48, 49, 37, 64, 48, 53,
                                                      64, 64, 49, 64, 64, 61, 47, 64,
                                                      64, 49, 44, 63, 53, 64, 63, 46,
                                                      64, 63, 59, 64, 61, 64, 48, 49 };
    
    /**
     * Array of residues to check when searching for base of perfect power.<br>
     * See: {@link #PERFECT_POWER_MODS}.<br>
     * Let m be PERFECT_POWER_MODS[ power ] and r be PERFECT_POWER_ALLOWED_RESIDUES[ power ].
     * For a given integer number n, if r[ n (mod m) ] = 0 then root of n of degree power
     * is not integer (otherwise it may be integer). Here r[ x ] means "get x-th bit of r".
     */
    private static final long PERFECT_POWER_ALLOWED_RESIDUES[] = { 0x4000000000000000L, 0xFFFFFFFFFFFFFFFFL, 0xC840804048000000L, 0xC080001818000102L, 0xC000800040000000L, 0xC10020E080104000L, 0xC000000808000000L, 0xC000300300008000L,
                                                                   0xC000000040000000L, 0xC000003800000400L, 0xC008000040000000L, 0xC000038000040000L, 0xC000000808000000L, 0xC000010200000800L, 0xC000200200000000L, 0xC010000000002008L,
                                                                   0xC000000000000000L, 0xD555555555555555L, 0xC000000808000000L, 0xD555555555555555L, 0xC004000000010000L, 0xC000000000008000L, 0xC000018000000000L, 0xC000000000020000L,
                                                                   0xC000000040000000L, 0xC10020E080104000L, 0xC000000000000800L, 0xC000003800000400L, 0xC000000600000000L, 0xC000000000000020L, 0xC000000000000008L, 0xD555555555555555L,
                                                                   0xC000000000000000L, 0xC000038000040000L, 0xC040804040000000L, 0xC000300300008000L, 0xC000000000000000L, 0xD555555555555555L, 0xC040804040000000L, 0xC000010200000800L,
                                                                   0xC000000040000000L, 0xD555555555555555L, 0xC000000000000000L, 0xD555555555555555L, 0xC000400040004000L, 0xC010000000002008L, 0xC000000000000000L, 0xD555555555555555L,
                                                                   0xC000000000000000L, 0xC000300300008000L, 0xC008000040000000L, 0xC080001818000102L, 0xC000000000000000L, 0xD555555555555555L, 0xC000000808000000L, 0xC000038000040000L,
                                                                   0xC000000040000000L, 0xC080001818000102L, 0xC000000000000000L, 0xD555555555555555L, 0xC000000000000000L, 0xD555555555555555L, 0xC040804040000000L, 0xC000000000008000L };

    /**
     * Some methods return this value as indication that the answer doesn't exist or is undefined.<br>
     * For example, when mod = 0 in modular arithmetics, or when a number isn't a perfect power and
     * a function should return its base.
     * <p>This constant equals to {@code Integer.MIN_VALUE} because {@code Integer.MIN_VALUE}
     * can't be a base of any perfect power that fits in long. Proof:<ul>
     * <li>Base of perfect square is a positive number.</li>
     * <li>Integer.MIN_VALUE<sup>3</sup> = ((-2)<sup>31</sup>)<sup>3</sup> = (-2)<sup>93</sup> &lt; (-2)<sup>63</sup> = Long.MIN_VALUE.</li>
     * <li>Higher powers would give higher values.</li></ul>
     * So no power greater than one can return base = {@code Integer.MIN_VALUE} for any long integer.
     * <p>Also, any modular arithmetic operation returns non-negative value,
     * so negative values can be used as error codes.
     * <p>Error codes are more desirable than throwing an exception when performance matters.
     */
    public static final int NOT_FOUND = Integer.MIN_VALUE;
    
    static
    {
        // Decimal values of Roman numerals initialization.
        DECIMAL_NUMBERS = new int[ ROMAN_NUMBERS.length ][];
        for ( int mode = 0; mode < ROMAN_NUMBERS.length; mode++ )
        {
            DECIMAL_NUMBERS[ mode ] = new int[ ROMAN_NUMBERS[ mode ].length ];
            for ( int i = 0; i < ROMAN_NUMBERS[ mode ].length; i++ ) DECIMAL_NUMBERS[ mode ][ i ] = toArabicNumeralsInternal( ROMAN_NUMBERS[ mode ][ i ], false, false, true, false );
        }
    }
  
    /**
     * Converts Roman numerals string to its decimal representation,
     * throwing an exception if the input is not a valid Roman numerals string.<br>
     * This is a classic form of Roman numerals with zero (N) permitted.<br>
     * III -&gt; 3<br>
     * MCMLXXXIV -&gt; 1984<br>
     * IV -&gt; 4<br>
     * IIII -&gt; exception
     * @param roman Roman numerals string to be converted
     * @return integer decimal value of input string
     * @throws IllegalArgumentException if input string is null, empty
     * or not a valid Roman number
     */
    public static int toArabicNumeralsInt( String roman ) throws IllegalArgumentException
    {
        return toArabicNumeralsInternal( roman, true, true, false, false ) >> 1;
    }
  
    /**
     * Converts Roman numerals string to its decimal representation.<br>
     * Allowed symbols are: M, D, C, L, X, V, I, S (half) and N (zero).
     * Minus sign is allowed too. No validation of characters order is performed.<br>
     * <p>III -&gt; 3<br>
     * MCMLXXXIV -&gt; 1984<br>
     * IV -&gt; 4<br>
     * IIII -&gt; 4<br>
     * IIX -&gt; 8 (I is subtracted twice from X)<br>
     * -M -&gt; -1000<br>
     * IVIX -&gt; 3 (I, V and I are subtracted from X)<br>
     * IVIXIV -&gt; 7 (IVIX + IV)<br>
     * IVIXIM -&gt; 982 (M - I - V - I - X - I)<br>
     * SV -&gt; 4.5<br>
     * SSS -&gt; 1.5
     * @param roman Roman numerals string to be converted
     * @return double floating point value of input string
     * @throws IllegalArgumentException if input contains illegal characters
     */
    public static double toArabicNumeralsDouble( String roman ) throws IllegalArgumentException
    {
        return toArabicNumeralsInternal( roman, false, true, true, true ) * 0.5;
    }
  
    /**
     * Converts Roman numerals string to its decimal representation.<br>
     * This function is identical to Microsoft Excel's ARABIC( text ) function.<br>
     * http://office.microsoft.com/en-gb/excel-help/arabic-function-HA102753258.aspx<br>
     * <p>That is:<ul>
     * <li>If the argument is null, empty or blank, 0 is returned.</li>
     * <li>If the input string is longer than 255 characters, an exception is thrown.
     * Therefore, 255000 is maximum value that can be returned.</li>
     * <li>The function is case insensitive.</li>
     * <li>Leading and trailing spaces are ignored.</li>
     * <li>Negative sign is permitted.</li>
     * <li>The input is not validated as in a classic variant {@link #toArabicNumeralsInt}.</li>
     * <li>However, if the input contains illegal characters, an exception is thrown.</li></ul>
     * <p>III -&gt; 3<br>
     * MCMLXXXIV -&gt; 1984<br>
     * IV -&gt; 4<br>
     * IIII -&gt; 4<br>
     * IIX -&gt; 8 (I is subtracted twice from X)<br>
     * -M -&gt; -1000<br>
     * IVIX -&gt; 3 (I, V and I are subtracted from X)<br>
     * IVIXIV -&gt; 7 (IVIX + IV)<br>
     * IVIXIM -&gt; 982 (M - I - V - I - X - I)
     * @param roman Roman numerals string to be converted
     * @return integer decimal value of input string
     * @throws IllegalArgumentException if input contains illegal characters
     */
    public static int toArabicNumeralsExcelInt( String roman ) throws IllegalArgumentException
    {
        if ( roman != null && roman.length() > 255 ) throw new IllegalArgumentException( "Input value is too long" );
        return toArabicNumeralsInternal( roman, false, false, false, true ) >> 1;
    }
    
    /**
     * Converts Roman numerals string to its decimal representation.
     * @param roman Roman numerals string to be converted<br>
     * @param validateInput <i>false:</i> allow any sequence of allowed characters, trim input, return 0 for blank<br>
     * <i>true:</i> allow only valid sequences of characters (deny "IM", "VIXD" etc.)<br>
     * @param permitZero allow character N as 0<br>
     * When validateInput is true then N cannot adjoin any other character except minus sign (if it's permitted)<br>
     * @param permitHalf allow character S as 0.5<br>
     * When validateInput is true then S is allowed only at last position of an input string<br>
     * @param permitMinus allow character "-" as negation of the resulting value<br>
     * This symbol is allowed only in the beginning of an input string<br>
     * @return integer decimal value of input string, <b>should be divided by 2</b><br>
     * @throws IllegalArgumentException if validateInput is true and input string is null, empty or not a valid Roman number;<br>
     * if validateInput is false and input string contains incorrect characters
     */
    private static int toArabicNumeralsInternal( String roman, boolean validateInput, boolean permitZero, boolean permitHalf, boolean permitMinus ) throws IllegalArgumentException
    {
        if ( validateInput )
        {
            if ( roman == null || roman.isEmpty() ) throw new IllegalArgumentException( "String cannot be empty" );
            // One minus sign at the beginning.
            // Input like "D-D" is considered not valid.
            String input = permitMinus && roman.startsWith( "-" ) ? roman.substring( 1 ) : roman;
            // The whole input string is "N".
            // Input like "XIN" is considered not valid.
            if ( !permitZero || !input.equalsIgnoreCase( "n" ) )
            {
                // One "S" at the end.
                // Input like "XSV" is considered not valid.
                if ( permitHalf && ( input.endsWith( "s" ) || input.endsWith( "S" ) ) ) input = input.substring( 0, input.length() - 1 );
                // This line (regular expression) rejects input like "IIII" and "MXD".
                // The rest part of this method works correctly for such kind of input numbers.
                // E.g. "IIII" results in 4, "MXD" would produce M + XD = M + ( D - X ) = 1000 + ( 500 - 10 ) = 1490.
                if ( !ROMAN_NUMBER.matcher( input ).matches() ) throw new IllegalArgumentException( "Not a valid Roman numeral" );
            }
        }
        else
        {
            if ( roman == null ) return 0;
            roman = roman.trim();
            if ( roman.isEmpty() ) return 0;
        }
        int base = 0;
        int ret = 0;
        char input[] = roman.toCharArray();
        for ( int i = input.length - 1; i >= 0; i-- )
        {
            int current;
            char c = input[ i ];
            if ( permitMinus && i == 0 && c == '-' )
            {
                ret = -ret;
                break;
            }
            switch ( c )
            {
                case 'M':
                case 'm':
                    // All values are doubled to allow half (S) processing in int.
                    current = 2000;
                    break;
                case 'D':
                case 'd':
                    current = 1000;
                    break;
                case 'C':
                case 'c':
                    current = 200;
                    break;
                case 'L':
                case 'l':
                    current = 100;
                    break;
                case 'X':
                case 'x':
                    current = 20;
                    break;
                case 'V':
                case 'v':
                    current = 10;
                    break;
                case 'I':
                case 'i':
                    current = 2;
                    break;
                case 'S':
                case 's':
                    if ( !permitHalf ) throw new IllegalArgumentException( "Half (S) is not permitted" );
                    // Half (semis).
                    // https://en.wikipedia.org/wiki/Roman_numerals#Fractions
                    current = 1;
                    break;
                case 'N':
                case 'n':
                    if ( !permitZero ) throw new IllegalArgumentException( "Zero (N) is not permitted" );
                    // Zero (nulla).
                    // https://en.wikipedia.org/wiki/Roman_numerals#Zero
                    current = 0;
                    break;
                default:
                    throw new IllegalArgumentException( "Unrecognized symbol found: " + c );
            }
            if ( base > current ) ret -= current;
            else
            {
                ret += current;
                base = current;
            }
        }
        return ret;
    }
  
    /**
     * Converts integer decimal number to Roman numerals string.<br>
     * Difference from {@link #toRomanNumeralsExcelString}: 0 (zero) is converted to N (nulla).
     * @param number a value to be converted, 0 &le; number &lt; 4000
     * @param shortest if true, then the minimal length Roman numerals string would be generated,<br>
     * otherwise this method obeys classic Arabic-to-Roman conversion rules
     * @return Roman numerals string representing value of number
     * @throws NumberFormatException if number &lt; 0 or number &ge; 4000
     */
    public static String toRomanNumeralsString( int number, boolean shortest ) throws NumberFormatException
    {
        return toRomanNumeralsInternal( number << 1, shortest ? 5 : 0, 8000, true, false, false );
    }
  
    /**
     * Converts double floating point number to Roman numerals string.<br>
     * Zero (0) is converted to N (nulla). Half (0.5) is converted to S (semis).
     * Negative numbers are permitted. Maximum allowed value is 999999.
     * @param number a value to be converted, -1e6 &le; number &le; 1e6
     * @param shortest if true, then the minimal length Roman numerals string would be generated,<br>
     * otherwise this method obeys classic Arabic-to-Roman conversion rules
     * @return Roman numerals string representing value of number
     * @throws NumberFormatException if number &le; -1e6 or number &ge; 1e6
     */
    public static String toRomanNumeralsString( double number, boolean shortest ) throws NumberFormatException
    {
        return toRomanNumeralsInternal( ( int )( number * 2.0 ), shortest ? 5 : 0, 2000000, true, true, false );
    }
  
    /**
     * Converts integer decimal number to Roman numerals string.<br>
     * This method is identical to Microsoft Excel's ROMAN( number, mode ) function.<br>
     * http://office.microsoft.com/en-gb/excel-help/roman-function-HA102752883.aspx?CTT=5&amp;origin=HA102753258<br>
     * <p>That is:<ul>
     * <li>Zero is converted to an empty string.</li>
     * <li>Negative numbers are denied.</li>
     * <li>Maximum input number is 3999.</li></ul>
     * @param number a value to be converted, 0 &le; number &lt; 4000
     * @param mode 0 - classic form of Roman numerals:<br>
     * I, V, X, L, C, D, M and subtractive sequences CM, CD, XC, XL, IX, IV are allowed<br>
     * 1 - more concise: LM, LD, VC, VL are also allowed<br>
     * 2 - more concise: XM, XD, IC, IL<br>
     * 3 - more concise: VM, VD<br>
     * 4 - minimal: IM, ID
     * @return Roman numerals string representing value of number
     * @throws NumberFormatException if number &lt; 0 or number &ge; 4000
     * @throws IllegalArgumentException is mode &lt; 0 or mode &gt; 4
     */
    public static String toRomanNumeralsExcelString( int number, int mode ) throws NumberFormatException, IllegalArgumentException
    {
        if ( mode < 0 || mode > 4 ) throw new IllegalArgumentException( "Acceptable modes are: 0, 1, 2, 3 and 4" );
        return toRomanNumeralsInternal( number << 1, mode, 8000, true, false, true );
    }
    
    /**
     * Converts integer decimal number to Roman numerals string.
     * @param number a value to be converted, <b>should be multiplied by 2</b><br>
     * @param mode 0 - classic form of Roman numerals:<br>
     * I, V, X, L, C, D, M and subtractive sequences CM, CD, XC, XL, IX, IV are allowed<br>
     * 1 - more concise: LM, LD, VC, VL are also allowed<br>
     * 2 - more concise: XM, XD, IC, IL<br>
     * 3 - more concise: VM, VD<br>
     * 4 - minimal: IM, ID<br>
     * 5 - absolutely minimal mode<br>
     * @param max permit values only in ( -max, max ) range, throw exception otherwise; should be positive<br>
     * @param permitZero permit zero input or throw exception<br>
     * @param permitNegative permit negative values as input or throw exception<br>
     * @param zeroAsEmpty if permitZero is set to true, return empty string (if true) or N (if false) for number = 0<br>
     * @return Roman numerals string representing value of number<br>
     * @throws NumberFormatException if one of the following situations occurs:<ul>
     * <li>number &le; -max</li>
     * <li>number &ge; max</li>
     * <li>number &lt; 0 and permitNegative = false</li>
     * <li>number = 0 and permitZero = false</li></ul>
     */
    private static String toRomanNumeralsInternal( int number, int mode, int max, boolean permitZero, boolean permitNegative, boolean zeroAsEmpty ) throws NumberFormatException
    {
        if ( number < 0 )
        {
            if ( number <= -max ) throw new NumberFormatException( "Number is too small" );
            if ( permitNegative ) return "-" + toRomanNumeralsInternal( -number, mode, max, permitZero, false, zeroAsEmpty );
            throw new NumberFormatException( "Number is negative" );
        }
        if ( number >= max ) throw new NumberFormatException( "Number is too big" );
        if ( number == 0 )
        {
            if ( permitZero )
            {
                // Microsoft Excel's ROMAN( 0, mode ) returns an empty string.
                if ( zeroAsEmpty ) return "";
                // Zero is N (nulla).
                // https://en.wikipedia.org/wiki/Roman_numerals#Zero
                return "N";
            }
            else throw new NumberFormatException( "Zero is not permitted" );
        }
        StringBuilder sb = new StringBuilder( 13 + number / 2000 );
        for ( int i = 0; i < ROMAN_NUMBERS[ mode ].length; i++ )
        {
            while ( number >= DECIMAL_NUMBERS[ mode ][ i ] )
            {
                number -= DECIMAL_NUMBERS[ mode ][ i ];
                sb.append( ROMAN_NUMBERS[ mode ][ i ] );
            }
        }
        return sb.toString();
    }

    /**
     * Returns a {@link java.math.BigDecimal} representation of a given number.<br>
     * If the class of a number isn't one of standard Java number classes,
     * the conversion is made through number doubleValue() method call.<br>
     * If a number is null then null is returned.
     * @param number a number to be converted
     * @return BigDecimal representation of a given number
     * @throws NumberFormatException if the number is infinite or NaN
     */
    public static BigDecimal toBigDecimal( Number number ) throws NumberFormatException
    {
        if ( number == null ) return null;
        if ( number instanceof BigDecimal ) return ( BigDecimal )number;
        if ( number instanceof Long ) return BigDecimal.valueOf( ( Long )number );
        if ( number instanceof Integer ) return BigDecimal.valueOf( ( Integer )number );
        if ( number instanceof BigInteger ) return new BigDecimal( ( BigInteger )number );
        // BigDecimal.valueOf( double ) is equivalent to new BigDecimal( Double.toString( double ) ).
        // new BigDecimal( double ) is inaccurate due to floating point representation errors.
        if ( number instanceof Double ) return BigDecimal.valueOf( ( Double )number );
        // BigDecimal.valueOf( float ) doesn't exist (at the time of Java 8)
        // and produces call to BigDecimal.valueOf( double ).
        // It leads float to double conversion.
        // The value in float could be accurately converted to string (e.g. 1e23 to "1.0e23").
        // But the same value in double has more precision places, so it is converted to string less accurately.
        // It sounds paradoxically, so let's take a look at the example:
        //             1E23  is   9.999999999999999E22
        //             1E23F is 1.0                E23
        //   ( double )1E23F is   9.999999778196308E22
        // The value has more precision places but is less accurate.
        // So it's better not to cast float to double but rather work with string representation of float
        // and call new BigDecimal( Float.toString( float ) ) by analogy to double.
        if ( number instanceof Float ) return new BigDecimal( Float.toString( ( Float )number ) );
        if ( number instanceof Short ) return BigDecimal.valueOf( ( Short )number );
        if ( number instanceof Byte ) return BigDecimal.valueOf( ( Byte )number );
        if ( number instanceof AtomicInteger ) return BigDecimal.valueOf( ( ( AtomicInteger )number ).get() );
        if ( number instanceof AtomicLong ) return BigDecimal.valueOf( ( ( AtomicLong )number ).get() );
        return BigDecimal.valueOf( number.doubleValue() );
    }

    /**
     * Return a BigInteger equal to the unsigned value of the argument.
     * @param number an unsigned int to be converted to BigInteger datatype
     * @return a BigInteger equal to the unsigned value of number
     */
    public static BigInteger toUnsignedBigInteger( int number )
    {
        return BigInteger.valueOf( Integer.toUnsignedLong( number ) );
    }

    /**
     * Return a BigInteger equal to the unsigned value of the argument.
     * This is a public version of a standard Java method {@link java.lang.Long#toUnsignedBigInteger}.
     * @see java.lang.Long#toUnsignedBigInteger
     * @param number an unsigned long to be converted to BigInteger datatype
     * @return a BigInteger equal to the unsigned value of number
     */
    public static BigInteger toUnsignedBigInteger( long number )
    {
        if ( number >= 0L ) return BigInteger.valueOf( number );
        return BigUtils.BI_2_POW_64.add( BigInteger.valueOf( number ) );
    }

    /**
     * Returns a string representation of a given number without an exponent field.<br>
     * When the value is {@link java.lang.Double#NaN} or infinite,
     * this method is equivalent to {@link java.lang.Double#toString()}.
     * It produces "NaN", "Infinity" and "-Infinity" for {@link java.lang.Double#NaN},
     * {@link java.lang.Double#POSITIVE_INFINITY} and {@link java.lang.Double#NEGATIVE_INFINITY} respectively.
     * When the value is integer, the fractional part is omitted. For instance, 1.0
     * would result in "1", not "1.0". Positive zero is converted to "0", negative zero - to "-0".
     * Fractional part is represented without an exponent field: 0.00000001 won't become "1e-8",
     * it will be "0.00000001".
     * @param value the double to be converted
     * @return string representation without an exponent field
     */
    public static String toPlainString( double value )
    {
        if ( value == 0.0 ) return Double.doubleToRawLongBits( value ) == 0L ? "0" : "-0";
        if ( Double.isNaN( value ) || Double.isInfinite( value ) ) return Double.toString( value );
        return BigDecimal.valueOf( value ).stripTrailingZeros().toPlainString();
    }

    /**
     * Returns a string representation of a given number without an exponent field.<br>
     * When the value is {@link java.lang.Float#NaN} or infinite,
     * this method is equivalent to {@link java.lang.Float#toString()}.
     * It produces "NaN", "Infinity" and "-Infinity" for {@link java.lang.Float#NaN},
     * {@link java.lang.Float#POSITIVE_INFINITY} and {@link java.lang.Float#NEGATIVE_INFINITY} respectively.
     * When the value is integer, the fractional part is omitted. For instance, 1.0
     * would result in "1", not "1.0". Positive zero is converted to "0", negative zero - to "-0".
     * Fractional part is represented without an exponent field: 0.00000001 won't become "1e-8",
     * it will be "0.00000001".
     * @param value the float to be converted
     * @return string representation without an exponent field
     */
    public static String toPlainString( float value )
    {
        if ( value == 0.0F ) return Float.floatToRawIntBits( value ) == 0 ? "0" : "-0";
        if ( Float.isNaN( value ) || Float.isInfinite( value ) ) return Float.toString( value );
        return new BigDecimal( Float.toString( value ) ).stripTrailingZeros().toPlainString();
    }

    /**
     * Returns a string representation of a number rounded to {@code precision} decimals
     * without an exponent field.<ul>
     * <li>When the number is null then null is returned.</li>
     * <li>When the value is double or float NaN then "NaN" is returned.</li>
     * <li>When the value is double or float Infinity then "Infinity" is returned.</li>
     * <li>For negative infinity, "-Infinity" is returned.</li>
     * <li>Both positive and negative zero produce "0" (e.g., -0.000234 rounded to 2 decimal places would produce "0", not "-0").</li>
     * <li>Negative number output equals to minus sign concatenated with positive number output (-2.4 produces "-2" with precision = 0 like 2.4 produces "2").</li>
     * <li>Round half up method is used for rounding positive numbers (the same as in {@link #round}).</li>
     * <li>Precision can be negative (5123.6 with precision -2 would produce "5100").</li></ul>
     * @see #round
     * @param number the value to be converted
     * @param precision quantity of places to the right of the decimal point
     * @return number string representation rounded to precision decimal points without an exponent field
     */
    public static String toRoundedString( Number number, int precision )
    {
        if ( number == null ) return null;
        if ( number instanceof Float )
        {
            Float f = ( Float )number;
            if ( f == 0.0F ) return "0";
            if ( Float.isNaN( f ) || Float.isInfinite( f ) ) return Float.toString( f );
        }
        else if ( number instanceof Double )
        {
            Double d = ( Double )number;
            if ( d == 0.0 ) return "0";
            if ( Double.isNaN( d ) || Double.isInfinite( d ) ) return Double.toString( d );
        }
        BigDecimal decimal = toBigDecimal( number );
        int signum = decimal.signum();
        if ( signum == 0 ) return "0";
        int scale = decimal.scale();
        precision = Math.max( Math.min( precision, scale ), scale - decimal.precision() );
        BigDecimal dr[] = decimal.scaleByPowerOfTen( precision ).divideAndRemainder( BigDecimal.ONE );
        if ( dr[ 1 ].abs().compareTo( BigUtils.BD_HALF ) >= 0 )
        {
            if ( signum > 0 ) decimal = dr[ 0 ].add( BigDecimal.ONE );
            else decimal = dr[ 0 ].subtract( BigDecimal.ONE );
        }
        else decimal = dr[ 0 ];
        decimal = decimal.scaleByPowerOfTen( -precision ).stripTrailingZeros();
        return decimal.toPlainString();
    }

    /**
     * Symmetric rounding half away from zero to {@code decimals} places to the right of the decimal point.
     * <p>http://en.wikipedia.org/wiki/Rounding#Round_half_away_from_zero<br>
     * Examples:<ul>
     * <li>round( 12.1, 0 ) = 12.0</li>
     * <li>round( 12.9, 0 ) = 13.0</li>
     * <li>round( 12.5, 0 ) = 13.0</li>
     * <li>round( 12.0, 0 ) = 12.0</li>
     * <li>round( 12.125, 2 ) = 12.13</li>
     * <li>round( 12.1, -1 ) = 10.0</li>
     * <li>round( -12.1, 0 ) = -12.0</li>
     * <li>round( -12.9, 0 ) = -13.0</li>
     * <li>round( -12.5, 0 ) = -13.0</li></ul>
     * This method differs from {@link java.lang.Math#round(double)} for negative numbers,
     * because the latter uses asymmetric rounding half up.<br>
     * See: http://en.wikipedia.org/wiki/Rounding#Round_half_up
     * @param value number to be rounded
     * @param decimals quantity of places to the right of the decimal point
     * @return {@code value} rounded to {@code decimals} places
     */
    public static double round( double value, int decimals )
    {
        double pw = pow10( decimals );
        if ( pw == Double.POSITIVE_INFINITY ) return value;
        if ( pw == 0.0 ) return Double.isFinite( value ) ? 0.0 : value;
        value *= pw;
        return ( value >= 0.0 ? Math.floor( value + 0.5 ) : Math.ceil( value - 0.5 ) ) / pw;
    }

    /**
     * Rounding towards zero (truncating) to {@code decimals} places to the right of the decimal point.
     * <p>http://en.wikipedia.org/wiki/Truncation<br>
     * Examples:<ul>
     * <li>trunc( 12.1, 0 ) = 12.0</li>
     * <li>trunc( 12.9, 0 ) = 12.0</li>
     * <li>trunc( 12.5, 0 ) = 12.0</li>
     * <li>trunc( 12.0, 0 ) = 12.0</li>
     * <li>trunc( 12.125, 2 ) = 12.12</li>
     * <li>trunc( 12.1, -1 ) = 10.0</li>
     * <li>trunc( -12.1, 0 ) = -12.0</li>
     * <li>trunc( -12.9, 0 ) = -12.0</li>
     * <li>trunc( -12.5, 0 ) = -12.0</li></ul>
     * @param value number to be truncated
     * @param decimals quantity of places to the right of the decimal point
     * @return {@code value} truncated to {@code decimals} places
     */
    public static double trunc( double value, int decimals )
    {
        double pw = pow10( decimals );
        if ( pw == Double.POSITIVE_INFINITY ) return value;
        if ( pw == 0.0 ) return Double.isFinite( value ) ? 0.0 : value;
        value *= pw;
        return ( value >= 0.0 ? Math.floor( value ) : Math.ceil( value ) ) / pw;
    }
  
    /**
     * Rounding up (ceiling) to {@code decimals} places to the right of the decimal point.
     * <p>http://en.wikipedia.org/wiki/Floor_and_ceiling_functions<br>
     * Examples:<ul>
     * <li>ceil( 12.1, 0 ) = 13.0</li>
     * <li>ceil( 12.9, 0 ) = 13.0</li>
     * <li>ceil( 12.5, 0 ) = 13.0</li>
     * <li>ceil( 12.0, 0 ) = 12.0</li>
     * <li>ceil( 12.125, 2 ) = 12.13</li>
     * <li>ceil( 12.1, -1 ) = 20.0</li>
     * <li>ceil( -12.1, 0 ) = -12.0</li>
     * <li>ceil( -12.9, 0 ) = -12.0</li>
     * <li>ceil( -12.5, 0 ) = -12.0</li></ul>
     * @param value number to be ceiled
     * @param decimals quantity of places to the right of the decimal point
     * @return {@code value} ceiled to {@code decimals} places
     */
    public static double ceil( double value, int decimals )
    {
        double pw = pow10( decimals );
        if ( pw == Double.POSITIVE_INFINITY ) return value;
        if ( pw == 0.0 ) return Double.isFinite( value ) ? 0.0 : value;
        value *= pw;
        return Math.ceil( value ) / pw;
    }

    /**
     * Rounding down (flooring) to {@code decimals} places to the right of the decimal point.
     * <p>http://en.wikipedia.org/wiki/Floor_and_ceiling_functions<br>
     * Examples:<ul>
     * <li>floor( 12.1, 0 ) = 12.0</li>
     * <li>floor( 12.9, 0 ) = 12.0</li>
     * <li>floor( 12.5, 0 ) = 12.0</li>
     * <li>floor( 12.0, 0 ) = 12.0</li>
     * <li>floor( 12.125, 2 ) = 12.12</li>
     * <li>floor( 12.1, -1 ) = 10.0</li>
     * <li>floor( -12.1, 0 ) = -13.0</li>
     * <li>floor( -12.9, 0 ) = -13.0</li>
     * <li>floor( -12.5, 0 ) = -13.0</li></ul>
     * @param value number to be floored
     * @param decimals quantity of places to the right of the decimal point
     * @return {@code value} floored to {@code decimals} places
     */
    public static double floor( double value, int decimals )
    {
        double pw = pow10( decimals );
        if ( pw == Double.POSITIVE_INFINITY ) return value;
        if ( pw == 0.0 ) return Double.isFinite( value ) ? 0.0 : value;
        value *= pw;
        return Math.floor( value ) / pw;
    }
    
    /**
     * Returns 10 raised to power y.
     * @param y the power
     * @return 10<sup>y</sup>
     */
    public static double pow10( int y )
    {
        // JDK-4358794: Math package: implement pow10 (power of 10) with optimization for integer powers.
        // http://bugs.sun.com/view_bug.do?bug_id=4358794
        return y < 0 || y >= 19 ? Double.parseDouble( "1E" + y ) : y < 10 ? pow( 10, y ) : pow( 10L, y );
    }
  
    /**
     * Overload of {@link java.lang.Math#pow} method for integer exponent.
     * @param x the base
     * @param y the exponent
     * @return the value x<sup>y</sup>
     * @see java.lang.Math#pow(double, double)
     */
    public static double pow( double x, int y )
    {
        if ( x == 10.0 ) return pow10( y );
        if ( x == -10.0 ) return ( y & 1 ) == 0 ? pow10( y ) : -pow10( y );
        double z = 1.0;
        if ( y < 0 )
        {
            y = -y;
            x = 1.0 / x;
        }
        while ( true )
        {
            if ( ( y & 1 ) != 0 ) z *= x;
            y >>>= 1;
            if ( y == 0 ) break;
            x *= x;
        }
        return z;
    }
    
    /**
     * Returns value of x raised in power y, throwing an exception if the result overflows a long.
     * @param x base
     * @param y power
     * @return x<sup>y</sup>
     * @throws ArithmeticException if the result overflows a long<br>
     * or if x = 0 and y &lt; 0 (resulting in infinity)
     */
    public static long powExact( long x, int y ) throws ArithmeticException
    {
        if ( y >= 0 )
        {
            long z = 1L;
            while ( true )
            {
                if ( ( y & 1 ) != 0 ) z = Math.multiplyExact( z, x );
                y >>>= 1;
                if ( y == 0 ) break;
                x = Math.multiplyExact( x, x );
            }
            return z;
        }
        else
        {
            if ( x == 0L ) throw new ArithmeticException( "Negative power of zero is infinity" );
            if ( x == 1L ) return 1L;
            if ( x == -1L ) return ( y & 1 ) == 0 ? 1L : -1L;
            return 0L;
        }
    }
    
    /**
     * Returns value of x raised in power y.
     * @param x base
     * @param y power
     * @return x<sup>y</sup>
     * @throws ArithmeticException if x = 0 and y &lt; 0 (resulting in infinity)
     */
    public static long pow( long x, int y ) throws ArithmeticException
    {
        if ( y >= 0 )
        {
            long z = 1L;
            while ( true )
            {
                if ( ( y & 1 ) != 0 ) z *= x;
                y >>>= 1;
                if ( y == 0 ) break;
                x *= x;
            }
            return z;
        }
        else
        {
            if ( x == 0L ) throw new ArithmeticException( "Negative power of zero is infinity" );
            if ( x == 1L ) return 1L;
            if ( x == -1L ) return ( y & 1 ) == 0 ? 1L : -1L;
            return 0L;
        }
    }
    
    /**
     * Returns value of x raised in power y, throwing an exception if the result overflows an int.
     * @param x base
     * @param y power
     * @return x<sup>y</sup>
     * @throws ArithmeticException if the result overflows an int<br>
     * or if x = 0 and y &lt; 0 (resulting in infinity)
     */
    public static int powExact( int x, int y ) throws ArithmeticException
    {
        if ( y >= 0 )
        {
            int z = 1;
            while ( true )
            {
                if ( ( y & 1 ) != 0 ) z = Math.multiplyExact( z, x );
                y >>>= 1;
                if ( y == 0 ) break;
                x = Math.multiplyExact( x, x );
            }
            return z;
        }
        else
        {
            if ( x == 0 ) throw new ArithmeticException( "Negative power of zero is infinity" );
            if ( x == 1 ) return 1;
            if ( x == -1 ) return ( y & 1 ) == 0 ? 1 : -1;
            return 0;
        }
    }
    
    /**
     * Returns value of x raised in power y.
     * @param x base
     * @param y power
     * @return x<sup>y</sup>
     * @throws ArithmeticException if x = 0 and y &lt; 0 (resulting in infinity)
     */
    public static int pow( int x, int y ) throws ArithmeticException
    {
        if ( y >= 0 )
        {
            int z = 1;
            while ( true )
            {
                if ( ( y & 1 ) != 0 ) z *= x;
                y >>>= 1;
                if ( y == 0 ) break;
                x *= x;
            }
            return z;
        }
        else
        {
            if ( x == 0 ) throw new ArithmeticException( "Negative power of zero is infinity" );
            if ( x == 1 ) return 1;
            if ( x == -1 ) return ( y & 1 ) == 0 ? 1 : -1;
            return 0;
        }
    }
    
    /**
     * Returns v (mod m).<br>
     * The value returned lies in range [ 0 .. |m| - 1 ].<br>
     * mod( x, m ) = mod( x, -m ).<br>
     * This method differs from v % m in that it always returns non-negative value.<br>
     * If m = 0 then {@link #NOT_FOUND} is returned.
     * @param v a value
     * @param m a modulus
     * @return v (mod m), or {@link #NOT_FOUND} if m = 0
     */
    public static int mod( int v, int m )
    {
        if ( m == 0 ) return NOT_FOUND;
        v %= m;
        if ( v < 0 )
        {
            // Note: if m = Integer.MIN_VALUE then addition behaves exactly like subtraction.
            if ( m < 0 ) v -= m;
            else v += m;
        }
        return v;
    }
    
    /**
     * Returns v (mod m).<br>
     * The value returned lies in range [ 0 .. |m| - 1 ].<br>
     * mod( x, m ) = mod( x, -m ).<br>
     * This method differs from v % m in that it always returns non-negative value.<br>
     * If m = 0 then {@link #NOT_FOUND} is returned.
     * @param v a value
     * @param m a modulus
     * @return v (mod m), or {@link #NOT_FOUND} if m = 0
     */
    public static long mod( long v, long m )
    {
        if ( m == 0L ) return NOT_FOUND;
        v %= m;
        if ( v < 0L )
        {
            // Note: if m = Long.MIN_VALUE then addition behaves exactly like subtraction.
            if ( m < 0L ) v -= m;
            else v += m;
        }
        return v;
    }
    
    /**
     * Signed mod.<br>
     * The value returned lies in range [ -( |m| - 1 ) / 2 .. |m| / 2 ].<br>
     * If m = 0 then {@link #NOT_FOUND} is returned.
     * @param v a value
     * @param m a modulus
     * @return v (mod m), or {@link #NOT_FOUND} if m = 0
     */
    public static int mods( int v, int m )
    {
        if ( m <= 0 )
        {
            if ( m == 0 ) return NOT_FOUND;
            if ( m == Integer.MIN_VALUE )
            {
                if ( v < -1073741823 || v > 1073741824 ) v += m;
                return v;
            }
            m = -m;
        }
        v %= m;
        if ( v > m >> 1 ) v -= m;
        else if ( v < -( ( m - 1 ) >> 1 ) ) v += m;
        return v;
    }
    
    /**
     * Signed mod.<br>
     * The value returned lies in range [ -( |m| - 1 ) / 2 .. |m| / 2 ].<br>
     * If m = 0 then {@link #NOT_FOUND} is returned.
     * @param v a value
     * @param m a modulus
     * @return v (mod m), or {@link #NOT_FOUND} if m = 0
     */
    public static long mods( long v, long m )
    {
        if ( m <= 0L )
        {
            if ( m == 0L ) return NOT_FOUND;
            if ( m == Long.MIN_VALUE )
            {
                if ( v < -4611686018427387903L || v > 4611686018427387904L ) v += m;
                return v;
            }
            m = -m;
        }
        v %= m;
        if ( v > m >> 1 ) v -= m;
        else if ( v < -( ( m - 1L ) >> 1 ) ) v += m;
        return v;
    }
  
    /**
     * Modular addition.<br>
     * Returns ( a + b )( mod m ).<br>
     * Differs from ( a + b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a + b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static int modAdd( long a, long b, int m )
    {
        if ( m <= 0 )
        {
            if ( m == 0 ) return NOT_FOUND;
            m = -m;
        }
        a %= m;
        b %= m;
        a = ( a + b ) % m;
        if ( a < 0L ) a += m;
        return ( int )a;
    }
  
    /**
     * Modular addition.<br>
     * Returns ( a + b )( mod m ).<br>
     * Differs from ( a + b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a + b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static long modAdd( long a, long b, long m )
    {
        if ( m <= 0L )
        {
            if ( m == 0L ) return NOT_FOUND;
            if ( m == Long.MIN_VALUE )
            {
                a += b;
                if ( a < 0L ) a += m;
                return a;
            }
            m = -m;
        }
        a %= m;
        b %= m;
        if ( a < 0L ) a += m;
        if ( b < 0L ) b += m;
        long leftTillOverflow = m - b;
        return leftTillOverflow > a ? a + b : a - leftTillOverflow;
    }
  
    /**
     * Modular subtraction.<br>
     * Returns ( a - b )( mod m ).<br>
     * Differs from ( a - b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a - b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static int modSubtract( long a, long b, int m )
    {
        if ( m <= 0 )
        {
            if ( m == 0 ) return NOT_FOUND;
            m = -m;
        }
        a %= m;
        b %= m;
        a = ( a - b ) % m;
        if ( a < 0L ) a += m;
        return ( int )a;
    }
  
    /**
     * Modular subtraction.<br>
     * Returns ( a - b )( mod m ).<br>
     * Differs from ( a - b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a - b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static long modSubtract( long a, long b, long m )
    {
        if ( m <= 0L )
        {
            if ( m == 0L ) return NOT_FOUND;
            if ( m == Long.MIN_VALUE )
            {
                a -= b;
                if ( a < 0L ) a += m;
                return a;
            }
            m = -m;
        }
        a %= m;
        b %= m;
        if ( a < 0L ) a += m;
        if ( b < 0L ) b = -b;
        else b = m - b;
        long leftTillOverflow = m - b;
        return leftTillOverflow > a ? a + b : a - leftTillOverflow;
    }
  
    /**
     * Modular multiplication.<br>
     * Returns ( a * b )( mod m ).<br>
     * Differs from ( a * b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a * b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static int modMultiply( long a, long b, int m )
    {
        if ( m <= 0 )
        {
            if ( m == 0 ) return NOT_FOUND;
            m = -m;
        }
        a %= m;
        b %= m;
        a = ( a * b ) % m;
        if ( a < 0L ) a += m;
        return ( int )a;
    }
  
    /**
     * Modular multiplication.<br>
     * Returns ( a * b )( mod m ).<br>
     * Differs from ( a * b ) % m in that it always returns non-negative value and never overflows.<br>
     * If m = 0, {@link #NOT_FOUND} is returned.
     * @param a first value
     * @param b second value
     * @param m a modulus
     * @return ( a * b )( mod m ), or {@link #NOT_FOUND} if m = 0
     */
    public static long modMultiply( long a, long b, long m )
    {
        if ( m <= 0L )
        {
            if ( m == 0L ) return NOT_FOUND;
            if ( m == Long.MIN_VALUE )
            {
                a *= b;
                if ( a < 0L ) a += m;
                return a;
            }
            m = -m;
        }
        a %= m;
        b %= m;
        if ( m <= Integer.MAX_VALUE )
        {
            // Safe simple multiplication available.
            a = ( a * b ) % m;
            if ( a < 0L ) a += m;
            return a;
        }
        if ( a < 0L ) a += m;
        if ( b < 0L ) b += m;
        // a = min( a, b ), b = max( a, b )
        if ( a > b )
        {
            long number = a;
            a = b;
            b = number;
        }
        // Corner cases of Schrage's method.
        if ( a < 2L ) return a * b;
        if ( b == m - 1L ) return m - a;
        // Safe simple multiplication available.
        if ( Long.numberOfLeadingZeros( a ) + Long.numberOfLeadingZeros( b ) > 64 ) return ( a * b ) % m;
        // Schrage's method.
        // http://home.earthlink.net/~pfenerty/pi/schrages_method.html
        // http://objectmix.com/java/312426-extending-schrage-multiplication.html
        long quot = m / a;
        long rem = m - quot * a;
        if ( rem < quot )
        {
            long number = b / quot;
            number = a * ( b - quot * number ) - rem * number;
            return number < 0L ? number + m : number;
        }
        // Bitwise multiplication.
        long leftTillOverflow;
        long number = 0L;
        while ( a > 0L )
        {
            if ( ( a & 1L ) == 1L )
            {
                leftTillOverflow = m - number;
                if ( leftTillOverflow > b ) number += b;
                else number = b - leftTillOverflow;
            }
            a >>= 1;
            leftTillOverflow = m - b;
            if ( leftTillOverflow > b ) b <<= 1;
            else b -= leftTillOverflow;
        }
        return number;
    }
    
    /**
     * Modular division. Returns a solution of equation ( x * b )( mod m ) = a.
     * <p>The solution has the form: x = x<sub>0</sub> + increment * k, where<ul>
     * <li>0 &le; k &lt; gcd, k is integer</li>
     * <li>increment = m / gcd</li>
     * <li>x<sub>0</sub> = a / gcd * {@link #modInverse}( b, m )</li>
     * <li>gcd = {@link #gcd}( b, m )</li></ul>
     * If a % gcd != 0 then the equation cannot be solved.<br>
     * The value returned is a tuple ( x<sub>0</sub>, increment, quantity ),
     * where quantity is the quantity of solutions, it equals to gcd.
     * If there's no solution, a tuple ( {@link #NOT_FOUND}, 0, 0 ) is returned.
     * <p>A simple loop to print all solutions (it handles situation when
     * solution[ 2 ] == Integer.MIN_VALUE - see section below):<br>
     * <code>int solution[] = modDivide( a, b, m );<br>
     * int x = solution[ 0 ];<br>
     * int increment = solution[ 1 ];<br>
     * for ( int k = -solution[ 2 ]; k &lt; 0; k++ )<br>
     * {<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println( x );<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;x += increment;<br>
     * }</code>
     * <p><b>Examples:</b><br><br>
     * <b>modDivide( 5, 3, 7 )</b><br>
     * modDivide( 5, 3, 7 ) = ( 5 / 3 )( mod 7 ) = ( 5 * ( 3<sup>-1</sup>( mod 7 ) ) )( mod 7 ) = ( 5 * 5 )( mod 7 ) = 4( mod 7 )<br>
     * <i>Check:</i> ( 4 * 3 )( mod 7 ) = 12( mod 7 ) = 5( mod 7 )<br>
     * <i>Returned value:</i> [ 4, 7, 1 ]<br><br>
     * <b>modDivide( 4, 2, 6 )</b><br>
     * gcd = gcd( 2, 6 ) = 2<br>
     * modDivide( 4 / gcd, 2 / gcd, 6 / gcd ) = modDivide( 2, 1, 3 ) = ( 2 * ( 1<sup>-1</sup>( mod 3 ) ) )( mod 3 ) = ( 2 * 1 )( mod 3 ) = 2( mod 3 )<br>
     * modDivide( 4, 2, 6 ) = ( 2 + 6 / gcd * k )( mod 6 ) = ( 2 + 3 * k )( mod 6 ), where 0 &le; k &lt; gcd = 2, so there are two solutions: 2 and 5<br>
     * <i>Check:</i> ( 2 * ( 2 + 3 * 0 ) )( mod 6 ) = ( 2 * 2 )( mod 6 ) = 4( mod 6 )<br>
     * <i>Check:</i> ( 2 * ( 2 + 3 * 1 ) )( mod 6 ) = ( 2 * 5 )( mod 6 ) = 4( mod 6 )<br>
     * <i>Returned value:</i> [ 2, 3, 2 ]
     * <p>About Integer.MIN_VALUE:<br>
     * First solution, increment and quantity are non-negative, except for special cases of MIN_VALUE.<ul>
     * <li>If b = 0 or MIN_VALUE and m = MIN_VALUE then solutions quantity (gcd) equals to MIN_VALUE (is negative).</li>
     * <li>If b &ne; 2<sup>k</sup> for any integer k and m = MIN_VALUE then solutions quantity (gcd) equals to 1 (one) and increment = MIN_VALUE.</li>
     * <li>First solution is never negative. {@link #NOT_FOUND} is used to indicate that the solution doesn't exist.</li></ul>
     * So increment = MIN_VALUE doesn't matter due to having only one solution (the increment won't be used).
     * The only exception situation is b = 0 or MIN_VALUE and m = MIN_VALUE leading to solutions quantity = MIN_VALUE.
     * @param a the dividend
     * @param b the divisor
     * @param m the modulus
     * @return a tuple ( first solution x<sub>0</sub>, increment, quantity ),
     * which produces a set of solutions of equation ( x * b )( mod m ) = a
     * in the form x = x<sub>0</sub> + increment * k, where k is integer, 0 &le; k &lt; quantity.<br>
     * If there's no solution, a tuple ( {@link #NOT_FOUND}, 0, 0 ) is returned.
     */
    public static int[] modDivide( int a, int b, int m )
    {
        if ( m == 0 ) return new int[]{ NOT_FOUND, 0, 0 };
        a %= m;
        // Extended GCD for b and m.
        int x = 0;
        int u = 1;
        int gcd = m;
        while ( b != 0 )
        {
            int q = gcd / b;
            int r = gcd - q * b;
            int n = x - u * q;
            gcd = b;
            b = r;
            x = u;
            u = n;
        }
        if ( gcd < 0 && gcd > Integer.MIN_VALUE )
        {
            gcd = -gcd;
            x = -x;
        }
        // gcd = 0 if and only if b = 0 and m = 0.
        // m != 0 => gcd != 0.
        u = a / gcd;
        if ( u * gcd == a )
        {
            m /= gcd;
            x = modMultiply( u, x, m );
            // x is the first solution,
            // m is the increment,
            // gcd is the quantity of solutions.
            if ( m < 0 ) m = -m;
            return new int[]{ x, m, gcd };
        }
        return new int[]{ NOT_FOUND, 0, 0 };
    }
    
    /**
     * Modular division. Returns a solution of equation ( x * b )( mod m ) = a.
     * <p>The solution has the form: x = x<sub>0</sub> + increment * k, where<ul>
     * <li>0 &le; k &lt; gcd, k is integer</li>
     * <li>increment = m / gcd</li>
     * <li>x<sub>0</sub> = a / gcd * {@link #modInverse}( b, m )</li>
     * <li>gcd = {@link #gcd}( b, m )</li></ul>
     * If a % gcd != 0 then the equation cannot be solved.<br>
     * The value returned is a tuple ( x<sub>0</sub>, increment, quantity ),
     * where quantity is the quantity of solutions, it equals to gcd.
     * If there's no solution, a tuple ( {@link #NOT_FOUND}, 0, 0 ) is returned.
     * <p>A simple loop to print all solutions (it handles situation when
     * solution[ 2 ] == Long.MIN_VALUE - see section below):<br>
     * <code>long solution[] = modDivide( a, b, m );<br>
     * long x = solution[ 0 ];<br>
     * long increment = solution[ 1 ];<br>
     * for ( long k = -solution[ 2 ]; k &lt; 0L; k++ )<br>
     * {<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println( x );<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;x += increment;<br>
     * }</code>
     * <p><b>Examples:</b><br><br>
     * <b>modDivide( 5, 3, 7 )</b><br>
     * modDivide( 5, 3, 7 ) = ( 5 / 3 )( mod 7 ) = ( 5 * ( 3<sup>-1</sup>( mod 7 ) ) )( mod 7 ) = ( 5 * 5 )( mod 7 ) = 4( mod 7 )<br>
     * <i>Check:</i> ( 4 * 3 )( mod 7 ) = 12( mod 7 ) = 5( mod 7 )<br>
     * <i>Returned value:</i> [ 4, 7, 1 ]<br><br>
     * <b>modDivide( 4, 2, 6 )</b><br>
     * gcd = gcd( 2, 6 ) = 2<br>
     * modDivide( 4 / gcd, 2 / gcd, 6 / gcd ) = modDivide( 2, 1, 3 ) = ( 2 * ( 1<sup>-1</sup>( mod 3 ) ) )( mod 3 ) = ( 2 * 1 )( mod 3 ) = 2( mod 3 )<br>
     * modDivide( 4, 2, 6 ) = ( 2 + 6 / gcd * k )( mod 6 ) = ( 2 + 3 * k )( mod 6 ), where 0 &le; k &lt; gcd = 2, so there are two solutions: 2 and 5<br>
     * <i>Check:</i> ( 2 * ( 2 + 3 * 0 ) )( mod 6 ) = ( 2 * 2 )( mod 6 ) = 4( mod 6 )<br>
     * <i>Check:</i> ( 2 * ( 2 + 3 * 1 ) )( mod 6 ) = ( 2 * 5 )( mod 6 ) = 4( mod 6 )<br>
     * <i>Returned value:</i> [ 2, 3, 2 ]
     * <p>About Long.MIN_VALUE:<br>
     * First solution, increment and quantity are non-negative, except for special cases of MIN_VALUE.<ul>
     * <li>If b = 0 or MIN_VALUE and m = MIN_VALUE then solutions quantity (gcd) equals to MIN_VALUE (is negative).</li>
     * <li>If b &ne; 2<sup>k</sup> for any integer k and m = MIN_VALUE then solutions quantity (gcd) equals to 1 (one) and increment = MIN_VALUE.</li>
     * <li>First solution is never negative. {@link #NOT_FOUND} is used to indicate that the solution doesn't exist.</li></ul>
     * So increment = MIN_VALUE doesn't matter due to having only one solution (the increment won't be used).
     * The only exception situation is b = 0 or MIN_VALUE and m = MIN_VALUE leading to solutions quantity = MIN_VALUE.
     * @param a the dividend
     * @param b the divisor
     * @param m the modulus
     * @return a tuple ( first solution x<sub>0</sub>, increment, quantity ),
     * which produces a set of solutions of equation ( x * b )( mod m ) = a
     * in the form x = x<sub>0</sub> + increment * k, where k is integer, 0 &le; k &lt; quantity.<br>
     * If there's no solution, a tuple ( {@link #NOT_FOUND}, 0, 0 ) is returned.
     */
    public static long[] modDivide( long a, long b, long m )
    {
        if ( Integer.MIN_VALUE < m && m <= Integer.MAX_VALUE )
        {
            if ( m == 0L ) return new long[]{ NOT_FOUND, 0L, 0L };
            int ret[] = modDivide( ( int )( a % m ), ( int )( b % m ), ( int )m );
            return new long[]{ ret[ 0 ], ret[ 1 ], ret[ 2 ] };
        }
        a %= m;
        // Extended GCD for b and m.
        long x = 0L;
        long u = 1L;
        long gcd = m;
        while ( b != 0L )
        {
            long q = gcd / b;
            long r = gcd - q * b;
            long n = x - u * q;
            gcd = b;
            b = r;
            x = u;
            u = n;
        }
        if ( gcd < 0L && gcd > Long.MIN_VALUE )
        {
            gcd = -gcd;
            x = -x;
        }
        // gcd = 0 if and only if b = 0 and m = 0.
        // m != 0 => gcd != 0.
        u = a / gcd;
        if ( u * gcd == a )
        {
            m /= gcd;
            x = modMultiply( u, x, m );
            // x is the first solution,
            // m is the increment,
            // gcd is the quantity of solutions.
            if ( m < 0L ) m = -m;
            return new long[]{ x, m, gcd };
        }
        return new long[]{ NOT_FOUND, 0L, 0L };
    }
    
    /**
     * Returns s = a<sup>-1</sup> (mod m), s is such a number that (s * a) (mod m) = 1.<br>
     * If such number s doesn't exist, {@link #NOT_FOUND} is returned (if a and m are not coprime). Notable cases:<ul>
     * <li>If m &lt; 0, the result is equivalent to modInverse( a, -m ).</li>
     * <li>If m = 0 then {@link #NOT_FOUND} is returned.</li>
     * <li>If m = 1 then 0 is always returned.</li>
     * <li>If m &gt; 1 and a = 0 then {@link #NOT_FOUND} is returned.</li>
     * <li>If m &gt; 1, m is prime and 0 &lt; a &lt; m then the result is positive.</li></ul>
     * The result is always non-negative if it exists, or {@link #NOT_FOUND} otherwise.<br>
     * http://en.wikipedia.org/wiki/Modular_multiplicative_inverse
     * @see java.math.BigInteger#modInverse
     * @param a value
     * @param m modulus
     * @return a<sup>-1</sup> (mod m),<br>
     * or {@link #NOT_FOUND} if the multiplicative inverse of a mod m doesn't exist (a and m are not coprime),<br>
     * or {@link #NOT_FOUND} if m = 0 (division by zero)
     */
    public static int modInverse( int a, int m )
    {
        if ( m == 0 ) return NOT_FOUND;
        if ( m == -1 || m == 1 ) return 0;
        // Extended GCD for a and m.
        int x = 0;
        int u = 1;
        int gcd = m;
        while ( a != 0 )
        {
            int q = gcd / a;
            int r = gcd - q * a;
            int n = x - u * q;
            gcd = a;
            a = r;
            x = u;
            u = n;
        }
        if ( gcd < 0 && gcd > Integer.MIN_VALUE )
        {
            gcd = -gcd;
            x = -x;
        }
        if ( gcd == 1 )
        {
            if ( x < 0 )
            {
                if ( m >= 0 ) x += m;
                else x -= m;
            }
            return x;
        }
        return NOT_FOUND;
    }

    /**
     * Returns s = a<sup>-1</sup> (mod m), s is such a number that (s * a) (mod m) = 1.<br>
     * If such number s doesn't exist, {@link #NOT_FOUND} is returned (if a and m are not coprime). Notable cases:<ul>
     * <li>If m &lt; 0, the result is equivalent to modInverse( a, -m ).</li>
     * <li>If m = 0 then {@link #NOT_FOUND} is returned.</li>
     * <li>If m = 1 then 0 is always returned.</li>
     * <li>If m &gt; 1 and a = 0 then {@link #NOT_FOUND} is returned.</li>
     * <li>If m &gt; 1, m is prime and 0 &lt; a &lt; m then the result is positive.</li></ul>
     * The result is always non-negative if it exists, or {@link #NOT_FOUND} otherwise.<br>
     * http://en.wikipedia.org/wiki/Modular_multiplicative_inverse
     * @see java.math.BigInteger#modInverse
     * @param a value
     * @param m modulus
     * @return a<sup>-1</sup> (mod m),<br>
     * or {@link #NOT_FOUND} if the multiplicative inverse of a mod m doesn't exist (a and m are not coprime),<br>
     * or {@link #NOT_FOUND} if m = 0 (division by zero)
     */
    public static long modInverse( long a, long m )
    {
        if ( Integer.MIN_VALUE <= m && m <= Integer.MAX_VALUE )
        {
            if ( m == 0L ) return NOT_FOUND;
            return modInverse( ( int )( a % m ), ( int )m );
        }
        // Extended GCD for a and m.
        long x = 0L;
        long u = 1L;
        long gcd = m;
        while ( a != 0L )
        {
            long q = gcd / a;
            long r = gcd - q * a;
            long n = x - u * q;
            gcd = a;
            a = r;
            x = u;
            u = n;
        }
        if ( gcd < 0L && gcd > Long.MIN_VALUE )
        {
            gcd = -gcd;
            x = -x;
        }
        if ( gcd == 1L )
        {
            if ( x < 0L )
            {
                if ( m >= 0L ) x += m;
                else x -= m;
            }
            return x;
        }
        return NOT_FOUND;
    }
    
    /**
     * Raise base to exponent power mod m.
     * @param base the base
     * @param exponent the exponent
     * @param m the modulus
     * @return base<sup>exponent</sup> (mod m),<br>
     * or {@link #NOT_FOUND} if exponent &lt; 0 and base is not relatively prime to m,<br>
     * or {@link #NOT_FOUND} if m = 0 (division by zero)
     */
    public static int modPow( long base, long exponent, int m )
    {
        if ( m <= 0 )
        {
            if ( m == 0 ) return NOT_FOUND;
            m = -m;
        }
        if ( m == 1 ) return 0;
        if ( exponent < 0L )
        {
            base = modInverse( base, m );
            if ( base == NOT_FOUND ) return NOT_FOUND;
            if ( exponent == Long.MIN_VALUE )
            {
                // One additional loop iteration.
                exponent >>= 1;
                base = ( base * base ) % m;
            }
            exponent = -exponent;
        }
        else
        {
            base %= m;
            if ( base < 0L )
            {
                // For m = Integer.MIN_VALUE.
                if ( m <= 0 ) base -= m;
                else base += m;
            }
        }
        long number = 1L;
        while ( exponent > 0L )
        {
            if ( ( exponent & 1L ) == 1L ) number = ( number * base ) % m;
            exponent >>= 1;
            base = ( base * base ) % m;
        }
        return ( int )number;
    }
  
    /**
     * Raise base to exponent power mod m.
     * @param base the base
     * @param exponent the exponent
     * @param m the modulus
     * @return base<sup>exponent</sup> (mod m),<br>
     * or {@link #NOT_FOUND} if exponent &lt; 0 and base is not relatively prime to m,<br>
     * or {@link #NOT_FOUND} if m = 0 (division by zero)
     */
    public static long modPow( long base, long exponent, long m )
    {
        if ( Integer.MIN_VALUE <= m && m <= Integer.MAX_VALUE ) return modPow( base, exponent, ( int )m );
        // Implementation via multiplication, like in modPow( int ), is slower than BigInteger.modPow
        // because modMultiply( long ) is not a constant time method (it performs bitwise multiplication in worst case) unlike modMultiply( int ).
        try
        {
            return BigInteger.valueOf( base ).modPow( BigInteger.valueOf( exponent ), BigInteger.valueOf( m ).abs() ).longValue();
        }
        catch ( ArithmeticException e )
        {
            return NOT_FOUND;
        }
    }
    
    /**
     * Extended Euclidean greatest common divisor (GCD) algorithm function.<br>
     * A tuple ( gcd, x, y ) is returned, where gcd is greatest common divisor of a and b,
     * x and y are integers such that x * a + y * b = gcd. The first value, gcd, is positive
     * except for these cases:<ul>
     * <li>Both a and b equal to 0 =&gt; gcd = 0.</li>
     * <li>One of the arguments equals to Integer.MIN_VALUE and another one equals to 0 =&gt; gcd = Integer.MIN_VALUE.</li>
     * <li>Both a and b equal to Integer.MIN_VALUE =&gt; gcd = Integer.MIN_VALUE</li></ul>
     * http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
     * @see #gcd
     * @param a first value with which the GCD is to be computed
     * @param b second value with which the GCD is to be computed
     * @return a tuple ( gcd, x, y ), where x * a + y * b = gcd = gcd( a, b )
     */
    public static int[] egcd( int a, int b )
    {
        int x = 0;
        int y = 1;
        int u = 1;
        int v = 0;
        while ( a != 0 )
        {
            int q = b / a;
            int r = b - q * a;
            int m = x - u * q;
            int n = y - v * q;
            b = a;
            a = r;
            x = u;
            y = v;
            u = m;
            v = n;
        }
        if ( b < 0 && b > Integer.MIN_VALUE )
        {
            b = -b;
            x = -x;
            y = -y;
        }
        return new int[]{ b, x, y };
    }
    
    /**
     * Extended Euclidean greatest common divisor (GCD) algorithm function.<br>
     * A tuple ( gcd, x, y ) is returned, where gcd is greatest common divisor of a and b,
     * x and y are integers such that x * a + y * b = gcd. The first value, gcd, is positive
     * except for these cases:<ul>
     * <li>Both a and b equal to 0 =&gt; gcd = 0.</li>
     * <li>One of the arguments equals to Long.MIN_VALUE and another one equals to 0 =&gt; gcd = Long.MIN_VALUE.</li>
     * <li>Both a and b equal to Long.MIN_VALUE =&gt; gcd = Long.MIN_VALUE</li></ul>
     * http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
     * @see #gcd
     * @param a first value with which the GCD is to be computed
     * @param b second value with which the GCD is to be computed
     * @return a tuple ( gcd, x, y ), where x * a + y * b = gcd = gcd( a, b )
     */
    public static long[] egcd( long a, long b )
    {
        long x = 0L;
        long y = 1L;
        long u = 1L;
        long v = 0L;
        while ( a != 0L )
        {
            long q = b / a;
            long r = b - q * a;
            long m = x - u * q;
            long n = y - v * q;
            b = a;
            a = r;
            x = u;
            y = v;
            u = m;
            v = n;
        }
        if ( b < 0L && b > Long.MIN_VALUE )
        {
            b = -b;
            x = -x;
            y = -y;
        }
        return new long[]{ b, x, y };
    }

    /**
     * Greatest common divisor.<br>
     * http://en.wikipedia.org/wiki/Greatest_common_divisor<br>
     * gcd( 6, 9 ) = 3<br>
     * gcd( 4, 9 ) = 1<br>
     * gcd( 0, 9 ) = 9 - see: http://math.stackexchange.com/questions/27719/what-is-gcd0-a-where-a-is-a-positive-integer<br>
     * gcd( 0, 0 ) = 0 - this is the only situation when the result is zero.<br>
     * gcd( 0, Integer.MIN_VALUE ) = Integer.MIN_VALUE<br>
     * gcd( Integer.MIN_VALUE, 0 ) = Integer.MIN_VALUE<br>
     * gcd( Integer.MIN_VALUE, Integer.MIN_VALUE ) = Integer.MIN_VALUE
     * - these are the only situations when the result is negative,
     * because abs( Integer.MIN_VALUE ) cannot fit in int.<br>
     * gcd( a, b ) = gcd( -a, b ) = gcd( a, -b ) = gcd( -a, -b ) = gcd( b, a )<br>
     * The result is always positive except four exceptional situations described above.
     * @param a first number
     * @param b second number
     * @return greatest common divisor of a and b
     */
    public static int gcd( int a, int b )
    {
        if ( a == 0 ) return b < 0 ? -b : b;
        if ( b == 0 ) return a < 0 ? -a : a;
        if ( a < 0 )
        {
            // Integer.MIN_VALUE is power of two, so greatest common divisor is lowest set bit of second argument.
            // See: Integer.lowestOneBit.
            if ( a == Integer.MIN_VALUE ) return b & -b;
            a = -a;
        }
        if ( b < 0 )
        {
            if ( b == Integer.MIN_VALUE ) return a & -a;
            b = -b;
        }
        // Euclidean algorithm.
        // Binary algorithm seems to be slower on modern computers.
        // Both algorithms have the same asymptotics.
        while ( b > 0 )
        {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    /**
     * Greatest common divisor.<br>
     * http://en.wikipedia.org/wiki/Greatest_common_divisor<br>
     * gcd( 6, 9 ) = 3<br>
     * gcd( 4, 9 ) = 1<br>
     * gcd( 0, 9 ) = 9 - see: http://math.stackexchange.com/questions/27719/what-is-gcd0-a-where-a-is-a-positive-integer<br>
     * gcd( 0, 0 ) = 0 - this is the only situation when the result is zero.<br>
     * gcd( 0, Long.MIN_VALUE ) = Long.MIN_VALUE<br>
     * gcd( Long.MIN_VALUE, 0 ) = Long.MIN_VALUE<br>
     * gcd( Long.MIN_VALUE, Long.MIN_VALUE ) = Long.MIN_VALUE
     * - these are the only situations when the result is negative,
     * because abs( Long.MIN_VALUE ) cannot fit in long.<br>
     * gcd( a, b ) = gcd( -a, b ) = gcd( a, -b ) = gcd( -a, -b ) = gcd( b, a )<br>
     * The result is always positive except four exceptional situations described above.
     * @param a first number
     * @param b second number
     * @return greatest common divisor of a and b
     */
    public static long gcd( long a, long b )
    {
        if ( a == 0L ) return b < 0L ? -b : b;
        if ( b == 0L ) return a < 0L ? -a : a;
        if ( a < 0L )
        {
            // Long.MIN_VALUE is power of two, so greatest common divisor is lowest set bit of second argument.
            // See: Long.lowestOneBit.
            if ( a == Long.MIN_VALUE ) return b & -b;
            a = -a;
        }
        if ( b < 0L )
        {
            if ( b == Long.MIN_VALUE ) return a & -a;
            b = -b;
        }
        if ( a <= Integer.MAX_VALUE && b <= Integer.MAX_VALUE )
        {
            int aa = ( int )a;
            int bb = ( int )b;
            while ( bb > 0 )
            {
                int c = aa % bb;
                aa = bb;
                bb = c;
            }
            return aa;
        }
        while ( b > 0L )
        {
            long c = a % b;
            a = b;
            b = c;
        }
        return a;
    }
    
    /**
     * Determine if a is relatively prime to b, i.e. gcd( a, b ) = 1.
     * @param a first number
     * @param b second number
     * @return true iff a is relatively prime to b
     */
    public static boolean isRelativelyPrime( int a, int b )
    {
        return gcd( a, b ) == 1;
    }
    
    /**
     * Determine if a is relatively prime to b, i.e. gcd( a, b ) = 1.
     * @param a first number
     * @param b second number
     * @return true iff a is relatively prime to b
     */
    public static boolean isRelativelyPrime( long a, long b )
    {
        return gcd( a, b ) == 1L;
    }
    
    /**
     * Least common multiple.<br>
     * http://en.wikipedia.org/wiki/Least_common_multiple<br>
     * lcm( 6, 9 ) = 18<br>
     * lcm( 4, 9 ) = 36<br>
     * lcm( 0, 9 ) = 0<br>
     * lcm( 0, 0 ) = 0
     * @param a first number
     * @param b second number
     * @return least common multiple of a and b
     */
    public static int lcm( int a, int b )
    {
        if ( a == 0 || b == 0 ) return 0;
        return Math.abs( a / gcd( a, b ) * b );
    }
    
    /**
     * Least common multiple.<br>
     * http://en.wikipedia.org/wiki/Least_common_multiple<br>
     * lcm( 6, 9 ) = 18<br>
     * lcm( 4, 9 ) = 36<br>
     * lcm( 0, 9 ) = 0<br>
     * lcm( 0, 0 ) = 0
     * @param a first number
     * @param b second number
     * @return least common multiple of a and b
     */
    public static long lcm( long a, long b )
    {
        if ( a == 0L || b == 0L ) return 0L;
        return Math.abs( a / gcd( a, b ) * b );
    }
    
    /**
     * Least common multiple with overflow check.<br>
     * http://en.wikipedia.org/wiki/Least_common_multiple<br>
     * lcm( 6, 9 ) = 18<br>
     * lcm( 4, 9 ) = 36<br>
     * lcm( 0, 9 ) = 0<br>
     * lcm( 0, 0 ) = 0
     * @param a first number
     * @param b second number
     * @return least common multiple of a and b
     * @throws ArithmeticException if the result overflows an int
     */
    public static int lcmExact( int a, int b ) throws ArithmeticException
    {
        if ( a == 0 || b == 0 ) return 0;
        a = Math.multiplyExact( a / gcd( a, b ), b );
        if ( a < 0 )
        {
            if ( a == Integer.MIN_VALUE ) throw new ArithmeticException( "int overflow" );
            return -a;
        }
        return a;
    }
    
    /**
     * Least common multiple with overflow check.<br>
     * http://en.wikipedia.org/wiki/Least_common_multiple<br>
     * lcm( 6, 9 ) = 18<br>
     * lcm( 4, 9 ) = 36<br>
     * lcm( 0, 9 ) = 0<br>
     * lcm( 0, 0 ) = 0
     * @param a first number
     * @param b second number
     * @return least common multiple of a and b
     * @throws ArithmeticException if the result overflows a long
     */
    public static long lcmExact( long a, long b ) throws ArithmeticException
    {
        if ( a == 0L || b == 0L ) return 0L;
        a = Math.multiplyExact( a / gcd( a, b ), b );
        if ( a < 0L )
        {
            if ( a == Long.MIN_VALUE ) throw new ArithmeticException( "long overflow" );
            return -a;
        }
        return a;
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
    public static long isqrt( long n ) throws ArithmeticException
    {
        if ( n < 0L ) throw new ArithmeticException( "Square root of negative number is undefined" );
        if ( n <= Integer.MAX_VALUE ) return isqrt( ( int )n );
        long ret = ( long )Math.sqrt( n );
        long retn = ret * ret;
        if ( retn < 0L || retn > n ) ret--;
        return ret;
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
    public static int isqrt( int n ) throws ArithmeticException
    {
        if ( n < 0 ) throw new ArithmeticException( "Square root of negative number is undefined" );
        int ret = ( int )Math.sqrt( n );
        int retn = ret * ret;
        if ( retn < 0 || retn > n ) ret--;
        return ret;
    }
    
    /**
     * Returns integer square root of n treated as unsigned. The resulting int is unsigned too.
     * <p>Integer square root: http://en.wikipedia.org/wiki/Integer_square_root
     * <p>The greatest integer less than or equal to the square root of n. Example:<br>
     * isqrt( 27 ) = 5 because 5 * 5 = 25 &le; 27 and 6 * 6 = 36 &gt; 27
     * @param n radicand
     * @return trunc( sqrt( n ) ) treated as unsigned
     */
    public static int uisqrt( long n )
    {
        // The source of the code below and explanations:
        // http://www.codecodex.com/wiki/Calculate_an_integer_square_root#Java
        if ( ( n & 0xFFF0000000000000L ) == 0L ) return ( int )Math.sqrt( n );
        long result = ( long )Math.sqrt( 2.0 * ( n >>> 1 ) );
        return result * result - n > 0L ? ( int )result - 1 : ( int )result;
    }
    
    /**
     * Returns integer square root of n treated as unsigned.
     * <p>Integer square root: http://en.wikipedia.org/wiki/Integer_square_root
     * <p>The greatest integer less than or equal to the square root of n. Example:<br>
     * isqrt( 27 ) = 5 because 5 * 5 = 25 &le; 27 and 6 * 6 = 36 &gt; 27
     * @param n radicand
     * @return trunc( sqrt( n ) )
     */
    public static int uisqrt( int n )
    {
        // The source of the code below and explanations:
        // http://www.codecodex.com/wiki/Calculate_an_integer_square_root#Java
        return ( int )Math.sqrt( n & 0xFFFFFFFFL );
    }
    
    /**
     * Returns integer cubic root of n.
     * <p>If n &ge; 0 then the result is the greatest integer less than or equal to the cubic root of n.<br>
     * If n &lt; 0 then the result is the least integer greater than or equal to the cubic root of n. Example:<br>
     * icbrt( 28 ) = 3 because 3 * 3 * 3 = 27 &le; 28 and 4 * 4 * 4 = 64 &gt; 28
     * @param n radicand
     * @return trunc( cbrt( n ) )
     */
    public static long icbrt( long n )
    {
        if ( n >= Integer.MIN_VALUE && n <= Integer.MAX_VALUE ) return icbrt( ( int )n );
        long ret = ( long )Math.cbrt( n );
        long retn = ret * ret * ret;
        if ( n >= 0L )
        {
            if ( retn < 0L || retn > n ) ret--;
        }
        else
        {
            if ( retn >= 0L || retn < n ) ret++;
        }
        return ret;
    }
    
    /**
     * Returns integer cubic root of n.
     * <p>If n &ge; 0 then the result is the greatest integer less than or equal to the cubic root of n.<br>
     * If n &lt; 0 then the result is the least integer greater than or equal to the cubic root of n. Example:<br>
     * icbrt( 28 ) = 3 because 3 * 3 * 3 = 27 &le; 28 and 4 * 4 * 4 = 64 &gt; 28
     * @param n radicand
     * @return trunc( cbrt( n ) )
     */
    public static int icbrt( int n )
    {
        int ret = ( int )Math.cbrt( n );
        int retn = ret * ret * ret;
        if ( n >= 0 )
        {
            if ( retn < 0 || retn > n ) ret--;
        }
        else
        {
            if ( retn >= 0 || retn < n ) ret++;
        }
        return ret;
    }
    
    /**
     * Returns integer root of n of given degree power.
     * <p>If n &ge; 0 then the result is the greatest integer less than or equal to the root of n.<br>
     * If n &lt; 0 then the result is the least integer greater than or equal to the root of n. Examples:<br>
     * iroot( 28, 3 ) = 3 because 3<sup>3</sup> = 27 &le; 28 and 4<sup>3</sup> = 64 &gt; 28<br>
     * iroot( -28, 3 ) = -3 because (-3)<sup>3</sup> = -27 &ge; -28 and (-4)<sup>3</sup> = -64 &lt; -28
     * <p>Special cases:<ul>
     * <li>power is even and n &lt; 0 =&gt; the answer should be a complex number and cannot be represented as rational integer</li>
     * <li>power = 0 and n = 0 =&gt; n<sup>1/power</sup> = 0<sup>&infin;</sup> = 0</li>
     * <li>power = 0 and n = 1 =&gt; n<sup>1/power</sup> = 1<sup>&infin;</sup> - undefined<br>
     * See: http://math.stackexchange.com/questions/10490/why-is-1-infty-considered-to-be-an-indeterminate-form</li>
     * <li>power = 0 and n &gt; 1 =&gt; n<sup>1/power</sup> = n<sup>&infin;</sup> = &infin; - out of range</li>
     * <li>power &lt; 0 and n = 0 =&gt; n<sup>1/power</sup> = 1 / 0<sup>1/abs(power)</sup> = 1 / 0 = &infin; - out of range</li>
     * <li>power &lt; 0 and n = &plusmn;1 =&gt; n<sup>1/power</sup> = 1 / (&plusmn;1)<sup>1/abs(power)</sup> = &plusmn;1</li>
     * <li>power &lt; 0 and abs(n) &gt; 1 =&gt; n<sup>1/power</sup> = 1 / n<sup>1/abs(power)</sup><br>
     * n<sup>1/abs(power)</sup> &gt; 1 =&gt; 1 / n<sup>1/abs(power)</sup> &lt; 1, so the answer is 0<br>
     * Examples: iroot( 16, -2 ) = trunc( 1 / root( 16, 2 ) ) = trunc( 1 / 4 ) = 0<br>
     * iroot( 16, -16 ) = trunc( 1 / root( 16, 16 ) ) = trunc( 1 / 1.189 ) = trunc( 0.841 ) = 0</li></ul>
     * @param n radicand
     * @param power degree of a root
     * @return trunc( root ) where root<sup>power</sup> = n
     * @throws ArithmeticException if one of the conditions holds:<ul>
     * <li>n &lt; 0 and power is even (the result is a complex number)</li>
     * <li>n = 0 and power &lt; 0 (resulting in infinity)</li>
     * <li>n = 1 and power = 0 (the result is 1<sup>&infin;</sup>, which is undefined)</li>
     * <li>n &gt; 1 and power = 0 (the result is n<sup>&infin;</sup> = &infin;)</li></ul>
     */
    public static long iroot( long n, int power ) throws ArithmeticException
    {
        if ( n >= Integer.MIN_VALUE && n <= Integer.MAX_VALUE ) return iroot( ( int )n, power );
        if ( n < 0L && ( power & 1 ) == 0 ) throw new ArithmeticException( "Even root of negative number is undefined" );
        if ( power <= 3 )
        {
            if ( power <= 1 )
            {
                if ( power < 0 )
                {
                    if ( n == -1L || n == 1L ) return n;
                    if ( n == 0L ) throw new ArithmeticException( "Negative root of zero is infinity" );
                    return 0L;
                }
                else if ( power == 0 )
                {
                    if ( n == 0L ) return n;
                    // http://math.stackexchange.com/questions/10490/why-is-1-infty-considered-to-be-an-indeterminate-form
                    if ( n == 1L ) throw new ArithmeticException( "Zero root of one is undefined" );
                    throw new ArithmeticException( "Zero root of positive number is infinity" );
                }
                // power == 1.
                else return n;
            }
            // Math.sqrt( n ) is faster than Math.pow( n, ( double )1 / 2 ).
            else if ( power == 2 ) return isqrt( n );
            // power == 3.
            // Math.cbrt( n ) is more precise than Math.pow( n, ( double )1 / 3 ).
            else return icbrt( n );
        }
        // For big powers (greater than or equal to 63):
        // n = 0 => root = 0.
        // n = 1 => root = 1.
        // n = -1 => root = -1 (even powers were already rejected for negative bases).
        // Roots of any other base that can be represented as long will be truncated to one.
        // The only exclusion is root( Long.MIN_VALUE, 63 ) = -2.
        if ( power >= 63 ) return power == 63 && n == Long.MIN_VALUE ? -2L : n < 0L ? -1L : n == 0L ? 0L : 1L;
        long ret;
        // Math.pow returns NaN for negative bases and fractional powers.
        // So we need to take an absolute value of the base.
        if ( n < 0L )
        {
            // Math.abs( Long.MIN_VALUE ) cannot be represented in long.
            if ( n == Long.MIN_VALUE )
            {
                switch ( power )
                {
                    // Cases less than 4 and more than 62 were processed before.
                    // Even cases were rejected for negative bases.
                    case 5:
                        return -6208L;
                    case 7:
                        return -512L;
                    case 9:
                        return -128L;
                    case 11:
                        return -52L;
                    case 13:
                        return -28L;
                    case 15:
                        return -18L;
                    case 17:
                        return -13L;
                    case 19:
                        return -9L;
                    case 21:
                        return -8L;
                    case 23:
                        return -6L;
                    case 25:
                    case 27:
                        return -5L;
                    case 29:
                    case 31:
                        return -4L;
                    case 33:
                    case 35:
                    case 37:
                    case 39:
                        return -3L;
                    default:
                        return -2L;
                }
            }
            ret = ( long )( Math.pow( -n, 1.0 / power ) + 0.5 );
        }
        else ret = ( long )( Math.pow( n, 1.0 / power ) + 0.5 );
        // Overflow check.
        if ( ret > MAX_ROOTS_LONG[ power ] ) ret = MAX_ROOTS_LONG[ power ];
        if ( n < 0L ) ret = -ret;
        long retn = pow( ret, power );
        if ( retn == n ) return ret;
        // Correction of the result.
        if ( n >= 0L )
        {
            if ( retn < 0L || retn > n ) ret--;
            else
            {
                try
                {
                    long nextRetn = powExact( ret + 1L, power );
                    if ( nextRetn > 0L && nextRetn <= n ) ret++;
                }
                catch ( ArithmeticException e )
                {
                    // Overflow. Stop increasing.
                }
            }
        }
        else
        {
            if ( retn >= 0L || retn < n ) ret++;
            else
            {
                try
                {
                    long nextRetn = powExact( ret - 1L, power );
                    if ( nextRetn < 0L && nextRetn >= n ) ret--;
                }
                catch ( ArithmeticException e )
                {
                    // Underflow. Stop decreasing.
                }
            }
        }
        return ret;
    }
    
    /**
     * Returns integer root of n of given degree power.
     * <p>If n &ge; 0 then the result is the greatest integer less than or equal to the root of n.<br>
     * If n &lt; 0 then the result is the least integer greater than or equal to the root of n. Examples:<br>
     * iroot( 28, 3 ) = 3 because 3<sup>3</sup> = 27 &le; 28 and 4<sup>3</sup> = 64 &gt; 28<br>
     * iroot( -28, 3 ) = -3 because (-3)<sup>3</sup> = -27 &ge; -28 and (-4)<sup>3</sup> = -64 &lt; -28
     * <p>Special cases:<ul>
     * <li>power is even and n &lt; 0 =&gt; the answer should be a complex number and cannot be represented as rational integer</li>
     * <li>power = 0 and n = 0 =&gt; n<sup>1/power</sup> = 0<sup>&infin;</sup> = 0</li>
     * <li>power = 0 and n = 1 =&gt; n<sup>1/power</sup> = 1<sup>&infin;</sup> - undefined<br>
     * See: http://math.stackexchange.com/questions/10490/why-is-1-infty-considered-to-be-an-indeterminate-form</li>
     * <li>power = 0 and n &gt; 1 =&gt; n<sup>1/power</sup> = n<sup>&infin;</sup> = &infin; - out of range</li>
     * <li>power &lt; 0 and n = 0 =&gt; n<sup>1/power</sup> = 1 / 0<sup>1/abs(power)</sup> = 1 / 0 = &infin; - out of range</li>
     * <li>power &lt; 0 and n = &plusmn;1 =&gt; n<sup>1/power</sup> = 1 / (&plusmn;1)<sup>1/abs(power)</sup> = &plusmn;1</li>
     * <li>power &lt; 0 and abs(n) &gt; 1 =&gt; n<sup>1/power</sup> = 1 / n<sup>1/abs(power)</sup><br>
     * n<sup>1/abs(power)</sup> &gt; 1 =&gt; 1 / n<sup>1/abs(power)</sup> &lt; 1, so the answer is 0<br>
     * Examples: iroot( 16, -2 ) = trunc( 1 / root( 16, 2 ) ) = trunc( 1 / 4 ) = 0<br>
     * iroot( 16, -16 ) = trunc( 1 / root( 16, 16 ) ) = trunc( 1 / 1.189 ) = trunc( 0.841 ) = 0</li></ul>
     * @param n radicand
     * @param power degree of a root
     * @return trunc( root ) where root<sup>power</sup> = n
     * @throws ArithmeticException if one of the conditions holds:<ul>
     * <li>n &lt; 0 and power is even (the result is a complex number)</li>
     * <li>n = 0 and power &lt; 0 (resulting in infinity)</li>
     * <li>n = 1 and power = 0 (the result is 1<sup>&infin;</sup>, which is undefined)</li>
     * <li>n &gt; 1 and power = 0 (the result is n<sup>&infin;</sup> = &infin;)</li></ul>
     */
    public static int iroot( int n, int power ) throws ArithmeticException
    {
        if ( n < 0 && ( power & 1 ) == 0 ) throw new ArithmeticException( "Even root of negative number is undefined" );
        if ( power <= 3 )
        {
            if ( power <= 1 )
            {
                if ( power < 0 )
                {
                    if ( n == -1 || n == 1 ) return n;
                    if ( n == 0 ) throw new ArithmeticException( "Negative root of zero is infinity" );
                    return 0;
                }
                else if ( power == 0 )
                {
                    if ( n == 0 ) return n;
                    // http://math.stackexchange.com/questions/10490/why-is-1-infty-considered-to-be-an-indeterminate-form
                    if ( n == 1 ) throw new ArithmeticException( "Zero root of one is undefined" );
                    throw new ArithmeticException( "Zero root of positive number is infinity" );
                }
                // power == 1.
                else return n;
            }
            // Math.sqrt( n ) is faster than Math.pow( n, ( double )1 / 2 ).
            else if ( power == 2 ) return isqrt( n );
            // power == 3.
            // Math.cbrt( n ) is more precise than Math.pow( n, ( double )1 / 3 ).
            else return icbrt( n );
        }
        // For big powers (greater than or equal to 31):
        // n = 0 => root = 0.
        // n = 1 => root = 1.
        // n = -1 => root = -1 (even powers were already rejected for negative bases).
        // Roots of any other base that can be represented as long will be truncated to one.
        // The only exclusion is root( Integer.MIN_VALUE, 31 ) = -2.
        if ( power >= 31 ) return power == 31 && n == Integer.MIN_VALUE ? -2 : n < 0 ? -1 : n == 0 ? 0 : 1;
        int ret;
        // Math.pow returns NaN for negative bases and fractional powers.
        // So we need to take an absolute value of the base.
        if ( n < 0 )
        {
            // Math.abs( Integer.MIN_VALUE ) cannot be represented in int.
            if ( n == Integer.MIN_VALUE )
            {
                switch ( power )
                {
                    // Cases less than 4 and more than 30 were processed before.
                    // Even cases were rejected for negative bases.
                    case 5:
                        return -73;
                    case 7:
                        return -21;
                    case 9:
                        return -10;
                    case 11:
                        return -7;
                    case 13:
                        return -5;
                    case 15:
                        return -4;
                    case 17:
                    case 19:
                        return -3;
                    default:
                        return -2;
                }
            }
            ret = ( int )( Math.pow( -n, 1.0 / power ) + 0.5 );
        }
        else ret = ( int )( Math.pow( n, 1.0 / power ) + 0.5 );
        // Overflow check.
        if ( ret > MAX_ROOTS_INT[ power ] ) ret = MAX_ROOTS_INT[ power ];
        if ( n < 0 ) ret = -ret;
        int retn = pow( ret, power );
        if ( retn == n ) return ret;
        // Correction of the result.
        if ( n >= 0 )
        {
            if ( retn < 0 || retn > n ) ret--;
            else
            {
                try
                {
                    int nextRetn = powExact( ret + 1, power );
                    if ( nextRetn > 0 && nextRetn <= n ) ret++;
                }
                catch ( ArithmeticException e )
                {
                    // Overflow. Stop increasing.
                }
            }
        }
        else
        {
            if ( retn >= 0 || retn < n ) ret++;
            else
            {
                try
                {
                    int nextRetn = powExact( ret - 1, power );
                    if ( nextRetn < 0 && nextRetn >= n ) ret--;
                }
                catch ( ArithmeticException e )
                {
                    // Underflow. Stop decreasing.
                }
            }
        }
        return ret;
    }
    
    /**
     * Given integer number n, find integer number s such that s<sup>2</sup> = n.<br>
     * Return {@link #NOT_FOUND} if such number s doesn't exists.
     * <p>Perfect square: http://en.wikipedia.org/wiki/Square_number
     * <p>Implementation is based on this discussion:<br>
     * http://stackoverflow.com/questions/295579/fastest-way-to-determine-if-an-integers-square-root-is-an-integer
     * @param n an integer square number
     * @return integer number s such that s<sup>2</sup> = n<br>
     * or {@link #NOT_FOUND} if such number s doesn't exist
     */
    public static long getBaseOfPerfectSquare( long n )
    {
        // Negative value cannot be square.
        if ( n < 0L ) return NOT_FOUND;
        // Square remainders mod 64 are only 0, 1, 4, 9 etc.
        // All of them are packed in a magic constant.
        // Each bit of this constant shows if the corresponding value can be a perfect square.
        if ( ( ( Long.MIN_VALUE >>> ( n & 0x3FL ) ) & 0xC840C04048404040L ) == 0L ) return NOT_FOUND;
        // The same check for mod 255. Allowed values are packed in array CAN_255_RESIDUE_BE_PERFECT_SQUARE.
        long in = n;
        in = ( in & 0xFFFFFFFFL ) + ( in >> 32 );
        in = ( in & 0xFFFFL ) + ( in >> 16 );
        int inn = ( int )in;
        inn = ( inn & 0xFF ) + ( ( inn >> 8 ) & 0xFF ) + ( inn >> 16 );
        if ( ( CAN_255_RESIDUE_BE_PERFECT_SQUARE[ inn >> 5 ] & ( Integer.MIN_VALUE >>> ( inn & 0x3F ) ) ) == 0 ) return NOT_FOUND;
        // Cut off powers of two.
        int b = Long.numberOfTrailingZeros( n );
        if ( b > 0 )
        {
            // If n is divisible by 2 but not by 4 then it cannot be a square.
            if ( ( b & 1 ) == 1 ) return NOT_FOUND;
            n >>= b;
            // One more extra check of allowed residues mod 8. Those are 0, 1 and 4.
            // But powers of 2 were eliminated, so 4 becomes 1, and 0 can remain only if n = 0.
            // It means that 1 is the only allowed residue mod 7 at this step (exclusive case is n = 0).
            if ( ( n & 7L ) != 1L ) return n == 0L ? 0L : NOT_FOUND;
        }
        long sqrt;
        // Threshold for Quake sqrt algorithm ( T = 15966596881 ), see below.
        if ( n < 15966596881L )
        {
            // Quake inverse square root hack.
            // http://www.codemaestro.com/reviews/9
            // This method doesn't suit for general integer root calculation.
            // Though it suits well for perfect squares root calculation.
            float x2 = n * 0.5F;
            float y = n;
            int i = Float.floatToRawIntBits( y );
            // This magic constant ( C = 1597463205 ) was gained by testing all integer values.
            // It gives maximum value of T mentioned above.
            // T( C ) is minimum N such that QuakeSqrt( N, C ) != isqrt( N ).
            // So it gives the best initial approximation for our needs.
            // It differs from original magic constant and from Chris Lomont constant.
            // http://www.lomont.org/Math/Papers/2003/InvSqrt.pdf
            // Their constants are less accurate for two iterations of method applied to integer sqrt search.
            i = 1597463205 - ( i >> 1 );
            y = Float.intBitsToFloat( i );
            y *= 1.5F - ( x2 * y * y );
            y *= 1.5F - ( x2 * y * y );
            sqrt = ( long )( 1.0F / y + 0.5F );
        }
        // Math.sqrt gives accurate integer result for perfect squares.
        // No additional search is required (unlike in isqrt function).
        else sqrt = ( long )Math.sqrt( n );
        // Return root if it exists for a given number n.
        // Also restore powers of two that were cut earlier (the resulting root contains only half of them).
        return sqrt * sqrt == n ? sqrt << ( b >> 1 ) : NOT_FOUND;
    }
    
    /**
     * Determine if a given number n is perfect square.<br>
     * http://en.wikipedia.org/wiki/Square_number
     * @param n number to check
     * @return true if and only if there exists integer number s such that s<sup>2</sup> = n
     */
    public static boolean isPerfectSquare( long n )
    {
        return getBaseOfPerfectSquare( n ) != NOT_FOUND;
    }
    
    /**
     * Given integer number n, find integer number s such that s<sup>3</sup> = n.<br>
     * Return {@link #NOT_FOUND} if such number s doesn't exists.
     * @param n an integer square number
     * @return integer number s such that s<sup>3</sup> = n<br>
     * or {@link #NOT_FOUND} if such number s doesn't exist
     */
    public static long getBaseOfPerfectCube( long n )
    {
        // Negative value cannot be square.
        if ( n <= 0L )
        {
            if ( n == 0L ) return 0L;
            // Long.MIN_VALUE cannot be negated, so it needs special processing.
            // cbrt( -2 ^ 63 ) = -2 ^ 21 = -2097152.
            if ( n == Long.MIN_VALUE ) return -2097152L;
            // cbrt( -x ) = -cbrt( x ).
            long ret = getBaseOfPerfectCube( -n );
            return ret == NOT_FOUND ? NOT_FOUND : -ret;
        }
        // Cube remainders mod 63 are only 0, 1, 8, 27 etc.
        // All of them are packed in a magic constant.
        // Each bit of this constant shows if the corresponding value can be a perfect cube.
        if ( ( ( Long.MIN_VALUE >>> ( n % 63L ) ) & 0xC080001818000102L ) == 0L ) return NOT_FOUND;
        // Cut off powers of two.
        int b = Long.numberOfTrailingZeros( n );
        if ( b > 0 )
        {
            // If n is divisible by 2 or 4 but not by 8 then it cannot be a cube.
            // If n = 0 then b = 64 and b % 3 != 0, so this case is processed in the beginning of the function.
            if ( b % 3 != 0 ) return NOT_FOUND;
            n >>= b;
        }
        // Math.cbrt returns accurate integer result for perfect cubes.
        // No additional search is required (unlike in icbrt function).
        long cbrt = ( long )Math.cbrt( n );
        // Return root if it exists for a given number n.
        // Also restore powers of two that were cut earlier (the resulting root contains only one third of them).
        return cbrt * cbrt * cbrt == n ? cbrt << ( b / 3 ) : NOT_FOUND;
    }
    
    /**
     * Determine if a given number n is perfect cube.
     * @param n number to check
     * @return true if and only if there exists integer number s such that s<sup>3</sup> = n
     */
    public static boolean isPerfectCube( long n )
    {
        return getBaseOfPerfectCube( n ) != NOT_FOUND;
    }
    
    /**
     * Given integer numbers n and power, find integer number s such that s<sup>power</sup> = n.<br>
     * Return {@link #NOT_FOUND} if such number s doesn't exists.
     * @param n an integer square number
     * @param power root degree
     * @return integer number s such that s<sup>power</sup> = n<br>
     * or {@link #NOT_FOUND} if such number s doesn't exist
     */
    public static long getBaseOfPerfectPower( long n, int power )
    {
        if ( power < 4 )
        {
            // These functions contain optimizations specific to powers 2 and 3.
            if ( power == 2 ) return getBaseOfPerfectSquare( n );
            if ( power == 3 ) return getBaseOfPerfectCube( n );
            // Perfect power is n = m ^ k such that k > 1.
            // http://en.wikipedia.org/wiki/Perfect_power
            // So lower powers are unacceptable.
            return NOT_FOUND;
        }
        // Negative value cannot be a square.
        if ( n <= 1L )
        {
            if ( n >= 0L ) return n;
            // Even root of negative power is a complex number.
            if ( ( power & 1 ) == 0 ) return NOT_FOUND;
            // Long.MIN_VALUE cannot be negated, so it needs special processing.
            if ( n == Long.MIN_VALUE )
            {
                switch ( power )
                {
                    // Case 1 is unacceptable due to definition of perfect power.
                    // Case 3 was processed before (in call to getBaseOfPerfectCube).
                    case 7:
                        return -512L;
                    case 9:
                        return -128L;
                    case 21:
                        return -8L;
                    case 63:
                        return -2L;
                    default:
                        return NOT_FOUND;
                }
            }
            // root( -x ) = -root( x ).
            long ret = getBaseOfPerfectPower( -n, power );
            return ret == NOT_FOUND ? NOT_FOUND : -ret;
        }
        if ( power >= 16 )
        {
            // Powers greater than 62 would lead to overflow, so cannot be represented as long.
            // Cases n = 0 and n = 1 were accepted earlier.
            if ( power > 62 ) return NOT_FOUND;
            // High prime powers like 17 or 61 have too many allowed residues mod m (2 <= m <= 64).
            // So residues filtering step would reject not more than a half of perfect power candidates.
            // And the rest half would reach the end of this method, where Math.pow is called, which is relatively slow.
            // But there are not so many perfect powers in long int range when power is greater than 15.
            // So we can just enumerate them all.
            return getBaseOfPerfectPowerFrom16To62( n, power );
        }
        // Perfect powers can have only several residues with the given mod.
        // If the remainder of n (mod m) is not in list of allowed residues,
        // then n is definitely not a perfect power (for method parameter "power").
        // See PERFECT_POWER_MODS and PERFECT_POWER_ALLOWED_RESIDUES javadoc for more detailed description.
        if ( ( ( Long.MIN_VALUE >>> ( n % PERFECT_POWER_MODS[ power ] ) ) & PERFECT_POWER_ALLOWED_RESIDUES[ power ] ) == 0L ) return NOT_FOUND;
        // Cut off powers of two.
        int b = Long.numberOfTrailingZeros( n );
        if ( b > 0 )
        {
            // If n is divisible by 2 but not by 2^power then it cannot be a perfect power.
            // If n = 0 then b = 64 and b % power may be not zero, so this case is processed in the beginning of the function.
            if ( b % power != 0 ) return NOT_FOUND;
            n >>= b;
        }
        // If power is big enough and n is small then root of n of degree power cannot be integer.
        // Cases n = 0 and n = 1 were accepted earlier, so minimal root at this step is 2.
        // And minimal root ^ power is 2 ^ power. Any n less than 2 ^ power is not acceptable.
        if ( power > b && n >> ( power - b ) == 0L ) return NOT_FOUND;
        // Math.pow value should be rounded (not truncated), because of floating point
        // representation error for second argument "1/power".
        // There's no need to iterate while pow( root, power ) != n like in iroot function.
        // We deal only with perfect powers. Math.pow gives accurate result for perfect powers.
        long root = ( long )( Math.pow( n, 1.0 / power ) + 0.5 );
        // Return root if it exists for a given number n.
        // Also restore powers of two that were cut earlier (the resulting root contains only 1/power of them).
        return pow( root, power ) == n ? root << ( b / power ) : NOT_FOUND;
    }
    
    /**
     * Determine if a given number n is perfect power with a given power.
     * @param n number to check
     * @param power power to check
     * @return true if and only if there exists integer number s such that s<sup>power</sup> = n
     */
    public static boolean isPerfectPower( long n, int power )
    {
        return getBaseOfPerfectPower( n, power ) != NOT_FOUND;
    }
    
    /**
     * There exist not so many perfect powers in long int range when power is between 16 and 62 (inclusively).<br>
     * This function enumerates them all and returns a corresponding root if it exists.
     * @param n an integer square number, n &ge; 2
     * @param power root degree, 16 &le; power &le; 62
     * @return integer number s such that s<sup>power</sup> = n<br>
     * or {@link #NOT_FOUND} if that number s doesn't exist
     */
    private static long getBaseOfPerfectPowerFrom16To62( long n, int power )
    {
        switch ( power )
        {
            case 16:
                // Java has no switch on long datatype, so this code is verbose.
                if ( n < 1853020188851841L )
                {
                    if ( n < 152587890625L )
                    {
                        if ( n < 4294967296L )
                        {
                            if ( n == 65536L ) return 2L;
                            if ( n == 43046721L ) return 3L;
                            return NOT_FOUND;
                        }
                        if ( n == 4294967296L ) return 4L;
                        return NOT_FOUND;
                    }
                    if ( n < 33232930569601L )
                    {
                        if ( n == 152587890625L ) return 5L;
                        if ( n == 2821109907456L ) return 6L;
                        return NOT_FOUND;
                    }
                    if ( n == 33232930569601L ) return 7L;
                    if ( n == 281474976710656L ) return 8L;
                    return NOT_FOUND;
                }
                if ( n < 184884258895036416L )
                {
                    if ( n < 45949729863572161L )
                    {
                        if ( n == 1853020188851841L ) return 9L;
                        if ( n == 10000000000000000L ) return 10L;
                        return NOT_FOUND;
                    }
                    if ( n == 45949729863572161L ) return 11L;
                    return NOT_FOUND;
                }
                if ( n < 2177953337809371136L )
                {
                    if ( n == 184884258895036416L ) return 12L;
                    if ( n == 665416609183179841L ) return 13L;
                    return NOT_FOUND;
                }
                if ( n == 2177953337809371136L ) return 14L;
                if ( n == 6568408355712890625L ) return 15L;
                return NOT_FOUND;
            case 17:
                if ( n < 2251799813685248L )
                {
                    if ( n < 762939453125L )
                    {
                        if ( n < 17179869184L )
                        {
                            if ( n == 131072L ) return 2L;
                            if ( n == 129140163L ) return 3L;
                            return NOT_FOUND;
                        }
                        if ( n == 17179869184L ) return 4L;
                        return NOT_FOUND;
                    }
                    if ( n < 232630513987207L )
                    {
                        if ( n == 762939453125L ) return 5L;
                        if ( n == 16926659444736L ) return 6L;
                        return NOT_FOUND;
                    }
                    if ( n == 232630513987207L ) return 7L;
                    return NOT_FOUND;
                }
                if ( n < 505447028499293771L )
                {
                    if ( n < 100000000000000000L )
                    {
                        if ( n == 2251799813685248L ) return 8L;
                        if ( n == 16677181699666569L ) return 9L;
                        return NOT_FOUND;
                    }
                    if ( n == 100000000000000000L ) return 10L;
                    return NOT_FOUND;
                }
                if ( n < 8650415919381337933L )
                {
                    if ( n == 505447028499293771L ) return 11L;
                    if ( n == 2218611106740436992L ) return 12L;
                    return NOT_FOUND;
                }
                if ( n == 8650415919381337933L ) return 13L;
                return NOT_FOUND;
            case 18:
                if ( n < 1628413597910449L )
                {
                    if ( n < 3814697265625L )
                    {
                        if ( n < 68719476736L )
                        {
                            if ( n == 262144L ) return 2L;
                            if ( n == 387420489L ) return 3L;
                            return NOT_FOUND;
                        }
                        if ( n == 68719476736L ) return 4L;
                        return NOT_FOUND;
                    }
                    if ( n == 3814697265625L ) return 5L;
                    if ( n == 101559956668416L ) return 6L;
                    return NOT_FOUND;
                }
                if ( n < 1000000000000000000L )
                {
                    if ( n < 150094635296999121L )
                    {
                        if ( n == 1628413597910449L ) return 7L;
                        if ( n == 18014398509481984L ) return 8L;
                        return NOT_FOUND;
                    }
                    if ( n == 150094635296999121L ) return 9L;
                    return NOT_FOUND;
                }
                if ( n == 1000000000000000000L ) return 10L;
                if ( n == 5559917313492231481L ) return 11L;
                return NOT_FOUND;
            case 19:
                if ( n < 609359740010496L )
                {
                    if ( n < 274877906944L )
                    {
                        if ( n == 524288L ) return 2L;
                        if ( n == 1162261467L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 274877906944L ) return 4L;
                    if ( n == 19073486328125L ) return 5L;
                    return NOT_FOUND;
                }
                if ( n < 144115188075855872L )
                {
                    if ( n == 609359740010496L ) return 6L;
                    if ( n == 11398895185373143L ) return 7L;
                    return NOT_FOUND;
                }
                if ( n == 144115188075855872L ) return 8L;
                if ( n == 1350851717672992089L ) return 9L;
                return NOT_FOUND;
            case 20:
                if ( n < 3656158440062976L )
                {
                    if ( n < 1099511627776L )
                    {
                        if ( n == 1048576L ) return 2L;
                        if ( n == 3486784401L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 1099511627776L ) return 4L;
                    if ( n == 95367431640625L ) return 5L;
                    return NOT_FOUND;
                }
                if ( n < 1152921504606846976L )
                {
                    if ( n == 3656158440062976L ) return 6L;
                    if ( n == 79792266297612001L ) return 7L;
                    return NOT_FOUND;
                }
                if ( n == 1152921504606846976L ) return 8L;
                return NOT_FOUND;
            case 21:
                if ( n < 476837158203125L )
                {
                    if ( n < 4398046511104L )
                    {
                        if ( n == 2097152L ) return 2L;
                        if ( n == 10460353203L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 4398046511104L ) return 4L;
                    return NOT_FOUND;
                }
                if ( n < 558545864083284007L )
                {
                    if ( n == 476837158203125L ) return 5L;
                    if ( n == 21936950640377856L ) return 6L;
                    return NOT_FOUND;
                }
                if ( n == 558545864083284007L ) return 7L;
                return NOT_FOUND;
            case 22:
                if ( n < 2384185791015625L )
                {
                    if ( n < 17592186044416L )
                    {
                        if ( n == 4194304L ) return 2L;
                        if ( n == 31381059609L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 17592186044416L ) return 4L;
                    return NOT_FOUND;
                }
                if ( n < 3909821048582988049L )
                {
                    if ( n == 2384185791015625L ) return 5L;
                    if ( n == 131621703842267136L ) return 6L;
                    return NOT_FOUND;
                }
                if ( n == 3909821048582988049L ) return 7L;
                return NOT_FOUND;
            case 23:
                if ( n < 11920928955078125L )
                {
                    if ( n < 70368744177664L )
                    {
                        if ( n == 8388608L ) return 2L;
                        if ( n == 94143178827L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 70368744177664L ) return 4L;
                    return NOT_FOUND;
                }
                if ( n == 11920928955078125L ) return 5L;
                if ( n == 789730223053602816L ) return 6L;
                return NOT_FOUND;
            case 24:
                if ( n < 59604644775390625L )
                {
                    if ( n < 281474976710656L )
                    {
                        if ( n == 16777216L ) return 2L;
                        if ( n == 282429536481L ) return 3L;
                        return NOT_FOUND;
                    }
                    if ( n == 281474976710656L ) return 4L;
                    return NOT_FOUND;
                }
                if ( n == 59604644775390625L ) return 5L;
                if ( n == 4738381338321616896L ) return 6L;
                return NOT_FOUND;
            case 25:
                if ( n < 1125899906842624L )
                {
                    if ( n == 33554432L ) return 2L;
                    if ( n == 847288609443L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 1125899906842624L ) return 4L;
                if ( n == 298023223876953125L ) return 5L;
                return NOT_FOUND;
            case 26:
                if ( n < 4503599627370496L )
                {
                    if ( n == 67108864L ) return 2L;
                    if ( n == 2541865828329L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 4503599627370496L ) return 4L;
                if ( n == 1490116119384765625L ) return 5L;
                return NOT_FOUND;
            case 27:
                if ( n < 18014398509481984L )
                {
                    if ( n == 134217728L ) return 2L;
                    if ( n == 7625597484987L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 18014398509481984L ) return 4L;
                if ( n == 7450580596923828125L ) return 5L;
                return NOT_FOUND;
            case 28:
                if ( n < 72057594037927936L )
                {
                    if ( n == 268435456L ) return 2L;
                    if ( n == 22876792454961L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 72057594037927936L ) return 4L;
                return NOT_FOUND;
            case 29:
                if ( n < 288230376151711744L )
                {
                    if ( n == 536870912L ) return 2L;
                    if ( n == 68630377364883L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 288230376151711744L ) return 4L;
                return NOT_FOUND;
            case 30:
                if ( n < 1152921504606846976L )
                {
                    if ( n == 1073741824L ) return 2L;
                    if ( n == 205891132094649L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 1152921504606846976L ) return 4L;
                return NOT_FOUND;
            case 31:
                if ( n < 4611686018427387904L )
                {
                    if ( n == 2147483648L ) return 2L;
                    if ( n == 617673396283947L ) return 3L;
                    return NOT_FOUND;
                }
                if ( n == 4611686018427387904L ) return 4L;
                return NOT_FOUND;
            case 32:
                if ( n == 4294967296L ) return 2L;
                if ( n == 1853020188851841L ) return 3L;
                return NOT_FOUND;
            case 33:
                if ( n == 8589934592L ) return 2L;
                if ( n == 5559060566555523L ) return 3L;
                return NOT_FOUND;
            case 34:
                if ( n == 17179869184L ) return 2L;
                if ( n == 16677181699666569L ) return 3L;
                return NOT_FOUND;
            case 35:
                if ( n == 34359738368L ) return 2L;
                if ( n == 50031545098999707L ) return 3L;
                return NOT_FOUND;
            case 36:
                if ( n == 68719476736L ) return 2L;
                if ( n == 150094635296999121L ) return 3L;
                return NOT_FOUND;
            case 37:
                if ( n == 137438953472L ) return 2L;
                if ( n == 450283905890997363L ) return 3L;
                return NOT_FOUND;
            case 38:
                if ( n == 274877906944L ) return 2L;
                if ( n == 1350851717672992089L ) return 3L;
                return NOT_FOUND;
            case 39:
                if ( n == 549755813888L ) return 2L;
                if ( n == 4052555153018976267L ) return 3L;
                return NOT_FOUND;
            case 40:
                return n == 1099511627776L ? 2L : NOT_FOUND;
            case 41:
                return n == 2199023255552L ? 2L : NOT_FOUND;
            case 42:
                return n == 4398046511104L ? 2L : NOT_FOUND;
            case 43:
                return n == 8796093022208L ? 2L : NOT_FOUND;
            case 44:
                return n == 17592186044416L ? 2L : NOT_FOUND;
            case 45:
                return n == 35184372088832L ? 2L : NOT_FOUND;
            case 46:
                return n == 70368744177664L ? 2L : NOT_FOUND;
            case 47:
                return n == 140737488355328L ? 2L : NOT_FOUND;
            case 48:
                return n == 281474976710656L ? 2L : NOT_FOUND;
            case 49:
                return n == 562949953421312L ? 2L : NOT_FOUND;
            case 50:
                return n == 1125899906842624L ? 2L : NOT_FOUND;
            case 51:
                return n == 2251799813685248L ? 2L : NOT_FOUND;
            case 52:
                return n == 4503599627370496L ? 2L : NOT_FOUND;
            case 53:
                return n == 9007199254740992L ? 2L : NOT_FOUND;
            case 54:
                return n == 18014398509481984L ? 2L : NOT_FOUND;
            case 55:
                return n == 36028797018963968L ? 2L : NOT_FOUND;
            case 56:
                return n == 72057594037927936L ? 2L : NOT_FOUND;
            case 57:
                return n == 144115188075855872L ? 2L : NOT_FOUND;
            case 58:
                return n == 288230376151711744L ? 2L : NOT_FOUND;
            case 59:
                return n == 576460752303423488L ? 2L : NOT_FOUND;
            case 60:
                return n == 1152921504606846976L ? 2L : NOT_FOUND;
            case 61:
                return n == 2305843009213693952L ? 2L : NOT_FOUND;
            case 62:
                return n == 4611686018427387904L ? 2L : NOT_FOUND;
            default:
                return NOT_FOUND;
        }
    }

    /**
     * Given integer number, find if there exist integer numbers s and q such that
     * number = s<sup>q</sup>, q &gt; 1. The value returned is a tuple ( s, q ).
     * The base s is minimal, the power q is maximal of all suitable tuples ( s, q ).
     * If such numbers s and q don't exist, null is returned.
     * <p>See also: http://en.wikipedia.org/wiki/Perfect_power<br>
     * The implementation is based on algorithms described by Daniel J. Bernstein in
     * "Detecting perfect powers in essentially linear time" (http://cr.yp.to/papers/powers.pdf).
     * <p>Special cases:<ul>
     * <li>number &gt; 1 =&gt; If number is a perfect power then s<sup>q</sup>
     * is returned, null otherwise. Numbers q and s are integers, q &gt; 1, s &gt; 1,
     * q is maximal, s is minimal of all suitable numbers.</li>
     * <li>number = 1 =&gt; 1<sup>2</sup> is returned</li>
     * <li>number = 0 =&gt; 0<sup>2</sup></li>
     * <li>number = -1 =&gt; (-1)<sup>3</sup></li>
     * <li>number &lt; -1 =&gt; If abs( number ) is a perfect power s<sup>q</sup>
     * and q = 2<sup>k</sup> * o, where o is odd, o &gt; 1, then (-s<sup>2^k</sup>)<sup>o</sup>
     * is returned, otherwise null is the answer.</li></ul>
     * <p>Examples:<br>
     * 64 = 2<sup>6</sup> = 4<sup>3</sup> = 8<sup>2</sup> - minimal base is 2
     * (maximal power is 6), so 2<sup>6</sup> is returned.<br>
     * -64 = (-4)<sup>3</sup> &ne; (-2)<sup>6</sup> = +64, so (-4)<sup>3</sup>
     * is returned.
     * @param number the value to be checked for being a perfect power
     * @return a tuple ( s, q ) such that s<sup>q</sup> = number, q &gt; 1,
     * or null if a given number is not a perfect power
     */
    public static long[] getBaseOfPerfectPower( long number )
    {
        if ( number <= 1L )
        {
            // 0 = 0^2, 1 = 1^2, -1 = (-1)^3.
            if ( number >= -1L ) return new long[]{ number, number < 0L? 3L : 2L };
            // Long.MIN_VALUE cannot be negated. Long.MIN_VALUE = -2^63 = (-2)^63.
            if ( number == Long.MIN_VALUE ) return new long[]{ -2L, 63L };
            // Negative number cannot be a perfect even power.
            // So the parity of power needs to be checked.
            long ret[] = getBaseOfPerfectPower( -number );
            if ( ret == null ) return null;
            int power = ( int )ret[ 1 ];
            int even = Integer.numberOfTrailingZeros( power );
            // Cut off even part.
            power >>= even;
            // The power is purely even. (-n)^(2k) is always positive.
            if ( power == 1 ) return null;
            even = 1 << even;
            // The answer is (-n^even)^power.
            return new long[]{ -pow( ret[ 0 ], even ), power };
        }
        // n = 2^k.
        // http://en.wikipedia.org/wiki/Power_of_two#Fast_algorithm_to_check_if_a_positive_number_is_a_power_of_two
        if ( ( number & ( number - 1L ) ) == 0L ) return number == 2L ? null : new long[]{ 2L, Long.numberOfTrailingZeros( number ) };
        // Factor out powers of two.
        int e2 = Long.numberOfTrailingZeros( number );
        long o = number >> e2;
        // And powers of three.
        int e3 = 0;
        while ( true )
        {
            long d = o / 3;
            if ( o != d * 3 ) break;
            o = d;
            e3++;
        }
        int gcd = gcd( e2, e3 );
        // n = 2^e2 * 3^e3 * o and gcd( e2, e3 ) = 1 => n is definitely not a perfect power.
        if ( gcd == 1 ) return null;
        // o = 1 => n consists only of multiplications of twos and threes.
        // n = ( 2^(e2/gcd) * 3^(e3/gcd) ) ^ gcd.
        if ( o == 1L ) return new long[]{ ( 1L << ( e2 / gcd ) ) * pow( 3L, e3 / gcd ), gcd };
        int ek = 1;
        long base = 0L;
        while ( true )
        {
            // Twos and threes are factored out. Minimum divisor now is five. So maximum power is log5( n ).
            // log5( n ) <= log4( n ) = log2( n ) / 2 <= bitLength( n ) / 2 = ( 32 - clz( n ) ) / 2.
            int log = 32 - ( Long.numberOfLeadingZeros( o ) >> 1 );
            if ( log < 2 ) log = 2;
            int ekc = 0;
            long basec = 0L;
            for ( int e : SMALL_PRIMES )
            {
                if ( e > log ) break;
                basec = getBaseOfPerfectPower( o, e );
                if ( basec != NOT_FOUND )
                {
                    ekc = e;
                    break;
                }
            }
            if ( ekc == 0 ) break;
            ek *= ekc;
            base = basec;
            o = basec;
        }
        if ( ek == 1 ) return null;
        // n = 2^e2 * 3^e3 * base^ek.
        gcd = gcd( gcd, ek );
        if ( gcd == 1 ) return null;
        // n = ( 2^(e2/gcd) * 3^(e3/gcd) * base^(ek/gcd) ) ^ gcd.
        return new long[]{ ( 1L << ( e2 / gcd ) ) * pow( 3L, e3 / gcd ) * pow( base, ek / gcd ), gcd };
    }
    
    /**
     * Determine if a given number n is a perfect power
     * (if there exist integer numbers s and q such that n = s<sup>q</sup> and q &gt; 1).<br>
     * http://en.wikipedia.org/wiki/Perfect_power
     * @param n a number to be checked
     * @return true if and only if n is a perfect power
     */
    public static boolean isPerfectPower( long n )
    {
        return getBaseOfPerfectPower( n ) != null;
    }
    
    /**
     * Returns the unsigned remainder from dividing the first argument
     * by the second where each argument and the result is interpreted
     * as an unsigned value.
     * <p>This method doesn't use long datatype unlike it's used in
     * {@link java.lang.Integer#remainderUnsigned}.
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned remainder of the first argument divided by
     * the second argument
     * @see java.lang.Integer#remainderUnsigned
     */
    public static int remainderUnsigned( int dividend, int divisor )
    {
        if ( divisor >= 0 )
        {
            if ( dividend >= 0 ) return dividend % divisor;
            // The implementation is a Java port of algorithm described in the book
            // "Hacker's Delight" (section "Unsigned short division from signed division").
            int q = ( ( dividend >>> 1 ) / divisor ) << 1;
            dividend -= q * divisor;
            if ( Integer.compareUnsigned( dividend, divisor ) >= 0 ) dividend -= divisor;
            return dividend;
        }
        return dividend >= 0 || dividend < divisor ? dividend : dividend - divisor;
    }
    
    /**
     * Returns the unsigned remainder from dividing the first argument
     * by the second where each argument and the result is interpreted
     * as an unsigned value.
     * <p>This method doesn't use BigInteger datatype unlike it's used in
     * {@link java.lang.Long#remainderUnsigned}.
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned remainder of the first argument divided by
     * the second argument
     * @see java.lang.Long#remainderUnsigned
     */
    public static long remainderUnsigned( long dividend, long divisor )
    {
        if ( divisor >= 0L )
        {
            if ( dividend >= 0L ) return dividend % divisor;
            // The implementation is a Java port of algorithm described in the book
            // "Hacker's Delight" (section "Unsigned short division from signed division").
            long q = ( ( dividend >>> 1 ) / divisor ) << 1;
            dividend -= q * divisor;
            if ( dividend < 0L || dividend >= divisor ) dividend -= divisor;
            return dividend;
        }
        return dividend >= 0L || dividend < divisor ? dividend : dividend - divisor;
    }
    
    /**
     * Returns the unsigned quotient of dividing the first argument by
     * the second where each argument and the result is interpreted as
     * an unsigned value.
     * <p>Note that in two's complement arithmetic, the three other
     * basic arithmetic operations of add, subtract, and multiply are
     * bit-wise identical if the two operands are regarded as both
     * being signed or both being unsigned. Therefore separate {@code
     * addUnsigned}, etc. methods are not provided.
     * <p>This method doesn't use long datatype unlike it's used in
     * {@link java.lang.Integer#divideUnsigned}.
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned quotient of the first argument divided by
     * the second argument
     * @see java.lang.Integer#divideUnsigned
     */
    public static int divideUnsigned( int dividend, int divisor )
    {
        if ( divisor >= 0 )
        {
            if ( dividend >= 0 ) return dividend / divisor;
            // The implementation is a Java port of algorithm described in the book
            // "Hacker's Delight" (section "Unsigned short division from signed division").
            int q = ( ( dividend >>> 1 ) / divisor ) << 1;
            if ( Integer.compareUnsigned( dividend - q * divisor, divisor ) >= 0 ) q++;
            return q;
        }
        return dividend >= 0 || dividend < divisor ? 0 : 1;
    }
    
    /**
     * Returns the unsigned quotient of dividing the first argument by
     * the second where each argument and the result is interpreted as
     * an unsigned value.
     * <p>Note that in two's complement arithmetic, the three other
     * basic arithmetic operations of add, subtract, and multiply are
     * bit-wise identical if the two operands are regarded as both
     * being signed or both being unsigned. Therefore separate {@code
     * addUnsigned}, etc. methods are not provided.
     * <p>This method doesn't use BigInteger datatype unlike it's used in
     * {@link java.lang.Long#divideUnsigned}.
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned quotient of the first argument divided by
     * the second argument
     * @see java.lang.Long#divideUnsigned
     */
    public static long divideUnsigned( long dividend, long divisor )
    {
        if ( divisor >= 0L )
        {
            if ( dividend >= 0L ) return dividend / divisor;
            // The implementation is a Java port of algorithm described in the book
            // "Hacker's Delight" (section "Unsigned short division from signed division").
            long q = ( ( dividend >>> 1 ) / divisor ) << 1;
            dividend -= q * divisor;
            if ( dividend < 0L || dividend >= divisor ) q++;
            return q;
        }
        return dividend >= 0L || dividend < divisor ? 0L : 1L;
    }
}