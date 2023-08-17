package plurallid.helpers;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import plurallid.entity.actions.Action;
import plurallid.entity.actions.Changes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filters extends Paging {

    private Integer entityId;
    private Integer entityValue;
    private String applicationId;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(Integer entityValue) {
        this.entityValue = entityValue;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public List<Action> initFilters(
        Integer entityId,
        Integer entityValue,
        Integer first,
        Integer page,
        Integer skip,
        String sort,
        String firstPage,
        String previousPage,
        String nextPage,
        String lastPage,
        PanacheQuery<Action> query
    ) {
        if ((entityId != null ||
            entityValue != null) &&
            (first == null &&
            page == null &&
            skip == null &&
            sort == null &&
            firstPage == null &&
            previousPage == null &&
            nextPage == null &&
            lastPage == null)
        ) {
            return buildFilters(entityId, entityValue).list();
        }
        if ((entityId == null &&
            entityValue == null) &&
            (first != null ||
            page != null ||
            skip != null ||
            sort != null ||
            firstPage != null ||
            previousPage != null ||
            nextPage != null ||
            lastPage != null)
        ) {
            return buildPaging(query, first, page, skip, sort, firstPage, previousPage, nextPage, lastPage, null).stream().toList();
        }
        if (filters != null) {
            PanacheQuery<Action> filteredActions = this.buildFilters(entityId, entityValue);
            return buildPaging(null, first, page, skip, sort, firstPage, previousPage, nextPage, lastPage, filteredActions);
        }
        return null;
    }

    public PanacheQuery<Action> buildFilters(Integer entityId, Integer entityValue) {
        Paging page = new Paging();
        if (entityId != null && entityValue != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("entityId", entityId);
            params.put("entityValue", entityValue);
            return Action.find("{'entityId': :entityId, 'entityValue': :entityValue}", params).page(Page.ofSize(page.getPageSize()));
        }
        return null;
    }

    public List<Changes> initChangesFilters(
        String applicationId,
        Integer first,
        Integer skip,
        String sort,
        List<Changes> changesList
    ) {
        if ((applicationId != null) &&
            (first == null &&
            skip == null &&
            sort == null))
        {
            return buildChangesFilters(applicationId, changesList);
        }
        if ((applicationId == null) &&
            (first != null ||
            skip != null ||
            sort != null))
        {
            return buildChangesPaging(first, skip, sort, changesList);
        }
        if (applicationId != null) {
            return buildChangesPaging(first, skip, sort, buildChangesFilters(applicationId, changesList));
        }
        return null;
    }

    public List<Changes> buildChangesFilters(String applicationId, List<Changes> changesList) {
        if (applicationId != null) {
            Predicate<Changes> selectedChange = changes -> Objects.equals(changes.getApplicationId(), applicationId);
            return changesList.stream().filter(selectedChange).collect(Collectors.toList());
        }
        return null;
    }
}
