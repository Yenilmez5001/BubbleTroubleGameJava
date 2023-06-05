/**
 * Name: Osman Selim Yuksel
 * Student ID: 2021400105
 * Date: 16.04.2023
 * This file contains code for representing and updating the arrow in the game.
 */

public class Arrow {
    private double xCoordinate;
    private double yCoordinate;
    private double height;
    private boolean active;

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * constructor
     * @param xCoordinate x Coordinate of the arrow
     * @param yCoordinate y Coordinate of the arrow
     */
    public Arrow(double xCoordinate, double yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * creates a new arrow and makes active variable false
     */
    Arrow() {
        active = false;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() {
        return active;
    }

    /**
     * updates the y coordinate and height of the arrow
     * method's main function in the animation is to make the arrow look like going up
     * @param time time difference between each two successive iteration (in miliseconds)
     */
    public void moveUp(double time) {
        this.setyCoordinate(this.getyCoordinate()+(Environment.VELOCITY_OF_ARROW*time));
        this.setHeight(this.getyCoordinate()*2);
    }

}
