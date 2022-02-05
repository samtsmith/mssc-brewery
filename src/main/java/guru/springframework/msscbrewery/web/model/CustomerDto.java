package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    @Null // This is one way to make this "read-only" (ie the client can't update this).
    private UUID id;

    @NotBlank
    @Size(min=3, max=100)
    private String name;

    @Null
    private OffsetDateTime createdDate;

    @Null
    private OffsetDateTime lateUpdatedDate;
}
