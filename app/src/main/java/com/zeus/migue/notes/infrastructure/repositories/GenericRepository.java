package com.zeus.migue.notes.infrastructure.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.dao.BaseDao;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class GenericRepository<T extends BaseEntity, DTO extends IFilterable & IEntityConverter<T>> {
    private BaseDao<T> dao;
    private ExecutorService executorService;
    private ILogger logger;

    GenericRepository(BaseDao<T> dao, ILogger logger) {
        this.dao = dao;
        this.logger = logger;
        executorService = Executors.newSingleThreadExecutor();
    }

    public BaseDao<T> getDao() {
        return dao;
    }

    public long insert(DTO dto) throws CustomError {
        Future<Long> promise = executorService.submit(() -> dao.insert(dto.toEntity()));
        try {
            return promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(InsertDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_INSERT_ERROR);
        }
    }

    public long[] insert(T[] entities) throws CustomError {
        Future<long[]> promise = executorService.submit(() -> dao.insert(entities));
        try {
            return promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(InsertDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_INSERT_ERROR);
        }
    }

    public void update(T[] entities) throws CustomError {
        Future promise = executorService.submit(() -> dao.update(entities));
        try {
            promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(UpdateDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_UPDATE_ERROR);
        }
    }

    public void update(DTO dto) throws CustomError {
        Future promise = executorService.submit(() -> dao.update(dto.toEntity()));
        try {
            promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(UpdateDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_UPDATE_ERROR);
        }
    }

    public void delete(T[] entities) throws CustomError {
        Future promise = executorService.submit(() -> dao.delete(entities));
        try {
            promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(UpdateDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_DELETE_ERROR);
        }
    }

    public void delete(DTO dto) throws CustomError {
        Future promise = executorService.submit(() -> dao.delete(dto.toEntity()));
        try {
            promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(UpdateDB) Exception occurred: %s", ex.getMessage()));
            throw new CustomError(Event.ROOM_DELETE_ERROR);
        }
    }


    //////////////////////////////////////// Tests that will be deleted soon //////////////////////////////////////


    public void addAnimalsGen(List<T> animals) {
    }

    public void addAnimals(List<BaseEntity> animals) {
    }

    private void test() {
        BaseEntity[] arr = new BaseEntity[5];
        Note[] note = new Note[10];
        Note[] notes2 = (Note[]) arr;
        BaseEntity[] arr2 = (BaseEntity[]) note;
        BaseEntity e = new BaseEntity();
        Note n = new Note();
        BaseEntity e2 = n;
        Note n2 = (Note) e;
        /*List<BaseEntity> list1 = new ArrayList<>();
        List<T> list2 = new ArrayList<>();

        List<NoteDTO> list3 = new ArrayList<>();

        List<? extends  BaseEntity> list4 = new ArrayList<>();
        List<? super  BaseEntity> list5 = new ArrayList<>();
        list4.add(new NoteDTO());
        list5.add(new NoteDTO());
        list1.add(new NoteDTO());
        list2.add(new NoteDTO());
        NoteDTO n = (NoteDTO) list1.get(0);
        NoteDTO n2 = (NoteDTO) list2.get(0);

        NoteDTO n5 = (T) list5.get(0);
        addAnimalsGen(list1);
        addAnimalsGen(list2);
        addAnimalsGen(list3);

        addAnimals(list1);
        addAnimals(list2);
        addAnimals(list3);*/
    }

    public class RawItem{
        public int Id;
        public String Name;
        public int ParentId;

        public RawItem(int id, String name, int parentId) {
            Id = id;
            Name = name;
            ParentId = parentId;
        }
    }
    public interface WherePredicate<T>{
        boolean pass(T item);
    }
    public class Item{
        public int Id;
        public String Name;
        public List<Item> SubItems;

        public Item(int id, String name) {
            Id = id;
            Name = name;
        }
    }
    public void TestX(){
        List<RawItem> rawItems = new ArrayList<RawItem>(){{
            int i = 0;
            add(new RawItem(++i, "Animales", 0));
            add(new RawItem(++i, "Plantas", 0));
            add(new RawItem(++i, "Mamiferos", 1));
            add(new RawItem(++i, "Reptiles", 1));
            add(new RawItem(++i, "Perros", 3));
            add(new RawItem(++i, "Gatos", 3));
            add(new RawItem(++i, "Labrador", 5));
            add(new RawItem(++i, "Golder", 5));
            add(new RawItem(++i, "Pastor Aleman", 5));
            add(new RawItem(++i, "Siames", 6));
            add(new RawItem(++i, "Bills", 6));
            add(new RawItem(++i, "Lagartija", 4));
            add(new RawItem(++i, "Cocodrilo", 4));

            add(new RawItem(++i, "Planta 1", 2));
            add(new RawItem(++i, "Planta 2", 2));
        }};

        List<Item> filtered = findItems(rawItems, (rawItem) -> rawItem.ParentId == 0);
        String json = new Gson().toJson(filtered);
    }
    public List<Item> findItems(List<RawItem> rawItems, WherePredicate<RawItem> predicate){
        List<Item> coincidences = new ArrayList<>();
        List<RawItem> coincidencesToDelete = new ArrayList<>();
        for (RawItem item: rawItems){
            if (predicate.pass(item)){
                coincidences.add(new Item(item.Id, item.Name));
                coincidencesToDelete.add(item);
            }
        }
        rawItems.removeAll(coincidencesToDelete);
        for (Item coincidence: coincidences){
            coincidence.SubItems = findItems(rawItems, item -> item.ParentId == coincidence.Id);
        }
        return coincidences;
    }
}
