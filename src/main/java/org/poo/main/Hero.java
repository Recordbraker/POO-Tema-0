package org.poo.main;

import org.poo.fileio.CardInput;

import java.util.ArrayList;
import java.util.Objects;

public class Hero extends Card {
    public Hero(CardInput input) {
        super(input);
        super.setHealth(30);
    }

    @Override
    public String toString() {
        return "Hero{}";
    }
}
