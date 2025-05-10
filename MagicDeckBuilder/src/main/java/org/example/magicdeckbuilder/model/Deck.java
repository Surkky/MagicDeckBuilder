package org.example.magicdeckbuilder.model;

import java.util.List;


public class Deck {
    private String name;
    private List<CardInstance> cards;
    private String format;

    public Deck(String name, List<CardInstance> cards, String format) {
        this.name = name;
        this.cards = cards;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardInstance> getCards() {
        return cards;
    }

    public void setCards(List<CardInstance> cards) {
        this.cards = cards;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "name='" + name + '\'' +
                ", cards=" + cards +
                ", format='" + format + '\'' +
                '}';
    }
}