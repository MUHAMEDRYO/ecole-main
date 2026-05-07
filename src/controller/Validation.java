package controller;

//sa3at input mta3 data ykoun 8alet 5astan el id
//id fi db yabda ml 1
//ken user da5el info 8altin el controller y7abes el 5edma w maynadich el dao
//safer lel code
//ken mana3mlouhech momkn tod5el ma3loumet na9sa wla 8alta lel dataset

public interface Validation<T> {
    void validateId(int id);
    void validate(T entity);
}
