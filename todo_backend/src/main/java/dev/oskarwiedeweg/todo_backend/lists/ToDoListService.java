package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoItemCreationRequest;
import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoItemPatchRequest;
import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoListDetail;
import dev.oskarwiedeweg.todo_backend.lists.dto.ToDoListItemResponse;
import dev.oskarwiedeweg.todo_backend.lists.item.ItemStatus;
import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItem;
import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItemRepository;
import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public ToDoListDetail getToDoList(UUID id, TodoUser user) {
        ToDoList toDoList = repository.findById(id).orElseThrow();
        if (!toDoList.getOwner().equals(user)) {
            throwUnauthorizedException();
        }
        return ToDoListDetail.builder()
                .id(toDoList.getId())
                .title(toDoList.getTitle())
                .items(toDoList.getToDoListItems().stream()
                        .map(item -> ToDoListItemResponse.builder()
                                .id(item.getId())
                                .content(item.getContent())
                                .type(item.getType())
                                .status(item.getStatus())
                                .build())
                        .toList())
                .build();
    }

    public ToDoListItem createToDoListItem(UUID toDoListId, ToDoItemCreationRequest request, TodoUser user) {
        ToDoList toDoList = repository.findById(toDoListId).orElseThrow();

        if (!toDoList.getOwner().equals(user)) {
            throwUnauthorizedException();
        }

        ToDoListItem item = ToDoListItem.builder()
                .list(toDoList)
                .content(request.getContent())
                .type(request.getType())
                .status(ItemStatus.TODO)
                .build();

        toDoList.getToDoListItems().add(item);
        itemRepository.saveAndFlush(item);
        repository.save(toDoList);

        return item;
    }

    public void deleteItem(UUID list, UUID item, TodoUser user) {
        ToDoListItem toDoListItem = itemRepository.findById(item).orElseThrow();
        ToDoList toDoList = repository.findById(list).orElseThrow();

        if (!toDoList.getOwner().equals(user)) {
            throwUnauthorizedException();
        }

        toDoList.getToDoListItems().remove(toDoListItem);

        itemRepository.delete(toDoListItem);
        repository.save(toDoList);
    }

    public ToDoListItem patchItem(UUID list, UUID item, ToDoItemPatchRequest request, TodoUser user) {
        ToDoListItem toDoListItem = itemRepository.findById(item).orElseThrow();

        if (!toDoListItem.getList().getId().equals(list)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item '%s' in '%s'!");
        }

        if (!toDoListItem.getList().getOwner().equals(user)) {
            throwUnauthorizedException();
        }

        if (request.getStatus() != null) {
            toDoListItem.setStatus(request.getStatus());
        }

        if (request.getItemType() != null) {
            toDoListItem.setType(request.getItemType());
        }

        if (request.getContent() != null) {
            toDoListItem.setContent(request.getContent());
        }

        itemRepository.save(toDoListItem);
        return toDoListItem;
    }

    private void throwUnauthorizedException() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this list!");
    }
}
