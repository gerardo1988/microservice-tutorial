package com.usuario.controller;

import com.usuario.entities.CuentaDTO;
import com.usuario.entities.Usuario;
import com.usuario.models.Carro;
import com.usuario.models.Moto;
import com.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/carros/{usuarioId}")
    public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);

        if(usuario == null){
            return ResponseEntity.noContent().build();
        }

        List<Carro> carros = usuarioService.getCarros(id);
        return ResponseEntity.ok(carros);
    }

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
    @PostMapping("/carro/{usuarioId}")
    public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId,
                                              @RequestBody Carro carro){

        Carro nuevoCarro = usuarioService.saveCarro(usuarioId,carro);
        return ResponseEntity.ok(nuevoCarro);

    }
    @PostMapping("/moto/{usuarioId}")
    public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId,
                                              @RequestBody Moto moto){

        Moto nuevaMoto = usuarioService.saveMoto(usuarioId,moto);
        return ResponseEntity.ok(nuevaMoto);

    }

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

}
