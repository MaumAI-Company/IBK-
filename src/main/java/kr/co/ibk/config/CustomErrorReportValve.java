package kr.co.ibk.config;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;

import javax.servlet.ServletException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CustomErrorReportValve extends ErrorReportValve {
//    @Override
//    public void invoke(Request request, Response response) throws IOException, ServletException {
//        getNext().invoke(request, response);
//    }

    @Override
    protected void report(Request request, Response response, Throwable t) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"));
            out.write("<!DOCTYPE html>");
            out.write("<html lang=\"en\">");
            out.write("<head>");
            out.write("    <meta charset=\"UTF-8\">");
            out.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.write("    <title>Error</title>");
            out.write("    <style>");
            out.write("        * {");
            out.write("            -webkit-box-sizing: border-box;");
            out.write("            box-sizing: border-box;");
            out.write("        }");
            out.write("        body {");
            out.write("            padding: 0;");
            out.write("            margin: 0;");
            out.write("            background-color: #f9f9f9;");
            out.write("            font-family: 'Noto Sans KR', Arial, sans-serif;");
            out.write("        }");
            out.write("        #notfound {");
            out.write("            position: relative;");
            out.write("            height: 100vh;");
            out.write("        }");
            out.write("        #notfound .notfound {");
            out.write("            position: absolute;");
            out.write("            left: 50%;");
            out.write("            top: 50%;");
            out.write("            -webkit-transform: translate(-50%, -50%);");
            out.write("            -ms-transform: translate(-50%, -50%);");
            out.write("            transform: translate(-50%, -50%);");
            out.write("            max-width: 460px;");
            out.write("            width: 100%;");
            out.write("            text-align: center;");
            out.write("            line-height: 1.4;");
            out.write("        }");
            out.write("        .notfound .notfound-404 {");
            out.write("            position: relative;");
            out.write("            width: 180px;");
            out.write("            height: 180px;");
            out.write("            margin: 0px auto 50px;");
            out.write("        }");
            out.write("        .notfound .notfound-404>div:first-child {");
            out.write("            position: absolute;");
            out.write("            left: 0;");
            out.write("            right: 0;");
            out.write("            top: 0;");
            out.write("            bottom: 0;");
            out.write("            background: #ffa200;");
            out.write("            -webkit-transform: rotate(45deg);");
            out.write("            -ms-transform: rotate(45deg);");
            out.write("            transform: rotate(45deg);");
            out.write("            border: 5px dashed #000;");
            out.write("            border-radius: 5px;");
            out.write("        }");
            out.write("        .notfound .notfound-404 h1 {");
            out.write("            color: #000;");
            out.write("            font-weight: 700;");
            out.write("            margin: 0;");
            out.write("            font-size: 90px;");
            out.write("            position: absolute;");
            out.write("            top: 50%;");
            out.write("            -webkit-transform: translate(-50%, -50%);");
            out.write("            -ms-transform: translate(-50%, -50%);");
            out.write("            transform: translate(-50%, -50%);");
            out.write("            left: 50%;");
            out.write("            text-align: center;");
            out.write("            height: 40px;");
            out.write("            line-height: 40px;");
            out.write("        }");
            out.write("        .notfound h2 {");
            out.write("            font-size: 33px;");
            out.write("            font-weight: 700;");
            out.write("            text-transform: uppercase;");
            out.write("            letter-spacing: 7px;");
            out.write("            margin-bottom: 20px;");
            out.write("        }");
            out.write("        .notfound a {");
            out.write("            display: inline-block;");
            out.write("            padding: 10px 25px;");
            out.write("            background-color: #8f8f8f;");
            out.write("            border: none;");
            out.write("            border-radius: 40px;");
            out.write("            color: #fff;");
            out.write("            font-size: 14px;");
            out.write("            font-weight: 700;");
            out.write("            text-transform: uppercase;");
            out.write("            text-decoration: none;");
            out.write("            -webkit-transition: 0.2s all;");
            out.write("            transition: 0.2s all;");
            out.write("        }");
            out.write("        .notfound a:hover {");
            out.write("            background-color: #2c2c2c;");
            out.write("        }");
            out.write("    </style>");
            out.write("</head>");
            out.write("<body>");

            out.write("<div id=\"notfound\">");
            out.write("    <div class=\"notfound\">");
            out.write("        <div class=\"notfound-404\">");
            out.write("            <div></div>");
            out.write("            <h1>!</h1>");
            out.write("        </div>");
            out.write("        <h2>OOPS! ERROR!</h2>");
            out.write("        <a onclick=\"reLogin()\">Home Page</a>");
            out.write("    </div>");
            out.write("</div>");

            out.write("<script>");
            out.write("    function reLogin() {");
            out.write("        var referrer = document.referrer;");
            out.write("        location.href = \"https://chatbot.smart.army.mil.kr:10443/\";");
            out.write("    }");
            out.write("</script>");

            out.write("</body>");
            out.write("</html>");

            out.close();

            // Log the error with your favorite logging framework...
//            logger.severe("Uncaught throwable was thrown: " + t.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
