package com.omgcms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.omgcms.bean.SystemInfo;
import com.omgcms.constants.CmsConstants;
import com.omgcms.constants.ResponseConstatnts;
import com.omgcms.exception.ExceptionI18nMessage;
import com.omgcms.model.core.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CmsUtil {

    private static CmsUtil cmsUtil = null;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private CmsUtil() {

    }

    public static CmsUtil getInstance() {

        if (cmsUtil == null) {
            cmsUtil = new CmsUtil();
        }

        return cmsUtil;
    }

    public static SystemInfo getSystemInfo(HttpServletRequest request) {

        String osName = System.getProperties().getProperty("os.name");
        String runtimeEnv = "Java(TM) SE Runtime Environment " + System.getProperties().getProperty("java.version");
        String javaVm = System.getProperties().getProperty("java.vm.name").concat(" ")
                .concat(System.getProperties().getProperty("java.vm.version"));

        double totalMemory = (Runtime.getRuntime().totalMemory()) / (1024.0 * 1024);
        double maxMemory = (Runtime.getRuntime().maxMemory()) / (1024.0 * 1024);
        // double freeMemory = (Runtime.getRuntime().freeMemory()) / (1024.0 *
        // 1024);
        double freeMemory = maxMemory - totalMemory;

        String serverInfo = request.getSession().getServletContext().getServerInfo();

        SystemInfo sysInfo = new SystemInfo(osName, runtimeEnv, javaVm, totalMemory, maxMemory, freeMemory, serverInfo);

        return sysInfo;

    }

    public static String objectToJsonString(Object dataObject) {

        try {

            String jsonData = "";

            if (dataObject instanceof String) {
                jsonData = String.valueOf(dataObject);
            } else {

                jsonData = OBJECT_MAPPER.writeValueAsString(dataObject);
            }

            return jsonData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void setSessionMessage(HttpServletRequest request, String msgCode) {
        request.getSession().setAttribute(CmsConstants.SESSION_MSG_KEY, msgCode);
    }

    public static String getAndClearSessionMessage(HttpServletRequest request) {
        String msg = (String) request.getSession().getAttribute(CmsConstants.SESSION_MSG_KEY);
        request.getSession().removeAttribute(CmsConstants.SESSION_MSG_KEY);
        return msg;
    }

    public static String getLocaleMessage(String msgKeyCode) {
        return ExceptionI18nMessage.getLocaleMessage(msgKeyCode);
    }

    public static String getLocaleMessage(String msgKeyCode, Object[] args) {
        return ExceptionI18nMessage.getLocaleMessage(msgKeyCode, args);
    }

    public static String getLocaleMessage(String msgKeyCode, String args) {
        return ExceptionI18nMessage.getLocaleMessage(msgKeyCode, args.split(StringPool.COMMA, -1));
    }

    public static String md5encodePassword(String password, String salt) {
        String md5password = new SimpleHash("md5", password, salt, 2).toHex();
        return md5password;
    }

    /**
     * Get current login user
     *
     * @return User
     */
    public static User getCurrentUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            return (User) principal;
        }
        return null;
    }


    /**
     * @param code  The operation result code
     * @param message The message info.
     * @return
     */
    public static Map<String, Object> genResponseMap(String code, String message) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ResponseConstatnts.RESPONSE_CODE_KEY, code);
        result.put(ResponseConstatnts.RESPONSE_MESSAGE_KEY, message);

        return result;
    }

    public static Map<String, Object> genResponseMap() {
        return new HashMap<String, Object>();
    }

    /**
     * Check is user logged in
     *
     * @return if login return {@code true}, else return {@code false}
     */
    public static boolean isLoggedIn() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

}
