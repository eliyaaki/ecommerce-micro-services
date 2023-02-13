package come.ecommercemicroservices.productservice.service;


import come.ecommercemicroservices.productservice.dto.ProductRequestDto;
import come.ecommercemicroservices.productservice.dto.ProductResponseDto;
import come.ecommercemicroservices.productservice.model.Product;
import come.ecommercemicroservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponseDto getProductById(String id) throws Exception {
        var product= productRepository.findById(id).orElseThrow(() -> new Exception("user not found"));;
        return ProductResponseDto.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public List<ProductResponseDto> getAllProducts() {
       return productRepository.findAll().stream().map(p->{
          return ProductResponseDto.builder()
                   .id(p.getId())
                   .description(p.getDescription())
                   .name(p.getName())
                   .price(p.getPrice())
                   .build();
       }).collect(Collectors.toList());
    }

    public void addProduct(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .description(productRequestDto.getDescription())
                .name(productRequestDto.getName())
                .price(productRequestDto.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product " + product.getId() + "is saved successfully to the mongo database");
    }
}
