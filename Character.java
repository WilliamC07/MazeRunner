import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private List<Ray> rays;
    private Point[] borderPoints;

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

            if(toStart != null){
                rays.add(toStart);
            }
            if(toEnd != null){
                rays.add(toEnd);
            }
        }

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
