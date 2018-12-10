package services;

import model.Musica;
import model.interfaces.SearchModel;
import services.interfaces.SearchService;

import java.util.ArrayList;
import java.util.List;

/* Search service that can be used to find car products according to user-defined conditions */
public class MusicService implements SearchService {

    /* List of existing car products. Emulates a database in this example */
    private List<Musica> cars = new ArrayList<>();

    /* The constructor fills the database with dummy data. */
    public MusicService()
    {

    }

    @Override
    public List<Object> search(SearchModel query) {
        /*if (query instanceof Musica)
        {
            Musica queryCar = (Musica) query;

            List<Object> results = new ArrayList<Object>();

            for (Musica car: cars)
            {
                boolean condition1 = Compare.strings(queryCar.getCarModel(), car.getCarModel());
                boolean condition2 = Compare.strings(queryCar.getManufacturer(), car.getManufacturer());
                boolean condition3 = Compare.integers(queryCar.getYear(), car.getYear());
                boolean condition4 = Compare.integers(queryCar.getPrice(), car.getPrice());

                // Assume AND condition
                if (condition1 & condition2 & condition3 & condition4)
                {
                    results.add(car);
                }
            }

            return results;

        }*/
        return null;
    }

}
