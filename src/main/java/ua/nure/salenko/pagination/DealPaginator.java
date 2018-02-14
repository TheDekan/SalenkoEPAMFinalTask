package ua.nure.salenko.pagination;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ua.nure.salenko.model.Deal;

@XmlRootElement
public class DealPaginator extends Paginator {

    public DealPaginator(Integer currentPage, String sortFields, String sortDirections, Integer pageSize) {
        super(currentPage, sortFields, sortDirections, pageSize);
    }

    private static final long serialVersionUID = 1L;

    @XmlElement
    private List<Deal> list;

    public List<Deal> getList() {
        return list;
    }

    public void setList(List<Deal> list) {
        this.list = list;
    }

    public List<Deal> getDealList() {
        return list;
    }

    public void setProductList(List<Deal> list) {
        this.list = list;
    }

}
