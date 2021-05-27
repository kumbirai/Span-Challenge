/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> RankingCalculator<br>
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
public class RankingCalculator
{
	public static final String OUTPUT_FILE = "league-table.txt";

	private static final int WIN_POINTS = 3;
	private static final int DRAW_POINTS = 1;
	private static final int LOSE_POINTS = 0;

	private String filename;
	private List<String> matches;
	private List<Team> teams;

	/**
	 * Constructor: 
	 */
	private RankingCalculator()
	{
		super();
		teams = new ArrayList<>();
	}

	/**
	 * Constructor: @param filename
	 * @throws IOException 
	 */
	public RankingCalculator(String filename) throws IOException
	{
		this();
		this.filename = filename;
		readInputFile();
	}

	/**
	 * Constructor: @param matches
	 */
	public RankingCalculator(List<String> matches)
	{
		this();
		this.matches = matches;
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * getFilePath<br>
	 * <br>
	 * @param file
	 * @return<br>
	 * <br>
	 */
	public static Path getFilePath(String file)
	{
		return FileSystems.getDefault()
				.getPath("", file)
				.toAbsolutePath();
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * calculateRanking<br>
	 * <br><br>
	 * <br>
	 * @throws IOException 
	 */
	public void calculateRanking() throws IOException
	{
		matches.forEach(this::determinePoints);
		createOutputFile();
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * readInputFile<br>
	 * <br><br>
	 * <br>
	 * @throws IOException 
	 * @throws  
	 */
	private void readInputFile() throws IOException
	{
		Path scoreFilePath = getFilePath(this.filename);

		try (Stream<String> stream = Files.lines(scoreFilePath))
		{
			matches = stream.collect(Collectors.toList());
		}
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * determinePoints<br>
	 * <br>
	 * @param match<br>
	 * <br>
	 */
	private void determinePoints(String match)
	{
		String[] teamScores = match.split(",");

		String[] teamA = extractTeamNameAndScoreIntoArray(teamScores[0]);
		String[] teamB = extractTeamNameAndScoreIntoArray(teamScores[1]);

		if (Integer.parseInt(teamA[1]) == Integer.parseInt(teamB[1]))
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA[0], DRAW_POINTS), new Team(0, teamB[0], DRAW_POINTS)));
		}

		if (Integer.parseInt(teamA[1]) > Integer.parseInt(teamB[1]))
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA[0], WIN_POINTS), new Team(0, teamB[0], LOSE_POINTS)));
		}

		if (Integer.parseInt(teamA[1]) < Integer.parseInt(teamB[1]))
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA[0], LOSE_POINTS), new Team(0, teamB[0], WIN_POINTS)));
		}
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * extractTeamNameAndScoreIntoArray<br>
	 * <br>
	 * @param teamScore
	 * @return<br>
	 * <br>
	 */
	private String[] extractTeamNameAndScoreIntoArray(String teamScore)
	{
		int idx = teamScore.lastIndexOf(" ");
		return new String[]
		{ teamScore.substring(0, idx)
				.trim(),
				teamScore.substring(idx)
						.trim() };
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * updateTeamScores<br>
	 * <br>
	 * @param points<br>
	 * <br>
	 */
	private void updateTeamScores(List<Team> points)
	{
		points.forEach(team ->
		{
			Optional<Team> findTeam = teams.stream()
					.filter(t -> t.getName()
							.equalsIgnoreCase(team.getName()))
					.findFirst();
			if (findTeam.isPresent())
			{
				findTeam.get()
						.addPoints(team.getPoints());
			}
			else
			{
				teams.add(team);
			}
		});
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * createOutputFile<br>
	 * <br><br>
	 * <br>
	 * @throws IOException 
	 */
	private void createOutputFile() throws IOException
	{
		//
		Path outputFilePath = getFilePath(OUTPUT_FILE);
		Files.write(outputFilePath, rankTeams());
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * rankTeams<br>
	 * <br>
	 * @return<br>
	 * <br>
	 */
	private List<String> rankTeams()
	{
		List<Team> pointsRanked = teams.stream()
				.sorted(Comparator.comparing(Team::getPoints)
						.reversed()
						.thenComparing(Team::getName))
				.collect(Collectors.toList());

		ToIntFunction<Team> pointsExtractor = Team::getPoints;
		SortedMap<Integer, List<Team>> ranking = new TreeMap<>();
		pointsRanked.forEach(item ->
		{
			Integer points = pointsExtractor.applyAsInt(item);

			if (ranking.isEmpty())
			{
				ranking.put(1, new LinkedList<>());
			}
			else
			{
				Integer rank = ranking.lastKey();
				List<Team> items = ranking.get(rank);
				if (!points.equals(pointsExtractor.applyAsInt(items.get(0))))
				{
					ranking.put(rank + items.size(), new LinkedList<>());
				}
			}

			Integer lastKey = ranking.lastKey();
			item.setRank(lastKey);
			ranking.get(lastKey)
					.add(item);
		});

		return pointsRanked.stream()
				.map(Team::toString)
				.collect(Collectors.toList());
	}
}
