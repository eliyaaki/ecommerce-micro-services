package come.ecommercemicroservices.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    @NotBlank(message = "id is a required field") private String id;
    @NotBlank(message = "name is a required field") private String name;
    @NotBlank(message = "description is a required field") private String description;
    @NotNull(message = "price is a required field") private BigDecimal price;
}
