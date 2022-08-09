package com.shop.product;

import com.shop.Brand.BrandService;
import com.shop.entity.Brand;
import com.shop.entity.Product;
import com.shop.entity.ProductImage;
import com.shop.user.UploadFileUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ProductController {

  //  private static  final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public String listAll(Model model){

        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts",listProducts);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){

        List<Brand> listBrands = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("product",product);
        model.addAttribute("pageTitle","Create New Product");

        return "products/products_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes
            ra,@RequestParam("fileImage") MultipartFile mainImageMultiPart,
    @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
    @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames

    ) throws IOException {
        setMainImageName(mainImageMultiPart,product);
        setExistingExtraImageNames(imageIDs,imageNames,product);
        setExtraImageNames(extraImageMultiparts,product);
        setProductDetails(detailIDs,detailNames,detailValues,product);


            Product savedProduct = service.save(product);
            saveUploadedImages(mainImageMultiPart,extraImageMultiparts,savedProduct);

            deleteExtraImagesWereRemoveOnForm(product);



        ra.addFlashAttribute("message","The product is saved");
        return "redirect:/products";
    }

    private void deleteExtraImagesWereRemoveOnForm(Product product) throws IOException {
        String extraImageDir = "../product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);
        try{
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();
                if (!product.containsImageName(fileName)){
                    try {
                        Files.delete(file);
                        System.out.println("Directory deleted"+fileName);
                    } catch (IOException e) {
                       e.printStackTrace();
                    }
                }
            });

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
         if (imageIDs == null || imageIDs.length == 0 ) return;

         Set<ProductImage> images = new HashSet<>();

         for (int count = 0; count < imageIDs.length; count++){
             Integer id = Integer.parseInt(imageIDs[count]);
             String name = imageNames[count];
             images.add( new ProductImage(id,name,product));
         }
         product.setImages(images);
    }

    private void setProductDetails(String[] detailIDs ,String[] detailNames, String[] detailValues, Product product) {
         if (detailNames == null || detailNames.length == 0) return;
         for (int count = 0 ; count < detailNames.length; count++){
             String name = detailNames[count];
             String value = detailValues[count];
             Integer id =Integer.parseInt(detailIDs[count]);
               if (id != 0){
                   product.addDetail(id,name,value);
               }else if (!name.isEmpty() && !value.isEmpty()){
                   product.addDetail(name,value);
               }

         }
    }

    private void saveUploadedImages(MultipartFile mainImageMultiPart,
                                    MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            String uploadDir = "../product-images/"+ savedProduct.getId();

            UploadFileUtil.cleanDirectory(uploadDir);
            UploadFileUtil.saveFile(uploadDir,fileName,mainImageMultiPart);
        }
        if (extraImageMultiparts.length > 0) {
            String uploadDir = "../product-images/"+ savedProduct.getId()+"/extras";
            for (MultipartFile multipartFile : extraImageMultiparts) {
                if (multipartFile.isEmpty()) continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                UploadFileUtil.saveFile(uploadDir,fileName,multipartFile);
            }
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product){
         if (extraImageMultiparts.length > 0){

             for (MultipartFile multipartFile : extraImageMultiparts){
                 if (!multipartFile.isEmpty()){
                     String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                     if (!product.containsImageName(fileName)){
                         product.addExtraImage(fileName);
                     }

                 }
             }
         }
     }

    private void setMainImageName(MultipartFile mainImageMultiPart , Product product){
        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id")Integer id,@PathVariable("status") boolean enabled,
                                              RedirectAttributes ra){

        service.updateProductEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Product ID "+id+ " is "+status;
        ra.addFlashAttribute("message",message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id")Integer id, Model model,RedirectAttributes ra) throws IOException {

        service.delete(id);
        String productExtraImageDir = "../product-images/"+id+"/extras";
        String productImageDir = "../product-images/"+id;
        UploadFileUtil.removeDir(productExtraImageDir);
        UploadFileUtil.removeDir(productImageDir);
        ra.addFlashAttribute("message","The Product ID "+id+" is Deleted");
        return "redirect:/products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model){
        try{
            Product product = service.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product",product);
            model.addAttribute("listBrands",listBrands);
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
            model.addAttribute("pageTitle","Edit Product of (ID : "+id+ ")");
            return "products/products_form";
        }catch (NullPointerException nsa){
            nsa.getMessage();
        }
        return "redirect:/products";

    }
}
