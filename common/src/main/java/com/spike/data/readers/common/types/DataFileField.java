package com.spike.data.readers.common.types;

import lombok.*;

/**
 * Description of field in file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataFileField {
    private long offset;
    private long length;
    private String value;
    private String description;
}
