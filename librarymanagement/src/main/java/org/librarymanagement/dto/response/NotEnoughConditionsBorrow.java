package org.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotEnoughConditionsBorrow {
    private int status;
    private String error;
    private Map<String, String> fieldErrors;
}
