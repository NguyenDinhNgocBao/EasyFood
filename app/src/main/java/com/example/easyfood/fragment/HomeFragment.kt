package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapter.CategoriesAdapter
import com.example.easyfood.adapter.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragment.bottomsheets.MealBottomSheetFragment
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.videoModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.easyfood.fragment.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragment.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragment.thumbMeal"
        const val CATEGORY_NAME ="com.example.easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //đã được khởi tạo từ mainActivity
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter = CategoriesAdapter()

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observeRandomMeal()
        //hàm chuyển qua chi tiết món ăn
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopulatorItemsLiveData()
        //hàm chuyển qua chi tiết món ăn
        onPopularItemClick()

        prepareCategoriesRecycleView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

        onPopularLongItemClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularLongItemClick() {
        popularItemsAdapter.onLongItemClick = {meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick ={category->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent
            )
        }
    }

    private fun prepareCategoriesRecycleView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoriesList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick ={meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    //cấu hình khi hiển thị ra giao diện
    private fun preparePopularItemsRecyclerView() {
        binding.rwViewMealPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopulatorItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,{ mealList ->
            popularItemsAdapter.setMeals(meaList = mealList as ArrayList<MealByCategory>)
        })
    }

    private fun onRandomMealClick() {
        binding.cvRandomMeal.setOnClickListener {
            val i = Intent(activity, MealActivity::class.java)
            i.putExtra(MEAL_ID,randomMeal.idMeal)
            i.putExtra(MEAL_NAME,randomMeal.strMeal)
            i.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(i)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            {meal ->
                Glide.with(this@HomeFragment)
                    .load(meal.strImageSource)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal
            })
    }
}