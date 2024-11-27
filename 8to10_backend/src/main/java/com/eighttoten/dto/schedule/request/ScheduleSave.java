package com.eighttoten.dto.schedule.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
public abstract class ScheduleSave {
    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80, groups = FieldErrorGroup.class)
    private String title;

    @NotNull
    private String commonDescription;

    protected ScheduleSave(String title, String commonDescription) {
        this.title = title;
        this.commonDescription = commonDescription;
    }
}