package dtos;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TypeDTO implements Serializable {
    String name, description;

    public TypeDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
