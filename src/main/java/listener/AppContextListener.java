package listener;

import dao.CurrencyDao;
import dao.CurrencyDaoImpl;
import dao.ExchangeRatesDao;
import dao.ExchangeRatesDaoImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.CurrencyService;
import service.ExchangeRateService;
import service.ExchangeService;
import util.ConnectionManager;

@WebListener
public class AppContextListener implements ServletContextListener {
    public static final String CURRENCY_SERVICE = "currencyService";
    public static final String EXCHANGE_RATE_SERVICE = "exchangeRateService";
    public static final String EXCHANGE_SERVICE = "exchangeService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        ExchangeRatesDao exchangeRateDao = new ExchangeRatesDaoImpl();

        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService =
                new ExchangeRateService(exchangeRateDao, currencyDao);
        ExchangeService exchangeService = new ExchangeService(exchangeRateDao);


        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute(CURRENCY_SERVICE, currencyService);
        ctx.setAttribute(EXCHANGE_RATE_SERVICE, exchangeRateService);
        ctx.setAttribute(EXCHANGE_SERVICE, exchangeService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.close();
    }
}
