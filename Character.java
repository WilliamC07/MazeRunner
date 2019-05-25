import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private Point[] borderPoints;
    private Wall[] borderWall;
    private List<Wall> allWalls;
    /**
     * When we construct a ray from the the character location to a wall endpoint, we need to generate two other rays
     * that is +/- this value.
     */
    private final float SLOPE_DELTA = .01F;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        this.walls = walls;
        Main main = Main.getInstance();
        this.borderPoints = new Point[]{
                new Point(0, 0),
                new Point(0, main.height),
                new Point(main.width, 0),
                new Point(main.width, main.height)
        };

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
        List<Ray> rays = getRays();
        rays.forEach(Ray::render);
//        drawVision(rays);

//        Point a = new Point(20, 20);
//        Point b = new Point(20, 100); // shared
//        Point c = new Point(200, 100);
//        Main main = Main.getInstance();
//        Ray ray = new Ray(new Point(main.mouseX, main.mouseY), b, true, allWalls.get(0));
//        Ray aux = createAuxiliaryRay(ray, allWalls.get(0));
//        ray.setAuxiliaryRay(aux);
//        ray.render();
//        //aux.render();
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
            Main.getInstance().fill(i * 10, 0, 0);
            Ray current = rays.get(i);
            Ray next = rays.get((i + 1) % rays.size());

            boolean shareWall = false;
            for(Wall wall : allWalls){
                if(wall.isAEndPoint(current.getEnd()) && wall.isAEndPoint(next.getEnd())){
                    shareWall = true;
                }
            }

            if(shareWall){
                // if the ray is drawing to the same wall, then use the main lines to connect
                Main.getInstance().triangle(location.getX(), location.getY(),
                                            current.getEnd().getX(), current.getEnd().getY(),
                                            next.getEnd().getX(), next.getEnd().getY());
                //Main.getInstance().fill(0, 0, 255);
                //Main.getInstance().text("same", current.getEnd().getX(), current.getEnd().getY());
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
                    if(intersection != null && !blockCheck.equals(currentAuxiliary.getPointOf()) && !blockCheck.equals(nextAuxiliary.getPointOf()) && !blockCheck.isBetweenTwoPoints(currentAuxiliary.getEnd(), nextAuxiliary.getEnd())){
                        isMidpointBlocked = true;
                        break;
                    }
                }


                if(!isRayToBorder(current) && !isRayToBorder(next) && isMidpointBlocked){
                    // Cannot connect the aux
                    // if the main can be drawn to the aux, then it is valid
                    Ray mainToAux = new Ray(current.getEnd(), nextAuxiliary.getEnd(), true, null);
                    boolean canDrawMainToAux = true;
                    // check if the ray can be drawn without intersecting the
                    for(Wall intersect : allWalls){
                        Point intersectionPoint = mainToAux.intersects(intersect);
                        if(intersectionPoint != null && !intersectionPoint.equals(current.getEnd()) && !intersectionPoint.equals(mainToAux.getEnd())){
                            canDrawMainToAux = false;
                        }
                    }

                    // make a ray can be drawn out from the character's location to the midpoint
                    Point midpointMainAux = Point.midpoint(current.getEnd(), nextAuxiliary.getEnd());
                    Ray testMainAux = new Ray(location, midpointMainAux, true, null);
                    if(canDrawMainToAux && !isMainRayBlocked(testMainAux, null)){
                        Main.getInstance().triangle(location.getX(), location.getY(),
                                current.getEnd().getX(), current.getEnd().getY(),
                                nextAuxiliary.getEnd().getX(), nextAuxiliary.getEnd().getY());
                    }else{
                        Main.getInstance().triangle(location.getX(), location.getY(),
                                currentAuxiliary.getEnd().getX(), currentAuxiliary.getEnd().getY(),
                                next.getEnd().getX(), next.getEnd().getY());
                    }
                }else{
                    // connecting auxiliary does work
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

    /**
     * Determines if the given ray is drawn to a border edge.
     * @param ray A main ray to check if it is drawn to the edge of a border
     * @return True if the ray is drawn to a border endpoint, false otherwise.
     */
    private boolean isRayToBorder(Ray ray){
        for(Wall border : borderWall){
            if(border.isAEndPoint(ray.getEnd())){
                return true;
            }
        }
        return false;
    }

    private Ray createAuxiliaryRay(Ray mainRay){
        // if the may ray is drawn to the border wall, the auxiliary ray is itself
        for(Wall borderWall : borderWall){
            if(borderWall.equals(mainRay.getPointOf())){
                return mainRay;
            }
        }
        for(Wall collideWith : allWalls){
            if(!collideWith.equals(mainRay.getPointOf())){
                Ray auxiliaryRay = new Ray(location, mainRay.getEnd(), false, null);
                Point intersection = auxiliaryRay.intersects(collideWith);
                if(intersection != null && !intersection.equals(mainRay.getEnd())){
                    boolean isBlocked = false;
                    for(Wall block : allWalls){
                        Point blockingPoint = auxiliaryRay.intersects(block);
                        if(!block.equals(collideWith) && !block.equals(mainRay.getPointOf()) &&
                                blockingPoint != null && !blockingPoint.equals(intersection) &&
                                !blockingPoint.equals(mainRay.getEnd())){
                            // make sure a ray drawn to blocking wall is not blocked by the collide with
                            Ray checkActualCollision = new Ray(location, blockingPoint, true, null);
                            if(checkActualCollision.intersects(collideWith) != null){
                                continue;
                            }

                            isBlocked = true;
                            break;
                        }
                    }
                    if(!isBlocked){
                        auxiliaryRay.setEnd(intersection);
                        auxiliaryRay.setPointOf(collideWith);
                        return auxiliaryRay;
                    }
                }
            }
        }
        System.out.println("Main ray end point: " + mainRay.getEnd());
        System.out.println("Main ray wall to " + mainRay.getPointOf());
        return null;
        //throw new IllegalStateException("Cannot generate auxiliary ray");
    }
}
