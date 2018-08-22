package com.ubs.accumulator.test;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringCalculator {

    private static final String DOUBLE_BACKSLASH_PREFIX = "//";
    private static final String NEW_LINE = "\n";
    private static final String PIPE_DELIMITER = "|";
    private static final String REGEX_DELIMITER = "]\\[";
    private static final String COMMA_DELIMITER = ",";
    public static final String WITH_PIPE_DELIMITER = ",|\n";
    private static final int DEFAULT_LIMIT = 2;
    private static final int MAX_ALLOWED_NUMBER = 1000;

    private String delimiter;
    private String numberSequence;

    private StringCalculator(String delimiter, String numberSequence) {
        this.delimiter = delimiter;
        this.numberSequence = numberSequence;
    }

    public static int add(String numbers) {
        return parseInput(numbers).sum();
    }

    //Parsing the input parameters to separate  delimiters and  number sequence and store to the respective fields.
    private static StringCalculator parseInput(String numbers) {
        if (numbers.startsWith(DOUBLE_BACKSLASH_PREFIX)) {
            String[] headerAndNumberSequence = numbers.split(NEW_LINE, DEFAULT_LIMIT);
            String delimiter = parseDelimiter(headerAndNumberSequence[0]);
            return new StringCalculator(delimiter, headerAndNumberSequence[1]);
        } else {
            return new StringCalculator(WITH_PIPE_DELIMITER, numbers);
        }
    }

    // To parse the delimiter in case of the delimiter char is gt than 1
    private static String parseDelimiter(String header) {
        String delimiter = header.substring(2);
        if (delimiter.startsWith("[")) {
            delimiter = delimiter.substring(1, delimiter.length() - 1);
        }
        return Stream.of(delimiter.split(REGEX_DELIMITER))
                .map(Pattern::quote)
                .collect(Collectors.joining(PIPE_DELIMITER));
    }

    private int sum() {
        checkForNegativeNumbers();
        return getNumberSequence().sum();
    }

    private void checkForNegativeNumbers() {
        // Converting the numbers to Integer and checking if any of the number is negative number.
        String negativeNumberSequence = getNumberSequence().filter(n -> n < 0)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(COMMA_DELIMITER));

        //If found the negative numbers then there will be an exception.
        if (!negativeNumberSequence.isEmpty()) {
            throw new IllegalArgumentException(String.format("Negative number(s): %s", negativeNumberSequence));
        }
    }

    /* Utility method to convert the number sequence to integer and filter only numbers lt or eq to 1000
    then add to IntStream.*/
    private IntStream getNumberSequence() {
        if (numberSequence.isEmpty()) {
            return IntStream.empty();
        } else {
            return Stream.of(numberSequence.split(delimiter))
                    .mapToInt(Integer::parseInt)
                    .filter(n -> n <= MAX_ALLOWED_NUMBER);
        }
    }
}
