import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Нужен перфоратор на пару дней",
                new UserDto(), LocalDateTime.now(), List.of());

        JsonContent<ItemRequestDto> dtoInputSaved = jacksonTester.write(itemRequestDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.requestor");
        assertThat(dtoInputSaved).hasJsonPath("$.created");
        assertThat(dtoInputSaved).hasJsonPath("$.items");
    }

    @Test
    void testDeserialize() throws Exception {
        Long id = 1L;
        String description = "Нужен перфоратор на пару дней";
        UserDto requestor = new UserDto();
        LocalDateTime created = LocalDateTime.now();
        List<Object> items = Collections.emptyList();

        String json = String.format("""
                {\
                  "id": %d,\s
                  "description": "%s",
                  "requestor": {},
                  "created": "%s",
                  "items": []
                }""", id, description, created.toString());

        ItemRequestDto deserializedItemRequestDto = jacksonTester.parseObject(json);

        assertThat(deserializedItemRequestDto.getId()).isEqualTo(id);
        assertThat(deserializedItemRequestDto.getDescription()).isEqualTo(description);
        assertThat(deserializedItemRequestDto.getRequestor()).isNotNull();
        assertThat(deserializedItemRequestDto.getCreated()).isEqualTo(created);
        assertThat(deserializedItemRequestDto.getItems()).isEmpty();
    }

}