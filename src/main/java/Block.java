/*
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Block {
    private int posistionX;
    private int posistionY;
    Terminal terminal;
    private long counter = 0;


    public Block(Terminal terminal, int posistionX, int posistionY) throws IOException {
        this.posistionX = posistionX;
        this.posistionY = posistionY;
        this.terminal = terminal;
    }

    public void moveBlock() throws IOException {
        for (int i = 0; i < 3; i++) {

            if (down) {
                posistionY++;
            }
            else {
                posistionYi--;
            }

            terminal.setCursorPosition(posistionX, posistionY);
            terminal.putCharacter('\u2588');
        }

        int spacePosition;

        if (down) {
            spacePosition = blockY - 1;
        } else {
            spacePosition = blockY + 1;
        }

        terminal.setCursorPosition(blockX, spacePosition);
        terminal.putCharacter(' ');

    }
        //this.posistionY--;



    public void displayBlock() throws IOException {
        terminal.setCursorPosition(this.posistionX, this.posistionY);
        terminal.putCharacter('\u2588');
        terminal.flush();

    }

    public void  increaseCounter(){
        this.counter++;
    }


}

*/
