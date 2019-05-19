import processing.core.PApplet;
public class Maze implements Renderable{
    private Wall[][] walls;
    private PApplet sketch;
    public Maze(int length, int width, PApplet sketch){
        this.sketch=sketch;
        walls = new Wall[2*length-2][];
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[width-1];
            } else {
                walls[i] = new Wall[width];
            }
        }
    }
    @Override
    public void render(){
    }
}
