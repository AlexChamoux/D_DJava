package fr.le_campus_numerique.java.dd.game;

import fr.le_campus_numerique.java.dd.enemy.*;
import fr.le_campus_numerique.java.dd.equipment.spell.*;
import fr.le_campus_numerique.java.dd.equipment.weapon.*;
import fr.le_campus_numerique.java.dd.player.Player;
import fr.le_campus_numerique.java.dd.potion.*;
import fr.le_campus_numerique.java.dd.space.Space;
import fr.le_campus_numerique.java.dd.space.EmptySpace;
import fr.le_campus_numerique.java.dd.database.DatabaseConnection;

import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the main game logic for the Dungeons and Dragons game.
 * This class manages the game board, player movement, and game flow.
 */
public class Game {
    ArrayList<Space> board;
    private final int BOARD_SIZE = 64;
    private Player player;
    private int currentPosition;
    private Random dice;
    private Menu menu;
//    private GameStatus status;

    /**
     * Constructs a new Game object.
     * Initializes the game board, creates a player, and sets up the initial game state.
     */
    public Game() {
        this.menu = new Menu();
        this.player = loadPlayer();
        if (this.player == null) {
            this.player = menu.createPlayer();
        }
        this.currentPosition = 0;
        this.dice = new Random();
        this.board = this.initBoard(BOARD_SIZE);
    }

    /**
     * Initializes the game board with various game elements.
     *
     * @param size The size of the game board
     */
    public ArrayList<Space> initBoard(int size){
        ArrayList<Space> boardToCreate = new ArrayList<>(64);
//        boardToCreate.ensureCapacity(size);
//        Collections.fill(boardToCreate, new EmptySpace());
        for (int i = 0; i < size; i++) {
            boardToCreate.add(new EmptySpace());
        }

        int[] dragons = {44, 51, 55, 61};
        int[] sorcerers = {9, 19, 2, 31, 34, 35, 36, 39, 43, 46};
        int[] goblins = {2, 5, 8, 11, 14, 17, 20, 23, 26, 29};
        int[] sledgeHammers = {1, 10, 4, 21, 37};
        int[] swords = {18, 25, 41, 52};
        int[] lightnings = {0, 3, 7, 16, 22};
        int[] fireBalls = {47, 48};
        int[] standardPotions = {6, 12, 30, 32, 38, 42};
        int[] bigPotions = {27, 40};

        for(int dragon : dragons){
            boardToCreate.set(dragon, new Dragon());
        }
        for(int sorcerer : sorcerers){
            boardToCreate.set(sorcerer, new Sorcerer());
        }
        for(int goblin : goblins){
            boardToCreate.set(goblin, new Goblin());
        }
        for(int sledgeHammer : sledgeHammers){
            boardToCreate.set(sledgeHammer, new SledgeHammer());
        }
        for(int sword : swords){
            boardToCreate.set(sword, new Sword());
        }
        for(int lightning : lightnings){
            boardToCreate.set(lightning, new LightningBolt());
        }
        for(int fireBall : fireBalls){
            boardToCreate.set(fireBall, new FireBall());
        }
        for(int standardPotion : standardPotions){
            boardToCreate.set(standardPotion, new StandardPotion());
        }
        for(int bigPotion : bigPotions){
            boardToCreate.set(bigPotion, new BigPotion());
        }
        Collections.shuffle(boardToCreate);
        return boardToCreate;
    }

    /**
     * Starts the game.
     */
    public void startGame(){
        playGame();
    }

    /**
     * Main game loop that manages the flow of the game.
     */
    public void playGame(){
        menu.welcome(player);
        while(true){
            if(!Objects.equals(player.getStockIndex(), 0)) {
                String result = menu.askUsePotion();
                if (result.equals("oui")) {
                    player.useStockPotion(player.getStockIndex() -1);
                }
            }
//            menu.getInput("Vous pouvez quitter à tout instant (exit)");
            menu.nextTurn();

            int roll = rollDice();
            menu.displayRollDice(roll);



            try {
                boolean gameFinished = playTurn(roll);
//                menu.displayCase(getCurrentPosition(), getBoardSize());
                if (gameFinished) {
                    menu.displayFinished();
                    if (!askToPlayAgain()) {
                        break;
                    }
                    resetGame();
                }
            }catch(OffSetPlayerException e) {
                menu.displayError(e);
                if (!askToPlayAgain()) {
                    break;
                }
                resetGame();
            }
        }
    }

    /**
     * Asks the player if they want to play again.
     *
     * @return true if the player wants to play again, false otherwise
     */
    private boolean askToPlayAgain(){
        String answer = menu.askToPlayAgain();
        if (answer.equals("oui")) {
            return true;
        } else {
            System.out.println("Merci d'avoir joué ! Au revoir.");
            return false;
        }
    }

    /**
     * Executes a single turn in the game.
     *
     * @param roll The result of the dice roll
     * @return true if the game is finished, false otherwise
     * @throws OffSetPlayerException if the player moves beyond the board
     */
    public boolean playTurn(int roll) throws OffSetPlayerException {
        movePlayer(roll);
        return currentPosition >= (BOARD_SIZE -1);
    }

    /**
     * Simulates rolling a six-sided die.
     *
     * @return A random number between 1 and 6
     */
    public int rollDice() {
        return dice.nextInt(6) + 1;
    }

    /**
     * Moves the player on the board based on the dice roll.
     *
     * @param roll The number of spaces to move
     * @throws OffSetPlayerException if the player moves beyond the board
     */
    private void movePlayer(int roll) throws OffSetPlayerException {
        int newPosition = currentPosition + roll;

        if (newPosition >= BOARD_SIZE) {
            throw new OffSetPlayerException("Erreur générée à la demande de cette merveilleuse itération qui nous demande de mettre des erreurs dans notre code pourtant parfait : Le joueur est allez plus loin que la dernière case !");
        }

        currentPosition = newPosition;

        menu.displayCase(getCurrentPosition(), getBoardSize());


        Space currentSpace = board.get(currentPosition);

        GameStatus status = currentSpace.interact(player);
        switch (status) {
            case ENNEMY_DEAD:
                break;
            case IN_FIGHT:

            case HERO_RETREAT:
                currentPosition = heroRetreat();
                currentSpace = board.get(currentPosition);
                currentSpace.interact(player);
                break;
        }
//        currentSpace.interact(player);
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        currentPosition = 0;
    }

    /**
     * Gets the current position of the player on the board.
     *
     * @return The current position of the player
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets the current player.
     *
     * @return The current player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the size of the game board.
     *
     * @return The size of the game board
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    private int heroRetreat() {
        Random random = new Random();
        int retreatSteps = random.nextInt(6) + 1;
        return Math.max(0, currentPosition - retreatSteps);
    }

    public Player loadPlayer() {
        String name = menu.getInput("Entrez le nom de votre personnage à charger (ou entrée si vous n'en avais pas) :");
        Player player = DatabaseConnection.getPlayerByName(name);
        if (player != null) {
            menu.displayPlayer(player);
            return player;
        } else {
            System.out.println("Aucun personnage trouvé avec ce nom.");
            return null;
        }
    }
}