package shop.mtcoding.orange.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.orange.model.Product;
import shop.mtcoding.orange.model.ProductRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductCotroller {

    @Autowired // type으로 찾아냄
    private ProductRepository productRepository;

    @Autowired
    private HttpSession session;

    // @GetMapping("/api/product/{name}")
    // @ResponseBody
    // public Product apiFindOneProduct(@PathVariable String name) {
    // Product product = productRepository.findOne(name);
    // return product;
    // }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/redirect") // redirect 연습
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException { // 디펜더스 의존성 주입
        session = request.getSession();
        session.setAttribute("name", "session metacoding");
        request.setAttribute("name", "metacoding"); // hash맵 이용
        response.sendRedirect("/test");
    }

    @GetMapping("/dispatcher") // dispatcher 연습
    public void dispatcher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // 디펜더스 의존성 주입
        session = request.getSession();
        session.setAttribute("name", "session metacoding");
        request.setAttribute("name", "metacoding"); // hash맵 이용
        RequestDispatcher dis = request.getRequestDispatcher("test");
        dis.forward(request, response);

    }

    @GetMapping("/api/product")
    @ResponseBody
    public List<Product> findAll() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }

    @GetMapping({ "/", "/product" })
    // public String findAllProduct(HttpServletRequest request) {
    public String findAll(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("productList", productList);
        return "product/main"; // request가 새로만들어 지면서 덮어씌어진다. (프레임워크)
        // return "redirect:/" 이건 덮어씌우지 않고 리다이렉트한다.
    }

    @GetMapping("/product/{id}")
    // public String findAllProduct(HttpServletRequest request) {
    public String findOne(@PathVariable int id, Model model) {
        Product product = productRepository.findOne(id);
        model.addAttribute("product", product);
        return "product/detail";
    }

    @GetMapping("product/addForm")
    public String addForm() {
        return "product/addForm";
    }

    @PostMapping("product/add")
    public String add(String name, int price, int qty) {
        int result = productRepository.insert(name, price, qty);
        if (result == 1) {
            return "redirect:/product";
        } else {
            return "redirect:/product/addForm";
        }
    }

    @PostMapping("product/{id}/delete")
    public String delete(@PathVariable int id) {
        int result = productRepository.delete(id);
        if (result == 1) {
            return "redirect:/product";
        } else {
            return "redirect:/product/" + id;
        }
    }

    @GetMapping("product/{id}/updateForm")
    public String update(@PathVariable int id, Model model) {
        Product product = productRepository.findOne(id);
        model.addAttribute("product", product);
        return "product/updateForm";
    }

    @PostMapping("/product/{id}/update")
    public String update(
            @PathVariable int id,
            String name,
            int price,
            int qty) {
        int result = productRepository.update(id, name, price, qty);
        if (result == 1) {
            return "redirect:/product/" + id;
        } else {
            return "redirect:/product/" + id + "/updateForm";
        }
    }

}
