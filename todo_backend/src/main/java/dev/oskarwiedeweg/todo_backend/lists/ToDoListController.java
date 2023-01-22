package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.lists.dto.*;
import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItem;
import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lists")
public class ToDoListController {

    private final ToDoListService toDoListService;

    @PostMapping
    public ResponseEntity<ToDoListResponse> createToDoList(Authentication authentication, @RequestBody @Valid ToDoCreationRequest request) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            ToDoList toDoList = toDoListService.createToDoList(request.getTitle(), user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ToDoListResponse.builder()
                            .id(toDoList.getId())
                            .title(toDoList.getTitle())
                            .build());
        }
        throw new IllegalStateException();
    }

    @GetMapping
    public ResponseEntity<Collection<ToDoListResponse>> getAllLists(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            Collection<ToDoListResponse> todoLists = toDoListService.getTodoLists(user).stream()
                    .map(list -> ToDoListResponse.builder()
                            .title(list.getTitle())
                            .id(list.getId())
                            .build())
                    .toList();
            return ResponseEntity.ok(todoLists);
        }

        throw new IllegalStateException();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoListDetail> getToDoList(@PathVariable UUID id, Authentication authentication) throws IllegalAccessException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            return ResponseEntity.ok(toDoListService.getToDoList(id, user));
        }

        throw new IllegalStateException();
    }

    @PostMapping("/{id}")
    public ResponseEntity<ToDoListItemResponse> createItem(@PathVariable UUID id,
                                                           @RequestBody @Valid ToDoItemCreationRequest request,
                                                           Authentication authentication) throws IllegalAccessException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            ToDoListItem toDoListItem = toDoListService.createToDoListItem(id, request, user);
            return ResponseEntity.ok(ToDoListItemResponse.builder()
                            .type(toDoListItem.getType())
                            .content(toDoListItem.getContent())
                            .status(toDoListItem.getStatus())
                            .id(toDoListItem.getId())
                    .build());
        }

        throw new IllegalStateException();
    }

    @DeleteMapping("/{list}/{item}")
    public void deleteItem(@PathVariable UUID list,
                           @PathVariable UUID item,
                           Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            toDoListService.deleteItem(list, item, user);
        }
        throw new IllegalStateException();
    }

    @PatchMapping("/{list}/{item}")
    public ResponseEntity<ToDoListItemResponse> patchItem(@PathVariable UUID list,
                                                  @PathVariable UUID item,
                                                  @RequestBody ToDoItemPatchRequest request,
                                                  Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof TodoUser user) {
            ToDoListItem toDoListItem = toDoListService.patchItem(list, item, request, user);
            return ResponseEntity.ok(ToDoListItemResponse.builder()
                    .id(toDoListItem.getId())
                    .content(toDoListItem.getContent())
                    .type(toDoListItem.getType())
                    .status(toDoListItem.getStatus())
                    .build());
        }
        throw new IllegalStateException();
    }

}
