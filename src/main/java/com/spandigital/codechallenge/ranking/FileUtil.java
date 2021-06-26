/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> FileUtil<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 */
public class FileUtil
{
    /**
     * Constructor:
     */
    private FileUtil()
    {
    }

    /**
     * Purpose:<br>
     * <br>
     * getFilePath<br>
     * <br>
     *
     * @param file - File name for path to get
     * @return Path of the filename given<br> <br>
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
     * createOutputFile<br>
     * <br>
     *
     * @param rankedTeams - List of the teams in their ranked order
     * @throws IOException <br> <br>
     */
    public static void createOutputFile(List<Team> rankedTeams) throws IOException
    {
        var outputFilePath = getFilePath(RankingCalculator.OUTPUT_FILE);
        Files.write(outputFilePath, rankedTeams.stream()
                .map(Team::toString)
                .collect(Collectors.toList()));
    }
}
