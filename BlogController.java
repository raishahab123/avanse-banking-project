package com.avanse.springboot.controller.globalPages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.avanse.springboot.model.Post;
import com.avanse.springboot.service.PostCategoryService;
import com.avanse.springboot.service.PostService;

@Controller
public class BlogController {

	@Autowired
	PostService postService;
	@Autowired
	PostCategoryService postCategoryService;

	@GetMapping("/blog/{extractedFileName}")
	public ModelAndView getAddedBlogPost(@PathVariable("extractedFileName") String extractedFileName, Model model) {
		//System.out.println("Into the getAddBlogPost Get");
		ModelAndView modelAndView = new ModelAndView("addedBlogPosts/"+extractedFileName);
		model.addAttribute("postCategories", postCategoryService.getAllPostCategories());
		model.addAttribute("posts", postService.getTopFourLatestPosts());
		model.addAttribute("relatedThreePosts",postService.randomThreePosts());

		return modelAndView;
	}
	
	@GetMapping(path = "/public/api/post/getPostCountInDb")
	@ResponseBody
	@CrossOrigin("*")
	public long getPostCountInDb() {
		return postService.numberOfPosts();
	}
	
	@GetMapping(path = "/public/api/post/getPostsInRange/{lowerLimit}/{upperLimit}")
	@ResponseBody
	@CrossOrigin("*")
	public List<Post> getPostsInRange(@PathVariable("lowerLimit") long lowerLimit, @PathVariable("upperLimit") long upperLimit) {
		return postService.getPostsInRange(lowerLimit, upperLimit);
	}
	@GetMapping(path = "/public/api/post/getPostsSearchResults/{searchKey}")
	@ResponseBody
	@CrossOrigin("*")
	public List<Post> getPostSearchResults(@PathVariable("searchKey") String searchKey) {
		return postService.getSearchResultsInBlogs(searchKey);
	}
	@GetMapping(path = "/public/api/post/getAllPosts")
	@ResponseBody
	@CrossOrigin("*")
	public List<Post> getAllPosts() {
		return postService.getAllPosts();
	}
	
	
	
	
	
	
	
}
