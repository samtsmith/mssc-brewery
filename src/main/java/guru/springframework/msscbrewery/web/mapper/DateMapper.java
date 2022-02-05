package guru.springframework.msscbrewery.web.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateMapper {
    public OffsetDateTime asOffsetDateTime(Timestamp timeStamp) {
        if (timeStamp != null) {
            return OffsetDateTime.of(
                    timeStamp.toLocalDateTime().getYear(),
                    timeStamp.toLocalDateTime().getMonthValue(),
                    timeStamp.toLocalDateTime().getDayOfMonth(),
                    timeStamp.toLocalDateTime().getHour(),
                    timeStamp.toLocalDateTime().getMinute(),
                    timeStamp.toLocalDateTime().getSecond(),
                    timeStamp.toLocalDateTime().getNano(),
                    ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime != null) {
            return Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        } else {
            return null;
        }
    }
}
