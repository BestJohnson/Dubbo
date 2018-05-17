package com.kaishengit.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.controller.ResponseBean.ResponseBean;
import com.kaishengit.entity.Product;
import com.kaishengit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String show(@RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo,
                       Model model) {
        PageInfo<Product> pageInfo =  productService.findProductByPageNo(pageNo);

        model.addAttribute("page",pageInfo);

        return "product/home";
    }

    @GetMapping("/new")
    public String newProduct() {

        return "product/new";
    }


    @PostMapping("/new")
    public String newProduct(Product product) {
        productService.saveProduct(product);

        return "redirect:/product";

    }


    @GetMapping("{id}/edit")
    public String update(@PathVariable Integer id,
                         Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product",product);

        return "product/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(Product product) {
        productService.updateProduct(product);

        return "redirect:/product";
    }

    @GetMapping("{id}/del")
    public String delProduct(@PathVariable Integer id) {
        productService.delProductById(id);
        return "redirect:/product";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id,
                       Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product",product);

        return "product/view";
    }

    @GetMapping("/buy/{id}")
    @ResponseBody
    public ResponseBean buyProduct(@PathVariable Integer id) {
        try {
            productService.buyProduct(id);
        } catch (RuntimeException e) {
            return ResponseBean.error(e.getMessage());
        }
        return ResponseBean.success();
    }



}
