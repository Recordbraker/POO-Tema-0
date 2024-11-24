package org.poo.main;

import org.poo.fileio.CardInput;

public class TheCursedOne extends Card{

    public TheCursedOne(CardInput input) {
        super(input);
    }

    public void useAbility(Card attacker, Card attacked) {
        System.out.println("CURSED");
        int health = attacked.getHealth();
        int attack = attacked.getAttack();
        attacked.setAttack(health);
        attacked.setHealth(attack);
        attacker.setHasAttacked(true);
    }
}
