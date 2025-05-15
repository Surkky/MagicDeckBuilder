package org.example.magicdeckbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class CardBuilder {
    private String name;
    private int manaCost;
    private CardType cardType;
    private List<String> subtypes = new ArrayList<>();
    private String flavorText;
    private String set;
    private String rarity;
    private String artist;
    private String imgUrl;

    public CardBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CardBuilder setManaCost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    public CardBuilder setCardType(CardType cardType) {
        this.cardType = cardType;
        return this;
    }

    public CardBuilder setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
        return this;
    }

    public CardBuilder setFlavorText(String flavorText) {
        this.flavorText = flavorText;
        return this;
    }

    public CardBuilder setSet(String set) {
        this.set = set;
        return this;
    }

    public CardBuilder setRarity(String rarity) {
        this.rarity = rarity;
        return this;
    }

    public CardBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public CardBuilder setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public Card build() {
        return new Card(name, manaCost, cardType, subtypes, flavorText, set, rarity, artist, imgUrl);
    }
}