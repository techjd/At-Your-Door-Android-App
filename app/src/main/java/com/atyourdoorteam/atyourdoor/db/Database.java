package com.atyourdoorteam.atyourdoor.db;

import androidx.room.RoomDatabase;

import com.atyourdoorteam.atyourdoor.db.dao.CartDAO;
import com.atyourdoorteam.atyourdoor.db.dao.GroceryDAO;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;


@androidx.room.Database(entities = {Cart.class, GroceryList.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract CartDAO getCartDao();

    public abstract GroceryDAO getGroceryDao();

}
