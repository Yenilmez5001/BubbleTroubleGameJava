/**
 * Name: Osman Selim Yuksel
 * Student ID: 2021400105
 * Date: 16.04.2023
 * This file contains code for representing and updating the time bar in the game.
 */

public class TimeBar {
    public double xCoordinate = (float)Environment.SCALE_X/2;
    public final double yCoordinate = Environment.BAR_SCALE/2;  // note that time bars y coordinate is fixed, so variable is final
    public double colorRValue = 225;
    private boolean gameOver = false;  // default value for arrow is inactive, which means false

    /**
     * Makes the bar shrink over time by updating its x coordinate
     * @param time Time difference between two iterations
     */
    public void pass (double time){
        this.setxCoordinate(xCoordinate - (time/Environment.TOTAL_GAME_DURATION)*Environment.SCALE_X);  // make the bar move in x coordinate in accordance with the time passed;
    }

    /**
     * Adjusts the color of time bar
     * @param time Time difference between two iterations
     */
    public void setColor(double time){
        if (colorRValue - 225*(time/Environment.TOTAL_GAME_DURATION) <= 0){
            this.gameOver = true;
        }
        setColorRValue(colorRValue - 225*(time/Environment.TOTAL_GAME_DURATION));
    }

    /**
     * no arg constructor
     * creates a new TimeBar and sets gameOver false
     */
    public TimeBar(){
        gameOver = false;
    }
    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    public void setColorRValue(double colorRValue) {
        this.colorRValue = colorRValue;
    }
    public double getxCoordinate() {
        return xCoordinate;
    }
    public boolean isGameOver() {
        return gameOver;
    }

}