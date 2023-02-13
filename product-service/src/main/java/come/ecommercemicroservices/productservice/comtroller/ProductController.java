package come.ecommercemicroservices.productservice.comtroller;


import come.ecommercemicroservices.productservice.dto.ProductRequestDto;
import come.ecommercemicroservices.productservice.dto.ProductResponseDto;
import come.ecommercemicroservices.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;


    @GetMapping("/GetProductById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto GetUserByEmail(@PathVariable @Valid String id) {
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllProducts")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }


    @PostMapping("/createProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
         productService.addProduct(productRequestDto);
    }

}




