package com.fstudio.topnews.util;

import com.fstudio.topnews.model.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

     String BASE_URL ="https://raw.githubusercontent.com/pranavpk404/news-api/";
   //  String BASE_URL_NEWS_API ="https://newsapi.org/v2/";

   /* @GET("news")
    Call<NewsModel> News(
            @Query("access_key") String access_key,
            @Query("languages") String languages,
            @Query("categories") String categories,
            @Query("countries") String countries,
            @Query("date") String date

    );*/

    @GET("main/{country}/{category}.json")
    Call<NewsModel> categoryNews(
            //  @Query("country") String category,

            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country,
            @Path("category") String category


    );
     @GET("top-headlines/category/entertainment/{country}.json")
    Call<NewsModel> categoryEntertainment(
            //  @Query("country") String category,
            //  @Query("category") String category,
            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country


    );
     @GET("top-headlines/category/health/{country}.json")
    Call<NewsModel> categoryHealth(
            //  @Query("country") String category,
            //  @Query("category") String category,
            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country


    );
     @GET("top-headlines/category/science/{country}.json")
    Call<NewsModel> categoryScience(
            //  @Query("country") String category,
            //  @Query("category") String category,
            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country


    );
     @GET("top-headlines/category/technology/{country}.json")
    Call<NewsModel> categoryTechnology(
            //  @Query("country") String category,
            //  @Query("category") String category,
            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country


    );
     @GET("top-headlines/category/sports/{country}.json")
    Call<NewsModel> categorySports(
            //  @Query("country") String category,
            //  @Query("category") String category,
            //  @Query("pageSize") int pageSize,
            //  @Query("apiKey") String apiKey
            @Path("country") String country


    );

}
