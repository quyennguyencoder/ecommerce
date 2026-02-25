package com.nguyenquyen.ecommerce.controller;



import com.github.javafaker.Faker;
import com.nguyenquyen.ecommerce.dtos.ProductDTO;
import com.nguyenquyen.ecommerce.dtos.ProductImageDTO;
import com.nguyenquyen.ecommerce.dtos.response.ProductListResponse;
import com.nguyenquyen.ecommerce.dtos.response.ProductResponse;
import com.nguyenquyen.ecommerce.exception.DataNotFoundException;
import com.nguyenquyen.ecommerce.exception.InvalidParamException;
import com.nguyenquyen.ecommerce.mapper.ProductMapper;
import com.nguyenquyen.ecommerce.models.Product;
import com.nguyenquyen.ecommerce.models.ProductImage;
import com.nguyenquyen.ecommerce.service.intf.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            // validate dto
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return  ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/uploads/{id}")
    public ResponseEntity<?> uploadImages (
            @PathVariable("id") Long productId,
            @RequestPart(value = "files") List<MultipartFile> files
    ) throws DataNotFoundException, IOException, InvalidParamException {
        Product existingProduct = productService.getProductById(productId);
        files = files == null ? new ArrayList<>() : files;
        if(files.size() > ProductImage.MAXIMUM_IMAGE_PER_PRODUCT){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("max image upload is " + ProductImage.MAXIMUM_IMAGE_PER_PRODUCT);
        }
        List<ProductImage> uploadedImages = new ArrayList<>();
        for(MultipartFile file : files) {
            if(file.getSize()==0){
                continue;
            }
            if(file.getSize() >10 * 1024 * 1024){
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("max file size is 10MB");
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")){
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("only image files are allowed");
            }
            // store file
            String storedFilename = storeFile(file);

            // lưu ảnh vào database với product tương ứng
            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                    .productId(existingProduct.getId())
                    .imageUrl(storedFilename)
                    .build();
            ProductImage productImage = productService.createProductImage(productImageDTO);
            uploadedImages.add(productImage);
        }
        return ResponseEntity.ok(uploadedImages);
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("createdAt").descending()
        );

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        ProductListResponse response = ProductListResponse.builder()
                .totalPages(totalPages)
                .products(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        Product existingProduct = productService.getProductById(id);
        ProductResponse productResponse = productMapper.productToProductResponse(existingProduct);
        productResponse.setCreatedAt(existingProduct.getCreatedAt());
        productResponse.setUpdatedAt(existingProduct.getUpdatedAt());
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProductDTO productDTO

    ) {
        Product productUpdated = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(productUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
    }

    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();

        for(int i = 0; i < 100; i++){
            String fakeProductName = faker.commerce().productName();
            if(productService.existsByName(fakeProductName)){
                continue;
            }

            ProductDTO productDTO = ProductDTO.builder()
                    .name(fakeProductName)
                    .price((float) faker.number().numberBetween(0, 100000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1,5))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("Fake products generated");
    }


    private String storeFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            throw new IOException("only image files are allowed");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String newFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), newFilename);
        // copy file vào đường dẫn
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return newFilename;
    }


}
