package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoListService {

    private final ToDoListsRepository repository;

    public ToDoList createToDoList(String title, TodoUser user) {
        ToDoList toDoList = ToDoList.builder()
                .owner(user)
                .title(title).build();

        repository.saveAndFlush(toDoList);

        return toDoList;
    }

}
