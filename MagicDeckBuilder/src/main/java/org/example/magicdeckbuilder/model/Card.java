package org.example.magicdeckbuilder.model;

import java.util.List;


public class Card {

    private String name;
    private int manaCost;
    private String cardType;
    private List<String> subtypes;
    private String flavorText;
    private String set;
    private String rarity;
    private String artist;

    public Card() {
    }

    public Card(String name, int manaCost, String cardType, List<String> subtypes, String flavorText, String set, String rarity, String artist) {
        this.name = name;
        this.manaCost = manaCost;
        this.cardType = cardType;
        this.subtypes = subtypes;
        this.flavorText = flavorText;
        this.set = set;
        this.rarity = rarity;
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", manaCost=" + manaCost +
                ", cardType='" + cardType + '\'' +
                ", subtypes=" + subtypes +
                ", flavorText='" + flavorText + '\'' +
                ", set='" + set + '\'' +
                ", rarity='" + rarity + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}