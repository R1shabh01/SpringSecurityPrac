package com.example.Practice.Controller;

import com.example.Practice.Model.Product;
import com.example.Practice.Model.Users;
import com.example.Practice.Service.ProductService;
import com.example.Practice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String Hello(){
        return "hi";
    }

    @GetMapping("/product")
    public List<Product> showProduct(){
        return productService.showAllProducts();
    }


    @GetMapping("/product/{id}")
    public Product oneProduct(@PathVariable int id){

        return productService.ById(id);

    }

    @PostMapping("/product")
    public void addProduct(@RequestBody List<Product> products){

        if (products.size() == 1) {
            productService.addProduct(products.get(0));  // Handle single product
        } else {
            productService.addAllProduct(products);  // Handle list of products
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody Users user){
        userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return userService.verify(users);
    }

    @DeleteMapping("/product/{id}")
    public void delProd(@PathVariable int id){
        productService.delProduct(id);

    }

}
