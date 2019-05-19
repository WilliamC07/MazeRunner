import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private List<Ray> rays;
    private Point[] borderPoints;
    private Wall[] borderWall;
    private List<Wall> allWalls;
    /**
     * When we construct a ray from the the character location to a wall endpoint, we need to generate two other rays
     * that is +/- this value.
     */
    private final float SLOPE_DELTA = .001F;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        this.walls = walls;
        this.rays = new ArrayList<>();
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
        findIntersectionPoints();
        rays.forEach(Ray::render);
    }

    private void findIntersectionPoints(){
        rays.clear();
        // n^2 algorithm yikes
        // find intersections for walls in the maze
        for(Wall wall : walls){
            // draw a ray to start and end point
            Ray toStart = new Ray(location, wall.getStart());
            Ray toEnd = new Ray(location, wall.getEnd());

            // if the ray will intersect another wall, ignore it
            for(Wall wallCheck : walls){
                if(wallCheck != wall){
                    if(toStart != null && toStart.intersects(wallCheck) != null){
                        toStart = null;
                    }
                    if(toEnd != null && toEnd.intersects(wallCheck) != null){
                        toEnd = null;
                    }
                }
            }

            List<Ray> additionalRays = new ArrayList<>(4);
            if(toStart != null){
                rays.add(toStart);
                float slope = location.slope(toStart.getEnd());
                additionalRays.add(new Ray(location, slope + SLOPE_DELTA));
                additionalRays.add(new Ray(location, slope - SLOPE_DELTA));
            }
            if(toEnd != null){
                rays.add(toEnd);
                float slope = location.slope(toEnd.getEnd());
                additionalRays.add(new Ray(location, slope + SLOPE_DELTA));
                additionalRays.add(new Ray(location, slope - SLOPE_DELTA));
            }

            // find where the additional ray ends, we need to include the border walls now
            for(Ray ray : additionalRays){
                // find where the ray intersects another wall
                for(Wall wallCollision : allWalls){
                    Point collision = ray.intersects(wallCollision);
                    if(collision != null){
                        // make sure that there is no other wall blocking the way
                        boolean valid = true;
                        for(Wall wallCheck : allWalls){
                            if(wallCheck != wallCollision){
                                Point collisionCheck = ray.intersects(wallCheck);
                                if(collisionCheck != null){
                                    valid = false;
                                    break;
                                }
                            }
                        }

                        //if there is no wall blocking add the ray to the view
                        if(valid){
                            rays.add(new Ray(location, collision));
                        }
                    }
                }
            }
        }

        // Draw rays to corner of screen to complete ray casting
        for(Point borderPoint : borderPoints){
            Ray toBorderPoint = new Ray(location, borderPoint);
            for(Wall wallCheck : walls){
                if(toBorderPoint != null && toBorderPoint.intersects(wallCheck) != null){
                    toBorderPoint = null;
                }
            }
            if(toBorderPoint != null){
                rays.add(toBorderPoint);
            }
        }
    }
}
