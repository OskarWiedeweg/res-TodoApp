package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ToDoListsRepository extends JpaRepository<ToDoList, UUID> {

    List<ToDoList> getAllByOwner(TodoUser owner);

}
