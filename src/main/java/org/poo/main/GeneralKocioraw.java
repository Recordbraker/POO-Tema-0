package org.poo.main;

import org.poo.fileio.CardInput;

public class GeneralKocioraw extends Hero{
    public GeneralKocioraw(CardInput input) {
        super(input);
    }

    public void useAbility(Table table, int row) {
        System.out.println("GENERAL");
        for (int i = 0; i < table.getCards().get(row).size(); i++) {
            Card card = table.getCards().get(row).get(i);
            card.setAttack(card.getAttack() + 1);
        }
        this.setHasAttacked(true);
    }
}
