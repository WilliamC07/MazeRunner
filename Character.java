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
            border.render();
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
        for(int i = 0; i < rays.size(); i++){
            Point current = rays.get(i).getEnd();
            Point next = rays.get((i + 1) % rays.size()).getEnd();
            Main.getInstance().fill(0);
            Main.getInstance().triangle(location.getX(), location.getY(), current.getX(), current.getY(), next.getX(), next.getY());
        }
    }

    /**
     * Finds all the rays that determines the extend of the user's vision.
     * @return Sorted list by slope of all the rays the user can see
     */
    private List<Ray> getRays(){
        List<Ray> rays = new ArrayList<>();
        // n^2 algorithm yikes
        // find intersections for walls in the maze
        for(Wall wall : walls){
            // draw a ray to start and end point
            Ray toStart = new Ray(location, wall.getStart());
            Ray toEnd = new Ray(location, wall.getEnd());

            List<Ray> additionalRays = new ArrayList<>(2);
            if(!isBlocked(toStart, wall)){
                rays.add(toStart);
                additionalRays.add(new Ray(location, wall.getStart(), wall));
            }
            if(!isBlocked(toEnd, wall)){
                rays.add(toEnd);
                additionalRays.add(new Ray(location, wall.getEnd(), wall));
            }

            // find where the additional ray ends, we need to include the border walls now
            for(Ray ray : additionalRays){
                // find where the ray intersects another wall
                for(Wall wallCollision : allWalls){
                    Point collision = ray.intersects(wallCollision);
                    if(wall != wallCollision && collision != null){
                        // make sure that there is no other wall blocking the way
                        boolean isBlocked = false;
                        for(Wall wallCheck : walls){
                            if(wallCollision != wallCheck && wall != wallCheck && ray.intersects(wallCheck) != null){
                                isBlocked = true;
                                break;
                            }
                        }
                        if(!isBlocked){
                            rays.add(new Ray(location, ray.getEnd(), collision));
                        }
                    }
                }
            }
        }

        // Draw rays to corner of screen to complete ray casting
        for(Point borderPoint : borderPoints){
            Ray toBorderPoint = new Ray(location, borderPoint);
            if(!isBlocked(toBorderPoint, null)){
                rays.add(toBorderPoint);
            }
        }

        Collections.sort(rays);
        return rays;
    }

    /**
     * Checks if the ray is blocked by a wall between the start of the ray and the end of the ray.
     * @param ray Ray to check for collision with a wall other than the one it is aiming for
     * @param wallToTouch Wall the ray is attempting to collide with. Null if the ray is pointing to a point
     * @return True if the ray is blocked, false otherwise
     */
    private boolean isBlocked(Ray ray, Wall wallToTouch){
        for(Wall wall : walls){
            if(wall != wallToTouch && ray.intersects(wall) != null){
                return true;
            }
        }
        return false;
    }
}
