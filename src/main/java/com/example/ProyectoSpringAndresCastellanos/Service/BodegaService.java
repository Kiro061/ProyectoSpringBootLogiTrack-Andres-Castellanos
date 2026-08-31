package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.BodegaMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import com.example.ProyectoSpringAndresCastellanos.Repository.BodegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PrimitiveIterator;

@Service
@RequiredArgsConstructor
public class BodegaService {

    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;

    public BodegaResponse crear(BodegaRequest request){
        Bodega bodega = bodegaMapper.toEntity(request);
        return bodegaMapper.toResponse(bodegaRepository.save(bodega));
    }
     public List<BodegaResponse> obtenerTodas(){
        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::toResponse)
                .toList();
     }

     public BodegaResponse actualizar(Long id, BodegaRequest request){
        Bodega bodega = buscarPorId(id);
        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(request.encargado());
        return bodegaMapper.toResponse(bodegaRepository.save(bodega));
     }

     public void eliminar(Long id){
        Bodega bodega = buscarPorId(id);
        bodegaRepository.delete(bodega);
     }

     private Bodega buscarPorId(Long id){
        return bodegaRepository.findById(id)
                .orElseThrow(()-> new BusinessRuleException("Bodega no encontrada con id: "+ id));
     }
}
