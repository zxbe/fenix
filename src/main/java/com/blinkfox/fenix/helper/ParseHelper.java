package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.exception.ParseExpressionException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

/**
 * MVEL 表达式解析相关的工具类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParseHelper {

    /**
     * 通过 MVEL 来解析表达式的值，该方法如果解析出错也不抛出异常.
     *
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseExpress(String exp, Object paramObj) {
        try {
            return MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            log.error("【Fenix 错误警示】解析表达式出错，表达式为:【{}】.", exp, e);
            return null;
        }
    }

    /**
     * 通过 MVEL 来解析表达式的值，该方法如果解析出错就抛出 {@link ParseExpressionException} 异常.
     *
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseExpressWithException(String exp, Object paramObj) {
        try {
            return MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            throw new ParseExpressionException("【Fenix 异常提示】解析表达式异常，解析出错的表达式为:【" + exp + "】.", e);
        }
    }

    /**
     * 通过 MVEL 来解析模板的值，该方法如果解析出错就抛出 {@link ParseExpressionException} 异常.
     *
     * @param template 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的字符串结果
     */
    public static String parseTemplate(String template, Object paramObj) {
        try {
            return (String) TemplateRuntime.eval(template, paramObj);
        } catch (Exception e) {
            throw new ParseExpressionException("【Fenix 异常提示】解析模板异常，解析出错的模板为:【" + template + "】.", e);
        }
    }

    /**
     * 判断 Fenix 标签中 `match` 属性中的表达式是否匹配通过.
     *
     * @param match match表达式
     * @param paramObj 参数上下文对象
     * @return 布尔值
     */
    public static boolean isMatch(String match, Object paramObj) {
        return StringHelper.isBlank(match) || isTrue(match, paramObj);
    }

    /**
     * 判断 Fenix 标签中 `match` 属性中的表达式是否`不`匹配通过.
     *
     * @param match match表达式
     * @param paramObj 参数上下文对象
     * @return 布尔值
     */
    public static boolean isNotMatch(String match, Object paramObj) {
        return !isMatch(match, paramObj);
    }

    /**
     * 判断解析的表达式是否为 true.
     *
     * @param exp 表达式
     * @param paramObj 参数上下文对象
     * @return 布尔值
     */
    public static boolean isTrue(String exp, Object paramObj) {
        return Boolean.TRUE.equals(ParseHelper.parseExpressWithException(exp, paramObj));
    }

}