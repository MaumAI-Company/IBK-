package kr.co.ibk.config;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardHost;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
    }


    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers((context) -> {
            if (context.getParent() instanceof StandardHost) {
                StandardHost parent = (StandardHost) context.getParent();
                parent.setErrorReportValveClass(CustomErrorReportValve.class.getName());
                parent.addValve(new CustomErrorReportValve());
            }
        });

        factory.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
                AbstractHttp11Protocol<?> protocolHandler = (AbstractHttp11Protocol<?>) connector
                        .getProtocolHandler();
                protocolHandler.setRelaxedPathChars("<>[\\\\]^`{|}");
                protocolHandler.setRelaxedQueryChars("<>[\\\\]^`{|}");
            }
        });

    }

}