package com.youtube.jwt.controller;

import com.youtube.jwt.entity.ImageModel;
import com.youtube.jwt.entity.Product;
import com.youtube.jwt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product addNewProduct(@RequestPart("product") Product product,
                                 @RequestPart("imageFile")MultipartFile[]file){
       // return productService.addNewProduct(product);

        try {
            Set<ImageModel> images = uploadImage(file);
            product.setProductImages(images);
            return productService.addNewProduct(product);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file : multipartFiles){
            ImageModel imageModel = new ImageModel();
            imageModel.setName(file.getName());
            imageModel.setPicByte(file.getBytes());
            imageModel.setType(file.getContentType());
            imageModels.add(imageModel);
        }

        return imageModels;
    }



    @GetMapping({"/getAllProducts"})
    public List<Product>  getAllProducts(@RequestParam (defaultValue = "0") int pageNumber,
                                         @RequestParam (defaultValue = "")String searchKey){
        return productService.getAllProducts(pageNumber,searchKey);
    }


    @GetMapping({"/getProductByDetailsByProductId/{productId}"})
    public Product getProductByProductId(@PathVariable("productId") Integer productId){
       return productService.getProductdetailsById(productId);

    }

    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public void deleteProductDetails(@PathVariable("productId") Integer productId){
        productService.deleteProductDetails(productId);

    }


    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
    @PreAuthorize("hasRole('User')")
    public List<Product> getProductDetails(@PathVariable("isSingleProductCheckout") boolean isSingleProductCheckout ,
                                           @PathVariable("productId") Integer productId){
        return productService.getProductDetails(isSingleProductCheckout,productId);

    }

}
