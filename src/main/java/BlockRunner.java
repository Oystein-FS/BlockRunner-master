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
    static int playerX;
    static int playerY;
    static boolean loop = true;
    private static Screen screen;
    private static List<Block> blocks;
    private static char player;
    static boolean gameOver = false;


    public static void main(String[] args) throws IOException, InterruptedException {

        // Set up terminal and screen, and create variable for use of TextGraphics
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        terminal.setCursorVisible(false);
        screen.startScreen();

        init(terminal);
        
        // game engine
        loop(terminal, player, blocks);
        terminal.close();

    }

    private static void init(Terminal terminal) throws IOException {
        playerX = 2;
        playerY = 10;
        // Set triangle symbol for player

        terminal.clearScreen();
        TextGraphics tg = screen.newTextGraphics();

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        player = Symbols.TRIANGLE_RIGHT_POINTING_BLACK;

        // Create rectangle symbol for the goal
        tg.drawRectangle(
                new TerminalPosition(79, 10),
                new TerminalSize(1, 1),
                Symbols.BLOCK_MIDDLE);

        terminal.flush();
        screen.refresh();

        terminal.setCursorVisible(false);
        terminal.setCursorPosition(playerX, playerY);
        //  terminal.putCharacter(player);
        terminal.flush();

        // crate blocks
        blocks = getBlocks(terminal);
    }

    private static void gameOverScreen(Terminal terminal) throws IOException, InterruptedException {
        // win or loose screen
        TextGraphics tg = terminal.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.GREEN);
        tg.setForegroundColor(TextColor.ANSI.MAGENTA);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        if (playerY == 10 && playerX == 78) {
            terminal.clearScreen();
            tg.putString(29, 15, "Press ENTER to try again", SGR.FRAKTUR);
            tg.putString(3, 22, "Press Q to Exit", SGR.FRAKTUR);
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(30, 10, "Winner! Winner! Winner!", SGR.BOLD, SGR.BLINK);

            screen.refresh();
            terminal.flush();

        } else {
            tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            tg.putString(33, 10, "G A M E  O V E R !", SGR.BOLD, SGR.BLINK);
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(29, 15, "Press ENTER to try again", SGR.FRAKTUR);
            tg.putString(3, 22, "Press Q to Exit", SGR.FRAKTUR);
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(61, 22, "You fucking loser", SGR.FRAKTUR);

            screen.refresh();
            terminal.flush();
        }
    }

    private static void loop(Terminal terminal, char player, List<Block> blocks) throws InterruptedException, IOException {
        while (loop) {
            KeyStroke keyStroke = null;
            Thread.sleep(5);
            keyStroke = terminal.pollInput();

            // create goal area
            TextGraphics tg = terminal.newTextGraphics();
            tg.drawRectangle(
                    new TerminalPosition(79, 10),
                    new TerminalSize(1, 1),
                    Symbols.BLOCK_MIDDLE);

            // sets start position
            terminal.setCursorPosition(playerX, playerY);
            if (!gameOver)
                terminal.putCharacter(player);
            else {
                gameOverScreen(terminal);
            }
            terminal.flush();

            //
            if (keyStroke != null) {

                KeyType direction = keyStroke.getKeyType();
                Character c = keyStroke.getCharacter();
                int columnTemp = playerX;
                int rowTemp = playerY;

                if (direction == KeyType.Enter) {
                    init(terminal);
                    gameOver = false;
                }

                // Method to steer player in arrowInput direction
                directionInput(direction, columnTemp, rowTemp, terminal);

                // quit game
                if (c != null) {
                    if (c == 'q' || c == 'Q') {
                        break;
                    }
                }

            }

            // display and move blocks
            if (!gameOver) {
                for (Block block : blocks) {
                    block.displayBlock();
                    block.moveBlock();
                    block.increaseCounter();
                }
            }

            // check if player posisiton is equal to any of the blocks
            boolean exit = false;
            for (int i = 0; i < blocks.size(); i++) {
                if (blocks.get(i).getX() == playerX && blocks.get(i).getY() == playerY) {
                    gameOver = true;
                }
            }

            // reached goal
            if (playerY == 10 && playerX == 78) {
                gameOver = true;
            }

        }

    }

    private static List<Block> getBlocks(Terminal terminal) throws IOException {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(terminal, 10, 3, 7, 2));
        blocks.add(new Block(terminal, 20, 1, 10, 3));
        blocks.add(new Block(terminal, 30, 12, 8, 2));
        blocks.add(new Block(terminal, 40, 20, 3, 4));
        blocks.add(new Block(terminal, 50, 20, 10, 4));
        blocks.add(new Block(terminal, 60, 3, 5, 5));
        blocks.add(new Block(terminal, 70, 3, 3, 2));
        return blocks;
    }

    private static void directionInput(KeyType direction, int columnTemp, int rowTemp, Terminal terminal) throws IOException {
        if (!gameOver) {
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
}
