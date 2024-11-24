package org.poo.main;

import org.poo.fileio.CardInput;

public class Hero extends Card {
    public Hero(CardInput input) {
        super(input);
        super.setHealth(30);
    }

    public void useAbility(Table table, int row) {
        System.out.println("APELATA DIN HERO");
    }

    @Override
    public String toString() {
        return "Hero{}";
    }
}
