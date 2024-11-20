package org.poo.main;

import java.util.ArrayList;
import java.util.Objects;


public class Table {
    private final static int length = 4;
    private final static int width = 5;
    private ArrayList< ArrayList <Card> > cards;

    public Table() {
        this.cards = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            ArrayList<Card> deck = new ArrayList<>();
            cards.add(deck);
        }
    }

    public void unfreezeRow(int index) {
        for (int i = 0; i < this.cards.get(index).size(); i++) {
            this.cards.get(index).get(i).setFrozen(false);
        }
    }

    public void resetAttackStatus() {
        for (int i = 0; i < this.cards.size(); i++) {
            for (int j = 0; j < this.cards.get(i).size(); j++) {
                this.cards.get(i).get(j).setHasAttacked(false);
            }
        }
    }

    public ArrayList<ArrayList<Card>> getCards() {
        return cards;
    }

    public void setCards(ArrayList<ArrayList<Card>> cards) {
        this.cards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(cards, table.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cards);
    }

    @Override
    public String toString() {
        return "Table{" +
                "cards=" + cards +
                '}';
    }
}
