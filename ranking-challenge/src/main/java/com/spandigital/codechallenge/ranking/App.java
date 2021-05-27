/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> App<br>
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
public class App
{
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());
	public static final String LINE = "##########################################################################";

	/**
	 * Constructor:
	 */
	public App()
	{
		super();
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * main<br>
	 * <br>
	 * @param args<br>
	 * <br>
	 */
	public static void main(String... args)
	{
		if (args.length != 1)
			throw new MissingRequiredArgumentException("Should pass 1 (one) text input file name delimited with quotes.");

		String filename = args[0];

		LOGGER.log(Level.INFO, LINE);
		LOGGER.log(Level.INFO, "Starting Application with filename: {0}", RankingCalculator.getFilePath(filename));
		LOGGER.log(Level.INFO, LINE);

		try
		{
			RankingCalculator calculator = new RankingCalculator(filename);
			calculator.calculateRanking();
		}
		catch (Exception ex)
		{
			LOGGER.log(Level.SEVERE, "[Exception] has been caught.", ex);
		}

		LOGGER.log(Level.INFO, LINE);
		LOGGER.log(Level.INFO, "Output saved to: {0}", RankingCalculator.getFilePath(RankingCalculator.OUTPUT_FILE));
		LOGGER.log(Level.INFO, LINE);
	}
}
