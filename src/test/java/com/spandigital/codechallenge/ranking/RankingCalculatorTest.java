/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> RankingCalculatorTest<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 */
class RankingCalculatorTest
{
	/**
	 * Constructor:
	 */
	public RankingCalculatorTest()
	{
		super();
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * test_calculateRanking<br>
	 * <br>
	 *
	 * @return - a stream of arguments to be used in the <code>test_calculateRanking</code> test<br> <br>
	 */
	static Stream<Arguments> test_calculateRanking()
	{
		return Stream.of(Arguments.of(Collections.singletonList("Lions 3, Snakes 3"), "1. Lions, 1 pt" + System.lineSeparator() + "1. Snakes, 1 pt"),
						 Arguments.of(Collections.singletonList("Lions 3, Snakes 1"), "1. Lions, 3 pts" + System.lineSeparator() + "2. Snakes, 0 pts"),
						 Arguments.of(Collections.singletonList("Lions 1, Snakes 3"), "1. Snakes, 3 pts" + System.lineSeparator() + "2. Lions, 0 pts"),
						 Arguments.of(Arrays.asList("Lions 3, Snakes 3", "Tarantulas 1, FC Awesome 0", "Lions 1, FC Awesome 1", "Tarantulas 3, Snakes 1", "Lions 4, Grouches 0"),
									  "1. Tarantulas, 6 pts" + System.lineSeparator() + "2. Lions, 5 pts" + System.lineSeparator() + "3. FC Awesome, 1 pt" + System.lineSeparator() + "3. Snakes, 1 pt" + System.lineSeparator() + "5. Grouches, 0 pts"));
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * test_constructor_fileNotFound<br>
	 * <br><br>
	 * <br>
	 */
	@Test
	void test_calculateRanking_fileNotFound()
	{
		assertThrows(IOException.class, () ->
		{
			RankingCalculator rankingCalculator = new RankingCalculator("test.txt");
			rankingCalculator.calculateRanking();
		});
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * test_calculateRanking<br>
	 * <br>
	 *
	 * @param matches  -
	 * @param expected -
	 * @throws Exception <br> <br>
	 */
	@ParameterizedTest
	@MethodSource
	void test_calculateRanking(List<String> matches, String expected) throws Exception
	{
		RankingCalculator rankingCalculator = new RankingCalculator(matches);
		rankingCalculator.calculateRanking();
		assertEquals(expected, readOutputFile());
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * readOutputFile<br>
	 * <br>
	 *
	 * @return -
	 * @throws IOException <br> <br>
	 */
	private String readOutputFile() throws IOException
	{
		return Files.readAllLines(FileSystems.getDefault()
										  .getPath("", RankingCalculator.OUTPUT_FILE)
										  .toAbsolutePath())
				.stream()
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
