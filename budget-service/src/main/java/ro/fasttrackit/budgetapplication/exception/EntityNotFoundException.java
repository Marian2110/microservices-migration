package ro.fasttrackit.budgetapplication.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
public class EntityNotFoundException extends RuntimeException {
    private final Long id;
    private final String entityName;

    public EntityNotFoundException(Long id, String entityName) {
        super(entityName + " with id " + id + " not found");
        this.entityName = entityName;
        this.id = id;
    }

    public static <T> EntityNotFoundException forEntity(Class<T> entity, Long id) {
        String entityName = entity.getSimpleName();
        return createException(id, "Could not find " + entityName + " with id " + id, entityName);
    }

    public Long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    private static EntityNotFoundException createException(Long id, String errorMessage, String entityName) {
        EntityNotFoundException entityNotFoundException = EntityNotFoundException.builder().entityName(entityName).id(id).build();
        log.error(errorMessage, id, entityNotFoundException);
        return entityNotFoundException;
    }
}
