package com.ubs.accumulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class StringCalculatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void addEmptyStringTo0() {
        assertThat(StringCalculator.add(""), is(0));
    }

    @Test
    public void addSingleNumberToItself() {
        assertThat(StringCalculator.add("5"), is(5));
        assertThat(StringCalculator.add("02"), is(2));
    }

    @Test
    public void addTwoNumbersSeparatedByComma() {
        assertThat(StringCalculator.add("1,2"), is(3));
        assertThat(StringCalculator.add("10,20"), is(30));
    }

    @Test
    public void addThreeNumbersSeparatedByComma() {
        assertThat(StringCalculator.add("1,2,3"), is(6));
    }

    @Test
    public void addNumbersDelimitedByNewline() {
        assertThat(StringCalculator.add("1\n2"), is(3));
    }

    @Test
    public void addNumbersDelimitedByCommaOrNewline() {
        assertThat(StringCalculator.add("1,2\n3"), is(6));
    }

    @Test
    public void addNumberWithMixedDelimiters() {
        assertThat(StringCalculator.add("//;\n1;2"), is(3));
        assertThat(StringCalculator.add("//.\n2.3.1"), is(6));
    }

    @Test
    public void throwExceptionOnNegativeNumber() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Negative number(s): -20");

        StringCalculator.add("-20");
    }

    @Test
    public void throwExceptionForNegativeNumbersWithAllNumbersInMessage() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Negative number(s): -1,-12,-8");

        StringCalculator.add("-1,7,-12,-8");
    }

    @Test
    public void ignoreNumbersGreaterThan1000AndSum() {
        assertThat(StringCalculator.add("2,1001"), is(2));
        assertThat(StringCalculator.add("200,10001"), is(200));
        assertThat(StringCalculator.add("1000,9999"), is(1000));
        assertThat(StringCalculator.add("0100,9999"), is(100));
    }

    @Test
    public void addNumbersWithDelimiterOfDifferentLength() {
        assertThat(StringCalculator.add("//[$$$]\n1$$$2$$$3"), is(6));
        assertThat(StringCalculator.add("//[##]\n01##02##03"), is(6));
        assertThat(StringCalculator.add("//[***]\n1000***200***30000"), is(1200));
    }

    @Test
    public void addWithMultipleDelimiters() {
        assertThat(StringCalculator.add("//[-][;]\n1-2;3"), is(6));
        assertThat(StringCalculator.add("//[--][...]\n4--10...11"), is(25));
    }
}