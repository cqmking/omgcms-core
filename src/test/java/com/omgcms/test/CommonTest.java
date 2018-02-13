package com.omgcms.test;

import com.omgcms.constants.CmsConstants;
import com.omgcms.model.core.Role;
import com.omgcms.service.RoleService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;


public class CommonTest {

    private static final String UUM_URL = null;

    private Logger logger = LoggerFactory.getLogger(CommonTest.class);

    private ApplicationContext ctx = null;
    private EntityManagerFactory entityManagerFactory;

    {
        ctx = new ClassPathXmlApplicationContext(
                new String[]{"config/spring-jpa.xml", "config/spring-context.xml", "config/spring-shiro.xml"});
        entityManagerFactory = ctx.getBean(EntityManagerFactory.class);
    }

    @Test
    public void initDBTest() throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println(entityManager);
        entityManager.close();
    }

    @Test
    public void testJoinQuery() throws SQLException {
        RoleService roleService = ctx.getBean(RoleService.class);
        Page<Role> findRolesByUsers = roleService.getRolesByUserId(1, 5, "roleKey", CmsConstants.ORDER_ASC, 2059);
        System.out.println("=====>" + findRolesByUsers.getSize());
    }

    @Test
    public void encryptPassword() {
        String password = new SimpleHash("md5", "123456", null, 2).toHex();
        System.out.println(password);
        String password_cipherText = new Md5Hash("123456", ByteSource.Util.bytes("cmsOmg"), 2).toHex();
        System.out.println(password_cipherText);
        String password2 = new SimpleHash("md5", "123456", ByteSource.Util.bytes("cmsOmg"), 2).toHex();
        System.out.println(password2);
    }


}
