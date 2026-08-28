package com.example.ProyectoSpringAndresCastellanos.Mapper;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public Bodega toEntity(BodegaRequest request){
        Bodega bodega = new Bodega();
        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(request.encargado());
        return bodega;
    }

    public BodegaResponse toResponse(Bodega bodega){
        return new BodegaResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCapacidad(),
                bodega.getEncargado()

        );
    }
}

