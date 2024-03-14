package webserver;

import ch.qos.logback.classic.spi.ILoggingEvent;
import db.Database;
import model.User;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

public class CreateHandlerTest {
    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setup() {
        logger = (Logger) LoggerFactory.getLogger(CreateHandler.class);

        // Logback의 ListAppender를 생성하여 로깅 이벤트를 캡처
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    @DisplayName("user create시, 로그 메세지 출력 테스트")
    void testCreateLog() {
        CreateHandler createHandler = new CreateHandler("/create?userId=zoonmy&password=password&name=zoon&email=zoonmy%40lucas.com");

        createHandler.create();

        assertThat(listAppender.list)
                .hasSize(1)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("create : User [userId=zoonmy, password=password, name=zoon, email=zoonmy%40lucas.com]");

        // Appender 정리
        logger.detachAppender(listAppender);
        listAppender.stop();
    }

    @Test
    @DisplayName("user create시, DB에 정상 저장되었는지 테스트")
    void testCreateDB() {
        CreateHandler createHandler = new CreateHandler("/create?userId=zoonmy&password=password&name=zoon&email=zoonmy%40lucas.com");

        createHandler.create();

        assertThat(Database.findAll().size()).isEqualTo(1);
        assertThat(Database.findUserById("zoonmy").getUserId()).isEqualTo("zoonmy");
        assertThat(Database.findUserById("zoonmy").getPassword()).isEqualTo("password");
        assertThat(Database.findUserById("zoonmy").getName()).isEqualTo("zoon");
        assertThat(Database.findUserById("zoonmy").getEmail()).isEqualTo("zoonmy%40lucas.com");
    }


}