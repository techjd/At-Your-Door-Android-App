package com.atyourdoorteam.atyourdoor.API;

import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.models.Address.PostAddress;
import com.atyourdoorteam.atyourdoor.models.Address.PrimaryAddress;
import com.atyourdoorteam.atyourdoor.models.Address.SecondaryAddress;
import com.atyourdoorteam.atyourdoor.models.Categories;
import com.atyourdoorteam.atyourdoor.models.FinalOrderedItems.FinalOrders;
import com.atyourdoorteam.atyourdoor.models.Message;
import com.atyourdoorteam.atyourdoor.models.OrderedItem;
import com.atyourdoorteam.atyourdoor.models.Orders;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.atyourdoorteam.atyourdoor.models.SearchProductsByLocation.SearchProducts;
import com.atyourdoorteam.atyourdoor.models.Shop;
import com.atyourdoorteam.atyourdoor.models.SubCategories;
import com.atyourdoorteam.atyourdoor.models.SubCategoriesProducts.ProductsBySubCategories;
import com.atyourdoorteam.atyourdoor.models.Token;
import com.atyourdoorteam.atyourdoor.models.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @FormUrlEncoded
    @POST("user/registerUser")
    Call<Token> registerUser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("number") String number
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<Token> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("user/me")
    Call<UserInfo> getLoggedInUserInfo(
            @Header("token") String token
    );

    @GET("categories")
    Call<List<Categories>> getCategoriesList();

    @GET("subcategories/{categoryId}")
    Call<List<SubCategories>> getSubCategoriesList(
            @Path("categoryId") String categoryId
    );

    @GET("shops/getShops")
    Call<List<Shop>> getShopsList();

    @GET("categories/getCategory/{shopId}")
    Call<List<Categories>> getCategoriesOfSpecificShop(
            @Path("shopId") String shopId
    );

    @GET("subcategories/getSubCategoryfromCategoryId/{categoryId}/{shopId}")
    Call<List<SubCategories>> getSubCategoriesOfSpecificShop(
            @Path("categoryId") String categoryId,
            @Path("shopId") String shopId
    );

    @GET("products/{shopId}/{subCategoryId}")
    Call<List<Products>> getProductsListBySubCategoryOfSpecificShop(
            @Path("shopId") String shopId,
            @Path("subCategoryId") String subCategoryId
    );

    @FormUrlEncoded
    @POST("orders/createOrder")
    Call<Message> checkOut(
            @Header("token") String token,
            @Field("shopId") String shopId,
            @Field("orderMode") String orderMode,
            @Field("orderedItems") String orderedItemList,
            @Field("shippingAddress") String shippingAddress,
            @Field("totalPrice") Integer totalPrice

    );

    @FormUrlEncoded
    @POST("orders/createOrderByUdhaar")
    Call<Message> checkOutByUdhaar(
            @Header("token") String token,
            @Field("shopId") String shopId,
            @Field("orderMode") String orderMode,
            @Field("orderedItems") String orderedItemList,
            @Field("shippingAddress") String shippingAddress,
            @Field("totalPrice") Integer totalPrice

    );

    @FormUrlEncoded
    @POST("orders/createOrderByCod")
    Call<Message> checkOutByCod(
            @Header("token") String token,
            @Field("shopId") String shopId,
            @Field("orderMode") String orderMode,
            @Field("orderedItems") String orderedItemList,
            @Field("shippingAddress") String shippingAddress,
            @Field("totalPrice") Integer totalPrice

    );

    @FormUrlEncoded
    @PUT("user/addPrimaryAddress")
    Call<Message> addPrimaryAddress(
            @Header("token") String token,
            @Field("address") String address,
            @Field("postalCode") String postalCode,
            @Field("city") String city,
            @Field("state") String state
    );

    @FormUrlEncoded
    @PUT("user/addSecondaryAddress")
    Call<Message> addSecondaryAddress(
            @Header("token") String token,
            @Field("address") String address,
            @Field("postalCode") String postalCode,
            @Field("city") String city,
            @Field("state") String state
    );

    @GET("products/{shopId}")
    Call<List<Products>> getProductsList(
            @Path("shopId") String shopId
    );

    @GET("products/get/a/single/product/{productId}")
    Call<Products> getAsingleProduct(
            @Path("productId") String productId
    );

    @PUT("orders/update/{orderid}")
    Call<Message> getResponse(
            @Header("token") String token,
            @Path("orderid") String orderid
    );

    @GET("orders/getAllOrder")
    Call<List<FinalOrders>> getAllOrders(
            @Header("token") String token
    );

    @GET("products/get/all/products/of/db")
    Call<List<SearchProducts>> getAllProductsForSearch();

    @GET("products/subCategory/{subCategoryId}")
    Call<List<ProductsBySubCategories>> getProductsBySubCategory(
            @Path("subCategoryId") String subCategoryId
    );

    @GET("shops/getShops/{latitude}/{longitude}")
    Call<List<Shop>> getShopsAccrodingToLocation(
            @Path("latitude") String latitude,
            @Path("longitude") String longitude
    );

}
