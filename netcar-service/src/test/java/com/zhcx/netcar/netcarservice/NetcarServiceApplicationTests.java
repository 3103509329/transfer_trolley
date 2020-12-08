package com.zhcx.netcar.netcarservice;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

import java.io.IOException;
import java.util.Map;

//@SpringBootTest
class NetcarServiceApplicationTests {

//    @Test
    void contextLoads() {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.print(objectMapper.readValue("{\"ORDERID\":\"0000000001\"}".getBytes(), Map.class).toString());;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String USER_SCHEMA = "{\"name\":\"jdbctest\",\"type\":\"record\",\"fields\":[{\"name\":\"ORDERID\",\"type\":\"string\"}]}";
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);
        JsonAvroConverter converter = new JsonAvroConverter();
        String str = "{\"ORDERID\":\"0000000002\"}";

        GenericData.Record avro = converter.convertToGenericDataRecord(str.getBytes(), schema);
        byte[] binaryJson = converter.convertToJson(avro);
        String jsonStr = new String (binaryJson);
        System.out.print("数据转换结果："+jsonStr);
    }
}
