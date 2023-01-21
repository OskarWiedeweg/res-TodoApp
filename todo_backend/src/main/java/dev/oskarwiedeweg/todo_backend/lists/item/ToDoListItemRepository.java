package dev.oskarwiedeweg.todo_backend.lists.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToDoListItemRepository extends JpaRepository<ToDoListItem, UUID> {
}
