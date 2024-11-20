package org.poo.main;

import org.poo.fileio.CardInput;

import java.util.Objects;

public class Minion extends Card {
    private String ability;

    public Minion(CardInput input) {
        super(input);
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Minion minion = (Minion) o;
        return Objects.equals(ability, minion.ability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ability);
    }

    @Override
    public String toString() {
        return "Minion{" +
                "ability='" + ability + '\'' +
                '}';
    }
}
