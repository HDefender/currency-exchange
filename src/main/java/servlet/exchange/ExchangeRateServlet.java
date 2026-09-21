package servlet.exchange;

import dto.request.ExchangeRateRequestDto;
import exception.IncorrectInputException;
import exception.ResponseCode.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        exchangeRateService = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String codePair = req.getPathInfo().substring(1).toUpperCase().strip();
        ValidationUtil.validateURL(codePair, 6);

        String baseCode = codePair.substring(0, 3);
        String targetCode = codePair.substring(3);
        ValidationUtil.validateCodePair(baseCode, targetCode);

        sendResponse(resp, ResponseCode.SUCCESS, exchangeRateService.findByCodes(baseCode, targetCode));
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codePair = req.getPathInfo().substring(1).toUpperCase().strip();
        ValidationUtil.validateURL(codePair, 6);

        String baseCode = codePair.substring(0, 3);
        String targetCode = codePair.substring(3);
        ValidationUtil.validateCodePair(baseCode, targetCode);

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString();

        String[] splittedBody = body.split("=", 2);

        if (splittedBody.length < 2) {
            throw new IncorrectInputException("Rate is missing");
        }

        String stringRate = splittedBody[1];

        ValidationUtil.validateInput(stringRate);
        ValidationUtil.validateRateFormat(stringRate);
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(splittedBody[1]));
        ValidationUtil.validateRate(rate);


        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ValidationUtil.validateExchangeRatesDto(exchangeRateRequestDto);
        sendResponse(resp, ResponseCode.SUCCESS, exchangeRateService.update(exchangeRateRequestDto));
    }

}