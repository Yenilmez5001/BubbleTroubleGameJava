/**
 * Name: Osman Selim Yuksel
 * Student ID: 2021400105
 * Date: 16.04.2023
 * This file contains code for representing and controlling balls in the game.
 * it contains a move() method to move the ball by updating its coordinates,
 * it also contains updateVelocity() method updating balls y velocity
 * it also contains findVy() to calculate the y velocity of a ball
 */
public class Ball {
    private int curLevel;
    public final double currentHeight;  // Will be calculated later on. currentHeight = Environment.minPossibleHeight * Math.pow(Environment.heightMultiplier,curLevel);
    private final double radius;
    private final double VyMax;
    private double velocityY;
    private double VelocityX = Environment.SCALE_X/(double) Environment.PERIOD_OF_BALL;   // velocity x (1/miliseconds) is the same for all balls. So it is final.
    private double xCoordinate;
    private double yCoordinate;

    /**
     * creates a new ball and sets its radius, x and coordinates, current height, current level, and maximum Y velocity
     * @param xCoordinate x coordinate of ball
     * @param yCoordinate y coordinate of ball
     * @param curLevel level of ball
     */
    Ball(double xCoordinate, double yCoordinate,int curLevel) {
        this.radius = Environment.MIN_POSSIBLE_RADIUS * Math.pow((Environment.RADIUS_MULTIPLIER), curLevel);  // calculated as suggested
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.currentHeight = (Environment.MIN_POSSIBLE_HEIGHT) * Math.pow(Environment.HEIGHT_MULTIPLIER, curLevel);   // calculated as suggested
        this.curLevel = curLevel;
        this.VyMax = Math.sqrt(2*Environment.GRAVITY*currentHeight); // calculated as suggested
    }

    public int getCurLevel() {
        return curLevel;
    }
    public double getCurrentHeight() {
        return currentHeight;
    }

    public double getRadius() {
        return radius;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityX(double velocityX) {
        this.VelocityX = velocityX;
    }

    public double getVelocityX() {
        return VelocityX;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public double getVyMax() {
        return VyMax;
    }

    /**
     * finds the current Y velocity of a given ball
     * Vy = sqrt(2 * G * (MaxHeight - CurrentHeight)) formula is used to find the current Y velocity
     * @param ball ball in the game
     */
    public static void findVy(Ball ball) {
        double Vy = Math.sqrt(2 * Environment.GRAVITY * (ball.getCurrentHeight() - ball.getyCoordinate()));   /// SUBTRACTING 2 * R !!  ??
        ball.setVelocityY(Vy);
    }

    /**
     * updates the x and y coordinates of the ball
     * @param time time difference between each two successive iteration (in miliseconds)
     */
    public void move(double time) {
        this.setyCoordinate(this.getyCoordinate()+(this.getVelocityY()*time));
        this.setxCoordinate(this.getxCoordinate()+(this.getVelocityX()*time));
    }

    /**
     * updates the y velocity of the ball by decreasing it,
     * updates x and y velocities of the ball when ball reaches the boundaries
     * @param time time difference between each two successive iteration (in miliseconds)
     */
    public static void updateVelocity(double time, Ball ball) {
        if (ball.getxCoordinate() + ball.radius > (double)Environment.SCALE_X || ball.getxCoordinate()<=ball.radius) {ball.setVelocityX(-ball.getVelocityX());}

        else if (ball.getyCoordinate() - ball.getRadius() <= 0.0) {
            // If ball has moved below bottom boundary, adjust its y-coordinate
            ball.setyCoordinate(ball.radius);  // set the y coordinate to the radius, which means the ball is tangent to the ground

            // Set vertical velocity to maximum value because when the ball touches the ground, it should have its max vertical velocity in projectile motion
            ball.setVelocityY(ball.VyMax);
        }
        else{
            ball.setVelocityY(ball.getVelocityY() - Environment.GRAVITY*time);
        }
    }
    /**
     * it does not have any usage in code
     * But since it is suggested, I write a no arg-constructor
     */
    public Ball() {
        currentHeight = 0.0;
        radius = 0.0;
        VyMax = 0.0;
    }
}
