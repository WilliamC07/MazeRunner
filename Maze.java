import processing.core.PApplet;
public class Maze implements Renderable{
    private Wall[][] walls;
    private float width;
    private float height;
    public Maze(int length, int width, PApplet sketch){
        width = sketch.width;
        height = sketch.height;
        walls = new Wall[2*length-2][];
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[width-1];
            } else {
                walls[i] = new Wall[width];
            }
        }
        walls[0][0] = new Wall(new Point(width/3,height/3),new Point(2*width/3,2*height/3));
    }
    @Override
    public void render(){
        for(Wall[] row : walls){
            for(Wall wall : row){
                if(wall!=null){
                    wall.render();
                }
            }
        }
    }
}
