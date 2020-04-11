package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.infrastructure.dao.ClipNotesDao;
import com.zeus.migue.notes.infrastructure.repositories.ClipsRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.BaseListViewModel;

import java.util.List;

public class ClipboardViewModel extends BaseListViewModel<ClipNote, ClipNoteDTO> {

    public ClipboardViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public LiveData<List<ClipNoteDTO>> initItemsLiveData() {
        return Transformations.switchMap(filterLiveData, input -> {
            if (Utils.stringIsNullOrEmpty(input)) {
                return ((ClipNotesDao) repository.getDao()).getAllClips();
            } else {
                return ((ClipNotesDao) repository.getDao()).getClipsByFilter("%" + input + "%");
            }
        });
    }

    @Override
    public ClipsRepository getRepository(Application application) {
        return ClipsRepository.getInstance(AppDatabase.getInstance(application).clipsDao(), Logger.getInstance(application));
    }
}
