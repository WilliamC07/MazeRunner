import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private List<Ray> rays;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        this.walls = walls;
        this.rays = new ArrayList<>();
    }

    public void move(Point newLocation){
        this.location = newLocation;
    }

    @Override
    public void render(){
        Main.getInstance().ellipse(location.getX(), location.getY(), 20, 20);
        findIntersectionPoints();
        rays.forEach(Ray::render);
    }

    private void findIntersectionPoints(){
        // n^2 algorithm yikes

        for(Wall wall : walls){
            // draw a ray to start and end point
            Ray toStart = new Ray(location, wall.getStart());
            Ray toEnd = new Ray(location, wall.getEnd());

            // if the ray will intersect another wall, ignore it
            for(Wall wallCheck : walls){
                if(wallCheck != wall){
                    if(toStart.intersects(wallCheck) != null){
                        toStart = null;
                    }
                    if(toEnd.intersects(wallCheck) != null){
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
    }
}
