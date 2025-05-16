package ru.javapractice.dailylunchvoting.user.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.app.config.*;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.user.UserData.*;

@WebMvcTest(controllers = AdminUserController.class)
@Import({
        AdminUserControllerCacheTest.TestConfig.class,
        TestCacheConfig.class,
        AppConfig.class,
        SecurityTestConfig.class,
})
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AdminUserControllerCacheTest extends AbstractControllerTest {

    public static final String REST_URL = AdminUserController.REST_URL;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserRepository repository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public UniqueMailValidator validator() {
            UniqueMailValidator mockValidator = Mockito.mock(UniqueMailValidator.class);
            Mockito.doAnswer(invocation -> null).when(mockValidator).validate(any(), any());
            return mockValidator;
        }
    }

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserRepository repository;
    @Autowired
    UniqueMailValidator validator;

    @AfterEach
    void clear() {
        clearCache();
    }

    @BeforeEach
    void setupValidator() {
        clearCache();
        Mockito.clearInvocations(repository);

        doAnswer(invocation -> null).when(validator).validate(any(), any());
        when(validator.supports(any())).thenReturn(true);
    }

    @Test
    @WithMockUser
    void testGetUserByIdCache() throws Exception {
        int user_1_Id = USER_1_ID;

        var copyUser = new User(USER_1);

        when(repository.getExisted(user_1_Id)).thenReturn(copyUser);

        verify(repository, times(0)).getExisted(user_1_Id);

        perform(MockMvcRequestBuilders.get(REST_URL + "/{id}", user_1_Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(repository, times(1)).getExisted(user_1_Id);

        perform(MockMvcRequestBuilders.get(REST_URL + "/{id}", user_1_Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(repository, times(1)).getExisted(user_1_Id);

        perform(MockMvcRequestBuilders.patch(REST_URL + "/{id}", user_1_Id)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(repository, atLeastOnce()).getExisted(user_1_Id);

        Mockito.clearInvocations(repository);

        perform(MockMvcRequestBuilders.get(REST_URL + "/{id}", user_1_Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(repository, times(1)).getExisted(user_1_Id);

    }

    @Test
    @WithMockUser
    void testGetAllUsersCache() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(repository, times(1)).findAll(any(Sort.class));

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk());

        verify(repository, times(1)).findAll(any(Sort.class));
    }

    @Test
    @WithMockUser
    void testCacheEvictionAfterCreate() throws Exception {

        var newUser = getNew();
        when(repository.prepareAndSave(any())).thenAnswer(invocation -> {
            User input = getNew();
            input.setId(99999);
            return input;
        });

        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(repository, times(1)).findAll(any(Sort.class));

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "newPass")))
                .andDo(print())
                .andExpect(status().isCreated());

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk());

        verify(repository, times(2)).findAll(any(Sort.class));
    }

    private void clearCache() {
        log.warn(StringUtils.repeat('*', 100) + "Clear cache{}", cacheManager.getCache(CacheNames.USER_CACHE));
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
