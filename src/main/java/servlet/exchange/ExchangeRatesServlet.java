package servlet.exchange;

import dto.request.ExchangeRatesRequestDto;
import exception.ResponseCode.ResponseCode;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRatesService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sendResponse(resp, ResponseCode.SUCCESS, exchangeRatesService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String baseCurrencyCode = checkCode(req,"baseCurrencyCode");
        String targetCurrencyCode = checkCode(req,"targetCurrencyCode");
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));

        ExchangeRatesRequestDto exchangeRatesRequestDto = new ExchangeRatesRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
        ValidationUtil.validateExchangeRatesDto(exchangeRatesRequestDto);

        sendResponse(resp,ResponseCode.SUCCESS_CREATED, exchangeRatesService.create(exchangeRatesRequestDto));
    }
}
