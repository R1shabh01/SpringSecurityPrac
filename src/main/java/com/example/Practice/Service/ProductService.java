package com.example.Practice.Service;

import com.example.Practice.Model.Product;
import com.example.Practice.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;


    public List<Product> showAllProducts() {
        List<Product> lp = productRepo.findAll();
        if(!lp.isEmpty()){
            return lp;
        }else{
            throw new RuntimeException("FETCH NOT WORKING");
        }
    }

    public Product ById(int id) {

        return productRepo.findById(id).orElse(null);

    }

    public void addProduct(Product product) {
         productRepo.save(product);
    }

    public void addAllProduct(List<Product> products) {
        productRepo.saveAll(products);
    }

    public void delProduct(int id) {
        productRepo.deleteById(id);
    }
}
