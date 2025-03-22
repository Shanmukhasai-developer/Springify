package com.example.EcomProjectSpring.controller;

import com.example.EcomProjectSpring.model.Product;
import com.example.EcomProjectSpring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
}


    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product=service.getProduct(id);
        if(product!=null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



@PostMapping("/product")
public  ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile){
        try {
            Product product1 =service.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
}


@GetMapping("/product/{productId}/image")
public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
       Product product =service.getProduct(productId);
       byte[] imageFile=product.getImageDate();
       return  ResponseEntity.ok()
               .contentType(MediaType.valueOf(product.getImageType()))
               .body(imageFile);
}


@PutMapping("/product/{id}")
public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile) throws IOException {
       Product product1=null;
    try {
        product1 = service.updateProduct(id, product, imageFile);
    } catch (IOException e) {
        return  new ResponseEntity<>("product not found",HttpStatus.BAD_REQUEST);
    }
    if(product1!=null){
            return new ResponseEntity<>("product updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("product not updated",HttpStatus.BAD_REQUEST);
        }
}


@DeleteMapping("/product/{id}")
public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product=service.getProduct(id);
        if(product!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Prodcut was Deleted",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product was Not found",HttpStatus.NOT_FOUND);
        }
}
@GetMapping("/products/search")
public ResponseEntity<List<Product>>searchProducts(@RequestParam String keyword){
        List<Product> products=service.searchProducts(keyword);
        return  new ResponseEntity<>(products,HttpStatus.OK);

    }
}


