package ua.nure.salenko.pagination;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Paginator implements Serializable {
    
    public Paginator(Integer currentPage, String sortFields, String sortDirections, Integer pageSize) {
        this.currentPage = currentPage;
        this.sortFields = sortFields;
        this.sortDirections = sortDirections;
        this.pageSize = pageSize;
    }

    private static final long serialVersionUID = 1L;
    private Integer currentPage;
    private Integer pageSize;
    private Long totalResults;

    private String sortFields;
    private String sortDirections;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public String getSortDirections() {
        return sortDirections;
    }

    public void setSortDirections(String sortDirections) {
        this.sortDirections = sortDirections;
    }

}
