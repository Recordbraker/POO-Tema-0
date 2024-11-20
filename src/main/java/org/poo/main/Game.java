package org.poo.main;

import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class Game {
    private Player player1;
    private Player player2;
    private int numberOfGames;
    private int whoStart;
    private int turn;
    private int seed;
    private int rounds;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.numberOfGames = 0;
        this.rounds = 0;
    }

    public Table beginGame(Player player1, Player player2, GameInput currentGame, Input input) {
        player1.setIndexDeck(currentGame.getStartGame().getPlayerOneDeckIdx());
        player2.setIndexDeck(currentGame.getStartGame().getPlayerTwoDeckIdx());

        CardInput hero1 = currentGame.getStartGame().getPlayerOneHero();
        CardInput hero2 = currentGame.getStartGame().getPlayerTwoHero();
        player1.setHero(hero1);
        player2.setHero(hero2);

        // reset mana
        player1.setMana(0);
        player2.setMana(0);
        // reset decks
        player1.resetDecks(input.getPlayerOneDecks());
        player2.resetDecks(input.getPlayerTwoDecks());

        this.setSeed(currentGame.getStartGame().getShuffleSeed());
        Random random1 = new Random(this.getSeed());
        Collections.shuffle(player1.getDecks().get(player1.getIndexDeck()), random1);
        Random random2 = new Random(this.getSeed());
        Collections.shuffle(player2.getDecks().get(player2.getIndexDeck()), random2);

        player1.setWasTurn(false);
        player2.setWasTurn(false);

        return new Table();
    }

    public void beginRound(Player player1, Player player2, GameInput currentGame, Game game, Table table) {
        if (!player1.getDecks().get(player1.getIndexDeck()).isEmpty()) {
            player1.getCardsInHand().add(player1.getDecks().get(player1.getIndexDeck()).get(0));
            player1.getDecks().get(player1.getIndexDeck()).remove(0);
        }

        if (!player2.getDecks().get(player2.getIndexDeck()).isEmpty()) {
            player2.getCardsInHand().add(player2.getDecks().get(player2.getIndexDeck()).get(0));
            player2.getDecks().get(player2.getIndexDeck()).remove(0);
        }

        this.incrementRound();
        int additionalMana = this.getRounds();
        if (this.getTurn() > 9) {
            additionalMana = 10;
        }
        player1.addMana(additionalMana);
        player2.addMana(additionalMana);

        player1.setWasTurn(false);
        player2.setWasTurn(false);
        game.setTurn(game.getWhoStart());

        this.setTurn(currentGame.getStartGame().getStartingPlayer());
        table.resetAttackStatus();
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void incrementRound() {
        this.rounds++;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getWhoStart() {
        return whoStart;
    }

    public void setWhoStart(int whoStart) {
        this.whoStart = whoStart;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberOfGames == game.numberOfGames && whoStart == game.whoStart && turn == game.turn && seed == game.seed && rounds == game.rounds && Objects.equals(player1, game.player1) && Objects.equals(player2, game.player2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGames, player1, player2, whoStart, turn, seed, rounds);
    }

    @Override
    public String toString() {
        return "Game{" +
                "numberOfGames=" + numberOfGames +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", whoStart=" + whoStart +
                ", turn=" + turn +
                ", seed=" + seed +
                ", rounds=" + rounds +
                '}';
    }
}
