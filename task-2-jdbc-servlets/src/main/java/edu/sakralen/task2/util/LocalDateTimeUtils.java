package edu.sakralen.task2.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class LocalDateTimeUtils {
    private LocalDateTimeUtils() {
    }

    public static boolean isEqualTruncated(LocalDateTime ldt1, LocalDateTime ldt2, TemporalUnit truncationUnit) {
        return Objects.equals(ldt1 != null ? ldt1.truncatedTo(truncationUnit) : null,
                ldt2 != null ? ldt2.truncatedTo(truncationUnit) : null);
    }

    public static Timestamp toTimestampIfPresent(LocalDateTime ldt) {
        return ldt != null ? Timestamp.valueOf(ldt) : null;
    }
}
