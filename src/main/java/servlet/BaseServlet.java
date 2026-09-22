package servlet;

import exception.ResponseCode.ResponseCode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JsonConverter;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

public abstract class BaseServlet extends HttpServlet {

    public void sendResponse (HttpServletResponse resp, ResponseCode respStatus, Object response) throws IOException {
        resp.setStatus(respStatus.getHttpStatus());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResp = JsonConverter.convertToJson(response);
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
        BigDecimal rate = ValidationUtil.parseRate(parameterValue);
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

    protected final <T> T appComponent(String key, Class<T> type) {
        return type.cast(getServletContext().getAttribute(key));
    }
}
