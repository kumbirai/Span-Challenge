/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import java.io.IOException;
import java.nio.file.Files;
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

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> RankingCalculator<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 */
public class RankingCalculator
{
    public static final String OUTPUT_FILE = "league-table.txt";

    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;
    private static final int LOSE_POINTS = 0;
    private final List<Team> teams;
    private String filename;
    private List<String> matches;

    /**
     * Constructor:
     */
    public RankingCalculator()
    {
        teams = new ArrayList<>();
    }

    /**
     * Constructor: @param filename
     */
    public RankingCalculator(String filename)
    {
        this();
        this.filename = filename;
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
     * calculateRanking<br>
     * <br><br>
     * <br>
     *
     * @throws IOException - An exception thrown if there is a problem with the match file
     */
    public void calculateRanking() throws IOException
    {
        if (this.matches == null)
        {
            matches = Files.readAllLines(FileUtil.getFilePath(filename));
        }
        matches.forEach(this::determinePoints);
        List<Team> rankedTeams = rankTeams();
        FileUtil.createOutputFile(rankedTeams);
    }

    /**
     * Purpose:<br>
     * <br>
     * determinePoints<br>
     * <br>
     *
     * @param match<br> <br>
     */
    private void determinePoints(String match)
    {
        String[] teamScores = match.split(",");

        var teamA = extractTeamNameAndScore(teamScores[0]);
        var teamB = extractTeamNameAndScore(teamScores[1]);

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
     *
     * @param teamScore -
     * @return <br> <br>
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
     *
     * @param points - <br> <br>
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
     *
     * @return <br> <br>
     */
    private List<Team> rankTeams()
    {
        List<Team> pointsRankedList = teams.stream()
                                           .sorted(Comparator.comparing(Team::getPoints)
                                                             .reversed()
                                                             .thenComparing(Team::getName))
                                           .collect(Collectors.toList());

        ToIntFunction<Team> pointsExtractor = Team::getPoints;
        SortedMap<Integer, List<Team>> rankingsMap = new TreeMap<>();
        pointsRankedList.forEach(team ->
                                 {
                                     Integer points = pointsExtractor.applyAsInt(team);

                                     if (rankingsMap.isEmpty())
                                     {
                                         rankingsMap.put(1, new LinkedList<>());
                                     }
                                     else
                                     {
                                         Integer rank = rankingsMap.lastKey();
                                         List<Team> teamsList = rankingsMap.get(rank);
                                         if (!points.equals(pointsExtractor.applyAsInt(teamsList.get(0))))
                                         {
                                             rankingsMap.put(rank + teamsList.size(), new LinkedList<>());
                                         }
                                     }

                                     Integer lastKey = rankingsMap.lastKey();
                                     team.setRank(lastKey);
                                     rankingsMap.get(lastKey)
                                                .add(team);
                                 });

        return pointsRankedList;
    }
}
