package com.example.demo.Dto;

import com.example.demo.Entity.Todo;
import com.example.demo.Enum.Status;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {
    private String title;
    private String description;
    private Status status;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .description(description)
                .status(status)
                .build();
    }
}
