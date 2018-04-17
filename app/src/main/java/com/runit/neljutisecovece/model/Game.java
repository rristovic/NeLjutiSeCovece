package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing game logic.
 */
public class Game {
    private static final int STANDARD_CELL_NUM = 40;
    private static final int PLAYER_IN_TEAM_NUM = 4;
    private static final int CLOSING_CELLS_PER_TEAM_NUM = 4;

    private ArrayList<Cell> gameFields;
    private Map<Long, Team> teams;
    private ArrayList<Player> players;
    private Map<Long, List<Cell>> closingCells;

    /**
     * Construct the game logic.
     *
     * @param teams array of string containing team names, which length must be from [1, 4].
     */
    public Game(String... teams) {
        initTeamsAndPlayers(teams.length, teams);
        initClosingCells();
        initCells();
    }

    /**
     * Constructs teams from provided team names.
     *
     * @param teamNames  team names.
     * @param teamNumber number of teams, must be from [1, 4];
     * @throws IllegalArgumentException if teamNumber is incorrect.
     */
    @SuppressLint("UseSparseArrays")
    private void initTeamsAndPlayers(int teamNumber, String... teamNames) throws IllegalArgumentException {
        if (teamNumber < 1 || teamNumber > 4) {
            throw new IllegalArgumentException("Players number is incorrect. Must be a number from 1 to 4, inclusive.");
        }
        this.teams = new HashMap<>(teamNumber);
        this.players = new ArrayList<>(teamNumber * PLAYER_IN_TEAM_NUM);
        for (int teamIndex = 0; teamIndex < teamNumber; teamIndex++) {
            Team team = new Team(teamIndex, Team.nextTeamColor(), teamNames[teamIndex]);
            this.teams.put(team.getTeamId(), team);
            // Fill the team with players
            for (int playerIndex = 0; playerIndex < PLAYER_IN_TEAM_NUM; playerIndex++) {
                players.add(new Player(Player.generatePlayerId(playerIndex, teamIndex), team));
            }
        }
    }

    /**
     * Constructs closing cells;
     */
    @SuppressLint("UseSparseArrays")
    private void initClosingCells() {
        closingCells = new HashMap<>(this.teams.keySet().size());
        for (Long teamId :
                this.teams.keySet()) {
            List<Cell> cells = new ArrayList<>(CLOSING_CELLS_PER_TEAM_NUM);
            for (int i = CLOSING_CELLS_PER_TEAM_NUM - 1; i >= 0; i--) {
                Cell c = new Cell(true);
                cells.add(c);
            }
            closingCells.put(teamId, cells);
        }
    }

    /**
     * Construct cells.
     */
    private void initCells() {
        gameFields = new ArrayList<>(STANDARD_CELL_NUM);
        // Setup first cell
        for (int i = 0; i < STANDARD_CELL_NUM; i++) {
            Cell c = new Cell();
            if (i % 10 == 0) {
                // Starting cell for the team
                Team t = getTeamForCellPosition(i);
                if (t != null) {
                    c.setCurrentCellTeamOwner(getTeamForCellPosition(i));
                }
            } else if (i % 10 == 9) {
                // Cell before ending cells and starting cell
                Team t = getTeamForCellPosition(i);
                if (t != null) {
                    c.setCurrentCellTeamOwner(t);
                    c.setNextClosingCell(
                            getFirstClosingCellForTeam(t)
                    );
                }
            }
        }
    }

    /**
     * Helper method for retrieving {@link Team} for provided cell position.
     *
     * @param position position of the cell.
     * @return team from the game, null if there is no team for provided position.
     */
    private Team getTeamForCellPosition(int position) {
        if (teams.size() > position / 10) {
            if (position % 10 == 0) {
                return teams.get(position / 10);
            } else {
                if (position / 10 == teams.size() - 1) {
                    return teams.get(0);
                } else
                    return teams.get((position / 10) + 1);
            }
        } else {
            return null;
        }
    }

    private Cell getFirstClosingCellForTeam(Team t) {
        return this.closingCells.get(t.getTeamId()).get(0);
    }

}
