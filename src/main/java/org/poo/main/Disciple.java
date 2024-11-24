package org.poo.main;

import org.poo.fileio.CardInput;

public class Disciple extends Card {
    public Disciple(CardInput input) {
        super(input);
    }

    public void useAbility(Card attacker, Card attacked) {
        System.out.println("DISCIPLE");
        attacked.addHealth(2);
        attacker.setHasAttacked(true);
    }
}
