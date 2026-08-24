package main.servlet;

import main.dto.request.ExchangeRequestDto;
import main.exception.ResponseCode.ResponseCode;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.service.ExchangeService;
import main.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {
    ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrency = checkCode (req, "from");
        String targetCurrency = checkCode (req,"to");
        BigDecimal amount = checkRate(req,"amount");

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCurrency,targetCurrency,amount);
        ValidationUtil.validateExchangeDto(exchangeRequestDto);

        sendResponse(resp, ResponseCode.SUCCESS,exchangeService.convert(exchangeRequestDto));
    }
}