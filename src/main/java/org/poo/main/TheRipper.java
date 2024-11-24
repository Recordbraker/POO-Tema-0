package org.poo.main;

import org.poo.fileio.CardInput;

public class TheRipper extends Card {
    public TheRipper(CardInput input) {
        super(input);
    }

    public void useAbility(Card attacker, Card attacked) {
        System.out.println("RIPPER");
        attacked.setAttack(attacked.getAttack() - 2);
        if (attacked.getAttack() < 0)
            attacked.setAttack(0);
        attacker.setHasAttacked(true);
    }
}
