package ffWork;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {
    private final List<Resource> resources = new ArrayList<>();

    @Override
    public void add(Resource r) {
        resources.add(r);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        return resources.stream().filter(r -> r.getName().equals(name)).findFirst();
    }

    @Override
    public List<Resource> findAll() {
        return List.copyOf(resources);
    }
}
