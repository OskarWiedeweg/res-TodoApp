package dev.oskarwiedeweg.todo_backend.lists.dto;

import dev.oskarwiedeweg.todo_backend.lists.item.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListItemResponse {

    private UUID id;
    private ItemType type;
    private String content;

}
