package com.atyourdoorteam.atyourdoor.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.atyourdoorteam.atyourdoor.db.entities.Cart;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cart cart);

    @Query("SELECT * FROM cart_table")
    List<Cart> getProducts();

    @Delete
    void delete(Cart cart);

    @Update
    void update(Cart cart);

    @Query("SELECT SUM(productQuantity*productPrice) AS Total_Amount FROM cart_table")
    int getTotal();

    @Query("DELETE FROM cart_table")
    void deleteAllRecords();

//    @Query("UPDATE cart_table SET productQuantity=:productQuantity WHERE id= :id")
//    void updateCart(int productQuantity, String id);

}
