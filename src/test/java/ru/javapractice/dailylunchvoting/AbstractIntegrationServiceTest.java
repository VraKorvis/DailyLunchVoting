package ru.javapractice.dailylunchvoting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.util.TimingExtension;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@ExtendWith(TimingExtension.class)
@Slf4j
public abstract class AbstractIntegrationServiceTest {
}
