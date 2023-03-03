package com.subrutin.catalog.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.subrutin.catalog.dto.*;
import com.subrutin.catalog.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.subrutin.catalog.domain.Author;
import com.subrutin.catalog.domain.Book;
import com.subrutin.catalog.domain.Category;
import com.subrutin.catalog.domain.Publisher;
import com.subrutin.catalog.exception.BadRequestException;
import com.subrutin.catalog.repository.AuthorRepository;
import com.subrutin.catalog.repository.BookRepository;
import com.subrutin.catalog.service.AuthorService;
import com.subrutin.catalog.service.BookService;
import com.subrutin.catalog.service.CategoryService;
import com.subrutin.catalog.service.PublisherService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service("bookService")
public class BookServiceImpl implements BookService{
	

	private final BookRepository bookRepository;
	
	private final AuthorService authorService;
	
	private final CategoryService categoryService;
	
	private final PublisherService publisherService;

	@Override
	public BookDetailResponseDTO findBookDetailById(String bookId) {
		Book book = bookRepository.findBySecureId(bookId)
				.orElseThrow(()-> new BadRequestException("book_id.invalid"));
		BookDetailResponseDTO dto = new BookDetailResponseDTO();
		dto.setBookId(book.getSecureId());
		dto.setCategories(categoryService.constructDTO(book.getCategories()));
		dto.setAuthors(authorService.constructDTO(book.getAuthors()));
		dto.setPublisher(publisherService.constructDTO(book.getPublisher()));
		dto.setBookTitle(book.getTitle());
		dto.setBookDescription(book.getDescription());
		return dto;
	}


	@Override
	public List<BookDetailResponseDTO> findBookListDetail() {
		List<Book> books = bookRepository.findAll();
		return books.stream().map((b)->{
			BookDetailResponseDTO dto = new BookDetailResponseDTO();
//			dto.setAuthorName(b.getAuthor().getName());
			dto.setBookDescription(b.getDescription());
//			dto.setBookId(b.getId());
			dto.setBookTitle(b.getTitle());
			return dto;
		}).collect(Collectors.toList());
	}


	@Override
	public void createNewBook(BookCreateRequestDTO dto) {
		List<Author> authors =  authorService.findAuthors(dto.getAuthorIdList());
		List<Category> categories =  categoryService.findCategories(dto.getCategoryList());
		Publisher publisher = publisherService.findPublisher(dto.getPublisherId());
		Book book = new Book();
		book.setAuthors(authors);
		book.setCategories(categories);
		book.setPublisher(publisher);
		book.setTitle(dto.getBookTitle());
		book.setDescription(dto.getDescription());
		bookRepository.save(book);
		
	}


	@Override
	public void updateBook(Long bookId, BookUpdateRequestDTO dto) {
		//get book from repository
		Book book = bookRepository.findById(bookId)
				.orElseThrow(()-> new BadRequestException("book_id.invalid"));
		//update
		book.setTitle(dto.getBookTitle());
		book.setDescription(dto.getDescription());
		//save
		bookRepository.save(book);
		
	}


	@Override
	public void deleteBook(Long bookId) {
		bookRepository.deleteById(bookId);
		
	}

	@Override
	public ResultPageResponseDTO<BookListResponseDTO> findBookList(Integer page, Integer limit, String sortBy, String direction, String publisherName, String bookTitle, String authorName) {
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(page, limit, sort);
		// Page<Book> pageResult = bookRepository.findBookList(bookTitle, publisherName, authorName, pageable);
		Page<BookQueryDTO> pageResult = bookRepository.findBookList(bookTitle, publisherName, authorName, pageable);
		List<Long> idList = pageResult.stream().map(b -> b.getId()).collect(Collectors.toList());
		Map<Long, List<String>> categoryMap = categoryService.findCategoriesMap(idList);
		Map<Long, List<String>> authorMap = authorService.findAuthorsMap(idList);
		List<BookListResponseDTO> dtos = pageResult.stream().map(b -> {
			BookListResponseDTO dto = new BookListResponseDTO();
			dto.setAuthorName(authorMap.get(b.getId()));
			dto.setCategoryCodes(categoryMap.get(b.getId()));
			// dto.setTitle(b.getTitle());
			dto.setTitle(b.getBookTitle());
			// dto.setPublisherName(b.getPublisher().getName());
			dto.setPublisherName(b.getPublisherName());
			dto.setDescription(b.getDescription());
			// dto.setId(b.getSecureId());
			dto.setId(b.getBookId());
			return dto;
		}).collect(Collectors.toList());
		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

//	public BookRepository getBookRepository() {
//		return bookRepository;
//	}
//
//	public void setBookRepository(BookRepository bookRepository) {
//		this.bookRepository = bookRepository;
//	}
	
	
	
	
}
