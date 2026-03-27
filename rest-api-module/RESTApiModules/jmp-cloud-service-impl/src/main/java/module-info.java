module jmp.cloud.service.impl {
    exports com.example.cloudServiceImpl;
    exports com.example.cloudServiceImpl.repository;
    exports com.example.cloudServiceImpl.service;

    requires jmp.domain;
    requires jmp.service.api;
    requires jmp.dto;
    requires org.apache.tomcat.embed.core;
    requires spring.beans;
    requires modelmapper;
    requires spring.context;
    requires spring.data.jpa;
    requires org.slf4j;
    requires static lombok;
    requires spring.data.commons;
    requires org.apache.logging.log4j;
    requires spring.web;
}