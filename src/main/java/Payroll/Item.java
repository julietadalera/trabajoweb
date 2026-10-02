package Payroll;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Item {

    private @Id
    @GeneratedValue Long id;

    private String nome;
    private String tipo;
    private String foto;

    Item() {}

    Item(String nome, String tipo, String foto) {

        this.nome = nome;
        this.tipo = tipo;
        this.foto = foto;
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Item))
            return false;

        Item item = (Item) o;

        return Objects.equals(this.id, item.id)
            && Objects.equals(this.nome, item.nome)
            && Objects.equals(this.tipo, item.tipo)
            && Objects.equals(this.foto, item.foto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.nome, this.tipo, this.foto);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + this.id +
                ", nome='" + this.nome + '\'' +
                ", tipo='" + this.tipo + '\'' +
                ", foto='" + this.foto + '\'' +
                '}';
    }
}