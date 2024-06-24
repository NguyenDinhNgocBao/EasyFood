package com.example.easyfood.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

//Buowcs 4 của room
//Đưa dữ liệu về kiểu dữ liệu nguyên thủy
@TypeConverters
class MealTypeConvertor {
    //muốn chèn vào csdl thì dùng hàm này
    @TypeConverter
    fun fromAnyToString(attribute: Any?): String{
        if(attribute == null){
            return ""
        }
        return attribute as String
    }
    //muốn lấy ra từ csdl thì dùng hàm này
    @TypeConverter
    fun fromStringToAny(attribute: String?): Any{
        if(attribute == null){
            return ""
        }
        return attribute
    }
}