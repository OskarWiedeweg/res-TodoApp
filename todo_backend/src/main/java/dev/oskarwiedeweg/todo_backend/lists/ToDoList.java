package dev.oskarwiedeweg.todo_backend.lists;

import dev.oskarwiedeweg.todo_backend.lists.item.ToDoListItem;
import dev.oskarwiedeweg.todo_backend.user.TodoUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private TodoUser owner;

    private String title;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ToDoListItem> toDoListItems;

}
