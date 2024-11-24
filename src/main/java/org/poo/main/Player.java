package org.poo.main;

import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;

import java.util.ArrayList;
import java.util.Objects;

public class Player {
    private int mana;
    private int numberOfCards;
    private int numberOfDecks;
    private int indexDeck;
    private int indexHero;
    private int hasTank;
    private int wins;
    private boolean wasTurn;
    private ArrayList < Card > cardsInHand;
    private ArrayList < ArrayList < Card > > decks;
    private Hero hero;

    public Player(DecksInput input, CardInput hero) {
        this.wins = 0;
        this.mana = 0;
        this.indexDeck = 0;
        this.hasTank = 0;
        this.indexHero = 0;
        this.numberOfCards = input.getNrCardsInDeck();
        this.numberOfDecks = input.getNrDecks();
        this.wasTurn = false;
        this.decks = new ArrayList<>();
        for (int i = 0; i < numberOfDecks; i++) {
            ArrayList<Card> deck = new ArrayList<>();
            this.decks.add(deck);
            for (int j = 0; j < numberOfCards; j++) {
                Card card;
                String name = input.getDecks().get(i).get(j).getName();

                if (name.equals("Miraj")) {
                    this.decks.get(i).add(new Miraj(input.getDecks().get(i).get(j)));
                } else if(name.equals("The Ripper")) {
                    this.decks.get(i).add(new TheRipper(input.getDecks().get(i).get(j)));
                } else if (name.equals("The Cursed One")) {
                    this.decks.get(i).add(new TheCursedOne(input.getDecks().get(i).get(j)));
                } else if (name.equals("Disciple")) {
                    this.decks.get(i).add(new Disciple(input.getDecks().get(i).get(j)));
                } else {
                    this.decks.get(i).add(new Card(input.getDecks().get(i).get(j)));
                }
            }
        }

        if (hero.getName().equals("Lord Royce")) {
            this.hero = new LordRoyce(hero);
        } else if (hero.getName().equals("Empress Thorina")) {
            this.hero = new EmpressThorina(hero);
        } else if (hero.getName().equals("King Mudface")) {
            this.hero = new KingMudface(hero);
        } else if (hero.getName().equals("General Kocioraw")) {
            this.hero = new GeneralKocioraw(hero);
        } else {
            this.hero = new Hero(hero);
        }

        this.cardsInHand = new ArrayList<>();
    }

    public void resetDecks(DecksInput input) {
        this.decks = new ArrayList<>();
        for (int i = 0; i < numberOfDecks; i++) {
            ArrayList<Card> deck = new ArrayList<>();
            decks.add(deck);
            for (int j = 0; j < numberOfCards; j++) {
                Card card = new Card(input.getDecks().get(i).get(j));
                decks.get(i).add(card);
            }
        }
    }

    public boolean isWasTurn() {
        return wasTurn;
    }

    public void setWasTurn(boolean wasTurn) {
        this.wasTurn = wasTurn;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void addMana(int mana) {
        this.mana = this.mana +  mana;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    public void setNumberOfDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    public int getIndexDeck() {
        return indexDeck;
    }

    public void setIndexDeck(int indexDeck) {
        this.indexDeck = indexDeck;
    }

    public int getIndexHero() {
        return indexHero;
    }

    public void setIndexHero(int indexHero) {
        this.indexHero = indexHero;
    }

    public int getHasTank() {
        return hasTank;
    }

    public void setHasTank(int hasTank) {
        this.hasTank = hasTank;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setHero(CardInput hero) {
        this.hero = new Hero(hero);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return mana == player.mana && numberOfCards == player.numberOfCards && numberOfDecks == player.numberOfDecks && indexDeck == player.indexDeck && indexHero == player.indexHero && hasTank == player.hasTank && wins == player.wins && Objects.equals(cardsInHand, player.cardsInHand) && Objects.equals(decks, player.decks) && Objects.equals(hero, player.hero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mana, numberOfCards, numberOfDecks, indexDeck, indexHero, hasTank, wins, cardsInHand, decks, hero);
    }

    @Override
    public String toString() {
        return "Player{" +
                "mana=" + mana +
                ", numberOfCards=" + numberOfCards +
                ", numberOfDecks=" + numberOfDecks +
                ", indexDeck=" + indexDeck +
                ", indexHero=" + indexHero +
                ", hasTank=" + hasTank +
                ", wins=" + wins +
                ", cardsInHand=" + cardsInHand +
                ", decks=" + decks +
                ", hero=" + hero +
                '}';
    }
}
