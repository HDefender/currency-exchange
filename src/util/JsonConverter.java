package util;

import com.google.gson.Gson;
import dto.BaseDto;

import java.util.List;

public class JsonConverter {
    private static Gson gson = new Gson();
    private JsonConverter() {
    }
    public static String convertToJson(BaseDto baseDto) {
        return gson.toJson(baseDto);
    }

    public static String convertToJson(List<? extends BaseDto> list) {
        return gson.toJson(list);
    }

}
