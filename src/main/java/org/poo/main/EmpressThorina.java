package org.poo.main;

import org.poo.fileio.CardInput;

public class EmpressThorina extends Hero{
    public EmpressThorina(CardInput input) {
        super(input);
    }

    public void useAbility(Table table, int row) {
        System.out.println("THORINA");
        Card destroy = table.getCards().get(row).get(0);
        for (int i = 0; i < table.getCards().get(row).size(); i++) {
            Card card = table.getCards().get(row).get(i);
            if (card.getHealth() > destroy.getHealth()) {
                destroy = card;
            }
        }
        table.getCards().get(row).remove(destroy);
        this.setHasAttacked(true);
    }
}
