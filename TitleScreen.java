import processing.core.PImage;

public class TitleScreen implements Renderable{
    private StringBuilder stringBuilder = new StringBuilder();
    private final Main main = Main.getInstance();
    private Mode mode;
    private final float centerX = main.width / 2;

    // title screen variables
    private final PImage titleImage, whatIsThisImage, playImage;
    private float titleImageY = .2F * main.height;
    private float whatIsThisButtonY = .40F * main.height;
    private float playButtonY = .60F * main.height;
    private final float widthTitleButton = .5F * main.width; // 50% of width
    private final float heightTitleButton = .1F * main.height; // 10% of width

    // play screen variables
    private final PImage widthImage, heightImage, monsterImage;
    private PlayField playField;
    private float leftX = .2F * main.width;
    private float widthY = .1F * main.height;
    private float heightY = .3F * main.height;
    private float monstersY = .5F * main.height;
    private float playY = .7F * main.height;
    private float inputAreaX = .7F * main.width;
    private float inputAreaWidth = .5F * main.width;
    private float inputAreaHeight = .1F * main.height;

    public TitleScreen(){
        mode = Mode.TITLE;
        titleImage = main.loadImage("maze_runner.png");
        titleImage.resize(800, 0);
        whatIsThisImage = main.loadImage("what_is_this.png");
        whatIsThisImage.resize(400, 0);
        playImage = main.loadImage("play.png");
        playImage.resize(200, 0);
        widthImage = main.loadImage("width.png");
        widthImage.resize(400, 0);
        heightImage = main.loadImage("height.png");
        heightImage.resize(400, 0);
        monsterImage = main.loadImage("monsters.png");
        monsterImage.resize(350, 0);
    }

    @Override
    public void render(){
        switch(mode){
            case TITLE:
                renderTitleMode();
                break;
            case PLAY:
                renderPlayMode();
                break;
        }
    }

    private void renderTitleMode(){
        main.imageMode(main.CENTER);
        main.image(titleImage, centerX, titleImageY);
        main.rectMode(main.CENTER);
        main.fill(0, 105, 217);
        main.rect(centerX, whatIsThisButtonY, widthTitleButton, heightTitleButton);
        main.image(whatIsThisImage, centerX, whatIsThisButtonY);
        main.fill(33, 136, 56);
        main.rect(centerX, playButtonY, widthTitleButton, heightTitleButton);
        main.image(playImage, centerX, playButtonY);
    }

    private void renderPlayMode(){
        main.imageMode(main.CENTER);
        main.rectMode(main.CENTER);

        // text input area is white
        main.fill(255);

        // width input
        main.rect(inputAreaX, widthY, inputAreaWidth, inputAreaHeight);
        main.image(widthImage, leftX, widthY);

        // height input
        main.rect(inputAreaX, heightY, inputAreaWidth, inputAreaHeight);
        main.image(heightImage, leftX, heightY);

        // monster amount input
        main.rect(inputAreaX, monstersY, inputAreaWidth, inputAreaHeight);
        main.image(monsterImage, leftX, monstersY);

        // play button
        main.fill(33, 136, 56);
        main.rect(centerX, playY, widthTitleButton, heightTitleButton);
        main.image(playImage, centerX, playY);
    }

    public void feedCharacter(char key){

    }

    public void click(){
        switch(mode){
            case TITLE:
                // what is this section clicked
                if(main.mouseX >= centerX - widthTitleButton / 2 && main.mouseX <= centerX + widthTitleButton / 2 &&
                        main.mouseY >= whatIsThisButtonY - heightTitleButton / 2 && main.mouseY <= whatIsThisButtonY + heightTitleButton / 2){
                    mode = Mode.WHAT_IS_THIS;
                }
                // play section clicked
                if(main.mouseX >= centerX - widthTitleButton / 2 && main.mouseX <= centerX + widthTitleButton / 2 &&
                        main.mouseY >= playButtonY - heightTitleButton / 2 && main.mouseY <= playButtonY + heightTitleButton / 2){
                    mode = Mode.PLAY;
                }
                break;
            case PLAY:
                // width section clicked


                // height section clicked


                // monster section clicked


                break;
            case WHAT_IS_THIS:

                break;
        }
    }

    private enum Mode{
        TITLE,
        WHAT_IS_THIS,
        PLAY
    }

    private enum PlayField{
        HEIGHT,
        WIDTH,
        MONSTER
    }
}
