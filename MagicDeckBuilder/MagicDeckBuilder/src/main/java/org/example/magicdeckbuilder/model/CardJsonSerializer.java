package org.example.magicdeckbuilder.model;

import org.example.magicdeckbuilder.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardJsonSerializer {

    public static JSONObject serialize(Card card) {
        JSONObject json = new JSONObject();
        json.put("name", card.getName());
        json.put("manaCost", card.getManaCost());
        json.put("cardType", card.getCardType().name());
        json.put("subtypes", new JSONArray(card.getSubtypes()));
        json.put("flavorText", card.getFlavorText());
        json.put("set", card.getSet());
        json.put("rarity", card.getRarity());
        json.put("artist", card.getArtist());
        json.put("imgurl", card.getImgUrl());
        return json;
    }

    public static Card deserialize(JSONObject json) {
        CardBuilder builder = new CardBuilder()
                .setName(json.getString("name"))
                .setManaCost(json.getInt("manaCost"))
                .setCardType(CardType.valueOf(json.getString("cardType")));

        JSONArray subtypesArray = json.optJSONArray("subtypes");
        if (subtypesArray != null) {
            List<String> subtypes = new ArrayList<>();
            for (int i = 0; i < subtypesArray.length(); i++) {
                subtypes.add(subtypesArray.getString(i));
            }
            builder.setSubtypes(subtypes);
        }

        builder.setFlavorText(json.optString("flavorText", ""));
        builder.setSet(json.optString("set", ""));
        builder.setRarity(json.optString("rarity", ""));
        builder.setArtist(json.optString("artist", ""));
        builder.setImgUrl(json.optString("imgUrl", ""));

        return builder.build();
    }
}
