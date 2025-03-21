package ru.javapractice.dailylunchvoting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javapractice.dailylunchvoting.repository.MenuItemRepository;

import java.util.Arrays;

public class SpringMain {
//    private static Logger logger = LoggerFactory.getLogger(SpringMain.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

//        logger.info("Bean definition names: {Arrays.toString(appCtx.getBeanDefinitionNames())}");
        MenuItemRepository menuItemRepository = appCtx.getBean(MenuItemRepository.class);
        menuItemRepository.getAll().forEach(System.out::println);
        appCtx.close();
    }
}

