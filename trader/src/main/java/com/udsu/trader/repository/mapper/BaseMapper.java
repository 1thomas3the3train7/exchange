package com.udsu.trader.repository.mapper;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public abstract class BaseMapper {
    protected static <T> T getVal(String name, Row row, RowMetadata metadata, Class<T> tClass) {
        return metadata.contains(name) ? row.get(name, tClass) : null;
    }

    protected static <T> T getVal(String nameMain, String nameIfFirstIsNull, Row row, RowMetadata rowMetadata, Class<T> tClass) {
        if (rowMetadata.contains(nameMain))
            return row.get(nameMain, tClass);
        else
            return rowMetadata.contains(nameIfFirstIsNull) ? row.get(nameIfFirstIsNull, tClass) : null;
    }

    protected static String getStringVal(String name, Row row, RowMetadata rowMetadata) {
        try {
            if (rowMetadata.contains(name)) {
                final String res = row.get(name, String.class);
                return res;
//                if (res != null)
//                    return res.trim();
            }
            return null;
        } catch (Exception e) {
            log.warn("ERROR PARSE RESULT FROM DB", e);
            return null;
        }
    }

    protected static String getStringVal(String name, String nameIfFirstIsNull, Row row, RowMetadata rowMetadata) {
        try {
            if (rowMetadata.contains(name)) {
                final String res = row.get(name, String.class);
                return res;
//                if (res != null)
//                    return res.trim();
            }

            if (rowMetadata.contains(nameIfFirstIsNull))
                return row.get(nameIfFirstIsNull, String.class);
            return null;
        } catch (Exception e) {
            log.warn("ERROR PARSE RESULT FROM DB", e);
            return null;
        }
    }

    protected static Long getLongVal(String name, Row row, RowMetadata rowMetadata) {
        return getVal(name, row, rowMetadata, Long.class);
    }

    protected static Long getLongVal(String name, String nameIfFirstIsNull, Row row, RowMetadata rowMetadata) {
        return getVal(name, nameIfFirstIsNull, row, rowMetadata, Long.class);
    }

    protected static LocalDateTime getLocalDateTimeVal(String name, Row row, RowMetadata rowMetadata) {
        return getVal(name, row, rowMetadata, LocalDateTime.class);
    }

    protected static Boolean getBoolVal(String name, Row row, RowMetadata rowMetadata) {
        return getVal(name, row, rowMetadata, Boolean.class);
    }

    protected static Float getFloatVal(String name, Row row, RowMetadata rowMetadata) {
        return getVal(name, row, rowMetadata, Float.class);
    }
}
