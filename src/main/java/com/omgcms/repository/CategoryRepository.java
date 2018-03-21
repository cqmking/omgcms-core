package com.omgcms.repository;


import com.omgcms.model.cms.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaSpecificationExecutor<Category>, JpaRepository<Category, Long> {

    Page<Category> findByParentCategoryId(Long parentCategoryId, Pageable pageable);

}
