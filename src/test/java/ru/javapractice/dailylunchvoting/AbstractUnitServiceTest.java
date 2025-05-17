package ru.javapractice.dailylunchvoting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.util.TimingExtension;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ExtendWith(TimingExtension.class)
@Slf4j
@Transactional
public class AbstractUnitServiceTest {
}
