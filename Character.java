import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.awt.event.KeyEvent;

public class Character implements Renderable{
    /**
     * The player does not leave the center of the screen. The map moves around the character. This gives the impression
     * that the character is moving around the map.
     */
    private final Point centerOfScreen;
    /**
     * This is the player's location in the matrix accounted for the scaling and offset. This is not the index into
     * the matrix.
     */
    private Point locationInMatrix;
    private Set<Point> verticies;
    private List<Wall> allWalls;
    public boolean movingUp,movingDown,movingLeft,movingRight;
    private Main sketch;
    private final Maze maze;

    public Character(Maze maze){
        sketch = Main.getInstance();
        this.maze = maze;
        this.centerOfScreen = new Point(sketch.width / 2, sketch.height / 2);
        maze.refresh(1, 1, Maze.WALL_SCALE, Maze.WALL_SCALE);
        // start off at the top left of the maze
        this.locationInMatrix = new Point(Maze.WALL_SCALE / 2, Maze.WALL_SCALE / 2); // temp
    }

    public void move(){
        int dx = 0;
        int dy = 0;
        if(movingLeft){
            dx-=1;
        }
        if(movingRight){
            dx+=1;
        }
        if(movingUp){
            dy-=1;
        }
        if(movingDown){
            dy+=1;
        }
        float scalar = sketch.isGodMode()? 7:3;
        boolean skipX = false;
        boolean skipY = false;
        if(!sketch.isGodMode()){
            float newX = centerOfScreen.getX()+scalar*dx+dx*10;
            float newY = centerOfScreen.getY()+scalar*dy+dy*10;
            Point newLocation = new Point(newX,newY);
            Ray movement = new Ray(centerOfScreen, newLocation, true);
            for(Wall blocking : allWalls){
                Point intersection = movement.intersects(blocking);
                if(intersection != null){
                    return;
                }
            }

            // skip horizontal and vertical values so we don't get the bug ¯\_(ツ)_/¯. Those 20+ hours on ray casting isn't worth it
            for(Wall wall : allWalls){
                if(Math.abs(wall.getStart().getX() - newX) <= .1F){
                    skipX = true;
                }

                if(Math.abs(wall.getStart().getY() - newY) <= .1F){
                    skipY = true;
                }
            }
        }


        locationInMatrix = new Point(locationInMatrix.getX()+scalar*dx + (skipX ? .5F : 0), locationInMatrix.getY()+scalar*dy + (skipY ? .5F : 0));
        int[] currentCell = Maze.getMatrixPoint(locationInMatrix);
        int[] previousCell = maze.getPathKeeper();
        if(Math.abs(currentCell[0]/2-previousCell[0])==1 ^ Math.abs(currentCell[1]/2-previousCell[1])==1){
            maze.updateCell(currentCell[0]/2,currentCell[1]/2);
        }
    }

    public Point getPos(){
        return locationInMatrix;
    }

    public void setVelocity(int key, boolean toggle){
        switch(key){
            case 'W':
            case 'w':
            case KeyEvent.VK_UP:
                movingUp = toggle;
                break;
            case 'S':
            case 's':
            case KeyEvent.VK_DOWN:
                movingDown = toggle;
                break;
            case 'A':
            case 'a':
            case KeyEvent.VK_LEFT:
                movingLeft = toggle;
                break;
            case 'D':
            case 'd':
            case KeyEvent.VK_RIGHT:
                movingRight = toggle;
                break;
        }
    }

    @Override
    public void render(){
        // find offset for the maze, which is the distance to the center
        float offsetX = centerOfScreen.getX() - locationInMatrix.getX();
        float offsetY = centerOfScreen.getY() - locationInMatrix.getY();
        maze.setOffsetX(offsetX);
        maze.setOffsetY(offsetY);
        maze.refresh(offsetX, offsetY, Maze.WALL_SCALE, Maze.WALL_SCALE);
        this.verticies = maze.verticies(offsetX, offsetY);
        this.allWalls = maze.getWalls();

        this.allWalls = maze.getWalls();
        if(!sketch.isGodMode()){
            drawVision(getRays());
        }
        sketch.fill(255,0,0);
        sketch.getInstance().noStroke();
        sketch.ellipse(centerOfScreen.getX(), centerOfScreen.getY(), 20, 20);

        if(didWin()){
            sketch.endGame(EndScreen.EndType.WIN);
        }else if(didLose()){
            sketch.endGame(EndScreen.EndType.LOSE);
        }
    }

