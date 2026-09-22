package servlet;

import dto.request.ExchangeRequestDto;
import exception.ResponseCode.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.AppContextListener;
import service.ExchangeRateService;
import service.ExchangeService;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {
    private ExchangeService exchangeService;

    @Override
    public void init() throws ServletException {
        exchangeService = appComponent(
                AppContextListener.EXCHANGE_SERVICE, ExchangeService.class);
    }

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