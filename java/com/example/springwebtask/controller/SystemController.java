package com.example.springwebtask.controller;

import com.example.springwebtask.entity.ProductRecord;
import com.example.springwebtask.entity.SessionUser;
import com.example.springwebtask.form.DetailForm;
import com.example.springwebtask.form.Form;
import com.example.springwebtask.form.LoginForm;
import com.example.springwebtask.service.ICategoryService;
import com.example.springwebtask.service.IProductService;
import com.example.springwebtask.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;

@Controller
public class SystemController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    // ログイン画面
    @GetMapping("/index")
    public String index(@ModelAttribute("loginForm") LoginForm loginForm){
        return "index";
    }
    @PostMapping("/index")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "index";
        }

        var user = userService.findUser(loginForm.getLoginId(), loginForm.getPassword());
        if(user == null) {
            model.addAttribute("errorMsg", "IDまたはパスワードが不正です。");
            return "index";
        } else {
            var loginUser = new SessionUser(user.id(), user.loginId(), user.name(), user.role());
            session.setAttribute("user", loginUser);
            return "redirect:/menu";
        }
    }
    @GetMapping("/menu")
    public String menu(@RequestParam(name="keyword", defaultValue="all") String key,
                       @RequestParam(name="order", defaultValue = "product_id,ASC") String order,
                       @ModelAttribute("successMsg") String successMsg, Model model){
        if(request.getSession(false)==null) return "redirect:/index";
        var recordNum = 0;
        if(key.equals("all") || key.replaceAll(" |　", "").equals("")) {
            var productAll = productService.findAllSort(order.split(",")[0], order.split(",")[1]);
            var categoryAll = categoryService.findAll();
            model.addAttribute("products", productAll);
            model.addAttribute("categories", categoryAll);
            recordNum = productAll.size();
        } else {
            var keys = Arrays.asList(key.replaceAll(" |　", " ").split(" "));
            var products = productService.findByNameSort(keys,order.split(",")[0], order.split(",")[1]);
            if (products != null) {
                var categoryAll = categoryService.findAll();
                model.addAttribute("products", products);
                model.addAttribute("categories", categoryAll);
                recordNum = products.size();
            }
        }
        model.addAttribute("recordNum", recordNum);
        model.addAttribute("successMsg", successMsg);
        return "menu";
    }
    @GetMapping("/logout")
    public String logout(){
        session.invalidate();
        return "logout";
    }
    @GetMapping("/insert")
    public String dispInsert(@ModelAttribute("insertForm") Form insertForm, Model model){
        if(request.getSession(false)==null) return "redirect:/index";
        var list = categoryService.findAll();
        model.addAttribute("categories", list);

        return "insert";
    }
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("insertForm") Form insertForm, BindingResult bindingResult, Model model){
        var list = categoryService.findAll();
        model.addAttribute("categories", list);

        if(bindingResult.hasErrors()){
            return "insert";
        }
        // 重複する商品IDがあるか調べる
        var recordNum = productService.findByProductId(insertForm.getProductId());
        if(recordNum == null) {
            MultipartFile file = insertForm.getFile();
            System.out.println(file);
            var insertProduct = new ProductRecord(-1, insertForm.getProductId(),
                    Integer.valueOf(insertForm.getCategory()),
                    insertForm.getName(), insertForm.getPrice(), file.getOriginalFilename(),
                    insertForm.getDescription(), null, null);
            productService.insert(insertProduct);
            // 画像保存
            insertImgFile(file);
            return "redirect:insertSuccess";
        } else {
            model.addAttribute("errorMsg", "商品IDが重複しています");
            return "insert";
        }
    }
    @GetMapping("/insertSuccess")
    public String insertSuccess(){
        if(request.getSession(false)==null) return "redirect:/index";
        return "insertSuccess";
    }
    @GetMapping("/detail/{id}")
    public String dispDetail(@ModelAttribute("form") DetailForm form, @PathVariable("id") int id, Model model){
        if(request.getSession(false)==null) return "redirect:/index";
        var product = productService.findById(id);
        form.setProductId(product.productId());
        form.setName(product.name());
        form.setPrice(product.price());
        form.setCategory(categoryService.findById(product.categoryId()).name());
        form.setDescription(product.description());
        model.addAttribute("id", product.id());
        model.addAttribute("imgName", product.imagePath());

        // データベースにファイルパスがない場合、画像を表示しない
        if(product.imagePath()!=null && !product.imagePath().equals("")) {
            File img = new File("./src/main/resources/static/img/" + product.imagePath());
            try {
                byte[] byteImg = Files.readAllBytes(img.toPath());
                String base64Data = Base64.getEncoder().encodeToString(byteImg);
                model.addAttribute("base64Data", "data:img/png;base64," + base64Data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "detail";
    }
    @PostMapping("/detail/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model){
        var result = productService.delete(productService.findById(id).productId());
        if(result == 0){
            model.addAttribute("errorMsg", "削除に失敗しました");
            return "/detail/{productId}";
        } else {
            redirectAttributes.addFlashAttribute("successMsg", "削除に成功しました");
            return "redirect:../menu";
        }
    }
    @GetMapping("/updateInput/{id}")
    public String dispUpdateInput(@ModelAttribute("form") Form updForm, @PathVariable("id")  int id, Model model){
        if(request.getSession(false)==null) return "redirect:/index";
        var product = productService.findById(id);
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        updForm.setProductId(product.productId());
        updForm.setName(product.name());
        updForm.setPrice(product.price());
        updForm.setCategory(categoryService.findById(product.categoryId()).name());
        updForm.setDescription(product.description());
        return "updateInput";
    }

    @PostMapping("/updateInput/{id}")
    public String updateInput(@Validated @ModelAttribute("form") Form updForm, BindingResult bindingResult,
                              @PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model){
        model.addAttribute("id", id);
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        if(bindingResult.hasErrors()){
            return "/updateInput";
        }

        // 重複する商品IDがあるか調べる
        var record = productService.findById(id);
        var compRecord = productService.findByProductId(updForm.getProductId());
        if(compRecord == null || record.productId().equals(updForm.getProductId())) {
            MultipartFile file = updForm.getFile();
            var updateProduct = new ProductRecord(record.id(), updForm.getProductId(),
                    categoryService.findByName(updForm.getCategory()).id(),
                    updForm.getName(), updForm.getPrice(), file.getOriginalFilename(),
                    updForm.getDescription(), null, null);
            productService.update(updateProduct);
            insertImgFile(file);
            redirectAttributes.addFlashAttribute("successMsg", "更新に成功しました");
            return "redirect:../menu";
        } else {
            model.addAttribute("errorMsg", "商品IDが重複しています");
            return "/updateInput";
        }
    }

    public void insertImgFile(MultipartFile file){
        final String UPLOAD_DIR = "./src/main/resources/static/img"; // アップロード先のディレクトリ

        try {
            if(!file.getOriginalFilename().equals("")) {
                // 画像ファイルの保存先パス
                String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
                // ファイルをディスクに保存
                Path destination = new File(filePath).toPath();
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
