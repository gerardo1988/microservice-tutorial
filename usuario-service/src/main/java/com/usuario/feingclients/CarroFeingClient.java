package com.usuario.feingclients;

import com.usuario.models.Carro;
import com.usuario.models.Moto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "carro-service", url = "http://localhost:8002", path = "/carro")
public interface CarroFeingClient {

    @PostMapping()
    public Carro save(@RequestBody Carro carro);

    @GetMapping("/usuario/{usuarioId}")
    public List<Carro> getCarros(@PathVariable("usuarioId") int usuarioId);
}
