package ffWork.Repo;

import ffWork.Domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void add(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
