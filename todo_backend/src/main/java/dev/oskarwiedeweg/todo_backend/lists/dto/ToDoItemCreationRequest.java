package dev.oskarwiedeweg.todo_backend.lists.dto;

import dev.oskarwiedeweg.todo_backend.lists.item.ItemType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoItemCreationRequest {

    private ItemType type = ItemType.TEXT;

    @NotNull
    private String content;

}
