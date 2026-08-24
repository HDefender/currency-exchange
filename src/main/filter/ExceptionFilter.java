package main.filter;

import com.google.gson.Gson;
import main.dto.ExceptionDto;
import main.exception.*;
import main.exception.ResponseCode.ResponseCode;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter implements Filter {
    Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try{
            filterChain.doFilter(req, resp);
        } catch(AlreadyExistException e){
            handleException(resp, ResponseCode.ALREADY_EXISTS.getHttpStatus(),e.getMessage());
        } catch(IncorrectInputException e){
            handleException(resp, ResponseCode.BAD_REQUEST.getHttpStatus(), e.getMessage());
        } catch (DataNotFoundException | ExchangeNotFoundException e){
            handleException(resp, ResponseCode.NOT_FOUND.getHttpStatus(),e.getMessage());
        } catch(InternalErrorException | DatabaseException e){
            handleException(resp, ResponseCode.INTERNAL_ERROR.getHttpStatus(), e.getMessage());
        } catch(Exception e){
            handleException(resp,ResponseCode.INTERNAL_ERROR.getHttpStatus(),"Another error occured");
        }
    }

    private void handleException (HttpServletResponse resp, int errorCode, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(errorCode);
        ExceptionDto exceptionDto = new ExceptionDto(message);
        resp.getWriter().write(gson.toJson(exceptionDto));
    }
}

