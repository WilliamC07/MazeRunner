import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private Wall[] borderWall;
    private List<Wall> allWalls;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        Main main = Main.getInstance();
        Point a = new Point(1, 1);
        Point b = new Point(main.width - 2, 1);
        Point c = new Point(main.width - 2, main.height - 2);
        Point d = new Point(1, main.height - 2);
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

    public void move(float dx, float dy){
        location = new Point(location.getX()+dx,location.getY()+dy);
    }

    public Point getPos(){
        return location;
    }

    @Override
    public void render(){
        location = new Point(Main.getInstance().mouseX, Main.getInstance().mouseY);
        Main.getInstance().ellipse(location.getX(), location.getY(), 20, 20);
        drawVision(getRays());
    }

    private List<Ray> getRays(){
        List<Ray> rays = new ArrayList<>();

        // Draw a ray to each endpoint if there is not a wall blocking the way
        for(Wall wall : allWalls){
            Ray mainRayStart = new Ray(location, wall.getStart(), true, wall);
            Ray mainRayEnd = new Ray(location, wall.getEnd(), true, wall);

            // If a ray cannot be drawn, do not keep track of it
            // If the ray can be drawn, keep track of it and generate the auxiliary ray
            if(!isMainRayBlocked(mainRayStart, wall)){
                rays.add(mainRayStart);
                mainRayStart.setAuxiliaryRay(createAuxiliaryRay(mainRayStart));
            }
            if(!isMainRayBlocked(mainRayEnd, wall)){
                rays.add(mainRayEnd);
                mainRayEnd.setAuxiliaryRay(createAuxiliaryRay(mainRayEnd));
            }
        }

        // counter clockwise sorting
        Collections.sort(rays);
        return rays;
    }

    private void drawVision(List<Ray> rays){
        Main.getInstance().stroke(255, 0, 0);
        for(int i = 0; i < rays.size(); i++){
            Main.getInstance().fill(255, 0, 0);
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
                Main.getInstance().triangle(location.getX(), location.getY(),
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
                Ray toMidPoint = new Ray(location, midpointAuxAux, true, null);
                for(Wall blockCheck : walls){
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
                    Ray checkBlocking = new Ray(current.getEnd(), nextAuxiliary.getEnd(), true, null);
                    for(Wall block : allWalls){
                        Point intersection = checkBlocking.intersects(block);
                        if(intersection != null && !intersection.equals(current.getEnd()) && !intersection.equals(nextAuxiliary.getEnd())){
                            canDrawMainAux = false;
                            break;
                        }
                    }

                    if(canDrawMainAux){
                        Main.getInstance().triangle(location.getX(), location.getY(),
                                current.getEnd().getX(), current.getEnd().getY(),
                                nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                    }else{
                        Main.getInstance().triangle(location.getX(), location.getY(),
                                currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                                next.getEnd().getX(), next.getEnd().getY());
                    }
                }else{
                    //connecting auxiliary does work
                    Main.getInstance().triangle(location.getX(), location.getY(),
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
    private boolean isMainRayBlocked(Ray ray, Wall wallToTouch){
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

    private Ray createAuxiliaryRay(Ray mainRay){
        // if the may ray is drawn to the border wall, the auxiliary ray is itself
        for(Wall borderWall : borderWall){
            if(borderWall.isPointOnWall(mainRay.getEnd())){
                return mainRay;
            }
        }
        // mainRay.getEnd() gives direction of the geometric ray
        Ray auxiliary = new Ray(mainRay.getStart(), mainRay.getEnd(), false, null);
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
                return new Ray(mainRay.getStart(), collisionPoint, true, collideWall);
            }
        }
        throw new IllegalStateException("Cannot generate auxiliary ray");
    }
}
