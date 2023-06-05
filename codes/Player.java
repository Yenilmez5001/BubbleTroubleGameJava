import java.awt.event.KeyEvent;

/**
 * Name: Osman Selim Yuksel
 * Student ID: 2021400105
 * Date: 16.04.2023
 * This file contains code for representing and controlling a player in the game.
 */
public class Player {
    public final double yCoordinate = Environment.PLAYERS_HEIGHT/2;   // Note that player does not move on vertical axis. So its y coordinate is fixed
    private double xCoordinate = (float)Environment.SCALE_X/2;
    private boolean winner;  // boolean variable to check whether the player wins the game
    private boolean isAlive;  // boolean variable to check whether the player is alive or not(ball hits the player)

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * checks whether the right and left keys are pressed by taking taking keyboard input
     * if key is pressed, move the player, which means update its x coordinate
     * also checks whether the player reaches the boundaries of the canvas
     * if player is in boundary, do not move it
     * @param time time difference between each two successive iteration (in miliseconds)
     */
    public void move(double time){

        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) & xCoordinate < 16.0-Environment.PLAYERS_WIDTH/2) {
            this.setxCoordinate(xCoordinate + Environment.VELOCITY_OF_PLAYER * time);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) & xCoordinate > Environment.PLAYERS_WIDTH/2){
            this.setxCoordinate(xCoordinate-Environment.VELOCITY_OF_PLAYER* time);
        }
    }

    /**
     * constructor, creates a player and sets its coordinate and boolean instance variables
     * @param xCoordinate x coordinate of player
     */
    Player(double xCoordinate) {
        this.xCoordinate = xCoordinate;
        this.winner = false;
        this.isAlive = true;
    }

    /**
     * this no-arg constructor has no usage
     * it is written just to comply with recommendations
     */
    Player(){ }
}
