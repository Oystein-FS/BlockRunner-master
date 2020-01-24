import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class BlockRunner {
    static boolean down = true;
    static int playerX = 2;
    static int playerY = 10;
    static long count = 0;


    public static void main(String[] args) throws IOException, InterruptedException {

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();

        terminal.setCursorVisible(false);

        screen.startScreen();


        int blockX = 10;
        int blockY = 3;

        char player = Symbols.TRIANGLE_RIGHT_POINTING_BLACK;

        tg.drawRectangle(
                new TerminalPosition(79, 10),
                new TerminalSize(1, 5),
                Symbols.BLOCK_MIDDLE);

        screen.refresh();

        terminal.setCursorVisible(false);
        terminal.setCursorPosition(playerX, playerY);
        terminal.putCharacter(player);

        terminal.flush();

        //Block block1 = new Block(terminal,20, 10);

        while (true) {
            KeyStroke keyStroke = null;

            Thread.sleep(5); // might throw InterruptedException
            keyStroke = terminal.pollInput();

            if (keyStroke != null) {

                KeyType direction = keyStroke.getKeyType();
                Character c = keyStroke.getCharacter();

                int columnTemp = playerX;
                int rowTemp = playerY;

                switch (direction) {
                    case ArrowDown:
                        playerY++;
                        break;
                    case ArrowUp:
                        playerY--;
                        break;
                    case ArrowLeft:
                        playerX--;
                        break;
                    case ArrowRight:
                        playerX++;
                        break;
                }

                if (playerX == 0 || playerX == 79) {
                    playerX = columnTemp;
                }
                if (playerY == 0 || playerY == 23) {
                    playerY = rowTemp;
                }

                if (playerY == 10 && playerX == 78) {
                    break;
                }

                terminal.setCursorPosition(columnTemp, rowTemp);
                terminal.putCharacter(' ');
            }

            if (count % 10 == 0) {
                if (down) {
                    blockY++;
                } else {
                    blockY--;
                }
            }

            if (blockY == 22) {
                down = false;
            }

            if (blockY == 1) {
                down = true;
            }

            if (!drawBlock(terminal, blockX, blockY)) {
                break;
            }

            /*block1.displayBlock();
            block1.moveBlock();
*/
            if (!drawBlock(terminal, 20, blockY)) {
                break;
            }

            if (!drawBlock(terminal, 30, blockY)) {
                break;
            }

            if (!drawBlock(terminal, 40, blockY)) {
                break;
            }

            if (!drawBlock(terminal, 50, blockY)) {
                break;
            }
            if (!drawBlock(terminal, 60, blockY)) {
                break;
            }
            if (!drawBlock(terminal, 70, blockY)) {
                break;
            }

            terminal.setCursorPosition(playerX, playerY);
            terminal.putCharacter(player);
            terminal.flush();

            count++;
        }

        TextGraphics textGraphics = terminal.newTextGraphics();

        if (playerY == 12 && playerX == 78) {
            textGraphics.putString(35, 10, "Winner!", SGR.BOLD);
            terminal.flush();
        } else {
            textGraphics.putString(33, 10, "G A M E  O V E R !", SGR.BOLD);
            terminal.flush();
        }

    }


    static boolean drawBlock(Terminal terminal, int blockX, int blockY) throws IOException {

        int lenght = 3;

        for (int i = 0; i < lenght; i++) {
            int position;
            if (down)
                position = blockY + i;
            else
                position = blockY - i;
            if (playerX == blockX && playerY == position) {

                return false;

            }

            terminal.setCursorPosition(blockX, position);
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

        return true;
    }


}
