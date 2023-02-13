package come.ecommercemicroservices.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "name is a required field") private String name;
    @NotBlank(message = "description is a required field") private String description;
    @NotNull(message = "price is a required field") private BigDecimal price;
}
