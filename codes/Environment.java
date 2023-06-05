import java.awt.*;
import java.util.ArrayList;

/**
 * Name: Osman Selim Yuksel
 * Student ID: 2021400105
 * Date: 16.04.2023
 * This file contains code for setting up and running the game environment.
 * It contains all the game related constants
 * It also contains static methods to check intersections between game objects, namely ballHitsPlayer()  arrowHitsWall() arrowHitsBall()
 * It contains the playTheGame() method which is the fundamental part of the code.
 * to provide replayability, playTheGame() method is set RECURSIVE
 * playTheGame method basically sets and draws the game objects to the canvas, updates their coordinates and the velocities at each while iteration by calling other class methods if needed.
 * This while iterations makes the canvas output seem to be like an animation.
 * to make game animation better, GRVITY and PAUSE_DURATION is chosen smaller than the suggested values.
 * Just setting them different may provide different, fast or slow game experiences. I set the game according to my opinion
 * Apart from those two constants, all the constants are set as suggested
 * in the animation wile loop, conditions that makes game over are also checked and if game is over, code breaks the loop
 */
public class Environment {
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;
    public static final int TOTAL_GAME_DURATION = 40000;  // in miliseconds
    public static int PAUSE_DURATION = 1;   // pause duration in miliseconds  I take 1 instead of 15, to make animation better
    public static final int SCALE_Y = 9;
    public static final int SCALE_X = 16;
    public static final double GRAVITY = 0.0000003 * SCALE_Y;  // gravity in miliseconds [1/10 * suggested value is taken to optimize the animation and game]

    //player
    public static final double PLAYER_HEIGHT_WIDTH_RATE = 37.0/27.0;
    public static final double PLAYER_HEIGHT_SCALE_Y_RATE = 1.0/8.0;
    public static double PLAYERS_HEIGHT = SCALE_Y * PLAYER_HEIGHT_SCALE_Y_RATE; // 9 * 1/8
    public static double PLAYERS_WIDTH = PLAYERS_HEIGHT/PLAYER_HEIGHT_WIDTH_RATE;  // 9/8 / 37/27
    public static final int PERIOD_OF_PLAYER = 6000;  // in miliseconds
    public static final double VELOCITY_OF_PLAYER = ((double)SCALE_X-PLAYERS_WIDTH) / (double) PERIOD_OF_PLAYER;

    //ball
    public static final int PERIOD_OF_BALL = 15000; // in mil seconds
    public static final double HEIGHT_MULTIPLIER = 1.75;
    public static final  double RADIUS_MULTIPLIER = 2.0;
    public static final double MIN_POSSIBLE_HEIGHT = (9.0/8.0)*1.4;      // player’s height in scale * 1.4
    public static final double MIN_POSSIBLE_RADIUS = SCALE_Y * 0.0175;

    //bar
    public static final double BAR_SCALE = -1.0;
    // arrow
    public static final int PERIOD_OF_ARROW = 1500;  // in miliseconds
    public static final double VELOCITY_OF_ARROW = (double) SCALE_Y / (double) PERIOD_OF_ARROW;
    public static final double ARROWS_WIDTH = (8/37.0) * PLAYERS_WIDTH;

    /**
     * A method set and draw the game objects to the canvas, update their coordinates and the velocities at each while iteration by calling other class methods if needed.
     * Method also checks intersections between the game objects and if any, does the necessary changes
     * Note that this method is recursive in order to make the game replayable
     *
     */
    public static void playTheGame() {

        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT); // set the size of the canvas
        StdDraw.setXscale(0.0, SCALE_X); // set the scale of the coordinate system of canvas
        StdDraw.setYscale(-1.0, SCALE_Y);
        StdDraw.enableDoubleBuffering(); // Use for faster animations

        Player player = new Player((float)Environment.SCALE_X/2);

        ArrayList<Ball> ballsList = new ArrayList<>();

        Ball ball0 = new Ball((double)SCALE_X/4, 0.5,0); // moving right
        Ball ball1 = new Ball((double)SCALE_X/3, 0.5,1); // moving left
        Ball ball2 = new Ball((double)SCALE_X/4, 0.5,2); // moving right

