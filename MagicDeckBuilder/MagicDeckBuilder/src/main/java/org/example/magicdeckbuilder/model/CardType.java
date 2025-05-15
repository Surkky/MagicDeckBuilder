package org.example.magicdeckbuilder.model;


public enum CardType {
    CREATURE, INSTANT, SORCERY, ENCHANTMENT, ARTIFACT, LAND, PLANESWALKER;

    public static CardType fromString(String type) {
        return CardType.valueOf(type.toUpperCase());
    }
}


