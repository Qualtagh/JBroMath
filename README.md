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
1. [PrimeUtils](#primeutils)

## MathUtils

- Test coverage: 100%
- Javadoc coverage: 100%

### Conversion functions

<a name="toarabicnumerals"></a>
**toArabicNumerals** - converts string representation of Roman numerals to decimal value.
There are three variants of this function:
```java
int toArabicNumeralsInt( String )
```
A classic one, supports `N` ([nulla](https://en.wikipedia.org/wiki/Roman_numerals#Zero)) for zero. Converts values to int in range from 0 to 3999 (inclusively). Performs input validation. E.g., inputs like `IIII`, `IIX` or `MMMMMXX` are considered incorrect.
```java
double toArabicNumeralsDouble( String )
```
Supports `N` for zero, `S` ([semis](https://en.wikipedia.org/wiki/Roman_numerals#Fractions)) for a half and minus sign for negative values. Converts values to double. Does not check the order of letters nor the length of the input. So any big value is permitted (e.g., `MMMMMMXI`). Incorrect letters are verified though
```java
int toArabicNumeralsExcelInt( String )
```
A variant identical to Microsoft Excel's `ARABIC( text )` function. Supports minus sign. Accepts strings up to 255 characters (inclusively).
___
<a name="toromannumerals"></a>
**toRomanNumerals** - converts a decimal number to its string representation as Roman numerals.
There are also three variants of this function:
```java
String toRomanNumeralsString( int number, boolean shortest )
```
A classic one. Permits zero (the result would be `N` - nulla). The value of `number` should lie in range [ 0 .. 3999 ]. If the argument `shortest` is set to true then the shortest possible output is generated (but it won't be a classic one). Example:

`toRomanNumeralsString( 49, false )` = `XLIX` - correct classic way;

`toRomanNumeralsString( 49, true )` = `IL` - it means `L - I = 50 - 1 = 49` according to Roman-to-Arabic conversion rules. And this is the shortest possible way to write `49` in Roman numerals. A reversed conversion can be done by `toArabicNumeralsExcelInt` function. Microsoft Excel would accept `IL` correctly too.
```java
String toRomanNumeralsString( double number, boolean shortest )
```
The same as a previous one but accepts half values also (which turn into `S` - semis).
```java
String toRomanNumeralsExcelString( int number, int mode )
```
A variant identical to Microsoft Excel's `ROMAN( number, mode )` function. The mode values are:

- 0 - Classic form of Roman numerals: `I, V, X, L, C, D, M` and subtractive sequences `CM, CD, XC, XL, IX, IV` are allowed.
- 1 - More concise: `LM, LD, VC, VL` are also allowed.
- 2 - More concise: `XM, XD, IC, IL`.
- 3 - More concise: `VM, VD`.
- 4 - Minimal (according to Excel's terms): `IM, ID`.

Note that the mode **4** doesn't produce absolutely minimal representation of all the numbers. Use `toRomanNumeralsString` with `shortest` set to true if the absolutely minimal form is required. Example: `ROMAN( 78; 4 ) = LXXVIII` but minimal is `IIXXC`. And `ARABIC` function correctly accepts both of these input strings.
___
<a name="tobigdecimal"></a>
```java
BigDecimal toBigDecimal( Number )
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

**[toRoundedString](#toroundedstring)** - Rounds a number to specified quantity of decimal places and returns a plain string representation of the result.
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
Overload of `Math.pow( double x, double y )` method for integer exponent.
```java
long pow( long x, int y ) throws ArithmeticException
int pow( int x, int y ) throws ArithmeticException
```
These two methods throw exception if `x = 0` and `y < 0` (resulting in infinity).
___
<a name="powExact"></a>
```java
long powExact( long x, int y ) throws ArithmeticException
int powExact( int x, int y ) throws ArithmeticException
```
Returns value of `x` raised in power `y` (x<sup>y</sup>), throwing an exception if the result overflows long (or int respectively). Analogous to `Math.multiplyExact` and other `Math.<...>Exact` methods.

Also throws ArithmeticException if `x = 0` and `y < 0` (the result becomes infinite).
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
Raise `base` to `exponent` power mod `m`. Returns `base<sup>exponent</sup> (mod m)`.

If `exponent < 0` and `base` is not relatively prime to `m` then `MathUtils.NOT_FOUND` is returned.

If `m = 0` then `MathUtils.NOT_FOUND` is returned.
