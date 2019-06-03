import processing.core.PImage;

public class TitleScreen implements Renderable{
    private StringBuilder stringBuilder = new StringBuilder();
    private final Main main = Main.getInstance();
    private Mode mode;
    private final PImage titleImage, whatIsThisImage, playImage;
    private final float centerX = main.width / 2;
    private float titleImageY = .2F * main.height;
    private float whatIsThisButtonY = .40F * main.height;
    private float playButtonY = .60F * main.height;
    private final float width = .5F * main.width; // 50% of width
    private final float height = .1F * main.height; // 10% of width

    public TitleScreen(){
        mode = Mode.TITLE;
        titleImage = main.loadImage("maze_runner.png");
        titleImage.resize(800, 0);
        whatIsThisImage = main.loadImage("what_is_this.png");
        whatIsThisImage.resize(400, 0);
        playImage = main.loadImage("play.png");
        playImage.resize(200, 0);
    }

    @Override
    public void render(){
        switch(mode){
            case TITLE:
                renderTitleMode();
                break;
        }
    }

    private void renderTitleMode(){
        main.imageMode(main.CENTER);
        main.image(titleImage, centerX, titleImageY);
        main.rectMode(main.CENTER);
        main.fill(0, 105, 217);
        main.rect(centerX, whatIsThisButtonY, width, height);
        main.image(whatIsThisImage, centerX, whatIsThisButtonY);
        main.fill(33, 136, 56);
        main.rect(centerX, playButtonY, width, height);
        main.image(playImage, centerX, playButtonY);
    }

    public void feedCharacter(char key){

    }

    public void click(){

    }

    private enum Mode{
        TITLE,
        WHAT_IS_THIS,
        PLAY
    }
}
