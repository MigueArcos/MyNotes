package com.zeus.migue.notes.infrastructure.utils;

import com.zeus.migue.notes.data.DTO.sync.EntityChanges;
import com.zeus.migue.notes.data.room.composite_entities.EntityIDs;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.dao.BaseDao;
import com.zeus.migue.notes.infrastructure.dao.ISyncDao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityChangesProcessor<Entity extends BaseEntity, DTO extends IEntityConverter<Entity>, Dao extends BaseDao<Entity> & ISyncDao> {
    private Dao dao;

    public EntityChangesProcessor(Dao dao) {
        this.dao = dao;
    }

    private Function<DTO, Entity> getMapper() {
        return dto -> {
            Entity entity = dto.toEntity();
            entity.setIsUploaded(true);
            return entity;
        };
    }

    @SuppressWarnings("ConstantConditions")
    private Function<DTO, Entity> getMapperModifications(Map<String, Long> idsMap) {
        return dto -> {
            Entity entity = dto.toEntity();
            entity.setIsUploaded(true);
            if (idsMap.containsKey(entity.getRemoteId())){
                entity.setId(idsMap.get(entity.getRemoteId()));
            }
            return entity;
        };
    }

    public void processChanges(EntityChanges<DTO> entityChanges, Function<Stream<Entity>, Entity[]> streamToArray){
        if (entityChanges != null){
            List<DTO> toAdd = entityChanges.getToAdd();
            List<DTO> toModify = entityChanges.getToModify();
            if (!Utils.listIsNullOrEmpty(toAdd)){
                Entity[] newEntities = streamToArray.apply(toAdd.stream().map(getMapper()));
                dao.deleteUploaded(false);
                dao.insert(newEntities);
            }
            if (!Utils.listIsNullOrEmpty(toModify)){
                Entity[] modifiedEntities = streamToArray.apply(toModify.stream().map(getMapperModifications(idsListToMap(dao.getIDs()))));
                dao.update(modifiedEntities);
            }
        }
    }
    /* Replaced with Function<Stream<Entity>, Entity[]>
    public interface StreamToArray<Entity extends BaseEntity>{
        Entity[] streamToArray(Stream<Entity> stream);
    }
    */
    private Map<String, Long> idsListToMap(List<EntityIDs> entityIDs){
        return entityIDs.stream().collect(Collectors.toMap(EntityIDs::getRemoteId, EntityIDs::getId));
    }
}
