package org.poo.main;

import org.poo.fileio.CardInput;

public class LordRoyce extends Hero {
    public LordRoyce(CardInput input) {
        super(input);
    }

    public void useAbility(Table table, int row) {
        System.out.println("LORD");
        for (int i = 0; i < table.getCards().get(row).size(); i++) {
            Card card = table.getCards().get(row).get(i);
            card.setFrozen(true);
        }
        this.setHasAttacked(true);
    }
}
