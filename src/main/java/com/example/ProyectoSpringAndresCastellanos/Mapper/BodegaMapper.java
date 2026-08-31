package com.example.ProyectoSpringAndresCastellanos.Mapper;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    // Convierte un Request en una entidad Bodega.
    public Bodega toEntity(BodegaRequest request) {

        Bodega bodega = new Bodega();

        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(request.encargado());

        return bodega;
    }

    // Convierte una entidad Bodega en un Response.
    public BodegaResponse toResponse(Bodega bodega) {

        return new BodegaResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCapacidad(),
                bodega.getEncargado()
        );
    }
}