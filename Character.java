import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        this.borderWall = new Wall[]{
                new Wall(new Point(1, 1), new Point(main.width - 1, 1)),
                new Wall(new Point(1, 1), new Point(1, main.height - 1)),
                new Wall(new Point(1, main.height-1), new Point(main.width -1 , main.height-1)),
                new Wall(new Point(main.width-1, 1), new Point(main.width-1, main.height-1))
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
        Main.getInstance().ellipse(location.getX(), location.getY(), 20, 20);
        List<Ray> rays = getRays();
        rays.forEach(Ray::render);
        allWalls.forEach(Wall::render);
//        for(int i = 0; i < rays.size(); i++){
//            Point current = rays.get(i).getEnd();
//            Point next = rays.get((i + 1) % rays.size()).getEnd();
//            Main.getInstance().fill(0);
//            Main.getInstance().triangle(location.getX(), location.getY(), current.getX(), current.getY(), next.getX(), next.getY());
//        }
    }

    private List<Ray> getRays(){
        List<Ray> rays = new ArrayList<>();

        // Draw a ray to each endpoint if there is not a wall blocking the way
        System.out.println(allWalls.size());
        for(Wall wall : allWalls){
            Ray mainRayStart = new Ray(location, wall.getStart(), true, wall);
            Ray mainRayEnd = new Ray(location, wall.getEnd(), true, wall);

            // If a ray cannot be drawn, do not keep track of it
            // If the ray can be drawn, keep track of it and generate the auxiliary ray
            if(!isBlocked(mainRayStart, wall)){
                rays.add(mainRayStart);
                mainRayStart.setAuxiliaryRay(createAuxiliaryRay(mainRayStart, wall));
            }
            if(!isBlocked(mainRayEnd, wall)){
                rays.add(mainRayEnd);
                mainRayEnd.setAuxiliaryRay(createAuxiliaryRay(mainRayEnd, wall));
            }
        }

        return rays;
    }

    /**
     * Checks if the ray is blocked by a wall between the start of the ray and the end of the ray.
     * @param ray Ray to check for collision with a wall other than the one it is aiming for
     * @param wallToTouch Wall the ray is attempting to collide with. Null if the ray is pointing to a point
     * @return True if the ray is blocked, false otherwise
     */
    private boolean isBlocked(Ray ray, Wall wallToTouch){
        for(Wall wall : allWalls){
            Point intersect = ray.intersects(wall);
            if(!wall.equals(wallToTouch) && intersect != null && !intersect.equals(wallToTouch.getStart()) && !intersect.equals(wallToTouch.getEnd())){
                return true;
            }
        }
        return false;
    }

    private Ray createAuxiliaryRay(Ray mainRay, Wall wallToIgnore){
        // if the may ray is drawn to the border wall, the auxiliary ray is itself
        for(Wall borderWall : borderWall){
            if(borderWall.equals(mainRay.getPointOf())){
                return mainRay;
            }
        }

        for(Wall collideWith : allWalls){
            if(!collideWith.equals(wallToIgnore)){
                Ray auxiliaryRay = new Ray(location, mainRay.getEnd(), false, null);
                Point intersection = auxiliaryRay.intersects(collideWith);
                boolean isBlocked = false;
                for(Wall block : allWalls){
                    if(!block.equals(collideWith) && !block.equals(wallToIgnore) && auxiliaryRay.intersects(block) != null &&
                       !block.shareCommonEnd(collideWith) && !block.shareCommonEnd(wallToIgnore)){
                        System.out.println("blocked at " + auxiliaryRay.intersects(block));
                        isBlocked = true;
                    }
                }
                if(intersection != null && !isBlocked){
                    auxiliaryRay.setEnd(intersection);
                    return auxiliaryRay;
                }
            }
        }
        //return null;
        System.out.println(wallToIgnore);
        throw new IllegalStateException("Cannot generate auxiliary ray");
    }
}
