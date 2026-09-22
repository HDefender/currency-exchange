package servlet.exchange;

import dto.request.ExchangeRateRequestDto;
import exception.ResponseCode.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.AppContextListener;
import service.ExchangeRateService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        exchangeRateService = appComponent(
                AppContextListener.EXCHANGE_RATE_SERVICE, ExchangeRateService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sendResponse(resp, ResponseCode.SUCCESS, exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String baseCurrencyCode = checkCode(req, "baseCurrencyCode");
        String targetCurrencyCode = checkCode(req, "targetCurrencyCode");
        BigDecimal rate = checkRate(req, "rate");

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
        ValidationUtil.validateExchangeRatesDto(exchangeRateRequestDto);

        sendResponse(resp, ResponseCode.SUCCESS_CREATED, exchangeRateService.create(exchangeRateRequestDto));
    }
}
