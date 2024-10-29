package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.*;
import vn.iotstar.model.*;
import vn.iotstar.service.categoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/admin/category")
public class adminController {
	@Autowired
	categoryService cateservice;
	@RequestMapping("")
	public String all(Model model) {
		List<Category> list = cateservice.findAll();
		model.addAttribute("list",list);
		return "views/admin/category/list";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		categoryModel cateModel = new categoryModel();
		cateModel.setEdit(false);
		model.addAttribute("category",cateModel);
		return "views/admin/category/add";
	}
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, 
			@Valid @ModelAttribute("category") categoryModel cateModel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("forward:/admin/category",model);
		}
		Category entity = new Category();
		BeanUtils.copyProperties(cateModel, entity);
		if (entity.getImage() == "" || entity.getImage() == null)
		{
			Optional<Category> opt = cateservice.findById(entity.getId());
			Category temp = opt.get();
			entity.setImage(temp.getImage());
		}
		cateservice.save(entity);
		String message="";
		if (cateModel.isEdit() == true)
		{
			message = "Category is Edited!!!!!";
		}
		else {
			message = "Category is saved!!!!";
		}
		model.addAttribute("message",message);
		
		return new ModelAndView("forward:/admin/category",model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateservice.findById(categoryId);
		categoryModel cateModel = new categoryModel();
		if (optCategory.isPresent())
		{
			Category entity = optCategory.get();
			
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setEdit(true);
			
			model.addAttribute("category",cateModel);
			
			return new ModelAndView("views/admin/category/add", model);
		}
		model.addAttribute("message","Category is not existed!!");
		return new ModelAndView("forward:/admin/category",model);
		
	}
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateservice.findById(categoryId);
		if (optCategory.isPresent())
		{
			cateservice.deleteById(categoryId);
			List<Category> list = cateservice.findAll();
			model.addAttribute("list",list);
			return new ModelAndView("views/admin/category/list", model);
		}
		model.addAttribute("message","Category is not existed!!");
		return new ModelAndView("forward:/admin/category",model);
		
	}
	
	@GetMapping("/search")
	public String search(Model model)
	{
		String name = "";
		model.addAttribute("name",name);
		return "views/admin/category/search";
	}
	
}
