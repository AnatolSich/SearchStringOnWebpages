package app.config;

import app.model.Initials;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Log4j2
@Configuration
public class InitialConfig {

    @Value("${developex.initial.url}")
    private String initialUrl;

    @Value("${developex.initial.targetString}")
    private String targetString;

    @Bean(name = "initials")
    public Initials initials() {
        checkInputs(initialUrl, targetString);
        return new Initials(initialUrl, targetString);
    }

    private void checkInputs(String initialUrl,
                             String targetString) {

        UrlValidator urlValidator = new UrlValidator(new String[]{"https"});

        if (!urlValidator.isValid(initialUrl)) {
            log.info("{} - is not valid URL");
            System.exit(1);
        }

        log.info("******Inputs********");

        log.info(initialUrl);

        log.info(targetString);

        log.info("************");
    }

    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        return executor;
    }


}
