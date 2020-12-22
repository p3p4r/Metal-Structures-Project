package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllSuppliers",
                query = "SELECT s FROM Supplier s ORDER BY s.username" // JPQL
        )
})
public class Supplier extends User {

    @ManyToMany(mappedBy = "suppliers")
    List<Variante> variantes;


    public Supplier() {
        super();
        this.variantes = new LinkedList<>();
    }

    public Supplier(String username, @NotNull String password, @NotNull String name, @NotNull String address, @NotNull String contactPerson, @Email String email) {
        super(username, password, name, address, contactPerson, email);
        this.variantes = new LinkedList<>();
    }

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> variantes) {
        this.variantes = variantes;
    }

    public void addVariante(Variante v) {
        this.variantes.add(v);
    }

    public void removeVariante(Variante v) {
        this.variantes.remove(v);
    }
}
