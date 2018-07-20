package com.example.miguel.misnotas.utilities;

import java.util.List;

/**
 * Created by 79812 on 18/07/2018.
 */

public interface ActionModeAdapterCallbacks<T> {
    void toggleSelection(int position);
    void clearSelections();
    int getSelectedCount();
    List<T> getSelectedItems();
}
