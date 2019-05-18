import java.util.List;
import java.util.Set;

public class Character implements Renderable{
    private Point location;
    private Set<Wall> walls;
    private List<Point> intersectionPoints;

    public Character(Point location, Set<Wall> walls){
        this.location = location;
        // TODO: update when the Maze.java is finished. I have some testing code for now
        this.walls = walls;
    }

    public void move(Point newLocation){
        this.location = newLocation;
    }

    @Override
    public void render(){
        Main.getInstance().ellipse(200, 200, 20, 20);
    }
}
