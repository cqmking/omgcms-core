package com.omgcms.service.impl;

import com.omgcms.model.cms.Category;
import com.omgcms.model.core.User;
import com.omgcms.repository.CategoryRepository;
import com.omgcms.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveOrUpdateCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.delete(categoryId);
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.getOne(categoryId);
    }

    @Override
    public Page<Category> findByParentCategoryId(int pageNo, int pageSize, String orderByProperty, String sortType, Long parentCategoryId) {

        Sort.Direction direction = Sort.Direction.ASC;

        if ("DESC".equals(sortType)) {
            direction = Sort.Direction.DESC;
        }

        if(StringUtils.isBlank(orderByProperty)){
            orderByProperty = "categoryName";
        }

        Sort.Order idOrder = new Sort.Order(direction, orderByProperty);
        Sort sort = new Sort(idOrder);

        PageRequest pageable = new PageRequest(pageNo - 1, pageSize, sort);

        Page<Category> page = categoryRepository.findByParentCategoryId(parentCategoryId, pageable);

        return page;
    }

}
