package dev.oskarwiedeweg.todo_backend.lists;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToDoListsRepository extends JpaRepository<ToDoList, UUID> {



}