        ball0.setyCoordinate(ball0.getyCoordinate()+ball0.getRadius());
        ball1.setyCoordinate(ball1.getyCoordinate()+ball1.getRadius());     // set the coordinates for the beginning of the game
        ball2.setyCoordinate(ball2.getyCoordinate()+ball2.getRadius());
        ball1.setVelocityX(-ball1.getVelocityX()); // moving left

        Ball.findVy(ball0);
        Ball.findVy(ball1);    // set the initial Y-velocities of the balls
        Ball.findVy(ball2);

        ballsList.add(ball0);
        ballsList.add(ball1);
        ballsList.add(ball2);

        // create an arrow
        Arrow arrow = new Arrow();

        ArrayList<Double> timeList = new ArrayList<>();
        double time1 = System.currentTimeMillis();
        timeList.add(time1);
        int index_counter = 0;  // variable to keep the index for time values in the list

        // create a time bar
        TimeBar timebar = new TimeBar();

        // loop to make the animation
        while (true) {
            StdDraw.clear();  // clear the canvas at each iteration
            double time2 = System.currentTimeMillis();
            timeList.add(time2);
            double time = timeList.get(index_counter+1) - timeList.get(index_counter);

            index_counter++;
            StdDraw.picture(8.0, 4.0, "background.png",SCALE_X,SCALE_Y+1.0);

            // arrow
            if (arrow.isActive()) {
                arrow.moveUp(time);
                StdDraw.picture(arrow.getxCoordinate(),arrow.getyCoordinate(),"arrow.png", ARROWS_WIDTH, arrow.getHeight());

                // check if arrow hits any ball and update the bool variable doesArrowHitBall accordingly
                boolean doesArrowHitBall = false;
                ArrayList<Ball> copy = (ArrayList<Ball>) ballsList.clone();  // we copy the ball arraylist because in foreach loop, removing elements is sometimes needed and in the loop we cannot remove the elements from the same list
                // iterate over the copied ball arraylist
                for (Ball ball : copy) {
                    if (arrowHitsBall(arrow,ball)){  // check whether arrow hits the ball
                        doesArrowHitBall = true;  // update the variable
                        if (ball.getCurLevel() > 0 ) {  // if balls level is bigger than 0, split it into two smaller level balls
                            ball1 = new Ball(ball.getxCoordinate(),ball.getyCoordinate(), ball.getCurLevel()-1);
                            ball1.setVelocityX(ball.getVelocityX());  // new ball will have the previous ball's X velocity
                            ball1.setVelocityY(ball1.getVyMax());     // new ball will have their max vertical velocity
                            ball2 = new Ball(ball.getxCoordinate(),ball.getyCoordinate(), ball.getCurLevel()-1);
                            ball2.setVelocityX(-ball.getVelocityX());    // new ball will have the previous ball's X velocity
                            ball2.setVelocityY(ball2.getVyMax());        // new ball will have their max vertical velocity

                            ballsList.add(ball1);    // add new balls to the ball list
                            ballsList.add(ball2);

                                ballsList.remove(ball);  // remove the ball which is hit by the arrow
                                break;  // if one ball is hit, break the loop so that other balls cannot be hit by the arrow
                            }
                            else{
                                ballsList.remove(ball);   // remove the exploded ball
                                break;

                            }
                        }
                    }

                    if (arrowHitsWall(arrow) || doesArrowHitBall) {  // if arrow reaches the upper bound or arrow hits a ball , arrow should disappear
                        arrow.setActive(false);     // deactivate the arrow
                        arrow.setHeight(0.0);       // set arrows positions as its passive position
                        arrow.setyCoordinate(0.0);
                    }
                }
                else if (StdDraw.isKeyPressed(32) & !arrow.isActive()){   // if spacebar is pressed and there is no arrow in game, activate the arrow
                    arrow.setActive(true);  // activate the arrow
                    arrow.setyCoordinate(0.0);    // set the coordinates of the arrow
                    arrow.setxCoordinate(player.getxCoordinate());
                    arrow.moveUp(time);    // move the arrow to the up
                    StdDraw.picture(arrow.getxCoordinate(),arrow.getyCoordinate(),"arrow.png", ARROWS_WIDTH, arrow.getHeight());  // draw the arrow
            }
            //player
            player.move(time);  // move the player by keyboard input
            StdDraw.picture(player.getxCoordinate(), player.yCoordinate, "player_back.png",PLAYERS_WIDTH, PLAYERS_HEIGHT);  // draw the player

            // if there is no ball to hit by arrow, player wins
            if (ballsList.isEmpty()) {   // check whether there is ball
                player.setWinner(true);
                break;
            }
            // time bar
            StdDraw.picture(8.0, -0.5, "bar.png",16.0,1.0);
            timebar.pass(time);     // adjust the x coordinate of the bar
            timebar.setColor(time);   // set the color

            // if player is run out of time, break the animation loop
            if (timebar.isGameOver()){ break; }

            // set time bar's color
            StdDraw.setPenColor(225,(int)timebar.colorRValue,0);
            StdDraw.filledRectangle(timebar.getxCoordinate(), timebar.yCoordinate, 8.0, 0.25);

            // balls
            for (Ball ball : ballsList) {
                if (!ballHitsPlayer(ball, player)) {    // if ball does not touch the player
                    Ball.updateVelocity(time, ball);   // update the velocity of the ball
                    ball.move(time);                   // move the ball, update its x and y coordinates
                }else{
                    player.setAlive(false); // if ball touches the player, set player not alive
                }
                StdDraw.picture(ball.getxCoordinate(), ball.getyCoordinate(), "ball.png", ball.getRadius()*2,ball.getRadius()*2);  // draw tha ball
            }

            if (! player.isAlive()) { break; }  // if player is not alive, break the loop, because game is over

            StdDraw.show();
            StdDraw.pause(PAUSE_DURATION); // pause duration in milliseconds
        }

