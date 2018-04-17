package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;

/**
 * Represents a player in the game.
 */
public class Player {
    // Player id in the game
    private final long playerId;
    // Player's team
    private final Team currentTeam;
    // Indicating if player is currently in the game
    private boolean isInGame;
    // Reference to the current player cell if the player is in the game.
    private Cell currentPlayerCell;

    public Player(long playerId, Team playerTeam) {
        this.playerId = playerId;
        this.currentTeam = playerTeam;
        this.isInGame = false;
        this.currentPlayerCell = null;
    }

    /**
     * Method to call when player has moved to another cell.
     *
     * @param newCell {@link Cell} object to move player to.
     */
    public void setCurrentCell(Cell newCell) {
        this.currentPlayerCell = newCell;
        this.isInGame = true;
    }

    /**
     * Method to call when this player has been removed from the game.
     */
    public void removedFromGame() {
        this.currentPlayerCell = null;
        this.isInGame = false;
    }

    /**
     * Chechks if this player is in the game and available to move.
     *
     * @return true if this player is in the game.
     */
    public boolean isInGame() {
        return this.isInGame;
    }

    /**
     * Checks if provided player is from the same team as this one.
     *
     * @param player {@link Player} object to compare the teams with.
     * @return true if both players are form the same team.
     */
    public boolean isFromSameTeam(Player player) {
        return this.currentTeam.equals(player.currentTeam);
    }

    /**
     * Checks if this player belongs to the provided team.
     *
     * @param team team to check the player with.
     * @return true this player belongs to provided team.
     */
    public boolean belongsToTeam(Team team) {
        return this.currentTeam.equals(team);
    }

    /**
     * Generates new player id for provided team id.
     * @param playerId long to be used in new player id.
     * @param teamId team id to be used in new player id.
     * @return long representing new player id based on the team id.
     */
    @SuppressLint("DefaultLocale")
    public static long generatePlayerId(long playerId, long teamId) {
        return Long.valueOf(String.format("%d%d", teamId, playerId));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Player) {
            Player compareWith = (Player) obj;
            return compareWith.playerId == this.playerId;
        } else {
            return false;
        }
    }
}
