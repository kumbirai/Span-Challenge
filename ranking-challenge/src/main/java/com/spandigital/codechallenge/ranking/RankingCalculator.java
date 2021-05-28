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
		List<Team> rankedTeams = rankTeams();
		createOutputFile(rankedTeams);
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

		Score teamA = extractTeamNameAndScore(teamScores[0]);
		Score teamB = extractTeamNameAndScore(teamScores[1]);

		if (teamA.getScore() == teamB.getScore())
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA.getName(), DRAW_POINTS), new Team(0, teamB.getName(), DRAW_POINTS)));
		}

		if (teamA.getScore() > teamB.getScore())
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA.getName(), WIN_POINTS), new Team(0, teamB.getName(), LOSE_POINTS)));
		}

		if (teamA.getScore() < teamB.getScore())
		{
			updateTeamScores(Arrays.asList(new Team(0, teamA.getName(), LOSE_POINTS), new Team(0, teamB.getName(), WIN_POINTS)));
		}
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * extractTeamNameAndScore<br>
	 * <br>
	 * @param teamScore
	 * @return<br>
	 * <br>
	 */
	private Score extractTeamNameAndScore(String teamScore)
	{
		int idx = teamScore.lastIndexOf(" ");

		return new Score(teamScore.substring(0, idx)
				.trim(),
				Integer.parseInt(teamScore.substring(idx)
						.trim()));
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
	 * rankTeams<br>
	 * <br>
	 * @return<br>
	 * <br>
	 */
	private List<Team> rankTeams()
	{
		List<Team> pointsRanked = teams.stream()
				.sorted(Comparator.comparing(Team::getPoints)
						.reversed()
						.thenComparing(Team::getName))
				.collect(Collectors.toList());

		ToIntFunction<Team> pointsExtractor = Team::getPoints;
		SortedMap<Integer, List<Team>> rankings = new TreeMap<>();
		pointsRanked.forEach(team ->
		{
			Integer points = pointsExtractor.applyAsInt(team);

			if (rankings.isEmpty())
			{
				rankings.put(1, new LinkedList<>());
			}
			else
			{
				Integer rank = rankings.lastKey();
				List<Team> teamsList = rankings.get(rank);
				if (!points.equals(pointsExtractor.applyAsInt(teamsList.get(0))))
				{
					rankings.put(rank + teamsList.size(), new LinkedList<>());
				}
			}

			Integer lastKey = rankings.lastKey();
			team.setRank(lastKey);
			rankings.get(lastKey)
					.add(team);
		});

		return pointsRanked;
	}

	/**
	 * Purpose:<br>
	 * <br>
	 * createOutputFile<br>
	 * <br>
	 * @param rankedTeams
	 * @throws IOException<br>
	 * <br>
	 */
	private void createOutputFile(List<Team> rankedTeams) throws IOException
	{
		//
		Path outputFilePath = getFilePath(OUTPUT_FILE);
		Files.write(outputFilePath, rankedTeams.stream()
				.map(Team::toString)
				.collect(Collectors.toList()));
	}
}
