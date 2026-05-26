package com.portafolio.zomtg.salesflow.products.mapper;

import com.portafolio.zomtg.salesflow.products.dto.ProductRequest;
import com.portafolio.zomtg.salesflow.products.dto.ProductResponse;
import com.portafolio.zomtg.salesflow.products.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductRequest request){
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImage(request.image());
        product.setCategory(request.category());
        product.setExistence(request.existence());
        product.setActive(request.active());
        product.setStoreId(request.storeId());
        return product;

    }

    public ProductResponse toProductResponse(Product product){
        ProductResponse response= new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.getCategory(),
                product.getExistence(),
                product.isActive(),
                product.getStoreId()
        );
        return response;
    }
}
