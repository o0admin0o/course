package com.course.generator.server;

import com.course.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;

public class ServerGenerator {
    static String moudle="business";
    static String toServicePath = "server\\src\\main\\java\\com\\course\\server\\service\\";
    static String toControllerPath =moudle+ "\\src\\main\\java\\com\\course\\"+moudle+"\\controller\\admin\\";
    public static void main(String[] args) throws IOException, TemplateException {
        String Domain="Section";
        String domain="section";
        HashMap<Object, Object> map = new HashMap<>();
        map.put("Domain",Domain);
        map.put("domain",domain);
        map.put("moudle",moudle);

        /*
        * 生成service
        * */
        FreemarkerUtil.initconfig("service.ftl");
        FreemarkerUtil.generator(toServicePath+Domain+"Service.java",map);

        /*
         * 生成controller
         * */
        FreemarkerUtil.initconfig("controller.ftl");
        FreemarkerUtil.generator(toControllerPath+Domain+"Controller.java",map);
}
}
