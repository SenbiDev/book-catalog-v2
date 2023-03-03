package com.subrutin.catalog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.subrutin.catalog.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.subrutin.catalog.domain.Category;
import com.subrutin.catalog.exception.BadRequestException;
import com.subrutin.catalog.repository.CategoryRepository;
import com.subrutin.catalog.service.CategoryService;
import com.subrutin.catalog.util.PaginationUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepository categoryRepository;

	@Override
	// public void createAndUpdateCategory(CategoryCreateUpdateRequestDTO dto) {
	public void createAndUpdateCategory(CategoryCreateUpdateRecordDTO dto) {
		 // Category category =  categoryRepository.findByCode(dto.getCode().toLowerCase()).orElse(new Category());
		Category category =  categoryRepository.findByCode(dto.code().toLowerCase()).orElse(new Category());
		if(category.getCode()==null) {
			 // category.setCode(dto.getCode().toLowerCase()); //new
			category.setCode(dto.code().toLowerCase());
		 }
		 // category.setName(dto.getName());
		 category.setName(dto.name());
		 // category.setDescription(dto.getDescription());
		category.setDescription(dto.description());
		 
		 categoryRepository.save(category);
	}

	@Override
	public ResultPageResponseDTO<CategoryListResponseDTO> findCategoryList(Integer pages, Integer limit, String sortBy,
			String direction, String categoryName) {
		categoryName =  StringUtils.isEmpty(categoryName) ? "%":categoryName+"%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);
		Page<Category> pageResult =  categoryRepository.findByNameLikeIgnoreCase(categoryName, pageable);
		List<CategoryListResponseDTO> dtos =  pageResult.stream().map((c)->{
			CategoryListResponseDTO dto = new CategoryListResponseDTO();
			dto.setCode(c.getCode());
			dto.setName(c.getName());
			dto.setDescription(c.getDescription());
			return dto;
		}).collect(Collectors.toList());
		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Override
	public List<Category> findCategories(List<String> categoryCodeList) {
		List<Category> categories= categoryRepository.findByCodeIn(categoryCodeList);
		if(categories.isEmpty()) throw new BadRequestException("category cant empty");
		return categories;
	}

	@Override
	public List<CategoryListResponseDTO> constructDTO(List<Category> categories) {
		return categories.stream().map((c)->{
			CategoryListResponseDTO dto = new CategoryListResponseDTO();
			dto.setCode(c.getCode());
			dto.setName(c.getName());
			dto.setDescription(c.getDescription());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Map<Long, List<String>> findCategoriesMap(List<Long> bookIdList) {
		List<CategoryQueryDTO> queryList = categoryRepository.findCategoryByBookIdList(bookIdList);
		Map<Long, List<String>> categoryMaps = new HashMap<>();
		List<String> categoryCodeList = null;

		// example queryList = [5,3,4,6,2,5]
		for(CategoryQueryDTO q: queryList) {
			// apakah salah satu id belum terdapat pada isi categoryMaps ?
			if (!categoryMaps.containsKey(q.getBookId())) {
				// jika iya (belum terdapat pada isinya) buat list baru
				categoryCodeList = new ArrayList<>();
			} else {
				// jika salah (sudah terdapat pada isinya), maka list diisi dari value-nya categoryMap berdasarkan id-nya
				categoryCodeList = categoryMaps.get(q.getBookId());
			}
			// list diisi dengan daftar kode kategori berdasarkan id book-nya
			categoryCodeList.add(q.getCategoryCode());
			// map diisi dengan key dari book id dan value diisi dari daftar kategori kode
			// jika id belum terdapat pada map, maka map diisi dengan key dengan book id baru dan value baru dari daftar kategori kode berdasarkan id-nya melalui kode 'categoryCodeList = new ArrayList<>();'
			// jika id sudah terdapat pada map, maka map diisi (ditimpa) dengan key dengan book id yang sudah ada pada sebelumnya dan value yang sudah ada melalui kode 'categoryMaps.get(q.getBookId());'
			categoryMaps.put(q.getBookId(), categoryCodeList);
		}

		return categoryMaps;
	}

}
