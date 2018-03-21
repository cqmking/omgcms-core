package com.omgcms.service;

import com.omgcms.model.cms.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {

    Category saveOrUpdateCategory(Category category);

    void deleteCategory(Long categoryId);

    Category getCategory(Long categoryId);

    Page<Category> findByParentCategoryId(int pageNo, int pageSize, String orderByProperty, String sortType, Long parentCategoryId);
}
