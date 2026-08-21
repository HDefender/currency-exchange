package servlet.currency;

import exception.ResponseCode.ResponseCode;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = req.getPathInfo().substring(1).toUpperCase().strip();

        ValidationUtil.validateURL(code,3);

        sendResponse(resp, ResponseCode.SUCCESS, currencyService.findByCode(code));
    }
}
