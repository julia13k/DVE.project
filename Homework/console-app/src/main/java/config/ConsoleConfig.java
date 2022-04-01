package config;

import api.console.*;
import com.geekhub.config.AppConfig;
import com.geekhub.mylogger.MyLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AppConfig.class)
public class ConsoleConfig {

    @Bean
    public ArgumentsScanner scanner() {
        return new ArgumentsScanner();
    }

    @Bean
    public CourseMenu courseMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new CourseMenu(scanner, logger);
    }

    @Bean
    public HomeworkMenu homeworkMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new HomeworkMenu(scanner, logger);
    }

    @Bean
    public LectionMenu lectionMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new LectionMenu(scanner, logger);
    }

    @Bean
    public PersonMenu personMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new PersonMenu(scanner, logger);
    }

    @Bean
    public ResourseMenu resourseMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new ResourseMenu(scanner, logger);
    }

    @Bean
    public MyLoggerMenu myLoggerMenu(ArgumentsScanner scanner, MyLogger logger) {
        return new MyLoggerMenu(scanner, logger);
    }

    @Bean
    public GeneralMenu generalMenu(ArgumentsScanner scanner, CourseMenu courseMenu, LectionMenu lectionMenu,
                                   PersonMenu personMenu, ResourseMenu resourseMenu, HomeworkMenu homeworkMenu,
                                   MyLoggerMenu myLoggerMenu) {
        return new GeneralMenu(scanner, courseMenu, lectionMenu, personMenu, resourseMenu, homeworkMenu, myLoggerMenu);
    }
}
