package com.example.bobstomatoes;

/**
 * Interface for custom recyclerviews
 */
public interface RecyclerViewInterface {

    /**
     * Method handling when item in recyclerview is clicked
     * @param position position of item in recycler view adapter
     */
    void onItemClick(int position);
}
