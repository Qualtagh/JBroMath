# JBroMath
Math utilities for Java.

Released into public domain.

Contents:

1. [MathUtils](#mathutils)
  1. [Conversion functions](#conversion-functions)
    1. [toArabicNumerals](#toarabicnumerals)
    1. [toRomanNumerals](#toromannumerals)
    1. [toBigDecimal](#tobigdecimal)
    1. [toUnsignedBigInteger](#tounsignedbiginteger)
    1. [toPlainString](#toplainstring)
    1. [toRoundedString](#toroundedstring)
  1. [Rounding](#rounding)
    1. [toRoundedString](#toroundedstring)
    1. [round](#round)
    1. [trunc](#trunc)
    1. [ceil](#ceil)
    1. [floor](#floor)
  1. [Exponentiation](#exponentiation)
    1. [pow10](#pow10)
    1. [pow](#pow)
    1. [powExact](#powExact)
  1. [Modular arithmetics](#modular-arithmetics)
    1. [mod](#mod)
    1. [mods](#mods)
    1. [modAdd](#modAdd)
    1. [modSubtract](#modSubtract)
    1. [modMultiply](#modMultiply)
    1. [modDivide](#modDivide)
    1. [modInverse](#modInverse)
    1. [modPow](#modPow)
  1. [Common divisors and multiples](#common-divisors-and-multiples)
    1. [egcd](#egcd)
    1. [gcd](#gcd)
    1. [lcm](#lcm)
    1. [lcmExact](#lcmExact)
  1. [Roots](#roots)
    1. [isqrt](#isqrt)
    1. [uisqrt](#uisqrt)
    1. [icbrt](#icbrt)
    1. [iroot](#iroot)
  1. [Perfect powers](#perfect-powers)
    1. [getBaseOfPerfectSquare](#getBaseOfPerfectSquare)
    1. [isPerfectSquare](#isPerfectSquare)
    1. [getBaseOfPerfectCube](#getBaseOfPerfectCube)
    1. [isPerfectCube](#isPerfectCube)
    1. [getBaseOfPerfectPower](#getBaseOfPerfectPower)
    1. [isPerfectPower](#isPerfectPower)
  1. [Unsigned arithmetic](#unsigned-arithmetic)
    1. [uisqrt](#uisqrt)
    1. [remainderUnsigned](#remainderUnsigned)
    1. [divideUnsigned](#divideUnsigned)
1. [PrimeUtils](#primeutils)

## MathUtils

This class contains functions with primitive-type (byte, short, int, long, float, double) arguments.

- Test coverage: 100%
- Javadoc coverage: 100%
- `Integer/Long.MIN_VALUE/MAX_VALUE` are handled properly.

### Conversion functions

<a name="toarabicnumerals"></a>
**toArabicNumerals** - converts string representation of Roman numerals to decimal value.
There are three variants of this function:
```java
int toArabicNumeralsInt( String ) throws IllegalArgumentException
```
A classic one, supports `N` ([nulla](https://en.wikipedia.org/wiki/Roman_numerals#Zero)) for zero. Converts values to int in range from 0 to 3999 (inclusively). Performs input validation. E.g., inputs like `IIII`, `IIX` or `MMMMMXX` are considered incorrect.

Throws `IllegalArgumentException` if the input is null, empty or not a valid Roman number.
```java
double toArabicNumeralsDouble( String ) throws IllegalArgumentException
```
Supports `N` for zero, `S` ([semis](https://en.wikipedia.org/wiki/Roman_numerals#Fractions)) for a half and minus sign for negative values. Converts values to double. Does not check the order of letters nor the length of the input. So any big value is permitted (e.g., `MMMMMMXI`). Incorrect letters are verified though.

Throws `IllegalArgumentException` if the input contains incorrect characters. If the input is null or empty then 0.0 is returned.
```java
int toArabicNumeralsExcelInt( String )
```
A variant identical to Microsoft Excel's `ARABIC( text )` function. Supports minus sign. Accepts strings up to 255 characters (inclusively).

Throws `IllegalArgumentException` if the input contains incorrect characters. If the input is null or empty then 0 is returned.
___
<a name="toromannumerals"></a>
**toRomanNumerals** - converts a decimal number to its string representation as Roman numerals.
There are also three variants of this function:
```java
String toRomanNumeralsString( int number, boolean shortest ) throws NumberFormatException
```
A classic one. Permits zero (the result would be `N` - nulla). The value of `number` should lie in range [ 0 .. 3999 ]. If the argument `shortest` is set to true then the shortest possible output is generated (but it won't be a classic one). Example:

`toRomanNumeralsString( 49, false )` = `XLIX` - correct classic way;

`toRomanNumeralsString( 49, true )` = `IL` - it means `L - I = 50 - 1 = 49` according to Roman-to-Arabic conversion rules. And this is the shortest possible way to write `49` in Roman numerals. A reversed conversion can be done by `toArabicNumeralsExcelInt` function. Microsoft Excel would accept `IL` correctly too.

Throws `NumberFormatException` if number < 0 or number &ge; 4000.
```java
String toRomanNumeralsString( double number, boolean shortest ) throws NumberFormatException
```
The same as a previous one but accepts half values also (which turn into `S` - semis).

Throws `NumberFormatException` if number &le; -1000000 or number &ge; 1000000.
```java
String toRomanNumeralsExcelString( int number, int mode ) throws NumberFormatException, IllegalArgumentException
```
A variant identical to Microsoft Excel's `ROMAN( number, mode )` function. The mode values are:

- 0 - Classic form of Roman numerals: `I, V, X, L, C, D, M` and subtractive sequences `CM, CD, XC, XL, IX, IV` are allowed.
- 1 - More concise: `LM, LD, VC, VL` are also allowed.
- 2 - More concise: `XM, XD, IC, IL`.
- 3 - More concise: `VM, VD`.
- 4 - Minimal (according to Excel's terms): `IM, ID`.

Note that the mode **4** doesn't produce absolutely minimal representation of all the numbers. Use `toRomanNumeralsString` with `shortest` set to true if the absolutely minimal form is required. Example: `ROMAN( 78; 4 ) = LXXVIII` but minimal is `IIXXC`. And `ARABIC` function correctly accepts both of these input strings.

Throws `NumberFormatException` if number < 0 or number &ge; 4000.

Throws `IllegalArgumentException` if mode < 0 or mode > 4.
___
<a name="tobigdecimal"></a>
```java
BigDecimal toBigDecimal( Number ) throws NumberFormatException
```
Converts a number to BigDecimal.

- If the number is double or float then decimal (not binary) representation is used.
- If the number class is not one of standard Java number classes then conversion is made via `doubleValue()` call.
- If the number is not finite (infinity or NaN) then NumberFormatException is thrown.

___
<a name="tounsignedbiginteger"></a>
**toUnsignedBigInteger** - converts an unsigned number to BigInteger. There are two versions of this method:
```java
BigInteger toUnsignedBigInteger( int )
BigInteger toUnsignedBigInteger( long )
```
One for int argument and one for long.
___
<a name="toplainstring"></a>
**toPlainString** - converts a floating-point number to its string representation without an exponent field.
```java
String toPlainString( double )
String toPlainString( float )
```

- NaN, Infinity and -Infinity produce "NaN", "Infinity" and "-Infinity" respectively.
- When the number is integer, the fractional part is omitted (1.0 would result in "1", not in "1.0").
- Positive and negative zeros produce "0" and "-0" respectively.
- Fractional part is represented without an exponent field (0.00000001 would result in "0.00000001", not in "1e-8").

___
<a name="toroundedstring"></a>
```java
String toRoundedString( Number number, int precision )
```
Rounds a number to `precision` decimal places and returns a plain string representation (without an exponent field) of the resulting number.

- NaN, Infinity and -Infinity produce "NaN", "Infinity" and "-Infinity" respectively.
- Both positive and negative zero produce "0" (if a negative number becomes -0.0 after rounding, it would result in "0" too).
- If number is negative then the output equals to `"-" + toRoundedString( -number )`.
- Round half up method is used for rounding positive numbers.
- Negative precision is supported (5123.6 with precision -2 outputs "5100").

___
### Rounding

**[toRoundedString](#toroundedstring)** - rounds a number to specified quantity of decimal places and returns a plain string representation of the result.
___
<a name="round"></a>
```java
double round( double value, int decimals )
```
Symmetric [rounding half away from zero](http://en.wikipedia.org/wiki/Rounding#Round_half_away_from_zero) to `decimals` places to the right of the decimal point.

This method differs from `Math.round( double )` for negative numbers, because the latter uses asymmetric [rounding half up](http://en.wikipedia.org/wiki/Rounding#Round_half_up).
```java
round( 12.7, 0 ) = 13.0
round( -12.25, 1 ) = -12.3
```
___
<a name="trunc"></a>
```java
double trunc( double value, int decimals )
```
Rounding towards zero ([truncating](http://en.wikipedia.org/wiki/Truncation)) to `decimals` places to the right of the decimal point.
```java
trunc( 12.7, 0 ) = 12.0
trunc( -12.25, 1 ) = -12.2
```
___
<a name="ceil"></a>
```java
double ceil( double value, int decimals )
```
Rounding up ([ceiling](http://en.wikipedia.org/wiki/Floor_and_ceiling_functions)) to `decimals` places to the right of the decimal point.
```java
ceil( 12.7, 0 ) = 13.0
ceil( -12.25, 1 ) = -12.2
```
___
<a name="floor"></a>
```java
double floor( double value, int decimals )
```
Rounding down ([flooring](http://en.wikipedia.org/wiki/Floor_and_ceiling_functions)) to `decimals` places to the right of the decimal point.
```java
floor( 12.7, 0 ) = 12.0
floor( -12.25, 1 ) = -12.3
```
___
### Exponentiation

<a name="pow10"></a>
```java
double pow10( int y )
```
Returns 10 raised to power `y` (10<sup>y</sup>). More info here: [JDK-4358794: Math package: implement pow10 (power of 10) with optimization for integer powers](http://bugs.sun.com/view_bug.do?bug_id=4358794). This method is more precise than `Math.pow` when the base is a power of 10.
___
<a name="pow"></a>
**pow** - returns `x` raised to power `y` (x<sup>y</sup>). There are three variations:
```java
double pow( double x, int y )
```
Overload of `Math.pow( double x, double y )` method for integer exponent. Returns NaN and infinity in the same situations as `Math.pow` does.
```java
long pow( long x, int y ) throws ArithmeticException
int pow( int x, int y ) throws ArithmeticException
```
Purely integer exponentiation. These two methods throw exception if `x = 0` and `y < 0` (resulting in infinity). May overflow. Use `powExact` if overflow check is needed.
___
<a name="powExact"></a>
```java
long powExact( long x, int y ) throws ArithmeticException
int powExact( int x, int y ) throws ArithmeticException
```
Returns value of `x` raised in power `y` (x<sup>y</sup>), throwing an exception if the result overflows long (or int respectively). Analogous to `Math.multiplyExact` and other `Math.<...>Exact` methods.

Also, throws ArithmeticException if `x = 0` and `y < 0` (the result becomes infinite).
___
### Modular arithmetics

<a name="mod"></a>
```java
int mod( int v, int m )
long mod( long v, long m )
```
Returns `v (mod m)`. The value returned lies in range `[ 0 .. |m| - 1 ]`.

This method differs from `v % m` in that it always returns non-negative value.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
<a name="mods"></a>
```java
int mods( int v, int m )
long mods( long v, long m )
```
Signed mod. The value returned lies in range `[ -( |m| - 1 ) / 2 .. |m| / 2 ]`.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
<a name="modAdd"></a>
```java
int modAdd( long a, long b, int m )
long modAdd( long a, long b, long m )
```
Modular addition. Returns `( a + b )( mod m )`.

Differs from `( a + b ) % m` in that it always returns non-negative value and never overflows.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
<a name="modSubtract"></a>
```java
int modSubtract( long a, long b, int m )
long modSubtract( long a, long b, long m )
```
Modular subtraction. Returns `( a - b )( mod m )`.

Differs from `( a - b ) % m` in that it always returns non-negative value and never overflows.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
<a name="modMultiply"></a>
```java
int modMultiply( long a, long b, int m )
long modMultiply( long a, long b, long m )
```
Modular multiplication. Returns `( a * b )( mod m )`.

Differs from `( a * b ) % m` in that it always returns non-negative value and never overflows.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
<a name="modDivide"></a>
```java
int[] modDivide( int a, int b, int m )
long[] modDivide( long a, long b, long m )
```
Modular division. Returns a solution of equation `( x * b )( mod m ) = a`.

The solution has the form: x = x<sub>0</sub> + increment * k, where

- 0 &le; k < gcd, k is integer
- increment = m / gcd
- x<sub>0</sub> = a / gcd * modInverse( b, m )
- gcd = gcd( b, m )

If `a % gcd != 0` then the equation cannot be solved.

The value returned is a tuple ( x<sub>0</sub>, increment, quantity ), where `quantity` is the quantity of solutions, it equals to `gcd`. If there's no solution, a tuple ( MathUtils.NOT_FOUND, 0, 0 ) is returned.
___
<a name="modInverse"></a>
```java
int modInverse( int a, int m )
long modInverse( long a, long m )
```
[Modular multiplicative inverse](http://en.wikipedia.org/wiki/Modular_multiplicative_inverse). Analogous to `BigInteger.modInverse`.

Returns s = a<sup>-1</sup> (mod m), `s` is such a number that `(s * a) (mod m) = 1`.

If such number `s` doesn't exist, `MathUtils.NOT_FOUND` is returned (if `a` and `m` are not coprime).

If `m = 0` then `MathUtils.NOT_FOUND` is returned.

The result is always non-negative if it exists.
___
<a name="modPow"></a>
```java
int modPow( long base, long exponent, int m )
long modPow( long base, long exponent, long m )
```
Raise `base` to `exponent` power mod `m`. Returns base<sup>exponent</sup> (mod m).

If `exponent < 0` and `base` is not relatively prime to `m` then `MathUtils.NOT_FOUND` is returned.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
___
### Common divisors and multiples

<a name="egcd"></a>
```java
int[] egcd( int a, int b )
long[] egcd( long a, long b )
```
[Extended](http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm) Euclidean greatest common divisor (GCD) algorithm function.

A tuple `( gcd, x, y )` is returned, where `gcd` is greatest common divisor of `a` and `b`.

`x` and `y` are integers such that `x * a + y * b = gcd`.
___
<a name="gcd"></a>
```java
int gcd( int a, int b )
long gcd( long a, long b )
```
Greatest common divisor ([GCD](http://en.wikipedia.org/wiki/Greatest_common_divisor)).
___
<a name="lcm"></a>
```java
int lcm( int a, int b )
long lcm( long a, long b )
```
Least common multiple ([LCM](http://en.wikipedia.org/wiki/Least_common_multiple)). May overflow. Use `lcmExact` if overflow check is needed.
___
<a name="lcmExact"></a>
```java
int lcmExact( int a, int b ) throws ArithmeticException
long lcmExact( long a, long b ) throws ArithmeticException
```
Least common multiple with overflow check. Analogous to `Math.multiplyExact` and other `Math.<...>Exact` methods.

Throws `ArithmeticException` if the result overflows int (or long respectively).
___
### Roots

<a name="isqrt"></a>
```java
long isqrt( long n ) throws ArithmeticException
int isqrt( int n ) throws ArithmeticException
```
Returns [integer square root](http://en.wikipedia.org/wiki/Integer_square_root) of n.

The greatest integer less than or equal to the square root of n. In other words, `trunc( sqrt( n ) )`. Example:

    isqrt( 27 ) = 5 because 5 * 5 = 25 &le; 27 and 6 * 6 = 36 > 27

Throws exception if `n < 0`.
___
<a name="uisqrt"></a>
```java
int uisqrt( long n )
int uisqrt( int n )
```
Unsigned integer square root. The argument and the returned value are treated as unsigned.
___
<a name="icbrt"></a>
```java
long icbrt( long n )
int icbrt( int n )
```
Integer cubic root. Returns `trunc( cbrt( n ) )`.
___
<a name="iroot"></a>
```java
long iroot( long n, int power ) throws ArithmeticException
int iroot( int n, int power ) throws ArithmeticException
```
Returns integer root of `n` of given degree `power`. In other words, returns trunc( <sup>power</sup>&radic;n ).

Throws exception if one of the following conditions holds:

- n < 0 and power is even (the result is a complex number)
- n = 0 and power < 0 (resulting in infinity)
- n = 1 and power = 0 (the result is 1<sup>&infin;</sup>, which is undefined)
- n > 1 and power = 0 (the result is n<sup>&infin;</sup> = &infin;)

___
### Perfect powers

<a name="getBaseOfPerfectSquare"></a>
```java
long getBaseOfPerfectSquare( long n )
```
Finds a root of a [perfect square](http://en.wikipedia.org/wiki/Square_number).

Given integer number `n`, find integer number `s` such that s<sup>2</sup> = n.

Returns `MathUtils.NOT_FOUND` if such number `s` doesn't exists.
___
<a name="isPerfectSquare"></a>
```java
boolean isPerfectSquare( long n )
```
Determine if a given number `n` is a perfect square.
___
<a name="getBaseOfPerfectCube"></a>
```java
long getBaseOfPerfectCube( long n )
```
Given integer number `n`, find integer number `s` such that s<sup>3</sup> = n.

Returns `MathUtils.NOT_FOUND` if such number `s` doesn't exists.
___
<a name="isPerfectCube"></a>
```java
boolean isPerfectCube( long n )
```
Determine if a given number `n` is a perfect cube.
___
<a name="getBaseOfPerfectPower"></a>
**getBaseOfPerfectPower** - finds a root of a [perfect power](http://en.wikipedia.org/wiki/Perfect_power).

```java
long getBaseOfPerfectPower( long n, int power )
```
Given integer numbers `n` and `power`, find integer number `s` such that s<sup>power</sup> = n.

Returns `MathUtils.NOT_FOUND` if such number `s` doesn't exists.

```java
long[] getBaseOfPerfectPower( long number )
```
Given integer number, find if there exist integer numbers `s` and `q` such that number = s<sup>q</sup>, `q > 1`.

The value returned is a tuple `( s, q )`. The base `s` is minimal, the power `q` is maximal of all suitable tuples `( s, q )`.

If such numbers `s` and `q` don't exist, `null` is returned.
___
<a name="getBaseOfPerfectPower"></a>
**isPerfectPower** - determine if a given number `n` is a perfect power.

```java
boolean isPerfectPower( long n, int power )
```
This method checks `n` against only one given power.
```java
boolean isPerfectPower( long n )
```
This method checks if there exist such numbers `s` and `q` that n = s<sup>q</sup>, `q > 1`. In other words, if `n` is a perfect power of any power `q > 1`.
___
### Unsigned arithmetic

**[uisqrt](#uisqrt)** - unsigned integer square root.
___
<a name="remainderUnsigned"></a>
```java
int remainderUnsigned( int dividend, int divisor )
long remainderUnsigned( long dividend, long divisor )
```
Returns the unsigned remainder from dividing the first argument by the second where each argument and the result is interpreted as an unsigned value.

This method is an optimized version of `Integer/Long.remainderUnsigned`.
___
<a name="divideUnsigned"></a>
```java
int divideUnsigned( int dividend, int divisor )
long divideUnsigned( long dividend, long divisor )
```
Returns the unsigned quotient from dividing the first argument by the second where each argument and the result is interpreted as an unsigned value.

This method is an optimized version of `Integer/Long.divideUnsigned`.