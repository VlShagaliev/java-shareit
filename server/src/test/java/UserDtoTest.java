import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");

        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);

        assertThat(userDtoSaved).hasJsonPath("$.id");
        assertThat(userDtoSaved).hasJsonPath("$.name");
        assertThat(userDtoSaved).hasJsonPath("$.email");

        assertThat(userDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(userDtoSaved).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    void testDeserialize() throws IOException {
        String json = "{" +
                "\"id\":1," +
                "\"name\":\"Владислав Шагалиев\"," +
                "\"email\":\"vl.shagaliev@ya.ru\"}";

        UserDto deserializedUserDto = jacksonTester.parseObject(json);

        assertThat(deserializedUserDto.getId()).isEqualTo(1L);
        assertThat(deserializedUserDto.getName()).isEqualTo("Владислав Шагалиев");
        assertThat(deserializedUserDto.getEmail()).isEqualTo("vl.shagaliev@ya.ru");
    }

}