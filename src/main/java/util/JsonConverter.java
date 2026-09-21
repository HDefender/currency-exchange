package util;

import com.google.gson.Gson;
import dto.BaseDto;

import java.util.List;

public class JsonConverter {
    private static final Gson gson = new Gson();
    private JsonConverter() {
    }
    public static String convertToJson(Object response) {
        return gson.toJson(response);
    }

    public static String convertToJson(List<? extends BaseDto> list) {
        return gson.toJson(list);
    }

}
