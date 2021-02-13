package ru.otus.task03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.task03.config.AppProps;
import ru.otus.task03.service.ExamService;

@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class Task03Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Task03Application.class, args);
        ExamService service = ctx.getBean(ExamService.class);
        System.out.println(service.testStudent());
    }
}
