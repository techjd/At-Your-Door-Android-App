package com.atyourdoorteam.atyourdoor.UI.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.Adapters.GroceriesListAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.SelectStoreActivity;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class GroceyListFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView groceryListReyclerView;
    private FloatingActionButton floatingActionButton;
    private Database database;
    private ArrayList<GroceryList> groceryLists;
    private GroceriesListAdapter groceriesListAdapter;
    private Button findAndAdd;
    private TextView noGroceryList;

    public GroceyListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grocey_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("YOUR GROCERY LIST");
        groceryListReyclerView = view.findViewById(R.id.groceryList);
        noGroceryList = view.findViewById(R.id.noGroceryListAvailable);
        findAndAdd = view.findViewById(R.id.findAndAdd);
        findAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectStore = new Intent(getContext(), SelectStoreActivity.class);
                startActivity(selectStore);
            }
        });
        groceryListReyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        groceriesListAdapter = new GroceriesListAdapter();
        groceryListReyclerView.setAdapter(groceriesListAdapter);
        database = Room.databaseBuilder(getContext(), Database.class, "ListDB").build();
        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setCancelable(false);
                final View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_add_grocery, null);
                builder.setView(view1);
                EditText editText;
                final String[] productName = new String[1];
                Button add;
                Button cancel;
                editText = view1.findViewById(R.id.edtProductName);

                add = view1.findViewById(R.id.addToList);
                cancel = view1.findViewById(R.id.cancel);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productName[0] = editText.getText().toString();
                        Toast.makeText(v.getContext(), productName[0], Toast.LENGTH_SHORT).show();
                        GroceryList groceryList = new GroceryList(productName[0]);

                        addProductToDB(groceryList);
                        editText.getText().clear();
                    }
                });


                AlertDialog alertDialog = builder.create();


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                GroceryList groceryList = groceryLists.get(viewHolder.getAdapterPosition());
                deleteItem(groceryList);
            }
        }).attachToRecyclerView(groceryListReyclerView);
    }

    private void addProductToDB(GroceryList groceryList) {
        new AddNewItemToList().execute(groceryList);
    }

    private class AddNewItemToList extends AsyncTask<GroceryList, Void, Void> {

        @Override
        protected Void doInBackground(GroceryList... groceryLists) {
            database.getGroceryDao().insert(groceryLists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadData();
        }
    }

    void loadData() {
        new GetAllItemsAsyncTask().execute();
    }

    private class GetAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            groceryLists = (ArrayList<GroceryList>) database.getGroceryDao().getGroceryList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("IS IT FINISHED", String.valueOf(groceryLists.size()));
            groceriesListAdapter.setGroceryLists(groceryLists);
        }
    }


    private void deleteItem(GroceryList groceryList) {
        new DeleteItemAsyncTask().execute(groceryList);
    }

    private class DeleteItemAsyncTask extends AsyncTask<GroceryList, Void, Void> {

        @Override
        protected Void doInBackground(GroceryList... groceryLists) {
            database.getGroceryDao().Delete(groceryLists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new ShoppingCartFragment()).commit();
            loadData();
        }
    }
}