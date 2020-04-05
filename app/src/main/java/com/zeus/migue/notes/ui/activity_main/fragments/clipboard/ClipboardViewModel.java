package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.ClipItem;
import com.zeus.migue.notes.infrastructure.dao.ClipsDao;
import com.zeus.migue.notes.infrastructure.repositories.ClipsRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BaseListViewModel;

import java.util.List;

public class ClipboardViewModel extends BaseListViewModel<ClipItem, ClipItemDTO> {

    public ClipboardViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public LiveData<List<ClipItemDTO>> initItemsLiveData() {
        return Transformations.switchMap(filterLiveData, input -> {
            if (Utils.stringIsNullOrEmpty(input)) {
                return ((ClipsDao) repository.getDao()).getAllClips();
            } else {
                return ((ClipsDao) repository.getDao()).getClipsByFilter("%" + input + "%");
            }
        });
    }

    @Override
    public ClipsRepository getRepository(Application application) {
        return ClipsRepository.getInstance(AppDatabase.getInstance(application).clipsDao(), Logger.getInstance(application));
    }
}
