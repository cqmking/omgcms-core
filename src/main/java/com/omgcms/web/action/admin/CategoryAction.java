package com.omgcms.web.action.admin;

import com.omgcms.bean.RestResponse;
import com.omgcms.model.cms.Category;
import com.omgcms.model.core.User;
import com.omgcms.service.CategoryService;
import com.omgcms.util.CmsUtil;
import com.omgcms.util.RestResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/category")
public class CategoryAction {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/create")
    public RestResponse<Category> createCategory(String categoryName, String description, Long parentCategoryId) {

        Category newCategory = new Category();

        newCategory.setCategoryName(categoryName);
        newCategory.setDescription(description);
        newCategory.setParentCategoryId(parentCategoryId);

        User user = CmsUtil.getCurrentUser();

        newCategory.setUserId(user.getUserId());
        newCategory.setUserAccount(user.getUserAccount());
        newCategory.setUserName(user.getUserName());

        Date now = new Date();

        newCategory.setCreateDate(now);
        newCategory.setModifyDate(now);

        Category savedCategory = categoryService.saveOrUpdateCategory(newCategory);

        return RestResponseGenerator.genSuccessResult(savedCategory);
    }

    @RequestMapping("/update")
    public RestResponse<Category> updateCategory(String categoryName, String description, Long categoryId) {

        Category category = categoryService.getCategory(categoryId);

        category.setCategoryName(categoryName);
        category.setDescription(description);

        category.setModifyDate(new Date());

        Category updatedCategory = categoryService.saveOrUpdateCategory(category);

        return RestResponseGenerator.genSuccessResult(updatedCategory);
    }

    @RequestMapping("/findByParentCategoryId")
    public RestResponse<?> findByParentCategoryId(Long parentCategoryId) {

        int pageNum = 1;
        int pageSize = 1000000; // 查询所有

        Page<Category> page = categoryService.findByParentCategoryId(pageNum, pageSize, null, null, parentCategoryId);

        return RestResponseGenerator.genSuccessResult(page);
    }

    @RequestMapping("/delete")
    public RestResponse<?> delete(Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return RestResponseGenerator.genErrorResult("删除成功！");
    }


}
