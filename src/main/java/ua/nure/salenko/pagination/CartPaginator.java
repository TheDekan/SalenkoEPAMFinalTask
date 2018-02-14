package ua.nure.salenko.pagination;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ua.nure.salenko.model.Cart;

@XmlRootElement
public class CartPaginator extends Paginator {

    public CartPaginator(Integer currentPage, String sortFields, String sortDirections, Integer pageSize) {
        super(currentPage, sortFields, sortDirections, pageSize);
    }

    private static final long serialVersionUID = 1L;

    @XmlElement
    private List<Cart> list;

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public List<Cart> getCartList() {
        return list;
    }

    public void setProductList(List<Cart> list) {
        this.list = list;
    }

}
