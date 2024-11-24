package org.poo.main;

import org.poo.fileio.CardInput;

import java.util.ArrayList;
import java.util.Objects;

public class Card {
    private int costMana;
    private int attack;
    private int health;
    private String description;
    private ArrayList < String > colors;
    private String name;
    private boolean isFrozen;
    private boolean hasAttacked;

    public Card(CardInput input) {
        this.costMana = input.getMana();
        this.attack = input.getAttackDamage();
        this.health = input.getHealth();
        this.description = input.getDescription();
        this.colors = input.getColors();
        this.name = input.getName();
        this.isFrozen = false;
        this.hasAttacked = false;
    }

    public void useAbility(Card attacker, Card attacked) {
        System.out.println("APELATA DIN CARD");
    }

    public void addHealth(int amount) {
        this.health = this.health + amount;
    }

    public void addAttack(int amount) {
        this.attack = this.attack + amount;
    }

    public int getCostMana() {
        return costMana;
    }

    public void setCostMana(int costMana) {
        this.costMana = costMana;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return costMana == card.costMana && attack == card.attack && health == card.health && isFrozen == card.isFrozen && hasAttacked == card.hasAttacked && Objects.equals(description, card.description) && Objects.equals(colors, card.colors) && Objects.equals(name, card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(costMana, attack, health, description, colors, name, isFrozen, hasAttacked);
    }

    @Override
    public String toString() {
        return "Card{" +
                "costMana=" + costMana +
                ", attack=" + attack +
                ", health=" + health +
                ", description='" + description + '\'' +
                ", colors=" + colors +
                ", name='" + name + '\'' +
                ", isFrozen=" + isFrozen +
                ", hasAttacked=" + hasAttacked +
                '}';
    }
}
