package services.interfaces;

import model.interfaces.SearchModel;

import java.util.List;

/* Every service needs to implement this interface and its function */
public interface SearchService {
    List<Object> search(SearchModel query);
}