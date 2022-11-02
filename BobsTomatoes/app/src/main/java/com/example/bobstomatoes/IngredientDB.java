package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IngredientDB {

    private final ArrayList<Ingredient> ingredientList = new ArrayList<>();

    private final FirebaseFirestore ingredientDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = ingredientDatabase.collection("Ingredients");



    public IngredientDB(ArrayList<Ingredient> aIngredientList) {
        //ingredientList = new ArrayList<Ingredient>();
        readData(new FireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Ingredient> ingredientsList) {
                Log.d("in database:", ingredientsList.toString());
                setIngredientList(aIngredientList);
            }
        });
    }

//    public interface MyCallBack {
//        void onCallBack(ArrayList<Ingredient> aIngredientList);
//    }


    public void addIngredient(Ingredient ingredient){
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());
        ingredientReference.document(ingredientName)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Data could not be added");
                    }
                });
        ingredientList.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient){
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());
        ingredientReference.document(ingredientName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Data could not be added");
                    }
                });
        ingredientList.remove(ingredient);
    }

    public void editIngredient(int oldIngredientPos, Ingredient updatedIngredient) {
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = updatedIngredient.getIngredientDesc();
        data.put("ingredientDesc", updatedIngredient.getIngredientDesc());
        data.put("ingredientDate", updatedIngredient.getIngredientDate());
        data.put("ingredientLocation", updatedIngredient.getIngredientLocation());
        data.put("ingredientAmount", updatedIngredient.getIngredientAmount());
        data.put("ingredientUnit", updatedIngredient.getIngredientUnit());
        data.put("ingredientCategory", updatedIngredient.getIngredientCategory());
        ingredientReference.document(ingredientName)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Data could not be added");
                    }
                });
        ingredientList.set(oldIngredientPos, updatedIngredient);
    }

    public void updateIngredientList(){
        Log.d("", "onEvent: Accessed 0000000000000000");
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i("", "onComplete: " + document.getId() + "=>" + document.getData());
                        Log.d("", document.getId() + " => " + document.getData());
                        String ingredientDesc = document.getId();
                        String ingredientDate = (String) document.getData().get("ingredientDate");
                        String ingredientLocation = (String) document.getData().get("ingredientLocation");
                        int ingredientAmount = Integer.parseInt(document.getData().get("ingredientAmount").toString());
                        int ingredientUnit = Integer.parseInt(document.getData().get("ingredientUnit").toString());
                        String ingredientCategory = (String) document.getData().get("ingredientCategory");
                        Ingredient ingredient = new Ingredient(ingredientDesc, ingredientDate, ingredientLocation, ingredientAmount, ingredientUnit, ingredientCategory);
                        Log.d("",ingredient + "");
                        ingredientList.add(ingredient);
                        Log.d("", ingredientList.get(0).getIngredientDesc());
                        Log.d("", ingredientList.get(0).getIngredientLocation());
//                      Ingredient ingredient = document.toObject(Ingredient.class);
                    }
                } else {
                    Log.d("", "onEvent: Accessed 1111111111111");
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void readData(FireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i("", "onComplete: " + document.getId() + "=>" + document.getData());
                        Log.d("", document.getId() + " => " + document.getData());
                        String ingredientDesc = document.getId();
                        String ingredientDate = (String) document.getData().get("ingredientDate");
                        String ingredientLocation = (String) document.getData().get("ingredientLocation");
                        int ingredientAmount = Integer.parseInt(document.getData().get("ingredientAmount").toString());
                        int ingredientUnit = Integer.parseInt(document.getData().get("ingredientUnit").toString());
                        String ingredientCategory = (String) document.getData().get("ingredientCategory");
                        Ingredient ingredient = new Ingredient(ingredientDesc, ingredientDate, ingredientLocation, ingredientAmount, ingredientUnit, ingredientCategory);
                        Log.d("",ingredient + "");
                        ingredientList.add(ingredient);
                        Log.d("", ingredientList.get(0).getIngredientDesc());
                        Log.d("", ingredientList.get(0).getIngredientLocation());
//                      Ingredient ingredient = document.toObject(Ingredient.class);
                    }
                    callBack.onCallBack(ingredientList);
                } else {
                    Log.d("", "onEvent: Accessed 1111111111111");
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface FireStoreCallback {
        void onCallBack(ArrayList<Ingredient> test);
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<Ingredient> setIngredientList(ArrayList<Ingredient> arrayList){
        arrayList = ingredientList;
        return arrayList;
    }

}