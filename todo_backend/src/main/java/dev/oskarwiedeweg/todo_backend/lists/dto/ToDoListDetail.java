package dev.oskarwiedeweg.todo_backend.lists.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListDetail {

    private UUID id;
    private String title;

    private Collection<ToDoListItemResponse> items;

}
