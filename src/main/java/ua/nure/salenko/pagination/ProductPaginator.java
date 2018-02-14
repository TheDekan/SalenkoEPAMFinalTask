package ua.nure.salenko.pagination;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ua.nure.salenko.model.Product;

@XmlRootElement
public class ProductPaginator extends Paginator {

    public ProductPaginator(Integer currentPage, String sortFields, String sortDirections, Integer pageSize) {
        super(currentPage, sortFields, sortDirections, pageSize);
    }

    private static final long serialVersionUID = 1L;

    @XmlElement
    private List<Product> list;

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public List<Product> getProductList() {
        return list;
    }

    public void setProductList(List<Product> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Product p : this.getProductList()) {
            sb.append(p.getModel() + " ");
        }
        return sb.toString();
    }

}
