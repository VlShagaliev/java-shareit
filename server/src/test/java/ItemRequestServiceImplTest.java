import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    private ItemRequest itemRequest;
    private User user;
    private UserDto userDto;
    private Item item;


    @BeforeEach
    void setUp() {
        user = new User(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        userDto = new UserDto(1L, "Обновленное имя", "updated@ya.ru");
        itemRequest = new ItemRequest(1L, "Нужен перфоратор на пару дней", user, LocalDateTime.now());
        item = new Item(1L, "Перфоратор", "Ударный перфоратор", true, user, itemRequest);
    }

    @Test
    public void testGetListQueriesCreatedByOtherUsers_UnauthorizedUser() {

        Long unknownUserId = 999L;

        given(userRepository.findById(unknownUserId))
                .willReturn(java.util.Optional.empty());

        Throwable exception = assertThrows(RuntimeException.class, () ->
                itemRequestService.getItemRequestsByOtherUsers(unknownUserId));
        assertThat(exception.getMessage()).startsWith("Пользователь не найден");
    }

    @Test
    public void testGetItemRequestById_Successful() {
        // given
        Long itemRequestId = 1L;
        List<Item> items = List.of(item);

        given(itemRequestRepository.findById(itemRequestId))
                .willReturn(Optional.of(itemRequest));
        given(itemRepository.findAllByRequest_Id(itemRequestId))
                .willReturn(items);

        // when
        ItemRequestDto result = itemRequestService.getItemRequestByItemRequestId(itemRequestId);

        // then
        assertThat(result.getDescription()).isEqualTo("Нужен перфоратор на пару дней");
        assertThat(result.getItems()).isNotEmpty();
        assertThat(result.getItems().iterator().next().getName()).isEqualTo("Перфоратор");
    }
}