package com.marketplace.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "CRUD REST API for product in Mercora MarketPlace",
        description = "CRUD Rest Api Create,Fetch,Update,Delete  Product details"
)
@RestController
@RequestMapping("/api/products")
public class ProductController {


}