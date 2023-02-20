package com.estore.api.estoreapi.persistence;

public class UserFileDAO implements UserDAO {

    public UserFileDAO(@Value("${user.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the beef from the file
    }



}
