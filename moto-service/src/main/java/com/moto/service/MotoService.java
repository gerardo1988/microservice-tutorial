package com.moto.service;

import com.moto.entities.Moto;
import com.moto.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MotoService {


    @Autowired()
    private MotoRepository  entityManagerFactory;


    public List<Moto> getAll(){
        return entityManagerFactory.findAll();
    }

    public Moto getMotoById(long id){
        return entityManagerFactory.findById(id).orElse(null);
    }

    public Moto save(Moto moto){
        Moto nuevaMoto = entityManagerFactory.save(moto);
        return nuevaMoto;
    }

    public List<Moto> byUsuarioId(long usuarioId){
        return entityManagerFactory.findByUsuarioId(usuarioId);
    }
}
