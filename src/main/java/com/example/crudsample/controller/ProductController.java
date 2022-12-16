package com.example.crudsample.controller;

import com.example.crudsample.dao.CategoryDao;
import com.example.crudsample.dao.ProductDao;
import com.example.crudsample.ds.Category;
import com.example.crudsample.ds.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ProductDao productDao;

    @GetMapping("/category")
    public String categoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    @PostMapping("/category")
    public String saveCategory(Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "category-form";
        }
        categoryDao.save(category);
        return "redirect:/list-category";
    }

    @GetMapping("/list-category")
    public String listCategory(Model model) {
        model.addAttribute("categories", categoryDao.findAll());
        return "list-category";
    }

    /*public String productForm(Model model) {

    }*/
    @GetMapping("/product")
    public ModelAndView productForm(@ModelAttribute("categories")List<Category> categories) {
        ModelAndView mv = new ModelAndView("product-form", "product", new Product());
        mv.addObject("listcategories", categoryDao.findAll());
        return mv;
    }
    @PostMapping("/product")
    @Transactional
    public String saveProduct(Product product, BindingResult result) {
        if(result.hasErrors()) {
            return "product-form";
        }
        Category category = categoryDao.findById(product.getCategory().getId()).get();
        category.addProduct(product);
        productDao.save(product);
        return "redirect:/list-products";
    }
    @GetMapping("/list-products")
    public String listProducts(Model model) {
        model.addAttribute("products", productDao.findAll());
        return "list-products";
    }
    @ModelAttribute("categories")
    public List<Category> listCategory() {
        return categoryDao.findAll();
    }
}
