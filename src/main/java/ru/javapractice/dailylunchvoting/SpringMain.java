package ru.javapractice.dailylunchvoting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javapractice.dailylunchvoting.repository.MenuItemRepository;

import java.util.Arrays;

public class SpringMain {
    private static final Logger logger = LoggerFactory.getLogger("default");
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");

        logger.info("Bean definition names: {}", Arrays.toString(appCtx.getBeanDefinitionNames()));
        MenuItemRepository menuItemRepository = appCtx.getBean(MenuItemRepository.class);
        menuItemRepository.getAll().forEach(x -> logger.info(x.toString()));
        appCtx.close();
    }
}

