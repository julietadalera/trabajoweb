package Payroll;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ItemController {

    private final ItemRepository repository;

    ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    // Listar todos os itens (200 OK)

    @GetMapping("/items")
    List<Item> all() {
        return repository.findAll();
    }

    // Cadastrar novo item (201 Created)

    @PostMapping("/items")
    ResponseEntity<Item> newItem(@RequestBody Item newItem) {

        Item saved = repository.save(newItem);

        return ResponseEntity
            .created(URI.create("/items/" + saved.getId()))
            .body(saved);
    }

    // Buscar um item pelo ID (200 OK ou 404 Not Found)

    @GetMapping("/items/{id}")
    Item one(@PathVariable Long id) {

        return repository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException(id));
    }

    // Atualizar um item (200 OK)

    @PutMapping("/items/{id}")
    Item replaceItem(@RequestBody Item newItem, @PathVariable Long id) {

        return repository.findById(id)
            .map(item -> {

                item.setNome(newItem.getNome());
                item.setTipo(newItem.getTipo());

                // So troca a foto se vier uma no corpo da requisicao
                if (newItem.getFoto() != null) {
                    item.setFoto(newItem.getFoto());
                }

                return repository.save(item);

            })
            .orElseGet(() -> {
                return repository.save(newItem);
            });
    }

    // Excluir um item (204 No Content)

    @DeleteMapping("/items/{id}")
    ResponseEntity<Void> deleteItem(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}