        // if game is over because time is run out
        if (timebar.isGameOver()) {
            // as long as player does not press the "Y" "replay" button, draw the objects in their old positions
            while (!StdDraw.isKeyPressed(89)){   // as long as "Y" is not pressed, this loop will be iterated through
                StdDraw.picture(8.0, 4.0, "background.png",SCALE_X,SCALE_Y+1.0);
                StdDraw.picture(8.0, -0.5, "bar.png",16.0,1.0);    // draw the bar background
                StdDraw.picture(player.getxCoordinate(), player.yCoordinate, "player_back.png",PLAYERS_WIDTH, PLAYERS_HEIGHT);    // draw the player

                // draw the game screen and write the messages
                StdDraw.picture(SCALE_X/2.0,SCALE_Y/2.18,"game_screen.png",SCALE_X/3.8, SCALE_Y/4.0);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.0, "Game Over!");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.3, "To Replay Click “Y”");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.6, "To Quit Click “N”");
                StdDraw.show();
                if (StdDraw.isKeyPressed(78)){ System.exit(0);}  // if user presses the N, quit button. terminate the code
            }
            // if "Y" is pressed, loop above will not be iterated, computer will read this line.
            playTheGame();  // if "Y" is pressed, recursive call will be active, play the game again
        }

        // if game is over because player is hit by the ball
        if (!player.isAlive()) {

            while (!StdDraw.isKeyPressed(89)){   // as long as "Y" replay button is not pressed, this loop will be iterated
                StdDraw.picture(8.0, 4.0, "background.png",SCALE_X,SCALE_Y+1.0);
                StdDraw.picture(8.0, -0.5, "bar.png",16.0,1.0);    // draw the background of the bar
                StdDraw.picture(player.getxCoordinate(), player.yCoordinate, "player_back.png",PLAYERS_WIDTH, PLAYERS_HEIGHT);    // draw the player

                // draw the game screen and write the messages
                StdDraw.picture(SCALE_X/2.0,SCALE_Y/2.18,"game_screen.png",SCALE_X/3.8, SCALE_Y/4.0);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.0, "Game Over!");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.3, "To Replay Click “Y”");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.6, "To Quit Click “N”");
                StdDraw.show();
                if (StdDraw.isKeyPressed(78)){ System.exit(0);}  // if user presses the N, quit button. terminate the code
            }
            playTheGame();   // if "Y" is pressed, recursive call will be active, play the game again
        }

        // if game is over because player wins
        if (player.isWinner()){
            while (!StdDraw.isKeyPressed(89)){   // as long as "Y" is not pressed, this loop will be iterated.
                StdDraw.picture(8.0, 4.0, "background.png",SCALE_X,SCALE_Y+1.0);  // draw the bacground
                StdDraw.picture(8.0, -0.5, "bar.png",16.0,1.0);    // draw the bar background
                StdDraw.picture(player.getxCoordinate(), player.yCoordinate, "player_back.png",PLAYERS_WIDTH, PLAYERS_HEIGHT);    // draw the player

                // draw the game screen
                StdDraw.picture(SCALE_X/2.0,SCALE_Y/2.18,"game_screen.png",SCALE_X/3.8, SCALE_Y/4.0);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.0, "You Won!");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.3, "To Replay Click “Y”");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(SCALE_X/2.0, SCALE_Y/2.6, "To Quit Click “N”");
                StdDraw.show();
                if (StdDraw.isKeyPressed(78)){ System.exit(0);}  // if user presses the N, quit button. terminate the code
            }
            playTheGame();   // if "Y" is pressed, play the game again
        }

    }

    /**
     * checks whether arrow hits the left and right wall or not
     * @param arrow
     * @return true if arrow hits the walls, else return false
     */

    private static boolean arrowHitsWall(Arrow arrow) {
        if (arrow.getyCoordinate() >= Environment.SCALE_Y/2.0){
            arrow.setActive(false);
            return true;
        }else{
            return false;
        }
    }

    /**
     * checks whether arrow touches the ball or not
     * @param arrow arrow
     * @param ball ball to be check
     * @return true if arrow hits the ball, else false
     */
    private static boolean arrowHitsBall(Arrow arrow, Ball ball){
        double squareOfRadius = Math.pow(ball.getRadius(),2);

        // Iterate over each point along the length of the arrow
        for (double y = 0.0; y <= arrow.getyCoordinate() * 2; y += 0.1) {
            double squareOfXDifferences = Math.pow(arrow.getxCoordinate() - ball.getxCoordinate(), 2);
            double squareOfYDifferences = Math.pow(y - ball.getyCoordinate(), 2);

            if (squareOfXDifferences + squareOfYDifferences <= squareOfRadius) {
                // split the ball into 2 pieces if possible, or ball will disappear
                arrow.setActive(false);  // when hits the ball, arrow should be inactive
                return true;
            }
        }
        return false;
    }

    /**
     * checks whether ball hits the player or not
     * Algorithm: this method basically calculates the closest distance between the borders of player and the center of ball
     * if this distance is smaller than the ball's radius, method understands that ball hits the player
     * @param ball ball to check
     * @param player player in the game
     * @return true if ball hits player, else return false
     */
    private static boolean ballHitsPlayer(Ball ball, Player player) {
        // calculate the player rectangle
        double playerX1 = player.getxCoordinate() - PLAYERS_WIDTH/2.0;
        double playerY1 = player.yCoordinate - PLAYERS_HEIGHT/2.0;
        double playerX2 = player.getxCoordinate() + PLAYERS_WIDTH/2.0;
        double playerY2 = player.yCoordinate + PLAYERS_HEIGHT/2.0;

        // calculate the closest point on the player (assumed to be rectangle) to the center of the ball
        double ballX = ball.getxCoordinate();
        double ballY = ball.getyCoordinate();
        double closestX = Math.max(playerX1, Math.min(ballX, playerX2));
        double closestY = Math.max(playerY1, Math.min(ballY, playerY2));

        // calculate the distance between the center of the ball and the closest point on the player rectangle
        double dx = closestX - ballX;
        double dy = closestY - ballY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // check if the distance is less than or equal to the radius of the ball
        return distance <= ball.getRadius() ;
    }

}
