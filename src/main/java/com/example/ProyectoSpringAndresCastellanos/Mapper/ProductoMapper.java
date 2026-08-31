package com.example.ProyectoSpringAndresCastellanos.Mapper;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.ProductoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request){
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setCategoria((request.categoria()));
        producto.setStock(request.stock());
        producto.setPrecio(request.precio());
        return producto;
    }

    public ProductoResponse toResponse(Producto producto){
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getStock(),
                producto.getPrecio()
        );
    }

}
