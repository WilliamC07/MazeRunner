public class TitleScreen implements Renderable{
    private StringBuilder stringBuilder = new StringBuilder();
    private final Main main = Main.getInstance();
    private Mode mode;

    public TitleScreen(){
        mode = Mode.TITLE;
    }

    private void showTitleScreen(){

    }

    @Override
    public void render(){
        switch(mode){
            case TITLE:

                break;
        }
    }

    private void renderTitleMode(){


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
