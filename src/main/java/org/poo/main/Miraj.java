package org.poo.main;

import org.poo.fileio.CardInput;

public class Miraj extends Card{
    public Miraj(CardInput input) {
        super(input);
    }

    public void useAbility(Card attacker, Card attacked) {
        System.out.println("MIRAJ");
        int attackerNewHealth = attacker.getHealth();
        int attackedNewHealth = attacked.getHealth();
        attacker.setHealth(attackedNewHealth);
        attacked.setHealth(attackerNewHealth);
        attacker.setHasAttacked(true);
    }
}
