package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoItemCreationRequest;
import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoListDetail;
import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoListItemResponse;
import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItem;
import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItemRepository;
import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToDoListService {

    private final ToDoListsRepository repository;
    private final ToDoListItemRepository itemRepository;

    public ToDoList createToDoList(String title, TodoUser user) {
        ToDoList toDoList = ToDoList.builder()
                .owner(user)
                .title(title).build();

        repository.saveAndFlush(toDoList);

        return toDoList;
    }

    public List<ToDoList> getTodoLists(TodoUser user) {
        return repository.getAllByOwner(user);
    }

    public ToDoListDetail getToDoList(UUID id, TodoUser user) throws IllegalAccessException {
        ToDoList toDoList = repository.findById(id).orElseThrow();
        if (!toDoList.getOwner().equals(user)) {
            throw new IllegalAccessException();
        }
        return ToDoListDetail.builder()
                .id(toDoList.getId())
                .title(toDoList.getTitle())
                .items(toDoList.getToDoListItems().stream()
                        .map(item -> ToDoListItemResponse.builder()
                                .id(item.getId())
                                .content(item.getContent())
                                .type(item.getType())
                                .build())
                        .toList())
                .build();
    }

    public ToDoListItem createToDoListItem(UUID toDoListId, ToDoItemCreationRequest request, TodoUser user) throws IllegalAccessException {
        ToDoList toDoList = repository.findById(toDoListId).orElseThrow();

        if (!toDoList.getOwner().equals(user)) {
            throw new IllegalAccessException();
        }

        ToDoListItem item = ToDoListItem.builder()
                .list(toDoList)
                .content(request.getContent())
                .type(request.getType())
                .build();

        toDoList.getToDoListItems().add(item);
        itemRepository.saveAndFlush(item);
        repository.save(toDoList);

        return item;
    }
}
