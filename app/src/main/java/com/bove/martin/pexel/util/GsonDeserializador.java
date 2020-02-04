package com.bove.martin.pexel.util;

import com.bove.martin.pexel.model.Foto;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martín Bove on 28/05/2018.
 * E-mail: mbove77@gmail.com
 */
public class GsonDeserializador implements JsonDeserializer<List<Foto>> {
    List<Foto> fotos = new ArrayList<Foto>();
    @Override
    public List<Foto> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray photos = json.getAsJsonObject().get("photos").getAsJsonArray();

        for (int i=0; i < photos.size(); i++) {
            int id = photos.get(i).getAsJsonObject().get("id").getAsInt();
            int width =  photos.get(i).getAsJsonObject().get("width").getAsInt();
            int height = photos.get(i).getAsJsonObject().get("height").getAsInt();
            String url = photos.get(i).getAsJsonObject().get("url").getAsString();
            String photographer = photos.get(i).getAsJsonObject().get("photographer").getAsString();
            String original = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("original").getAsString();
            String large = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("large").getAsString();
            String large2x = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("large2x").getAsString();
            String medium = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("medium").getAsString();
            String small = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("small").getAsString();
            String portrait = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("portrait").getAsString();
            String landscape = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("landscape").getAsString();
            String tiny = photos.get(i).getAsJsonObject().get("src").getAsJsonObject().get("tiny").getAsString();

            Foto currentFoto = new Foto(id, width, height, url, photographer, original, large, large2x, medium, small, portrait, landscape, tiny);

            if(!fotos.contains(currentFoto))
                fotos.add(currentFoto);
        }

        return fotos;
    }
}
