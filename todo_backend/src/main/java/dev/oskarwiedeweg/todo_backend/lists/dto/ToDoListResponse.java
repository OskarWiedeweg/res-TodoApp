package dev.oskarwiedeweg.todo_backend.lists.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListResponse {

    private UUID id;
    private String title;

}
