package org.example.magicdeckbuilder.model;


public enum CardType {
    CREATURE, INSTANT, SORCERY, ENCHANTMENT, ARTIFACT, LAND, PLANESWALKER;

    public static CardType fromString(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            throw new IllegalArgumentException("Invalid card type: " + rawType);
        }

        String[] parts = rawType.trim().split("\\s+");
        String base = parts[parts.length - 1].toUpperCase();
        return CardType.valueOf(base);
    }
}


