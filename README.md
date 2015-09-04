# JBroMath
Math utilities for Java.

## MathUtils

- Test coverage: 100%
- Javadoc coverage: 100%

### Conversion functions

**toArabicNumerals** - converts string representation of Roman numerals to decimal value.
There are three variants of this function:
```java
int toArabicNumeralsInt( String )
```
A classical one, supports N for zero. Converts values to int in range from 0 to 3999 (inclusively). Performs input validation.
```java
double toArabicNumeralsDouble( String )
```
Supports N for zero, S for a half and minus sign for negative values. Converts values to double. Does no verification. So any big value is acceptable (e.g., MMMMMMXI).
```java
int toArabicNumeralsExcelInt( String )
```
A variant identical to Microsoft Excel's ARABIC( text ) function. Supports minus sign. Accepts strings up to 255 characters (inclusively).

**toRomanNumerals** - converts a decimal number to its string representation as Roman numerals.
There are also three variants of this function:
```java
String toRomanNumeralsString( int number, boolean shortest )
```
A classical one. Permits zero (the result would be "N" - nulla). The value of "number" should lie in range [ 0 .. 3999 ]. If the argument "shortest" is set to true then the shortest possible output is generated (but it won't be a classical one). Example: toRomanNumeralsString( 49, false ) = "XLIX" - correct classical way; toRomanNumeralsString( 49, true ) = "IL" - it means L - I = 50 - 1 = 49 according to Roman-to-Arabic conversion rules. And this is the shortest possible way to write 49 in Roman numerals. A reversed conversion can be done by toArabicNumeralsExcelInt function. Microsoft Excel would accept "IL" correctly too.
```java
String toRomanNumeralsString( double number, boolean shortest )
```
The same as a previous one but accepts half values also (which turn into "S" - semis).
```java
String toRomanNumeralsExcelString( int number, int mode )
```
A variant identical to Microsoft Excel's ROMAN( number, mode ) function. The mode values are:
0. Classic form of Roman numerals: I, V, X, L, C, D, M and subtractive sequences CM, CD, XC, XL, IX, IV are allowed.
1. More concise: LM, LD, VC, VL are also allowed.
2. More concise: XM, XD, IC, IL.
3. More concise: VM, VD.
4. Minimal (according to Excel's terms): IM, ID.

Note that the mode 4 doesn't produce absolutely minimal representation of all the numbers. Use toRomanNumeralsString with "shortest" set to true if the absolutely minimal form is required. Example: ROMAN( 78; 4 ) = "LXXVIII" but minimal is "IIXXC". And ARABIC function correctly accepts this input.

Released into public domain.