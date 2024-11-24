package org.poo.main;

import org.poo.fileio.CardInput;

public class KingMudface extends Hero{
    public KingMudface(CardInput input) {
        super(input);
    }

    public void useAbility(Table table, int row) {
        System.out.println("KING");
        for (int i = 0; i < table.getCards().get(row).size(); i++) {
            Card card = table.getCards().get(row).get(i);
            card.setHealth(card.getHealth() + 1);
        }
        this.setHasAttacked(true);
    }
}
