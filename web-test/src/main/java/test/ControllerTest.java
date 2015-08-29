package test;

import com.elaine.web.api.annotations.Controller;
import com.elaine.web.api.annotations.Param;
import com.elaine.web.api.annotations.RequestMapping;
import com.elaine.web.constants.HttpMethod;

import javax.annotation.Resource;

/**
 * Created by jianlan on 15-7-29.
 */
@Controller(urlMapping = "/test/controller")
public class ControllerTest {
    @Resource
    ServiceTest serviceTest;

    public ControllerTest() {
        System.out.println("create a ControllerTest...");
    }

    @RequestMapping(value = "/doGet", method = HttpMethod.GET)
    public String doGet(@Param("name") String name, @Param("pwd")String pwd, @Param("category") int category) {
        serviceTest.doGet();
        return "test";
    }
}
