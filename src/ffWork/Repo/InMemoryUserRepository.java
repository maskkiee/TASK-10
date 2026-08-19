package ffWork.Repo;

import ffWork.Domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public void add(User u) {
        users.add(u);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users);
    }
}
