import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Block {
    private int posistionX;
    private int posistionY;
    private int speed;
    private int size;
    Terminal terminal;
    private long counter = 0;
    boolean down = true;
    int spacePosition;


    public Block(Terminal terminal, int posistionX, int posistionY, int speed, int size) throws IOException {
        this.posistionX = posistionX;
        this.posistionY = posistionY;
        this.terminal = terminal;
        this.speed = speed;
        this.size = size;
    }

    public void moveBlock() throws IOException {
        // move blocks bases on the defined speed
        if (this.counter %  speed == 0){
            if (down) {
                this.spacePosition = this.posistionY;
                this.posistionY++;
            } else {
                this.spacePosition = this.posistionY;
                this.posistionY--;
            }
        }

        // defines boundaries
        if (this.posistionY == 22) {
            down = false;
        }

        if (this.posistionY == 1) {
            down = true;
        }
    }

    public void displayBlock() throws IOException {
        for (int i = 0; i < size; i++) {
            if (down) {
                terminal.setCursorPosition(this.posistionX, this.posistionY - i);
                terminal.putCharacter('\u2588');
                terminal.setCursorPosition(this.posistionX, this.spacePosition - i);
                terminal.putCharacter(' ');
                terminal.flush();
            } else {
                terminal.setCursorPosition(this.posistionX, this.posistionY + i);
                terminal.putCharacter('\u2588');
                terminal.setCursorPosition(this.posistionX, this.spacePosition + i);
                terminal.putCharacter(' ');
                terminal.flush();
            }
        }

    }

    public void increaseCounter(){
        this.counter++;
    }

    public int getX(){
      return this.posistionX;
    }

    public int getY(){
        return this.posistionY;
    }

}

