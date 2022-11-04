package com.example.bobstomatoes;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class RecipeDBTest {
    RecipeDB recipeDB;

    ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        return ingredientList;
    }

    @Before
    public void initializeDB(){
        this.recipeDB = new RecipeDB();

    }

    @Test
    public void testAddRecipeDB() {
        boolean isInDocument;

        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList());

        DocumentReference recipeRef = recipeReference.document(recipe.getRecipeTitle());

        int PreSize = recipeList.size();

        recipeDB.addRecipe(recipe);

        recipeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(1,1);
                    } else {
                        assertEquals(0,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        assertEquals(PreSize+1, recipeList.size());

        //Remove the recipe after the test
        recipeDB.removeRecipe(recipe);
    }


    @Test
    public void testDeleteRecipe() {
        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList());

        DocumentReference recipeRef = recipeReference.document(recipe.getRecipeTitle());

        int PreSize = recipeList.size();

        recipeDB.addRecipe(recipe);
        recipeDB.removeRecipe(recipe);

        recipeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(0,1);
                    } else {
                        assertEquals(1,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        assertEquals(PreSize, recipeList.size());
    }

    @Test
    public void testEditRecipe() {
        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList());
        Recipe recipe2 = new Recipe("Spaghetti", 25, 9, "Pasta", "Gourmet", mockIngredientList());

        DocumentReference recipe1Ref = recipeReference.document(recipe.getRecipeTitle());

        DocumentReference recipe2Ref = recipeReference.document(recipe2.getRecipeTitle());

        int PreSize = recipeList.size();

        recipeDB.addRecipe(recipe);

        recipe1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(1,1);
                    } else {
                        assertEquals(0,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        recipeDB.editRecipe(0,recipe2);

        recipe1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(0,1);
                    } else {
                        assertEquals(1,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        recipe2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(1,1);
                    } else {
                        assertEquals(0,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });


        assertEquals(PreSize+1, recipeList.size());

        recipeDB.removeRecipe(recipe2);
    }
}
