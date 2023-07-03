package com.usuario.service;

import com.usuario.entities.CuentaDTO;
import com.usuario.entities.Usuario;
import com.usuario.feingclients.CarroFeingClient;
import com.usuario.feingclients.MotoFeignClient;
import com.usuario.models.Carro;
import com.usuario.models.Moto;
import com.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //inyecto el feingClient
    @Autowired(required=true)
    private CarroFeingClient carroFeingClient;

    @Autowired(required=true)
    private MotoFeignClient motoFeignClient;
    //fin inyeccion feign

    public List<Carro> getCarros(int usuarioId){
        List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" +usuarioId,
                List.class);

        return carros;
    }

    public List<Moto> getMotos(int usuarioId){
        List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" +usuarioId,
                List.class);

        return motos;
    }

    //metodos con feign
    public Carro saveCarro(int usuarioId, Carro carro){
        carro.setUsuarioId(usuarioId);
        Carro nuevoCarro = carroFeingClient.save(carro);
        return nuevoCarro;
    }

    public Moto saveMoto(int usuarioId, Moto moto){
        moto.setUsuarioId(usuarioId);
        Moto nuevaMoto = motoFeignClient.save(moto);
        return nuevaMoto;
    }

    public Map<String,Object> getUsuarioAndVehiculos(int usuarioId){

        Map<String,Object> resultado = new HashMap<>();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if(usuario == null){
            resultado.put("Mensaje", "el usuario no existe");
            return resultado;
        }

        resultado.put("usuario", usuario);

        List<Carro> carros = carroFeingClient.getCarros(usuarioId);
        if (carros.isEmpty()){
            resultado.put("Carros", "El usuario no tiene carros");
        }else {
            resultado.put("Carros", carros);
        }

        List<Moto> motos = motoFeignClient.getMotos(usuarioId);
        if(motos.isEmpty()){
            resultado.put("Motos", "El usuario no tiene motos");
        }else{
            resultado.put("Motos", motos);
        }
        return resultado;
    }
    //fin metodos feign

    public List<Usuario> getAll(){
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(int id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario){
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return nuevoUsuario;
    }

    //mi metodo
    public int cuenta(CuentaDTO cuentaDTO){
        int num1 = cuentaDTO.getN1();
        int num2 = cuentaDTO.getN2();

        int resultado = num1 + num2;
        return resultado;
    }
}
