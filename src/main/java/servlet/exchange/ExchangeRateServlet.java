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
    private static final int CODE_PAIR_LENGTH = 6;

    @Override
    public void init() throws ServletException {
        exchangeRateService = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String codePair = checkPathInfo(req);
        ValidationUtil.validateUrl(codePair, CODE_PAIR_LENGTH);

        String baseCode = codePair.substring(0, CODE_PAIR_LENGTH/2);
        String targetCode = codePair.substring(CODE_PAIR_LENGTH/2);
        ValidationUtil.validateCodePair(baseCode, targetCode);

        sendResponse(resp, ResponseCode.SUCCESS, exchangeRateService.findByCodes(baseCode, targetCode));
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codePair = checkPathInfo(req);
        ValidationUtil.validateUrl(codePair, CODE_PAIR_LENGTH);

        String baseCode = codePair.substring(0, CODE_PAIR_LENGTH/2);
        String targetCode = codePair.substring(CODE_PAIR_LENGTH/2);
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

        if (splittedBody.length < 2 || !"rate".equals(splittedBody[0].strip())) {
            throw new IncorrectInputException("Body should contain 'rate' parameter");
        }

        String stringRate = splittedBody[1];

        ValidationUtil.validateInput(stringRate);
        BigDecimal rate = ValidationUtil.parseRate(stringRate);
        ValidationUtil.validateRate(rate);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ValidationUtil.validateExchangeRatesDto(exchangeRateRequestDto);
        sendResponse(resp, ResponseCode.SUCCESS, exchangeRateService.update(exchangeRateRequestDto));
    }

    private String checkPathInfo(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        ValidationUtil.validateInput(pathInfo);
        return pathInfo.substring(1).toUpperCase().strip();
    }

}