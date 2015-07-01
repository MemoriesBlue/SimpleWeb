amah是一个IOC框架。负责扫描项目的classpath下的所有class，加载指定注解的class到IOC容器。
其中注解有：
用于类的注解：@Controller，@Service，@Dao，@Bean
用于属性的注解：@Autowired