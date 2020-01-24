import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockRunner {
    static int playerX = 2;
    static int playerY = 10;
    static boolean loop = true;

    public static void main(String[] args) throws IOException, InterruptedException {

        // Set up terminal and screen, and create variable for use of TextGraphics
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        terminal.setCursorVisible(false);
        screen.startScreen();

        // Set triangle symbol for player
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        char player = Symbols.TRIANGLE_RIGHT_POINTING_BLACK;

        // Create rectangle symbol for the goal
        tg.drawRectangle(
                new TerminalPosition(79, 10),
                new TerminalSize(1, 1),
                Symbols.BLOCK_MIDDLE);

        screen.refresh();

        terminal.setCursorVisible(false);
        terminal.setCursorPosition(playerX, playerY);
        terminal.putCharacter(player);
        terminal.flush();

        // crate blocks
        List<Block> blocks = getBlocks(terminal);

        // game engine
        while (loop) {
            KeyStroke keyStroke = null;
            Thread.sleep(5);
            keyStroke = terminal.pollInput();

            if (keyStroke != null) {

                KeyType direction = keyStroke.getKeyType();
                Character c = keyStroke.getCharacter();
                int columnTemp = playerX;
                int rowTemp = playerY;

                // Method to steer player in arrowInput direction
                directionInput(direction, columnTemp, rowTemp, terminal);

                // Detect when player reach the goal
                if (playerY == 10 && playerX == 78) {
                    loop = false;
                }
            }

            for (Block block : blocks) {
                block.displayBlock();
                block.moveBlock();
                block.increaseCounter();
            }

            boolean exit = false;
            for (int i = 0; i < blocks.size(); i++){
                if (blocks.get(i).getX() == playerX && blocks.get(i).getY() == playerY) {
                    exit = true;
                }
            }

            if (exit) {
                loop = false;
            }

            terminal.setCursorPosition(playerX, playerY);
            terminal.putCharacter(player);
            terminal.flush();
        }

        // win or loose screen
        TextGraphics textGraphics = terminal.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.GREEN);
        tg.setForegroundColor(TextColor.ANSI.MAGENTA);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        if (playerY == 10 && playerX == 78) {
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            terminal.clearScreen();
            tg.putString(33, 10, "Winner! Winner! Winner!", SGR.BOLD, SGR.BLINK);
            tg.putString(33, 15, "Press ENTER to try again", SGR.FRAKTUR);


            screen.refresh();
            terminal.flush();
        } else {
            tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            tg.putString(33, 10, "G A M E  O V E R !", SGR.BOLD, SGR.BLINK);
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(61, 22, "you fucking loser", SGR.FRAKTUR);
            tg.putString(33, 15, "Press ENTER to try again", SGR.FRAKTUR);
            screen.refresh();
            terminal.flush();
        }
    }

    private static List<Block> getBlocks(Terminal terminal) throws IOException {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(terminal, 10, 3, 7, 2));
        blocks.add(new Block(terminal, 20, 1, 10,3));
        blocks.add(new Block(terminal, 30, 12, 8,2));
        blocks.add(new Block(terminal, 40, 20, 3,4));
        blocks.add(new Block(terminal, 50, 20, 10,4));
        blocks.add(new Block(terminal, 60, 3, 5,5));
        blocks.add(new Block(terminal, 70, 3, 3,2));
        return blocks;
    }

    private static void directionInput(KeyType direction, int columnTemp, int rowTemp, Terminal terminal) throws IOException {
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
            case Enter:
                loop = true;

        }

        // Erase "tale" of player
        if (playerX == 0 || playerX == 79) {
            playerX = columnTemp;
        }
        if (playerY == 0 || playerY == 23) {
            playerY = rowTemp;
        }
        terminal.setCursorPosition(columnTemp, rowTemp);
        terminal.putCharacter(' ');
    }
}
