module jmp.service.api {
    exports com.example.serviceApi;

    requires spring.context;
    requires jmp.dto;
    requires spring.data.commons;
}