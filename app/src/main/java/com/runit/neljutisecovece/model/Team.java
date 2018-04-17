package com.runit.neljutisecovece.model;

/**
 * Created by Radovan Ristovic on 4/16/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class Team {
    private final long teamId;
    private final long teamColor;
    private final String teamName;

    public Team(long teamId, long teamColor, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamColor = teamColor;
    }

    public long getTeamColor() {
        return teamColor;
    }

    public String getTeamName() {
        return teamName;
    }

    long getTeamId() {
        return teamId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Team) {
            Team compareWith = (Team) obj;
            return compareWith.teamId == this.teamId;
        } else {
            return false;
        }
    }

    public static int nextTeamColor() {
        return 00;
    }
}
