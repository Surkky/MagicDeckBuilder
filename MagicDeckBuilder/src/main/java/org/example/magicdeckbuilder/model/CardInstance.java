package org.example.magicdeckbuilder.model;



public class CardInstance {
    private Card card;
    private int quantity;

    public CardInstance(Card card, int quantity) {
        this.card = card;
        this.quantity = quantity;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CardInstance{" +
                "card=" + card +
                ", quantity=" + quantity +
                '}';
    }
}