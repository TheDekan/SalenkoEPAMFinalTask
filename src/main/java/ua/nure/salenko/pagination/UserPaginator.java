package ua.nure.salenko.pagination;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ua.nure.salenko.model.User;

@XmlRootElement
public class UserPaginator extends Paginator {

    public UserPaginator(Integer currentPage, String sortFields, String sortDirections, Integer pageSize) {
        super(currentPage, sortFields, sortDirections, pageSize);
    }

    private static final long serialVersionUID = 1L;

    @XmlElement
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public List<User> getUserList() {
        return list;
    }

    public void setUserList(List<User> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User p : this.getUserList()) {
            sb.append(p.getName() + " ");
        }
        return sb.toString();
    }

}
