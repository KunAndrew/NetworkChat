package server.auth;

import org.jetbrains.annotations.Nullable;

public interface AuthService {

    void start();
    void stop();

    /**
     *
     * @param login
     * @param pass
     * @return nick or null
     */
    @Nullable
    String getNickByLoginPass(String login, String pass);

}
