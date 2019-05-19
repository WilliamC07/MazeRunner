import processing.core.PApplet;
public class Maze implements Renderable{
    private Wall[][] walls;
    private float width;
    private float height;
    public Maze(int rows, int cols, PApplet sketch){
        width = sketch.width;
        height = sketch.height;
        walls = new Wall[2*rows-1][];
        float rowWidth = 4*height/5/rows;
        float colWidth = 4*width/5/cols;
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[rows-1];

            } else {
                walls[i] = new Wall[rows];
            }
            for(int j = 0; j<rows-(i%2==0? 1:0); j++){
                float startX,startY,endX,endY;
                if(i%2==0){
                    startX = width/10+(j+1)*colWidth;
                    startY = height/10+(i/2)*rowWidth+1;
                    endX = startX;
                    endY = height/10+(i/2+1)*rowWidth-1;
                } else {
                    startX = width/10+j*colWidth+1;
                    startY = height/10+(i/2+1)*rowWidth;
                    endX = width/10+(j+1)*colWidth-1;
                    endY = startY;
                }
                Point start = new Point(startX,startY);
                Point end = new Point(endX,endY);
                walls[i][j] = new Wall(start,end);
            }
        }
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
