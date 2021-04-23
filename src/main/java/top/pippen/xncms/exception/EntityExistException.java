package top.pippen.xncms.exception;

import org.springframework.util.StringUtils;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
public class EntityExistException extends RuntimeException {

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }
    public EntityExistException(String field) {
        super("[ '"+field+"' ] 已存在该值，请重新输入！");
    }
    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}