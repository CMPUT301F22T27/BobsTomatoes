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

/**
 * Recipe database test, test adding, deleting, and editing recipe, tests will execute on an Android device
 */
public class RecipeDBTest {
    RecipeDB recipeDB;


    ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        return ingredientList;
    }

    /**
     * Initialize database for recipes
     */
    @Before
    public void initializeDB(){
        this.recipeDB = new RecipeDB();

    }

    /**
     * Test adding of recipe into database
     */
    @Test
    public void testAddRecipeDB() {
        boolean isInDocument;

        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList(), "Image");

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

    /**
     * Testing deletion/removal of recipe from database
     */
    @Test
    public void testDeleteRecipe() {
        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList(), "Image");

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

    /**
     * Testing editing of recipe in database
     */
    @Test
    public void testEditRecipe() {
        CollectionReference recipeReference = recipeDB.getRecipeReference();

        ArrayList<Recipe> recipeList = recipeDB.getRecipeList();
        Recipe recipe = new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList(),"Image");
        Recipe recipe2 = new Recipe("Spaghetti", 25, 9, "Pasta", "Gourmet", mockIngredientList(), "Image");

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

        Recipe oldRecipe = recipeList.get(0);
        recipeDB.editRecipe(0,recipe2, oldRecipe);

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
        assertEquals(recipe2, recipeList.get(0));
        recipeDB.removeRecipe(recipe2);
    }
}
