import processing.core.PImage;

public class EndScreen implements Renderable{
    private final Main main;
    private EndType endType;
    private final PImage wonImage, lostImage, newGameImage;
    private final float centerX;
    private final float titleY;
    private final float newGameY;
    private final float newGameWidth, newGameHeight;

    public EndScreen(){
        this.main = Main.getInstance();
        this.centerX = Main.getInstance().width / 2;
        this.titleY = Main.getInstance().height * .3F;
        this.newGameY = Main.getInstance().height * .6F;
        this.newGameHeight = Main.getInstance().height * .1F;
        this.newGameWidth = Main.getInstance().width * .5F;

        this.wonImage = main.loadImage("won.png");
        this.lostImage = main.loadImage("lost.png");
        this.newGameImage = main.loadImage("new_game.png");
    }

    public void render(){
        main.imageMode(main.CENTER);

        switch (endType) {
            case WIN:
                main.image(wonImage, centerX, titleY);
                break;
            case LOSE:
                main.image(lostImage, centerX, titleY);
                break;
        }

        // new game button
        main.rectMode(main.CENTER);
        main.fill(33, 136, 56);
        main.rect(centerX, newGameY, newGameWidth, newGameHeight);
        main.image(newGameImage, centerX, newGameY);
    }

    public void show(EndType endType){
        this.endType = endType;
    }

    public void click(){
        if(main.mouseX >= centerX - newGameWidth / 2 && main.mouseX <= centerX + newGameWidth / 2 &&
           main.mouseY >= newGameY - newGameHeight / 2 && main.mouseY <= newGameY + newGameHeight / 2){
            main.bringToTitleScreen();
        }
    }

    public enum EndType{
        WIN,
        LOSE
    }
}
