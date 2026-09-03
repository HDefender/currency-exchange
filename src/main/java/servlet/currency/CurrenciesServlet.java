package servlet.currency;

import dto.request.CurrencyRequestDto;
import exception.ResponseCode.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        currencyService = new CurrencyService();
    }

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

        sendResponse(resp,ResponseCode.SUCCESS_CREATED, currencyService.create(currencyRequestDto));

    }
}