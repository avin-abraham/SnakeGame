/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;

/**
 *
 * @author skinnnero5
 */
public class AISnake2 extends Snake {

    boolean isPathing;
    SquareCoords scanLocation;
    public Rect2d vision, pathX, pathY, target;
    int randomCooldown;
    private static final int avoidEdgeDist = 10;

    public static Color PATHING_COLOR = Color.red;
    public static Color NOT_PATHING_COLOR = Color.YELLOW;
    
    public static final int AI2_SPEED_MULTIPLIER = SnakePanel.AI2_SPEED_MULTIPLIER;
    
    public static final boolean AI2_RANDOM_SPAWN_TOGGLE = SnakePanel.AI2_RANDOM_SPAWN_TOGGLE;
    
    public static final int AI2_NON_RANDOM_SPAWN_X = SnakePanel.AI2_NON_RANDOM_SPAWN_X;
    public static final int AI2_NON_RANDOM_SPAWN_Y = SnakePanel.AI2_NON_RANDOM_SPAWN_Y;

    public AISnake2(Color color, String name) {
        super(color, name);
        isPathing = false;
        isPlayer = false;
        vision = new Rect2d(this.getHead().getCenter().x - 500, this.getHead().getCenter().y - 500, 1000, 1000);
        pathX = new Rect2d(this.getHead().getLeft(), this.getHead().getTop(), 1000, this.getWidth());
        pathY = new Rect2d(this.getHead().getLeft(), this.getHead().getCenter().y - 500, this.getWidth(), 1000);
        randomCooldown = 0;
        dir = Direction.Right;
        target = Rect2d.EmptyRect;
    }

    @Override
    public void setHeadLoc() {
        if(AI2_RANDOM_SPAWN_TOGGLE){
        startX = (double) random_number(100, SnakePanel.getScreenWidth()-100);
        startY = (double) random_number(100, SnakePanel.getScreenHeight()-100);
        head = new Rect2d(startX, startY, snakeWidth, snakeWidth);
        this.addS(head);
        }
        else{
        head = new Rect2d(AI2_NON_RANDOM_SPAWN_X, AI2_NON_RANDOM_SPAWN_Y, snakeWidth, snakeWidth);
        this.addS(head);
        }
    }

    Rect2d scan() {// find the closest food
        double tempDist;
        int tempIndex;
        tempDist = this.findDistance(SnakePanel.food.get(0));
        tempIndex = -1;
        for (int i = 0; i < SnakePanel.food.size(); i++) {//that intesect vision
            if (tempDist >= this.findDistance(SnakePanel.food.get(i)) && vision.checkCollisions(SnakePanel.food.get(i))) {
                tempDist = this.findDistance(SnakePanel.food.get(i));
                tempIndex = i;
            }
        }
        if (tempIndex != -1) {//return the food
            return SnakePanel.food.get(tempIndex);
        }// if none in vision return "dummy" value
        return Rect2d.EmptyRect;
    }

    void pathTo() {
        this.color = PATHING_COLOR;
        //if there is nothing in vision 
        if (target == Rect2d.EmptyRect) {
            this.color = NOT_PATHING_COLOR;
            isPathing = false;
            // avoid Right side       
            if (this.getHead().getCenter().x - avoidEdgeDist >= SnakePanel.WINDOW_WIDTH) {
                if (this.dir != Direction.Right) {
                    dir = Direction.Left;
                } else {
                    dir = Direction.Down;
                }
            }
            // avoid the Bottom(Its actually the bottom)
            if (this.getHead().getCenter().y - avoidEdgeDist >= SnakePanel.WINDOW_HEIGHT) {
                if (this.dir != Direction.Down) {
                    dir = Direction.Up;
                } else {
                    dir = Direction.Right;
                }
            }
            // avoid the Top
            if (this.getHead().getCenter().y - avoidEdgeDist < 0) {
                if (this.dir != Direction.Up) {
                    dir = Direction.Down;
                } else {
                    dir = Direction.Left;
                }
            }
            // avoid the Left
            if (this.getHead().getCenter().x - avoidEdgeDist < 0) {
                if (this.dir != Direction.Left) {
                    dir = Direction.Right;
                } else {
                    dir = Direction.Up;
                }
            } else if (randomCooldown <= 0) {
                randomDirection();
                randomCooldown = (int) this.getSSize();
            }
            return;
        }

        //if in Straight path Up or Down
        if (Rect2d.intersect(this.pathY, target) != Rect2d.EmptyRect) {
            //if above...
            if (this.getHead().getTop() > target.getBottom()) {
                if (this.dir != Direction.Down) {
                    this.dir = Direction.Up;
                }
            } //if below...
            if (this.getHead().getBottom() < target.getTop()) {
                if (this.dir != Direction.Up) {
                    this.dir = Direction.Down;
                }
            }
            return;
        } //if in Straight path Left or Right
        else if (Rect2d.intersect(this.pathX, target) != Rect2d.EmptyRect) {
            //if left
            if (this.getHead().getLeft() > target.getRight()) {
                if (this.dir != Direction.Right) {
                    this.dir = Direction.Left;
                }
            } //if right
            if (this.getHead().getRight() < target.getLeft()) {
                if (this.dir != Direction.Left) {
                    this.dir = Direction.Right;
                }
            }
            return;
        } //if there is something in sight, but not straight(make it better)
        else {
            if (this.dir != Direction.Down) {
                if (this.getHead().getTop() > target.getBottom()) {
                    this.dir = Direction.Up;
                }
            } else if (this.dir != Direction.Up) {
                if (this.getHead().getBottom() < target.getTop()) {
                    this.dir = Direction.Down;
                }
            } else if (this.dir != Direction.Left) {
                if (this.getHead().getLeft() > target.getRight()) {
                    this.dir = Direction.Right;
                }
            } //if right
            else if (this.dir != Direction.Right) {
                this.dir = Direction.Left;
            }
        }
    }

