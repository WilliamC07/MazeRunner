import java.util.*;
public class Monster extends Character{
    private ArrayList<Cell> path;
    private int x,y;
    public Monster(int x, int y){
        this.x=x;
        this.y=y;
    }
}