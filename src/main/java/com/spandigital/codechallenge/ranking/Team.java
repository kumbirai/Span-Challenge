/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> Team<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 */
@Data
@AllArgsConstructor
public class Team
{
    private int rank;
    private String name;
    private int points;

    /**
     * Purpose:<br>
     * <br>
     * addPoints<br>
     * <br>
     *
     * @param addPoints<br> <br>
     */
    public void addPoints(int addPoints)
    {
        this.points += addPoints;
    }

    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return String.format("%s. %s, %s ", rank, name, points)
                     .concat(points != 1 ? "pts" : "pt");
    }
}
