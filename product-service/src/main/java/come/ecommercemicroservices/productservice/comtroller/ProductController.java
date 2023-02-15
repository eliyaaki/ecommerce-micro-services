package come.ecommercemicroservices.productservice.comtroller;


import come.ecommercemicroservices.productservice.dto.ProductRequestDto;
import come.ecommercemicroservices.productservice.dto.ProductResponseDto;
import come.ecommercemicroservices.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
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
    public void createProduct(@RequestHeader("authorities") String[] authorities, @RequestHeader("username") String userName, @RequestBody @Valid ProductRequestDto productRequestDto) {
        log.error("userName:  "+userName);
        var userAuthoritiesList= Arrays.stream(authorities).toList();
        log.error("authorities:  "+userAuthoritiesList);
        productService.addProduct(productRequestDto);
    }


}




