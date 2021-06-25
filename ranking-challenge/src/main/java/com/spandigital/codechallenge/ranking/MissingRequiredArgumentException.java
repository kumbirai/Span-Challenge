/*
 Copyright (c) 2021

 All rights reserved.
 */
package com.spandigital.codechallenge.ranking;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> MissingRequiredArgumentException<br>
 * <br>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 * <br>
 * @date 27 May 2021<br>
 */
public class MissingRequiredArgumentException extends RuntimeException
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4339596283898606878L;

    /**
     * Constructor: @param message
     */
    public MissingRequiredArgumentException(String message)
    {
        super(message);
    }
}
