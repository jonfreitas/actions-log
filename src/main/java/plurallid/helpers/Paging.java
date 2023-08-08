package plurallid.helpers;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import plurallid.entity.actions.Action;
import plurallid.entity.actions.Changes;

import java.util.*;
import java.util.stream.Collectors;

public class Paging {

    Integer first;
    Integer pageSize = 20;
    Integer page;
    Integer skip;
    String sort;
    String firstPage;
    String previousPage;
    String nextPage;
    String lastPage;
    List<Paging> filters = new ArrayList<>();

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public List<Paging> getFilters() {
        return filters;
    }

    public void setFilters(List<Paging> filters) {
        this.filters = filters;
    }

    public List<Action> buildPaging(
        PanacheQuery<Action> query,
        Integer first,
        Integer page,
        Integer skip,
        String sort,
        String firstPage,
        String previousPage,
        String nextPage,
        String lastPage,
        PanacheQuery<Action> filteredactions
    ) {
        if (query != null) {
            return this.filterPaging(first, page, skip, sort, firstPage, previousPage, nextPage, lastPage, query);
        }
        if (filteredactions != null) {
            return this.filterPaging(first, page, skip, sort, firstPage, previousPage, nextPage, lastPage, filteredactions);
        }
        return null;
    }

    public List<Action> filterPaging(
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
        if (first != null) {
            return query.stream().limit(first).toList();
        }
        if (page != null) {
            return query.page(Page.of(page, getPageSize())).stream().toList();
        }
        if (skip != null) {
            return query.stream().skip(skip).toList();
        }
        if (Objects.equals(sort, "ASC")) {
            return query.stream().sorted(Comparator.comparing(Action::getEntityValue)).toList();
        }
        if (Objects.equals(firstPage, "true")) {
            return query.firstPage().list();
        }
        if (Objects.equals(previousPage, "true")) {
            return query.previousPage().list();
        }
        if (Objects.equals(nextPage, "true")) {
            return query.nextPage().list();
        }
        if (Objects.equals(lastPage, "true")) {
            return query.lastPage().list();
        }
        return null;
    }

    public List<Changes> buildChangesPaging(
        Integer first,
        Integer skip,
        String sort,
        List<Changes> changesList
    ) {
        if (changesList != null) {
            return this.filterChangesPaging(first, skip, sort, changesList);
        }
        return null;
    }

    public List<Changes> filterChangesPaging(
        Integer first,
        Integer skip,
        String sort,
        List<Changes> changesList)
    {
        if (first != null && sort != null && Objects.equals(sort, "ASC") && changesList != null) {
            return changesList.stream()
                    .sorted(Comparator.comparing(Changes::getWhen))
                    .limit(first)
                    .collect(Collectors.toList());
        }
        if (first != null && changesList != null) {
            return changesList.stream().sorted(Comparator.comparing(Changes::getWhen).reversed()).limit(first).toList();
        }
        if (skip != null && sort != null && Objects.equals(sort, "ASC") && changesList != null) {
            return changesList.stream()
                    .sorted(Comparator.comparing(Changes::getWhen))
                    .skip(skip)
                    .collect(Collectors.toList());
        }
        if (skip != null && changesList != null) {
            return changesList.stream().sorted(Comparator.comparing(Changes::getWhen).reversed()).skip(skip).toList();
        }
        if (sort != null && Objects.equals(sort, "ASC") && changesList != null) {
            return changesList.stream().sorted(Comparator.comparing(Changes::getWhen)).collect(Collectors.toList());
        }
        return null;
    }

}
