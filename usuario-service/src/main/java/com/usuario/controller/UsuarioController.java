package com.usuario.controller;

import com.usuario.entities.CuentaDTO;
import com.usuario.entities.Usuario;
import com.usuario.models.Carro;
import com.usuario.models.Moto;
import com.usuario.service.UsuarioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.getAll();

        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    //metodos con Rest Template
    @CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackGetCarros")
    @GetMapping("/carros/{usuarioId}")
    public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);

        if(usuario == null){
            return ResponseEntity.noContent().build();
        }

        List<Carro> carros = usuarioService.getCarros(id);
        return ResponseEntity.ok(carros);
    }

    @CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackGetMotos")
    @GetMapping("/moto/{usuarioId}")
    public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);

        if(usuario == null){
            return ResponseEntity.noContent().build();
        }

        List<Moto> motos = usuarioService.getMotos(id);
        return ResponseEntity.ok(motos);
    }

    //metodos con Feing
    @CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackSaveCarros")
    @PostMapping("/carro/{usuarioId}")
    public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId,
                                              @RequestBody Carro carro){

        Carro nuevoCarro = usuarioService.saveCarro(usuarioId,carro);
        return ResponseEntity.ok(nuevoCarro);

    }

    @CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackSaveMotos")
    @PostMapping("/moto/{usuarioId}")
    public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId,
                                              @RequestBody Moto moto){

        Moto nuevaMoto = usuarioService.saveMoto(usuarioId,moto);
        return ResponseEntity.ok(nuevaMoto);

    }

    @CircuitBreaker(name = "todosCB", fallbackMethod = "fallBackGetTodos")
    @GetMapping("/todos/{usuarioId}")
    public ResponseEntity<Map<String,Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId){
        Map<String, Object> resultado = usuarioService.getUsuarioAndVehiculos(usuarioId);
        return ResponseEntity.ok(resultado);

    }
    //fin metodos feign
    @PostMapping("/cuentita")
    public ResponseEntity<Integer> resultado(@RequestBody CuentaDTO cuentaDTO) {
        int resultado = usuarioService.cuenta(cuentaDTO);
        return ResponseEntity.ok(resultado);
    }

    //metodos para circuit breaker
    private ResponseEntity<List<Carro>> fallBackGetCarros(@PathVariable("usuarioId") int usuarioId,
                                                          RuntimeException exception){

        return new ResponseEntity("El usuario: " + usuarioId + " no tiene diponible los carros" , HttpStatus.OK);
    }

    private ResponseEntity<Carro> fallBackSaveCarros(@PathVariable("usuarioId") int usuarioId,
                                                           @RequestBody Carro carro,
                                                           RuntimeException exception){

        return new ResponseEntity("El usuario: " + usuarioId + " no tiene dinero para los carros" , HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int usuarioId,
                                                          RuntimeException exception){

        return new ResponseEntity("El usuario: " + usuarioId + " no tiene diponible los motos" , HttpStatus.OK);
    }

    private ResponseEntity<Carro> fallBackSaveMotos(@PathVariable("usuarioId") int usuarioId,
                                                           @RequestBody Moto moto,
                                                           RuntimeException exception){

        return new ResponseEntity("El usuario: " + usuarioId + " no tiene dinero para los motos" , HttpStatus.OK);
    }

    private ResponseEntity<Map<String,Object>> fallBackGetTodos(@PathVariable("usuarioId") int usuarioId,
                                                                RuntimeException exception){

        return new ResponseEntity("El usuario: " + usuarioId + " no tiene dinero para los vehiculos" , HttpStatus.OK);
    }

}
