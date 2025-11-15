import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private Item item;

    @Spy
    private User user;

    @Spy
    private ItemDto itemCreateDto;

    @Spy
    private ItemDto itemDto;

    @Spy
    private ItemDto itemUpdateDto;

    @Spy
    private UserDto userDto;

    @Spy
    Comment comment;

    @Spy
    CommentDto commentDto;

    @Spy
    CommentDto commentCreateDto;


    @BeforeEach
    void setUp() {

        user = new User(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        userDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        item = new Item(1L, "Перфоратор", "Ударный перфоратор", true, user, null);
        itemCreateDto = new ItemDto(null, "Перфоратор", "Ударный перфоратор", true, 1L, null, null, null, null);
        itemUpdateDto = new ItemDto(null, "Перфоратор", "Ударный перфоратор", true, 1L, null, null, null, null);
        itemDto = new ItemDto(1L, "Перфоратор", "Ударный перфоратор", true, userDto.getId(), null, null, null, List.of());
        comment = new Comment(1L, "Все отлично", item, user, LocalDateTime.now());
        commentDto = new CommentDto(1L,"Все отлично", "Бузмаков Иван", LocalDateTime.now());
        commentCreateDto = new CommentDto(1L, "Все отлично", "Бузмаков Иван", LocalDateTime.now());
    }

    @Test
    void testAddItemSuccess() {

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(itemRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.createItem(itemCreateDto, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(itemDto.getName());
    }

    @Test
    void testAddItemUserNotFound() {

        Long invalidUserId = 999L;
        ItemDto createDto = new ItemDto();
        createDto.setName("test-item");

        given(userRepository.findById(invalidUserId)).willReturn(java.util.Optional.empty()); // возвращаем пустую Optional

        Throwable exception = catchThrowable(() -> itemService.createItem(createDto, invalidUserId));
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь не найден"); // ожидаем исключение
    }

    @Test
    void testAddItemWithValidOwner() {
        // given
        Long validUserId = 1L;
        User owner = new User(validUserId, "test-user", "valid@mail.ru");
        ItemDto dto = new ItemDto();
        dto.setName("new-item");

        given(userRepository.findById(validUserId)).willReturn(Optional.of(owner));
        given(itemRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ItemDto savedItem = itemService.createItem(dto, validUserId);
        assertThat(savedItem.getOwnerId()).isEqualTo(validUserId);
    }

    @Test
    void testSuccessfulUpdate() {

        Long userId = 1L;
        Long itemId = 1L;
        User owner = new User();
        owner.setId(userId);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setOwner(owner);
        existingItem.setName("old-name");

        ItemDto updateDto = new ItemDto();
        updateDto.setName("updated-name");

        given(userRepository.findById(userId)).willReturn(Optional.of(owner));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(existingItem));
        given(itemRepository.save(any(Item.class))).willAnswer(i -> i.getArguments()[0]);

        ItemDto updatedItem = itemService.updateItemByIdItem(updateDto, itemId, userId);
        assertThat(updatedItem.getName()).isEqualTo("updated-name");
    }

    @Test
    public void testFindItemsWithEmptySearchText() {

        String searchText = "";

        Collection<ItemDto> result = itemService.getItemsBySearchName(searchText);

        assertThat(result).isEmpty();
    }

    @Test
    public void testFindItemsWithWhitespace() {

        String searchText = " ";

        Collection<ItemDto> result = itemService.getItemsBySearchName(searchText);

        assertThat(result).isEmpty();
    }
}