package com.keeprecipes.android.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.keeprecipes.android.data.dao.CollectionDao;
import com.keeprecipes.android.data.dao.CollectionWithRecipesDao;
import com.keeprecipes.android.data.dao.RecipeDao;
import com.keeprecipes.android.data.dao.RecipeWithCollectionsDao;
import com.keeprecipes.android.data.entities.CategoriesRecipeCrossRef;
import com.keeprecipes.android.data.entities.Category;
import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.entities.RecipeFts;

@Database(entities = {Recipe.class, Category.class, RecipeFts.class, CategoriesRecipeCrossRef.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    public abstract CollectionDao recipeCollectionDao();

    public abstract RecipeWithCollectionsDao recipeWithCollectionsDao();

    public abstract CollectionWithRecipesDao collectionWithRecipesDao();
}