package main.servlet.currency;

import main.dto.request.CurrencyRequestDto;
import main.exception.ResponseCode.ResponseCode;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.service.CurrencyService;
import main.servlet.BaseServlet;
import main.util.ValidationUtil;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sendResponse(resp, ResponseCode.SUCCESS, currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = checkCode(req,"code");
        String name = checkName(req,"name");
        String sign = checkSign(req,"sign");

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);
        ValidationUtil.validateCurrencyDto(currencyRequestDto);

        sendResponse(resp,ResponseCode.SUCCESS_CREATED, currencyService.post(currencyRequestDto));

    }
}