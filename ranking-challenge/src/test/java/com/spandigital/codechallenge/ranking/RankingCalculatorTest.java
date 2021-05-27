/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> RankingCalculatorTest<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 26 May 2021<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 *					
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
	 * test_constructor_fileNotFound<br>
	 * <br><br>
	 * <br>
	 */
	@Test
	void test_constructor_fileNotFound()
	{
		assertThrows(IOException.class, () ->
		{
			new RankingCalculator("test.txt");
		});
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * test_calculateRanking<br>
	 * <br>
	 * @return<br>
	 * <br>
	 */
	static Stream<Arguments> test_calculateRanking()
	{
		return Stream.of(Arguments.of(Arrays.asList("Lions 3, Snakes 3"), "1. Lions, 1 pt" + System.lineSeparator() + "1. Snakes, 1 pt"),
				Arguments.of(Arrays.asList("Lions 3, Snakes 1"), "1. Lions, 3 pts" + System.lineSeparator() + "2. Snakes, 0 pts"),
				Arguments.of(Arrays.asList("Lions 1, Snakes 3"), "1. Snakes, 3 pts" + System.lineSeparator() + "2. Lions, 0 pts"),
				Arguments.of(
						Arrays.asList("Lions 3, Snakes 3", "Tarantulas 1, FC Awesome 0", "Lions 1, FC Awesome 1", "Tarantulas 3, Snakes 1",
								"Lions 4, Grouches 0"),
						"1. Tarantulas, 6 pts" + System.lineSeparator() + "2. Lions, 5 pts" + System.lineSeparator() + "3. FC Awesome, 1 pt"
								+ System.lineSeparator() + "3. Snakes, 1 pt" + System.lineSeparator() + "5. Grouches, 0 pts"));
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * test_calculateRanking<br>
	 * <br>
	 * @param match
	 * @param expected
	 * @throws Exception<br>
	 * <br>
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
	 * @return
	 * @throws IOException<br>
	 * <br>
	 */
	private String readOutputFile() throws IOException
	{
		Path path = FileSystems.getDefault()
				.getPath("", RankingCalculator.OUTPUT_FILE)
				.toAbsolutePath();
		String output;
		try (Stream<String> stream = Files.lines(path))
		{
			output = stream.collect(Collectors.joining(System.lineSeparator()));
		}
		return output;
	}
}
