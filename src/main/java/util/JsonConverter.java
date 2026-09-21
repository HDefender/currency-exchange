package util;

import com.google.gson.Gson;

public class JsonConverter {
    private static final Gson gson = new Gson();
    private JsonConverter() {
    }
    public static String convertToJson(Object response) {
        return gson.toJson(response);
    }

}
