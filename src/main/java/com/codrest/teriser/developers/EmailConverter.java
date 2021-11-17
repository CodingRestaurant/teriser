/*
 * EmailConverter.java
 * Author : 박찬형
 * Created Date : 2021-08-01
 */
package com.codrest.teriser.developers;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmailConverter implements AttributeConverter<Email, String> {
    @Override
    public String convertToDatabaseColumn(Email attribute) {
        if(attribute == null){
            return null;
        }
        return attribute.getAddress();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()){
            return null;
        }
        return Email.of(dbData);
    }
}
