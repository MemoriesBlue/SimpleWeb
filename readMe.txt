ApplicationContextBuilder
    build ApplicationContext: all controller, url router,beanMaps

BootstrapServlet:
    convert HttpServletRequest to Request,RequestPattern
    find the handler by RequestPattern in router
    invoke the handler and get the result
    send the result to HttpServletResponse
