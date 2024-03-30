package com.esmt.lms.controller;

import java.util.List;

import com.esmt.lms.model.Book;
import com.esmt.lms.model.Issue;
import com.esmt.lms.model.Member;
import com.esmt.lms.service.BookService;
import com.esmt.lms.service.CategoryService;
import com.esmt.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.esmt.lms.common.Constants;
import com.esmt.lms.model.Category;
import com.esmt.lms.service.IssueService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/issue")
public class IssueController {

	@Autowired
	private IssueService issueService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private BookService bookService;
	
	@ModelAttribute(name = "memberTypes")
	public List<String> memberTypes() {
		return Constants.MEMBER_TYPES;
	}
	
	@ModelAttribute(name = "categories")
	public List<Category> getCategories() {
		return categoryService.getAllBySort();
	}

	@ModelAttribute(name = "members")
	public List<Member> getMembers(){return memberService.getAll();}

	@ModelAttribute(name = "books")
	public List<Book> getBooks(){return bookService.getAll();}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listIssuePage(Model model) {
		model.addAttribute("issues", issueService.getAllUnreturned());
		return "/issue/list";
	}

	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newIssuePage(Model model) {
		model.addAttribute("issue", new Issue());
		return "/issue/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveIssue(Issue issue, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//		if( bindingResult.hasErrors() ) {
//			return "/issue/form";
//		}

		if( issue.getId() == null ) {
			issueService.addNew(issue);
			redirectAttributes.addFlashAttribute("successMsg", " Cet emprunt a été enregistré");
			return "redirect:/issue/new";
		} else {
			Issue updateIssue = issueService.save(issue);
			redirectAttributes.addFlashAttribute("successMsg", "Cet emprunt a été mis à jour");
			return "redirect:/issue/edit/"+updateIssue.getId();
		}
	}
	
}
