package vn.ifa.study.infi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "vn.ifa")
public class HtmlTemplate2PdfApplication {

    public static void main(final String[] args) {

        SpringApplication.run(HtmlTemplate2PdfApplication.class, args);
    }

}
