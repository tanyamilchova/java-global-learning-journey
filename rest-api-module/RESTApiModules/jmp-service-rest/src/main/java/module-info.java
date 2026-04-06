open module jmp.service.rest {
    exports com.example.serviceRest;

    requires jmp.service.api;
    requires jmp.dto;
    requires spring.beans;
    requires spring.context;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires java.sql;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires modelmapper;
    requires io.swagger.v3.oas.annotations;
    requires spring.hateoas;
    requires spring.aop;

}