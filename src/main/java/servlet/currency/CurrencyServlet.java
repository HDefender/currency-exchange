package servlet.currency;

import exception.ResponseCode.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.AppContextListener;
import service.CurrencyService;
import servlet.BaseServlet;
import util.ValidationUtil;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        currencyService = appComponent(
                AppContextListener.CURRENCY_SERVICE, CurrencyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ValidationUtil.validateInput(req.getPathInfo());

        String code = req.getPathInfo().substring(1).toUpperCase().strip();

        ValidationUtil.validateUrl(code, 3);
        sendResponse(resp, ResponseCode.SUCCESS, currencyService.findByCode(code));
    }
}
