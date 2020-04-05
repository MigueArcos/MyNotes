package com.zeus.migue.notes.ui.invisible_text_processor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.infrastructure.dao.ClipsDao;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.repositories.ClipsRepository;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BaseActivity;
import com.zeus.migue.notes.ui.shared.LoaderDialog;

import java.util.Date;

public class InvisibleTextProcessor extends BaseActivity {

    @Override
    public void initializeViews() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_NoDisplay);
        super.onCreate(savedInstanceState);
        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (text != null){
            ClipsDao clipsDao = AppDatabase.getInstance(this).clipsDao();
            ILogger logger = Logger.getInstance(this);
            ClipsRepository clipsRepository = ClipsRepository.getInstance(clipsDao, logger);
            String isoDate = Utils.toIso8601(new Date(), true);
            ClipItemDTO note = new ClipItemDTO(text.toString(), isoDate, isoDate);
            try {
                clipsRepository.insert(note);
            } catch (CustomError customError) {
                logger.log("(InsertDB) " + customError.getEvent().getMessage());
            }
            Toast.makeText(this, R.string.text_copied, Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }
}
