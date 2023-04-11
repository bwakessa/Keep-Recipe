//package com.keeprecipes.android.presentationLayer.home;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.AppCompatImageView;
//import androidx.appcompat.widget.LinearLayoutCompat;
//import androidx.cardview.widget.CardView;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.keeprecipes.android.dataLayer.entities.Categories;
//import com.keeprecipes.android.dataLayer.entities.Recipe;
//import com.keeprecipes.android.databinding.RecipeItemBinding;
//import com.keeprecipes.android.utils.Util;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//
//public class CategoriesAdapter extends ListAdapter<Categories, CategoriesAdapter.ViewHolder> {
//
//    public CategoriesAdapter(@NonNull DiffUtil.ItemCallback<Categories> diffCallback) {
//        super(Categories.DIFF_CALLBACK);
//    }
//
//    @NonNull
//    @Override
//    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new CategoriesAdapter.ViewHolder(RecipeItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
//
//    }
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView recipeTitle;
//        private final AppCompatImageView recipeImage;
//
//        private final CardView cardView;
//
//        public ViewHolder(RecipeItemBinding binding) {
//            super(binding.getRoot());
//            // Define click listener for the ViewHolder's View
//            recipeTitle = binding.recipeTitle;
//            recipeImage = binding.recipeImage;
//            cardView = binding.cardView;
//        }
//
//        public void bind(Recipe recipe) {
//            recipeTitle.setText(recipe.title);
//            if (!Util.isEmpty(recipe.photos)) {
//                File file = new File(recipeImage.getContext().getFilesDir(), recipe.photos.get(0));
//                Picasso.get()
//                        .load(file)
//                        .fit()
//                        .centerCrop()
//                        .into(recipeImage);
//            } else {
//                // If there is now image then remove the imageView space from card
//                recipeImage.getLayoutParams().height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
//                recipeImage.requestLayout();
//            }
//            // Rewrite for setOnClickListener
//            // https://www.digitalocean.com/community/tutorials/android-recyclerview-data-binding
//            cardView.setOnClickListener(view -> {
//                com.keeprecipes.android.presentationLayer.home.HomeFragmentDirections.ActionNavigationHomeToRecipeDetailFragment action = HomeFragmentDirections.actionNavigationHomeToRecipeDetailFragment();
//                action.setRecipeId((int) recipe.recipeId);
//                Navigation.findNavController(view).navigate(action);
//            });
//        }
//    }
//}
