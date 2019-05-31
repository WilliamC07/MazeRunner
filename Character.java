import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.KeyEvent;

public class Character implements Renderable{
    private Point location;
    private Wall[] borderWall;
    private List<Wall> allWalls;
    public boolean movingUp,movingDown,movingLeft,movingRight;
    private PApplet sketch;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        sketch = Main.getInstance();
        // TODO: update when the Maze.java is finished. I have some testing code for now
        Point a = new Point(1, 1);
        Point b = new Point(sketch.width - 2, 1);
        Point c = new Point(sketch.width - 2, sketch.height - 2);
        Point d = new Point(1, sketch.height - 2);
        this.borderWall = new Wall[]{
                new Wall(a, b),
                new Wall(a, d),
                new Wall(d, c),
                new Wall(b, c)
        };
        allWalls = new ArrayList<>(walls);
        for(Wall border : borderWall){
            allWalls.add(border);
        }
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
        float newX = sketch.constrain(location.getX()+3f*dx,sketch.width/10f+10,9*sketch.width/10f-10);
        float newY = sketch.constrain(location.getY()+3f*dy,sketch.height/10f+10,9*sketch.height/10f-10);
        location = new Point(newX,newY);
    }

    public Point getPos(){
        return location;
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
        //location = new Point(sketch.mouseX, sketch.mouseY);
        sketch.fill(255,0,0);
        sketch.ellipse(location.getX(), location.getY(), 20, 20);
        drawVision(getRays());
    }

    private List<Ray> getRays(){
        List<Ray> rays = new ArrayList<>();

        // Draw a ray to each endpoint if there is not a wall blocking the way
        for(Wall wall : allWalls){
            Ray sketchRayStart = new Ray(location, wall.getStart(), true, wall);
            Ray sketchRayEnd = new Ray(location, wall.getEnd(), true, wall);

            // If a ray cannot be drawn, do not keep track of it
            // If the ray can be drawn, keep track of it and generate the auxiliary ray
            if(!issketchRayBlocked(sketchRayStart, wall)){
                rays.add(sketchRayStart);
                sketchRayStart.setAuxiliaryRay(createAuxiliaryRay(sketchRayStart));
            }
            if(!issketchRayBlocked(sketchRayEnd, wall)){
                rays.add(sketchRayEnd);
                sketchRayEnd.setAuxiliaryRay(createAuxiliaryRay(sketchRayEnd));
            }
        }

        // counter clockwise sorting
        Collections.sort(rays);
        return rays;
    }

    private void drawVision(List<Ray> rays){
        sketch.stroke(255, 0, 0);
        for(int i = 0; i < rays.size(); i++){
            sketch.fill(255, 0, 0);
            Ray current = rays.get(i);
            Ray next = rays.get((i + 1) % rays.size());

            boolean shareWall = false;
            for(Wall wall : allWalls){
                if(wall.isPointOnWall(current.getEnd()) && wall.isPointOnWall(next.getEnd())){
                    shareWall = true;
                }
            }

            if(shareWall){
                // if the ray is drawing to the same wall, then use the sketch lines to connect
                sketch.triangle(location.getX(), location.getY(),
                                            current.getEnd().getX(), current.getEnd().getY(),
                                            next.getEnd().getX(), next.getEnd().getY());
            }else{
                Ray currentAuxiliary = current.getAuxiliaryRay();
                Ray nextAuxiliary = next.getAuxiliaryRay();

                // The rays are not drawn to the same point
                // If a ray cannot be drawn to the midpoint between the two auxiliary rays, we must connect
                // auxiliary to sketch

                Point midpointAuxAux = Point.midpoint(currentAuxiliary.getEnd(), nextAuxiliary.getEnd());
                boolean isMidpointBlocked = false;
                Ray toMidPoint = new Ray(location, midpointAuxAux, true, null);
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

                    boolean canDrawsketchAux = true;
                    // if the sketch cannot be drawn to the aux midpoint without intersecting a wall at another point other
                    // than the start of the sketch ray or the aux endpoint, it is not valid
                    Ray checkBlocking = new Ray(current.getEnd(), nextAuxiliary.getEnd(), true, null);
                    for(Wall block : allWalls){
                        Point intersection = checkBlocking.intersects(block);
                        if(intersection != null && !intersection.equals(current.getEnd()) && !intersection.equals(nextAuxiliary.getEnd())){
                            canDrawsketchAux = false;
                            break;
                        }
                    }

                    if(canDrawsketchAux){
                        sketch.triangle(location.getX(), location.getY(),
                                current.getEnd().getX(), current.getEnd().getY(),
                                nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                    }else{
                        sketch.triangle(location.getX(), location.getY(),
                                currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                                next.getEnd().getX(), next.getEnd().getY());
                    }
                }else{
                    //connecting auxiliary does work
                    sketch.triangle(location.getX(), location.getY(),
                            currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                            nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                }
            }
        }
    }


    /**
     * Checks if the ray is blocked by a wall between the start of the ray and the end of the ray.
     * @param ray Ray to check for collision with a wall other than the one it is aiming for
     * @param wallToTouch Wall the ray is attempting to collide with. Null if the ray is pointing to a point
     * @return True if the ray is blocked, false otherwise
     */
    private boolean issketchRayBlocked(Ray ray, Wall wallToTouch){
        for(Wall wall : allWalls){
            // The ray will always touch the wall it is aiming to touch
            if(wall.equals(wallToTouch)){
                continue;
            }

            Point intersect = ray.intersects(wall);
            // Make sure there is an intersection and
            // the intersection is not the point the ray ends at
            if(intersect != null && !intersect.equals(ray.getEnd())){
                return true;
            }
        }
        return false;
    }

    private Ray createAuxiliaryRay(Ray sketchRay){
        // if the may ray is drawn to the border wall, the auxiliary ray is itself
        for(Wall borderWall : borderWall){
            if(borderWall.isPointOnWall(sketchRay.getEnd())){
                return sketchRay;
            }
        }
        // sketchRay.getEnd() gives direction of the geometric ray
        Ray auxiliary = new Ray(sketchRay.getStart(), sketchRay.getEnd(), false, null);
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
                Ray startToCollision = new Ray(auxiliary.getStart(), collisionPoint, true, null);
                if(startToCollision.intersects(blockingWall) == null){
                    continue;
                }

                isAuxBlocked = true;
                break;
            }

            if(!isAuxBlocked){
                return new Ray(sketchRay.getStart(), collisionPoint, true, collideWall);
            }
        }
        return sketchRay;
    }
}
