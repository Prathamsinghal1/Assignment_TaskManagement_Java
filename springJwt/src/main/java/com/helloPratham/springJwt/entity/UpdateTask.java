package com.helloPratham.springJwt.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTask {

    private LocalDate dueDate; // Optional deadline support

    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO; // Default status
}
