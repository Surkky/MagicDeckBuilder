package org.example.magicdeckbuilder.model;

import java.util.List;


public class CreatureCard extends Card{
    private int power;
    private int toughness;
    private List<String> abilities;

    public CreatureCard(int power, int toughness, List<String> abilities) {
        this.power = power;
        this.toughness = toughness;
        this.abilities = abilities;
    }

    public CreatureCard(String name, int manaCost, String cardType, List<String> subtypes, String flavorText, String set, String rarity, String artist, int power, int toughness, List<String> abilities) {
        super(name, manaCost, cardType, subtypes, flavorText, set, rarity, artist);
        this.power = power;
        this.toughness = toughness;
        this.abilities = abilities;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return super.toString() +
                "power=" + power +
                ", toughness=" + toughness +
                ", abilities=" + abilities +
                '}';
    }
}