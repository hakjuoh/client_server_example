package org.oagi.srt.thesaurus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ThesaurusApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ThesaurusApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ThesaurusApplication.class);
    }
}
