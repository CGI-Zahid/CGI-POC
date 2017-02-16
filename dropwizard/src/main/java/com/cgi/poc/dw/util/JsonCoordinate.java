/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author dawna.floyd
 */
public class JsonCoordinate extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

           String coordAsString="0";
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            
            LinkedHashMap obj  = jp.readValueAs(LinkedHashMap.class);
            // convert this... 
           //  "y": {"s": 1, "e": 1, "c": [ 3, 1,  9, 9,  6, 6, 6, 7,  0, 0, 0, 0, 8, 2, 1, 4, 1 ] }
           // to
           // 3.1996667000082141
           // s =(1 or -1.. for pos or negative)
           // e = index of element 'c' where the decimla will be
           // c =  the numner to convert.
           int indexForDecPoint = (int)obj.get("e");
           ArrayList<Integer> listOfDigits = (ArrayList<Integer>)(obj.get("c"));
           for (int i = 0; i < listOfDigits.size(); i++) {
              if (i == indexForDecPoint){
                  coordAsString += ".";
              }
                  coordAsString +=  listOfDigits.get(i).toString();
           }

         BigDecimal bgValue = new BigDecimal(coordAsString);

         if ((int)obj.get("s") == -1){
           bgValue = bgValue.multiply(BigDecimal.valueOf(-1));
         }

         return bgValue;


    }
    
}