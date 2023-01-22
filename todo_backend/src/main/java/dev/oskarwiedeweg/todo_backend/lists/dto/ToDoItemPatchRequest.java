package dev.oskarwiedeweg.todo_backend.lists.dto;

import dev.oskarwiedeweg.todo_backend.lists.item.ItemStatus;
import dev.oskarwiedeweg.todo_backend.lists.item.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoItemPatchRequest {

    private ItemStatus status;
    private ItemType itemType;
    private String content;

}
