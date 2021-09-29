package com.atyourdoorteam.atyourdoor.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;

import java.util.List;

@Dao
public interface GroceryDAO {

    @Insert
    void insert(GroceryList groceryList);

    @Update
    void update(GroceryList groceryList);

    @Delete
    void Delete(GroceryList groceryList);

    @Query("SELECT * FROM grocery_table")
    List<GroceryList> getGroceryList();

}
