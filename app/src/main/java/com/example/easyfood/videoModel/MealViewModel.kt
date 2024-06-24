package com.example.easyfood.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.MealApi
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
): ViewModel() {

    private var mealDetailsLiveData = MutableLiveData<Meal>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeal() // return live data

    fun getMealDetail(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }

        })
    }

    fun observerMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    //cả 2 hàm đều đc gọi lấy từ database nhằm thêm và xóa trong mục favorite
    //Lấy món ăn từ database
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            //gọi từ database
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    //Hàm quan sát livedata
    fun observeFavoritesLiveData(): LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }
}