    double findDistance(Rect2d target) {
        double distance;
        double x1 = this.getHead().getCenter().x;
        double y1 = this.getHead().getCenter().y;
        double x2 = target.getCenter().x;
        double y2 = target.getCenter().y;
        double temp = (Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2));
        distance = Math.sqrt(temp);
        return distance;
    }

    void randomDirection() {
        int random = random_number(0, 4);
        switch (random) {
            case 0:
                if (dir != Direction.Down) {
                    this.dir = Direction.Up;
                    break;
                }
                randomDirection();
                break;

            case 1:
                if (dir != Direction.Up) {
                    this.dir = Direction.Down;
                    break;
                }
                randomDirection();
                break;

            case 2:
                if (dir != Direction.Right) {
                    this.dir = Direction.Left;
                    break;
                }
                randomDirection();
                break;

            case 3:
                if (dir != Direction.Left) {
                    this.dir = Direction.Right;
                    break;
                }
                randomDirection();
                break;

            default:
                System.out.println("I AM ERROR");
        }
    }

    @Override
    void update() {
        if (alive == false) {
            return;
        }

        //System.out.println("s" + this.getSSize());
        double widthfactor = 1;
        for (int j = 0; j < SnakePanel.food.size(); j++) {
            if (Rect2d.intersect(SnakePanel.food.get(j), this.getHead()) != Rect2d.EmptyRect) {//when snake touches food
                //Rect2d.resolveOverlap(food.get(j), snake.get(0));
                this.addS(new Rect2d(1000, 1000.0, this.getWidth(), this.getWidth()));
                this.addH(new SquareCoords(0, 0));
                SnakePanel.food.remove(j);
                SnakePanel.food.add(new Rect2d(random_number(0, (int) SnakePanel.WINDOW_WIDTH), random_number(0, (int) SnakePanel.WINDOW_HEIGHT), 10, 10));
                SnakeGame.updateScoreboard();
                //widthfactor = this.getSSize() / 10;
                //widthfactor += 1;
                //this.setWidth(10 + (widthfactor * 5));
            }
        }

        for (int j = 1; j < this.getSSize(); j++) {
            if (Rect2d.intersect(this.getRect(j), this.getHead()) != Rect2d.EmptyRect) {//when snake touches food
                //this.die();
                //break;
            }
        }

        for (int i = 0; i < this.getSSize(); i++) {
            this.setH(i, new SquareCoords((int) this.getRect(i).getLeft(), (int) this.getRect(i).getTop()));
        }
        this.setMoving(true);

        for (int i = 0; i < this.getHSize(); i++) {
            if (i == 0) {
                target = scan();
                pathTo();
                randomCooldown--;
                if (SnakePanel.keysPressed.Random) {
                    randomDirection();
                }
            } else {
                if (this.isMoving()) {
                    this.getRect(i).moveTo(this.getH(i - 1));
                }

            }

        }

        switch (this.dir) {
            case Left:
                this.getHead().translate(-AI2_SPEED_MULTIPLIER*this.getWidth(), 0.0);
                break;

            case Right:
                this.getHead().translate(AI2_SPEED_MULTIPLIER*this.getWidth(), 0.0);
                break;

            case Down:
                this.getHead().translate(0.0, AI2_SPEED_MULTIPLIER*this.getWidth());
                break;

            case Up:
                this.getHead().translate(0.0, -AI2_SPEED_MULTIPLIER*this.getWidth());
                break;
        }

        //update vision after movement
        vision = new Rect2d(this.getHead().getCenter().x - 500, this.getHead().getCenter().y - 500, 1000, 1000);
        pathX = new Rect2d(this.getHead().getCenter().x - 500, this.getHead().getTop(), 1000, this.getWidth());
        pathY = new Rect2d(this.getHead().getLeft(), this.getHead().getCenter().y - 500, this.getWidth(), 1000);

        this.updateSize();

    }
    
      public static int random_number(int low, int high) {
        double rand = Math.random(); //generates a random number
        int rand2 = (int) (rand * 100000); //casts the random number as int
        int interval = high - low;//interval in which to put the number ie 1-100
        rand2 = rand2 % interval;//puts the number into the interval
        rand2 = rand2 + low;//acertains that the number is above the minimum
        int randNum = rand2;//assigns the random number's value
        return randNum;//returns the random number's value
    }


}