    private List<Ray> getRays(){
        List<Ray> rays = new ArrayList<>();

        // Draw a ray to each endpoint if there is not a wall blocking the way
        for(Point vertex : verticies){
            Ray mainRay = new Ray(centerOfScreen, vertex, true);
            if(!isMainRayBlocked(mainRay)){
                mainRay.setAuxiliaryRay(createAuxiliaryRay(mainRay));
                rays.add(mainRay);
            }
        }
        // counter clockwise sorting
        Collections.sort(rays);
        return rays;
    }

    private void drawVision(List<Ray> rays){
        sketch.stroke(255);
        for(int i = 0; i < rays.size(); i++){
            Main.getInstance().fill(255);
            Ray current = rays.get(i);
            Ray next = rays.get((i + 1) % rays.size());

            boolean shareWall = false;
            for(Wall wall : allWalls){
                if(wall.isPointOnWall(current.getEnd()) && wall.isPointOnWall(next.getEnd())){
                    shareWall = true;
                }
            }

            if(shareWall){
                // if the ray is drawing to the same wall, then use the main lines to connect
                Main.getInstance().triangle(centerOfScreen.getX(), centerOfScreen.getY(),
                        current.getEnd().getX(), current.getEnd().getY(),
                        next.getEnd().getX(), next.getEnd().getY());
            }else{
                Ray currentAuxiliary = current.getAuxiliaryRay();
                Ray nextAuxiliary = next.getAuxiliaryRay();

                // The rays are not drawn to the same point
                // If a ray cannot be drawn to the midpoint between the two auxiliary rays, we must connect
                // auxiliary to main

                Point midpointAuxAux = Point.midpoint(currentAuxiliary.getEnd(), nextAuxiliary.getEnd());
                boolean isMidpointBlocked = false;
                Ray toMidPoint = new Ray(centerOfScreen, midpointAuxAux, true);
                for(Wall blockCheck : allWalls){
                    Point intersection = toMidPoint.intersects(blockCheck);
                    // make sure the wall is not between the two line segment
                    if(intersection != null &&
                            !intersection.equals(midpointAuxAux)){
                        isMidpointBlocked = true;
                        break;
                    }
                }

                if(isMidpointBlocked){
                    // Cannot connect the aux

                    boolean canDrawMainAux = true;
                    // if the main cannot be drawn to the aux midpoint without intersecting a wall at another point other
                    // than the start of the main ray or the aux endpoint, it is not valid
                    Ray checkBlockingMainAux = new Ray(current.getEnd(), nextAuxiliary.getEnd(), true);
                    for(Wall block : allWalls){
                        Point intersection = checkBlockingMainAux.intersects(block);
                        if(intersection != null && !intersection.equals(current.getEnd()) && !intersection.equals(nextAuxiliary.getEnd())){
                            canDrawMainAux = false;
                            break;
                        }
                    }

                    if(canDrawMainAux){
                        Main.getInstance().triangle(centerOfScreen.getX(), centerOfScreen.getY(),
                                current.getEnd().getX(), current.getEnd().getY(),
                                nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                    }else{
                        // make sure the auxiliary to main can be drawn
                        boolean canDrawAuxMain = true;
                        Ray checkBlockingAuxMain = new Ray(currentAuxiliary.getEnd(), next.getEnd(), true);
                        for(Wall block : allWalls){
                            Point intersection = checkBlockingAuxMain.intersects(block);
                            if(intersection != null && !intersection.equals(currentAuxiliary.getEnd()) && !intersection.equals(next.getEnd())){
                                canDrawAuxMain = false;
                                break;
                            }
                        }

                        if(canDrawAuxMain){
                            Main.getInstance().triangle(centerOfScreen.getX(), centerOfScreen.getY(),
                                    currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                                    next.getEnd().getX(), next.getEnd().getY());
                        }else{
                            Main.getInstance().triangle(centerOfScreen.getX(), centerOfScreen.getY(),
                                    currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                                    nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                        }
                    }
                }else{
                    //connecting auxiliary does work
                    Main.getInstance().triangle(centerOfScreen.getX(), centerOfScreen.getY(),
                            currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                            nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                }
            }
        }
    }

    /**
     * Checks if the ray is blocked by a wall between the start of the ray and the end of the ray.
     * @param ray Ray to check for collision with a wall other than the one it is aiming for
     * @return True if the ray is blocked, false otherwise
     */
    private boolean isMainRayBlocked(Ray ray){
        for(Wall wall : allWalls){
            // The ray will always touch the wall it is aiming to touch

            Point intersect = ray.intersects(wall);
            // Make sure there is an intersection and
            // the intersection is not the point the ray ends at
            if(intersect != null && !intersect.equals(ray.getEnd())){
                return true;
            }
        }
        return false;
    }

    private Ray createAuxiliaryRay(Ray mainRay){
        // mainRay.getEnd() gives direction of the geometric ray
        Ray auxiliary = new Ray(mainRay.getStart(), mainRay.getEnd(), false);
        for(Wall collideWall : allWalls){
            Point collisionPoint = auxiliary.intersects(collideWall);
            // make sure there is an intersection and the collision point is not the directional point (since directional
            // point is a collision point itself, but a trivial one)
            if(collisionPoint == null || collisionPoint.equals(auxiliary.getEnd())){
                continue;
            }

            // Make sure there is no wall blocking the auxiliary to its destination point
            boolean isAuxBlocked = false;
            for(Wall blockingWall : allWalls){
                Point blockingPoint  = auxiliary.intersects(blockingWall);
                // make sure there is an intersection and
                // the intersection is not the directional part of the aux ray (which is a trivial intersection) and
                // the intersection is not the intersection from the wall we want to collide with and
                // the intersection is not where the aux ray starts
                if(blockingPoint == null || blockingPoint.equals(auxiliary.getEnd()) ||
                        blockingPoint.equals(collisionPoint) || blockingPoint.equals(auxiliary.getStart())){
                    continue;
                }

                // make sure the blocking wall is not blocked by the collision wall
                Ray startToCollision = new Ray(auxiliary.getStart(), collisionPoint, true);
                if(startToCollision.intersects(blockingWall) == null){
                    continue;
                }

                isAuxBlocked = true;
                break;
            }

            if(!isAuxBlocked){
                return new Ray(mainRay.getStart(), collisionPoint, true);
            }
        }
        return mainRay;
    }

    public boolean canSeeCharacter(Character character1, Character character2){
        Point thisLocation;
        Point otherLocation;
        if(character1 instanceof Monster){
            thisLocation = ((Monster) character1).location();
        }else{
            thisLocation = character1.centerOfScreen;
        }

        if(character2 instanceof Monster){
            otherLocation = ((Monster) character2).location();
        }else{
            otherLocation = character2.centerOfScreen;
        }

        Ray vision = new Ray(thisLocation, otherLocation, true);
        sketch.line(thisLocation.getX(), thisLocation.getY(), otherLocation.getX(), otherLocation.getY());
        for(Wall wall : allWalls){
            Point intersection = vision.intersects(wall);
            if(intersection != null){
                return false;
            }
        }

        return true;
    }

    public boolean didWin(){
        int[] curPos = Maze.getMatrixPoint(locationInMatrix);
        return curPos[1]/2==maze.getLength()-1 && curPos[0]/2==maze.getWidth()-1;
    }

    public boolean onTopOfPlayer(){
        return true;
    }

    public boolean didLose(){
        if(!sketch.isGodMode()){
            int[] curPos = Maze.getMatrixPoint(locationInMatrix);
            List<Monster> monsters = new ArrayList<Monster>();
            for(int i = 1; i<sketch.getMovables().size(); i++){
                if(sketch.getMovables().get(i).onTopOfPlayer()){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
