package main.servlet;

import main.dto.BaseDto;
import main.exception.ResponseCode.ResponseCode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.util.JsonConverter;
import main.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public abstract class BaseServlet extends HttpServlet {

    public void sendResponse (HttpServletResponse resp, ResponseCode respStatus, BaseDto baseDto) throws IOException {
        resp.setStatus(respStatus.getHttpStatus());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResp = JsonConverter.convertToJson(baseDto);
        resp.getWriter().write(jsonResp);
    }

    public void sendResponse (HttpServletResponse resp, ResponseCode respStatus, List< ? extends BaseDto> list) throws IOException {
        resp.setStatus(respStatus.getHttpStatus());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResp = JsonConverter.convertToJson(list);
        resp.getWriter().write(jsonResp);
    }

    public String checkCode (HttpServletRequest request, String parameterName){
        String parameterValue = request.getParameter(parameterName);
        ValidationUtil.validateInput(parameterValue);
        return parameterValue.strip().toUpperCase();
    }

    public BigDecimal checkRate (HttpServletRequest request, String parameterName){
        String parameterValue = request.getParameter(parameterName);
        ValidationUtil.validateInput(parameterValue);
        BigDecimal rate = new BigDecimal(parameterValue);
        ValidationUtil.validateRate(rate);
        return rate;
    }

    public String checkName (HttpServletRequest request, String parameterName){
        String parameterValue = request.getParameter(parameterName);
        ValidationUtil.validateInput(parameterValue);
        return parameterValue.trim();
    }

    public String checkSign (HttpServletRequest request, String parameterName){
        String parameterValue = request.getParameter(parameterName);
        ValidationUtil.validateInput(parameterValue);
        return parameterValue.strip();
    }

}
