package com.esmt.lms.restcontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esmt.lms.common.Constants;
import com.esmt.lms.model.Book;
import com.esmt.lms.model.Issue;
import com.esmt.lms.model.IssuedBook;
import com.esmt.lms.model.Member;
import com.esmt.lms.service.BookService;
import com.esmt.lms.service.IssueService;
import com.esmt.lms.service.IssuedBookService;
import com.esmt.lms.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Issue Management", description = "APIs for managing book issues")
@RestController
@RequestMapping(value = "/rest/issue")
public class IssueRestController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private IssuedBookService issuedBookService;


	@Operation(summary = "Save issue", description = "Save a new issue with books")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Issue saved successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Member or book not found")
	})
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String save(@RequestParam Map<String, String> payload) {
		
		String memberIdStr = (String)payload.get("member");
		String[] bookIdsStr = payload.get("books").toString().split(",");
		
		Long memberId = null;
		List<Long> bookIds = new ArrayList<Long>();
		try {
			memberId = Long.parseLong( memberIdStr );
			for(int k=0 ; k<bookIdsStr.length ; k++) {
				bookIds.add( Long.parseLong(bookIdsStr[k]) );
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return "invalid number format";
		}
		
		Member member = memberService.get( memberId );
		List<Book> books = bookService.get(bookIds);
		
		Issue issue = new Issue();
		issue.setMember(member);
		issue = issueService.addNew(issue);
		
		for( int k=0 ; k<books.size() ; k++ ) {
			Book book = books.get(k);
			book.setStatus( Constants.BOOK_STATUS_ISSUED );
			book = bookService.save(book);
			
			IssuedBook ib = new IssuedBook();
			ib.setBook( book );
			ib.setIssue( issue );
			issuedBookService.addNew( ib );
			
		}
		
		return "success";
	}


	@Operation(summary = "Return all books for an issue", description = "Mark all books for an issue as returned")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Books returned successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid issue ID supplied"),
			@ApiResponse(responseCode = "404", description = "Issue not found")
	})
	@RequestMapping(value = "/{id}/return/all", method = RequestMethod.GET)
	public String returnAll(@PathVariable(name = "id") Long id) {
		Issue issue = issueService.get(id);
		if( issue != null ) {
			List<IssuedBook> issuedBooks = issue.getIssuedBooks();
			for( int k=0 ; k<issuedBooks.size() ; k++ ) {
				IssuedBook ib = issuedBooks.get(k);
				ib.setReturned( Constants.BOOK_RETURNED );
				issuedBookService.save( ib );
				
				Book book = ib.getBook();
				book.setStatus( Constants.BOOK_STATUS_AVAILABLE );
				bookService.save(book);
			}
			
			issue.setReturned( Constants.BOOK_RETURNED );
			issueService.save(issue);
			
			return "successful";
		} else {
			return "unsuccessful";
		}
	}

	@Operation(summary = "Return selected books for an issue", description = "Mark selected books for an issue as returned")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Selected books returned successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid issue ID or book IDs supplied"),
			@ApiResponse(responseCode = "404", description = "Issue or books not found")
	})
	@RequestMapping(value="/{id}/return", method = RequestMethod.POST)
	public String returnSelected(@RequestParam Map<String, String> payload, @PathVariable(name = "id") Long id) {
		Issue issue = issueService.get(id);
		String[] issuedBookIds = payload.get("ids").split(",");
		if( issue != null ) {
			
			List<IssuedBook> issuedBooks = issue.getIssuedBooks();
			for( int k=0 ; k<issuedBooks.size() ; k++ ) {
				IssuedBook ib = issuedBooks.get(k);
				if( Arrays.asList(issuedBookIds).contains( ib.getId().toString() ) ) {
					ib.setReturned( Constants.BOOK_RETURNED );
					issuedBookService.save( ib );
					
					Book book = ib.getBook();
					book.setStatus( Constants.BOOK_STATUS_AVAILABLE );
					bookService.save(book);
				}
			}
			
			return "successful";
		} else {
			return "unsuccessful";
		}
	}
	
}
