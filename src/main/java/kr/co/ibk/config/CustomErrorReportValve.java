package kr.co.ibk.config;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;

import javax.servlet.ServletException;
import java.io.IOException;

public class CustomErrorReportValve extends ErrorReportValve {
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }
}
