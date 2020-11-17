package com.zhouyou.http.func;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by haoxuhong on 2020/7/21
 *
 * @description: 解决gson序列化实体类里有的string类型的属性值为null时,全局处理给他个默认值""
 */
public class StringAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        //如果对象类型为String，返回自己实现的StringAdapter
        if (rawType != String.class) {
            return null;
        }
        return (TypeAdapter<T>) new StringAdapter();
    }


    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            //如果值为null，返回空字符串
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        //序列化用到的，这里我们实现默认的代码就行
        public void write(JsonWriter writer, String value) throws IOException {

            writer.value(value);
        }
    }
}
