package test;


import com.elaine.web.api.annotations.Service;

/**
 * Created by jianlan on 15-8-1.
 */
@Service
public class ServiceTest {
    public ServiceTest() {
        System.out.println("create service ...");
    }

    public void doGet(){
        System.out.println("service doGet()...");
    }
}
