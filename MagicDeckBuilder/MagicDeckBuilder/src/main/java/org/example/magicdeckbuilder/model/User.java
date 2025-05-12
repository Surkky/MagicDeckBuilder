package org.example.magicdeckbuilder.model;

import java.util.List;
import java.util.Scanner;

public class User {
   private String name;
   private String password;
   private List<Card> collection;
   private List<Deck> decks;


    public User(String name, String password, List<Card> collection, List<Deck> decks) {
        this.name = name;
        this.password = password;
        this.collection = collection;
        this.decks = decks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Card> getCollection() {
        return collection;
    }

    public void setCollection(List<Card> collection) {
        this.collection = collection;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", collection=" + collection +
                ", decks=" + decks +
                '}';
    }
}