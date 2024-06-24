package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.pojo.Meal
//Bước 2 cảu room
//Xây dựng các thuộc tính DAO (đọc, thêm, xóa,....)
@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)//nếu chèn thêm 1 phần tử nữa thì sẽ cập nhật luôn(ko cần hàm update)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeal():LiveData<List<Meal>>

}