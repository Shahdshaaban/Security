package com.test.demo.Security;

import jakarta.persistence.AttributeConverter;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Jasypt implements AttributeConverter<String, String> {

    private final AES256TextEncryptor textEncryptor;

    // Read from application.properties
    public Jasypt(@Value("${jasypt.encryptor.password}") String secretPassword) {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretPassword);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return textEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return textEncryptor.decrypt(dbData);
    }
}
