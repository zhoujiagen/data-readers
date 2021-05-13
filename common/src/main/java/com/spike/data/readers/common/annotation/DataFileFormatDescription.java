package com.spike.data.readers.common.annotation;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = {ElementType.TYPE})
@Documented
public @interface DataFileFormatDescription {
}
