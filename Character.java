import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Character implements Renderable{
    private Point location;
    private List<Wall> walls;
    private List<Point> intersectionPoints;

    public Character(Point location, List<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        this.walls = walls;
        this.intersectionPoints = new ArrayList<>();
    }

    public void move(Point newLocation){
        this.location = newLocation;
    }

    @Override
    public void render(){
        Main.getInstance().ellipse(200, 200, 20, 20);
    }

    private void findIntersectionPoints(){

    }
}
